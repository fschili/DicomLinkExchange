package de.fschili.dlx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.fschili.dlx.jwt.JWTAuthenticationEntryPoint;
import de.fschili.dlx.jwt.JWTRequestFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String URL_BASE = "/dlx/v1";

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // CSRF not needed here
        httpSecurity.csrf().disable()
                .authorizeRequests()
                // exclude api-docs, swagger-ui and token endpoints from authentication
                .antMatchers("/api-docs/**", "/swagger-ui/**", URL_BASE + "/token/**", URL_BASE + "/tokentfa/**", URL_BASE + "/api_info/**").permitAll()
                // other requests must be authenticated by JWT
                .anyRequest().authenticated().and()
                // add JWT authentication implementation
                .exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
                // make sure we use stateless session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // add a filter to validate the token with every request
        httpSecurity.addFilterBefore(this.jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
