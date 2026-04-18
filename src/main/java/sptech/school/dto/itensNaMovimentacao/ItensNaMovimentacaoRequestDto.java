package sptech.school.dto.itensNaMovimentacao;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItensNaMovimentacaoRequestDto(
        @NotNull
        Integer movimentacaoEstoqueId,

        @NotNull
        Integer itemId,

        Integer qtd,

        Double precoUnitario
) {
}

