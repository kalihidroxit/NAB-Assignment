package com.lozado.security;

import com.lozado.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    protected AppUserRepo appUserRepo;

    @Override
    public void configure(WebSecurity web) {
        // Filters will not get executed for the resources
        web.ignoring().antMatchers("/h2/**");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated()
                .and().csrf().disable().addFilterBefore(new APIKeyAuthFilter(appUserRepo), UsernamePasswordAuthenticationFilter.class);
    }
}
