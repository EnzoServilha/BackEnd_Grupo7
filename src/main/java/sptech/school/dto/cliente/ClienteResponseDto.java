package sptech.school.dto.cliente;

import sptech.school.dto.endereco.EnderecoResponseDto;

import java.time.LocalDateTime;

public record ClienteResponseDto(
        Integer id,
        String nomeEmpresa,
        String nomeContato,
        String cpfCnpj,
        String telefone,
        String email,
        String observacoes,
        LocalDateTime dataCadastro,
        EnderecoResponseDto endereco
) {
}