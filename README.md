# BackEnd_Grupo7
BackEnd do projeto de extensão

## Docker com H2 (testes e demonstração)

Execute na raiz do projeto:

```powershell
docker build -t backend-grupo7 -f Docker/Dockerfile .
docker run -d --name backend-grupo7 -p 8080:8080 -e SPRING_PROFILES_ACTIVE=h2 backend-grupo7
docker logs -f backend-grupo7
```

A API fica disponível em `http://localhost:8080`; Swagger em
`http://localhost:8080/swagger-ui/index.html`.

- O perfil `h2` cria um banco em memória e carrega os dados de demonstração de `data.sql`.
- Os dados são perdidos e recriados ao reiniciar a aplicação. Não use esse perfil em produção.
- O console H2 fica desabilitado. Não é necessário instalar MySQL para esse perfil.
- Sem `SPRING_PROFILES_ACTIVE=h2`, a configuração padrão usa MySQL e as variáveis `DB_*`.
- `-f Docker/Dockerfile .` é usado apenas no build, não no comando de execução.
- Para ver todos os containers, use `docker ps -a`. Para reiniciar o existente, use
  `docker start backend-grupo7` em vez de criar outro com o mesmo nome.

## Geração de etiquetas (RabbitMQ)

O backend **não gera o PDF nem envia e-mail**: ele é apenas o *producer*. Valida a solicitação, consulta o item no
próprio banco e publica uma mensagem no RabbitMQ. O microserviço `etiquetas` (consumer) gera o PDF e o envia por
e-mail ao Marcos.

```text
Frontend -> POST /etiquetas/{itemId}/fila -> BackEnd_Grupo7 -> banco próprio
BackEnd_Grupo7 (producer) -> RabbitMQ -> etiquetas (consumer) -> PDF -> e-mail
```

### Endpoint

`POST /etiquetas/{itemId}/fila` (exige login, como os demais endpoints).

| Status | Quando |
|--------|--------|
| `202` | Solicitação publicada e **confirmada pelo broker**. Corpo: `{"solicitacaoId", "itemId", "status": "ACEITA", "mensagem"}`. Significa apenas "aceita": o PDF ainda será gerado e enviado. |
| `400` | `itemId` inválido ou item sem `codigoInterno`. |
| `401` | Usuário não autenticado. |
| `404` | Item inexistente ou inativo (nada é publicado). |
| `503` | RabbitMQ indisponível, mensagem não confirmada no tempo limite ou sem rota até a fila. |

O endpoint síncrono `GET /etiquetas/{id}/pdf` deixou de existir neste serviço (a geração do PDF é do consumer). Por isso
foram removidos `etiqueta.jrxml` e as dependências `jasperreports` e `commons-collections4`, e foi adicionado
`spring-boot-starter-amqp`.

### RabbitMQ

| Item | Nome |
|------|------|
| Exchange (direct, durável) | `exchange_etiqueta` |
| Routing key | `routing_key_etiqueta` |
| Fila (durável) | `queue_etiqueta` |

- Mensagens JSON, persistentes (`delivery_mode=2`), com `messageId` igual ao `solicitacaoId`.
- *Publisher confirms* + `mandatory` + *returns*: o `202` só é devolvido depois que o broker confirma que a mensagem
  chegou à fila; qualquer falha vira `503` (detalhes apenas no log).
- Exchange, fila e binding são declarados por este serviço e pelo consumer com **a mesma definição** (declaração
  idempotente). Não acrescente argumentos (ex.: `x-dead-letter-exchange`) à fila em apenas um dos lados: o RabbitMQ
  recusa a declaração (`PRECONDITION_FAILED`) e não altera os argumentos de uma fila já criada. A DLQ
  (`queue_etiqueta_dlq`) é responsabilidade do consumer.

Mensagem publicada:

```json
{
  "solicitacaoId": "UUID gerado a cada solicitação (chave de idempotência do consumer)",
  "itemId": 1,
  "codigoInterno": "AMOR-001",
  "marca": "Cofap",
  "descricao": "Amortecedor Dianteiro Direito",
  "dataCadastro": "2025-09-03T23:00:15 (ISO-8601) ou null"
}
```

### Variáveis de ambiente

| Variável | Padrão | Uso |
|----------|--------|-----|
| `RABBITMQ_HOST` | `localhost` | Host do broker |
| `RABBITMQ_PORT` | `5672` | Porta AMQP |
| `RABBITMQ_USERNAME` | `admin` | Usuário |
| `RABBITMQ_PASSWORD` | `admin` | Senha. O padrão é apenas o do broker local do `compose.yml`; em qualquer outro ambiente defina a variável |
| `RABBITMQ_CONNECTION_TIMEOUT` | `5s` | Tempo máximo para conectar ao broker |
| `ETIQUETA_FILA_TIMEOUT_CONFIRMACAO` | `5s` | Tempo máximo de espera pela confirmação do broker |

Os mesmos nomes (`RABBITMQ_*`) são usados pelo consumer, então os dois serviços devem apontar para o mesmo broker.
Não há trigger SQL nesse fluxo: o gatilho é a própria requisição HTTP do botão "Gerar Etiqueta".

### RabbitMQ local

Suba o broker com o `compose.yml` do projeto `etiquetas` (painel em `http://localhost:15672`) e inicie o consumer:

```powershell
cd ..\etiquetas\etiquetas
docker compose up -d
```

O passo a passo do consumer (e-mail do Marcos, SMTP, DLQ) está em `etiquetas/etiquetas/README.md`.

### Testes

`mvn test` cobre o endpoint (`EtiquetaControllerTest`), o serviço (`EtiquetaServiceTest`), a publicação com
confirms/returns (`SolicitacaoEtiquetaPublisherTest`) e a topologia (`RabbitEtiquetaConfigTest`).