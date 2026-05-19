package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.CategoriaMapper;
import sptech.school.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public CategoriaResponseDto buscarPorNome(String nome) {
        return CategoriaMapper.toResponseDto(repository.findByNomeContaining(nome));
    }

    public CategoriaResponseDto buscarPorId(Integer id) {
        return CategoriaMapper.toResponseDto(repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria não encontrada com id: ", id)));
    }

    public CategoriaResponseDto criar(CategoriaRequestDto fabricante) {
        return CategoriaMapper.toResponseDto(repository.save(CategoriaMapper.toEntity(fabricante)));
    }


    public void deletar(Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Marca não encontrada", id);
        repository.deleteById(id);
    }
}
