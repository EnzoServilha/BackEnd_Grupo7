package sptech.school.src.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioCriacaoDto {

  @Size(min = 3, max = 10)
  @Schema(description = "Nome do usuário", example = "John Doe")
  private String nome;

  @Email
  @Schema(description = "Email do usuário", example = "john@doe.com")
  private String email;

  @Size(min = 6, max = 20)
  @Schema(description = "Senha do usuário", example = "123456")
  private String senha;

  @NotNull
  @Schema(description = "Id da permissão do usuário", example = "1")
  private Integer permissaoId;

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

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Integer getPermissaoId() {
    return permissaoId;
  }

  public void setPermissaoId(Integer permissaoId) {
    this.permissaoId = permissaoId;
  }
}
