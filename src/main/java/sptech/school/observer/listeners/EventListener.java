package sptech.school.observer.listeners;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public interface EventListener {
    void update(String eventType, LocalDateTime dataUltimoFechamento);

    default String formatarData(LocalDateTime agora) {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(formatador);
    }
}
