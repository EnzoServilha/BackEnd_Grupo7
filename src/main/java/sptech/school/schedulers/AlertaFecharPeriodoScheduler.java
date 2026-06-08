package sptech.school.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sptech.school.entity.Periodo;
import sptech.school.observer.publisher.EventPeriodoManager;
import sptech.school.repository.PeriodoRepository;
import sptech.school.service.PeriodoService;

@Component
public class AlertaFecharPeriodoScheduler {
    private final EventPeriodoManager eventPeriodoManager;

    public AlertaFecharPeriodoScheduler(PeriodoService periodoService, EventPeriodoManager eventPeriodoManager) {
        this.eventPeriodoManager = eventPeriodoManager;
    }

    @Scheduled(cron = "0 14 1 */2 *")
    public void enviarAlerta() {
        eventPeriodoManager.notifyListeners();
    }
}
