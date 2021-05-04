package com.lozado.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Data
@Table(name = "SUPPLIERS")
public class Suppliers extends BaseEntity{

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ACCOUNT_NO")
    private String accountNo;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    private List<Products> productsList = new ArrayList<>();
}
