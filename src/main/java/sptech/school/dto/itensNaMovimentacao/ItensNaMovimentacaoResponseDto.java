package sptech.school.dto.itensNaMovimentacao;

import sptech.school.dto.item.ItemResponseDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;

import java.math.BigDecimal;

public record ItensNaMovimentacaoResponseDto(
        Integer id,
        MovimentacaoEstoqueResponseDto movimentacaoEstoque,
        ItemResponseDto item,
        Integer qtd,
        BigDecimal precoUnitario
) {
}
