package mflix.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, List<String>> handle(MethodArgumentNotValidException exception) {
    return Collections.singletonMap(
        "error",
        exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList()));
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, List<Map<String, Object>>> handle(ConstraintViolationException exception) {
    return Collections.singletonMap(
        "error",
        exception
            .getConstraintViolations()
            .stream()
            .map(
                x -> {
                  HashMap<String, Object> error = new HashMap<>();
                  error.put("field", x.getPropertyPath().toString());
                  error.put("error", x.getMessage());
                  return error;
                })
            .collect(Collectors.toList()));
  }
}
