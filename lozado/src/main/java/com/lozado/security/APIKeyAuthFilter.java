package com.lozado.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.lozado.entity.AppUser;
import com.lozado.repo.AppUserRepo;
import com.lozado.utils.ApiResponse;
import com.lozado.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class APIKeyAuthFilter extends OncePerRequestFilter {

    private final AppUserRepo appUserRepo;

    public APIKeyAuthFilter(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter)
            throws ServletException, IOException {
        try {
            String apiKey = req.getHeader(Constants.X_API_KEY);

            appUserRepo.findByApiKey(apiKey).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            filter.doFilter(req, res);
        } catch (UsernameNotFoundException e) {
            setResponse(res, "User Not Found", HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
        } catch (NullPointerException nil) {
            setResponse(res, "Invalid Token", HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN);
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
