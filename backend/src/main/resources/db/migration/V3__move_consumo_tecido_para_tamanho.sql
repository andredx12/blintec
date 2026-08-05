ALTER TABLE tamanho_modelo
    ADD COLUMN consumo_tecido_por_peca NUMERIC(6,2);

UPDATE tamanho_modelo
    SET consumo_tecido_por_peca = m.consumo_tecido_por_peca
    FROM modelo m
    WHERE tamanho_modelo.modelo_id = m.id;

ALTER TABLE tamanho_modelo
    ALTER COLUMN consumo_tecido_por_peca SET NOT NULL;

ALTER TABLE tamanho_modelo
    ADD CONSTRAINT chk_consumo_tecido_positivo CHECK (consumo_tecido_por_peca > 0);

ALTER TABLE modelo
    DROP COLUMN consumo_tecido_por_peca;