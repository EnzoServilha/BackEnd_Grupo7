package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.categoria.CategoriaRequestDto;
import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.service.CategoriaService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private CategoriaService service;
    private final UsuarioService usuarioService;

    public CategoriaController(CategoriaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/administracao")
    public ResponseEntity<List<CategoriaResponseDto>> listarAdministrativo(
            @RequestParam(defaultValue = "todos") String ativo) {
        return ResponseEntity.ok(service.listarAdministrativo(ativo));
    }

    @GetMapping("/{nome}")
    public ResponseEntity<CategoriaResponseDto> buscarPorNome(@PathVariable String nome){
        return ResponseEntity.status(200).body(service.buscarPorNome(nome));
    }

    @GetMapping("/porId/{id}")
    public ResponseEntity<CategoriaResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> criar(@RequestBody @Valid CategoriaRequestDto requestDto){
        return ResponseEntity.status(200).body(service.criar(requestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Null> deletar(@PathVariable Integer id, Authentication authentication){
        service.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}/desativacao")
    public ResponseEntity<Void> desativar(@PathVariable Integer id, Authentication authentication) {
        service.desativar(id, usuarioService.buscarAtivoPorEmail(authentication.getName()));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativacao")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
