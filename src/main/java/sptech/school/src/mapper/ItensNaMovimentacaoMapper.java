package sptech.school.src.mapper;

import sptech.school.src.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.src.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.src.entity.Item;
import sptech.school.src.entity.ItensNaMovimentacao;
import sptech.school.src.entity.MovimentacaoEstoque;

import java.util.List;

public class ItensNaMovimentacaoMapper {

    public static ItensNaMovimentacao toEntity(ItensNaMovimentacaoRequestDto dto) {
        ItensNaMovimentacao item = new ItensNaMovimentacao();

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setId(dto.movimentacaoEstoqueId());
        item.setMovimentacaoEstoque(movimentacao);

        Item itemRef = new Item();
        itemRef.setId(dto.itemId());
        item.setItem(itemRef);

        item.setQtd(dto.qtd());
        item.setPrecoUnitario(dto.precoUnitario());
        return item;
    }

    public static ItensNaMovimentacaoResponseDto toResponseDto(ItensNaMovimentacao itens) {
        return new ItensNaMovimentacaoResponseDto(
                itens.getId(),
                itens.getMovimentacaoEstoque() != null ? MovimentacaoEstoqueMapper.toResponse(itens.getMovimentacaoEstoque()) : null,
                itens.getItem() != null ? ItemMapper.toResponseDto(itens.getItem()) : null,
                itens.getQtd(),
                itens.getPrecoUnitario()
        );
    }

    public static List<ItensNaMovimentacaoResponseDto> toResponseDtoList(List<ItensNaMovimentacao> lista) {
        return lista.stream().map(ItensNaMovimentacaoMapper::toResponseDto).toList();
    }
}
