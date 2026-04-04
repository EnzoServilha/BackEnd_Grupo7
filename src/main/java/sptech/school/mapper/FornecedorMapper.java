package sptech.school.mapper;

import sptech.school.dto.fornecedor.FornecedorRequestDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.entity.Categoria;
import sptech.school.entity.Endereco;
import sptech.school.entity.Fornecedor;

import java.util.List;

public class FornecedorMapper {

    public static Fornecedor toEntity(FornecedorRequestDto dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial(dto.razaoSocial());
        fornecedor.setCnpj(dto.cnpj());
        fornecedor.setNomeContato(dto.nomeContato());
        fornecedor.setNomeEmpresa(dto.nomeEmpresa());
        fornecedor.setTelefone(dto.telefone());
        fornecedor.setEmail(dto.email());
        fornecedor.setObservacoes(dto.observacoes());
        fornecedor.setDataCadastro(dto.dataCadastro());
        if (dto.categoriaId() != null) {
            Categoria categoria = new Categoria();
            categoria.setId(dto.categoriaId());
            fornecedor.setCategoria(categoria);
        }
        if (dto.enderecoId() != null) {
            Endereco endereco = new Endereco();
            endereco.setId(dto.enderecoId());
            fornecedor.setEndereco(endereco);
        }
        return fornecedor;
    }

    public static FornecedorResponseDto toResponseDto(Fornecedor fornecedor) {
        return new FornecedorResponseDto(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getNomeContato(),
                fornecedor.getNomeEmpresa(),
                fornecedor.getTelefone(),
                fornecedor.getEmail(),
                fornecedor.getObservacoes(),
                fornecedor.getDataCadastro(),
                fornecedor.getCategoria() != null ? CategoriaMapper.toResponseDto(fornecedor.getCategoria()) : null,
                fornecedor.getEndereco() != null ? EnderecoMapper.toResponseDto(fornecedor.getEndereco()) : null
        );
    }

    public static List<FornecedorResponseDto> toResponseDtoList(List<Fornecedor> fornecedores) {
        return fornecedores.stream().map(FornecedorMapper::toResponseDto).toList();
    }
}

