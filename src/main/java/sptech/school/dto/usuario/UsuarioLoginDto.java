package sptech.school.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioLoginDto {

  @NotBlank
  @Email
  @Size(max = 150)
  @Schema(description = "E-mail do usuário", example = "john@doe.com")
  private String email;

  @NotBlank
  @Size(max = 72)
  @Schema(description = "Senha do usuário", example = "Senha@123")
  private String senha;

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
}
