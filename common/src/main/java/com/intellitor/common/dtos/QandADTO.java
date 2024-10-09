package com.intellitor.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QandADTO extends BaseDTO{

    private Long quizId;

    private String question;

    private String selection1;

    private String selection2;

    private String selection3;

    private Byte correctAnswer;
}
