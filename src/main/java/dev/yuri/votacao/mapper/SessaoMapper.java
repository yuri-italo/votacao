package dev.yuri.votacao.mapper;

import dev.yuri.votacao.dto.request.SessaoDTO;
import dev.yuri.votacao.dto.response.SessaoResponse;
import dev.yuri.votacao.exception.EntityNotFoundException;
import dev.yuri.votacao.model.Pauta;
import dev.yuri.votacao.model.Sessao;
import dev.yuri.votacao.repository.PautaRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public abstract class SessaoMapper {

    @Autowired
    private PautaRepository pautaRepository;

    @Mapping(target = "pauta", source = "pautaId")
    public abstract Sessao toEntity(SessaoDTO sessaoDTO);

    protected Pauta pautaIdToPauta(Long pautaId) {
        if (pautaId == null) {
            return null;
        }
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada com o ID: " + pautaId));
    }


    public SessaoResponse toResponse(Sessao sessao) {
        if (sessao == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataInicioFormatada = sessao.getDataInicio().format(formatter);
        String dataFimFormatada = sessao.getDataFim().format(formatter);

        return new SessaoResponse(
                sessao.getId(),
                dataInicioFormatada,
                dataFimFormatada,
                sessao.getStatus().toString(),
                sessao.getPauta().getNome()
        );
    }
}