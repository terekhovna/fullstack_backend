package edu.phystech.terekhov_na.stickers.config;

import edu.phystech.terekhov_na.stickers.security.CustomAuthenticationEntryPoint;
import edu.phystech.terekhov_na.stickers.security.MyAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class MyWebSecurityConfiguredAdapter extends WebSecurityConfigurerAdapter {

    private final MyAuthenticationProvider authProvider;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/api/tabs/**").authenticated()
                .antMatchers("/api/tasks/**").authenticated()
                .antMatchers("/api/user").authenticated()
                .antMatchers("/api/**").permitAll()
                .and()
                .formLogin().loginProcessingUrl("/api/sign_in/**")
                    .permitAll().successForwardUrl("/api/user")
                    .failureForwardUrl("/api/sign_in_error")
                .and()
                .logout().logoutUrl("/api/logout")
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint);
        http.authenticationProvider(authProvider);
    }
}
