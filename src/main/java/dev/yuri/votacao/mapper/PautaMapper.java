package dev.yuri.votacao.mapper;

import dev.yuri.votacao.dto.request.PautaDTO;
import dev.yuri.votacao.model.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PautaMapper {

    PautaMapper INSTANCE = Mappers.getMapper(PautaMapper.class);

    Pauta toEntity(PautaDTO pautaDTO);
}