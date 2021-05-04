package com.lozado.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@Table(name = "BRANDS")
public class Brands extends BaseEntity{

    @Column(name = "COUNTRY")
    private String country;
}
