package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.entity.ItensNaMovimentacao;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.exceptions.MovimentacaoNaoEncontrada;
import sptech.school.mapper.MovimentacaoEstoqueMapper;
import sptech.school.repository.ItemMovimentacaoRepository;
import sptech.school.repository.MovimentacaoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {
    private MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public MovimentacaoEstoqueResponseDto buscarPorId(Integer id){
        MovimentacaoEstoque movimentacao = movimentacaoRepository.findById(id).orElseThrow(() -> new MovimentacaoNaoEncontrada("Movimentação não encontrada"));


        return MovimentacaoEstoqueMapper.toResponse(movimentacao);
    }

    public List<MovimentacaoEstoqueResponseDto> listar(){
        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacaoRepository.findAll());
    }

    public List<MovimentacaoEstoqueResponseDto> buscarPorTipo(String tipo){
        List<MovimentacaoEstoque> movimentacao = movimentacaoRepository.buscarPorTipo(tipo);


        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacao);
    }

    public List<MovimentacaoEstoqueResponseDto> buscarPorStatus(String status){
        List<MovimentacaoEstoque> movimentacao = movimentacaoRepository.buscarPorStatus(status);


        return MovimentacaoEstoqueMapper.toResponseDtoList(movimentacao);
    }

    public MovimentacaoEstoqueResponseDto criar(MovimentacaoEstoqueRequestDto request){

        MovimentacaoEstoque movimentacao = MovimentacaoEstoqueMapper.toEntity(request);

        movimentacao.setDataMovimentacao(LocalDateTime.now());

        return  MovimentacaoEstoqueMapper.toResponse(movimentacaoRepository.save(movimentacao));
    }

    public MovimentacaoEstoqueResponseDto editar(MovimentacaoEstoqueRequestDto request, Integer id){

        existe(id);

        MovimentacaoEstoque movimentacao = MovimentacaoEstoqueMapper.toEntity(request);

        movimentacao.setId(id);

        return MovimentacaoEstoqueMapper.toResponse(movimentacaoRepository.save(movimentacao));
    }

    public void deletar(Integer id){
        existe(id);

        movimentacaoRepository.deleteById(id);
    }

    public void existe(Integer id){
        boolean existe = movimentacaoRepository.existsById(id);
        if(!existe){
            throw new MovimentacaoNaoEncontrada("Movimentação não encontrada para edição");
        }
    }
}
