package com.lozado.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lozado.entity.Categories;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CategoryDto extends BaseDto {

    public CategoryDto(Categories categories) {
        super(categories);
    }
}
