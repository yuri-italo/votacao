package dev.yuri.votacao.repository;

import dev.yuri.votacao.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    Optional<Resultado> findByPautaId(Long pautaId);
}
