package com.intellitor.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDTO extends BaseDTO{

    private Long enrollmentId;

    private List<QandADTO> qasDTOs;
}
