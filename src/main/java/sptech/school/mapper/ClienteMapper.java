package sptech.school.mapper;

import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.entity.Cliente;
import sptech.school.entity.Endereco;

import java.time.LocalDateTime;
import java.util.List;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNomeEmpresa(dto.nomeEmpresa());
        cliente.setNomeContato(dto.nomeContato());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());
        cliente.setObservacoes(dto.observacoes());
//        cliente.setDataCadastro(dto.dataCadastro());
        cliente.setDataCadastro(LocalDateTime.now());

        return cliente;
    }

    public static ClienteResponseDto toResponseDto(Cliente cliente) {
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNomeEmpresa(),
                cliente.getNomeContato(),
                cliente.getCpfCnpj(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getObservacoes(),
                cliente.getDataCadastro(),
                cliente.getEndereco() != null ? EnderecoMapper.toResponseDto(cliente.getEndereco()) : null
        );
    }

    public static List<ClienteResponseDto> toResponseDtoList(List<Cliente> clientes) {
        return clientes.stream().map(ClienteMapper::toResponseDto).toList();
    }
}

