package org.smartchoice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartchoice.entity.ProductLookup;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ProductDto extends BaseDto {
    private Double price;

    private Double discount;

    private String promotion;

    private Double rating;

    private String updatedAt;

    private String discountExp;

    private String discountStart;

    private String url;

    private CategoryDto categoryDto;

    private BrandDto brandDto;

    private SupplierDto supplierDto;

    public ProductDto (ProductLookup productLookup) {
        this.setName(productLookup.getName());
        this.price = productLookup.getPrice();
        this.discount = productLookup.getDiscount();
        this.promotion = productLookup.getPromotion();
        this.rating = productLookup.getRating();
        this.updatedAt = productLookup.getUpdatedAt().toString();
        this.discountExp = productLookup.getDiscountExp().toString();
        this.discountStart = productLookup.getDiscountStart().toString();
        this.url = productLookup.getUrl();
        this.setId(productLookup.getProductId());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(productLookup.getCategory());
        this.categoryDto = categoryDto;

        BrandDto brandDto = new BrandDto();
        brandDto.setName(productLookup.getBrand());
        this.brandDto = brandDto;

        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setName(productLookup.getSupplierName());
        supplierDto.setFullInfo(productLookup.getSupplierInfo());
        this.supplierDto = supplierDto;
    }

}
