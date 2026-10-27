package sptech.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.dto.etiqueta.SolicitacaoEtiquetaResponseDto;
import sptech.school.messaging.SolicitacaoEtiquetaMessage;
import sptech.school.service.EtiquetaService;

@RestController
@RequestMapping("/etiquetas")
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    /**
     * Solicita a geração assíncrona da etiqueta do item: 202 indica que a solicitação foi publicada na fila,
     * não que o PDF já foi gerado ou enviado por e-mail.
     */
    @PostMapping("/{itemId}/fila")
    public ResponseEntity<SolicitacaoEtiquetaResponseDto> solicitarGeracao(@PathVariable Integer itemId) {
        SolicitacaoEtiquetaMessage solicitacao = etiquetaService.solicitarGeracao(itemId);
        return ResponseEntity.accepted().body(SolicitacaoEtiquetaResponseDto.aceita(solicitacao));
    }
}
