package com.lozado.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;
}
