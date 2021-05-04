package com.lozado.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lozado.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class BaseDto {

    private Long id;

    private String name;

    public BaseDto(BaseEntity baseEntity) {
        this.name = baseEntity.getName();
    }
}
