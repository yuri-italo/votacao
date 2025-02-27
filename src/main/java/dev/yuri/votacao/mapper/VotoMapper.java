package dev.yuri.votacao.mapper;

import dev.yuri.votacao.dto.request.VotoDTO;
import dev.yuri.votacao.dto.response.VotoResponse;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Associado;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Voto;
import dev.yuri.votacao.repository.AssociadoRepository;
import dev.yuri.votacao.repository.PautaRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class VotoMapper {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Mapping(target = "pauta", source = "pautaId")
    @Mapping(target = "associado", source = "associadoId")
    public abstract Voto toEntity(VotoDTO votoDTO);

    protected Pauta pautaIdToPauta(Long pautaId) {
        if (pautaId == null) {
            return null;
        }
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada com o ID: " + pautaId));
    }

    protected Associado associadoIdToAssociado(Long associadoId) {
        if (associadoId == null) {
            return null;
        }
        return associadoRepository.findById(associadoId)
                .orElseThrow(() -> new EntityNotFoundException("Associado não encontrado com o ID: " + associadoId));
    }

    @Mapping(target = "pauta", source = "pauta.nome")
    @Mapping(target = "nomeAssociado", source = "associado.nome")
    public abstract VotoResponse toResponse(Voto voto);
}