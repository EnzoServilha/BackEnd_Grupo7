package sptech.school.dto.periodo;

public class PeriodoQtdPecasDTO {
    private Integer id;
    private Integer qtd;

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

    public PeriodoQtdPecasDTO(Integer id, Integer qtd) {
        this.id = id;
        this.qtd = qtd;
    }

    public PeriodoQtdPecasDTO(Integer id, Long qtd) {
        this.id = id;
        this.qtd = qtd.intValue();
    }

    public PeriodoQtdPecasDTO() {
    }
}
