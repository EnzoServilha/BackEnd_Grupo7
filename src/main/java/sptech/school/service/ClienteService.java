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
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;

    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(String.valueOf(id)));
    }

    public Cliente buscarPorIdIncluindoInativo(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(String.valueOf(id)));
    }


    public List<Cliente> listarTodos() {
        return clienteRepository.findAllByAtivoTrue();
    }

    public List<Cliente> listarAdministrativo(String ativo) {
        return FiltroAtivacao.filtrar(clienteRepository.findAll(), ativo);
    }

    public ClienteResponseDto cadastrar(ClienteRequestDto cliente) {

        Cliente entidade = ClienteMapper.toEntity(cliente);

        preencher(entidade, cliente);

        entidade.setDataCadastro(java.time.LocalDateTime.now());


        return ClienteMapper.toResponseDto(clienteRepository.save(entidade));
    }

    public ClienteResponseDto atualizar(ClienteRequestDto cliente, Integer id) {
        Cliente entidade = clienteRepository.findById(id)
            .filter(encontrado -> Boolean.TRUE.equals(encontrado.getAtivo()))
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado", id));

        ClienteMapper.atualizar(entidade, cliente);
        preencher(entidade, cliente);

        return ClienteMapper.toResponseDto(clienteRepository.save(entidade));
    }

    @Transactional
    public void desativar(Integer id, Usuario usuarioExecutor) {
        Cliente cliente = buscarPorIdIncluindoInativo(id);
        cliente.desativar(usuarioExecutor);
        clienteRepository.save(cliente);
    }

    public void deletar(Integer id) {
        desativar(id, null);
    }

    @Transactional
    public void reativar(Integer id) {
        Cliente cliente = buscarPorIdIncluindoInativo(id);
        cliente.reativar();
        clienteRepository.save(cliente);
    }

    public void preencher(Cliente entidade, ClienteRequestDto cliente){
        if (cliente.enderecoId() != null) {
            Endereco endereco = enderecoRepository.findById(cliente.enderecoId())
                .filter(enderecoEncontrado -> Boolean.TRUE.equals(enderecoEncontrado.getAtivo()))
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Endereço não encontrado", cliente.enderecoId()));


            entidade.setEndereco(endereco);
        }

    }
}
