package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exceptions.MovimentacaoNaoEncontrada;
import sptech.school.mapper.MovimentacaoEstoqueMapper;
import sptech.school.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoEstoqueService {
    private MovimentacaoRepository movimentacaoRepository;
    private UsuarioRepository usuarioRepository;
    private TipoRepository tipoRepository;
    private StatusRepository statusRepository;
    private ClienteRepository clienteRepository;
    private FornecedorRepository fornecedorRepository;

    public MovimentacaoEstoqueService(MovimentacaoRepository movimentacaoRepository,
            UsuarioRepository usuarioRepository,
            TipoRepository tipoRepository,
            StatusRepository statusRepository,
            ClienteRepository clienteRepository,
            FornecedorRepository fornecedorRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoRepository = tipoRepository;
        this.statusRepository = statusRepository;
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
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

        validarRelacionamentos(request);

        MovimentacaoEstoque movimentacao = MovimentacaoEstoqueMapper.toEntity(request);

        movimentacao.setDataMovimentacao(LocalDateTime.now());

        return  MovimentacaoEstoqueMapper.toResponse(movimentacaoRepository.save(movimentacao));
    }

    public MovimentacaoEstoqueResponseDto editar(MovimentacaoEstoqueRequestDto request, Integer id){

         existe(id);

         validarRelacionamentos(request);

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

    private void validarRelacionamentos(MovimentacaoEstoqueRequestDto request) {

        if (request.usuarioId() != null && !usuarioRepository.existsById(request.usuarioId().longValue())) {
            throw new EntidadeNaoEncontradaException("Usuário não encontrado", request.usuarioId());
        }

        if (request.tipoId() != null && !tipoRepository.existsById(request.tipoId())) {
            throw new EntidadeNaoEncontradaException("Tipo não encontrado", request.tipoId());
        }

        if (request.statusId() != null && !statusRepository.existsById(request.statusId())) {
            throw new EntidadeNaoEncontradaException("Status não encontrado", request.statusId());
        }

        if (request.clienteId() != null && !clienteRepository.existsById(request.clienteId())) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado", request.clienteId());
        }

        if (request.fornecedorId() != null && !fornecedorRepository.existsById(request.fornecedorId())) {
            throw new EntidadeNaoEncontradaException("Fornecedor não encontrado", request.fornecedorId());
        }

        if (request.movimentacaoOriginalId() != null && !movimentacaoRepository.existsById(request.movimentacaoOriginalId())) {
            throw new MovimentacaoNaoEncontrada("Movimentação original não encontrada");
        }
    }
}
