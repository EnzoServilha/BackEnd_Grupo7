package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.exceptions.ItemNaMovimentacaoNaoEncontrado;
import sptech.school.mapper.ItensNaMovimentacaoMapper;
import sptech.school.repository.ItemMovimentacaoRepository;

@Service
public class ItemNaMovimentacaoService {
    private ItemMovimentacaoRepository itemMovimentacaoRepository;

    public ItemNaMovimentacaoService(ItemMovimentacaoRepository itemMovimentacaoRepository) {
        this.itemMovimentacaoRepository = itemMovimentacaoRepository;
    }

    public ItensNaMovimentacaoResponseDto criar(ItensNaMovimentacaoRequestDto requestDto){
        return ItensNaMovimentacaoMapper.toResponseDto(itemMovimentacaoRepository.save(ItensNaMovimentacaoMapper.toEntity(requestDto)));
    }

    public ItensNaMovimentacaoResponseDto editar(ItensNaMovimentacaoRequestDto requestDto, Integer id){
        existe(id);

        ItensNaMovimentacao itens = ItensNaMovimentacaoMapper.toEntity(requestDto);

        itens.setId(id);

        return ItensNaMovimentacaoMapper.toResponseDto(itemMovimentacaoRepository.save(itens));
    }

    public void deletar(Integer id){
        existe(id);

        itemMovimentacaoRepository.deleteById(id);
    }

    public void existe(Integer id){
        if(!itemMovimentacaoRepository.existsById(id)){
            throw new ItemNaMovimentacaoNaoEncontrado("Item na movimentação não encontrado");
        }
    }
}
