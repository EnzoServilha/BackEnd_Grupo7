package sptech.school.mapper;

import sptech.school.dto.codigoAssociado.CodigoAssociadoResponseDto;
import sptech.school.dto.item.ItemRequestDto;
import sptech.school.dto.item.ItemResponseDto;
import sptech.school.entity.Item;

import java.util.Collections;
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
        List<CodigoAssociadoResponseDto> codigosDto = item.getCodigosAssociados() != null
                ? CodigoAssociadoMapper.toResponseDtoList(item.getCodigosAssociados())
                : Collections.emptyList();

        List<ItemResponseDto.ItemResumoDto> similaresDto = item.getItensSimilares() != null
                ? item.getItensSimilares().stream()
                    .map(s -> new ItemResponseDto.ItemResumoDto(s.getId(), s.getCodigoInterno(), s.getMarca()))
                    .toList()
                : Collections.emptyList();

        return new ItemResponseDto(
                item.getId(),
                item.getCodigoInterno(),
                item.getMarca(),
                item.getAno(),
                item.getDescricao(),
                item.getLocalizacao(),
                item.getDataCadastro(),
                codigosDto,
                similaresDto,
                item.getAtivo(),
                item.getDesativadoPor() != null ? item.getDesativadoPor().getId() : null
        );
    }

    public static List<ItemResponseDto> toResponseDtoList(List<Item> itens) {
        return itens.stream().map(ItemMapper::toResponseDto).toList();
    }
}