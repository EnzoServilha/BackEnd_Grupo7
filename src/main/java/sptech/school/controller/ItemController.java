package sptech.school.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.item.ItemRequestDto;
import sptech.school.dto.item.ItemResponseDto;
import sptech.school.entity.Item;
import sptech.school.mapper.ItemMapper;
import sptech.school.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> listarTodos() {
        List<Item> itens = itemService.listarTodos();
        if (itens.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ItemMapper.toResponseDtoList(itens));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> buscarPorId(@PathVariable Integer id) {
        Item item = itemService.buscarPorId(id);
        return ResponseEntity.ok(ItemMapper.toResponseDto(item));
    }

    @GetMapping("/codigo/{codigoInterno}")
    public ResponseEntity<ItemResponseDto> buscarPorCodigoInterno(@PathVariable String codigoInterno) {
        Item item = itemService.buscarPorCodigoInterno(codigoInterno);
        return ResponseEntity.ok(ItemMapper.toResponseDto(item));
    }

    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<ItemResponseDto>> listarPorMarca(@PathVariable String marca) {
        List<Item> itens = itemService.listarPorMarca(marca);
        if (itens.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ItemMapper.toResponseDtoList(itens));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<ItemResponseDto>> pesquisar(@RequestParam String termo) {
        List<Item> itens = itemService.pesquisar(termo);
        if (itens.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ItemMapper.toResponseDtoList(itens));
    }

    @GetMapping("/por-codigo-associado")
    public ResponseEntity<List<ItemResponseDto>> buscarPorCodigoAssociado(@RequestParam String codigo) {
        List<Item> itens = itemService.buscarPorCodigoAssociado(codigo);
        if (itens.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ItemMapper.toResponseDtoList(itens));
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> cadastrar(@RequestBody @Valid ItemRequestDto request) {
        Item item = ItemMapper.toEntity(request);
        Item salvo = itemService.cadastrar(item, request.codigosAssociadosIds(), request.itensSimilaresIds());
        return ResponseEntity.created(null).body(ItemMapper.toResponseDto(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid ItemRequestDto request) {
        Item item = ItemMapper.toEntity(request);
        Item atualizado = itemService.atualizar(id, item);
        return ResponseEntity.ok(ItemMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        itemService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{itemId}/codigos-associados/{codigoAssociadoId}")
    public ResponseEntity<ItemResponseDto> adicionarCodigoAssociado(@PathVariable Integer itemId, @PathVariable Integer codigoAssociadoId) {
        Item item = itemService.adicionarCodigoAssociado(itemId, codigoAssociadoId);
        return ResponseEntity.ok(ItemMapper.toResponseDto(item));
    }

    @DeleteMapping("/{itemId}/codigos-associados/{codigoAssociadoId}")
    public ResponseEntity<Void> removerCodigoAssociado(@PathVariable Integer itemId, @PathVariable Integer codigoAssociadoId) {
        itemService.removerCodigoAssociado(itemId, codigoAssociadoId);
        return ResponseEntity.noContent().build();
    }
}
