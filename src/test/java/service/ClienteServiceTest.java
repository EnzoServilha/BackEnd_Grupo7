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
import sptech.school.entity.Cliente;
import sptech.school.entity.Endereco;
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
    private  EnderecoRepository enderecoRepository;

    @InjectMocks
    private ClienteService clienteService;



    @Nested
    @DisplayName("Função Atualizar")
    class AtualizarTeste{

        @Test
        @DisplayName("Deve atualizar corretamente o cliente")
        void deveAtualizar (){

            ClienteRequestDto clientePassado = new ClienteRequestDto("Empresa", "contato","18487221801","98452-7389",
                    "teste@email.com","", LocalDateTime.now(),1);

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
    }
}
