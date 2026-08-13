package sptech.school.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.marca.MarcaRequestDto;
import sptech.school.dto.marca.MarcaResponseDto;
import sptech.school.service.MarcaService;
import sptech.school.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

    private MarcaService service;
    private final UsuarioService usuarioService;

    public MarcaController(MarcaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/administracao")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MarcaResponseDto>> listarAdministrativo(
            @RequestParam(defaultValue = "todos") String ativo) {
        return ResponseEntity.ok(service.listarAdministrativo(ativo));
    }

    @GetMapping("/{nome}")
    public ResponseEntity<MarcaResponseDto> buscarPorNome(@PathVariable String nome){
        return ResponseEntity.status(200).body(service.buscarPorNome(nome));
    }

    @GetMapping("/porId/{id}")
    public ResponseEntity<MarcaResponseDto> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.status(200).body(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MarcaResponseDto> criar(@RequestBody @Valid MarcaRequestDto requestDto){
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reativar(@PathVariable Integer id) {
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
