package com.intellitor.dao.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Quiz extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QandA> qas = new ArrayList<>();

    public void setQas(List<QandA> newQas) {
        this.qas.clear();
        if (newQas != null) {
            newQas.forEach(qa -> qa.setQuiz(this));
            this.qas.addAll(newQas);
        }
    }
}
