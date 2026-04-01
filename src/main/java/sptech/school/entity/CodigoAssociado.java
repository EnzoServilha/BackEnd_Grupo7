package sptech.school.entity;

import jakarta.persistence.*;
import sptech.school.entity.enums.TipoCodigoEnum;

import java.util.List;

@Entity
@Table(name = "codigo_associado")
public class CodigoAssociado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_codigo", nullable = false)
    private TipoCodigoEnum tipoCodigo;

    @Column(name = "codigo", nullable = false, length = 100)
    private String codigo;

    @Column(name = "fk_fornecedor")
    private Integer fkFornecedor;

    @Column(name = "fk_cliente")
    private Integer fkCliente;

    @ManyToMany(mappedBy = "codigosAssociados")
    private List<Item> itens;

    public CodigoAssociado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoCodigoEnum getTipoCodigo() {
        return tipoCodigo;
    }

    public void setTipoCodigo(TipoCodigoEnum tipoCodigo) {
        this.tipoCodigo = tipoCodigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getFkFornecedor() {
        return fkFornecedor;
    }

    public void setFkFornecedor(Integer fkFornecedor) {
        this.fkFornecedor = fkFornecedor;
    }

    public Integer getFkCliente() {
        return fkCliente;
    }

    public void setFkCliente(Integer fkCliente) {
        this.fkCliente = fkCliente;
    }

    public List<Item> getPecas() {
        return itens;
    }

    public void setPecas(List<Item> pecas) {
        this.itens = pecas;
    }
}

