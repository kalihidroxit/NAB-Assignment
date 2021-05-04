package org.smartchoice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.smartchoice.dto.BrandDto;
import org.smartchoice.dto.CategoryDto;
import org.smartchoice.dto.ProductDto;
import org.smartchoice.dto.SupplierDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_LOOKUP")
public class ProductLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "NAME")
    private String name;

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

    @Column(name = "PULLED_AT")
    private LocalDateTime pulledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private ThirdParty thirdParty;

    @Column(name = "PRODUCT_IDF")
    private Long productId;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "SUPPLIER_NAME")
    private String supplierName;

    @Column(name = "SUPPLIER_INFO")
    private String supplierInfo;


    public ProductLookup(ProductDto productDto) {
        this.name = productDto.getName();
        this.price = productDto.getPrice();
        this.discount = productDto.getDiscount();
        this.promotion = productDto.getPromotion();
        this.rating = productDto.getRating();
        this.updatedAt = LocalDateTime.parse(productDto.getUpdatedAt());
        this.discountExp = LocalDateTime.parse(productDto.getDiscountExp());
        this.discountStart = LocalDateTime.parse(productDto.getDiscountStart());
        this.url = productDto.getUrl();
        this.productId = productDto.getId();
        this.category = productDto.getCategoryDto() != null?productDto.getCategoryDto().getName():null;
        this.brand = productDto.getBrandDto() != null?productDto.getBrandDto().getName():null;
        if (productDto.getSupplierDto() != null) {
            this.supplierName = productDto.getSupplierDto().getName();
            String addr = productDto.getSupplierDto().getAddress()!=null?productDto.getSupplierDto().getAddress():"";
            String phone = productDto.getSupplierDto().getPhone()!=null?productDto.getSupplierDto().getPhone():"";
            this.supplierInfo = "Addr: " + addr + ", Phone: " + phone;
        }
    }
}
