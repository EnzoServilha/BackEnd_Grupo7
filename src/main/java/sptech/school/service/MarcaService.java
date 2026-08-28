package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.entity.Marca;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.MarcaMapper;
import sptech.school.repository.MarcaRepository;
import sptech.school.util.BuscaSanitizer;

import java.util.List;

@Service
public class MarcaService {
    private final MarcaRepository repository;

    public MarcaService(MarcaRepository repository) {
        this.repository = repository;
    }

    public MarcaResponseDto buscarPorNome(String nome) {
        Marca marca = repository.findByNomeEmpresaContaining(BuscaSanitizer.escaparLike(nome));
        if (marca != null && !Boolean.TRUE.equals(marca.getAtivo())) return null;
        if (marca == null) return null;
        return MarcaMapper.toResponseDto(marca);
    }

    public List<MarcaResponseDto> listarAdministrativo(String ativo) {
        return MarcaMapper.toResponseDtoList(FiltroAtivacao.filtrar(repository.findAll(), ativo));
    }

    public MarcaResponseDto buscarPorId(Integer id) {
        return MarcaMapper.toResponseDto(repository.findById(id)
            .filter(marca -> Boolean.TRUE.equals(marca.getAtivo()))
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada com id: ", id)));
    }

    public MarcaResponseDto criar(MarcaRequestDto fabricante) {
        return MarcaMapper.toResponseDto(repository.save(MarcaMapper.toEntity(fabricante)));
    }


    @Transactional
    public void desativar(Integer id, sptech.school.entity.Usuario usuarioExecutor) {
        Marca marca = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada", id));
        marca.desativar(usuarioExecutor);
        repository.save(marca);
    }

    public void deletar(Integer id) {
        desativar(id, null);
    }

    @Transactional
    public void reativar(Integer id) {
        Marca marca = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada", id));
        marca.reativar();
        repository.save(marca);
    }
}
