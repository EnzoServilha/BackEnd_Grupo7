package sptech.school.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sptech.school.exception.PublicacaoEtiquetaException;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static sptech.school.config.RabbitEtiquetaConfig.EXCHANGE_ETIQUETA;
import static sptech.school.config.RabbitEtiquetaConfig.ROUTING_KEY_ETIQUETA;

/**
 * Publica as solicitações de etiqueta no RabbitMQ. Não gera o PDF nem envia e-mail: isso é responsabilidade
 * do microserviço "etiquetas", que consome a fila.
 *
 * <p>A publicação só é considerada bem-sucedida depois do publisher confirm do broker e se a mensagem foi roteada
 * para uma fila (publisher returns + mandatory). Assim, a resposta 202 da API só é devolvida quando a solicitação
 * já está guardada na fila durável.</p>
 */
@Component
public class SolicitacaoEtiquetaPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolicitacaoEtiquetaPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final Duration timeoutConfirmacao;

    public SolicitacaoEtiquetaPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${etiqueta.fila.timeout-confirmacao:5s}") Duration timeoutConfirmacao
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.timeoutConfirmacao = timeoutConfirmacao;
    }

    /**
     * @throws PublicacaoEtiquetaException se o broker estiver indisponível, recusar a mensagem, não confirmar
     *                                     a tempo ou não houver fila para a routing key
     */
    public void publicar(SolicitacaoEtiquetaMessage mensagem) {
        CorrelationData correlacao = new CorrelationData(mensagem.solicitacaoId());

        try {
            rabbitTemplate.convertAndSend(
                    EXCHANGE_ETIQUETA,
                    ROUTING_KEY_ETIQUETA,
                    mensagem,
                    persistenteComId(mensagem.solicitacaoId()),
                    correlacao
            );

            CorrelationData.Confirm confirmacao = correlacao.getFuture()
                    .get(timeoutConfirmacao.toMillis(), TimeUnit.MILLISECONDS);

            if (!confirmacao.isAck()) {
                throw falha(mensagem, "O broker recusou (nack) a mensagem: " + confirmacao.getReason(), null);
            }

            ReturnedMessage devolvida = correlacao.getReturned();
            if (devolvida != null) {
                throw falha(mensagem, "A mensagem não foi roteada para nenhuma fila: "
                        + devolvida.getReplyText() + " (exchange '" + devolvida.getExchange()
                        + "', routing key '" + devolvida.getRoutingKey() + "')", null);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw falha(mensagem, "A publicação foi interrompida antes da confirmação do broker.", e);
        } catch (TimeoutException e) {
            throw falha(mensagem, "O broker não confirmou o recebimento em " + timeoutConfirmacao + ".", e);
        } catch (ExecutionException | AmqpException e) {
            throw falha(mensagem, "Não foi possível publicar a mensagem no RabbitMQ.", e);
        }

        LOGGER.info("Solicitacao de etiqueta {} (itemId={}) publicada e confirmada pelo broker (exchange '{}', routing key '{}').",
                mensagem.solicitacaoId(), mensagem.itemId(), EXCHANGE_ETIQUETA, ROUTING_KEY_ETIQUETA);
    }

    private MessagePostProcessor persistenteComId(String solicitacaoId) {
        return message -> {
            message.getMessageProperties().setMessageId(solicitacaoId);
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        };
    }

    private PublicacaoEtiquetaException falha(SolicitacaoEtiquetaMessage mensagem, String motivo, Throwable causa) {
        LOGGER.error("Falha ao publicar a solicitacao de etiqueta {} (itemId={}): {}",
                mensagem.solicitacaoId(), mensagem.itemId(), motivo, causa);
        return new PublicacaoEtiquetaException(motivo, causa);
    }
}
