package sptech.school.dto.fabricante;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record FabricanteRequestDto(
        @Size(max = 100)
        String nomeContato,

        @Size(max = 100)
        String email,

        @Size(max = 20)
        String telefone,

        @Size(max = 18)
        String cnpj,

        String observacoes,

        LocalDateTime dataCadastro,

        Integer enderecoId
) {
}

