package sptech.school.dto.usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sptech.school.entity.Usuario;

import java.util.Collection;
import java.util.List;

public class UsuarioDetalhesDto implements UserDetails {

    private final String nome;

    private final String email;

    private final String senha;

    private final boolean ativo;

    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioDetalhesDto(Usuario usuario) {
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.ativo = Boolean.TRUE.equals(usuario.getAtivo());
        this.authorities = usuario.getPermissao() == null
            ? List.of()
            : List.of(new SimpleGrantedAuthority(usuario.getPermissao().getNome()));
    }

    public String getNome() {
        return nome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
