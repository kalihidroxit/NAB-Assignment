package org.smartchoice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.smartchoice.entity.TokenStore;
import org.smartchoice.exception.MissingHeaderException;
import org.smartchoice.repo.AppUserRepo;
import org.smartchoice.repo.TokenStoreRepo;
import org.smartchoice.utils.ApiResponse;
import org.smartchoice.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AppUserRepo appUserRepo;

    private final TokenStoreRepo tokenStoreRepo;

    public JwtAuthenticationFilter(AppUserRepo appUserRepo, TokenStoreRepo tokenStoreRepo) {
        this.appUserRepo = appUserRepo;
        this.tokenStoreRepo = tokenStoreRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter)
            throws ServletException, IOException {
        TokenStore tokenStore = null;
        try {
            String header = req.getHeader(Constants.AUTH_HEADER_NAME);
            if (header != null && header.startsWith(Constants.AUTH_HEADER_TOKEN_PREFIX)) {
                String authToken = header.replace(Constants.AUTH_HEADER_TOKEN_PREFIX, "");
                tokenStore = tokenStoreRepo.findByTokenAndIsExpFalse(authToken)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                Jwts.parser().setSigningKey(Constants.SECRET).parseClaimsJws(authToken).getBody().getSubject();

                filter.doFilter(req, res);
            } else {
                throw new MissingHeaderException("Missing header");
            }
        } catch (UsernameNotFoundException e) {
            setResponse(res, "User Not Found", HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
        } catch (NullPointerException nil) {
            setResponse(res, "Invalid Token", HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN);
        } catch (ExpiredJwtException | MissingHeaderException exp){
            if (exp instanceof ExpiredJwtException && tokenStore != null) {
                tokenStore.setIsExp(Boolean.TRUE);
                tokenStoreRepo.save(tokenStore);
            }
            setResponse(res, exp.getMessage(), HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN);
        }
    }

    private void setResponse(HttpServletResponse response, String message, int httpStatusValue, HttpStatus httpStatus)
            throws IOException {
        response.setStatus(httpStatusValue);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(httpStatus);
        apiResponse.setStatusCode(httpStatusValue);
        apiResponse.setMessage(message);
        String jsonRespString = ow.writeValueAsString(apiResponse);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(jsonRespString);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
