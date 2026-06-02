package sptech.school.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.school.dto.periodo.PeriodoResponseDto;
import sptech.school.entity.Periodo;
import sptech.school.exception.EntidadeConflitanteException;
import sptech.school.mapper.PeriodoMapper;
import sptech.school.repository.PeriodoRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PeriodoService")
class PeriodoServiceTest {
    @Mock
    private PeriodoRepository periodoRepository;

    @InjectMocks
    private PeriodoService periodoService;

    @Nested
    @DisplayName("Buscar ultimo período")
    class BuscarUltimoPeriodoTest {

        @Test
        @DisplayName("Deve retornar último período")
        void buscarUltimoPeriodoComSucesso() {
            Periodo periodo = new Periodo();
            periodo.setId(1);
            periodo.setQtdPecas(30);

            when(periodoRepository.findFirstByOrderByIdDesc()).thenReturn(periodo);

            PeriodoResponseDto response = PeriodoMapper.toResponseDto(periodoService.buscarUltimoPeriodo());

            assertEquals(periodo.getId(), response.getId());
            assertEquals(periodo.getQtdPecas(), response.getQtdPecas());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o período vier null")
        void buscarUltimoPeriodoNull() {
            Periodo periodo = null;

            when(periodoRepository.findFirstByOrderByIdDesc()).thenReturn(periodo);

            assertThrows(EntidadeConflitanteException.class,
                    () -> periodoService.buscarUltimoPeriodo());

        }
    }

    @Nested
    @DisplayName("Fechar estoque")
    class FecharEstoqueTest {
        @Test
        @DisplayName("Deve fechar estoque corretamente")
        void fecharEstoqueComSucesso() {
            int qtdPecas = 45;
            Periodo ultimoPeriodo = new Periodo();
            ultimoPeriodo.setId(2);
            ultimoPeriodo.setQtdPecas(0);

            when(periodoRepository.findFirstByOrderByIdDesc()).thenReturn(ultimoPeriodo);
            when(periodoRepository.pegarTotalDePecasDoPeriodo(ultimoPeriodo.getId())).thenReturn(qtdPecas);
            when(periodoRepository.save(Mockito.any(Periodo.class))).thenReturn(ultimoPeriodo);

            Periodo periodo = periodoService.fecharEstoque();

            assertEquals(qtdPecas, periodo.getQtdPecas());
            assertEquals(ultimoPeriodo.getId(), periodo.getId());
        }

        @Test
        @DisplayName("Deve lançar exceção quando período for null")
        void fecharEstoqueNull() {
            Periodo ultimoPeriodo = null;

            when(periodoRepository.findFirstByOrderByIdDesc()).thenReturn(ultimoPeriodo);

            assertThrows(RuntimeException.class,
                    () -> periodoService.fecharEstoque());
        }
    }

    @Nested
    @DisplayName("Cadastrar Período")
    class CadastrarPeriodoTest {

        @Test
        @DisplayName("Deve cadastrar corretamente")
        void cadastrarPeriodoComSucesso() {
            LocalDateTime agora = LocalDateTime.now();
            String descricao = "teste";
            Integer qtdPecas = 0;

            Periodo periodo = new Periodo();
            periodo.setId(1);
            periodo.setQtdPecas(qtdPecas);
            periodo.setAnotacao(descricao);
            periodo.setDataCriacao(agora);

            when(periodoRepository.save(Mockito.any(Periodo.class))).thenReturn(periodo);

            Periodo periodoCadastrado = periodoService.cadastrarPeriodo(descricao);

            assertEquals(agora, periodoCadastrado.getDataCriacao());
            assertEquals(qtdPecas, periodoCadastrado.getQtdPecas());
        }
    }
}