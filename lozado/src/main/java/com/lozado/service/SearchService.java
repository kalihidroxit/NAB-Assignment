package com.lozado.service;

import com.lozado.utils.ApiResponse;

import java.util.List;

public interface SearchService {
    ApiResponse getProductByName(String name, List<String> brands, List<String> categories) throws Exception;
}
