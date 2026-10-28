package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.entity.Marca;
import sptech.school.entity.Usuario;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.MarcaMapper;
import sptech.school.repository.MarcaRepository;

import java.util.List;

@Service
public class MarcaService {
    private final MarcaRepository repository;

    public MarcaService(MarcaRepository repository) {
        this.repository = repository;
    }

    // Busca parcial para autocomplete (apenas ativas). O Spring Data já escapa curingas do LIKE,
    // por isso NÃO usamos BuscaSanitizer.escaparLike aqui (evita escape duplo).
    public List<MarcaResponseDto> pesquisar(String termo) {
        return MarcaMapper.toResponseDtoList(
                repository.findTop10ByNomeEmpresaContainingIgnoreCaseAndAtivoTrue(termo.trim()));
    }

    // Busca exata (ignora maiúsculas/minúsculas). Retorna null se não existir ou estiver inativa.
    public MarcaResponseDto buscarPorNome(String nome) {
        return repository.findByNomeEmpresaIgnoreCase(nome.trim())
                .filter(m -> Boolean.TRUE.equals(m.getAtivo()))
                .map(MarcaMapper::toResponseDto)
                .orElse(null);
    }

    public List<MarcaResponseDto> listarAdministrativo(String ativo) {
        return MarcaMapper.toResponseDtoList(FiltroAtivacao.filtrar(repository.findAll(), ativo));
    }

    public MarcaResponseDto buscarPorId(Integer id) {
        return MarcaMapper.toResponseDto(repository.findById(id)
                .filter(marca -> Boolean.TRUE.equals(marca.getAtivo()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Marca não encontrada com id: ", id)));
    }

    @Transactional
    public MarcaResponseDto criar(MarcaRequestDto dto) {
        return MarcaMapper.toResponseDto(buscarOuCriarEntidade(dto));
    }

    @Transactional
    public Marca buscarOuCriarEntidade(MarcaRequestDto dto) {
        return buscarOuCriarPorNome(MarcaMapper.toEntity(dto).getNomeEmpresa());
    }

    /**
     * Usado pelo FornecedorService: reaproveita a existente (reativando se inativa) ou cria nova.
     */
    @Transactional
    public Marca buscarOuCriarPorNome(String nomeBruto) {
        String nome = nomeBruto.trim();

        return repository.findByNomeEmpresaIgnoreCase(nome)
                .map(existente -> {
                    if (!Boolean.TRUE.equals(existente.getAtivo())) {
                        existente.reativar();
                        return repository.save(existente);
                    }
                    return existente;
                })
                .orElseGet(() -> {
                    Marca nova = new Marca();
                    nova.setNomeEmpresa(nome);
                    return repository.save(nova);
                });
    }

    @Transactional
    public void desativar(Integer id, Usuario usuarioExecutor) {
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