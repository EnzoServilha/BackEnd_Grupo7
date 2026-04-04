package sptech.school.dto.fornecedor;

import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.dto.endereco.EnderecoResponseDto;

import java.time.LocalDateTime;

public record FornecedorResponseDto(
        Integer id,
        String razaoSocial,
        String cnpj,
        String nomeContato,
        String nomeEmpresa,
        String telefone,
        String email,
        String observacoes,
        LocalDateTime dataCadastro,
        CategoriaResponseDto categoria,
        EnderecoResponseDto endereco
) {
}

