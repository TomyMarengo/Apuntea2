package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationCodesService;
import ar.edu.itba.paw.webapp.auth.ApunteaAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.auth.filters.AbstractAuthFilter;
import ar.edu.itba.paw.webapp.auth.handlers.ApunteaAccessDeniedHandler;
import ar.edu.itba.paw.webapp.auth.handlers.AuthFailureHandler;
import ar.edu.itba.paw.webapp.auth.handlers.AuthSuccessHandler;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthProvider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.services"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Value("classpath:rememberme.key")
    private Resource remembermeKey;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Autowired
    private ApunteaAuthenticationEntryPoint authEntryPoint;

    @Autowired
    private JwtAuthProvider jwtAuthProvider;

    @Autowired
    private Environment env;

    @Autowired
    private VerificationCodesService verificationCodesService;

    @Autowired
    private UserService userService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ApunteaAccessDeniedHandler();
    }

    @Bean
    public ObjectMapper objectMapper() {
        MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jacksonMessageConverter.getObjectMapper();

        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return jacksonMessageConverter.getObjectMapper();
    }

    /*@Override
    public void configure(final HttpSecurity http) throws Exception {
        http.addFilterBefore(new AuthPageFilter(env.getProperty("base.root"), anonymousPaths), DefaultLoginPageGeneratingFilter.class)
                .sessionManagement()
                .invalidSessionUrl("/login")
                .and().authorizeRequests()
                    .antMatchers(anonymousPaths).anonymous()
                    .antMatchers(HttpMethod.GET,
                                "/",
                                "/institutions",
                                "/search",
                                "/notes/{noteId}",
                                "/notes/{noteId}/download",
                                "/notes/{noteId}/reviews",
                                "/directory/{directoryId}",
                                "/profile/{userId}/picture",
                                "/user/{userId}/note-board",
                                "/user/{userId}/reviews"

                    ).permitAll()
                    .regexMatchers(HttpMethod.GET, "/[0-9][0-9][0-9]").permitAll()
                    .antMatchers("/manage/**").hasRole(Role.ROLE_ADMIN.getShortName())
                    .anyRequest().authenticated()

                .and().formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", false)
                    .failureHandler(loginFailureHandler)
                .and().rememberMe()
                    .rememberMeParameter("rememberMe")
                    .userDetailsService(userDetailsService)
                    .key(StreamUtils.copyToString(remembermeKey.getInputStream(), StandardCharsets.UTF_8))
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(15))

                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")

                .and().exceptionHandling()
                    .accessDeniedPage("/403")

                .and().headers().frameOptions().sameOrigin()

                .and().csrf().disable();
    }*/

    @Bean
    public AbstractAuthFilter abstractAuthFilter() throws Exception {
        AbstractAuthFilter abstractAuthFilter = new AbstractAuthFilter(userService, verificationCodesService);
        abstractAuthFilter.setAuthenticationManager(authenticationManagerBean());
        abstractAuthFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        abstractAuthFilter.setAuthenticationFailureHandler(authFailureHandler);
        return abstractAuthFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Access-Token","Refresh-Token", "X-Total-Pages", "X-Total-Count", "Content-Disposition", "Link", "Authorization", "Cache-Control", "Content-Type", "Location"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }


    // TODO: Change to more specific endpoints
    private static final String[] PERMIT_ALL_ENDPOINTS = {
            "/api/users/**", "/api/directories/**", "/api/notes/**", "/api/reviews/**", "/api/institutions/**", "/api/pictures/{id}", "/api/subjects/**"
    };

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .headers().frameOptions().disable()
                .and().exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(accessDeniedHandler()).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users").anonymous()
                .antMatchers(HttpMethod.GET, PERMIT_ALL_ENDPOINTS).not().hasRole(Role.ROLE_VERIFY.getShortName()) // During the verification process, users can only change their passwords
                .antMatchers(HttpMethod.HEAD, PERMIT_ALL_ENDPOINTS).not().hasRole(Role.ROLE_VERIFY.getShortName())
                .antMatchers(HttpMethod.PATCH, "/api/users/{userId}").authenticated()
                .anyRequest().hasAnyRole(Role.ROLE_ADMIN.getShortName(), Role.ROLE_STUDENT.getShortName())
                .and().addFilterAfter(abstractAuthFilter(), AnonymousAuthenticationFilter.class);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/svg/**", "/css/**", "/js/**", "/image/**", "favicon.ico", "/errors/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(jwtAuthProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}