package sptech.school.dto.etiqueta;

import sptech.school.messaging.SolicitacaoEtiquetaMessage;

public record SolicitacaoEtiquetaResponseDto(
        String solicitacaoId,
        Integer itemId,
        String status,
        String mensagem
) {

    public static final String STATUS_ACEITA = "ACEITA";

    public static SolicitacaoEtiquetaResponseDto aceita(SolicitacaoEtiquetaMessage solicitacao) {
        return new SolicitacaoEtiquetaResponseDto(
                solicitacao.solicitacaoId(),
                solicitacao.itemId(),
                STATUS_ACEITA,
                "Solicitação aceita. A etiqueta será gerada e enviada por e-mail após o processamento."
        );
    }
}
