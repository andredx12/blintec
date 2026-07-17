\# 05 — Especificação das APIs REST



Convenções gerais:

\- Todas as rotas exigem `Authorization: Bearer <token>`, exceto `/auth/login`

\- Respostas de erro seguem formato padrão: `{ "erro": "mensagem", "timestamp": "..." }`

\- Paginação em listagens: `?page=0\&size=20`



\## Auth



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| POST | /auth/login | Público | Autentica e retorna JWT |

| GET | /auth/me | Qualquer autenticado | Retorna dados do usuário logado |



\## Usuários



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| GET | /usuarios | ADMINISTRADOR | Lista usuários |

| POST | /usuarios | ADMINISTRADOR | Cria usuário |

| PUT | /usuarios/{id} | ADMINISTRADOR | Atualiza usuário/perfil |

| PATCH | /usuarios/{id}/status | ADMINISTRADOR | Ativa/desativa usuário |



\## Clientes



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| GET | /clientes | SUPERVISOR, ADMINISTRADOR | Lista clientes |

| POST | /clientes | SUPERVISOR, ADMINISTRADOR | Cria cliente |

| GET | /clientes/{id} | SUPERVISOR, ADMINISTRADOR | Detalhe do cliente |



\## Modelos e Tamanhos



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| GET | /modelos | Qualquer autenticado | Lista modelos (SENASP, RB, EXECUTIVO) |

| GET | /modelos/{id}/tamanhos | Qualquer autenticado | Lista grade de tamanhos do modelo |

| POST | /modelos | ADMINISTRADOR | Cadastra novo modelo |



\## Pedidos



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| GET | /pedidos | Qualquer autenticado | Lista pedidos (filtros: status, cliente, atrasado) |

| GET | /pedidos/{id} | Qualquer autenticado | Detalhe do pedido + itens |

| POST | /pedidos | SUPERVISOR, ADMINISTRADOR | Cria pedido manualmente (RF01) |

| POST | /pedidos/importar | SUPERVISOR, ADMINISTRADOR | Importa pedidos via Excel (RF02) |

| PATCH | /pedidos/{id}/avancar-etapa | OPERADOR, SUPERVISOR, ADMINISTRADOR | Avança status (RN018, valida sequência) |



\*\*Exemplo de request — POST /pedidos:\*\*

```json

{

&#x20; "clienteId": 12,

&#x20; "numeroPedido": "PED-2026-0043",

&#x20; "modeloId": 2,

&#x20; "cor": "Preto",

&#x20; "capaExtra": false,

&#x20; "dataEntrega": "2026-08-15",

&#x20; "itens": \[

&#x20;   { "tamanhoModeloId": 5, "quantidade": 20 },

&#x20;   { "tamanhoModeloId": 6, "quantidade": 15 }

&#x20; ]

}

```



\*\*Exemplo de response — GET /pedidos/{id}:\*\*

```json

{

&#x20; "id": 43,

&#x20; "numeroPedido": "PED-2026-0043",

&#x20; "cliente": { "id": 12, "nome": "Corpo de Bombeiros SP" },

&#x20; "modelo": "RB",

&#x20; "cor": "Preto",

&#x20; "status": "AGUARDANDO\_PROGRAMACAO",

&#x20; "atrasado": false,

&#x20; "proximoVencimento": false,

&#x20; "itens": \[

&#x20;   { "tamanho": "M", "quantidade": 20, "componentes": { "frentes": 20, "costas": 20, "ombrinhos": 40, "palas": 40, "abas": 80 } }

&#x20; ]

}

```



\## Programação de Enfesto



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| GET | /pedidos/{id}/enfesto/sugestao | SUPERVISOR, ADMINISTRADOR | Retorna sugestão automática + rolos disponíveis (RF06) |

| POST | /pedidos/{id}/enfesto | SUPERVISOR, ADMINISTRADOR | Confirma programação, com rolos escolhidos manualmente (RF07, RN016) |



\*\*Exemplo de request — POST /pedidos/{id}/enfesto:\*\*

```json

{

&#x20; "rolosSelecionados": \[

&#x20;   { "roloId": 8, "metragemUtilizada": 20.00 },

&#x20;   { "roloId": 15, "metragemUtilizada": 17.40 }

&#x20; ]

}

```



\## Estoque de Rolos



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| GET | /rolos | SUPERVISOR, ADMINISTRADOR | Lista rolos (filtro: tipoTecido, cor, saldo mínimo) |

| POST | /rolos | SUPERVISOR, ADMINISTRADOR | Registra entrada de novo rolo (RF08) |

| GET | /tipos-tecido | SUPERVISOR, ADMINISTRADOR | Lista tipos de tecido com estoque mínimo configurado |

| PUT | /tipos-tecido/{id} | ADMINISTRADOR | Ajusta estoque mínimo de alerta |



\## Dashboard



| Método | Rota | Perfil | Descrição |

|--------|------|--------|-----------|

| GET | /dashboard/resumo | SUPERVISOR, ADMINISTRADOR | Contagem de pedidos por status (RF15, RN023) |

| GET | /dashboard/atrasados | SUPERVISOR, ADMINISTRADOR | Lista pedidos atrasados/próximos vencimento (RN021, RN022) |

| GET | /dashboard/estoque | SUPERVISOR, ADMINISTRADOR | Indicadores de estoque, alertas abaixo do mínimo (RN024) |

| GET | /dashboard/consumo?inicio=\&fim= | SUPERVISOR, ADMINISTRADOR | Consumo de tecido no período (RN025) |

