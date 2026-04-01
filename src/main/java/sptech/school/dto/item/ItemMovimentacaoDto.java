package sptech.school.dto.item;

import java.math.BigDecimal;

public record ItemMovimentacaoDto(
        Integer pecaId,
        Integer qtd,
        BigDecimal precoUnitario
) {
}

