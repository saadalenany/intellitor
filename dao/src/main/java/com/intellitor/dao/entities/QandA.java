package com.intellitor.dao.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class QandA extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column
    private String question;

    @Column
    private String selection1;

    @Column
    private String selection2;

    @Column
    private String selection3;

    @Column(name = "correct_answer")
    private Byte correctAnswer;
}
