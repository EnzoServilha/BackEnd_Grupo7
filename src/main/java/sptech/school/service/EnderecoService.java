package sptech.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.school.entity.Endereco;
import sptech.school.exception.EnderecoNaoEncontradoException;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.repository.FornecedorRepository;

import java.util.List;

@Service
public class EnderecoService {

    private EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;
    private final FornecedorRepository fornecedorRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, ClienteRepository clienteRepository,
                           FornecedorRepository fornecedorRepository) {
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    // ----------------------------------------------------------------------------------------------------
    // Listar todos os endereços
    // ----------------------------------------------------------------------------------------------------
    public List<Endereco> listarTodos() {
        return enderecoRepository.findAllByAtivoTrue();
    }

    public List<Endereco> listarAdministrativo(String ativo) {
        return FiltroAtivacao.filtrar(enderecoRepository.findAll(), ativo);
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    public Endereco buscarPorId(Integer id) {
        return enderecoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(String.valueOf(id)));
    }

    /*
    -------------------------------------------------------------------------------------------------------
    ADVERTÊNCIA:
    Cadastro e atualização de endereço já são feitos pelo cadastro e atualização de cliente e fornecedor
    -------------------------------------------------------------------------------------------------------
    */

    // ----------------------------------------------------------------------------------------------------
    // Cadastrar endereço (só de enfeite)
    // ----------------------------------------------------------------------------------------------------
    public Endereco cadastrar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    // ----------------------------------------------------------------------------------------------------
    // Atualizar endereço por ID (só de enfeite)
    // ----------------------------------------------------------------------------------------------------
    public Endereco atualizar(Integer id, Endereco enderecoAtualizado) {

        Endereco existente = buscarPorId(id);

        existente.setCep(enderecoAtualizado.getCep());
        existente.setLogradouro(enderecoAtualizado.getLogradouro());
        existente.setNumero(enderecoAtualizado.getNumero());
        existente.setComplemento(enderecoAtualizado.getComplemento());
        existente.setBairro(enderecoAtualizado.getBairro());
        existente.setCidade(enderecoAtualizado.getCidade());
        existente.setUf(enderecoAtualizado.getUf());

        return enderecoRepository.save(existente);
    }

    // ----------------------------------------------------------------------------------------------------
    // Deletar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    @Transactional
    public void desativar(Integer id, sptech.school.entity.Usuario usuarioExecutor) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(String.valueOf(id)));
        if (Boolean.TRUE.equals(endereco.getAtivo())
                && (clienteRepository.existsByEnderecoIdAndAtivoTrue(id)
                || fornecedorRepository.existsByEnderecoIdAndAtivoTrue(id))) {
            throw new EntidadeConflitanteException("Endereço em uso por cliente ou fornecedor ativo");
        }
        endereco.desativar(usuarioExecutor);
        enderecoRepository.save(endereco);
    }

    public void deletar(Integer id) {
        desativar(id, null);
    }

    @Transactional
    public void reativar(Integer id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(String.valueOf(id)));
        endereco.reativar();
        enderecoRepository.save(endereco);
    }

    // mais métodos aqui se necessário aqui
}