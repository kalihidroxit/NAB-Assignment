package com.lozado.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lozado.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ProductDto extends BaseDto{
    private Double price;

    private Double discount;

    private String promotion;

    private Double rating;

    private LocalDateTime updatedAt;

    private LocalDateTime discountExp;

    private LocalDateTime discountStart;

    private CategoryDto categoryDto;

    private BrandDto brandDto;

    private SupplierDto supplierDto;

    private String url;

    public ProductDto(Products products) {
        super(products);
        this.setId(products.getId());
        this.price = products.getPrice();
        this.discount = products.getDiscount();
        this.promotion = products.getPromotion();
        this.rating = products.getRating();
        this.updatedAt = products.getUpdatedAt();
        this.discountExp = products.getDiscountExp();
        this.discountStart = products.getDiscountStart();
        this.url = products.getUrl();
        this.categoryDto = new CategoryDto(products.getCategory());
        this.brandDto = new BrandDto(products.getBrand());
        this.supplierDto = new SupplierDto(products.getSupplier());
    }
}
