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
| **Total de bugs corrigidos** | 11 / 12 |
| **Total de ajustes de Clean Code** | 6 / 6 |
| **Total de testes novos escritos** | 6 / 6 |
| **Suíte final (Run As → JUnit Test)** | 26 testes, 0 falhas |

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
| clean01|AtendimentoFactory.criar(): parâmetros p, t, n, po, tu, d |Nomes significativos / revelar a intenção. Nomes de uma letra obrigam o leitor a adivinhar o que cada parâmetro representa (e n, po, tu ainda se confundem entre si). |Renomeei para protocolo, tipo, petNome, porte, tutorNome, dataHora, sem alterar o comportamento. |
| clean02 |AtendimentoController: método privado calcularDescontoFidelidade(int) e bloco de comentário "Fidelidade (futuro)" |Código morto e YAGNI (You Aren't Gonna Need It). O método nunca era chamado e implementava uma funcionalidade ainda não aprovada, poluindo a classe. Também corrigi um comentário que citava ?tutorNome=Ana no endpoint errado. |Removi o método e o bloco de comentário. Ajustei o comentário do agendar para POST /api/atendimentos - Agendar atendimento. |
| clean03 |AgendaService.agendar(): System.out.println("Recibo: ...") |Separação de responsabilidades. O service de regra de negócio imprimia no console (efeito colateral de debug). Além disso, chamava salvo.getProtocolo() e quebrava com NullPointerException quando o repository era mockado. |Removi o println e passei a retornar direto repository.save(novo). |
| clean04 |GeradorProtocolo: System.out.println("GeradorProtocolo criado!") no construtor |Sem efeitos colaterais desnecessários / sem lixo de depuração. Um Singleton não deve poluir o console; o log ainda tornava visível o bug de várias instâncias, mas não pertence ao código final. |Removi o println do construtor, que ficou só com contador = 0. |
| clean05 |Atendimento e AgendaService: strings "AGENDADO", "CONCLUIDO" e "CANCELADO" repetidas em vários pontos |Evitar "magic strings" / DRY (Don't Repeat Yourself). Um erro de digitação passaria despercebido pelo compilador e mudar um status exigiria alterar vários lugares. |Criei as constantes STATUS_AGENDADO, STATUS_CONCLUIDO e STATUS_CANCELADO em Atendimento e as usei no model e no service. |
| clean06 |GeradorProtocolo: comentário dizia "Thread-safe", mas getInstancia() e proximo() não eram sincronizados |Comentários que não mentem / código coerente com a documentação. Em acesso concorrente, duas requisições poderiam criar dois geradores ou receber o mesmo protocolo. |Adicionei synchronized em getInstancia() e proximo(), tornando a classe realmente thread-safe como o comentário prometia. |

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

Eu li cada mensagem de falha como uma pista. `expected: <Rex> but was: <null>` diz que o campo nunca recebeu o valor, o que me levou ao `petNome = petNome;` no `AtendimentoBuilder.comPet()`, que atribuía o parâmetro a ele mesmo. `expected: <2> but was: <1>` em `deveGerarProtocolosSequenciais` apontou para o `getInstancia()` criando um gerador novo, com `contador = 0`, a cada chamada. `Expected ... to be thrown, but nothing was thrown` indicou validação ausente (bug05 e bug10). A mensagem dizia qual regra quebrou e em qual classe, sem eu precisar depurar.

Com `curl` eu teria que subir a aplicação, montar o JSON e conferir a resposta de cabeça, e a conferência valeria só para aquele momento. A suíte roda em segundos, sem banco e sem Spring. Ela vira regressão automática: o `BanhoPrecoTest` avisa se alguém inverter os preços do `Banho` de novo. Foi esse erro que passou despercebido porque os testes antigos só cobriam pontos e duração.

### 2. Mock e injeção de dependência (Aulas 13 a 15)

O `AgendaService` declara `@Autowired private AtendimentoRepository repository;` e não faz `new` do repository. Ele só diz do que precisa, e alguém de fora fornece. Em produção, quem injeta é o container do Spring: ele sobe o contexto, cria o bean real do repository (ligado ao Oracle) e o coloca no campo. No `AgendaServiceTest`, o Mockito assume esse papel. O `@Mock` cria um `AtendimentoRepository` falso e o `@InjectMocks` instancia o `AgendaService` e coloca o falso no campo.

O teste roda sem banco e sem Spring porque o service depende só do tipo `AtendimentoRepository`, e não de uma implementação concreta. Isso dá controle total sobre o cenário: `when(repository.findByPetNome("Rex")).thenReturn(List.of())` simula um pet sem agenda. O `AgendaServicePassadoTest` usa `verifyNoInteractions(repository)` para provar que o banco nem é consultado quando a data está no passado.

### 3. `==` vs `.equals()` (Aula 7)

Em objetos, `==` compara a referência (se é o mesmo objeto na memória) e `.equals()` compara o conteúdo. No `agendar()`, o atendimento já salvo e o `novo` são objetos diferentes. Dois `LocalDateTime` com a mesma data e hora, criados separadamente, são instâncias distintas, então `a.getDataHora() == novo.getDataHora()` dava `false`. O conflito nunca era detectado e o fluxo seguia até o `save()`. No teste, o mock devolvia `null` nesse ponto, daí o `NullPointerException` em vez de `HorarioOcupadoException`.

Com literais como `"Rex"` o `==` "funciona por sorte" porque a JVM guarda Strings literais iguais no String pool e reaproveita o mesmo objeto. Uma String vinda de JSON, do banco ou de `new String(...)` é outro objeto, e aí o `==` falha. A correção foi `a.getPetNome().equals(novo.getPetNome()) && a.getDataHora().equals(novo.getDataHora())`, que compara o valor e não o endereço.

### 4. Sobrescrita vs sobrecarga (Aula 7)

Na `Tosa`, o método `getDuracaoMinutos(String porte)` tinha o mesmo nome do `getDuracaoMinutos()` da `Atendimento`, mas outra lista de parâmetros. Isso é sobrecarga (overload): um método novo, com outra assinatura, que convive com o da superclasse. Sobrescrita (override) exige a mesma assinatura e substitui o comportamento herdado. Por isso o polimorfismo continuou chamando o `getDuracaoMinutos()` da `Atendimento`, que devolve 30, em vez dos 60 da Tosa.

Compilava sem erro porque sobrecarga é válida em Java. Com `@Override`, o compilador exigiria que o método existisse na superclasse com a mesma assinatura e acusaria erro na hora. Hoje a `Tosa` tem `@Override public int getDuracaoMinutos() { return 60; }`, que é a forma correta.

### 5. Singleton manual vs bean do Spring (Aula 14)

O `GeradorProtocolo` garante uma única instância: construtor `private` e acesso só pelo `getInstancia()`, que cria o objeto uma vez e o reaproveita. Assim o `proximo()` gera uma numeração global e sequencial, usada pelo `AtendimentoController`. O bug era que o `getInstancia()` fazia `return new GeradorProtocolo()` sem guardar o resultado no campo `static instancia`. Toda chamada criava um gerador novo com `contador = 0`, e todo protocolo saía como 1. Hoje a instância é guardada, e o `synchronized` em `getInstancia()` e `proximo()` faz o comentário "thread-safe" ser verdadeiro. Sem ele, duas requisições simultâneas poderiam criar dois geradores ou receber o mesmo número.

O `AgendaService` não corre esse risco porque o escopo padrão de um `@Service` já é singleton: o container cria uma instância e a injeta em quem precisar. Ninguém escreve `getInstancia()` à mão, então não há como esquecer de guardar a instância. O service também não guarda estado mutável, só o repository injetado, o que o mantém seguro para várias requisições.

### 6. Cobertura de testes: onde parar? (Aula 15)

Vale manter os verdes. O `ConsultaPrecoTest` (R$ 150 fixo) e o `AgendaServiceCancelamentoTest` (AGENDADO vira CANCELADO e salva) passaram de cara, mas protegem contra regressão. O próprio projeto mostrou isso: o preço do `Banho` estava invertido e ninguém notou, porque os testes antigos só cobriam pontos e duração. Um teste verde hoje fica vermelho no dia em que alguém mexer na regra sem querer, e custa pouco manter.

Em um projeto real com prazo, eu priorizaria nesta ordem. Primeiro o caminho feliz das regras centrais (agendar, cobrar o preço certo). Depois os caminhos de erro críticos, como horário ocupado, data no passado e cancelar atendimento concluído, porque foi neles que apareceram os bugs 05, 06, 07, 10 e 11. Por último eu não perseguiria 100% de cobertura: getters, setters e código trivial dão número bonito sem proteger nada. Cobertura mede linhas executadas, não regras verificadas. Prefiro poucos testes que protegem o contrato a muitos que só inflam a métrica.

## Parte 5 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```

```
