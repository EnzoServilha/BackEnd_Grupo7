package sptech.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private Integer codigoInterno;

    @NotBlank
    @Column(nullable = true)
    private String marca;

    @NotBlank
    @Column(nullable = true)
    private Integer ano;

    @NotBlank
    @Column(nullable = true, columnDefinition = "TEXT")
    private String descricao;

    @NotBlank
    @Column(nullable = false)
    private String localidade;

    @NotBlank
    @Column(nullable = false)
    private LocalDate dataCadastro;

    @ManyToMany
    @JoinTable(
        name = "peca_codigo_associado",
        joinColumns = @JoinColumn(name = "fk_peca"),
        inverseJoinColumns = @JoinColumn(name = "fk_codigo_associado")
    )
    private List<CodigoAssociado> codigosAssociados;

    public Item(Integer id, Integer codigoInterno, String marca, Integer ano, String descricao, String localidade, LocalDate dataCadastro) {
        this.id = id;
        this.codigoInterno = codigoInterno;
        this.marca = marca;
        this.ano = ano;
        this.descricao = descricao;
        this.localidade = localidade;
        this.dataCadastro = dataCadastro;
    }

    public Item() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(Integer codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<CodigoAssociado> getCodigosAssociados() {
        return codigosAssociados;
    }

    public void setCodigosAssociados(List<CodigoAssociado> codigosAssociados) {
        this.codigosAssociados = codigosAssociados;
    }
}
