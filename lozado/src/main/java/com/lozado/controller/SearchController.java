package com.lozado.controller;

import com.lozado.dto.ProductDto;
import com.lozado.entity.Brands;
import com.lozado.entity.Categories;
import com.lozado.entity.Products;
import com.lozado.repo.ProductsRepo;
import com.lozado.service.SearchService;
import com.lozado.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name,
                                                        @RequestParam(required = false) List<String> brands,
                                                        @RequestParam(required = false) List<String> categories) throws Exception{
        ApiResponse apiResponse = searchService.getProductByName(name, brands, categories);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
