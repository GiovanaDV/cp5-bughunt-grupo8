package br.com.fiap.petfiap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ConsultaPrecoTest {

    @Test
    void deveCobrar150ReaisQuandoConsultaForDeQualquerPorte() {
        // Arrange
        LocalDateTime data = LocalDateTime.now().plusDays(1);
        Atendimento pequeno = new ConsultaVeterinaria(1, "Mimi", "PEQUENO", "Ana", data);
        Atendimento medio = new ConsultaVeterinaria(2, "Mimi", "MEDIO", "Ana", data);
        Atendimento grande = new ConsultaVeterinaria(3, "Mimi", "GRANDE", "Ana", data);

        // Act + Assert
        assertEquals(150.0, pequeno.calcularPreco());
        assertEquals(150.0, medio.calcularPreco());
        assertEquals(150.0, grande.calcularPreco());
    }
}