package sptech.school.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
    name = "itens_na_movimentacao",
    uniqueConstraints = @UniqueConstraint(columnNames = {"movimentacao_estoque_id", "item_id"})
)
public class ItensNaMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "movimentacao_estoque_id", nullable = false)
    private MovimentacaoEstoque movimentacaoEstoque;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column
    private Integer qtd;

    @Column(name = "preco_unitario", precision = 10, scale = 2)
    private Double precoUnitario;

    public ItensNaMovimentacao() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public MovimentacaoEstoque getMovimentacaoEstoque() { return movimentacaoEstoque; }
    public void setMovimentacaoEstoque(MovimentacaoEstoque movimentacaoEstoque) { this.movimentacaoEstoque = movimentacaoEstoque; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
