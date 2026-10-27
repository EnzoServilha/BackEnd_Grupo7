package sptech.school.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.expression.Expression;
import org.springframework.test.util.ReflectionTestUtils;
import sptech.school.messaging.SolicitacaoEtiquetaMessage;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RabbitEtiquetaConfigTest {

    private final RabbitEtiquetaConfig config = new RabbitEtiquetaConfig();

    @Test
    @DisplayName("A fila principal deve ser durável e manter a declaração original (sem argumentos), compatível com a fila já existente no broker")
    void filaPrincipalDeveSerDuravelESemArgumentos() {
        Queue fila = config.etiquetaQueue();

        Assertions.assertEquals("queue_etiqueta", fila.getName());
        Assertions.assertTrue(fila.isDurable());
        Assertions.assertFalse(fila.isExclusive());
        Assertions.assertFalse(fila.isAutoDelete());
        Assertions.assertTrue(fila.getArguments().isEmpty());
    }

    @Test
    @DisplayName("Exchange, routing key e fila devem ter os nomes do contrato com o consumer")
    void topologiaDeveSeguirContratoComConsumer() {
        Binding binding = config.etiquetaBinding(config.etiquetaQueue(), config.etiquetaExchange());

        Assertions.assertEquals("exchange_etiqueta", binding.getExchange());
        Assertions.assertEquals("routing_key_etiqueta", binding.getRoutingKey());
        Assertions.assertEquals("queue_etiqueta", binding.getDestination());
    }

    @Test
    @DisplayName("A mensagem publicada deve ser um JSON com os campos esperados pelo consumer")
    void mensagemDeveSerSerializadaNoFormatoDoContrato() throws Exception {
        SolicitacaoEtiquetaMessage mensagem = new SolicitacaoEtiquetaMessage(
                "6f1c2d0e-3b5a-4c7d-9e8f-0a1b2c3d4e5f", 7, "ABC-123", "Bosch", "Filtro de óleo", "2025-09-03T23:00:15");

        Message convertida = config.jsonConverter().toMessage(mensagem, new org.springframework.amqp.core.MessageProperties());
        JsonNode json = new ObjectMapper().readTree(new String(convertida.getBody(), StandardCharsets.UTF_8));

        Assertions.assertEquals("application/json", convertida.getMessageProperties().getContentType());
        assertThat(json.fieldNames()).toIterable()
                .containsExactlyInAnyOrder("solicitacaoId", "itemId", "codigoInterno", "marca", "descricao", "dataCadastro");
        Assertions.assertEquals("6f1c2d0e-3b5a-4c7d-9e8f-0a1b2c3d4e5f", json.get("solicitacaoId").asText());
        Assertions.assertEquals(7, json.get("itemId").asInt());
        Assertions.assertEquals("ABC-123", json.get("codigoInterno").asText());
        Assertions.assertEquals("Bosch", json.get("marca").asText());
        Assertions.assertEquals("Filtro de óleo", json.get("descricao").asText());
        Assertions.assertEquals("2025-09-03T23:00:15", json.get("dataCadastro").asText());
    }

    @Test
    @DisplayName("As propriedades do application.properties devem ativar confirms, returns e mandatory no RabbitTemplate")
    void propriedadesDevemAtivarConfirmsReturnsEMandatory() {
        new ApplicationContextRunner()
                .withInitializer(new ConfigDataApplicationContextInitializer())
                .withConfiguration(AutoConfigurations.of(RabbitAutoConfiguration.class))
                .withUserConfiguration(RabbitEtiquetaConfig.class)
                .run(contexto -> {
                    assertThat(contexto).hasNotFailed();

                    CachingConnectionFactory conexao = contexto.getBean(CachingConnectionFactory.class);
                    assertThat(conexao.isPublisherConfirms()).isTrue();
                    assertThat(conexao.isPublisherReturns()).isTrue();

                    RabbitTemplate template = contexto.getBean(RabbitTemplate.class);
                    Expression mandatory = (Expression) ReflectionTestUtils.getField(template, "mandatoryExpression");
                    assertThat(mandatory).isNotNull();
                    assertThat(mandatory.getValue()).isEqualTo(Boolean.TRUE);
                    assertThat(template.getMessageConverter()).isInstanceOf(Jackson2JsonMessageConverter.class);

                    assertThat(contexto.getBeansOfType(Queue.class)).hasSize(1);
                    assertThat(contexto.getBeansOfType(MessageConverter.class)).hasSize(1);
                });
    }
}
