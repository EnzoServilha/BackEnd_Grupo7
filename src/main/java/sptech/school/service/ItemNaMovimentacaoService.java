package sptech.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoRequestDto;
import sptech.school.dto.itensNaMovimentacao.ItensNaMovimentacaoResponseDto;
import sptech.school.entity.Item;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exception.ItemNaMovimentacaoNaoEncontrado;
import sptech.school.mapper.ItensNaMovimentacaoMapper;
import sptech.school.repository.ItemRepository;
import sptech.school.repository.ItensNaMovimentacaoRepository;
import sptech.school.repository.MovimentacaoEstoqueRepository;

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

    @Transactional
    public ItensNaMovimentacaoResponseDto criar(ItensNaMovimentacaoRequestDto requestDto){
        MovimentacaoEstoque movimentacaoEstoque = movimentacaoEstoqueRepository.findById(requestDto.movimentacaoEstoqueId()).orElseThrow(() -> new EntidadeNaoEncontradaException("Movimentação de estoque não encontrada", requestDto.movimentacaoEstoqueId()));
        validarPendente(movimentacaoEstoque);

        Item item = itemRepository.findById(requestDto.itemId())
            .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Item ativo não encontrado", requestDto.itemId()));

        if (itemMovimentacaoRepository.existsByMovimentacaoEstoqueIdAndItemId(
            requestDto.movimentacaoEstoqueId(), requestDto.itemId())) {
            throw new EntidadeConflitanteException(
                "O item já está cadastrado nessa movimentação");
        }

        ItensNaMovimentacao itensNaMovimentacao = ItensNaMovimentacaoMapper.toEntity(requestDto);
        itensNaMovimentacao.setItem(item);
        itensNaMovimentacao.setMovimentacaoEstoque(movimentacaoEstoque);

        return  ItensNaMovimentacaoMapper.toResponseDto(itemMovimentacaoRepository.save(itensNaMovimentacao));
    }

    @Transactional
    public ItensNaMovimentacaoResponseDto editar(ItensNaMovimentacaoRequestDto requestDto, Integer id){
        ItensNaMovimentacao itens = itemMovimentacaoRepository.findById(id)
                .orElseThrow(() -> new ItemNaMovimentacaoNaoEncontrado("Item na movimentação não encontrado"));
        validarPendente(itens.getMovimentacaoEstoque());
        itens.setQtd(requestDto.qtd());
        itens.setPrecoUnitario(requestDto.precoUnitario());

        return ItensNaMovimentacaoMapper.toResponseDto(itemMovimentacaoRepository.save(itens));
    }

    @Transactional
    public void deletar(Integer id){
        ItensNaMovimentacao item = itemMovimentacaoRepository.findById(id)
                .orElseThrow(() -> new ItemNaMovimentacaoNaoEncontrado("Item na movimentação não encontrado"));
        validarPendente(item.getMovimentacaoEstoque());
        itemMovimentacaoRepository.deleteById(id);
    }

    public void existe(Integer id){
        if(!itemMovimentacaoRepository.existsById(id)){
            throw new ItemNaMovimentacaoNaoEncontrado("Item na movimentação não encontrado");
        }
    }

    private void validarPendente(MovimentacaoEstoque movimentacao) {
        if (movimentacao.getStatus() == null || !"PENDENTE".equals(movimentacao.getStatus().getNome())) {
            throw new EntidadeConflitanteException("Itens só podem ser alterados em movimentações pendentes");
        }
    }
}
