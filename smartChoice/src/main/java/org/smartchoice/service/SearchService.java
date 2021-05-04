package org.smartchoice.service;

import org.smartchoice.utils.ApiResponse;

import java.util.List;

public interface SearchService {
    ApiResponse searchByKeyword(String keyword, List<String> categories, List<String> brands, List<String> thirdParties, String token);

    ApiResponse getItemDetail(Long id, String thirdParty) throws Exception;
}
