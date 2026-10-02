package br.com.fiap.petfiap.factory;

import br.com.fiap.petfiap.model.Atendimento;
import br.com.fiap.petfiap.model.Banho;
import br.com.fiap.petfiap.model.ConsultaVeterinaria;
import br.com.fiap.petfiap.model.Tosa;

import java.time.LocalDateTime;

// Padrao Factory (Aula 14): o unico lugar que conhece as subclasses concretas.
// O resto do codigo depende apenas do tipo abstrato Atendimento.
public class AtendimentoFactory {

	public static Atendimento criar(int protocolo, String tipo, String petNome, String porte,
            String tutorNome, LocalDateTime dataHora) {
			return switch (tipo) {
				case "BANHO" -> new Banho(protocolo, petNome, porte, tutorNome, dataHora);
				case "TOSA" -> new Tosa(protocolo, petNome, porte, tutorNome, dataHora);
				case "CONSULTA" -> new ConsultaVeterinaria(protocolo, petNome, porte, tutorNome, dataHora);
				default -> throw new IllegalArgumentException("Tipo invalido: " + tipo);
		};
	}
}
