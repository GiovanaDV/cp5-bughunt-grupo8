# Checkpoint 5 — Bug Hunt PetFiap

> Copie este arquivo para a raiz do seu repositório com o nome **README.md**
> e preencha todas as seções.

## Identificação

**Grupo:** ___

| Integrante | RM | Turma |
|---|---|---|
| Allan de Souza Cardoso | 561721 | 2CCPH |
| Eduardo Bacelar Rudner | 564925 | 2CCPH |
| Giovana Dias Valentini | 562390 | 2CCPH |
| Júlia Borges Paschoalinoto | 564725 | 2CCPH |
| Raquel Amaral de Oliveira | 566491 | 2CCPH |

| Campo | |
|---|---|
| **Total de bugs corrigidos** | ___ / 12 |
| **Total de ajustes de Clean Code** | ___ / 6 |
| **Total de testes novos escritos** | ___ / 6 |
| **Suíte final (Run As → JUnit Test)** | ___ testes, ___ falhas |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 |Rodei a suíte: deveManterUmaUnicaInstancia falhou (esperava o mesmo objeto, veio outro) e deveGerarProtocolosSequenciais falhou (esperado 2, veio 1). O console imprimiu "GeradorProtocolo criado!" 5 vezes. |GeradorProtocolo.getInstancia() (~linha 16): fazia return new GeradorProtocolo() sem guardar a instância no campo static, então cada chamada criava um gerador novo com contador = 0. |Atribuí o resultado ao campo: instancia = new GeradorProtocolo(); dentro do if (instancia == null) e retornei instancia. |Padrão Singleton (Aula 14); atributos static |
| bug02 |devePreencherOsDadosDoPetNaConsulta falhou: esperado <Mimi>, veio <null>. |ConsultaVeterinaria, construtor com parâmetros (~linha 14): chamava super() (construtor vazio) e descartava os parâmetros, deixando todos os campos nulos. |Troquei por super(protocolo, petNome, petPorte, tutorNome, dataHora);. |Herança e construtores; chamada ao construtor da superclasse com super (POO) |
| bug03 |deveCriarTosaQuandoTipoForTosa falhou: tipo esperado Tosa, veio Banho. |AtendimentoFactory.criar() (~linha 15): o case "TOSA" instanciava new Banho(...) (copiar e colar sem ajustar). |Troquei por case "TOSA" -> new Tosa(p, n, po, tu, d);. |Padrão Factory (Aula 14); polimorfismo |
| bug04 |deveMontarAtendimentoCompleto falhou: esperado <Rex>, veio <null>. O Eclipse avisava "The assignment to variable petNome has no effect". |AtendimentoBuilder.comPet() (~linha 24): petNome = petNome; atribuía o parâmetro a ele mesmo, e o atributo da classe nunca recebia o valor. |Troquei por this.petNome = petNome;. |Padrão Builder (Aula 14); palavra-chave this e escopo de variáveis (POO) |
| bug05 |deveRecusarMontagemSemNomeDoPet e deveRecusarMontagemSemPorte falharam: esperava IllegalArgumentException, mas nada foi lançado. |AtendimentoBuilder.construir() (~linha 40): não validava os campos obrigatórios (o comentário delegava a validação ao controller), então o objeto nascia inválido. |Adicionei, antes do return, a validação de petNome e petPorte (nulo ou em branco) lançando IllegalArgumentException com mensagem clara, e atualizei o comentário. |Padrão Builder: objeto só nasce válido; validação e exceções (Aula 11) |
| bug06 |deveLancarExcecaoQuandoAtendimentoNaoExiste falhou: esperava AtendimentoNaoEncontradoException, mas nada foi lançado (o método retornava null). |AgendaService.buscarPorId() (~linha 36): o catch (Exception e) { return null; } engolia a exceção do orElseThrow. Esse null também causaria NullPointerException em concluir() e cancelar(). |Removi o try/catch, mantendo só o orElseThrow, para a exceção chegar a quem chamou (o controller trata e devolve 404). |Exceções customizadas (Aula 11); catch genérico que engole erro |
| bug07 |deveRecusarAgendamentoComHorarioJaOcupado falhou com NullPointerException em AgendaService.agendar (linha 30) em vez de HorarioOcupadoException. |AgendaService.agendar() (~linha 24): usava == para comparar petNome (String) e dataHora (LocalDateTime), o que compara referências de objeto e não o conteúdo. O conflito nunca era detectado, o fluxo chegava ao save() e o resultado do mock era null. |Troquei por a.getPetNome().equals(novo.getPetNome()) && a.getDataHora().equals(novo.getDataHora()) |== vs equals(); comparação de objetos (POO); regra de conflito de horário no service |
| bug08 |Escrevi o teste01 (preço do Banho por porte) e ele falhou em BanhoPrecoTest:19: expected: <60.0> but was: <100.0>. |Banho.calcularPreco() (~linha 25): valores invertidos, PEQUENO retornava 100 e GRANDE 60. Os testes existentes só cobriam pontos e duração, então ninguém notou. |Ajustei para PEQUENO 60, MEDIO 80 e GRANDE 100, conforme o contrato. |Regra de negócio no model; cobertura de testes (JUnit, Aula 15) |
| bug09 |Escrevi o teste02 (duração da Tosa) e ele falhou: expected: <60> but was: <30>. |Tosa (~linha 38): o método getDuracaoMinutos(String porte) tem assinatura diferente da classe pai, então é sobrecarga e não sobrescrita. O polimorfismo continuava usando o método da Atendimento, que devolve 30. |Troquei por @Override public int getDuracaoMinutos() retornando 60. |Sobrescrita (@Override) vs sobrecarga; polimorfismo (POO) |
| bug10 |Escrevi o teste03 (cancelar atendimento concluído) e ele falhou: nada foi lançado e o atendimento realizado virou CANCELADO. |Atendimento.cancelar() (~linha 62): só fazia status = "CANCELADO", sem checar o status atual, ao contrário do concluir(). |Adicionei a checagem !"AGENDADO".equals(status) lançando StatusInvalidoException. |Encapsulamento das regras de transição de status no model; exceções customizadas (Aula 11) |
| bug11 |Escrevi o teste04 (agendar com data de ontem) e ele falhou: nenhuma IllegalArgumentException foi lançada e o repository foi consultado. |AgendaService.agendar() (~linha 22): não validava a data antes de acessar o repository. |Adicionei a checagem isBefore(LocalDateTime.now()) no início do método, lançando IllegalArgumentException. |Validação antes de acessar o banco (fail fast); Mockito verifyNoInteractions (Aula 15) |
| bug12 | | | | |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | | | |
| clean02 | | | |
| clean03 | | | |
| clean04 | | | |
| clean05 | | | |
| clean06 | | | |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

> Uma linha por teste novo (`test: ...`). "Regra coberta" é o comportamento do
> contrato (seção 3 do enunciado) que o teste protege. Em "Resultado", diga se o
> teste ficou vermelho ao ser escrito (revelou bug — qual?) ou verde de cara
> (regra já estava correta).

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 |BanhoPrecoTest.deveCobrarPrecoPorPorteQuandoCalcularPrecoDoBanho |Banho: preço por porte (PEQUENO R$ 60, MEDIO R$ 80, GRANDE R$ 100). |Vermelho: expected: <60.0> but was: <100.0>. Revelou o bug08 (preço do Banho invertido). |
| teste02 |TosaDuracaoTest.deveDurar60MinutosQuandoAtendimentoForTosa |Tosa dura 60 minutos. |Vermelho: expected: <60> but was: <30>. Revelou o bug09 (sobrecarga em vez de sobrescrita). |
| teste03 |CancelamentoTest.deveRecusarCancelamentoQuandoAtendimentoJaEstiverConcluido |cancelar() recusa atendimento já realizado (CONCLUIDO) com StatusInvalidoException. |Vermelho: Expected StatusInvalidoException to be thrown, but nothing was thrown. Revelou o bug10 (cancelar() sem validar status). |
| teste04 |AgendaServicePassadoTest.deveRecusarAgendamentoQuandoDataForNoPassado |Agendar com data/hora no passado lança IllegalArgumentException e o banco nem é consultado. |Vermelho: esperava IllegalArgumentException, veio NullPointerException. Revelou o bug11 (sem validação de data). |
| teste05 |ConsultaPrecoTest.deveCobrar150ReaisQuandoConsultaForDeQualquerPorte |Consulta tem preço fixo de R$ 150 em qualquer porte. |Verde de cara: a regra já estava correta, o teste protege contra regressões. |
| teste06 |AgendaServiceCancelamentoTest.deveCancelarAtendimentoQuandoStatusForAgendado |Cancelar pelo service um atendimento AGENDADO muda o status para CANCELADO e salva. |Verde de cara: a regra já estava correta, o teste protege contra regressões. |

---

## Parte 4 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do
> projeto como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. A suíte como contrato (Aula 15)
O projeto chegou com 20 testes, 9 vermelhos. Descreva como você usou as
mensagens de falha (ex.: `expected: <Rex> but was: <null>`) para caçar os bugs.
O que a suíte de testes tem de melhor do que testar tudo na mão com curl?

### 2. Mock e injeção de dependência (Aulas 13 a 15)
No `AgendaServiceTest`, o `@Mock` cria um `AtendimentoRepository` falso e o
`@InjectMocks` o injeta no service. Explique a relação disso com o `@Autowired`
que o Spring faz em produção — quem "injeta" em cada mundo, e por que o teste
consegue rodar sem banco e sem subir o Spring?

### 3. `==` vs `.equals()` (Aula 7)
Um dos bugs fazia o agendamento duplicado passar pela verificação de conflito.
Explique por que `==` entre Strings e `LocalDateTime` falhou aqui, por que ele
"funciona por sorte" com literais como `"Rex"`, e o que a sua correção mudou.

### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: um método parecia sobrescrever
`getDuracaoMinutos`, mas na verdade criava uma assinatura nova. Explique a
diferença entre override e overload nesse caso e por que a anotação `@Override`
teria impedido o bug.

### 5. Singleton manual vs bean do Spring (Aula 14)
O `GeradorProtocolo` é um Singleton escrito à mão e causou um dos bugs.
Explique o que ele garante, qual foi o bug, e por que o `AgendaService`
(`@Service`) não corre o mesmo risco no container do Spring.

### 6. Cobertura de testes: onde parar? (Aula 15)
Dos 6 testes novos que você escreveu, alguns ficaram vermelhos (revelaram
bugs) e outros verdes de cara (regras já corretas). Vale a pena manter os que
ficaram verdes? Em um projeto real com prazo, o que você priorizaria testar:
caminho feliz, caminhos de erro, ou 100% de cobertura? Justifique.

---

## Parte 5 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```

```
