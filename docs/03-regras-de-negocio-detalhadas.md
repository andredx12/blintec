\# 03 — Regras de Negócio Detalhadas



\## Bloco 1 — Gestão de Pedidos



\*\*RN001 — Campos obrigatórios do pedido\*\*

\- Pré-condição: nenhuma

\- Validação: cliente, número do pedido, modelo, cor, grade de tamanhos, quantidades e data de entrega são todos obrigatórios

\- Caso de borda: `numeroPedido` deve ser único no sistema — tentativa de duplicar deve ser rejeitada com erro 409 (Conflict)



\*\*RN002/RN003 — Origem do cadastro\*\*

\- Importação via Excel e cadastro manual geram o \*\*mesmo resultado final\*\* (um Pedido + N ItemPedido)

\- Caso de borda: linha de Excel com dado inválido (ex: tamanho inexistente pro modelo) deve ser reportada linha a linha, sem abortar a importação inteira



\*\*RN004 — Máquina de estados do status\*\*

\- Transições válidas (ordem estrita, sem pular etapas — ver RN018):

- Caso de borda: qualquer tentativa de transição fora dessa sequência deve ser rejeitada com erro 422 (Unprocessable Entity)



\## Bloco 2 — Modelagens e Tamanhos



\*\*Tabela de equivalência (usada no cálculo de componentes):\*\*



| Modelo | Tamanho Feminino | Equivale a (Masculino) |

|--------|------------------|------------------------|

| RB / ROBOCOP | PFEM | PP |

| RB / ROBOCOP | MFEM | P |

| RB / ROBOCOP | GFEM | M |

| RB / ROBOCOP | GGFEM | G |

| SENASP | PPFEM, PFEM, MFEM, GFEM, GGFEM | (tamanhos próprios, sem equivalência — grade feminina independente) |

| EXECUTIVO | (não possui grade feminina definida na documentação) | — |



\- Caso de borda: cadastro de pedido com modelo EXECUTIVO + tamanho feminino deve ser bloqueado na validação (grade não suportada)



\## Bloco 3 — Cálculo de Componentes da Capa



\*\*RN005 — Fórmula de componentes por peça\*\*



Para `Q` = quantidade de peças de um item:



| Componente | Fórmula |

|-----------|---------|

| Frente | Q × 1 |

| Costa | Q × 1 |

| Ombrinhos | Q × 2 |

| Palas | Q × 2 |

| Abas | Q × 4 |



\- Exemplo de validação (20 peças): 20 Frentes, 20 Costas, 40 Ombrinhos, 40 Palas, 80 Abas ✅ (bate com o documento original)

\- Cálculo é feito \*\*por ItemPedido\*\* (por linha de tamanho), depois somado no nível do Pedido



\## Bloco 4 — Capa Extra



\*\*RN006/RN007/RN008 — Duplicação condicional\*\*





\- Importante: a capa extra \*\*dobra a quantidade de capas\*\*, e por consequência dobra também os componentes calculados no Bloco 3 (frentes, costas, ombrinhos, palas, abas) — não é um cálculo separado

\- Exemplo (20 capas G, capaExtra = true): 20 normais + 20 extras = 40 capas totais → 40 Frentes, 40 Costas, 80 Ombrinhos, 80 Palas, 160 Abas



\## Bloco 5 — Programação de Enfesto



\*\*RN009-RN012 — Geração e ajuste\*\*

\- Pré-condição: Pedido em status AGUARDANDO\_PROGRAMACAO

\- Entrada do cálculo automático: modelo, tamanho (via ItemPedido), cor, quantidade, capaExtra

\- Ajuste manual: sobrescreve a sugestão automática, mas \*\*sempre registra\*\* `programadoPor` e `ajustadoManualmente = true`

\- Caso de borda: pedido já programado não pode ser reprogramado diretamente — precisa de fluxo explícito de "reprogramação" (fora de escopo v1, listado como risco técnico)



\## Bloco 6 — Controle de Rolos e Consumo



\*\*RN013-RN016 — Consumo automático de tecido\*\*

- Exemplo de validação: 1,87m/peça × 20 peças = 37,40m ✅ (bate com o documento original)

\- \*\*RN016 — Débito automático de estoque:\*\* ao confirmar a programação de enfesto, o sistema deve:

&#x20; 1. Verificar se `saldoAtual` do(s) rolo(s) selecionado(s) ≥ `consumoTotal`

&#x20; 2. Se sim: criar `MovimentacaoEstoque` (tipo SAIDA) e decrementar `saldoAtual`

&#x20; 3. Se não: \*\*bloquear a confirmação\*\* e retornar erro informando o déficit (quantidade faltante)

\- Caso de borda: consumo pode ser atendido por \*\*múltiplos rolos\*\* (ex: rolo A tem 20m, faltam 17,40m do rolo B) — sistema deve suportar rateio entre rolos, gerando uma `MovimentacaoEstoque` por rolo consumido



\## Bloco 7 — Produção



\*\*RN017/RN018 — Sequência obrigatória de etapas\*\*

\- Já formalizado como máquina de estados no Bloco 1 (RN004)

\- Reforço: a transição é sempre \*\*uma etapa por vez\*\*, nunca "pula direto" (ex: Corte → Expedição é inválido mesmo que Costura e Arrematação estejam de fato concluídas fora do sistema)



\*\*RN019 — Auditoria de movimentação\*\*

\- Toda transição de status cria um registro `MovimentacaoProducao` com usuário, data e hora (imutável — ver observação de design do Dia 2)



\## Bloco 8 — Prazos



\*\*RN020-RN022 — Cálculo de atraso\*\*

- Esses dois indicadores são \*\*calculados dinamicamente\*\* (não armazenados como coluna), pois dependem da data atual no momento da consulta

\- Caso de borda: pedido ENTREGUE nunca é considerado atrasado, mesmo que a entrega tenha ocorrido após a data prevista (isso vira métrica separada: "atraso na entrega", não "pedido em atraso")



\## Bloco 9 — Dashboard



\*\*RN023-RN025 — Indicadores exibidos\*\*

\- Contagem de pedidos agrupada por `status` (query com `GROUP BY`)

\- Indicadores de estoque: soma de `saldoAtual` por tipo de tecido/cor, e lista de rolos abaixo de um limite mínimo configurável (limite mínimo fica marcado como decisão em aberto — ver seção abaixo)

\- Consumo de tecido por período: soma de `MovimentacaoEstoque` (tipo SAIDA) agrupada por intervalo de datas informado pelo usuário



\## Decisões em Aberto (a resolver no Dia 3 — Arquitetura)



1\. Existe estoque mínimo por tipo de rolo para alerta no dashboard? (documento original não especifica um valor)

2\. \~\~Rateio de consumo entre múltiplos rolos\~\~ \*\*RESOLVIDO:\*\* seleção manual pelo Supervisor/Administrador. FIFO automático foi descartado porque o rolo mais antigo costuma estar fisicamente no fundo do estoque, sendo mais difícil de acessar — forçar uso automático dele criaria atrito na operação real. O sistema deve \*\*sugerir\*\* rolos com saldo suficiente, mas a escolha final é manual.

3\. Reprogramação de enfesto (alterar programação já confirmada): fluxo formal fica para v2, mas precisa de decisão sobre se entra no MVP ou não


