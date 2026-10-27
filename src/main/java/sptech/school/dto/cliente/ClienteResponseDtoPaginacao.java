package sptech.school.dto.cliente;

import com.fasterxml.jackson.annotation.JsonInclude;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.entity.Cliente;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteResponseDtoPaginacao {

    // Resposta paginada (listagem)
    private List<Cliente> conteudo;
    private long totalItens;
    private int paginaAtual;
    private int totalPaginas;

    // Resposta de um único cliente (busca, buscarPorId...)
    private Integer id;
    private String nomeEmpresa;
    private String nomeContato;
    private String cpfCnpj;
    private String telefone;
    private String email;
    private String observacoes;
    private LocalDateTime dataCadastro;
    private EnderecoResponseDto endereco;
    private Boolean ativo;
    private Long desativadoPor;

    public ClienteResponseDtoPaginacao() {
    }

    public ClienteResponseDtoPaginacao(List<Cliente> conteudo, long totalItens, int paginaAtual, int totalPaginas) {
        this.conteudo = conteudo;
        this.totalItens = totalItens;
        this.paginaAtual = paginaAtual;
        this.totalPaginas = totalPaginas;
    }

    public ClienteResponseDtoPaginacao(Integer id, String nomeEmpresa, String nomeContato, String cpfCnpj,
                                       String telefone, String email, String observacoes,
                                       LocalDateTime dataCadastro, EnderecoResponseDto endereco,
                                       Boolean ativo, Long desativadoPor) {
        this.id = id;
        this.nomeEmpresa = nomeEmpresa;
        this.nomeContato = nomeContato;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.email = email;
        this.observacoes = observacoes;
        this.dataCadastro = dataCadastro;
        this.endereco = endereco;
        this.ativo = ativo;
        this.desativadoPor = desativadoPor;
    }

    public List<Cliente> getConteudo() { return conteudo; }
    public long getTotalItens() { return totalItens; }
    public int getPaginaAtual() { return paginaAtual; }
    public int getTotalPaginas() { return totalPaginas; }

    public Integer getId() { return id; }
    public String getNomeEmpresa() { return nomeEmpresa; }
    public String getNomeContato() { return nomeContato; }
    public String getCpfCnpj() { return cpfCnpj; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getObservacoes() { return observacoes; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public EnderecoResponseDto getEndereco() { return endereco; }
    public Boolean getAtivo() { return ativo; }
    public Long getDesativadoPor() { return desativadoPor; }
}