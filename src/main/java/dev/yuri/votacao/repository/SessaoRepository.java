package dev.yuri.votacao.repository;

import dev.yuri.votacao.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    boolean existsByPautaId(Long pautaId);
}
