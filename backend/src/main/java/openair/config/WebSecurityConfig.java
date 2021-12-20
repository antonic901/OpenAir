package openair.config;

import openair.security.auth.RestAuthenticationEntryPoint;
import openair.security.auth.TokenAuthenticationFilter;
import openair.service.UserService;
import openair.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private TokenUtils tokenUtils;

    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder, UserService userService, RestAuthenticationEntryPoint restAuthenticationEntryPoint, TokenUtils tokenUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.tokenUtils = tokenUtils;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/auth/check-username/**").permitAll()
                .antMatchers("/uploads").permitAll()
                .antMatchers("/email/send-mail").permitAll()
                .antMatchers("/api/money/convert/{date}/{fromCurrency}/{toCurrency}/{quantity}").permitAll()
                .antMatchers("/auth/role").permitAll()
                .anyRequest().authenticated().and()
                .cors().and()
                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userService), BasicAuthenticationFilter.class);
        http.csrf().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers(HttpMethod.GET, "/auth/login");
        //web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");
    }
}
