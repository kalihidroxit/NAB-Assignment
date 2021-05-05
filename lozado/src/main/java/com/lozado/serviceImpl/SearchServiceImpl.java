package com.lozado.serviceImpl;

import com.lozado.dto.ProductDto;
import com.lozado.entity.Brands;
import com.lozado.entity.Categories;
import com.lozado.entity.Products;
import com.lozado.service.SearchService;
import com.lozado.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private EntityManager entityManager;

    @Override
    public ApiResponse getProductByName(String name, List<String> brands, List<String> categories) throws Exception {
        ApiResponse apiResponse = ApiResponse.getFailureResponse();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Products> root = cq.from(Products.class);
        List<Predicate> predicateList = new ArrayList<>();

        Predicate keyword = cb.like(cb.upper(root.get("name").as(String.class)), "%"+name.toUpperCase()+"%");
        predicateList.add(keyword);

        if (brands != null && !brands.isEmpty()) {
            System.out.println(brands);
            Subquery<Long> brandSubquery = cq.subquery(Long.class);
            Root<Brands> brandsRoot = brandSubquery.from(Brands.class);

            brandSubquery.select(brandsRoot.get("id"));

            List<Predicate> subConditions = new ArrayList<>();
            Predicate inBrand = brandsRoot.get("name").in(brands);
            subConditions.add(inBrand);

            brandSubquery.where(subConditions.toArray(new Predicate[0]));

            predicateList.add(root.get("brand").in(brandSubquery));
        }

        if (categories != null && !categories.isEmpty()) {
            Subquery<Long> categorySubquery = cq.subquery(Long.class);
            Root<Categories> categoryRoot = categorySubquery.from(Categories.class);

            categorySubquery.select(categoryRoot.get("id"));

            List<Predicate> subConditions = new ArrayList<>();
            Predicate inCategory = categoryRoot.get("name").in(brands);
            subConditions.add(inCategory);

            categorySubquery.where(subConditions.toArray(new Predicate[0]));

            predicateList.add(root.get("category").in(categorySubquery));
        }

        cq.select(root).distinct(true).where(predicateList.toArray(new Predicate[0]));
        TypedQuery<Products> typedQuery = entityManager.createQuery(cq);

        List<Products> productsList = typedQuery.getResultList();

        List<ProductDto> productDtoList = productsList.stream().map(ProductDto::new).collect(Collectors.toList());

        apiResponse = ApiResponse.getSuccessResponse();
        apiResponse.setData(productDtoList);
        return apiResponse;
    }
}
