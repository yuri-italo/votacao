ALTER TABLE sessao DROP CONSTRAINT fk_sessao_pauta;

ALTER TABLE sessao
ADD CONSTRAINT fk_sessao_pauta
FOREIGN KEY (pauta_id) REFERENCES pauta(id) ON DELETE CASCADE;
