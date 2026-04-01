package sptech.school.dto.item;

import java.time.LocalDateTime;

public record ItemResponseDto(
        Integer id,
        Integer codigoInterno,
        String marca,
        Integer ano,
        String descricao,
        String localizacao,
        LocalDateTime dataCadastro
) {
}
