package dev.yuri.votacao.repository;

import dev.yuri.votacao.model.Associado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class AssociadoRepositoryTest {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Test
    void testExistsByCpf_QuandoCpfExistir() {
        String cpf = "12345678901";
        Associado associado = new Associado();
        associado.setNome("John Doe");
        associado.setCpf(cpf);
        associadoRepository.save(associado);

        boolean exists = associadoRepository.existsByCpf(cpf);

        assertTrue(exists);
    }

    @Test
    void testExistsByCpf_QuandoCpfNaoExistir() {
        String cpf = "12345678901";

        boolean exists = associadoRepository.existsByCpf(cpf);

        assertFalse(exists);
    }
}