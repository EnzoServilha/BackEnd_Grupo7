package sptech.school.src.service;

import org.springframework.stereotype.Service;
import sptech.school.src.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.src.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.src.entity.Item;
import sptech.school.src.entity.ItensNaMovimentacao;
import sptech.school.src.entity.MovimentacaoEstoque;
import sptech.school.src.exception.EntidadeNaoEncontradaException;
import sptech.school.src.exception.ItemNaMovimentacaoNaoEncontrado;
import sptech.school.src.mapper.ItensNaMovimentacaoMapper;
import sptech.school.src.repository.ItemRepository;
import sptech.school.src.repository.ItensNaMovimentacaoRepository;
import sptech.school.src.repository.MovimentacaoEstoqueRepository;

import java.util.List;

@Service
public class ItemNaMovimentacaoService {
    private ItensNaMovimentacaoRepository itemMovimentacaoRepository;
    private ItemRepository itemRepository;
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public ItemNaMovimentacaoService(ItensNaMovimentacaoRepository itemMovimentacaoRepository, ItemRepository itemRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.itemMovimentacaoRepository = itemMovimentacaoRepository;
        this.itemRepository = itemRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public List<ItensNaMovimentacaoResponseDto> listar(){
        return ItensNaMovimentacaoMapper.toResponseDtoList(itemMovimentacaoRepository.findAll());
    }

    public List<ItensNaMovimentacaoResponseDto> listarPorItem(Integer id){
        return ItensNaMovimentacaoMapper.toResponseDtoList(itemMovimentacaoRepository.findAllByItemId(id));
    }

    public List<ItensNaMovimentacaoResponseDto> listarPorMovimentacao(Integer id){
        return ItensNaMovimentacaoMapper.toResponseDtoList(itemMovimentacaoRepository.findAllByMovimentacaoEstoqueId(id));
    }

    public ItensNaMovimentacaoResponseDto criar(ItensNaMovimentacaoRequestDto requestDto){

        ItensNaMovimentacao itensNaMovimentacao =   itemMovimentacaoRepository.save(ItensNaMovimentacaoMapper.toEntity(requestDto));

        MovimentacaoEstoque movimentacaoEstoque = movimentacaoEstoqueRepository.findById(requestDto.movimentacaoEstoqueId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Movimentação de estoque não encontrada", requestDto.movimentacaoEstoqueId()));

        Item item = itemRepository.findById(requestDto.itemId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Item não encontrado", requestDto.itemId()));

        itensNaMovimentacao.setItem(item);
        itensNaMovimentacao.setMovimentacaoEstoque(movimentacaoEstoque);

        return  ItensNaMovimentacaoMapper.toResponseDto(itemMovimentacaoRepository.save(itensNaMovimentacao));
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
