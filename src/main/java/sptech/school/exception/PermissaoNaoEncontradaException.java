package sptech.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PermissaoNaoEncontradaException extends RuntimeException {
    public PermissaoNaoEncontradaException(String message) {
        super(message);
    }
}
