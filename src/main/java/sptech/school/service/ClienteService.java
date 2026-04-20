package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.entity.Cliente;
import sptech.school.exception.ClienteNaoEncontradoException;
import sptech.school.repository.*;

import java.util.List;

@Service
public class ClienteService {

//    @Autowired
    private ClienteRepository clienteRepository;
//    @Autowired
    private CodigoAssociadoRepository codigoAssociadoRepository;
//    @Autowired
    private EnderecoRepository enderecoRepository;
//    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public ClienteService(ClienteRepository clienteRepository, CodigoAssociadoRepository codigoAssociadoRepository, EnderecoRepository enderecoRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.clienteRepository = clienteRepository;
        this.codigoAssociadoRepository = codigoAssociadoRepository;
        this.enderecoRepository = enderecoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }


    // ----------------------------------------------------------------------------------------------------
    // Listar todos os clientes
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
    // TODO: Cadastrar cliente
    // ----------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------
    // TODO: Atualizar cliente por ID
    // ----------------------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------------------
    // Deletar cliente por ID
    // ----------------------------------------------------------------------------------------------------
    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontradoException(String.valueOf(id));
        }
        clienteRepository.deleteById(id);
    }


    // mais métodos provavelmente vão ser necessários
}
