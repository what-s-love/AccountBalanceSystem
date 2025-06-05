package ge.accbalsystem.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Handled IllegalArgumentException: {}", ex.getMessage());
        return Mono.just(
                ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()))
        );
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleValidation(ServerWebInputException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return Mono.just(
                ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + ex.getReason()))
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleNoResource(NoResourceFoundException ex) {
        log.warn("Wrong path: {}", ex.getMessage());
        return Mono.just(
                ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + ex.getReason()))
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Internal server error"))
        );
    }
}
