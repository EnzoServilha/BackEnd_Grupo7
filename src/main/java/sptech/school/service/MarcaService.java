package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.MarcaMapper;
import sptech.school.repository.MarcaRepository;

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
