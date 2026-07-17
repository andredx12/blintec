\# 04 — Arquitetura do Sistema e Estrutura do Banco



\## 1. Decisão em Aberto Resolvida



\*\*Estoque mínimo para alerta:\*\* valor padrão de \*\*200 metros\*\*, configurável por tipo de tecido (campo `estoqueMinimo` na entidade `TipoTecido` — ver seção 3). Isso evita hardcode e permite ajuste futuro sem deploy de código novo.



\## 2. Arquitetura do Sistema



\### 2.1 Visão em Camadas (Backend)

┌─────────────────────────────────────┐

│         Controller (REST)            │  ← recebe HTTP, valida entrada

├─────────────────────────────────────┤

│         Service (regras de negócio)  │  ← RN001-RN025, máquina de estados

├─────────────────────────────────────┤

│         Repository (Spring Data JPA) │  ← acesso a dados

├─────────────────────────────────────┤

│         PostgreSQL                    │

└─────────────────────────────────────┘



Mesma arquitetura em camadas do Estoque API (Dia 1), mas com uma camada adicional:



\- \*\*DTO (Data Transfer Object):\*\* Controllers nunca expõem as entidades JPA diretamente na API — usam DTOs de entrada/saída. Isso evita vazar estrutura interna do banco e permite validações específicas por operação (ex: criar Pedido exige campos diferentes de atualizar status)

\- \*\*Security:\*\* camada transversal (Spring Security + JWT) que intercepta toda requisição, valida token e aplica RF17 (controle por perfil)



\### 2.2 Módulos do Sistema (pacotes)

com.blintec/

├── auth/          (login, JWT, perfis)

├── pedido/        (Pedido, ItemPedido, Cliente)

├── modelo/        (Modelo, TamanhoModelo)

├── enfesto/       (ProgramacaoEnfesto)

├── estoque/       (Rolo, TipoTecido, MovimentacaoEstoque)

├── producao/      (MovimentacaoProducao, máquina de estados)

├── dashboard/     (queries agregadas, somente leitura)

└── shared/        (exceptions, configs, utils)



Cada módulo segue a mesma estrutura interna (controller/service/repository/model), mas fica isolado por pacote — facilita manutenção e deixa claro onde cada regra de negócio "mora".



\### 2.3 Diagrama de Componentes

┌──────────────┐      HTTPS/JSON      ┌─────────────────┐

│   React SPA   │ ───────────────────▶ │  Spring Boot API │

│  (frontend)   │ ◀─────────────────── │   (backend)      │

└──────────────┘                       └────────┬─────────┘

│ JDBC

┌────────▼─────────┐

│   PostgreSQL      │

└───────────────────┘

Ambos os serviços (backend + banco) conteinerizados via Docker Compose.



\### 2.4 Autenticação e Autorização



\- Login gera um \*\*JWT\*\* (JSON Web Token) contendo `usuarioId` e `perfil`

\- Cada requisição subsequente envia o token no header `Authorization: Bearer <token>`

\- Controllers usam `@PreAuthorize` (Spring Security) pra restringir endpoints por perfil — ex: endpoints de cadastro de Pedido exigem `SUPERVISOR` ou `ADMINISTRADOR`



\## 3. Estrutura do Banco de Dados (DDL)



```sql

CREATE TYPE perfil\_usuario AS ENUM ('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR');

CREATE TYPE status\_pedido AS ENUM (

&#x20;   'AGUARDANDO\_PROGRAMACAO', 'PROGRAMADO', 'EM\_CORTE',

&#x20;   'EM\_COSTURA', 'EM\_ARREMATACAO', 'EM\_EXPEDICAO', 'ENTREGUE'

);

CREATE TYPE genero\_tamanho AS ENUM ('MASCULINO', 'FEMININO');

CREATE TYPE tipo\_movimentacao\_estoque AS ENUM ('ENTRADA', 'SAIDA');



CREATE TABLE usuario (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   nome VARCHAR(150) NOT NULL,

&#x20;   email VARCHAR(150) UNIQUE NOT NULL,

&#x20;   senha\_hash VARCHAR(255) NOT NULL,

&#x20;   perfil perfil\_usuario NOT NULL,

&#x20;   ativo BOOLEAN NOT NULL DEFAULT TRUE,

&#x20;   criado\_em TIMESTAMP NOT NULL DEFAULT NOW()

);



CREATE TABLE cliente (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   nome VARCHAR(150) NOT NULL,

&#x20;   cnpj\_cpf VARCHAR(20) UNIQUE NOT NULL,

&#x20;   criado\_em TIMESTAMP NOT NULL DEFAULT NOW()

);



CREATE TABLE modelo (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   nome VARCHAR(50) UNIQUE NOT NULL,

&#x20;   consumo\_tecido\_por\_peca NUMERIC(6,2) NOT NULL CHECK (consumo\_tecido\_por\_peca > 0)

);



CREATE TABLE tamanho\_modelo (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   modelo\_id BIGINT NOT NULL REFERENCES modelo(id),

&#x20;   tamanho VARCHAR(10) NOT NULL,

&#x20;   genero genero\_tamanho NOT NULL,

&#x20;   tamanho\_equivalente\_id BIGINT REFERENCES tamanho\_modelo(id),

&#x20;   UNIQUE (modelo\_id, tamanho)

);



CREATE TABLE tipo\_tecido (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   nome VARCHAR(100) NOT NULL,

&#x20;   estoque\_minimo NUMERIC(8,2) NOT NULL DEFAULT 200.00 CHECK (estoque\_minimo >= 0)

);



CREATE TABLE rolo (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   codigo VARCHAR(30) UNIQUE NOT NULL,

&#x20;   tipo\_tecido\_id BIGINT NOT NULL REFERENCES tipo\_tecido(id),

&#x20;   cor VARCHAR(50) NOT NULL,

&#x20;   metragem\_inicial NUMERIC(8,2) NOT NULL CHECK (metragem\_inicial > 0),

&#x20;   saldo\_atual NUMERIC(8,2) NOT NULL CHECK (saldo\_atual >= 0),

&#x20;   criado\_em TIMESTAMP NOT NULL DEFAULT NOW()

);



CREATE TABLE pedido (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   cliente\_id BIGINT NOT NULL REFERENCES cliente(id),

&#x20;   numero\_pedido VARCHAR(30) UNIQUE NOT NULL,

&#x20;   modelo\_id BIGINT NOT NULL REFERENCES modelo(id),

&#x20;   cor VARCHAR(50) NOT NULL,

&#x20;   capa\_extra BOOLEAN NOT NULL DEFAULT FALSE,

&#x20;   data\_entrega DATE NOT NULL,

&#x20;   status status\_pedido NOT NULL DEFAULT 'AGUARDANDO\_PROGRAMACAO',

&#x20;   criado\_em TIMESTAMP NOT NULL DEFAULT NOW(),

&#x20;   criado\_por BIGINT NOT NULL REFERENCES usuario(id)

);



CREATE TABLE item\_pedido (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   pedido\_id BIGINT NOT NULL REFERENCES pedido(id),

&#x20;   tamanho\_modelo\_id BIGINT NOT NULL REFERENCES tamanho\_modelo(id),

&#x20;   quantidade INT NOT NULL CHECK (quantidade > 0)

);



CREATE TABLE programacao\_enfesto (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   pedido\_id BIGINT UNIQUE NOT NULL REFERENCES pedido(id),

&#x20;   data\_programacao TIMESTAMP NOT NULL DEFAULT NOW(),

&#x20;   ajustado\_manualmente BOOLEAN NOT NULL DEFAULT FALSE,

&#x20;   programado\_por BIGINT NOT NULL REFERENCES usuario(id),

&#x20;   consumo\_tecido\_total NUMERIC(8,2) NOT NULL CHECK (consumo\_tecido\_total > 0)

);



CREATE TABLE movimentacao\_estoque (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   rolo\_id BIGINT NOT NULL REFERENCES rolo(id),

&#x20;   tipo tipo\_movimentacao\_estoque NOT NULL,

&#x20;   quantidade NUMERIC(8,2) NOT NULL CHECK (quantidade > 0),

&#x20;   pedido\_id BIGINT REFERENCES pedido(id),

&#x20;   data TIMESTAMP NOT NULL DEFAULT NOW(),

&#x20;   usuario\_id BIGINT NOT NULL REFERENCES usuario(id)

);



CREATE TABLE movimentacao\_producao (

&#x20;   id BIGSERIAL PRIMARY KEY,

&#x20;   pedido\_id BIGINT NOT NULL REFERENCES pedido(id),

&#x20;   etapa\_anterior status\_pedido NOT NULL,

&#x20;   etapa\_nova status\_pedido NOT NULL,

&#x20;   usuario\_id BIGINT NOT NULL REFERENCES usuario(id),

&#x20;   data\_hora TIMESTAMP NOT NULL DEFAULT NOW()

);



\-- Índices para consultas frequentes do dashboard e telas de listagem

CREATE INDEX idx\_pedido\_status ON pedido(status);

CREATE INDEX idx\_pedido\_data\_entrega ON pedido(data\_entrega);

CREATE INDEX idx\_movimentacao\_estoque\_rolo ON movimentacao\_estoque(rolo\_id);

CREATE INDEX idx\_movimentacao\_producao\_pedido ON movimentacao\_producao(pedido\_id);

```



\## 4. Notas de Design do Banco



1\. \*\*`tipo\_tecido` separado de `rolo`\*\* — antes, o Dia 2 tinha `tipoTecido` como atributo texto direto no Rolo. Agora virou tabela própria porque o estoque mínimo (200m) é definido \*\*por tipo de tecido\*\*, não por rolo individual (múltiplos rolos podem ser do mesmo tipo).

2\. \*\*`NUMERIC` em vez de `FLOAT`\*\* para metragens e consumo — evita erro de arredondamento de ponto flutuante em valores financeiros/físicos importantes (prática padrão em sistemas de produção/financeiro).

3\. \*\*`CHECK` constraints\*\* no banco (não só validação no backend) — garante integridade mesmo se alguém inserir dado direto no banco, ou se houver bug na camada de aplicação.

4\. \*\*Nenhuma tabela permite `DELETE`\*\* de `movimentacao\_estoque` ou `movimentacao\_producao` — reforçando RNF07 (não há operação de exclusão prevista nem no banco nem na API para essas tabelas).

