package sptech.school.src.dto.status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StatusRequestDto(
        @NotBlank
        @Size(max = 45)
        String nome

) {
}

