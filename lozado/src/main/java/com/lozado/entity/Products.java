package com.lozado.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "PRODUCTS")
public class Products extends BaseEntity{

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "DISCOUNT")
    private Double discount;

    @Column(name = "PROMOTION")
    private String promotion;

    @Column(name = "RATING")
    private Double rating;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "DISCOUNT_EXP")
    private LocalDateTime discountExp;

    @Column(name = "DISCOUNT_START")
    private LocalDateTime discountStart;

    @Column(name = "URL_IMAGE")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Categories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Brands brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Suppliers supplier;
}
