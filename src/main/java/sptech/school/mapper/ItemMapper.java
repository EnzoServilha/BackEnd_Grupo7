package sptech.school.mapper;

import sptech.school.dto.item.ItemRequestDto;
import sptech.school.dto.item.ItemResponseDto;
import sptech.school.entity.Item;

import java.util.List;

public class ItemMapper {

    public static Item toEntity(ItemRequestDto dto) {
        Item item = new Item();
        item.setCodigoInterno(dto.codigoInterno());
        item.setMarca(dto.marca());
        item.setAno(dto.ano());
        item.setDescricao(dto.descricao());
        item.setLocalizacao(dto.localizacao());
        item.setDataCadastro(dto.dataCadastro());
        return item;
    }

    public static ItemResponseDto toResponseDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getCodigoInterno(),
                item.getMarca(),
                item.getAno(),
                item.getDescricao(),
                item.getLocalizacao(),
                item.getDataCadastro()
        );
    }

    public static List<ItemResponseDto> toResponseDtoList(List<Item> itens) {
        return itens.stream().map(ItemMapper::toResponseDto).toList();
    }
}