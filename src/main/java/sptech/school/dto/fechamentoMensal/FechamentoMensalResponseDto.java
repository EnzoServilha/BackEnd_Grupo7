package sptech.school.dto.fechamentoMensal;

import sptech.school.dto.item.ItemResponseDto;

public record FechamentoMensalResponseDto(
        Integer id,
        Integer mes,
        Integer ano,
        Integer qtd,
        ItemResponseDto item
) {
}

