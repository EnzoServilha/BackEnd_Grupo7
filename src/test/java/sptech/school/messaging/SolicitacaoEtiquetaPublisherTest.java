package sptech.school.messaging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import sptech.school.exception.PublicacaoEtiquetaException;

import java.net.ConnectException;
import java.time.Duration;
import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
class SolicitacaoEtiquetaPublisherTest {

    private static final SolicitacaoEtiquetaMessage MENSAGEM = new SolicitacaoEtiquetaMessage(
            "6f1c2d0e-3b5a-4c7d-9e8f-0a1b2c3d4e5f", 7, "ABC-123", "Bosch", "Filtro", "2025-09-03T23:00:15");

    @Mock
    private RabbitTemplate rabbitTemplate;

    private SolicitacaoEtiquetaPublisher publisher(Duration timeout) {
        return new SolicitacaoEtiquetaPublisher(rabbitTemplate, timeout);
    }

    /** Simula o broker respondendo ao publish: o template completa o future do CorrelationData. */
    private void brokerResponde(Consumer<CorrelationData> resposta) {
        Mockito.doAnswer(invocation -> {
            resposta.accept(invocation.getArgument(4));
            return null;
        }).when(rabbitTemplate).convertAndSend(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(Object.class),
                Mockito.any(MessagePostProcessor.class), Mockito.any(CorrelationData.class));
    }

    @Test
    @DisplayName("Deve publicar na exchange e routing key do contrato quando o broker confirma (ack)")
    void devePublicarQuandoBrokerConfirma() {
        brokerResponde(correlacao -> correlacao.getFuture().complete(new CorrelationData.Confirm(true, null)));

        Assertions.assertDoesNotThrow(() -> publisher(Duration.ofSeconds(1)).publicar(MENSAGEM));

        ArgumentCaptor<CorrelationData> correlacao = ArgumentCaptor.forClass(CorrelationData.class);
        Mockito.verify(rabbitTemplate).convertAndSend(
                Mockito.eq("exchange_etiqueta"), Mockito.eq("routing_key_etiqueta"), Mockito.eq(MENSAGEM),
                Mockito.any(MessagePostProcessor.class), correlacao.capture());
        Assertions.assertEquals(MENSAGEM.solicitacaoId(), correlacao.getValue().getId());
    }

    @Test
    @DisplayName("Deve publicar mensagem persistente com messageId igual ao solicitacaoId")
    void devePublicarMensagemPersistenteComMessageId() {
        brokerResponde(correlacao -> correlacao.getFuture().complete(new CorrelationData.Confirm(true, null)));

        publisher(Duration.ofSeconds(1)).publicar(MENSAGEM);

        ArgumentCaptor<MessagePostProcessor> postProcessor = ArgumentCaptor.forClass(MessagePostProcessor.class);
        Mockito.verify(rabbitTemplate).convertAndSend(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(Object.class),
                postProcessor.capture(), Mockito.any(CorrelationData.class));
        Message processada = postProcessor.getValue().postProcessMessage(MessageBuilder.withBody(new byte[0]).build());

        Assertions.assertEquals(MENSAGEM.solicitacaoId(), processada.getMessageProperties().getMessageId());
        Assertions.assertEquals(MessageDeliveryMode.PERSISTENT, processada.getMessageProperties().getDeliveryMode());
    }

    @Test
    @DisplayName("Deve falhar quando o broker recusa a mensagem (nack)")
    void deveFalharQuandoBrokerRecusa() {
        brokerResponde(correlacao -> correlacao.getFuture().complete(new CorrelationData.Confirm(false, "fila cheia")));

        PublicacaoEtiquetaException ex = Assertions.assertThrows(PublicacaoEtiquetaException.class,
                () -> publisher(Duration.ofSeconds(1)).publicar(MENSAGEM));

        Assertions.assertTrue(ex.getMessage().contains("fila cheia"));
    }

    @Test
    @DisplayName("Deve falhar quando a mensagem não é roteada para nenhuma fila (returned)")
    void deveFalharQuandoMensagemNaoTemRota() {
        brokerResponde(correlacao -> {
            correlacao.setReturned(new ReturnedMessage(
                    MessageBuilder.withBody(new byte[0]).build(), 312, "NO_ROUTE", "exchange_etiqueta", "routing_key_etiqueta"));
            correlacao.getFuture().complete(new CorrelationData.Confirm(true, null));
        });

        PublicacaoEtiquetaException ex = Assertions.assertThrows(PublicacaoEtiquetaException.class,
                () -> publisher(Duration.ofSeconds(1)).publicar(MENSAGEM));

        Assertions.assertTrue(ex.getMessage().contains("NO_ROUTE"));
    }

    @Test
    @DisplayName("Deve falhar quando o broker não confirma dentro do tempo limite")
    void deveFalharQuandoBrokerNaoConfirma() {
        brokerResponde(correlacao -> { });

        PublicacaoEtiquetaException ex = Assertions.assertThrows(PublicacaoEtiquetaException.class,
                () -> publisher(Duration.ofMillis(50)).publicar(MENSAGEM));

        Assertions.assertInstanceOf(java.util.concurrent.TimeoutException.class, ex.getCause());
    }

    @Test
    @DisplayName("Deve falhar quando o RabbitMQ está indisponível")
    void deveFalharQuandoBrokerIndisponivel() {
        AmqpConnectException causa = new AmqpConnectException(new ConnectException("Connection refused"));
        Mockito.doThrow(causa).when(rabbitTemplate).convertAndSend(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(Object.class),
                Mockito.any(MessagePostProcessor.class), Mockito.any(CorrelationData.class));

        PublicacaoEtiquetaException ex = Assertions.assertThrows(PublicacaoEtiquetaException.class,
                () -> publisher(Duration.ofSeconds(1)).publicar(MENSAGEM));

        Assertions.assertSame(causa, ex.getCause());
    }

    @Test
    @DisplayName("Deve preservar o sinal de interrupção da thread ao ser interrompido aguardando o broker")
    void devePreservarInterrupcao() {
        brokerResponde(correlacao -> Thread.currentThread().interrupt());

        try {
            Assertions.assertThrows(PublicacaoEtiquetaException.class,
                    () -> publisher(Duration.ofSeconds(1)).publicar(MENSAGEM));
            Assertions.assertTrue(Thread.currentThread().isInterrupted());
        } finally {
            Thread.interrupted();
        }
    }
}
