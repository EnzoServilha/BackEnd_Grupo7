package sptech.school.dto.itensNaMovimentacao;

import sptech.school.dto.item.ItemResponseDto;

import java.math.BigDecimal;

public record ItensNaMovimentacaoResponseDto(
        Integer id,
        Integer movimentacaoEstoqueId,
        ItemResponseDto item,
        Integer qtd,
        BigDecimal precoUnitario
) {
}
