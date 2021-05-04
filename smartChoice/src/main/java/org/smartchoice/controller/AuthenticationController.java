package org.smartchoice.controller;

import org.smartchoice.entity.AppUser;
import org.smartchoice.entity.TokenStore;
import org.smartchoice.repo.AppUserRepo;
import org.smartchoice.repo.TokenStoreRepo;
import org.smartchoice.service.AuthenticationService;
import org.smartchoice.utils.ApiResponse;
import org.smartchoice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

   @Autowired
   private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestParam String userName, @RequestParam String password) throws Exception{
        ApiResponse apiResponse = authenticationService.login(userName, password);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(Constants.AUTH_HEADER_NAME) String token) throws UsernameNotFoundException{
        ApiResponse apiResponse = authenticationService.logout(token);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
