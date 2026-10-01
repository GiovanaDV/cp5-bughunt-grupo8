package br.com.fiap.petfiap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TosaDuracaoTest {

    @Test
    void deveDurar60MinutosQuandoAtendimentoForTosa() {
        // Arrange
        Atendimento tosa = new Tosa(1, "Rex", "PEQUENO", "Ana", LocalDateTime.now().plusDays(1));

        // Act
        int duracao = tosa.getDuracaoMinutos();

        // Assert
        assertEquals(60, duracao);
    }
}