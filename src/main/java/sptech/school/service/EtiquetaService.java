package sptech.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sptech.school.entity.Item;
import sptech.school.exception.RequisicaoInvalidaException;
import sptech.school.messaging.SolicitacaoEtiquetaMessage;
import sptech.school.messaging.SolicitacaoEtiquetaPublisher;

import java.util.UUID;

/**
 * Solicita a geração de etiquetas. O PDF é gerado (e enviado por e-mail) pelo microserviço "etiquetas",
 * que consome a fila do RabbitMQ; aqui apenas se valida o item e se publica a solicitação.
 */
@Service
public class EtiquetaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EtiquetaService.class);

    private final ItemService itemService;
    private final SolicitacaoEtiquetaPublisher publisher;

    public EtiquetaService(ItemService itemService, SolicitacaoEtiquetaPublisher publisher) {
        this.itemService = itemService;
        this.publisher = publisher;
    }

    /**
     * @return a solicitação publicada, já confirmada pelo broker
     * @throws sptech.school.exception.EntidadeNaoEncontradaException se o item não existir ou estiver inativo
     * @throws RequisicaoInvalidaException                           se o item não tiver os dados mínimos da etiqueta
     * @throws sptech.school.exception.PublicacaoEtiquetaException   se a mensagem não puder ser publicada
     */
    public SolicitacaoEtiquetaMessage solicitarGeracao(Integer itemId) {
        Item item = itemService.buscarPorId(itemId);

        if (item.getCodigoInterno() == null || item.getCodigoInterno().isBlank()) {
            throw new RequisicaoInvalidaException(
                    "O item %d não possui código interno e não é possível gerar a etiqueta.".formatted(itemId));
        }

        SolicitacaoEtiquetaMessage solicitacao = new SolicitacaoEtiquetaMessage(
                UUID.randomUUID().toString(),
                itemId,
                item.getCodigoInterno(),
                item.getMarca(),
                item.getDescricao(),
                item.getDataCadastro() != null ? item.getDataCadastro().toString() : null
        );

        publisher.publicar(solicitacao);
        LOGGER.info("Geracao de etiqueta solicitada: solicitacaoId={}, itemId={}", solicitacao.solicitacaoId(), itemId);

        return solicitacao;
    }
}
