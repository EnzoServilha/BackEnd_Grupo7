package sptech.school.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sptech.school.dto.endereco.EnderecoRequestDto;

import java.time.LocalDateTime;

public record ClienteRequestDto(

        @NotBlank
        @Size(max = 100)
        String nomeEmpresa,

        @Size(max = 150)
        String nomeContato,

        @Size(max = 18)
        String cpfCnpj,

        @Size(max = 20)
        String telefone,

        @Size(max = 100)
        String email,

        String observacoes,

        LocalDateTime dataCadastro,
        Integer enderecoId
) {
}

