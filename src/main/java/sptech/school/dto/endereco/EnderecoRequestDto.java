package sptech.school.dto.endereco;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDto(
        @Size(max = 10)
        @Pattern(regexp = "\\d{5}-?\\d{3}")
        String cep,

        @Size(max = 150)
        String logradouro,

        @Size(max = 20)
        // Pode null
        String numero,

        @Size(max = 100)
        // Pode null
        String complemento,

        @Size(max = 100)
        String bairro,

        @Size(max = 100)
        String cidade,

        @Size(max = 2)
        String uf
) {
}