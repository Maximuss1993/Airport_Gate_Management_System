package com.maximus.Airport_Gate_Management_System.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class FlightNotFoundException extends EntityNotFoundException {

  public FlightNotFoundException(String message) {
    super(message);
  }
}
