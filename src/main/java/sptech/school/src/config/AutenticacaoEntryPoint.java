package sptech.school.src.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AutenticacaoEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (authException instanceof BadCredentialsException
                || authException instanceof InsufficientAuthenticationException) {
            // 401: credenciais inválidas ou ausentes (sem token JWT no header)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            // 403: autenticado, mas sem permissão suficiente para o recurso
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
