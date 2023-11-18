package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.models.user.Role;
import ar.edu.itba.paw.webapp.auth.AuthPageFilter;
import ar.edu.itba.paw.webapp.auth.LoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Value("classpath:rememberme.key")
    private Resource remembermeKey;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private Environment env;
    private final static String[] anonymousPaths = {"/login", "/forgot-password", "/challenge", "/register"};

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.addFilterBefore(new AuthPageFilter(env.getProperty("base.root"), anonymousPaths), DefaultLoginPageGeneratingFilter.class)
                .sessionManagement()
                .invalidSessionUrl("/login")
                .and().authorizeRequests()
                    .antMatchers(anonymousPaths).anonymous()
                    .antMatchers(HttpMethod.GET,
                                "/",
                                "/search",
                                "/notes/{noteId}",
                                "/notes/{noteId}/download",
                                "/notes/{noteId}/reviews",
                                "/directory/{directoryId}",
                                "/profile/{userId}/picture",
                                "/user/{userId}/note-board"
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
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/svg/**", "/css/**", "/js/**", "/image/**", "favicon.ico", "/errors/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}