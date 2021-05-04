package com.lozado.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lozado.entity.Brands;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrandDto extends BaseDto {

    private String country;

    public BrandDto(Brands brands) {
        super(brands);
        this.country = brands.getCountry();
    }
}

