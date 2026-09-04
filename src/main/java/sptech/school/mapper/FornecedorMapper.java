package sptech.school.mapper;

import sptech.school.dto.fornecedor.FornecedorRequestDto;
import sptech.school.dto.fornecedor.FornecedorResponseDto;
import sptech.school.entity.Fornecedor;

import java.util.List;

public class FornecedorMapper {

    public static Fornecedor toEntity(FornecedorRequestDto dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial(dto.getRazaoSocial());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setNomeContato(dto.getNomeContato());
        fornecedor.setNomeEmpresa(dto.getNomeEmpresa());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setObservacoes(dto.getObservacoes());
        return fornecedor;
    }

    public static void atualizar(Fornecedor fornecedor, FornecedorRequestDto dto) {
        fornecedor.setRazaoSocial(dto.getRazaoSocial());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setNomeContato(dto.getNomeContato());
        fornecedor.setNomeEmpresa(dto.getNomeEmpresa());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setObservacoes(dto.getObservacoes());
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
                fornecedor.getCategoria() != null ? CategoriaMapper.toResponseDtoList(fornecedor.getCategoria()) : null,
                fornecedor.getMarcas() != null ? MarcaMapper.toResponseDtoList(fornecedor.getMarcas()) : null,
                fornecedor.getEndereco() != null ? EnderecoMapper.toResponseDto(fornecedor.getEndereco()) : null,
                fornecedor.getAtivo(),
                fornecedor.getDesativadoPor() != null ? fornecedor.getDesativadoPor().getId() : null
        );
    }

    public static List<FornecedorResponseDto> toResponseDtoList(List<Fornecedor> fornecedores) {
        return fornecedores.stream().map(FornecedorMapper::toResponseDto).toList();
    }
}

