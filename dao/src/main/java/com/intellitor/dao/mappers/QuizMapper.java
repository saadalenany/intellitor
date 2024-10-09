package com.intellitor.dao.mappers;

import com.intellitor.common.dtos.QuizDTO;
import com.intellitor.dao.entities.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

import java.util.List;

@Mapper(uses = QandAMapper.class, mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG, componentModel = "spring")
public interface QuizMapper {

    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "qas", target = "qasDTOs")
    QuizDTO toModel(Quiz entity);
    List<QuizDTO> toModels(List<Quiz> entities);

    @Mapping(source = "enrollmentId", target = "enrollment.id")
    @Mapping(source = "qasDTOs", target = "qas")
    Quiz toEntity(QuizDTO model);
    List<Quiz> toEntities(List<QuizDTO> models);
}
