package sptech.school.mapper;

import sptech.school.dto.fabricante.FabricanteRequestDto;
import sptech.school.dto.fabricante.FabricanteResponseDto;
import sptech.school.entity.Endereco;
import sptech.school.entity.Fabricante;

import java.util.List;

public class FabricanteMapper {

    public static Fabricante toEntity(FabricanteRequestDto dto) {
        Fabricante fabricante = new Fabricante();
        fabricante.setNomeContato(dto.nomeContato());
        fabricante.setEmail(dto.email());
        fabricante.setTelefone(dto.telefone());
        fabricante.setCnpj(dto.cnpj());
        fabricante.setObservacoes(dto.observacoes());
        fabricante.setDataCadastro(dto.dataCadastro());
        if (dto.enderecoId() != null) {
            Endereco endereco = new Endereco();
            endereco.setId(dto.enderecoId());
            fabricante.setEndereco(endereco);
        }
        return fabricante;
    }

    public static FabricanteResponseDto toResponseDto(Fabricante fabricante) {
        return new FabricanteResponseDto(
                fabricante.getId(),
                fabricante.getNomeContato(),
                fabricante.getEmail(),
                fabricante.getTelefone(),
                fabricante.getCnpj(),
                fabricante.getObservacoes(),
                fabricante.getDataCadastro(),
                fabricante.getEndereco() != null ? EnderecoMapper.toResponseDto(fabricante.getEndereco()) : null
        );
    }

    public static List<FabricanteResponseDto> toResponseDtoList(List<Fabricante> fabricantes) {
        return fabricantes.stream().map(FabricanteMapper::toResponseDto).toList();
    }
}

