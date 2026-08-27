package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.item.ItemRequestDto;
import sptech.school.dto.item.ItemResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.entity.Item;
import sptech.school.mapper.ItemMapper;
import sptech.school.service.ItemService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/itens")
@Validated
public class ItemController {

    private final ItemService itemService;
    private final UsuarioService usuarioService;

    public ItemController(ItemService itemService, UsuarioService usuarioService) {
        this.itemService = itemService;
        this.usuarioService = usuarioService;
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
    public ResponseEntity<ItemResponseDto> buscarPorCodigoInterno(
            @PathVariable
            @NotBlank
            @Size(max = 50)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String codigoInterno) {
        Item item = itemService.buscarPorCodigoInterno(codigoInterno);
        return ResponseEntity.ok(ItemMapper.toResponseDto(item));
    }

    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<ItemResponseDto>> listarPorMarca(
            @PathVariable
            @NotBlank
            @Size(max = 50)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String marca) {
        List<Item> itens = itemService.listarPorMarca(marca);
        if (itens.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ItemMapper.toResponseDtoList(itens));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<ItemResponseDto>> pesquisar(
            @RequestParam
            @NotBlank
            @Size(max = 100)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String termo) {
        List<Item> itens = itemService.pesquisar(termo);
        if (itens.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ItemMapper.toResponseDtoList(itens));
    }

    @GetMapping("/por-codigo-associado")
    public ResponseEntity<List<ItemResponseDto>> buscarPorCodigoAssociado(
            @RequestParam
            @NotBlank
            @Size(max = 100)
            @Pattern(regexp = "^[\\p{L}\\p{N}\\s._@-]+$")
            String codigo) {
        List<Item> itens = itemService.buscarPorCodigoAssociado(codigo);
        if (itens.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(ItemMapper.toResponseDtoList(itens));
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> cadastrar(@RequestBody @Valid ItemRequestDto request) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        Item item = ItemMapper.toEntity(request);
        Item salvo = itemService.cadastrar(item, request.codigosAssociadosIds(), request.itensSimilaresIds());
        return ResponseEntity.created(null).body(ItemMapper.toResponseDto(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ItemRequestDto request) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        Item item = ItemMapper.toEntity(request);
        Item atualizado = itemService.atualizar(id, item);
        return ResponseEntity.ok(ItemMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        itemService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{itemId}/codigos-associados/{codigoAssociadoId}")
    public ResponseEntity<ItemResponseDto> adicionarCodigoAssociado(
            @PathVariable Integer itemId,
            @PathVariable Integer codigoAssociadoId) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        Item item = itemService.adicionarCodigoAssociado(itemId, codigoAssociadoId);
        return ResponseEntity.ok(ItemMapper.toResponseDto(item));
    }

    @DeleteMapping("/{itemId}/codigos-associados/{codigoAssociadoId}")
    public ResponseEntity<Void> removerCodigoAssociado(@PathVariable Integer itemId,
                                                       @PathVariable Integer codigoAssociadoId) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        itemService.removerCodigoAssociado(itemId, codigoAssociadoId);
        return ResponseEntity.noContent().build();
    }
}
