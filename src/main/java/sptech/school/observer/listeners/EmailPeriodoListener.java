package sptech.school.observer.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailPeriodoListener implements EventListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailPeriodoListener.class);

    private final JavaMailSender javaMailSender;

    public EmailPeriodoListener(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String emailRemetente;

    @Override
    public void update(String eventType, LocalDateTime dataUltimoFechamento) {
        try {
            String mensagem = eventType + formatarData(dataUltimoFechamento);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(emailRemetente);
            simpleMailMessage.setTo("enzo.servilha@sptech.school");
            simpleMailMessage.setText(mensagem);
            javaMailSender.send(simpleMailMessage);
            LOGGER.info("[MONITORAMENTO] Alerta de fechamento de periodo enviado com sucesso.");
        }
        catch (Exception e) {
            LOGGER.error("[MONITORAMENTO] Falha ao enviar alerta de fechamento de periodo.", e);
        }
    }
}