package sptech.school.dto.dashboard;

public class CotacoesPorcentagemDto {
    private Long totalCotacoes;
    private Long totalCotacoesConcluidasParcial;
    private Long totalCotacoesConcluidasTotalmente;
    private Long totalCotacoesViraramPedidos;

    private Double porcentagemViraramPedidos;
    private Double porcentagemConcluidasTotalmente;
    private Double porcentagemConcluidasParcial;

    public CotacoesPorcentagemDto(Long totalCotacoes, Long totalCotacoesConcluidasParcial,
                                  Long totalCotacoesConcluidasTotalmente, Long totalCotacoesViraramPedidos) {

        this.totalCotacoes = totalCotacoes != null ? totalCotacoes : 0L;
        this.totalCotacoesConcluidasParcial = totalCotacoesConcluidasParcial != null ? totalCotacoesConcluidasParcial : 0L;
        this.totalCotacoesConcluidasTotalmente = totalCotacoesConcluidasTotalmente != null ? totalCotacoesConcluidasTotalmente : 0L;
        this.totalCotacoesViraramPedidos = totalCotacoesViraramPedidos != null ? totalCotacoesViraramPedidos : 0L;

        if (this.totalCotacoes > 0) {
            this.porcentagemViraramPedidos = (this.totalCotacoesViraramPedidos * 100.0) / this.totalCotacoes;
            this.porcentagemConcluidasTotalmente = (this.totalCotacoesConcluidasTotalmente * 100.0) / this.totalCotacoes;
            this.porcentagemConcluidasParcial = (this.totalCotacoesConcluidasParcial * 100.0) / this.totalCotacoes;
        } else {
            this.porcentagemViraramPedidos = 0.0;
            this.porcentagemConcluidasTotalmente = 0.0;
            this.porcentagemConcluidasParcial = 0.0;
        }
    }

    public Long getTotalCotacoes() { return totalCotacoes; }
    public Long getTotalCotacoesConcluidasParcial() { return totalCotacoesConcluidasParcial; }
    public Long getTotalCotacoesConcluidasTotalmente() { return totalCotacoesConcluidasTotalmente; }
    public Long getTotalCotacoesViraramPedidos() { return totalCotacoesViraramPedidos; }
    public Double getPorcentagemViraramPedidos() { return porcentagemViraramPedidos; }
    public Double getPorcentagemConcluidasTotalmente() { return porcentagemConcluidasTotalmente; }
    public Double getPorcentagemConcluidasParcial() { return porcentagemConcluidasParcial; }
}