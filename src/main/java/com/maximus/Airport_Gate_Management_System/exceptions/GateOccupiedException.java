package com.maximus.Airport_Gate_Management_System.exceptions;

public class GateOccupiedException extends RuntimeException {

  public GateOccupiedException(String message) {
    super(message);
  }
}