package sptech.school.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GerenciadorTokenJwt {

    private static final int TAMANHO_MINIMO_CHAVE_HS256_BYTES = 32;

     //A chave deve ser armazenada em Base64 no arquivo de propriedades</li>
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tempo de validade do token em <b>segundos</b>.
     * Exemplo: {@code 3600} = 1 hora.
    */
    @Value("${jwt.validity}")
    private long jwtTokenValidity;

    /**
     * Gera um token JWT assinado a partir de uma autenticação bem-sucedida.
     *
     * <p>O token gerado contém os seguintes claims no payload:</p>
     * <ul>
     *   <li>{@code sub} (subject): e-mail/username do usuário autenticado</li>
     *   <li>{@code authorities}: perfis/roles separados por vírgula (ex: "ROLE_USER,ROLE_ADMIN")</li>
     *   <li>{@code iat} (issued at): momento em que o token foi emitido</li>
     *   <li>{@code exp} (expiration): momento em que o token expira</li>
     * </ul>
     *
     * @param authentication objeto de autenticação gerado pelo Spring Security após validar as credenciais
     * @return token JWT compactado como String (formato: header.payload.signature)
     */
    public String generateToken(final Authentication authentication) {
        // Coleta todas as authorities (roles/perfis) do usuário separadas por vírgula
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(authentication.getName())           // claim "sub": quem é o usuário
                .claim("authorities", authorities)           // claim customizado: perfis do usuário
                .issuedAt(new Date(System.currentTimeMillis()))                             // claim "iat"
                .expiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1_000)) // claim "exp"
                .signWith(parseSecret(), Jwts.SIG.HS256)     // assina com HMAC-SHA256
                .compact();                                  // serializa para String
    }

    /**
     * Extrai o username (e-mail) do payload do token.
     *
     * @param token token JWT
     * @return username armazenado no claim "sub" (subject)
     */
    public String getUsernameFromToken(String token) {
        return getClaimForToken(token, Claims::getSubject);
    }

    /**
     * Extrai a data de expiração do payload do token.
     *
     * @param token token JWT
     * @return data de expiração (claim "exp")
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimForToken(token, Claims::getExpiration);
    }

    /**
     * Valida se o token é válido para o usuário informado.
     *
     * <p>A validação verifica dois critérios:</p>
     * <ol>
     *   <li>O subject (username) do token corresponde ao usuário carregado do banco</li>
     *   <li>O token não está expirado</li>
     * </ol>
     *
     * <p><b>Nota:</b> o JJWT já verifica a assinatura automaticamente ao parsear o token
     * em {@link #getAllClaimsFromToken}. Se a assinatura for inválida, uma exceção é lançada
     * antes mesmo de chegar neste método.</p>
     *
     * @param token       token JWT recebido na requisição
     * @param userDetails dados do usuário carregados do banco de dados
     * @return {@code true} se o token for válido para o usuário informado
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extrai um claim específico do token, aplicando uma função de resolução.
     *
     * <p>Utiliza o padrão funcional para evitar duplicação de código ao extrair
     * diferentes tipos de claims.</p>
     *
     * @param token          token JWT
     * @param claimsResolver função que extrai o claim desejado do objeto {@link Claims}
     * @param <T>            tipo do claim retornado
     * @return valor do claim
     */
    public <T> T getClaimForToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    /**
     * Parseia o token JWT e retorna todos os claims do payload.
     *
     * <p>O JJWT valida automaticamente a assinatura digital ao parsear.
     * Se o token tiver sido adulterado (payload ou header modificados),
     * a verificação da assinatura falhará e uma exceção será lançada.</p>
     *
     * <p><b>Exceções possíveis (tratadas no filtro):</b></p>
     * <ul>
     *   <li>{@code ExpiredJwtException}: token expirado</li>
     *   <li>{@code MalformedJwtException}: token com formato inválido</li>
     *   <li>{@code SecurityException}: assinatura inválida (token adulterado)</li>
     * </ul>
     *
     * @param token token JWT
     * @return objeto {@link Claims} com todos os dados do payload
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(parseSecret())       // define a chave para verificar a assinatura
                .build()
                .parseSignedClaims(token)        // parseia e valida assinatura + expiração
                .getPayload();                   // retorna o payload (Claims)
    }

    /**
     * Converte o segredo (em Base64) em um objeto {@link SecretKey} para HMAC-SHA.
     *
     * <p>Usar {@link Decoders#BASE64} é essencial para garantir que a chave tenha
     * o tamanho correto em bytes após decodificação — independente da codificação de
     * caracteres da plataforma. Para HS256, o mínimo é 256 bits (32 bytes).</p>
     *
     * @return {@link SecretKey} pronta para uso na assinatura/verificação
     */
    private SecretKey parseSecret() {
        if (this.secret == null || this.secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET deve ser informado em Base64.");
        }

        byte[] chave;
        try {
            chave = Decoders.BASE64.decode(this.secret);
        } catch (DecodingException e) {
            throw new IllegalStateException("JWT_SECRET deve estar codificado em Base64.", e);
        }

        if (chave.length < TAMANHO_MINIMO_CHAVE_HS256_BYTES) {
            throw new IllegalStateException("JWT_SECRET deve possuir ao menos 256 bits apos decodificar Base64.");
        }

        return Keys.hmacShaKeyFor(chave);
    }
}
