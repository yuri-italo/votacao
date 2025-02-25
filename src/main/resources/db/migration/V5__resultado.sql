CREATE TABLE IF NOT EXISTS resultado (
    id BIGSERIAL PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    quantidade_sim BIGINT,
    quantidade_nao BIGINT,
    situacao VARCHAR(255),
    CONSTRAINT fk_resultado_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);
