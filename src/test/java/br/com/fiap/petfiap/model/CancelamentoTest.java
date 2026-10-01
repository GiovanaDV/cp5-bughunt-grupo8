package br.com.fiap.petfiap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.fiap.petfiap.exception.StatusInvalidoException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class CancelamentoTest {

    @Test
    void deveRecusarCancelamentoQuandoAtendimentoJaEstiverConcluido() {
        // Arrange
        Atendimento banho = new Banho(1, "Rex", "PEQUENO", "Ana", LocalDateTime.now().plusDays(1));
        banho.concluir();

        // Act + Assert
        assertThrows(StatusInvalidoException.class, () -> banho.cancelar());
        assertEquals("CONCLUIDO", banho.getStatus());
    }
}