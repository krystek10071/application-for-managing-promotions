package com.example.managingpromotions.config;

import com.example.managingpromotions.filters.JsonObjectAuthenticationFilter;
import com.example.managingpromotions.filters.JwtAuthorizationFilter;
import com.example.managingpromotions.filters.RestAuthenticationFailureHandler;
import com.example.managingpromotions.filters.RestAuthenticationSuccessHandler;
import com.example.managingpromotions.model.repository.UserRepository;
import com.example.managingpromotions.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationSuccessHandler authenticationSuccessHandler;
    private final RestAuthenticationFailureHandler authenticationFailureHandler;
    private final String secret;
    private final DataSource dataSource;
    private final UserRepository userRepository;

    public SecurityConfig(RestAuthenticationSuccessHandler authenticationSuccessHandler, RestAuthenticationFailureHandler authenticationFailureHandler, @Value("${jwt.secret}") String secret, DataSource dataSource, UserRepository userRepository) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.secret = secret;
        this.dataSource = dataSource;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), new UserDetailsServiceImpl(userRepository), secret))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .headers().frameOptions().disable();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) {
        builder.authenticationProvider(authenticationProvider());
    }

    @Bean
    public JsonObjectAuthenticationFilter authenticationFilter() throws Exception {

        JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setAuthenticationManager(super.authenticationManager());

        return filter;
    }
}
