package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.entity.Usuario;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.CategoriaMapper;
import sptech.school.repository.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    // Busca parcial para autocomplete (apenas ativas). O Spring Data já escapa curingas do LIKE,
    // por isso NÃO usamos BuscaSanitizer.escaparLike aqui (evita escape duplo).
    public List<CategoriaResponseDto> pesquisar(String termo) {
        return CategoriaMapper.toResponseDtoList(
                repository.findTop10ByNomeContainingIgnoreCaseAndAtivoTrue(termo.trim()));
    }

    // Busca exata (ignora maiúsculas/minúsculas). Retorna null se não existir ou estiver inativa.
    public CategoriaResponseDto buscarPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome.trim())
                .filter(m -> Boolean.TRUE.equals(m.getAtivo()))
                .map(CategoriaMapper::toResponseDto)
                .orElse(null);
    }

    public List<CategoriaResponseDto> listarAdministrativo(String ativo) {
        return CategoriaMapper.toResponseDtoList(FiltroAtivacao.filtrar(repository.findAll(), ativo));
    }

    public CategoriaResponseDto buscarPorId(Integer id) {
        return CategoriaMapper.toResponseDto(repository.findById(id)
                .filter(categoria -> Boolean.TRUE.equals(categoria.getAtivo()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria não encontrada com id: ", id)));
    }

    @Transactional
    public CategoriaResponseDto criar(CategoriaRequestDto dto) {
        return CategoriaMapper.toResponseDto(buscarOuCriarEntidade(dto));
    }

    @Transactional
    public Categoria buscarOuCriarEntidade(CategoriaRequestDto dto) {
        return buscarOuCriarPorNome(CategoriaMapper.toEntity(dto).getNome());
    }

    /**
     * Usado pelo FornecedorService: reaproveita a existente (reativando se inativa) ou cria nova.
     */
    @Transactional
    public Categoria buscarOuCriarPorNome(String nomeBruto) {
        String nome = nomeBruto.trim();

        return repository.findByNomeIgnoreCase(nome)
                .map(existente -> {
                    if (!Boolean.TRUE.equals(existente.getAtivo())) {
                        existente.reativar();
                        return repository.save(existente);
                    }
                    return existente;
                })
                .orElseGet(() -> {
                    Categoria nova = new Categoria();
                    nova.setNome(nome);
                    return repository.save(nova);
                });
    }

    @Transactional
    public void desativar(Integer id, Usuario usuarioExecutor) {
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