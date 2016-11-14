package com.flymatcher.skyscanner.adaptor.exception;

import java.util.List;

import com.flymatcher.skyscanner.cheapestquotes.ValidationErrorDto;

public class SkyscannerBadRequestException extends RuntimeException {

  private static final long serialVersionUID = -2193279443658669664L;

  private final List<ValidationErrorDto> validationErrors;

  public SkyscannerBadRequestException(final String message,
      final List<ValidationErrorDto> validationErrors) {
    super(message);
    this.validationErrors = validationErrors;
  }

  public List<ValidationErrorDto> getValidationErrors() {
    return validationErrors;
  }


}
