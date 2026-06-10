package fr.apgis4.tp508;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Object> handleUnsupportedMedia(HttpMediaTypeNotSupportedException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("message", "Format de requête non supporté. Utilisez application/json ou application/xml");
    error.put("status", 415);
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<Object> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("message", "Format de réponse non disponible. Utilisez application/json ou application/xml");
    error.put("status", 406);
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("message", "Une erreur interne est survenue : " + ex.getMessage());
    error.put("status", 500);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("message", "Il y a des erreurs dans les données envoyées : " + ex.getMessage());
    error.put("status", 400);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleJsonParse(HttpMessageNotReadableException ex) {
    Map<String, Object> error = new HashMap<>();
    error.put("message", "Le JSON envoyé est invalide ou malformé : " + ex.getMessage());
    error.put("status", 400);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    Map<String, Object> error = new HashMap<>();
    List<String> details = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
        .collect(Collectors.toList());
    error.put("message", "Il y a des erreurs dans les données envoyées");
    error.put("details", details);
    error.put("status", 400);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
