package com.intellitor.common.dtos;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO extends UserDTO {

    private List<CourseDTO> courseDTOs;

}
