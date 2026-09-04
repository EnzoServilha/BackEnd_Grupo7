package sptech.school.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioCriacaoDto {

  @NotBlank
  @Size(min = 3, max = 100)
  @Schema(description = "Nome do usuário", example = "John Doe")
  private String nome;

  @NotBlank
  @Email
  @Size(max = 150)
  @Schema(description = "Email do usuário", example = "john@doe.com")
  private String email;

  @NotBlank
  @Size(min = 8, max = 72)
  @Pattern(
          regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\p{L}\\p{N}\\s])\\S+$",
          message = "A senha deve conter letra maiúscula, letra minúscula, número, caractere especial e não pode ter espaços"
  )
  @Schema(description = "Senha do usuário", example = "Senha@123")
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
