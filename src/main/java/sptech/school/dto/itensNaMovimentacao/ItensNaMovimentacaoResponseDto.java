package sptech.school.dto.itensNaMovimentacao;

import sptech.school.dto.item.ItemResponseDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;

public record ItensNaMovimentacaoResponseDto(
        Integer id,
        MovimentacaoEstoqueResponseDto movimentacaoEstoque,
        ItemResponseDto item,
        Integer qtd,
        Double precoUnitario
) {
}
