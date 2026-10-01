package sptech.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TentativaLoginService.class);

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

        LOGGER.warn("[SEGURANCA] Login bloqueado por excesso de tentativas: email={}, ip={}, bloqueadoAte={}",
                normalizarEmail(email), ip, tentativa.bloqueadoAte);
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                "Muitas tentativas de login. Tente novamente mais tarde.");
    }

    public void registrarSucesso(String email, String ip) {
        TentativaLogin removida = tentativas.remove(gerarChave(email, ip));
        if (removida != null) {
            LOGGER.info("[SEGURANCA] Login bem-sucedido apos falhas anteriores: email={}, ip={}",
                    normalizarEmail(email), ip);
        }
    }

    public void registrarFalha(String email, String ip) {
        String chave = gerarChave(email, ip);
        tentativas.compute(chave, (k, tentativaAtual) -> {
            TentativaLogin tentativa = tentativaAtual == null ? new TentativaLogin() : tentativaAtual;
            tentativa.quantidade++;

            if (tentativa.quantidade >= maxTentativas) {
                tentativa.bloqueadoAte = Instant.now().plus(tempoBloqueio);
                LOGGER.warn("[SEGURANCA] Limite de falhas de login atingido: email={}, ip={}, tentativas={}, bloqueadoAte={}",
                        normalizarEmail(email), ip, tentativa.quantidade, tentativa.bloqueadoAte);
            } else {
                LOGGER.warn("[SEGURANCA] Falha de login registrada: email={}, ip={}, tentativas={}",
                        normalizarEmail(email), ip, tentativa.quantidade);
            }

            return tentativa;
        });
    }

    private String gerarChave(String email, String ip) {
        String emailNormalizado = normalizarEmail(email);
        String ipNormalizado = ip == null ? "" : ip;
        return emailNormalizado + "|" + ipNormalizado;
    }

    private String normalizarEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static class TentativaLogin {
        private int quantidade;
        private Instant bloqueadoAte;
    }
}
