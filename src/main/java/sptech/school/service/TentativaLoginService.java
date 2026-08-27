package sptech.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TentativaLoginService {

    private final Map<String, TentativaLogin> tentativas = new ConcurrentHashMap<>();
    private final int maxTentativas;
    private final Duration tempoBloqueio;

    public TentativaLoginService(
            @Value("${security.login.max-failed-attempts:5}") int maxTentativas,
            @Value("${security.login.lock-duration-minutes:15}") long tempoBloqueioMinutos) {
        if (maxTentativas <= 0) {
            throw new IllegalArgumentException("security.login.max-failed-attempts deve ser maior que zero.");
        }
        if (tempoBloqueioMinutos <= 0) {
            throw new IllegalArgumentException("security.login.lock-duration-minutes deve ser maior que zero.");
        }

        this.maxTentativas = maxTentativas;
        this.tempoBloqueio = Duration.ofMinutes(tempoBloqueioMinutos);
    }

    public void verificarBloqueio(String email, String ip) {
        String chave = gerarChave(email, ip);
        TentativaLogin tentativa = tentativas.get(chave);

        if (tentativa == null || tentativa.bloqueadoAte == null) {
            return;
        }

        if (Instant.now().isAfter(tentativa.bloqueadoAte)) {
            tentativas.remove(chave);
            return;
        }

        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "Muitas tentativas de login. Tente novamente mais tarde.");
    }

    public void registrarSucesso(String email, String ip) {
        tentativas.remove(gerarChave(email, ip));
    }

    public void registrarFalha(String email, String ip) {
        String chave = gerarChave(email, ip);
        tentativas.compute(chave, (k, tentativaAtual) -> {
            TentativaLogin tentativa = tentativaAtual == null ? new TentativaLogin() : tentativaAtual;
            tentativa.quantidade++;

            if (tentativa.quantidade >= maxTentativas) {
                tentativa.bloqueadoAte = Instant.now().plus(tempoBloqueio);
            }

            return tentativa;
        });
    }

    private String gerarChave(String email, String ip) {
        String emailNormalizado = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        String ipNormalizado = ip == null ? "" : ip;
        return emailNormalizado + "|" + ipNormalizado;
    }

    private static class TentativaLogin {
        private int quantidade;
        private Instant bloqueadoAte;
    }
}
