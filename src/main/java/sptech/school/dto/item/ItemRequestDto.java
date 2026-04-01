package sptech.school.dto.item;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ItemRequestDto {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private Integer codigoInterno;

    @NotBlank
    @Column(nullable = true)
    private String marca;

    @NotBlank
    @Column(nullable = true)
    private Integer ano;

    @NotBlank
    @Column(nullable = true, columnDefinition = "TEXT")
    private String descricao;

    @NotBlank
    @Column(nullable = false)
    private String localidade;

    @NotBlank
    @Column(nullable = false)
    private LocalDate dataCadastro;

}
