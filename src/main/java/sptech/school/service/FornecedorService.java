package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.dto.fornecedor.FornecedorRequestDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.entity.Endereco;
import sptech.school.entity.Marca;
import sptech.school.entity.Fornecedor;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.FornecedorMapper;
import sptech.school.repository.CategoriaRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.repository.FornecedorRepository;
import sptech.school.repository.MarcaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EnderecoRepository enderecoRepository;

    public FornecedorService(FornecedorRepository repository, MarcaRepository marcaRepository, CategoriaRepository categoriaRepository, EnderecoRepository enderecoRepository) {
        this.repository = repository;
        this.marcaRepository = marcaRepository;
        this.categoriaRepository = categoriaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public List<FornecedorResponseDto> listar() {
        return FornecedorMapper.toResponseDtoList(repository.findAll().stream()
            .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo())).toList());
    }

    public List<FornecedorResponseDto> listarAdministrativo(String ativo) {
        return FornecedorMapper.toResponseDtoList(FiltroAtivacao.filtrar(repository.findAll(), ativo));
    }

    public FornecedorResponseDto buscarPorId(Integer id) {
        return FornecedorMapper.toResponseDto(repository.findById(id)
            .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo()))
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", id)));
    }

    public List<FornecedorResponseDto> buscarPorNomeContato(String nomeContato) {
        return FornecedorMapper.toResponseDtoList(repository.findByNomeContatoContaining(nomeContato).stream()
            .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo())).toList()) ;
    }

    public List<FornecedorResponseDto> buscarPorNomeEmpresa(String nomeEmpresa) {
        return FornecedorMapper.toResponseDtoList(repository.findByNomeEmpresaContaining(nomeEmpresa).stream()
            .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo())).toList()) ;
    }

    public List<FornecedorResponseDto> listarPorCategoria(Integer idCategoria) {
        List<Fornecedor> fornecedor = repository.findByIdCategoria(idCategoria);

        return FornecedorMapper.toResponseDtoList(fornecedor);
    }

    public List<FornecedorResponseDto> listarPorMarca(Integer idMarca) {
        List<Fornecedor> fornecedor = repository.findByIdMarca(idMarca);

        return FornecedorMapper.toResponseDtoList(fornecedor);
    }

    public FornecedorResponseDto criar(FornecedorRequestDto fornecedor) {
        Fornecedor novoFornecedor = FornecedorMapper.toEntity(fornecedor);

        novoFornecedor.setDataCadastro(LocalDateTime.now());

        preencher(novoFornecedor, fornecedor);

        return FornecedorMapper.toResponseDto(repository.save(novoFornecedor));
    }

    public FornecedorResponseDto atualizar(FornecedorRequestDto fornecedor, Integer id) {
        if (!repository.existsById(id)) throw new EntidadeNaoEncontradaException("Fornecedor não encontrado", id);
        repository.findById(id).ifPresent(encontrado -> {
            if (!Boolean.TRUE.equals(encontrado.getAtivo())) {
                throw new EntidadeNaoEncontradaException("Fornecedor não encontrado", id);
            }
        });

        Fornecedor fornecedorEntity = FornecedorMapper.toEntity(fornecedor);

        fornecedorEntity.setId(id);

        preencher(fornecedorEntity, fornecedor);

        return FornecedorMapper.toResponseDto(repository.save(fornecedorEntity));
    }

    @Transactional
    public void desativar(Integer id, sptech.school.entity.Usuario usuarioExecutor) {
        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", id));
        fornecedor.desativar(usuarioExecutor);
        repository.save(fornecedor);
    }

    public void deletar(Integer id) {
        desativar(id, null);
    }

    @Transactional
    public void reativar(Integer id) {
        Fornecedor fornecedor = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", id));
        fornecedor.reativar();
        repository.save(fornecedor);
    }

    public void preencher(Fornecedor novoFornecedor, FornecedorRequestDto fornecedor){
        if (fornecedor.getMarcaId() != null && !fornecedor.getMarcaId().isEmpty()) {
                List<Marca> marcasEncontradas = marcaRepository.findAllById(fornecedor.getMarcaId()).stream()
                    .filter(marca -> Boolean.TRUE.equals(marca.getAtivo())).toList();

            if (marcasEncontradas.size() != fornecedor.getMarcaId().size()) {
                throw new EntidadeNaoEncontradaException("Nenhuma marca encontrada para os IDs fornecidos", fornecedor.getMarcaId());
            }

            novoFornecedor.setMarcas(marcasEncontradas);
        }

        if (fornecedor.getCategoriaId() != null && !fornecedor.getCategoriaId().isEmpty()) {
                List<Categoria> categoriasEncontradas = categoriaRepository.findAllById(fornecedor.getCategoriaId()).stream()
                    .filter(categoria -> Boolean.TRUE.equals(categoria.getAtivo())).toList();

            if(categoriasEncontradas.size() != fornecedor.getCategoriaId().size()){
                throw new EntidadeNaoEncontradaException("Nenhuma categoria encontrada para os IDs fornecidos", fornecedor.getCategoriaId());
            }

            novoFornecedor.setCategoria(categoriasEncontradas);
        }

        if (fornecedor.getEnderecoId() != null) {
            Endereco endereco = enderecoRepository.findById(fornecedor.getEnderecoId())
                    .filter(enderecoEncontrado -> Boolean.TRUE.equals(enderecoEncontrado.getAtivo()))
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado", fornecedor.getEnderecoId()));
            novoFornecedor.setEndereco(endereco);
        }
    }
}

