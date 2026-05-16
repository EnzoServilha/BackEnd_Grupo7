package sptech.school.src.service;

import org.springframework.stereotype.Service;
import sptech.school.src.dto.tipo.TipoRequestDto;
import sptech.school.src.dto.tipo.TipoResponseDto;
import sptech.school.src.entity.Tipo;
import sptech.school.src.exception.EntidadeNaoEncontradaException;
import sptech.school.src.mapper.TipoMapper;
import sptech.school.src.repository.TipoRepository;

@Service
public class TipoService {

    private final TipoRepository repository;

    public TipoService(TipoRepository repository) {
        this.repository = repository;
    }

    public TipoResponseDto criar(TipoRequestDto requestDto) {
        Tipo tipo = TipoMapper.toEntity(requestDto);
        return TipoMapper.toResponseDto(repository.save(tipo));
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Tipo não encontrado", id);
        }
        repository.deleteById(id);
    }
}

