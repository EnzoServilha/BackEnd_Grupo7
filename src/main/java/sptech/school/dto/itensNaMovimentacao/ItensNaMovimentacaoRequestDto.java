package sptech.school.dto.itensNaMovimentacao;

import jakarta.validation.constraints.NotNull;

public record ItensNaMovimentacaoRequestDto(
        @NotNull
        Integer movimentacaoEstoqueId,

        @NotNull
        Integer itemId,

        Integer qtd,

        Double precoUnitario
) {
}

