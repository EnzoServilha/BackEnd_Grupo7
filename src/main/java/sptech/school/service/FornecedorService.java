package sptech.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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
import java.util.stream.Collectors;

@Service
public class FornecedorService {
    private final FornecedorRepository repository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;
    private final MarcaService marcaService;
    private final CategoriaService categoriaService;

    public FornecedorService(FornecedorRepository repository, MarcaRepository marcaRepository,
                             CategoriaRepository categoriaRepository, EnderecoRepository enderecoRepository,
                             ClienteRepository clienteRepository, MarcaService marcaService,
                             CategoriaService categoriaService) {
        this.repository = repository;
        this.marcaRepository = marcaRepository;
        this.categoriaRepository = categoriaRepository;
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
        this.marcaService = marcaService;
        this.categoriaService = categoriaService;
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

        return FornecedorMapper.toResponseDtoList(fornecedores);
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
                .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo())).toList());
    }

    public List<FornecedorResponseDto> buscarPorNomeEmpresa(String nomeEmpresa) {
        return FornecedorMapper.toResponseDtoList(repository.findByNomeEmpresaContaining(
                        BuscaSanitizer.escaparLike(nomeEmpresa)).stream()
                .filter(fornecedor -> Boolean.TRUE.equals(fornecedor.getAtivo())).toList());
    }

    public List<FornecedorResponseDto> listarPorCategoria(Integer idCategoria) {
        return FornecedorMapper.toResponseDtoList(repository.findByIdCategoria(idCategoria));
    }

    public List<FornecedorResponseDto> listarPorMarca(Integer idMarca) {
        return FornecedorMapper.toResponseDtoList(repository.findByIdMarca(idMarca));
    }

    // @Transactional: se algo falhar depois de criar marca/categoria, tudo é desfeito junto.
    @Transactional
    public FornecedorResponseDto criar(FornecedorRequestDto fornecedor) {
        validarCnpjDisponivel(fornecedor.getCnpj(), null);

        Fornecedor novoFornecedor = FornecedorMapper.toEntity(fornecedor);

        novoFornecedor.setDataCadastro(LocalDateTime.now());

        preencher(novoFornecedor, fornecedor);

        return FornecedorMapper.toResponseDto(repository.save(novoFornecedor));
    }

    @Transactional
    public FornecedorResponseDto atualizar(FornecedorRequestDto fornecedor, Integer id) {
        Fornecedor fornecedorEntity = repository.findById(id)
                .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado", id));

        validarCnpjDisponivel(fornecedor.getCnpj(), id);

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

    public void preencher(Fornecedor novoFornecedor, FornecedorRequestDto fornecedor) {
        // MARCAS: IDs existentes + nomes novos (buscados ou criados)
        if (fornecedor.getMarcaId() != null || temItens(fornecedor.getMarcasNovas())) {
            List<Marca> resultado = new ArrayList<>();

            if (fornecedor.getMarcaId() != null && !fornecedor.getMarcaId().isEmpty()) {
                List<Integer> ids = fornecedor.getMarcaId().stream().distinct().toList();
                List<Marca> encontradas = marcaRepository.findAllById(ids).stream()
                        .filter(marca -> Boolean.TRUE.equals(marca.getAtivo()))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (encontradas.size() != ids.size()) {
                    throw new EntidadeNaoEncontradaException("Nenhuma marca encontrada para os IDs fornecidos", ids);
                }
                resultado.addAll(encontradas);
            }

            if (fornecedor.getMarcasNovas() != null) {
                for (String nome : fornecedor.getMarcasNovas()) {
                    if (nome == null || nome.isBlank()) continue;
                    Marca marca = marcaService.buscarOuCriarPorNome(nome);
                    if (resultado.stream().noneMatch(m -> m.getId().equals(marca.getId()))) {
                        resultado.add(marca);
                    }
                }
            }

            novoFornecedor.setMarcas(resultado);
        }

        // CATEGORIAS: IDs existentes + nomes novos (buscados ou criados)
        if (fornecedor.getCategoriaId() != null || temItens(fornecedor.getCategoriasNovas())) {
            List<Categoria> resultado = new ArrayList<>();

            if (fornecedor.getCategoriaId() != null && !fornecedor.getCategoriaId().isEmpty()) {
                List<Integer> ids = fornecedor.getCategoriaId().stream().distinct().toList();
                List<Categoria> encontradas = categoriaRepository.findAllById(ids).stream()
                        .filter(categoria -> Boolean.TRUE.equals(categoria.getAtivo()))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (encontradas.size() != ids.size()) {
                    throw new EntidadeNaoEncontradaException("Nenhuma categoria encontrada para os IDs fornecidos", ids);
                }
                resultado.addAll(encontradas);
            }

            if (fornecedor.getCategoriasNovas() != null) {
                for (String nome : fornecedor.getCategoriasNovas()) {
                    if (nome == null || nome.isBlank()) continue;
                    Categoria categoria = categoriaService.buscarOuCriarPorNome(nome);
                    if (resultado.stream().noneMatch(c -> c.getId().equals(categoria.getId()))) {
                        resultado.add(categoria);
                    }
                }
            }

            novoFornecedor.setCategoria(resultado);
        }

        if (fornecedor.getEnderecoId() != null) {
            Endereco endereco = enderecoRepository.findById(fornecedor.getEnderecoId())
                    .filter(enderecoEncontrado -> Boolean.TRUE.equals(enderecoEncontrado.getAtivo()))
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado", fornecedor.getEnderecoId()));
            novoFornecedor.setEndereco(endereco);
        } else if (novoFornecedor.getEndereco() != null) {
            novoFornecedor.setEndereco(null);
        }
    }

    // Evita o 500 por constraint UNIQUE: devolve 409 com mensagem clara.
    private void validarCnpjDisponivel(String cnpj, Integer idAtual) {
        if (cnpj == null || cnpj.isBlank()) return;

        repository.findByCnpj(cnpj.trim())
                .filter(existente -> !existente.getId().equals(idAtual))
                .ifPresent(existente -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            Boolean.TRUE.equals(existente.getAtivo())
                                    ? "Já existe um fornecedor cadastrado com este CNPJ."
                                    : "Já existe um fornecedor inativo com este CNPJ. Reative-o em vez de cadastrar novamente.");
                });
    }

    private boolean temItens(List<String> lista) {
        return lista != null && lista.stream().anyMatch(s -> s != null && !s.isBlank());
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