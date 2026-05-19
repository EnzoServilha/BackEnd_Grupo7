package sptech.school.src.service;

import org.springframework.stereotype.Service;
import sptech.school.src.dto.marca.MarcaRequestDto;
import sptech.school.src.dto.marca.MarcaResponseDto;
import sptech.school.src.exception.EntidadeNaoEncontradaException;
import sptech.school.src.mapper.MarcaMapper;
import sptech.school.src.repository.MarcaRepository;

@Service
public class MarcaService {
    private final MarcaRepository repository;

    public MarcaService(MarcaRepository repository) {
        this.repository = repository;
    }

    public MarcaResponseDto buscarPorNome(String nome) {
        return MarcaMapper.toResponseDto(repository.findByNomeEmpresaContaining(nome));
    }

    public MarcaResponseDto buscarPorId(Integer id) {
        return MarcaMapper.toResponseDto(repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada com id: ", id)));
    }

    public MarcaResponseDto criar(MarcaRequestDto fabricante) {
        return MarcaMapper.toResponseDto(repository.save(MarcaMapper.toEntity(fabricante)));
    }


    public void deletar(Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Marca não encontrada", id);
        repository.deleteById(id);
    }
}
