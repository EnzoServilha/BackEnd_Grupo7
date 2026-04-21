package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.entity.Categoria;
import sptech.school.entity.Marca;
import sptech.school.entity.Fornecedor;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.FornecedorRepository;

import java.util.List;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    public List<Fornecedor> listar() {
        return repository.findAll();
    }

    public Fornecedor buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", id));
    }

    public Fornecedor buscarPorNomeContato(String nomeContato) {
        return repository.findByNomeContatoContaining(nomeContato);
    }

    public Fornecedor buscarPorNomeEmpresa(String nomeEmpresa) {
        return repository.findByNomeEmpresaContaining(nomeEmpresa);
    }

    public List<Fornecedor> listarPorCategoria(Categoria categoria) {
        return repository.findAllByCategoria(categoria);
    }

    public List<Marca> listarFabricantesPorFornecedorId(Integer fornecedorId) {
        Fornecedor fornecedor = repository.findById(fornecedorId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", fornecedorId));

        return fornecedor.getFabricantes();
    }

    public Fornecedor criar(Fornecedor fornecedor) {
        return repository.save(fornecedor);
    }

    public Fornecedor atualizar(Fornecedor fornecedor, Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Fornecedor não encontrado", id);
        fornecedor.setId(id);
        return repository.save(fornecedor);
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Fornecedor não encontrado", id);
        repository.deleteById(id);
    }
}
