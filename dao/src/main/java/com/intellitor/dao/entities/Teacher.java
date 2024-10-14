package com.intellitor.dao.entities;

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
public class Teacher extends User {

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();

    public void setCourses(List<Course> newCourses) {
        this.courses.clear();
        if (newCourses != null) {
            newCourses.forEach(course -> course.setTeacher(this));
            this.courses.addAll(newCourses);
        }
    }
}
