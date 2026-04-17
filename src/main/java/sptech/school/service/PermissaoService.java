package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import sptech.school.dto.permissao.PermissaoRequestDto;
import sptech.school.dto.permissao.PermissaoResponseDto;
import sptech.school.entity.Permissao;
import sptech.school.exception.PermissaoNaoEncontradaException;
import sptech.school.mapper.PermissaoMapper;
import sptech.school.repository.PermissaoRepository;

import java.util.List;

public class PermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

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
