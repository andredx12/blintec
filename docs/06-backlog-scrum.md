\# 06 — Backlog Scrum



\## Épico 1: Autenticação e Controle de Acesso

\- \*\*US01\*\* Como usuário, quero fazer login com email e senha, para acessar o sistema de acordo com meu perfil

&#x20; - Critério: senha incorreta retorna 401; token JWT expira em 8h

\- \*\*US02\*\* Como Administrador, quero cadastrar e gerenciar usuários, para controlar quem acessa o sistema

&#x20; - Critério: e-mail duplicado é rejeitado; senha é armazenada com hash (RNF05)



\## Épico 2: Gestão de Pedidos

\- \*\*US03\*\* Como Supervisor, quero cadastrar um pedido manualmente, para iniciar o fluxo de produção

&#x20; - Critério: sistema calcula automaticamente os componentes da capa (RN005)

\- \*\*US04\*\* Como Supervisor, quero importar pedidos via Excel, para cadastrar vários de uma vez

&#x20; - Critério: erros de linha são reportados individualmente, sem abortar a importação inteira

\- \*\*US05\*\* Como usuário, quero visualizar a lista de pedidos com filtro por status, para acompanhar o fluxo

&#x20; - Critério: pedidos atrasados aparecem destacados (RN021)



\## Épico 3: Programação de Enfesto

\- \*\*US06\*\* Como Supervisor, quero ver uma sugestão automática de programação, para agilizar o planejamento

&#x20; - Critério: sugestão considera modelo, tamanho, cor, quantidade e capa extra

\- \*\*US07\*\* Como Supervisor, quero escolher manualmente quais rolos usar na programação, para respeitar a acessibilidade física do estoque

&#x20; - Critério: sistema bloqueia confirmação se saldo total selecionado < consumo necessário



\## Épico 4: Controle de Estoque de Rolos

\- \*\*US08\*\* Como Supervisor, quero registrar entrada de novos rolos, para manter o estoque atualizado

&#x20; - Critério: código do rolo é único

\- \*\*US09\*\* Como Administrador, quero configurar o estoque mínimo de alerta por tipo de tecido, para ser avisado de reposição necessária

&#x20; - Critério: valor padrão de 200m aplicado se não configurado



\## Épico 5: Produção

\- \*\*US10\*\* Como Operador, quero avançar o status de um pedido para a próxima etapa, para refletir o progresso real na fábrica

&#x20; - Critério: sistema bloqueia pular etapas (RN018); registra usuário/data/hora (RN019)



\## Épico 6: Dashboard Gerencial

\- \*\*US11\*\* Como Supervisor, quero visualizar um painel com pedidos por status, para ter visão geral da operação

\- \*\*US12\*\* Como Supervisor, quero ver indicadores de estoque com alertas de nível mínimo, para planejar compras de tecido

\- \*\*US13\*\* Como Supervisor, quero consultar o consumo de tecido por período, para analisar eficiência de uso



\## Priorização Sugerida (MVP)



| Sprint | Foco |

|--------|------|

| Sprint 1 | US01, US02 (autenticação) + setup do projeto |

| Sprint 2 | US03, US05 (pedidos - cadastro manual e listagem) |

| Sprint 3 | US08, US09 (estoque de rolos) |

| Sprint 4 | US06, US07 (programação de enfesto) |

| Sprint 5 | US10 (produção) |

| Sprint 6 | US11, US12, US13 (dashboard) |

| Backlog (pós-MVP) | US04 (importação Excel) 

