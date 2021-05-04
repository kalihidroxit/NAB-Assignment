package com.lozado.controller;

import com.lozado.entity.AppUser;
import com.lozado.repo.AppUserRepo;
import com.lozado.repo.CategoriesRepo;
import com.lozado.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    @Autowired
    private CategoriesRepo categoriesRepo;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse> Login() throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        apiResponse.setData("Hello World");
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
