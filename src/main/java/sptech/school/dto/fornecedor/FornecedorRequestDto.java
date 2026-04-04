package sptech.school.dto.fornecedor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record FornecedorRequestDto(
        @NotBlank
        @Size(max = 150)
        String razaoSocial,

        @Size(max = 18)
        String cnpj,

        @Size(max = 100)
        String nomeContato,

        @Size(max = 100)
        String nomeEmpresa,

        @Size(max = 20)
        String telefone,

        @Size(max = 100)
        String email,

        String observacoes,

        LocalDateTime dataCadastro,

        Integer categoriaId,

        Integer enderecoId
) {
}

