package dev.yuri.votacao.controller;

import dev.yuri.votacao.dto.request.VotoDTO;
import dev.yuri.votacao.dto.response.VotoResponse;
import dev.yuri.votacao.mapper.ResultadoMapper;
import dev.yuri.votacao.mapper.VotoMapper;
import dev.yuri.votacao.service.VotoService;
import dev.yuri.votacao.util.UriUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/voto")
@Tag(name = "Voto", description = "Endpoint para registrar votos nas sessões de votação")
public class VotoController {
    private static final Logger log = LoggerFactory.getLogger(VotoController.class);

    private final VotoService votoService;
    private final VotoMapper votoMapper;

    public VotoController(VotoService votoService, VotoMapper votoMapper, ResultadoMapper resultadoMapper) {
        this.votoService = votoService;
        this.votoMapper = votoMapper;
    }

    @PostMapping
    @Transactional
    @Operation(
            summary = "Registrar um voto",
            description = "Permite que um usuário registre seu voto em uma sessão de votação aberta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Sessão de votação não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<VotoResponse> vote(@Valid @RequestBody VotoDTO votoDTO) {
        log.info("Recebendo voto: {}", votoDTO);
        var voto = votoMapper.toEntity(votoDTO);
        var savedVoto = votoService.save(voto);

        log.info("Voto registrado com sucesso: {}", savedVoto);
        return ResponseEntity
                .created(UriUtil.buildUri(savedVoto.getId()))
                .body(votoMapper.toResponse(savedVoto));
    }
}
