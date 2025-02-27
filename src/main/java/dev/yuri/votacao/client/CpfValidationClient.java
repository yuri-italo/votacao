package dev.yuri.votacao.client;

import dev.yuri.votacao.client.enums.Status;
import dev.yuri.votacao.client.exception.InvalidCpfException;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CpfValidationClient {

    private static final Random RANDOM = new Random();

    public Status validarCpf(String cpf) {
        if (!isCpfValido(cpf)) {
            throw new InvalidCpfException("CPF não é válido para votação");
        }

        return RANDOM.nextBoolean() ? Status.ABLE_TO_VOTE : Status.UNABLE_TO_VOTE;
    }

    private boolean isCpfValido(String cpf) {
        return cpf.matches("\\d{11}");
    }
}
