package org.smartchoice;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.smartchoice.controller.SearchController;
import org.smartchoice.dto.BrandDto;
import org.smartchoice.dto.CategoryDto;
import org.smartchoice.dto.ProductDto;
import org.smartchoice.dto.SupplierDto;
import org.smartchoice.factory.SearchFactory;
import org.smartchoice.factory.SearchResult;
import org.smartchoice.repo.AppUserRepo;
import org.smartchoice.repo.TokenStoreRepo;
import org.smartchoice.service.SearchService;
import org.smartchoice.utils.ApiResponse;
import org.smartchoice.utils.ThirdPartyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
public class SearchTest {

    @MockBean
    private SearchService searchService;

    @MockBean
    private AppUserRepo appUserRepo;

    @MockBean
    private TokenStoreRepo tokenStoreRepo;

    @Before
    public void setUp() {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            SupplierDto supplierDto = new SupplierDto();
            supplierDto.setName("sup" + i);
            supplierDto.setAddress("addr" + i);
            supplierDto.setPhone("phone" + i);

            BrandDto brandDto = new BrandDto();
            brandDto.setName("br" + i);

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName("cate" + i);

            ProductDto productDto = new ProductDto();
            productDto.setId((long) i);
            productDto.setName("item" + i);
            productDto.setPrice(100.0 * i);
            productDto.setDiscount(0.01 * i);
            productDto.setPromotion("promo" + i);
            productDto.setRating(1.0*i);
            productDto.setUpdatedAt(LocalDateTime.now().toString());
            productDto.setDiscountExp(LocalDateTime.now().toString());
            productDto.setDiscountStart(LocalDateTime.now().toString());
            productDto.setUrl("url" + i);
            productDto.setCategoryDto(categoryDto);
            productDto.setBrandDto(brandDto);
            productDto.setSupplierDto(supplierDto);

            productDtoList.add(productDto);
        }
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        apiResponse.setData(productDtoList);

        Mockito.when(searchService.searchByKeyword(Mockito.anyString(), Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), Mockito.anyString())).thenReturn(apiResponse);
    }

    @Test
    public void testCallFromThirdParty() {
        ApiResponse apiResponse = searchService.searchByKeyword(Mockito.anyString(), Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), Mockito.anyString());
        Assertions.assertEquals(apiResponse.getStatus(), HttpStatus.OK);
    }

    @Test
    public void testSearchFactory() {
        ApiResponse apiResponse = searchService.searchByKeyword(Mockito.anyString(), Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), Mockito.anyString());
        SearchResult searchResult = SearchFactory.getSearch(ThirdPartyType.Lozado);
        List<ProductDto> productDtoList = searchResult.getProduct(apiResponse.getData());
        Assertions.assertEquals(2, productDtoList.size());
    }

    @Test
    public void testSearchResultSupplier() {
        ApiResponse apiResponse = searchService.searchByKeyword(Mockito.anyString(), Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), Mockito.anyString());
        SearchResult searchResult = SearchFactory.getSearch(ThirdPartyType.Lozado);
        List<ProductDto> productDtoList = searchResult.getProduct(apiResponse.getData());

        List<String> names = Arrays.asList("sup0", "sup1");
        SupplierDto supplierDto1 = productDtoList.get(0).getSupplierDto();
        Assertions.assertTrue(names.contains(supplierDto1.getName()));

        SupplierDto supplierDto2 = productDtoList.get(1).getSupplierDto();
        Assertions.assertTrue(names.contains(supplierDto2.getName()));

        Assertions.assertNotEquals(supplierDto1.getName(), supplierDto2.getName());
    }

    @Test
    public void testSearchResultBrand() {
        ApiResponse apiResponse = searchService.searchByKeyword(Mockito.anyString(), Mockito.anyList(), Mockito.anyList(), Mockito.anyList(), Mockito.anyString());
        SearchResult searchResult = SearchFactory.getSearch(ThirdPartyType.Lozado);
        List<ProductDto> productDtoList = searchResult.getProduct(apiResponse.getData());

        List<String> names = Arrays.asList("br0", "br1");
        BrandDto brandDto1 = productDtoList.get(0).getBrandDto();
        Assertions.assertTrue(names.contains(brandDto1.getName()));

        BrandDto brandDto2 = productDtoList.get(1).getBrandDto();
        Assertions.assertTrue(names.contains(brandDto2.getName()));

        Assertions.assertNotEquals(brandDto1.getName(), brandDto2.getName());
    }
}
