package sptech.school.src.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class UsuarioSenhaDto {


    @Size(min = 6, max = 20)
    @Schema(description = "Senha atual do usuário", example = "123456")
    private String senhaAtual;

    @Size(min = 6, max = 20)
    @Schema(description = "Nova senha do usuário", example = "novaSenha123")
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
