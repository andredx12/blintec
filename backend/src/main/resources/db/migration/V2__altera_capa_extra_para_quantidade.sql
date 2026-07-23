ALTER TABLE pedido
    ALTER COLUMN capa_extra DROP DEFAULT;

ALTER TABLE pedido
    ALTER COLUMN capa_extra TYPE INTEGER USING (CASE WHEN capa_extra THEN 1 ELSE 0 END);

ALTER TABLE pedido
    ALTER COLUMN capa_extra SET DEFAULT 0;

ALTER TABLE pedido
    ADD CONSTRAINT chk_capa_extra_nao_negativo CHECK (capa_extra >= 0);