package com.intellitor.dao.mappers;

import com.intellitor.common.dtos.QandADTO;
import com.intellitor.dao.entities.QandA;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

import java.util.List;

@Mapper(mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG, componentModel = "spring")
public interface QandAMapper {

    @Mapping(source = "quiz.id", target = "quizId")
    QandADTO toModel(QandA entity);
    List<QandADTO> toModels(List<QandA> entities);

    @Mapping(source = "quizId", target = "quiz.id")
    QandA toEntity(QandADTO model);
    List<QandA> toEntities(List<QandADTO> models);
}
