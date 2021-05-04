package com.lozado.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "PRODUCT_COMMENTS")
public class ProductComments extends BaseEntity {

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Products product;
}
