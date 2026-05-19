package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueRequestDto;
import sptech.school.dto.movimentacaoEstoque.MovimentacaoEstoqueResponseDto;
import sptech.school.entity.MovimentacaoEstoque;
import sptech.school.entity.Usuario;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exception.MovimentacaoNaoEncontrada;
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
    private PeriodoRepository periodoRepository;

    public MovimentacaoEstoqueService(MovimentacaoRepository movimentacaoRepository, UsuarioRepository usuarioRepository, TipoRepository tipoRepository, StatusRepository statusRepository, ClienteRepository clienteRepository, FornecedorRepository fornecedorRepository, PeriodoRepository periodoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoRepository = tipoRepository;
        this.statusRepository = statusRepository;
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.periodoRepository = periodoRepository;
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
        preencher(movimentacao, request);

        return  MovimentacaoEstoqueMapper.toResponse(movimentacaoRepository.save(movimentacao));
    }

    public MovimentacaoEstoqueResponseDto editar(MovimentacaoEstoqueRequestDto request, Integer id){

         existe(id);


         MovimentacaoEstoque movimentacao = MovimentacaoEstoqueMapper.toEntity(request);

         movimentacao.setId(id);

         preencher(movimentacao, request);

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


    public void preencher(MovimentacaoEstoque entidade, MovimentacaoEstoqueRequestDto requestDto){
        if (requestDto.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(requestDto.usuarioId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado", requestDto.usuarioId()));


            entidade.setUsuario(usuario);
        }

        if(requestDto.tipoId() != null){
            entidade.setTipo(tipoRepository.findById(requestDto.tipoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Tipo não encontrado", requestDto.tipoId())));
        }

        if(requestDto.statusId() != null){
            entidade.setStatus(statusRepository.findById(requestDto.statusId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Status não encontrado", requestDto.statusId())));
        }

        if(requestDto.clienteId() != null){
            entidade.setCliente(clienteRepository.findById(requestDto.clienteId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado", requestDto.clienteId())));
        }

        if(requestDto.fornecedorId() != null){
            entidade.setFornecedor(fornecedorRepository.findById(requestDto.fornecedorId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", requestDto.fornecedorId())));
        }

        if (requestDto.movimentacaoOriginalId() != null) {
            entidade.setMovimentacaoOriginal(movimentacaoRepository.findById(requestDto.movimentacaoOriginalId())
                    .orElseThrow(() -> new MovimentacaoNaoEncontrada("Movimentação original não encontrada")));
        }

            entidade.setPeriodo(periodoRepository.findById(requestDto.periodoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Período não encontrado", requestDto.periodoId())));

    }
}
