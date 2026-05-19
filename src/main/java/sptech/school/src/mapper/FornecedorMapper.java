package sptech.school.src.mapper;

import sptech.school.src.dto.fornecedor.FornecedorRequestDto;
import sptech.school.src.dto.fornecedor.FornecedorResponseDto;
import sptech.school.src.entity.Fornecedor;

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
        fornecedor.setDataCadastro(dto.getDataCadastro());


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
                fornecedor.getCategoria() != null ? CategoriaMapper.toResponseDtoList(fornecedor.getCategoria()) : null,
                fornecedor.getMarcas() != null ? MarcaMapper.toResponseDtoList(fornecedor.getMarcas()) : null,
                fornecedor.getEndereco() != null ? EnderecoMapper.toResponseDto(fornecedor.getEndereco()) : null
        );
    }

    public static List<FornecedorResponseDto> toResponseDtoList(List<Fornecedor> fornecedores) {
        return fornecedores.stream().map(FornecedorMapper::toResponseDto).toList();
    }
}

