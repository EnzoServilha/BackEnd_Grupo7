package sptech.school.dto.movimentacaoEstoque;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ItemFechamentoCotacaoRequestDto(
        @NotNull Integer itemId,
        @NotNull @Positive Integer qtd,
        @NotNull @PositiveOrZero BigDecimal precoUnitario
) {
}