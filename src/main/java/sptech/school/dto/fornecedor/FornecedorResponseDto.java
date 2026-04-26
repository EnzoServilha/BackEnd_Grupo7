package sptech.school.dto.fornecedor;

import sptech.school.dto.categoria.CategoriaResponseDto;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.dto.marca.MarcaResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class FornecedorResponseDto {
    private Integer id;
    private String razaoSocial;
    private String cnpj;
    private String nomeContato;
    private String nomeEmpresa;
    private String telefone;
    private String email;
    private String observacoes;
    private LocalDateTime dataCadastro;
    private List<CategoriaResponseDto> categoria;
    private List<MarcaResponseDto> marcas;
    private EnderecoResponseDto endereco;

    public FornecedorResponseDto() {}

    public FornecedorResponseDto(Integer id, String razaoSocial, String cnpj, String nomeContato,
                                String nomeEmpresa, String telefone, String email, String observacoes,
                                LocalDateTime dataCadastro, List<CategoriaResponseDto> categoria,
                                List<MarcaResponseDto> marcas, EnderecoResponseDto endereco) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.nomeContato = nomeContato;
        this.nomeEmpresa = nomeEmpresa;
        this.telefone = telefone;
        this.email = email;
        this.observacoes = observacoes;
        this.dataCadastro = dataCadastro;
        this.categoria = categoria;
        this.marcas = marcas;
        this.endereco = endereco;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getNomeContato() { return nomeContato; }
    public void setNomeContato(String nomeContato) { this.nomeContato = nomeContato; }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public List<CategoriaResponseDto> getCategoria() { return categoria; }
    public void setCategoria(List<CategoriaResponseDto> categoria) { this.categoria = categoria; }

    public List<MarcaResponseDto> getMarcas() { return marcas; }
    public void setMarcas(List<MarcaResponseDto> marcas) { this.marcas = marcas; }

    public EnderecoResponseDto getEndereco() { return endereco; }
    public void setEndereco(EnderecoResponseDto endereco) { this.endereco = endereco; }
}
