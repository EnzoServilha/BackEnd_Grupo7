package sptech.school.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ItemRequestDto(

        @NotBlank
        @Size(max = 50)
        String codigoInterno,

        @Size(max = 50)
        String marca,

        Integer ano,

        String descricao,

        @NotNull
        @Size(max = 100)
        String localizacao,

        LocalDateTime dataCadastro
) {
}
