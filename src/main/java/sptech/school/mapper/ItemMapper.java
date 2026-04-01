package sptech.school.mapper;

import sptech.school.dto.item.ItemRequestDto;
import sptech.school.dto.item.ItemResponseDto;
import sptech.school.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {

    // DTO de entrada (Request) → Entidade
    // Usado no cadastro e atualização
    public static Item toEntity(ItemRequestDto dto) {
        Item item = new Item();
        item.setCodigoInterno(dto.codigoInterno()); // record: sem "get", acessa direto pelo nome
        item.setMarca(dto.marca());
        item.setAno(dto.ano());
        item.setDescricao(dto.descricao());
        item.setLocalidade(dto.localizacao());
        item.setDataCadastro(dto.dataCadastro() != null ? dto.dataCadastro().toLocalDate() : null);
        return item;
    }

    // Entidade → DTO de saída (Response)
    // Usado ao retornar dados para o cliente
    public static ItemResponseDto toResponseDto(Item item) {
        return new ItemResponseDto(       // record: só tem construtor com todos os campos
                item.getId(),
                item.getCodigoInterno(),
                item.getMarca(),
                item.getAno(),
                item.getDescricao(),
                item.getLocalidade(),
                item.getDataCadastro() != null
                        ? item.getDataCadastro().atStartOfDay()
                        : null
        );
    }

    // Lista de Entidades → Lista de DTOs de saída
    public static List<ItemResponseDto> toResponseDtoList(List<Item> itens) {
        return itens.stream()
                .map(ItemMapper::toResponseDto)
                .toList();
    }
}

