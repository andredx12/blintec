\# 02 — Diagrama de Entidades (Modelo Conceitual)



\## Entidades e Atributos



\### Usuario

\- id (PK)

\- nome

\- email (único)

\- senhaHash

\- perfil (ENUM: OPERADOR, SUPERVISOR, ADMINISTRADOR)

\- ativo (boolean)

\- criadoEm



\### Cliente

\- id (PK)

\- nome

\- cnpjCpf

\- criadoEm



\### Modelo

\- id (PK)

\- nome (ex: SENASP, RB, ROBOCOP, EXECUTIVO)

\- consumoTecidoPorPeca (decimal, em metros)



\### TamanhoModelo

\- id (PK)

\- modeloId (FK → Modelo)

\- tamanho (ex: PP, P, M, G, GG, EGG, PPFEM, PFEM...)

\- genero (ENUM: MASCULINO, FEMININO)

\- tamanhoEquivalente (FK opcional → TamanhoModelo, para casos como RB onde PFEM corresponde a PP)



\### Pedido

\- id (PK)

\- clienteId (FK → Cliente)

\- numeroPedido (único)

\- modeloId (FK → Modelo)

\- cor

\- capaExtra (boolean)

\- dataEntrega (date)

\- status (ENUM: AGUARDANDO\_PROGRAMACAO, PROGRAMADO, EM\_CORTE, EM\_COSTURA, EM\_ARREMATACAO, EM\_EXPEDICAO, ENTREGUE)

\- criadoEm

\- criadoPor (FK → Usuario)



\### ItemPedido

\- id (PK)

\- pedidoId (FK → Pedido)

\- tamanhoModeloId (FK → TamanhoModelo)

\- quantidade (int)



\### ProgramacaoEnfesto

\- id (PK)

\- pedidoId (FK → Pedido, único — 1:1)

\- dataProgramacao

\- ajustadoManualmente (boolean)

\- programadoPor (FK → Usuario)

\- consumoTecidoTotal (decimal, calculado)



\### Rolo

\- id (PK)

\- codigo (único)

\- tipoTecido

\- cor

\- metragemInicial (decimal)

\- saldoAtual (decimal)

\- criadoEm



\### MovimentacaoEstoque

\- id (PK)

\- roloId (FK → Rolo)

\- tipo (ENUM: ENTRADA, SAIDA)

\- quantidade (decimal)

\- pedidoId (FK → Pedido, nullable — só preenchido em saídas vinculadas a programação)

\- data

\- usuarioId (FK → Usuario)



\### MovimentacaoProducao

\- id (PK)

\- pedidoId (FK → Pedido)

\- etapaAnterior (ENUM de status)

\- etapaNova (ENUM de status)

\- usuarioId (FK → Usuario)

\- dataHora (timestamp)



\## Relacionamentos (Cardinalidade)



\- Cliente 1 —— N Pedido

\- Modelo 1 —— N Pedido

\- Modelo 1 —— N TamanhoModelo

\- Pedido 1 —— N ItemPedido

\- TamanhoModelo 1 —— N ItemPedido

\- Pedido 1 —— 1 ProgramacaoEnfesto (criada somente após programação)

\- Rolo 1 —— N MovimentacaoEstoque

\- Pedido 1 —— N MovimentacaoEstoque (uma programação pode consumir de múltiplos rolos)

\- Pedido 1 —— N MovimentacaoProducao

\- Usuario 1 —— N Pedido (criadoPor)

\- Usuario 1 —— N ProgramacaoEnfesto (programadoPor)

\- Usuario 1 —— N MovimentacaoEstoque

\- Usuario 1 —— N MovimentacaoProducao



\## Observações de Design



1\. \*\*ItemPedido separa a grade de tamanhos do Pedido\*\* — um pedido pode ter várias linhas (ex: 10 P, 15 M, 5 G), cada uma virando um ItemPedido. Isso evita colunas fixas tipo `qtdP`, `qtdM`, `qtdG` (rígido demais, quebra se aparecer um tamanho novo).



2\. \*\*TamanhoModelo com tamanhoEquivalente\*\* resolve a regra RN (modelagem RB/ROBOCOP, onde PFEM = PP, MFEM = P etc.) sem duplicar lógica de cálculo — o sistema sempre calcula em cima do tamanho equivalente quando ele existir.



3\. \*\*MovimentacaoEstoque e MovimentacaoProducao nunca são deletadas\*\* (RNF07 — auditoria). Cancelamentos, se existirem no futuro, devem ser modelados como nova movimentação de estorno, não como DELETE.



4\. \*\*ProgramacaoEnfesto é 1:1 com Pedido\*\* porque, pela regra de negócio (RN009-RN012), cada pedido recebe uma programação (ainda que ajustada manualmente depois).

