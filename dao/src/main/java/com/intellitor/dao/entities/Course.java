package com.intellitor.dao.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Course extends BaseEntity{

    @Column(unique = true)
    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    public void setEnrollments(List<Enrollment> newEnrollments) {
        this.enrollments.clear();
        if (newEnrollments != null) {
            newEnrollments.forEach(enrollment -> enrollment.setCourse(this));
            this.enrollments.addAll(newEnrollments);
        }
    }
}
