CREATE TABLE IF NOT EXISTS voto (
    id BIGSERIAL PRIMARY KEY,
    escolha BOOLEAN NOT NULL,
    pauta_id BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    FOREIGN KEY (pauta_id) REFERENCES pauta(id) ON DELETE CASCADE,
    FOREIGN KEY (associado_id) REFERENCES associado(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_voto_associado_pauta_unique ON voto(associado_id, pauta_id);
