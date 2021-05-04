package org.smartchoice.controller;

import org.smartchoice.service.SearchService;
import org.smartchoice.utils.ApiResponse;
import org.smartchoice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(@RequestParam String keyword,
                                              @RequestParam(required = false) List<String> categories,
                                              @RequestParam(required = false) List<String> brands,
                                              @RequestParam(required = false) List<String> thirdParties,
                                              @RequestHeader(Constants.AUTH_HEADER_NAME) String token){
        ApiResponse apiResponse = searchService.searchByKeyword(keyword, categories, brands, thirdParties, token);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/item")
    public ResponseEntity<ApiResponse> search(@RequestParam Long id,
                                              @RequestParam String thirdParty) throws Exception{
        ApiResponse apiResponse = searchService.getItemDetail(id, thirdParty);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
