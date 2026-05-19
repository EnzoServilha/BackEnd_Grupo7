package sptech.school.src.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

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

        LocalDateTime dataCadastro,

        List<Integer> codigosAssociadosIds,

        List<Integer> itensSimilaresIds
) {
}
