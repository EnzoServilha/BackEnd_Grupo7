package sptech.school.observer.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sptech.school.observer.listeners.EmailPeriodoListener;
import sptech.school.observer.listeners.EventListener;
import sptech.school.service.PeriodoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class EventPeriodoManager {
    private final PeriodoService periodoService;
    private final EmailPeriodoListener emailPeriodoListener;
    private List<EventListener> listeners;

    public EventPeriodoManager(PeriodoService periodoService, EmailPeriodoListener emailPeriodoListener) {
        this.periodoService = periodoService;
        this.emailPeriodoListener = emailPeriodoListener;
        listeners = List.of(
            emailPeriodoListener
        );
    }

    public void notifyListeners() {
        LocalDateTime dataUltimoPeriodo = periodoService.buscarUltimoPeriodo().getDataCriacao();

        for (EventListener listener : listeners) {
            listener.update("""
                                      Prezado Marcos,\n
                                      Para garantir a coerência dos dados de estoque, nós da CodeTracker recomendamos a realização
                                      do fechamento de um novo período, visto que o último fechamento ocorreu em: """, dataUltimoPeriodo);
        }
    }
}
