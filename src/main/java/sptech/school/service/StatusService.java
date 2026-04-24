package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.status.StatusRequestDto;
import sptech.school.dto.status.StatusResponseDto;
import sptech.school.entity.Status;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.StatusMapper;
import sptech.school.repository.StatusRepository;

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

