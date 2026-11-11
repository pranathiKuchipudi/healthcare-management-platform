package com.portfolio.healthcare.shared;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<ApiError> notFound(ResourceNotFoundException e){return build(HttpStatus.NOT_FOUND,e.getMessage(),Map.of());}
 @ExceptionHandler(DuplicateResourceException.class) public ResponseEntity<ApiError> conflict(DuplicateResourceException e){return build(HttpStatus.CONFLICT,e.getMessage(),Map.of());}
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ApiError> validation(MethodArgumentNotValidException e){
  Map<String,String> errors=e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(x->x.getField(),x->x.getDefaultMessage()==null?"Invalid value":x.getDefaultMessage(),(a,b)->a));
  return build(HttpStatus.BAD_REQUEST,"Request validation failed",errors);
 }
 @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<ApiError> integrity(DataIntegrityViolationException e){return build(HttpStatus.CONFLICT,"Request conflicts with existing data",Map.of());}
 private ResponseEntity<ApiError> build(HttpStatus s,String m,Map<String,String> errors){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),s.getReasonPhrase(),m,errors));}
}
