package org.smartchoice.service;

import org.smartchoice.entity.AppUser;
import org.smartchoice.utils.ApiResponse;
import org.smartchoice.utils.Constants;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthenticationService {
    ApiResponse login(String userName, String password) throws Exception;

    ApiResponse logout(String token) throws UsernameNotFoundException;

    String doGenerateToken(AppUser appUser);
}
