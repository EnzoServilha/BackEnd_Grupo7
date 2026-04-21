package sptech.school.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.entity.Fornecedor;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.CategoriaMapper;
import sptech.school.repository.CategoriaRepository;
import sptech.school.repository.FornecedorRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final FornecedorRepository fornecedorRepository;

    public CategoriaService(CategoriaRepository repository, FornecedorRepository fornecedorRepository) {
        this.repository = repository;
        this.fornecedorRepository = fornecedorRepository;
    }

    public CategoriaResponseDto buscarPorNome(String nome) {
        return CategoriaMapper.toResponseDto(repository.findByNomeContaining(nome));
    }

    public CategoriaResponseDto criar(CategoriaRequestDto fabricante) {
        return CategoriaMapper.toResponseDto(repository.save(CategoriaMapper.toEntity(fabricante)));
    }

    @Transactional
    public CategoriaResponseDto associarCategoriaaFornecedor(Integer categoriaId, Integer fornecedorId) {
        Categoria categoria = repository.findById(categoriaId).orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada", categoriaId));

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", fornecedorId));

        if (!categoria.getFornecedores().contains(fornecedor)) {
            categoria.getFornecedores().add(fornecedor);
        }

        return CategoriaMapper.toResponseDto(repository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDto desassociarCategoriaaFornecedor(Integer categoriaId, Integer fornecedorId) {
        Categoria categoria = repository.findById(categoriaId).orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada", categoriaId));

        Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId).orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", fornecedorId));

        categoria.getFornecedores().remove(fornecedor);

        fornecedor.getMarcas().remove(categoria);

        return CategoriaMapper.toResponseDto(repository.save(categoria));
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Marca não encontrada", id);
        repository.deleteById(id);
    }
}
