package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.entity.Cliente;
import sptech.school.entity.Endereco;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.mapper.ClienteMapper;
import sptech.school.repository.ClienteRepository;
import sptech.school.repository.EnderecoRepository;
import sptech.school.service.ClienteService;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private ClienteService clienteService;


    @Nested
    @DisplayName("Função Atualizar")
    class AtualizarTeste {

        @Test
        @DisplayName("Deve atualizar corretamente o cliente")
        void deveAtualizar() {

            ClienteRequestDto clientePassado = new ClienteRequestDto("Empresa", "contato", "18487221801", "98452-7389",
                    "teste@email.com", "", LocalDateTime.now(), 1);

            Integer id = 1;

            Cliente entidade = ClienteMapper.toEntity(clientePassado);

            entidade.setId(id);


            Endereco endereco = new Endereco();

            endereco.setId(1);

            Mockito.when(clienteRepository.existsById(1))
                    .thenReturn(true);

            Mockito.when(enderecoRepository.findById(clientePassado.enderecoId()))
                    .thenReturn(Optional.of(endereco));


            Mockito.when(clienteRepository.save(Mockito.any(Cliente.class)))
                    .thenReturn(entidade);


            Assertions.assertEquals(ClienteMapper.toResponseDto(entidade), clienteService.atualizar(clientePassado, id));

        }

        @Test
        @DisplayName("Deve lançar EntidadeNaoEncontradaException se nãoa char o cliente")
        void lancarEntidadeNaoEncontradaException() {

            Integer id = 1;

            ClienteRequestDto clientePassado = new ClienteRequestDto(
                    "Empresa de Teste LTDA",
                    "João da Silva",
                    "18487221801",
                    "11984527389",
                    "teste@email.com",
                    "Observação de teste",
                    LocalDateTime.now(),
                    id
            );

            Mockito.when(clienteRepository.existsById(2))
                    .thenReturn(false);

            Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> clienteService.atualizar(clientePassado, 2));

        }

    }

    @Nested
    @DisplayName("Função cadastrar")
    class cadastrarCliente {
        @Test
        @DisplayName("Deve cadastrar corretamente")
        void cadastrarCorretamente() {

            Integer id = 1;


            ClienteRequestDto clientePassado = new ClienteRequestDto(
                    "Empresa de Teste LTDA",
                    "João da Silva",
                    "18487221801",
                    "11984527389",
                    "teste@email.com",
                    "Observação de teste",
                    LocalDateTime.now(),
                    id
            );

            Endereco endereco = new Endereco();

            endereco.setId(1);

            Cliente entidade = ClienteMapper.toEntity(clientePassado);


            Mockito.when(enderecoRepository.findById(clientePassado.enderecoId()))
                    .thenReturn(Optional.of(endereco));

            Mockito.when(clienteRepository.save(Mockito.any(Cliente.class)))
                    .thenReturn(entidade);


            ClienteResponseDto resposta = ClienteMapper.toResponseDto(entidade);


            Assertions.assertEquals(resposta, clienteService.cadastrar(clientePassado));

        }


    }

    @Nested
    @DisplayName("Teste do metodo preencher")
    class preencher {


        @Test
        @DisplayName("O preencher deve lançar EntidadeNaoEncontradaException se não achar o endereço correto")
        void preencherComEnderecoErrado() {

            ClienteRequestDto clientePassado = new ClienteRequestDto("Empresa", "contato", "18487221801", "98452-7389",
                    "teste@email.com", "", LocalDateTime.now(), 1);

            Integer id = 1;

            Cliente entidade = ClienteMapper.toEntity(clientePassado);

            entidade.setId(id);

            Mockito.when(enderecoRepository.findById(clientePassado.enderecoId()))
                    .thenReturn(Optional.empty());


            Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> clienteService.preencher(entidade, clientePassado));

        }

        @Test
        @DisplayName("O preencher não deve fazer nada se o enderecoId for null")
        void preencherComEnderecoNull() {

            ClienteRequestDto clientePassado = new ClienteRequestDto("Empresa", "contato", "18487221801", "98452-7389",
                    "teste@email.com", "", LocalDateTime.now(), null);

            Integer id = 1;

            Cliente entidade = ClienteMapper.toEntity(clientePassado);

            entidade.setId(id);


            clienteService.preencher(entidade, clientePassado);

            Assertions.assertNull(entidade.getEndereco());
        }
    }
}
