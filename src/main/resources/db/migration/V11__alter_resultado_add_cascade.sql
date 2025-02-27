ALTER TABLE resultado DROP CONSTRAINT fk_resultado_pauta;

ALTER TABLE resultado
ADD CONSTRAINT fk_resultado_pauta
FOREIGN KEY (pauta_id) REFERENCES pauta(id) ON DELETE CASCADE;
