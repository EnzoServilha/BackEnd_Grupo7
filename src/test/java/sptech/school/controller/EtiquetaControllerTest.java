package sptech.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sptech.school.exception.EntidadeNaoEncontradaException;
import sptech.school.exception.GlobalExceptionHandler;
import sptech.school.exception.PublicacaoEtiquetaException;
import sptech.school.exception.RequisicaoInvalidaException;
import sptech.school.messaging.SolicitacaoEtiquetaMessage;
import sptech.school.service.EtiquetaService;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EtiquetaControllerTest {

    private static final String SOLICITACAO_ID = "6f1c2d0e-3b5a-4c7d-9e8f-0a1b2c3d4e5f";

    @Mock
    private EtiquetaService etiquetaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EtiquetaController(etiquetaService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /etiquetas/{itemId}/fila deve responder 202 sem afirmar que o PDF já foi gerado")
    void deveResponder202QuandoSolicitacaoAceita() throws Exception {
        Mockito.when(etiquetaService.solicitarGeracao(7)).thenReturn(
                new SolicitacaoEtiquetaMessage(SOLICITACAO_ID, 7, "ABC-123", "Bosch", "Filtro", "2025-09-03T23:00:15"));

        mockMvc.perform(post("/etiquetas/7/fila"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.solicitacaoId").value(SOLICITACAO_ID))
                .andExpect(jsonPath("$.itemId").value(7))
                .andExpect(jsonPath("$.status").value("ACEITA"))
                .andExpect(jsonPath("$.mensagem").value(containsString("será gerada")));
    }

    @Test
    @DisplayName("Deve responder 404 quando o item não existe")
    void deveResponder404QuandoItemNaoExiste() throws Exception {
        Mockito.when(etiquetaService.solicitarGeracao(99)).thenThrow(new EntidadeNaoEncontradaException("Item", 99));

        mockMvc.perform(post("/etiquetas/99/fila"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value(containsString("Item")));
    }

    @Test
    @DisplayName("Deve responder 400 quando os dados do item são inconsistentes")
    void deveResponder400QuandoItemInconsistente() throws Exception {
        Mockito.when(etiquetaService.solicitarGeracao(7))
                .thenThrow(new RequisicaoInvalidaException("O item 7 não possui código interno"));

        mockMvc.perform(post("/etiquetas/7/fila"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value(containsString("código interno")));
    }

    @Test
    @DisplayName("Deve responder 503 sem expor detalhes do broker quando a publicação falha")
    void deveResponder503QuandoPublicacaoFalha() throws Exception {
        Mockito.when(etiquetaService.solicitarGeracao(7))
                .thenThrow(new PublicacaoEtiquetaException("Connection refused: rabbitmq:5672", null));

        mockMvc.perform(post("/etiquetas/7/fila"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.mensagem").value(not(containsString("rabbitmq"))))
                .andExpect(jsonPath("$.mensagem").value(containsString("Tente novamente")));
    }

    @Test
    @DisplayName("Deve responder 400 quando o itemId não é numérico")
    void deveResponder400QuandoItemIdInvalido() throws Exception {
        mockMvc.perform(post("/etiquetas/abc/fila").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(etiquetaService);
    }

    @Test
    @DisplayName("O endpoint síncrono de PDF não existe mais no backend: o PDF é gerado pelo microserviço de etiquetas")
    void naoDeveGerarPdfNoBackend() throws Exception {
        mockMvc.perform(get("/etiquetas/7/pdf"))
                .andExpect(status().is4xxClientError());

        Mockito.verifyNoInteractions(etiquetaService);
    }
}
