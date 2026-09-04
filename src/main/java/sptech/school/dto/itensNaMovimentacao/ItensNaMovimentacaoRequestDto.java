package sptech.school.dto.itensNaMovimentacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ItensNaMovimentacaoRequestDto(
        @NotNull
        Integer movimentacaoEstoqueId,

        @NotNull
        Integer itemId,

        @NotNull
        @Positive
        Integer qtd,

        @NotNull
        @PositiveOrZero
        BigDecimal precoUnitario
) {
}

