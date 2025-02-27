package dev.yuri.votacao.mapper;

import dev.yuri.votacao.dto.request.VotoDTO;
import dev.yuri.votacao.dto.response.VotoResponse;
import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.service.AssociadoService;
import dev.yuri.votacao.service.PautaService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class VotoMapper {

    @Autowired
    private PautaService pautaService;

    @Autowired
    private AssociadoService associadoService;

    @Mapping(target = "pauta", source = "pautaId")
    @Mapping(target = "associado", source = "associadoId")
    public abstract Voto toEntity(VotoDTO votoDTO);

    protected Pauta pautaIdToPauta(Long pautaId) {
        if (pautaId == null) {
            return null;
        }
        return pautaService.findById(pautaId);
    }

    protected Associado associadoIdToAssociado(Long associadoId) {
        if (associadoId == null) {
            return null;
        }
        return associadoService.findById(associadoId);
    }

    @Mapping(target = "pauta", source = "pauta.nome")
    @Mapping(target = "nomeAssociado", source = "associado.nome")
    public abstract VotoResponse toResponse(Voto voto);
}