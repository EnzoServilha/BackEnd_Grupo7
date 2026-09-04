package sptech.school.dto.codigoAssociado;

public record CodigoAssociadoResponseDto(
        Integer id,
        String codigo,
        FornecedorResumo fornecedor,
        ClienteResumo cliente,
        Boolean ativo,
        Long desativadoPorId
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