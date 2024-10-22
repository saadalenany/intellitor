package com.intellitor.common.mappers;

import com.intellitor.common.dtos.QandADTO;
import com.intellitor.common.entities.QandA;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QandAMapper {

    @Mapping(source = "quiz.id", target = "quizId")
    QandADTO toModel(QandA entity);
    List<QandADTO> toModels(List<QandA> entities);

    @Mapping(source = "quizId", target = "quiz.id")
    QandA toEntity(QandADTO model);
    List<QandA> toEntities(List<QandADTO> models);
}
