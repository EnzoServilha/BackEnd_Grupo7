package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.item.ItemResponseDto;
import sptech.school.entity.Item;
import sptech.school.mapper.ItemMapper;
import sptech.school.service.ItensSimilaresService;

import java.util.List;

@RestController
@RequestMapping("/itens/{itemId}/similares")
public class ItensSimilaresController {

    private final ItensSimilaresService itensSimilaresService;

    public ItensSimilaresController(ItensSimilaresService itensSimilaresService) {
        this.itensSimilaresService = itensSimilaresService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto.ItemResumoDto>> listarSimilares(@PathVariable Integer itemId) {
        List<Item> similares = itensSimilaresService.listarSimilares(itemId);
        if (similares.isEmpty()) return ResponseEntity.noContent().build();
        List<ItemResponseDto.ItemResumoDto> dto = similares.stream()
                .map(s -> new ItemResponseDto.ItemResumoDto(s.getId(), s.getCodigoInterno(), s.getMarca()))
                .toList();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{similarId}")
    public ResponseEntity<ItemResponseDto> adicionarSimilar(@PathVariable Integer itemId, @PathVariable Integer similarId) {
        Item item = itensSimilaresService.adicionarSimilar(itemId, similarId);
        return ResponseEntity.ok(ItemMapper.toResponseDto(item));
    }

    @DeleteMapping("/{similarId}")
    public ResponseEntity<Void> removerSimilar(@PathVariable Integer itemId, @PathVariable Integer similarId) {
        itensSimilaresService.removerSimilar(itemId, similarId);
        return ResponseEntity.noContent().build();
    }
}
