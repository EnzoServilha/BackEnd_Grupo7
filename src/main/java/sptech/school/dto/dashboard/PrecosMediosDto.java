package sptech.school.dto.dashboard;

import java.math.BigDecimal;

public class PrecosMediosDto {
    private BigDecimal precoMedioCompra;
    private BigDecimal precoMedioVenda;

    public BigDecimal getPrecoMedioCompra() {
        return precoMedioCompra;
    }

    public void setPrecoMedioCompra(BigDecimal precoMedioCompra) {
        this.precoMedioCompra = precoMedioCompra;
    }

    public BigDecimal getPrecoMedioVenda() {
        return precoMedioVenda;
    }

    public void setPrecoMedioVenda(BigDecimal precoMedioVenda) {
        this.precoMedioVenda = precoMedioVenda;
    }

    public PrecosMediosDto(BigDecimal precoMedioCompra, BigDecimal precoMedioVenda) {
        this.precoMedioCompra = precoMedioCompra;
        this.precoMedioVenda = precoMedioVenda;
    }

    public PrecosMediosDto() {
    }
}
