package br.com.fiap.petfiap.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.fiap.petfiap.model.Atendimento;
import br.com.fiap.petfiap.model.Banho;
import br.com.fiap.petfiap.repository.AtendimentoRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgendaServiceCancelamentoTest {

    @Mock
    private AtendimentoRepository repository;

    @InjectMocks
    private AgendaService service;

    @Test
    void deveCancelarAtendimentoQuandoStatusForAgendado() {
        // Arrange
        Atendimento banho = new Banho(1, "Rex", "PEQUENO", "Ana", LocalDateTime.now().plusDays(1));
        when(repository.findById(1L)).thenReturn(Optional.of(banho));
        when(repository.save(any(Atendimento.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Atendimento resultado = service.cancelar(1L);

        // Assert
        assertEquals("CANCELADO", resultado.getStatus());
        verify(repository).save(banho);
    }
}