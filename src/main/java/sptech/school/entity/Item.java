package sptech.school.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "item")
public class Item extends EntidadeAtivavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @Column(name = "codigo_interno", length = 50)
    private String codigoInterno;

    @Size(max = 50)
    @Column(length = 50)
    private String marca;

    @Column
    private Integer ano;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(length = 100)
    private String localizacao;

    @Column
    private LocalDateTime dataCadastro;

    @ManyToMany
    @JoinTable(
        name = "item_codigo_associado",
        joinColumns = @JoinColumn(name = "fk_item"),
        inverseJoinColumns = @JoinColumn(name = "fk_codigo_associado")
    )
    private List<CodigoAssociado> codigosAssociados;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "item_similar",
        joinColumns = @JoinColumn(name = "fk_item"),
        inverseJoinColumns = @JoinColumn(name = "fk_item_similar")
    )
    private List<Item> itensSimilares;

    public Item() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public List<CodigoAssociado> getCodigosAssociados() { return codigosAssociados; }
    public void setCodigosAssociados(List<CodigoAssociado> codigosAssociados) { this.codigosAssociados = codigosAssociados; }

    public List<Item> getItensSimilares() { return itensSimilares; }
    public void setItensSimilares(List<Item> itensSimilares) { this.itensSimilares = itensSimilares; }
}
