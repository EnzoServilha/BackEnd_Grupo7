package sptech.school.mapper;

import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDto;
import sptech.school.entity.Cliente;
import sptech.school.entity.Endereco;

import java.util.List;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());
        cliente.setObservacoes(dto.observacoes());
        cliente.setDataCadastro(dto.dataCadastro());
        if (dto.enderecoId() != null) {
            Endereco endereco = new Endereco();
            endereco.setId(dto.enderecoId());
            cliente.setEndereco(endereco);
        }
        return cliente;
    }

    public static ClienteResponseDto toResponseDto(Cliente cliente) {
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
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

