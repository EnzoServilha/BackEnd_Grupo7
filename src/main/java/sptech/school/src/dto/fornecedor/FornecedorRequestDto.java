package sptech.school.src.dto.fornecedor;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class FornecedorRequestDto{
        @Size(max = 150)
        private String razaoSocial;

        @Size(max = 18)
        private String cnpj;

        @Size(max = 100)
        private String nomeContato;

        @Size(max = 100)
        private String nomeEmpresa;

        @Size(max = 20)
        private String telefone;

        @Size(max = 100)
        private String email;

        private String observacoes;

        private LocalDateTime dataCadastro;

        private List<Integer> categoriaId;

        private List<Integer> marcaId;

        private Integer enderecoId;

        public List<Integer> getMarcaId() {
                return marcaId;
        }

        public void setMarcaId(List<Integer> marcaId) {
                this.marcaId = marcaId;
        }

        public String getRazaoSocial() {
                return razaoSocial;
        }

        public void setRazaoSocial(String razaoSocial) {
                this.razaoSocial = razaoSocial;
        }

        public String getCnpj() {
                return cnpj;
        }

        public void setCnpj(String cnpj) {
                this.cnpj = cnpj;
        }

        public String getNomeContato() {
                return nomeContato;
        }

        public void setNomeContato(String nomeContato) {
                this.nomeContato = nomeContato;
        }

        public String getNomeEmpresa() {
                return nomeEmpresa;
        }

        public void setNomeEmpresa(String nomeEmpresa) {
                this.nomeEmpresa = nomeEmpresa;
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

        public String getObservacoes() {
                return observacoes;
        }

        public void setObservacoes(String observacoes) {
                this.observacoes = observacoes;
        }

        public LocalDateTime getDataCadastro() {
                return dataCadastro;
        }

        public void setDataCadastro(LocalDateTime dataCadastro) {
                this.dataCadastro = dataCadastro;
        }

        public List<Integer> getCategoriaId() {
                return categoriaId;
        }

        public void setCategoriaId(List<Integer> categoriaId) {
                this.categoriaId = categoriaId;
        }

        public Integer getEnderecoId() {
                return enderecoId;
        }

        public void setEnderecoId(Integer enderecoId) {
                this.enderecoId = enderecoId;
        }

        @Override
        public String toString() {
                return "FornecedorRequestDto{" +
                        "razaoSocial='" + razaoSocial + '\'' +
                        ", cnpj='" + cnpj + '\'' +
                        ", nomeContato='" + nomeContato + '\'' +
                        ", nomeEmpresa='" + nomeEmpresa + '\'' +
                        ", telefone='" + telefone + '\'' +
                        ", email='" + email + '\'' +
                        ", observacoes='" + observacoes + '\'' +
                        ", dataCadastro=" + dataCadastro +
                        ", categoriaId=" + categoriaId +
                        ", enderecoId=" + enderecoId +
                        '}';
        }
}


