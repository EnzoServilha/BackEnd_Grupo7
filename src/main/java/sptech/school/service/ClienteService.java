package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.entity.*;
import sptech.school.exception.ClienteNaoEncontradoException;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.repository.*;

import java.util.List;

@Service
public class ClienteService {

    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;
    private CodigoAssociadoRepository codigoAssociadoRepository;
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, CodigoAssociadoRepository codigoAssociadoRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.codigoAssociadoRepository = codigoAssociadoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }


    // ----------------------------------------------------------------------------------------------------
    // Listar todos os clientes (
    // ----------------------------------------------------------------------------------------------------
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(String.valueOf(id)));
    }

    // ----------------------------------------------------------------------------------------------------
    // Cadastrar cliente
    // ----------------------------------------------------------------------------------------------------
    public Cliente cadastrar(Cliente cliente) {

        cliente.setDataCadastro(java.time.LocalDateTime.now());

        if (cliente.getEndereco() != null) {
            Endereco enderecoSalvo = enderecoRepository.save(cliente.getEndereco());
            cliente.setEndereco(enderecoSalvo);
        }

        return clienteRepository.save(cliente);
    }

    // ----------------------------------------------------------------------------------------------------
    // Atualizar cliente por ID
    // ----------------------------------------------------------------------------------------------------
    public Cliente atualizar(Integer id, Cliente clienteAtualizado) {

        Cliente existente = buscarPorId(id); // Já lança exception sozinho

        existente.setNomeEmpresa(clienteAtualizado.getNomeEmpresa());
        existente.setNomeContato(clienteAtualizado.getNomeContato());
        existente.setCpfCnpj(clienteAtualizado.getCpfCnpj());
        existente.setTelefone(clienteAtualizado.getTelefone());
        existente.setEmail(clienteAtualizado.getEmail());
        existente.setObservacoes(clienteAtualizado.getObservacoes());

        // atualização de endereço
        if (clienteAtualizado.getEndereco() != null) {
            Endereco enderecoSalvo = enderecoRepository.save(clienteAtualizado.getEndereco());
            existente.setEndereco(enderecoSalvo);
        }

        return clienteRepository.save(existente);
    }

    // ----------------------------------------------------------------------------------------------------
    // Deletar cliente por ID
    // ----------------------------------------------------------------------------------------------------
    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontradoException(String.valueOf(id));
        }
        clienteRepository.deleteById(id);
    }


    // métodos mais especificados:

    // ----------------------------------------------------------------------------------------------------
    // Contar clientes
    // ----------------------------------------------------------------------------------------------------
    public long contarClientes() { return clienteRepository.count(); }


    // ----------------------------------------------------------------------------------------------------
    // Listar movimentações no estoque por cliente
    // ----------------------------------------------------------------------------------------------------
    public List<MovimentacaoEstoque> listarMovimentacoesPorCliente(Integer clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNaoEncontradoException(String.valueOf(clienteId));
        }
        return movimentacaoEstoqueRepository.findByClienteId(clienteId);
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar cliente por nome da empresa OU contato
    // ----------------------------------------------------------------------------------------------------
    public List<Cliente> buscarPorNomeOuContato(String termo) {
        return clienteRepository
                .findByNomeEmpresaContainingIgnoreCaseOrNomeContatoContainingIgnoreCase(termo, termo);
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar cliente por CPF/CNPJ
    // ----------------------------------------------------------------------------------------------------
    public Cliente buscarPorCpfCnpj(String cpfCnpj) {
        return clienteRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new ClienteNaoEncontradoException(cpfCnpj));
    }
    // ----------------------------------------------------------------------------------------------------
    // Buscar clientes por cidade
    // ----------------------------------------------------------------------------------------------------
    public List<Cliente> buscarPorCidade(String cidade) { return clienteRepository.findByCidade(cidade); }

    // ----------------------------------------------------------------------------------------------------
    // Buscar clientes por estado
    // ----------------------------------------------------------------------------------------------------
    public List<Cliente> buscarPorUf(String uf) { return clienteRepository.findByUf(uf); }


    // mais métodos aqui se necessário aqui
}
