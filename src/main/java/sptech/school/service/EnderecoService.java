package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.entity.Endereco;
import sptech.school.exception.EnderecoNaoEncontradoException;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.repository.FabricanteRepository;
import sptech.school.repository.FornecedorRepository;

import java.util.List;

@Service
public class EnderecoService {

//    @Autowired
    private EnderecoRepository enderecoRepository;
//    @Autowired
//    private FabricanteRepository fabricanteRepository;
//    @Autowired
//    private FornecedorRepository fornecedorRepository;
//    @Autowired
//    private ClienteRepository clienteRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    // ----------------------------------------------------------------------------------------------------
    // Listar todos os endereços
    // ----------------------------------------------------------------------------------------------------
    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    // ----------------------------------------------------------------------------------------------------
    // Buscar endereço por ID
    // ----------------------------------------------------------------------------------------------------
    public Endereco buscarPorId(Integer id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(String.valueOf(id)));
    }

    /*
    -------------------------------------------------------------------------------------------------------
    ADVERTÊNCIA:
    Cadastro e atualização de endereço já são feitos pelo cadastro e atualização de cliente e fornecedor
    -------------------------------------------------------------------------------------------------------
    */

    // ----------------------------------------------------------------------------------------------------
    // Cadastrar endereço
    // ----------------------------------------------------------------------------------------------------
    public Endereco cadastrar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    // ----------------------------------------------------------------------------------------------------
    // Atualizar endereço por ID
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
    public void deletar(Integer id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EnderecoNaoEncontradoException(String.valueOf(id));
        }
        enderecoRepository.deleteById(id);
    }


    // mais métodos provavelmente vão ser necessários
}