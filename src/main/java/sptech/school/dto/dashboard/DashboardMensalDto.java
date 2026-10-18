package sptech.school.dto.dashboard;

import java.math.BigDecimal;

public class DashboardMensalDto {
    private Integer mes;
    private String nomeMes;
    private BigDecimal totalCompras;
    private BigDecimal totalVendas;
    private BigDecimal lucro;

    public DashboardMensalDto(Object mes, Object totalCompras, Object totalVendas) {
        this.mes = mes != null ? ((Number) mes).intValue() : 0;
        this.totalCompras = totalCompras != null ? new BigDecimal(totalCompras.toString()) : BigDecimal.ZERO;
        this.totalVendas = totalVendas != null ? new BigDecimal(totalVendas.toString()) : BigDecimal.ZERO;
        this.lucro = this.totalVendas.subtract(this.totalCompras);
    }

    public DashboardMensalDto() {
    }

    public Integer getMes() { return mes; }
    public BigDecimal getTotalCompras() { return totalCompras; }
    public BigDecimal getTotalVendas() { return totalVendas; }
    public BigDecimal getLucro() { return lucro; }
}