package dev.yuri.votacao.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriUtil {
    private UriUtil() {
        throw new IllegalStateException("Não é possível instanciar: Classe utilitária");
    }

    public static URI buildUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
