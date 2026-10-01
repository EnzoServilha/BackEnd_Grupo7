package sptech.school.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import sptech.school.service.AutenticacaoService;


/*
 * <h3>Fluxo de autenticação no login:</h3>
 * <pre>
 *   POST /usuarios/login {email, senha}
 *     → UsuarioService.autenticar()
 *       → AuthenticationManager.authenticate()
 *         → AutenticacaoProvider.authenticate()  ← você está aqui
 *           → AutenticacaoService.loadUserByUsername()  (busca no banco)
 *           → BCrypt.matches(senhaDigitada, hashNoBanco)
 *           → retorna autenticação válida ou lança BadCredentialsException
 */

public class AutenticacaoProvider implements AuthenticationProvider {

    private final AutenticacaoService usuarioAutorizacaoService;
    private final PasswordEncoder passwordEncoder;

    public AutenticacaoProvider(AutenticacaoService usuarioAutorizacaoService, PasswordEncoder passwordEncoder) {
        this.usuarioAutorizacaoService = usuarioAutorizacaoService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autentica o usuário verificando e-mail e senha.
     *
     * <p>A senha digitada é comparada com o hash BCrypt armazenado no banco usando
     * {@link PasswordEncoder#matches}. O BCrypt inclui o salt no próprio hash,
     * então a comparação é feita diretamente (sem gerar o salt separadamente).</p>
     *
     * @param authentication objeto contendo username (e-mail) e password (senha digitada)
     * @return token de autenticação com UserDetails e authorities se as credenciais forem válidas
     * @throws BadCredentialsException se o usuário não existir ou a senha não bater
     */
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        UserDetails userDetails;
        try {
            userDetails = this.usuarioAutorizacaoService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        // Compara a senha digitada com o hash BCrypt armazenado no banco
        if (this.passwordEncoder.matches(password, userDetails.getPassword())) {
            // Credenciais válidas: retorna autenticação com authorities (perfis do usuário)
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            // Lança exceção genérica para não revelar se o erro foi no e-mail ou na senha
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }
    }

    /**
     * Indica que este provider suporta autenticação por username/password.
     *
     * <p>O Spring Security usa este método para selecionar o provider correto
     * quando há múltiplos providers registrados.</p>
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
