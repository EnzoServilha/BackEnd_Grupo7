package sptech.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException(String mensagem) {
        super(mensagem);
    }
}

