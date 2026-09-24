package sptech.school.dto.cliente;
import sptech.school.dto.endereco.EnderecoResponseDto;
import sptech.school.entity.Cliente;

import java.time.LocalDateTime;
import java.util.List;

public class ClienteResponseDtoPaginacao {

    private List<Cliente> conteudo;
    private long totalItens;
    private int paginaAtual;
    private int totalPaginas;

    public ClienteResponseDtoPaginacao(List<Cliente> conteudo, long totalItens, int paginaAtual, int totalPaginas) {
        this.conteudo = conteudo;
        this.totalItens = totalItens;
        this.paginaAtual = paginaAtual;
        this.totalPaginas = totalPaginas;
    }

    public ClienteResponseDtoPaginacao(Integer id, String nomeEmpresa, String nomeContato, String cpfCnpj, String telefone, String email, String observacoes, LocalDateTime dataCadastro, EnderecoResponseDto enderecoResponseDto, Boolean ativo, Long aLong) {
    }

    public List<Cliente> getConteudo() { return conteudo; }
    public long getTotalItens() { return totalItens; }
    public int getPaginaAtual() { return paginaAtual; }
    public int getTotalPaginas() { return totalPaginas; }

    public ClienteResponseDtoPaginacao() {
    }
}

