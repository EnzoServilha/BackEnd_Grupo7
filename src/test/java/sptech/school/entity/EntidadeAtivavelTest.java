package sptech.school.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EntidadeAtivavelTest {

    @Test
    void deveDesativarDeFormaIdempotenteEReativar() {
        Cliente cliente = new Cliente();
        Usuario primeiroExecutor = new Usuario();
        Usuario segundoExecutor = new Usuario();

        cliente.desativar(primeiroExecutor);
        cliente.desativar(segundoExecutor);

        assertFalse(cliente.getAtivo());
        assertSame(primeiroExecutor, cliente.getDesativadoPor());

        cliente.reativar();

        assertTrue(cliente.getAtivo());
        assertNull(cliente.getDesativadoPor());
    }
}