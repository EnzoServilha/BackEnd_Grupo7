package sptech.school.exception;

/**
 * A solicitação de etiqueta não pôde ser entregue ao RabbitMQ (broker fora do ar, mensagem recusada ou sem rota).
 * A causa técnica fica no log; ao cliente é devolvido 503.
 */
public class PublicacaoEtiquetaException extends RuntimeException {
    public PublicacaoEtiquetaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
