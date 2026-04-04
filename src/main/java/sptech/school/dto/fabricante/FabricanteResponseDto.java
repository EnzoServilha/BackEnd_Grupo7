package sptech.school.dto.fabricante;

import sptech.school.dto.endereco.EnderecoResponseDto;

import java.time.LocalDateTime;

public record FabricanteResponseDto(
        Integer id,
        String nomeContato,
        String email,
        String telefone,
        String cnpj,
        String observacoes,
        LocalDateTime dataCadastro,
        EnderecoResponseDto endereco
) {
}

