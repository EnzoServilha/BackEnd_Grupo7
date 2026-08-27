package sptech.school.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioSenhaDto {


    @NotBlank
    @Size(max = 72)
    @Schema(description = "Senha atual do usuário", example = "SenhaAtual@123")
    private String senhaAtual;

    @NotBlank
    @Size(min = 8, max = 72)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\p{L}\\p{N}\\s])\\S+$",
            message = "A senha deve conter letra maiúscula, letra minúscula, número, caractere especial e não pode ter espaços"
    )
    @Schema(description = "Nova senha do usuário", example = "NovaSenha@123")
    private String novaSenha;

    public String getSenhaAtual() {
        return senhaAtual;
    }
    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }
    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

}
