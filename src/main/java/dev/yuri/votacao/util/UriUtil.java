package dev.yuri.votacao.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Classe utilitária para a construção de URIs baseadas no ID de uma entidade.
 * Esta classe fornece métodos que ajudam a gerar URIs com base na requisição atual,
 * tipicamente usadas para retornar o URI de um recurso recém-criado.
 */
public class UriUtil {
    private UriUtil() {
        throw new IllegalStateException("Não é possível instanciar: Classe utilitária");
    }

    /**
     * Constrói um URI com base no ID fornecido, utilizando a requisição atual.
     * Tipicamente utilizado para construir o URI de um recurso recém-criado em uma API REST.
     *
     * @param id O ID da entidade para a qual o URI será gerado.
     * @return O URI completo para o recurso identificado pelo ID.
     */
    public static URI buildUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
