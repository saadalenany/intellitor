package com.intellitor.common.dtos;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO extends UserDTO {

    private List<EnrollmentDTO> enrollmentDTOs;

}
