CREATE TABLE IF NOT EXISTS pauta (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL
);
