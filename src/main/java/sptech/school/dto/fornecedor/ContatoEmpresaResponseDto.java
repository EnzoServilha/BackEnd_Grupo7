package sptech.school.dto.fornecedor;

public class ContatoEmpresaResponseDto {

    private Integer id;
    private String tipoEntidade; // "CLIENTE" ou "FORNECEDOR" para diferenciar no front-end
    private String nomeEmpresa;
    private String nomeContato;
    private String telefone;
    private String email;
    private String cidade;
    private String logradouro;

    public ContatoEmpresaResponseDto() {}

    public ContatoEmpresaResponseDto(Integer id, String tipoEntidade, String nomeEmpresa, String nomeContato, String telefone, String email, String cidade, String logradouro) {
        this.id = id;
        this.tipoEntidade = tipoEntidade;
        this.nomeEmpresa = nomeEmpresa;
        this.nomeContato = nomeContato;
        this.telefone = telefone;
        this.email = email;
        this.cidade = cidade;
        this.logradouro = logradouro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public void setTipoEntidade(String tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
}
