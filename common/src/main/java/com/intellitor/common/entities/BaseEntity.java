package com.intellitor.common.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreationTimestamp
    private Instant createdOn;

    @Column
    @CreationTimestamp
    private Instant updatedOn;

//    @Column(unique=true)
//    private String createdBy;
//
//    @Column(unique=true)
//    private String updatedBy;
}
