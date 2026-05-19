package sptech.school.src.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "codigo_associado")
public class CodigoAssociado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "fk_fornecedor")
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "fk_cliente")
    private Cliente cliente;

    @ManyToMany(mappedBy = "codigosAssociados")
    private List<Item> itens;

    public CodigoAssociado() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<Item> getItens() { return itens; }
    public void setItens(List<Item> itens) { this.itens = itens; }
}
