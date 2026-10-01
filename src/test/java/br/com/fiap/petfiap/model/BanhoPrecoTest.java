package br.com.fiap.petfiap.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class BanhoPrecoTest {

    @Test
    void deveCobrarPrecoPorPorteQuandoCalcularPrecoDoBanho() {
        // Arrange
        LocalDateTime data = LocalDateTime.now().plusDays(1);
        Banho pequeno = new Banho(1, "Rex", "PEQUENO", "Ana", data);
        Banho medio = new Banho(2, "Rex", "MEDIO", "Ana", data);
        Banho grande = new Banho(3, "Rex", "GRANDE", "Ana", data);

        // Act + Assert
        assertEquals(60.0, pequeno.calcularPreco());
        assertEquals(80.0, medio.calcularPreco());
        assertEquals(100.0, grande.calcularPreco());
    }
}