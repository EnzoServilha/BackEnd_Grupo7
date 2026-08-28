package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.item.ItemResponseDto;
import sptech.school.dto.usuario.UsuarioResponseDto;
import sptech.school.entity.Item;
import sptech.school.mapper.ItemMapper;
import sptech.school.service.ItensSimilaresService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/itens/{itemId}/similares")
public class ItensSimilaresController {

    private final ItensSimilaresService itensSimilaresService;
    private final UsuarioService usuarioService;

    public ItensSimilaresController(ItensSimilaresService itensSimilaresService, UsuarioService usuarioService) {
        this.itensSimilaresService = itensSimilaresService;
        this.usuarioService = usuarioService;
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
    public ResponseEntity<ItemResponseDto> adicionarSimilar(@PathVariable Integer itemId,
                                                            @PathVariable Integer similarId) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        Item item = itensSimilaresService.adicionarSimilar(itemId, similarId);
        return ResponseEntity.ok(ItemMapper.toResponseDto(item));
    }

    @DeleteMapping("/{similarId}")
    public ResponseEntity<Void> removerSimilar(@PathVariable Integer itemId,
                                               @PathVariable Integer similarId) {
        UsuarioResponseDto logado = usuarioService.buscarUsuarioLogado();
        usuarioService.verificarAcesso(logado);

        itensSimilaresService.removerSimilar(itemId, similarId);
        return ResponseEntity.noContent().build();
    }
}
