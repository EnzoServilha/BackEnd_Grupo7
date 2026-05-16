package sptech.school.src.dto.endereco;

public record EnderecoResponseDto(
        Integer id,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf
) {
}