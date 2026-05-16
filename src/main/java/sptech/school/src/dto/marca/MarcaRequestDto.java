package sptech.school.src.dto.marca;

import jakarta.validation.constraints.NotBlank;

public class MarcaRequestDto{
        @NotBlank
        private String nome;

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }
}

