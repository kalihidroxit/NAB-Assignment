package org.smartchoice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartchoice.controller.AuthenticationController;
import org.smartchoice.entity.AppUser;
import org.smartchoice.entity.TokenStore;
import org.smartchoice.repo.AppUserRepo;
import org.smartchoice.repo.TokenStoreRepo;
import org.smartchoice.service.AuthenticationService;
import org.smartchoice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private AppUserRepo appUserRepo;

    @MockBean
    private TokenStoreRepo tokenStoreRepo;

    @Autowired
    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        String encryptedPwd = bCryptPasswordEncoder.encode("123456");
        AppUser appUser = new AppUser();
        appUser.setUserName("mvp_user");
        appUser.setPassword(encryptedPwd);

        Mockito.when(appUserRepo.findByUserName("mvp_user")).thenReturn(Optional.of(appUser));

        String token = authenticationService.doGenerateToken(appUser);

        TokenStore tokenStore = new TokenStore();
        tokenStore.setToken(token);
        tokenStore.setUser("mvp_user");

        Mockito.when(tokenStoreRepo.findByTokenAndIsExpFalse("mvp_user_token")).thenReturn(Optional.of(tokenStore));
    }

    @Test
    public void testFoundUser(){
        Assertions.assertTrue(appUserRepo.findByUserName("mvp_user").isPresent());
    }

    @Test
    public void testNotFoundUser() {
        UsernameNotFoundException ex = Assertions.assertThrows(UsernameNotFoundException.class,() -> appUserRepo.findByUserName("guest_user")
                .orElseThrow(() -> new UsernameNotFoundException("user not found")));

        Assertions.assertEquals(ex.getMessage(), "user not found");
    }

    @Test
    public void testTokenIsAvailable() {
        Optional<TokenStore> tokenStore = tokenStoreRepo.findByTokenAndIsExpFalse("mvp_user_token");

        Assertions.assertTrue(tokenStore.isPresent());
    }

    @Test
    public void testGeneratedToken() {
        AppUser appUser = appUserRepo.findByUserName("mvp_user").get();
        String token = authenticationService.doGenerateToken(appUser);

        Assertions.assertDoesNotThrow(() -> Jwts.parser().setSigningKey(Constants.SECRET).parseClaimsJws(token));
    }

    @Test
    public void testTokenExpired() {
        LocalDateTime dateTime = LocalDateTime.now();
        String token = Jwts.builder().setSubject("mvp_user")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(dateTime.plusSeconds(1).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, Constants.SECRET).compact();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        Assertions.assertThrows(ExpiredJwtException.class, () -> Jwts.parser().setSigningKey(Constants.SECRET).parseClaimsJws(token));
    }

    @Test
    public void testLogout() {
        TokenStore tokenStore = tokenStoreRepo.findByTokenAndIsExpFalse("mvp_user_token").get();
        tokenStore.setIsExp(Boolean.TRUE);
        Mockito.when(tokenStoreRepo.save(tokenStore)).thenReturn(tokenStore);

        tokenStoreRepo.save(tokenStore);

        Assertions.assertFalse(tokenStoreRepo.findByTokenAndIsExpFalse(tokenStore.getToken()).isPresent());
    }
}
