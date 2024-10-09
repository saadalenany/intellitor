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
public class CourseDTO extends BaseDTO{

    private String title;

    private String description;

    private Long teacherId;

    private List<EnrollmentDTO> enrollmentDTOs;

}
