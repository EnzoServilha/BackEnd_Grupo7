package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.dto.fornecedor.ContatoEmpresaResponseDto;
import sptech.school.dto.fornecedor.FornecedorRequestDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.entity.*;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.FornecedorMapper;
import sptech.school.repository.*;
import sptech.school.util.BuscaSanitizer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;

    public FornecedorService(FornecedorRepository repository, MarcaRepository marcaRepository, CategoriaRepository categoriaRepository, EnderecoRepository enderecoRepository, ClienteRepository clienteRepository) {
        this.repository = repository;
        this.marcaRepository = marcaRepository;
        this.categoriaRepository = categoriaRepository;
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<FornecedorResponseDto> listar() {
        return FornecedorMapper.toResponseDtoList(repository.findAll().stream()
            .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo())).toList());
    }

    public List<FornecedorResponseDto> pesquisar(
            String nomeEmpresa, String nomeContato, String telefone,
            String email, String localizacao, List<String> fabricantes, List<String> categorias) {

        nomeEmpresa = (nomeEmpresa == null || nomeEmpresa.isBlank()) ? null : nomeEmpresa;
        nomeContato = (nomeContato == null || nomeContato.isBlank()) ? null : nomeContato;
        telefone = (telefone == null || telefone.isBlank()) ? null : telefone;
        email = (email == null || email.isBlank()) ? null : email;
        localizacao = (localizacao == null || localizacao.isBlank()) ? null : localizacao;

        // Normaliza listas vazias para null para não quebrar o JPQL
        List<String> fabClean = (fabricantes == null || fabricantes.isEmpty()) ? null : fabricantes;
        List<String> catClean = (categorias == null || categorias.isEmpty()) ? null : categorias;

        List<Fornecedor> fornecedores = repository.pesquisarEFiltrar(
                nomeEmpresa, nomeContato, telefone, email, localizacao, fabClean, catClean
        );

        List<FornecedorResponseDto> response = FornecedorMapper.toResponseDtoList(fornecedores);

        return response;
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
        return FornecedorMapper.toResponseDtoList(repository.findByNomeContatoContaining(
                BuscaSanitizer.escaparLike(nomeContato)).stream()
            .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo())).toList()) ;
    }

    public List<FornecedorResponseDto> buscarPorNomeEmpresa(String nomeEmpresa) {
        return FornecedorMapper.toResponseDtoList(repository.findByNomeEmpresaContaining(
                BuscaSanitizer.escaparLike(nomeEmpresa)).stream()
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
        Fornecedor fornecedorEntity = repository.findById(id)
                .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", id));

        FornecedorMapper.atualizar(fornecedorEntity, fornecedor);
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

    public List<ContatoEmpresaResponseDto> pesquisarGeral(
            String nomeEmpresa, String nomeContato, String telefone, String email, String localizacao) {

        nomeEmpresa = (nomeEmpresa == null || nomeEmpresa.isBlank()) ? null : nomeEmpresa;
        nomeContato = (nomeContato == null || nomeContato.isBlank()) ? null : nomeContato;
        telefone = (telefone == null || telefone.isBlank()) ? null : telefone;
        email = (email == null || email.isBlank()) ? null : email;
        localizacao = (localizacao == null || localizacao.isBlank()) ? null : localizacao;

        // Busca nas duas entidades
        List<Fornecedor> fornecedores = repository.pesquisarEFiltrar(
                nomeEmpresa, nomeContato, telefone, email, localizacao, null, null
        );

        List<Cliente> clientes = clienteRepository.pesquisarEFiltrar(
                nomeEmpresa, nomeContato, telefone, email, localizacao
        );

        List<ContatoEmpresaResponseDto> resultados = new ArrayList<>();

        for (Fornecedor f : fornecedores) {
            ContatoEmpresaResponseDto dto = new ContatoEmpresaResponseDto();
            dto.setId(f.getId());
            dto.setTipoEntidade("FORNECEDOR");
            dto.setNomeEmpresa(f.getNomeEmpresa());
            dto.setNomeContato(f.getNomeContato());
            dto.setTelefone(f.getTelefone());
            dto.setEmail(f.getEmail());
            if (f.getEndereco() != null) {
                dto.setCidade(f.getEndereco().getCidade());
                dto.setLogradouro(f.getEndereco().getLogradouro());
            }
            resultados.add(dto);
        }

        for (Cliente c : clientes) {
            ContatoEmpresaResponseDto dto = new ContatoEmpresaResponseDto();
            dto.setId(c.getId());
            dto.setTipoEntidade("CLIENTE");
            dto.setNomeEmpresa(c.getNomeEmpresa());
            dto.setNomeContato(c.getNomeContato());
            dto.setTelefone(c.getTelefone());
            dto.setEmail(c.getEmail());
            if (c.getEndereco() != null) {
                dto.setCidade(c.getEndereco().getCidade());
                dto.setLogradouro(c.getEndereco().getLogradouro());
            }
            resultados.add(dto);
        }

        return resultados;
    }
}
