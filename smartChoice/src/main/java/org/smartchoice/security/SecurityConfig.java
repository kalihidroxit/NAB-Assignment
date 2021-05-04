package org.smartchoice.security;

import org.smartchoice.repo.AppUserRepo;
import org.smartchoice.repo.TokenStoreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    protected AppUserRepo appUserRepo;

    @Autowired
    protected TokenStoreRepo tokenStoreRepo;

    @Override
    public void configure(WebSecurity web) {
        // Filters will not get executed for the resources
        web.ignoring().antMatchers("/h2/**", "/auth/login**");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated()
                .and().csrf().disable().addFilterBefore(new JwtAuthenticationFilter(appUserRepo, tokenStoreRepo), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
