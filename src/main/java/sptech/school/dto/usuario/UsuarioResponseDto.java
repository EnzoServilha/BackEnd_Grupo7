package sptech.school.dto.usuario;

import sptech.school.dto.permissao.PermissaoResponseDto;

import java.time.LocalDateTime;

public class UsuarioResponseDto {
    private Long id;
    private String nome;
    private String email;
    private LocalDateTime dataCadastro;
    private PermissaoResponseDto permissao;

    // ✅ Construtor vazio (necessário para JPA/Jackson)
    public UsuarioResponseDto() {}

    // ✅ Construtor com argumentos (usado pelo mapper)
    public UsuarioResponseDto(Long id, String nome, String email, LocalDateTime dataCadastro, PermissaoResponseDto permissao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCadastro = dataCadastro;
        this.permissao = permissao;
    }

    // ✅ Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public PermissaoResponseDto getPermissao() {
        return permissao;
    }

    public void setPermissao(PermissaoResponseDto permissao) {
        this.permissao = permissao;
    }
}
