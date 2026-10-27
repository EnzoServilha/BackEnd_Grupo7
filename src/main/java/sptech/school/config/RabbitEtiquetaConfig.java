package sptech.school.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topologia da fila de solicitações de etiqueta, compartilhada com o microserviço "etiquetas" (consumer).
 *
 * <p>O producer e o consumer declaram a mesma exchange, fila e binding, então a ordem de subida dos serviços não
 * importa. A fila é durável e sem argumentos, como já estava declarada no broker antes desta mudança: o RabbitMQ não
 * permite alterar os argumentos de uma fila existente, e qualquer diferença causa PRECONDITION_FAILED. Mantenha as
 * duas configurações iguais. A DLQ (mensagens que o consumer não conseguiu processar) é de responsabilidade do
 * consumer.</p>
 */
@Configuration
public class RabbitEtiquetaConfig {

    public static final String EXCHANGE_ETIQUETA = "exchange_etiqueta";
    public static final String QUEUE_ETIQUETA = "queue_etiqueta";
    public static final String ROUTING_KEY_ETIQUETA = "routing_key_etiqueta";

    @Bean
    public DirectExchange etiquetaExchange() {
        return new DirectExchange(EXCHANGE_ETIQUETA);
    }

    @Bean
    public Queue etiquetaQueue() {
        return QueueBuilder
                .durable(QUEUE_ETIQUETA)
                .build();
    }

    @Bean
    public Binding etiquetaBinding(Queue etiquetaQueue, DirectExchange etiquetaExchange) {
        return BindingBuilder
                .bind(etiquetaQueue)
                .to(etiquetaExchange)
                .with(ROUTING_KEY_ETIQUETA);
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
