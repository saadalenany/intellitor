package com.intellitor.common.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    public void setEnrollments(List<Enrollment> newEnrollments) {
        this.enrollments.clear();
        if (newEnrollments != null) {
            newEnrollments.forEach(enrollment -> enrollment.setStudent(this));
            this.enrollments.addAll(newEnrollments);
        }
    }
}
