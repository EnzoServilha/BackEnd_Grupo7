package sptech.school.service;

import sptech.school.entity.Fabricante;
import sptech.school.entity.Fornecedor;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.FabricanteRepository;

import java.util.List;

public class FabricanteService {
    private final FabricanteRepository repository;

    public FabricanteService(FabricanteRepository repository) {
        this.repository = repository;
    }

    public List<Fabricante> listar() {
        return repository.findAll();
    }

    public Fabricante buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fabricante não encontrado", id));
    }

    public Fabricante buscarPorNomeContato(String nomeContato) {
        return repository.findByNomeContatoContaining(nomeContato);
    }

    public List<Fornecedor> listarFornecedoresPorFabricanteId(Integer fabricanteId) {
        Fabricante fabricante = repository.findById(fabricanteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fabricante não encontrado", fabricanteId));

        return fabricante.getFornecedores();
    }

    public Fabricante criar(Fabricante fabricante) {
        return repository.save(fabricante);
    }

    public Fabricante atualizar(Fabricante fabricante, Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Fabricante não encontrado", id);
        fabricante.setId(id);
        return repository.save(fabricante);
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Fabricante não encontrado", id);
        repository.deleteById(id);
    }
}
