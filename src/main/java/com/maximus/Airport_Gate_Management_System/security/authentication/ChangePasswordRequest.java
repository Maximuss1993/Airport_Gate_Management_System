package com.maximus.Airport_Gate_Management_System.security.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

  private String currentPassword;
  private String newPassword;
  private String confirmationPassword;

}