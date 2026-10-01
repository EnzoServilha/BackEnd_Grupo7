package sptech.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import sptech.school.entity.EntidadeAtivavel;

import java.util.List;

final class FiltroAtivacao {

    private FiltroAtivacao() {
    }

    static <T extends EntidadeAtivavel> List<T> filtrar(List<T> entidades, String ativo) {
        if (ativo == null || "todos".equalsIgnoreCase(ativo)) {
            return entidades;
        }
        if (!"true".equalsIgnoreCase(ativo) && !"false".equalsIgnoreCase(ativo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O filtro ativo deve ser true, false ou todos");
        }
        boolean estado = Boolean.parseBoolean(ativo);
        return entidades.stream()
                .filter(entidade -> estado == Boolean.TRUE.equals(entidade.getAtivo()))
                .toList();
    }
}