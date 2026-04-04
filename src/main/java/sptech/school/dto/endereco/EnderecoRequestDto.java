package sptech.school.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDto(
        @Size(max = 10)
        String cep,

        @Size(max = 150)
        String logradouro,

        @Size(max = 20)
        String numero,

        @Size(max = 100)
        String complemento,

        @Size(max = 100)
        String bairro,

        @NotBlank
        @Size(max = 100)
        String cidade,

        @NotBlank
        @Size(max = 2)
        String uf
) {
}

