\# 01 — Requisitos e Casos de Uso



\## 1. Requisitos Funcionais



| ID | Descrição |

|----|-----------|

| RF01 | O sistema deve permitir cadastro de pedidos (cliente, número, modelo, cor, grade de tamanhos, quantidades, data de entrega) |

| RF02 | O sistema deve permitir importação de pedidos via planilha Excel |

| RF03 | O sistema deve controlar o status do pedido em 7 etapas (Aguardando Programação → Programado → Corte → Costura → Arrematação → Expedição → Entregue) |

| RF04 | O sistema deve calcular automaticamente a quantidade de componentes de cada capa (Frente, Costa, Ombrinhos, Palas, Abas) a partir da quantidade de peças |

| RF05 | O sistema deve aplicar regra de capa extra quando informada no pedido, dobrando a quantidade de capas daquele item |

| RF06 | O sistema deve gerar programação de enfesto automaticamente, considerando modelo, tamanho, cor, quantidade e capa extra |

| RF07 | O sistema deve permitir ajuste manual da programação de enfesto, registrando o usuário responsável |

| RF08 | O sistema deve controlar entrada e saída de rolos de tecido (código, tipo, cor, metragem inicial, saldo atual) |

| RF09 | O sistema deve calcular automaticamente o consumo de tecido por pedido, com base no consumo por peça |

| RF10 | O sistema deve atualizar o estoque de rolos automaticamente após a programação de um pedido |

| RF11 | O sistema deve impedir avanço de etapa de produção sem que a etapa anterior esteja concluída |

| RF12 | O sistema deve registrar usuário, data e hora em toda movimentação de etapa de produção |

| RF13 | O sistema deve identificar automaticamente pedidos atrasados (data prevista < data atual e status ≠ Entregue) |

| RF14 | O sistema deve destacar pedidos próximos ao vencimento (ex: ≤ 3 dias da data prevista) |

| RF15 | O sistema deve exibir um dashboard com contagem de pedidos por status, indicadores de estoque e consumo de tecido por período |

| RF16 | O sistema deve autenticar usuários via login e senha |

| RF17 | O sistema deve controlar acesso por perfil: Operador, Supervisor, Administrador |



\## 2. Requisitos Não Funcionais



| ID | Descrição |

|----|-----------|

| RNF01 | O backend deve ser desenvolvido em Java 21 com Spring Boot |

| RNF02 | O banco de dados deve ser PostgreSQL |

| RNF03 | O frontend deve ser desenvolvido em React |

| RNF04 | A aplicação deve ser conteinerizada com Docker |

| RNF05 | As senhas de usuário devem ser armazenadas com hash (nunca em texto puro) |

| RNF06 | O sistema deve responder às consultas do dashboard em até 2 segundos para até 10.000 pedidos |

| RNF07 | O sistema deve manter histórico auditável de todas as movimentações de produção (não permitir exclusão física de registros de movimentação) |

| RNF08 | A API deve seguir padrão REST, com respostas em JSON |



\## 3. Perfis de Usuário



| Perfil | Permissões |

|--------|-----------|

| \*\*Operador\*\* | Registrar avanço de etapa de produção (Corte, Costura, Arrematação, Expedição). Sem acesso a cadastro de pedidos ou dashboard gerencial |

| \*\*Supervisor\*\* | Tudo do Operador + cadastrar/editar pedidos, gerenciar programação de enfesto, gerenciar estoque de rolos, visualizar dashboard |

| \*\*Administrador\*\* | Tudo do Supervisor + gerenciar usuários e perfis, acesso irrestrito |



\## 4. Casos de Uso



\### UC01 — Cadastrar Pedido

\*\*Ator:\*\* Supervisor, Administrador

\*\*Fluxo principal:\*\*

1\. Usuário acessa tela de novo pedido

2\. Informa cliente, número do pedido, modelo, cor, grade de tamanhos, quantidades e data de entrega

3\. Sistema calcula automaticamente os componentes da capa (RF04)

4\. Sistema define status inicial "Aguardando Programação"

5\. Sistema salva o pedido



\*\*Fluxo alternativo:\*\* Importação via Excel (RF02) — mesmo resultado final, múltiplos pedidos de uma vez



\### UC02 — Programar Enfesto

\*\*Ator:\*\* Supervisor, Administrador

\*\*Pré-condição:\*\* Pedido em status "Aguardando Programação"

\*\*Fluxo principal:\*\*

1\. Sistema sugere programação automática (RF06)

2\. Usuário pode ajustar manualmente (RF07)

3\. Sistema calcula consumo de tecido necessário (RF09)

4\. Sistema verifica saldo de rolos disponível

5\. Sistema atualiza status do pedido para "Programado" e debita o estoque (RF10)



\*\*Fluxo de exceção:\*\* Saldo de rolo insuficiente → sistema alerta e impede confirmação



\### UC03 — Avançar Etapa de Produção

\*\*Ator:\*\* Operador, Supervisor, Administrador

\*\*Pré-condição:\*\* Etapa anterior concluída (RF11)

\*\*Fluxo principal:\*\*

1\. Usuário seleciona pedido na etapa atual

2\. Confirma conclusão e avanço para próxima etapa

3\. Sistema registra usuário, data e hora (RF12)

4\. Sistema atualiza status do pedido



\### UC04 — Controlar Estoque de Rolos

\*\*Ator:\*\* Supervisor, Administrador

\*\*Fluxo principal:\*\*

1\. Usuário registra entrada de novo rolo (código, tipo, cor, metragem)

2\. Sistema mantém saldo atualizado a cada consumo (RF09, RF10)



\### UC05 — Visualizar Dashboard Gerencial

\*\*Ator:\*\* Supervisor, Administrador

\*\*Fluxo principal:\*\*

1\. Usuário acessa o dashboard

2\. Sistema exibe pedidos por status, pedidos atrasados/próximos do vencimento, indicadores de estoque e consumo por período (RF13-RF15)



\### UC06 — Autenticar e Controlar Acesso

\*\*Ator:\*\* Todos

\*\*Fluxo principal:\*\*

1\. Usuário informa login e senha

2\. Sistema valida credenciais

3\. Sistema libera funcionalidades conforme perfil (RF17)



\## 5. Fora de Escopo (v1)



Para manter o projeto viável dentro do cronograma de portfólio, ficam \*\*fora\*\* da primeira versão:

\- Múltiplas fábricas/multi-tenant

\- Integração com ERP externo

\- Notificações por e-mail/SMS

\- App mobile nativo

