package sptech.school.mapper;

import sptech.school.dto.endereco.EnderecoRequestDto;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.entity.Endereco;

import java.util.List;

public class EnderecoMapper {

    public static Endereco toEntity(EnderecoRequestDto dto) {
        Endereco endereco = new Endereco();
        endereco.setCep(dto.cep());
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setUf(dto.uf());
        return endereco;
    }

    public static EnderecoResponseDto toResponseDto(Endereco endereco) {
        return new EnderecoResponseDto(
                endereco.getId(),
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getUf(),
                endereco.getAtivo(),
                endereco.getDesativadoPor() != null ? endereco.getDesativadoPor().getId() : null
        );
    }

    public static List<EnderecoResponseDto> toResponseDtoList(List<Endereco> enderecos) {
        return enderecos.stream().map(EnderecoMapper::toResponseDto).toList();
    }
}