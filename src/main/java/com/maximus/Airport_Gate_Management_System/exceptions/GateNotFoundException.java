package com.maximus.Airport_Gate_Management_System.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class GateNotFoundException extends EntityNotFoundException {

  public GateNotFoundException(String message) {
    super(message);
  }
}
