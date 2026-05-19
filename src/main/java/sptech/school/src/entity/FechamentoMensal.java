package sptech.school.src.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "fechamento_mes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"ano", "mes", "fk_item"})
)
public class FechamentoMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer mes;

    @Column
    private Integer ano;

    @Column
    private Integer qtd;

    @ManyToOne
    @JoinColumn(name = "fk_item")
    private Item item;

    public FechamentoMensal() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public Integer getQtd() { return qtd; }
    public void setQtd(Integer qtd) { this.qtd = qtd; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
}

