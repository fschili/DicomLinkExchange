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
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Added workaround with @SecurityScheme for declarative description of JWT to avoid bug described here:
 * 
 * https://stackoverflow.com/questions/77717946/enabling-an-authorize-button-in-swagger-ui-declaratively
 * 
 * Normally the should work out of the box with the specifications in the dicomLinkExchange.yaml file *
 */

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "bearerAuth", description = "authorization with JWT token", scheme = "bearer", bearerFormat = "JWT")
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
