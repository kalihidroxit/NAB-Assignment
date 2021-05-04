package org.smartchoice.seviceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.smartchoice.entity.AppUser;
import org.smartchoice.entity.TokenStore;
import org.smartchoice.repo.AppUserRepo;
import org.smartchoice.repo.TokenStoreRepo;
import org.smartchoice.service.AuthenticationService;
import org.smartchoice.utils.ApiResponse;
import org.smartchoice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private TokenStoreRepo tokenStoreRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ApiResponse login(String userName, String password) throws Exception {
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();

        AppUser appUser = appUserRepo.findByUserName(userName).orElseThrow(() -> new Exception("Invalid credentials"));
        Assert.isTrue(bCryptPasswordEncoder.matches(password, appUser.getPassword()), "Invalid credentials");

        String token = doGenerateToken(appUser);
        TokenStore tokenStore = new TokenStore();
        tokenStore.setToken(token);
        tokenStore.setUser(appUser.getUserName());
        tokenStoreRepo.save(tokenStore);
        apiResponse.setData(token);

        return apiResponse;
    }

    @Override
    public ApiResponse logout(String token) throws UsernameNotFoundException {
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        token = token.replace(Constants.AUTH_HEADER_TOKEN_PREFIX, "");
        // TODO: several cases not handle, just using for demo
        TokenStore tokenStore = tokenStoreRepo.findByTokenAndIsExpFalse(token)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        tokenStore.setIsExp(Boolean.TRUE);
        tokenStoreRepo.save(tokenStore);
        return apiResponse;
    }

    @Override
    public String doGenerateToken(AppUser appUser) {
        LocalDateTime dateTime = LocalDateTime.now();
        return Jwts.builder().setSubject(appUser.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(dateTime.plusMinutes(Constants.EXPIRATION_TIME)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, Constants.SECRET).compact();
    }
}
