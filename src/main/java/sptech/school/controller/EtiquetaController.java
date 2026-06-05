package sptech.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.service.EtiquetaService;

@RestController
@RequestMapping("/etiquetas")
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @GetMapping("/{itemId}/pdf")
    public ResponseEntity<byte[]> gerarEtiqueta(@PathVariable Integer itemId) {
        byte[] pdf = etiquetaService.gerarEtiquetaPdf(itemId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=etiqueta-item-" + itemId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
