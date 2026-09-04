package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.CategoriaMapper;
import sptech.school.repository.CategoriaRepository;
import sptech.school.util.BuscaSanitizer;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public CategoriaResponseDto buscarPorNome(String nome) {
        Categoria categoria = repository.findByNomeContaining(BuscaSanitizer.escaparLike(nome));
        if (categoria != null && !Boolean.TRUE.equals(categoria.getAtivo())) return null;
        if (categoria == null) return null;
        return CategoriaMapper.toResponseDto(categoria);
    }

    public List<CategoriaResponseDto> listarAdministrativo(String ativo) {
        return CategoriaMapper.toResponseDtoList(FiltroAtivacao.filtrar(repository.findAll(), ativo));
    }

    public CategoriaResponseDto buscarPorId(Integer id) {
        return CategoriaMapper.toResponseDto(repository.findById(id)
            .filter(categoria -> Boolean.TRUE.equals(categoria.getAtivo()))
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria não encontrada com id: ", id)));
    }

    public CategoriaResponseDto criar(CategoriaRequestDto fabricante) {
        return CategoriaMapper.toResponseDto(repository.save(CategoriaMapper.toEntity(fabricante)));
    }


    @Transactional
    public void desativar(Integer id, sptech.school.entity.Usuario usuarioExecutor) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria não encontrada", id));
        categoria.desativar(usuarioExecutor);
        repository.save(categoria);
    }

    public void deletar(Integer id) {
        desativar(id, null);
    }

    @Transactional
    public void reativar(Integer id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria não encontrada", id));
        categoria.reativar();
        repository.save(categoria);
    }
}
