package com.lozado.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lozado.entity.Products;
import com.lozado.entity.Suppliers;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDto extends BaseDto{

    private String address;

    private String phone;

    private String accountNo;

    public SupplierDto(Suppliers suppliers) {
        super(suppliers);
        this.address = suppliers.getAddress();
        this.phone = suppliers.getPhone();
        this.accountNo = suppliers.getAccountNo();
    }
}
