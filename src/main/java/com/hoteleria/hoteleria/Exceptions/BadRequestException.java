package com.hoteleria.hoteleria.Exceptions;

public class BadRequestException extends RuntimeException {

  public BadRequestException() {
    super();
  }

  public BadRequestException(String message) {
    super(message);
  }
}
