package sptech.school.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntidadeAtivavel {

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "desativado_por")
    private Usuario desativadoPor;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Usuario getDesativadoPor() {
        return desativadoPor;
    }

    public void setDesativadoPor(Usuario desativadoPor) {
        this.desativadoPor = desativadoPor;
    }

    public void desativar(Usuario usuarioExecutor) {
        if (Boolean.TRUE.equals(ativo)) {
            ativo = false;
            desativadoPor = usuarioExecutor;
        }
    }

    public void reativar() {
        if (Boolean.FALSE.equals(ativo)) {
            ativo = true;
            desativadoPor = null;
        }
    }
}