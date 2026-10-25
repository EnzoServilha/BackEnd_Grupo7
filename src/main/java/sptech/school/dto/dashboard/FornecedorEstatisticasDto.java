package sptech.school.dto.dashboard;

public class FornecedorEstatisticasDto {
    private Integer fornecedorId;
    private String nomeEmpresa;
    private String ufCidade;
    private Double percentualEntregasNoPrazo;
    private Double percentualFretePago;
    private Double medianaTempoEntregaDias;

    public FornecedorEstatisticasDto(Integer fornecedorId, String nomeEmpresa, String ufCidade, Double percentualEntregasNoPrazo, Double percentualFretePago, Double medianaTempoEntregaDias) {
        this.fornecedorId = fornecedorId;
        this.nomeEmpresa = nomeEmpresa;
        this.ufCidade = ufCidade;
        this.percentualEntregasNoPrazo = percentualEntregasNoPrazo;
        this.percentualFretePago = percentualFretePago;
        this.medianaTempoEntregaDias = medianaTempoEntregaDias;
    }

    public FornecedorEstatisticasDto() {
    }

    public Integer getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Integer fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getUfCidade() {
        return ufCidade;
    }

    public void setUfCidade(String ufCidade) {
        this.ufCidade = ufCidade;
    }

    public Double getPercentualEntregasNoPrazo() {
        return percentualEntregasNoPrazo;
    }

    public void setPercentualEntregasNoPrazo(Double percentualEntregasNoPrazo) {
        this.percentualEntregasNoPrazo = percentualEntregasNoPrazo;
    }

    public Double getPercentualFretePago() {
        return percentualFretePago;
    }

    public void setPercentualFretePago(Double percentualFretePago) {
        this.percentualFretePago = percentualFretePago;
    }

    public Double getMedianaTempoEntregaDias() {
        return medianaTempoEntregaDias;
    }

    public void setMedianaTempoEntregaDias(Double medianaTempoEntregaDias) {
        this.medianaTempoEntregaDias = medianaTempoEntregaDias;
    }
}