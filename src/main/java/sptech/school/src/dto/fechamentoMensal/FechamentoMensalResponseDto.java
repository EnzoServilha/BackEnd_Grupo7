package sptech.school.src.dto.fechamentoMensal;

import sptech.school.src.dto.item.ItemResponseDto;

public record FechamentoMensalResponseDto(
        Integer id,
        Integer mes,
        Integer ano,
        Integer qtd,
        ItemResponseDto item
) {
}

