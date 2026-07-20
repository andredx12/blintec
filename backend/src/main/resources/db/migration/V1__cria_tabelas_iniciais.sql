CREATE TYPE perfil_usuario AS ENUM ('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR');
CREATE TYPE status_pedido AS ENUM (
    'AGUARDANDO_PROGRAMACAO', 'PROGRAMADO', 'EM_CORTE',
    'EM_COSTURA', 'EM_ARREMATACAO', 'EM_EXPEDICAO', 'ENTREGUE'
);
CREATE TYPE genero_tamanho AS ENUM ('MASCULINO', 'FEMININO');
CREATE TYPE tipo_movimentacao_estoque AS ENUM ('ENTRADA', 'SAIDA');

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    perfil perfil_usuario NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cnpj_cpf VARCHAR(20) UNIQUE NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE modelo (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL,
    consumo_tecido_por_peca NUMERIC(6,2) NOT NULL CHECK (consumo_tecido_por_peca > 0)
);

CREATE TABLE tamanho_modelo (
    id BIGSERIAL PRIMARY KEY,
    modelo_id BIGINT NOT NULL REFERENCES modelo(id),
    tamanho VARCHAR(10) NOT NULL,
    genero genero_tamanho NOT NULL,
    tamanho_equivalente_id BIGINT REFERENCES tamanho_modelo(id),
    UNIQUE (modelo_id, tamanho)
);

CREATE TABLE tipo_tecido (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    estoque_minimo NUMERIC(8,2) NOT NULL DEFAULT 200.00 CHECK (estoque_minimo >= 0)
);

CREATE TABLE rolo (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(30) UNIQUE NOT NULL,
    tipo_tecido_id BIGINT NOT NULL REFERENCES tipo_tecido(id),
    cor VARCHAR(50) NOT NULL,
    metragem_inicial NUMERIC(8,2) NOT NULL CHECK (metragem_inicial > 0),
    saldo_atual NUMERIC(8,2) NOT NULL CHECK (saldo_atual >= 0),
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES cliente(id),
    numero_pedido VARCHAR(30) UNIQUE NOT NULL,
    modelo_id BIGINT NOT NULL REFERENCES modelo(id),
    cor VARCHAR(50) NOT NULL,
    capa_extra BOOLEAN NOT NULL DEFAULT FALSE,
    data_entrega DATE NOT NULL,
    status status_pedido NOT NULL DEFAULT 'AGUARDANDO_PROGRAMACAO',
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    criado_por BIGINT NOT NULL REFERENCES usuario(id)
);

CREATE TABLE item_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id),
    tamanho_modelo_id BIGINT NOT NULL REFERENCES tamanho_modelo(id),
    quantidade INT NOT NULL CHECK (quantidade > 0)
);

CREATE TABLE programacao_enfesto (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT UNIQUE NOT NULL REFERENCES pedido(id),
    data_programacao TIMESTAMP NOT NULL DEFAULT NOW(),
    ajustado_manualmente BOOLEAN NOT NULL DEFAULT FALSE,
    programado_por BIGINT NOT NULL REFERENCES usuario(id),
    consumo_tecido_total NUMERIC(8,2) NOT NULL CHECK (consumo_tecido_total > 0)
);

CREATE TABLE movimentacao_estoque (
    id BIGSERIAL PRIMARY KEY,
    rolo_id BIGINT NOT NULL REFERENCES rolo(id),
    tipo tipo_movimentacao_estoque NOT NULL,
    quantidade NUMERIC(8,2) NOT NULL CHECK (quantidade > 0),
    pedido_id BIGINT REFERENCES pedido(id),
    data TIMESTAMP NOT NULL DEFAULT NOW(),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id)
);

CREATE TABLE movimentacao_producao (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido(id),
    etapa_anterior status_pedido NOT NULL,
    etapa_nova status_pedido NOT NULL,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    data_hora TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pedido_status ON pedido(status);
CREATE INDEX idx_pedido_data_entrega ON pedido(data_entrega);
CREATE INDEX idx_movimentacao_estoque_rolo ON movimentacao_estoque(rolo_id);
CREATE INDEX idx_movimentacao_producao_pedido ON movimentacao_producao(pedido_id);