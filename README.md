# \# Blintec — Sistema de Planejamento e Controle de Produção

# 

# Sistema completo de gestão de produção para uma fábrica de coletes balísticos, cobrindo o fluxo real de operação: cadastro de pedidos, controle de estoque de tecido, programação de enfesto (corte), acompanhamento de produção por etapas e um dashboard gerencial.

# 

# Projeto desenvolvido do zero, começando pela modelagem de requisitos e arquitetura antes de qualquer linha de código, e evoluindo em etapas diárias documentadas ao longo de todo o desenvolvimento.

# 

# \## Telas

# 

# \*\*Login\*\*

# !\[Login](./docs/screenshots/login.png)

# 

# \*\*Pedidos\*\*

# !\[Pedidos](./docs/screenshots/pedidos.png)

# 

# \*\*Detalhe do pedido — produção e enfesto\*\*

# !\[Detalhe do pedido](./docs/screenshots/detalhe-pedido.png)

# 

# \*\*Estoque\*\*

# !\[Estoque](./docs/screenshots/estoque.png)

# 

# \*\*Dashboard\*\*

# !\[Dashboard](./docs/screenshots/dashboard.png)

# 

# \## Funcionalidades

# 

# \- Autenticação com JWT e controle de acesso por perfil (Operador, Supervisor, Administrador)

# \- Cadastro de pedidos com cálculo automático de componentes da capa a partir da grade de tamanhos

# \- Controle de estoque de rolos de tecido, com entrada, saída e alerta de estoque mínimo configurável

# \- Programação de enfesto: sugestão automática de rolos disponíveis e confirmação manual, com débito de estoque transacional

# \- Acompanhamento de produção por máquina de estados (Corte → Costura → Arrematação → Expedição → Entregue), com histórico auditável de cada movimentação

# \- Dashboard com resumo de pedidos por status, pedidos atrasados ou próximos do vencimento, indicadores de estoque e consumo de tecido por período

# 

# \## Arquitetura

# 

# Backend em arquitetura de camadas (Controller → Service → Repository), organizado por módulo de domínio (`auth`, `pedido`, `estoque`, `enfesto`, `producao`, `dashboard`). Banco de dados PostgreSQL com schema versionado via Flyway. Frontend em React consumindo a API via REST.

# 

# A modelagem completa do sistema — requisitos, casos de uso, regras de negócio, diagrama de entidades, especificação das APIs e backlog em formato Scrum — está documentada na pasta \[`docs/`](./docs) e foi escrita antes da implementação.

# 

# \## Tecnologias

# 

# \*\*Backend:\*\* Java 21, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL, Flyway, JWT (JJWT)

# \*\*Frontend:\*\* \[React, Vite, React Router, Axios](https://github.com/andredx12/blintec-frontend)

# \*\*Infraestrutura:\*\* Docker e Docker Compose

# 

# \## Como rodar

# 

# Requer Docker e Docker Compose instalados, e o repositório do frontend clonado na mesma pasta pai que este repositório:

# 

# Projetos/

# ├── blintec/

# └── blintec-frontend/

# 

# 

# Clone os dois repositórios:

# 

# ```bash

# git clone https://github.com/andredx12/blintec.git

# git clone https://github.com/andredx12/blintec-frontend.git

# ```

# 

# Na pasta `blintec`, suba tudo com um único comando:

# 

# ```bash

# docker compose up

# ```

# 

# Isso inicia o banco PostgreSQL, aplica as migrations, sobe o backend e o frontend. A aplicação fica disponível em `http://localhost:5173`.

# 

# Um usuário administrador é criado automaticamente na primeira execução:

# 

# E-mail: admin@blintec.com

# Senha: admin123

# 

# 

# \## Estrutura do repositório

# 

# blintec/

# ├── backend/ # API REST em Spring Boot

# ├── docs/ # Modelagem completa do sistema (requisitos, arquitetura, APIs, backlog)

# └── docker-compose.yml

# 

# 

# \## Documentação completa

# 

# A pasta \[`docs/`](./docs) contém a modelagem completa do projeto, escrita antes da implementação:

# 

# 1\. \[Requisitos e casos de uso](./docs/01-requisitos-casos-de-uso.md)

# 2\. \[Diagrama de entidades](./docs/02-diagrama-entidades.md)

# 3\. \[Regras de negócio detalhadas](./docs/03-regras-de-negocio-detalhadas.md)

# 4\. \[Arquitetura e banco de dados](./docs/04-arquitetura-e-banco.md)

# 5\. \[Especificação das APIs REST](./docs/05-especificacao-api-rest.md)

# 6\. \[Backlog Scrum](./docs/06-backlog-scrum.md)

