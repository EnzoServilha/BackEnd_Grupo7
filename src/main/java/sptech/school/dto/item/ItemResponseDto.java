package sptech.school.dto.item;

import sptech.school.dto.codigoAssociado.CodigoAssociadoResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record ItemResponseDto(
        Integer id,
        String codigoInterno,
        String marca,
        Integer ano,
        String descricao,
        String localizacao,
        LocalDateTime dataCadastro,
        List<CodigoAssociadoResponseDto> codigosAssociados,
        List<ItemResumoDto> itensSimilares
) {
    public record ItemResumoDto(
            Integer id,
            String codigoInterno,
            String marca
    ) {}
}
