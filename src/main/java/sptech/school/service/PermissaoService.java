package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.permissao.PermissaoRequestDto;
import sptech.school.dto.permissao.PermissaoResponseDto;
import sptech.school.entity.Permissao;
import sptech.school.exception.PermissaoNaoEncontradaException;
import sptech.school.mapper.PermissaoMapper;
import sptech.school.observer.publisher.EventPeriodoManager;
import sptech.school.repository.PermissaoRepository;

import java.util.List;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;
    private final EventPeriodoManager eventPeriodoManager;

    public PermissaoService(PermissaoRepository permissaoRepository, EventPeriodoManager eventPeriodoManager) {
        this.permissaoRepository = permissaoRepository;
        this.eventPeriodoManager = eventPeriodoManager;
    }

    public PermissaoResponseDto criar(PermissaoRequestDto dto) {
        Permissao permissao = PermissaoMapper.toEntity(dto);
        return PermissaoMapper.toResponseDto(permissaoRepository.save(permissao));
    }

    public List<PermissaoResponseDto> listarTodos() {
        eventPeriodoManager.notifyListeners();
        return PermissaoMapper.toResponseDtoList(permissaoRepository.findAll());
    }

    public PermissaoResponseDto buscarPorId(Integer id) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissão não encontrada"));
        return PermissaoMapper.toResponseDto(permissao);
    }

    public PermissaoResponseDto atualizar(Integer id, PermissaoRequestDto dto) {
        Permissao permissao = permissaoRepository.findById(id)
                .orElseThrow(() -> new PermissaoNaoEncontradaException("Permissão não encontrada"));
        permissao.setNome(dto.nome());
        return PermissaoMapper.toResponseDto(permissaoRepository.save(permissao));
    }

    public void deletar(Integer id) {
        if (!permissaoRepository.existsById(id)) {
            throw new PermissaoNaoEncontradaException("Permissão não encontrada");
        }
        permissaoRepository.deleteById(id);
    }
}
