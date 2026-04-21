package sptech.school.dto.fornecedor;

import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.dto.marca.MarcaResponseDto;

import java.time.LocalDateTime;
import java.util.List;

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
        List<CategoriaResponseDto> categoria,
        List<MarcaResponseDto> marcas,
        EnderecoResponseDto endereco
) {
}

