package br.com.emersondias.ebd.exceptions.handler;

import br.com.emersondias.ebd.dtos.StandardErrorDTO;
import br.com.emersondias.ebd.exceptions.ResourceNotFoundException;
import br.com.emersondias.ebd.utils.LogHelper;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Objects;

@ControllerAdvice
@ApiResponses(value = {
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "403"),
        @ApiResponse(responseCode = "404")
})
public class ControllerExceptionHandler {

    private static final LogHelper LOG = LogHelper.getInstance();

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> resourceNotFoundHandler(ResourceNotFoundException e, HttpServletRequest request) {
        var status = HttpStatus.NOT_FOUND;
        var error = StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Recurso não encontrado")
                .message("Não foi encontrado o recurso solicitado [" + e.getClassType().getSimpleName() + ":" + e.getTerm() + "]")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> validationError(MethodArgumentNotValidException e,
                                                            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        var error = StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Entidade não processável")
                .message("Erro de validação de dados")
                .path(request.getRequestURI())
                .build();
        e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .forEach(error::addAdditionalData);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorDTO> genericHandler(Exception e, HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var error = StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error("Erro inesperado")
                .message("Ocorreu um erro inesperado, por favor tente novamente mais tarde ou comunique o administrador do sistema")
                .path(request.getRequestURI())
                .build();
        LOG.stackTrace("An unexpected error has occurred", e);
        return ResponseEntity.status(status).body(error);
    }

}
