package com.flymatcher.skyscanner.adaptor.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.flymatcher.error.ErrorType;
import com.flymatcher.error.FlymatcherError;
import com.flymatcher.error.FlymatcherProviderError;
import com.flymatcher.skyscanner.cheapestquotes.ValidationErrorDto;

@ControllerAdvice
public class SkyscannerAdaptorExceptionHandler {

  @ExceptionHandler(value = {SkyscannerServerException.class})
  public ResponseEntity<FlymatcherError> handleException(
      final SkyscannerServerException exception) {

    return buildErrorResponse(INTERNAL_SERVER_ERROR, ErrorType.INTERNAL_SERVER_ERROR,
        exception.getMessage());
  }

  @ExceptionHandler(value = {SkyscannerBadRequestException.class})
  public ResponseEntity<FlymatcherError> handleException(
      final SkyscannerBadRequestException exception) {

    return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorType.BAD_REQUEST, exception.getMessage(),
        exception.getValidationErrors());
  }

  private ResponseEntity<FlymatcherError> buildErrorResponse(final HttpStatus status,
      final ErrorType errorType, final String message) {

    return new ResponseEntity<>(buildFlymatcherError(message, errorType), status);
  }

  private ResponseEntity<FlymatcherError> buildErrorResponse(final HttpStatus status,
      final ErrorType errorType, final String message,
      final List<ValidationErrorDto> validationErrors) {

    final FlymatcherError error = buildFlymatcherError(message, errorType);

    final List<FlymatcherProviderError> providerErrors = new ArrayList<>();
    validationErrors.forEach(e -> {
      final FlymatcherProviderError providerError = new FlymatcherProviderError();
      providerError.setMessage(e.getMessage());
      providerError.setParameterName(e.getParameterName());
      providerError.setParameterValue(e.getParameterValue());
      providerErrors.add(providerError);
    });
    error.setProviderErrors(providerErrors);

    return new ResponseEntity<>(error, status);
  }

  private FlymatcherError buildFlymatcherError(final String message, final ErrorType errorType) {
    final FlymatcherError flymatcherError = new FlymatcherError();

    flymatcherError.setErrorDescription(message);
    flymatcherError.setErrorType(errorType);

    return flymatcherError;
  }

}
