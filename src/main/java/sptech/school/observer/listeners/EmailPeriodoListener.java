package sptech.school.observer.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailPeriodoListener implements EventListener{

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
            System.out.println("TESTANDO");
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(emailRemetente);
            simpleMailMessage.setTo("enzo.servilha@sptech.school");
            simpleMailMessage.setText(mensagem);
            javaMailSender.send(simpleMailMessage);
            System.out.println("FOI");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}