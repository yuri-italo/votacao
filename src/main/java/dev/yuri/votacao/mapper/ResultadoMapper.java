package dev.yuri.votacao.mapper;

import dev.yuri.votacao.dto.response.ResultadoResponse;
import dev.yuri.votacao.model.Resultado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResultadoMapper {

    ResultadoMapper INSTANCE = Mappers.getMapper(ResultadoMapper.class);

    @Mapping(target = "pauta", source = "pauta.nome")
    @Mapping(target = "votosFavoraveis", source = "quantidadeSim")
    @Mapping(target = "votosContra", source = "quantidadeNao")
    @Mapping(target = "situacao", source = "situacao")
    ResultadoResponse toResponse(Resultado resultado);
}