package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.dto.codigoAssociado.CodigoAssociadoRequestDto;
import sptech.school.dto.codigoAssociado.CodigoAssociadoResponseDto;
import sptech.school.entity.Cliente;
import sptech.school.entity.CodigoAssociado;
import sptech.school.entity.Fornecedor;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.CodigoAssociadoMapper;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.CodigoAssociadoRepository;
import sptech.school.repository.FornecedorRepository;
import sptech.school.util.BuscaSanitizer;

import java.util.List;

@Service
public class CodigoAssociadoService {

    private final CodigoAssociadoRepository codigoAssociadoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ClienteRepository clienteRepository;

    public CodigoAssociadoService(CodigoAssociadoRepository codigoAssociadoRepository, FornecedorRepository fornecedorRepository, ClienteRepository clienteRepository) {
        this.codigoAssociadoRepository = codigoAssociadoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<CodigoAssociado> listarTodos() {
        return codigoAssociadoRepository.findAll().stream()
            .filter(codigo -> Boolean.TRUE.equals(codigo.getAtivo())).toList();
    }

    public List<CodigoAssociado> listarAdministrativo(String ativo) {
        return FiltroAtivacao.filtrar(codigoAssociadoRepository.findAll(), ativo);
    }

    public CodigoAssociado buscarPorId(Integer id) {
        return codigoAssociadoRepository.findById(id)
            .filter(codigo -> Boolean.TRUE.equals(codigo.getAtivo()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Código Associado", id));
    }

    public List<CodigoAssociado> pesquisarPorCodigo(String codigo) {
        return codigoAssociadoRepository.findByCodigoContainingIgnoreCase(
                BuscaSanitizer.escaparLike(codigo)).stream()
            .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo())).toList();
    }

    public CodigoAssociadoResponseDto cadastrar(CodigoAssociadoRequestDto codigoAssociado) {

        CodigoAssociado entity = CodigoAssociadoMapper.toEntity(codigoAssociado);

        preencher(codigoAssociado, entity);

        CodigoAssociado salvo = codigoAssociadoRepository.save(entity);
        return CodigoAssociadoMapper.toResponseDto(salvo);

    }

    public CodigoAssociadoResponseDto atualizar(Integer id, CodigoAssociadoRequestDto requestDto) {

        boolean existe = codigoAssociadoRepository.existsById(id);
        if (!existe) {
            throw new EntidadeNaoEncontradaException("Código Associado", id);
        }
        CodigoAssociado existente = CodigoAssociadoMapper.toEntity(requestDto);
        existente.setId(id);

        preencher(requestDto, existente);

        return CodigoAssociadoMapper.toResponseDto(codigoAssociadoRepository.save(existente));
    }

    @Transactional
    public void desativar(Integer id, sptech.school.entity.Usuario usuarioExecutor) {
        CodigoAssociado codigo = codigoAssociadoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Código Associado", id));
        codigo.desativar(usuarioExecutor);
        codigoAssociadoRepository.save(codigo);
    }

    public void deletar(Integer id) {
        desativar(id, null);
    }

    @Transactional
    public void reativar(Integer id) {
        CodigoAssociado codigo = codigoAssociadoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Código Associado", id));
        codigo.reativar();
        codigoAssociadoRepository.save(codigo);
    }

    public void preencher(CodigoAssociadoRequestDto request, CodigoAssociado entity){
        if (request.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                    .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor", request.fornecedorId()));
            entity.setFornecedor(fornecedor);
        }
        if (request.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(request.clienteId())
                    .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", request.clienteId()));
            entity.setCliente(cliente);
        }
    }
}
