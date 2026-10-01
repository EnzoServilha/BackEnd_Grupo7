package sptech.school.dto.periodo;

public class PeriodoQtdPecasDTO {
    private Integer id;
    private Integer qtd;
    private String descricao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PeriodoQtdPecasDTO(Integer id, Integer qtd) {
        this.id = id;
        this.qtd = qtd;
    }

    public PeriodoQtdPecasDTO(Integer id, Long qtd) {
        this.id = id;
        this.qtd = qtd.intValue();
    }

    public PeriodoQtdPecasDTO(Integer id, Long qtd, String descricao) {
        this.id = id;
        this.qtd = qtd.intValue();
        this.descricao = descricao;
    }

    public PeriodoQtdPecasDTO() {
    }
}
