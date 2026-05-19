package sptech.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeConflitanteException extends RuntimeException {
    public EntidadeConflitanteException(String mensagem) {
        super(mensagem);
    }
}

