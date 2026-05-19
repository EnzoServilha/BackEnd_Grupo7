package sptech.school.src.service;

import org.springframework.stereotype.Service;
import sptech.school.src.dto.permissao.PermissaoRequestDto;
import sptech.school.src.dto.permissao.PermissaoResponseDto;
import sptech.school.src.entity.Permissao;
import sptech.school.src.exception.PermissaoNaoEncontradaException;
import sptech.school.src.mapper.PermissaoMapper;
import sptech.school.src.repository.PermissaoRepository;

import java.util.List;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    public PermissaoResponseDto criar(PermissaoRequestDto dto) {
        Permissao permissao = PermissaoMapper.toEntity(dto);
        return PermissaoMapper.toResponseDto(permissaoRepository.save(permissao));
    }

    public List<PermissaoResponseDto> listarTodos() {
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
