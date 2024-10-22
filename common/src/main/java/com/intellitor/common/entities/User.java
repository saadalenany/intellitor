package com.intellitor.common.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class User extends BaseEntity{

    @Column(unique=true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String phone;
}
