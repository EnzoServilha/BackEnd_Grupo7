package sptech.school.dto.dashboard;

import java.math.BigDecimal;

public class DashboardMensalDto {
    private Integer mes; // 1 a 12
    private String nomeMes; // Opcional (Janeiro, Fevereiro...) caso o front prefira formatado
    private BigDecimal totalCompras;
    private BigDecimal totalVendas;
    private BigDecimal lucro;

    public DashboardMensalDto(Integer mes, BigDecimal totalCompras, BigDecimal totalVendas) {
        this.mes = mes;
        this.totalCompras = totalCompras != null ? totalCompras : BigDecimal.ZERO;
        this.totalVendas = totalVendas != null ? totalVendas : BigDecimal.ZERO;
        // Lucro = Vendas - Compras (ou a regra de negócio que vocês utilizam)
        this.lucro = this.totalVendas.subtract(this.totalCompras);
    }

    // Getters e Setters
    public Integer getMes() { return mes; }
    public BigDecimal getTotalCompras() { return totalCompras; }
    public BigDecimal getTotalVendas() { return totalVendas; }
    public BigDecimal getLucro() { return lucro; }
}