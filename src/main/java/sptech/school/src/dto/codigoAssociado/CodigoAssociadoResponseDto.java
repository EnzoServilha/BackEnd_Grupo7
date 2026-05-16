package sptech.school.src.dto.codigoAssociado;

public record CodigoAssociadoResponseDto(
        Integer id,
        String codigo,
        FornecedorResumo fornecedor,
        ClienteResumo cliente
) {
        public record FornecedorResumo(
                Integer id,
                String razaoSocial,
                String email
        ) {}

        public record ClienteResumo(
                Integer id,
                String nome,
                String email
        ) {}
}