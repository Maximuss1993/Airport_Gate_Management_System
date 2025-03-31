package com.maximus.Airport_Gate_Management_System.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximus.Airport_Gate_Management_System.exceptions.UserNotFoundException;
import com.maximus.Airport_Gate_Management_System.security.jwt.JwtService;
import com.maximus.Airport_Gate_Management_System.security.tokens.Token;
import com.maximus.Airport_Gate_Management_System.security.tokens.TokenRepository;
import com.maximus.Airport_Gate_Management_System.security.tokens.TokenType;
import com.maximus.Airport_Gate_Management_System.security.users.User;
import com.maximus.Airport_Gate_Management_System.security.users.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = getUser(request.getEmail());
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer "))
      return;
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = getUser(userEmail);
      revokeUserTokenIfIsValid(response, refreshToken, user);
    }
  }

  private User getUser(String userEmail) {
    return this.repository.findByEmail(userEmail)
        .orElseThrow(() ->
            new UserNotFoundException("User not found, email: " + userEmail));
  }

  private void revokeUserTokenIfIsValid(
      HttpServletResponse response,
      String refreshToken,
      User user)
      throws IOException {
    if (jwtService.isTokenValid(refreshToken, user)) {
      var accessToken = jwtService.generateToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, accessToken);
      var authResponse = AuthenticationResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();
      new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }
  }
}
