package dev.yuri.votacao.repository;

import dev.yuri.votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaIdAndAssociadoId(Long pautaId, Long associadoId);

    List<Voto> findAllByPautaId(Long pautaId);
}
