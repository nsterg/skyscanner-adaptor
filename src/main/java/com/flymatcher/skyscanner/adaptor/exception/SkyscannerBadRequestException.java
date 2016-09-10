package com.flymatcher.skyscanner.adaptor.exception;

public class SkyscannerBadRequestException extends RuntimeException {

  private static final long serialVersionUID = -2193279443658669664L;

  public SkyscannerBadRequestException(final String message) {
    super(message);
  }

}
