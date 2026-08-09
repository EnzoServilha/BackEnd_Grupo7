package sptech.school.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "total_gasto_impostos", precision = 10, scale = 2)
    private BigDecimal totalGastoImpostos;

    @Column(name = "preco_frete", precision = 10, scale = 2)
    private BigDecimal precoFrete;

    @Column(name = "data_movimentacao")
    private LocalDateTime dataMovimentacao;

    @Column(name = "data_entrega_prevista")
    private LocalDate dataEntregaPrevista;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "movimentacao_original")
    private MovimentacaoEstoque movimentacaoOriginal;

    @Column(name = "numero_nota_fiscal", length = 45)
    private String numeroNotaFiscal;

    @OneToMany(mappedBy = "movimentacaoEstoque")
    private List<ItensNaMovimentacao> itens;

    // --- NOVO CAMPO ASSOCIAÇÃO ---
    @ManyToOne
    @JoinColumn(name = "periodo_id")
    private Periodo periodo;

    public MovimentacaoEstoque() {}

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public BigDecimal getTotalGastoImpostos() { return totalGastoImpostos; }
    public void setTotalGastoImpostos(BigDecimal totalGastoImpostos) { this.totalGastoImpostos = totalGastoImpostos; }

    public BigDecimal getPrecoFrete() { return precoFrete; }
    public void setPrecoFrete(BigDecimal precoFrete) { this.precoFrete = precoFrete; }

    public LocalDateTime getDataMovimentacao() { return dataMovimentacao; }
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }

    public LocalDate getDataEntregaPrevista() { return dataEntregaPrevista; }
    public void setDataEntregaPrevista(LocalDate dataEntregaPrevista) { this.dataEntregaPrevista = dataEntregaPrevista; }

    public LocalDate getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public MovimentacaoEstoque getMovimentacaoOriginal() { return movimentacaoOriginal; }
    public void setMovimentacaoOriginal(MovimentacaoEstoque movimentacaoOriginal) { this.movimentacaoOriginal = movimentacaoOriginal; }

    public String getNumeroNotaFiscal() { return numeroNotaFiscal; }
    public void setNumeroNotaFiscal(String numeroNotaFiscal) { this.numeroNotaFiscal = numeroNotaFiscal; }

    public List<ItensNaMovimentacao> getItens() { return itens; }
    public void setItens(List<ItensNaMovimentacao> itens) { this.itens = itens; }

    public Periodo getPeriodo() { return periodo; }
    public void setPeriodo(Periodo periodo) { this.periodo = periodo; }
}