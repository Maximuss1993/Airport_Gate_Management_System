package com.maximus.Airport_Gate_Management_System.security.users;

import com.maximus.Airport_Gate_Management_System.security.authentication.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;

  private final UserRepository repository;

  public void changePassword(
      ChangePasswordRequest request,
      Principal connectedUser) {

    var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser)
        .getPrincipal();

    if (!isNewPasswordMatchesWithOld(request, user)) {
      throw new IllegalStateException("Wrong password.");
    }

    if (!isNewPasswordConfirmed(request)) {
      throw new IllegalStateException("Password are not the same.");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

    repository.save(user);
  }

  private static boolean isNewPasswordConfirmed(
      ChangePasswordRequest request) {
    return request.getNewPassword()
        .equals(request.getConfirmationPassword());
  }

  private boolean isNewPasswordMatchesWithOld(
      ChangePasswordRequest request,
      User user) {
    return passwordEncoder.matches(
        request.getCurrentPassword(),
        user.getPassword());
  }
}