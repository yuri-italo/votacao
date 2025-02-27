package dev.yuri.votacao.mapper;

import dev.yuri.votacao.dto.request.AssociadoDTO;
import dev.yuri.votacao.dto.response.AssociadoResponse;
import dev.yuri.votacao.model.Associado;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssociadoMapper {

    AssociadoMapper INSTANCE = Mappers.getMapper(AssociadoMapper.class);

    Associado toEntity(AssociadoDTO associadoDTO);

    AssociadoResponse toResponse(Associado associado);
}