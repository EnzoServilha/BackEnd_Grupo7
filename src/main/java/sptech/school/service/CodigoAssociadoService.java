package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.entity.CodigoAssociado;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.CodigoAssociadoRepository;

import java.util.List;

@Service
public class CodigoAssociadoService {

    private final CodigoAssociadoRepository codigoAssociadoRepository;

    public CodigoAssociadoService(CodigoAssociadoRepository codigoAssociadoRepository) {
        this.codigoAssociadoRepository = codigoAssociadoRepository;
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

    public CodigoAssociado cadastrar(CodigoAssociado codigoAssociado) {
        return codigoAssociadoRepository.save(codigoAssociado);
    }

    public CodigoAssociado atualizar(Integer id, CodigoAssociado atualizado) {
        CodigoAssociado existente = buscarPorId(id);
        existente.setCodigo(atualizado.getCodigo());
        existente.setFornecedor(atualizado.getFornecedor());
        existente.setCliente(atualizado.getCliente());
        return codigoAssociadoRepository.save(existente);
    }

    public void deletar(Integer id) {
        if (!codigoAssociadoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Código Associado", id);
        }
        codigoAssociadoRepository.deleteById(id);
    }
}
