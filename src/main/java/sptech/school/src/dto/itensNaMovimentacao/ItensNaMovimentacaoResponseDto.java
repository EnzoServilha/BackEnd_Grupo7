package sptech.school.src.dto.itensNaMovimentacao;

import sptech.school.src.dto.item.ItemResponseDto;
import sptech.school.src.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;

public record ItensNaMovimentacaoResponseDto(
        Integer id,
        MovimentacaoEstoqueResponseDto movimentacaoEstoque,
        ItemResponseDto item,
        Integer qtd,
        Double precoUnitario
) {
}
