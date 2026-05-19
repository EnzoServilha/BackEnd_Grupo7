package sptech.school.service;

import org.springframework.stereotype.Service;
import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.entity.*;
import sptech.school.exception.ClienteNaoEncontradoException;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.ClienteMapper;
import sptech.school.repository.*;

import java.util.List;

@Service
public class ClienteService {

    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;

    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(String.valueOf(id)));
    }


    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public ClienteResponseDto cadastrar(ClienteRequestDto cliente) {

        Cliente entidade = ClienteMapper.toEntity(cliente);

        preencher(entidade, cliente);

        entidade.setDataCadastro(java.time.LocalDateTime.now());


        return ClienteMapper.toResponseDto(clienteRepository.save(entidade));
    }

    public ClienteResponseDto atualizar(ClienteRequestDto cliente, Integer id) {
        if (!clienteRepository.existsById(id)) throw new EntidadeNaoEncontradaException("Cliente não encontrado", id);

        Cliente entidade = ClienteMapper.toEntity(cliente);

        entidade.setId(id);

        preencher(entidade, cliente);

        return ClienteMapper.toResponseDto(clienteRepository.save(entidade));
    }

    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNaoEncontradoException(String.valueOf(id));
        }
        clienteRepository.deleteById(id);
    }

    public void preencher(Cliente entidade, ClienteRequestDto cliente){
        if (cliente.enderecoId() != null) {
            Endereco endereco = enderecoRepository.findById(cliente.enderecoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado", cliente.enderecoId()));


            entidade.setEndereco(endereco);
        }

    }
}
