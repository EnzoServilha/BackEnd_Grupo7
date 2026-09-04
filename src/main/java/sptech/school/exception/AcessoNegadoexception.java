package sptech.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AcessoNegadoexception extends RuntimeException {
    public AcessoNegadoexception(String message) {
        super(message);
    }
}
