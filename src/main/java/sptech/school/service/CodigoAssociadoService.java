package sptech.school.service;

import org.springframework.stereotype.Service;
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
        return codigoAssociadoRepository.findAll();
    }

    public CodigoAssociado buscarPorId(Integer id) {
        return codigoAssociadoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Código Associado", id));
    }

    public List<CodigoAssociado> pesquisarPorCodigo(String codigo) {
        return codigoAssociadoRepository.findByCodigoContainingIgnoreCase(codigo);
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

    public void deletar(Integer id) {
        if (!codigoAssociadoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Código Associado", id);
        }
        codigoAssociadoRepository.deleteById(id);
    }

    public void preencher(CodigoAssociadoRequestDto request, CodigoAssociado entity){
        if (request.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(request.fornecedorId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor", request.fornecedorId()));
            entity.setFornecedor(fornecedor);
        }
        if (request.clienteId() != null) {
            Cliente cliente = clienteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente", request.clienteId()));
            entity.setCliente(cliente);
        }
    }
}
