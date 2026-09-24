package sptech.school.mapper;

import org.springframework.data.domain.Page;
import sptech.school.dto.cliente.ClienteRequestDto;
import sptech.school.dto.cliente.ClienteResponseDtoPaginacao;
import sptech.school.entity.Cliente;

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
        return cliente;
    }

    public static void atualizar(Cliente cliente, ClienteRequestDto dto) {
        cliente.setNomeEmpresa(dto.nomeEmpresa());
        cliente.setNomeContato(dto.nomeContato());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());
        cliente.setObservacoes(dto.observacoes());
    }

    public static ClienteResponseDtoPaginacao toResponseDto(Cliente cliente) {
        return new ClienteResponseDtoPaginacao(
                cliente.getId(),
                cliente.getNomeEmpresa(),
                cliente.getNomeContato(),
                cliente.getCpfCnpj(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getObservacoes(),
                cliente.getDataCadastro(),
                cliente.getEndereco() != null ? EnderecoMapper.toResponseDto(cliente.getEndereco()) : null,
                cliente.getAtivo(),
                cliente.getDesativadoPor() != null ? cliente.getDesativadoPor().getId() : null
        );
    }

    public static List<ClienteResponseDtoPaginacao> toResponseDtoList(List<Cliente> clientes) {
        return clientes.stream().map(ClienteMapper::toResponseDto).toList();
    }
    public static ClienteResponseDtoPaginacao toPaginadoDto(Page<Cliente> page) {
        return new ClienteResponseDtoPaginacao(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber(),
                page.getTotalPages()
        );
}

}

