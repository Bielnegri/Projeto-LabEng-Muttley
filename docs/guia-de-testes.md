# Muttley - Guia de Implementação de Testes

## 1. Objetivo

Este guia apresenta uma estratégia prática para criar testes confiáveis para o Muttley. Os cenários devem utilizar como referência:

- [Requisitos e regras de negócio](./requisitos-e-regras-de-negocio.md);
- contratos HTTP dos controllers;
- regras implementadas nos services;
- restrições das entidades e repositories.

## 2. Estratégia recomendada

Utilize uma pirâmide de testes:

```text
                 E2E
            poucos e críticos
          --------------------
           Integração/API
        fluxos e contratos reais
      ----------------------------
          Unitários e JPA
       rápidos, isolados e numerosos
```

### Distribuição sugerida

| Tipo | Objetivo | Quantidade relativa |
|---|---|:---:|
| Unitário | Validar regras dos services e classes utilitárias. | Alta |
| Repository | Validar queries, relacionamentos e persistência. | Média |
| Controller | Validar HTTP, DTOs e autorização. | Média |
| Integração | Validar fluxos com banco e transações. | Média |
| Contrato | Validar comunicação com microsserviços. | Baixa |
| E2E | Validar jornadas críticas completas. | Baixa |

Evite testar todas as combinações por E2E. Casos de borda devem ficar principalmente em testes unitários e de integração.

## 3. Ordem de implementação

### Prioridade 1 - Regras críticas

Implementar primeiro:

1. autenticação e autorização;
2. ciclo de vida dos eventos;
3. inscrição pública;
4. confirmação de presença;
5. conclusão do evento;
6. geração única de certificado;
7. geração única de medalha bronze;
8. isolamento de dados em `/api/me`.

Esses fluxos afetam segurança, integridade dos dados e entregas ao participante.

### Prioridade 2 - Integrações

1. publicação dos emails;
2. solicitação e consumo de QR Codes;
3. geração do PDF;
4. upload e leitura de assinatura.

### Prioridade 3 - Administração

1. CRUDs auxiliares;
2. filtros e ordenação;
3. estatísticas do painel;
4. respostas e mensagens secundárias.

## 4. Testes unitários prioritários

### `EventoService`

| ID | Cenário | Resultado esperado |
|---|---|---|
| UT-EVT-01 | Criar evento válido | Salvar com status `CRIADO`. |
| UT-EVT-02 | Horário final anterior ao inicial | Lançar `IllegalArgumentException`. |
| UT-EVT-03 | Horário fora de `HH:mm` | Lançar `IllegalArgumentException`. |
| UT-EVT-04 | Disciplina inexistente | Lançar `EntityNotFoundException`. |
| UT-EVT-05 | Atualizar evento finalizado | Lançar `IllegalStateException`. |
| UT-EVT-06 | Cancelar evento finalizado | Lançar `IllegalStateException`. |
| UT-EVT-07 | Cancelar evento já cancelado | Lançar `IllegalStateException`. |
| UT-EVT-08 | Concluir evento criado | Lançar `IllegalStateException`. |
| UT-EVT-09 | Concluir evento em andamento | Alterar para `FINALIZADO`. |
| UT-EVT-10 | Consultar evento após o início | Alterar para `EM_ANDAMENTO`. |

### `ParticipacaoService`

| ID | Cenário | Resultado esperado |
|---|---|---|
| UT-PAR-01 | Inscrição de nova pessoa | Criar pessoa mínima e participação. |
| UT-PAR-02 | Pessoa encontrada por CPF | Reutilizar pessoa. |
| UT-PAR-03 | Pessoa encontrada por email | Reutilizar pessoa. |
| UT-PAR-04 | CPF e email de pessoas distintas | Retornar conflito. |
| UT-PAR-05 | Pessoa já inscrita | Retornar conflito. |
| UT-PAR-06 | Evento iniciado | Rejeitar inscrição. |
| UT-PAR-07 | Confirmar presença válida | Marcar `presente=true`. |
| UT-PAR-08 | Confirmar novamente | Retornar conflito. |
| UT-PAR-09 | CPF não inscrito | Retornar `404`. |

### `CertificadoService`

| ID | Cenário | Resultado esperado |
|---|---|---|
| UT-CER-01 | Gerar para participação válida | Criar certificado com data atual. |
| UT-CER-02 | Participação já certificada | Não criar outro certificado. |
| UT-CER-03 | Participação inexistente | Lançar `EntityNotFoundException`. |
| UT-CER-04 | Certificado sem código | Gerar UUID e URLs. |
| UT-CER-05 | Atualizar assinatura por evento | Atualizar todos os certificados encontrados. |

### `MedalhaService`

| ID | Cenário | Resultado esperado |
|---|---|---|
| UT-MED-01 | Participação presente | Criar medalha `BRONZE`. |
| UT-MED-02 | Bronze já existente | Não criar outra. |
| UT-MED-03 | Participação ausente | Lançar `IllegalArgumentException`. |
| UT-MED-04 | Admin cria prata ou ouro | Persistir tipo informado. |

### Autenticação e utilitários

| ID | Unidade | Cenário |
|---|---|---|
| UT-AUT-01 | `PessoaService` | Codificar senha ao criar. |
| UT-AUT-02 | `PessoaService` | Preservar senha quando não alterada. |
| UT-AUT-03 | `JwtService` | Gerar claims e expiração corretos. |
| UT-AUT-04 | `HashIdService` | Codificar e decodificar ID. |
| UT-AUT-05 | `HashIdService` | Rejeitar hash inválido. |

## 5. Testes de repository

Utilize `@DataJpaTest`.

### Casos prioritários

- buscar participação por evento e pessoa;
- listar participações de uma pessoa com evento carregado;
- identificar inscrição duplicada;
- obter maior número de inscrição;
- encontrar certificado por código com todos os dados necessários;
- verificar certificado existente por participação;
- listar certificados de uma pessoa;
- verificar medalha por participação e tipo;
- contar certificados por período;
- filtrar eventos por status, tema e paginação.

### Restrições a validar

- email de pessoa único;
- código de certificado único;
- URL pública de certificado única;
- relacionamentos obrigatórios de participação.

Também devem existir testes que demonstrem as restrições ainda ausentes, como CPF único e certificado único por participação, caso elas sejam adicionadas.

## 6. Testes de controller e segurança

Utilize `@WebMvcTest`, MockMvc e tokens JWT de teste.

### Segurança

| ID | Cenário | Resultado |
|---|---|:---:|
| CT-SEC-01 | Rota pública sem token | `2xx` conforme recurso. |
| CT-SEC-02 | `/api/me` sem token | `401`. |
| CT-SEC-03 | Rota admin sem token | `401`. |
| CT-SEC-04 | USER em rota admin | `403`. |
| CT-SEC-05 | ADMIN em rota admin | Acesso permitido. |
| CT-SEC-06 | Token expirado ou inválido | `401`. |

### Validação

Testar `400 Bad Request` para:

- email inválido;
- CPF inválido no cadastro completo;
- campos obrigatórios ausentes;
- data de evento no passado;
- certificado com data futura;
- medalha sem tipo;
- referências obrigatórias ausentes.

Verificar também o formato:

```json
{
  "erros": [
    "campo: mensagem"
  ]
}
```

### Contratos HTTP importantes

- inscrição pública retorna `201`;
- login retorna token, tipo, expiração e usuário;
- conflito de inscrição retorna `409`;
- presença repetida retorna `409`;
- recurso inexistente retorna `404`;
- preview do certificado retorna `application/pdf` e `inline`;
- download retorna `application/pdf` e `attachment`;
- conclusão aceita apenas `multipart/form-data`.

## 7. Testes de integração

Utilize `@SpringBootTest` para os fluxos que atravessam múltiplos services e repositories.

### Fluxos prioritários

#### Cadastro e login

1. cadastrar primeira pessoa;
2. confirmar role `ADMIN`;
3. autenticar;
4. validar JWT e claims;
5. cadastrar segunda pessoa;
6. confirmar role `USER`.

#### Inscrição pública e conclusão de cadastro

1. criar evento disponível;
2. realizar inscrição sem autenticação;
3. confirmar criação de pessoa sem senha;
4. confirmar criação da participação;
5. completar cadastro;
6. autenticar;
7. impedir segunda conclusão do cadastro.

#### Presença e medalha

1. criar pessoa, evento e participação;
2. confirmar presença;
3. validar `presente=true`;
4. validar medalha `BRONZE`;
5. repetir confirmação;
6. validar conflito e ausência de duplicidade.

#### Conclusão do evento

1. preparar evento `EM_ANDAMENTO`;
2. criar participantes presentes e ausentes;
3. enviar assinatura;
4. concluir evento;
5. validar estado `FINALIZADO`;
6. validar medalhas somente para presentes;
7. validar certificados somente para presentes;
8. validar ausência de duplicidade;
9. validar mensagens publicadas.

#### Área do usuário

1. autenticar pessoa A;
2. criar dados para pessoas A e B;
3. consultar `/api/me/**`;
4. confirmar que somente dados de A são retornados.

### Banco recomendado

Use Testcontainers com MySQL para testes que dependem de:

- comportamento real de constraints;
- JPQL e fetch joins;
- transações;
- concorrência;
- diferenças entre H2 e MySQL.

## 8. Testes de contrato das integrações

### Kafka

Validar os payloads publicados em:

- `email.inscricao.confirmada`;
- `email.completar.cadastro`;
- `email.evento.cancelado`;
- `email.evento.concluido`;
- `email.certificado`;
- `qrcode.gerar.request`.

Para o consumidor de QR Code, validar:

- atualização da URL de inscrição;
- atualização da URL de confirmação;
- resposta com status `ERROR`;
- payload inválido.

Pode-se usar `spring-kafka-test` com Embedded Kafka ou Testcontainers Kafka.

### PDF e QR Code HTTP

Use WireMock ou MockWebServer para simular:

- PDF gerado com sucesso;
- serviço indisponível;
- timeout;
- resposta vazia ou inválida;
- download de QR Code;
- erro no download.

## 9. Testes end-to-end

Os E2E devem cobrir poucas jornadas de alto valor.

### Localização dos testes

Os testes E2E devem ficar no projeto do frontend React, pois representam jornadas iniciadas e executadas pela interface do usuário.

Estrutura sugerida no frontend:

```text
front-muttley/
├── e2e/
│   ├── autenticacao.spec.ts
│   ├── inscricao.spec.ts
│   ├── presenca.spec.ts
│   ├── certificado.spec.ts
│   ├── medalha.spec.ts
│   └── admin.spec.ts
└── playwright.config.ts
```

O repositório do backend deve concentrar:

- testes unitários;
- testes de repository;
- testes de controller e segurança;
- testes de integração da API;
- testes de contrato com os microsserviços.

Os testes E2E do frontend devem executar contra uma instância real do backend preparada para testes. Frontend e backend podem ser iniciados por scripts próprios, pipeline de CI ou Docker Compose.

Durante a execução E2E, o ambiente deve possuir:

- frontend React;
- backend Spring Boot;
- banco de dados isolado;
- dados iniciais previsíveis;
- serviços externos reais ou simulados para email, PDF e QR Code;
- mecanismo de limpeza ou restauração do banco entre cenários.

### E2E-01 - Jornada do participante

1. acessar listagem pública;
2. escolher evento;
3. realizar inscrição;
4. completar cadastro;
5. fazer login;
6. consultar a participação.

### E2E-02 - Presença e recompensa

1. participante inscrito confirma presença;
2. sistema informa sucesso;
3. usuário acessa suas medalhas;
4. medalha bronze é exibida.

### E2E-03 - Conclusão e certificado

1. admin autentica;
2. seleciona evento em andamento;
3. marca presentes;
4. envia assinatura;
5. conclui o evento;
6. participante acessa seus certificados;
7. abre o preview;
8. baixa o PDF.

### E2E-04 - Autorização

1. usuário comum autentica;
2. tenta acessar recurso administrativo;
3. acesso é negado;
4. administrador acessa o mesmo recurso.

### E2E-05 - Administração de medalha

1. administrador consulta participantes;
2. concede medalha de prata ou ouro;
3. participante consulta suas medalhas;
4. nova medalha é exibida com o tipo correto.

Playwright é a ferramenta recomendada e deve ser configurado no projeto React.

REST Assured, MockMvc ou Newman podem validar fluxos completos da API no backend, mas esses testes devem ser classificados como testes de integração/API, e não como E2E de interface.

## 10. Testes de concorrência e idempotência

São especialmente importantes:

- duas inscrições simultâneas da mesma pessoa no mesmo evento;
- geração simultânea do número de inscrição;
- duas confirmações de presença simultâneas;
- duas conclusões do mesmo evento;
- duas gerações de certificado para a mesma participação;
- duas gerações de bronze para a mesma participação.

O resultado esperado é não produzir registros duplicados.

Esses testes podem inicialmente falhar por ausência de constraints no banco. Nesse caso, devem orientar ajustes de integridade.

## 11. Testes de upload e arquivos

Validar:

- arquivo de assinatura válido;
- arquivo ausente;
- arquivo vazio;
- extensão diferente de imagem;
- MIME type inválido;
- arquivo acima do limite definido;
- nomes com caracteres especiais;
- falha de escrita;
- arquivo de assinatura inexistente ao gerar PDF;
- isolamento por diretório temporário.

Use `@TempDir` para evitar escrita na pasta real `uploads/`.

## 12. Requisitos para testes determinísticos

Antes de ampliar a suíte, recomenda-se:

1. abstrair `LocalDate.now()` e `LocalDateTime.now()` usando `Clock`;
2. configurar diretório de upload por propriedade;
3. desabilitar `MockDataInitializer` no profile de testes;
4. criar profile `test`;
5. evitar dependência real de Kafka e microsserviços nos testes comuns;
6. fornecer builders ou fixtures reutilizáveis;
7. limpar o banco entre cenários;
8. usar IDs e dados exclusivos por teste.

## 13. Organização sugerida

```text
src/test/java/com/fatec/muttley/
├── unit/
│   ├── evento/
│   ├── participacao/
│   ├── certificado/
│   ├── medalha/
│   └── security/
├── repository/
├── web/
├── integration/
├── contract/
└── support/
    ├── fixtures/
    ├── builders/
    └── containers/
```

## 14. Convenções

- nomear testes com comportamento esperado;
- utilizar padrão Arrange, Act, Assert;
- um teste deve validar uma regra principal;
- não compartilhar estado mutável entre testes;
- não depender da ordem de execução;
- validar resultado e efeitos colaterais;
- evitar mocks de classes simples de domínio;
- mockar integrações externas, não a regra que está sendo testada.

Exemplo:

```java
@Test
void deveRejeitarInscricaoQuandoPessoaJaParticipaDoEvento() {
    // Arrange
    // Act
    // Assert
}
```

## 15. Critério mínimo para a primeira suíte

A primeira entrega de testes deve garantir:

- autenticação e autorização;
- criação e transições de evento;
- inscrição pública;
- confirmação de presença;
- medalha bronze idempotente;
- certificado único;
- conclusão transacional do evento;
- isolamento dos dados do usuário;
- contratos públicos do certificado.

CRUDs simples e estatísticas podem ser cobertos depois que esses fluxos estiverem estáveis.
