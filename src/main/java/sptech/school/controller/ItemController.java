package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sptech.school.dto.item.ItemRequestDto;
import sptech.school.dto.item.ItemResponseDto;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/Itens")
public class ItemController {

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> listarTodos() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(new ItemResponseDto(null, null, null, null, null, null, null));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ItemResponseDto> buscarPorCodigoInterno(@PathVariable Integer codigo) {
        return ResponseEntity.ok(new ItemResponseDto(null, null, null, null, null, null, null));
    }

    @GetMapping("/{marca}")
    public ResponseEntity<List<ItemResponseDto>> listarTodosPorMarca(@PathVariable String marca) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> cadastrar(@RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(new ItemResponseDto(null, null, null, null, null, null, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> atualizar(@PathVariable Integer id, @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok(new ItemResponseDto(null, null, null, null, null, null, null));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ItemResponseDto> deletar(@PathVariable Integer id) {
        return ResponseEntity.ok(new ItemResponseDto(null, null, null, null, null, null, null));
    }
}
