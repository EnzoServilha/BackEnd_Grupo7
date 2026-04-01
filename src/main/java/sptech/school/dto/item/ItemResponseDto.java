package sptech.school.dto.item;

import java.time.LocalDate;

public class ItemResponseDto {

    private Integer id;
    private Integer codigoInterno;
    private String marca;
    private Integer ano;
    private String descricao;
    private String localidade;
    private LocalDate dataCadastro;

    public ItemResponseDto(Integer id, Integer codigoInterno, String marca, Integer ano, String descricao, String localidade, LocalDate dataCadastro) {
        this.id = id;
        this.codigoInterno = codigoInterno;
        this.marca = marca;
        this.ano = ano;
        this.descricao = descricao;
        this.localidade = localidade;
        this.dataCadastro = dataCadastro;
    }

    public ItemResponseDto() {
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
}
