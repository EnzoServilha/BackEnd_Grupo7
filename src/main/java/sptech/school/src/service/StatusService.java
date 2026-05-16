package sptech.school.src.service;

import org.springframework.stereotype.Service;
import sptech.school.src.dto.status.StatusRequestDto;
import sptech.school.src.dto.status.StatusResponseDto;
import sptech.school.src.entity.Status;
import sptech.school.src.exception.EntidadeNaoEncontradaException;
import sptech.school.src.mapper.StatusMapper;
import sptech.school.src.repository.StatusRepository;

@Service
public class StatusService {

    private final StatusRepository repository;

    public StatusService(StatusRepository repository) {
        this.repository = repository;
    }

    public StatusResponseDto criar(StatusRequestDto requestDto) {
        Status status = StatusMapper.toEntity(requestDto);
        return StatusMapper.toResponseDto(repository.save(status));
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Status não encontrado", id);
        }
        repository.deleteById(id);
    }
}

