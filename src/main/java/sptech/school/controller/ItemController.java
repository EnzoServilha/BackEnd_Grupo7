package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.dto.item.ItemResponseDto;

import java.util.List;

@RestController
@RequestMapping("/pecas")
public class ItemController {

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> listarTodosItem() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping
    public ResponseEntity<ItemResponseDto> buscarItemPorCodigoInterno(Integer codigo) {
        return ResponseEntity.ok(new ItemResponseDto());
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> listarItemPorMarca(String marca) {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping
    public ResponseEntity<ItemResponseDto> buscarPorCodigoAssociado() {
        return ResponseEntity.ok(new ItemResponseDto());
    }
}
