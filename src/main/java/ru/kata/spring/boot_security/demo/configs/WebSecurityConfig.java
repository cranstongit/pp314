package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, PasswordEncoder passwordEncoder, UserService userService) {
        this.successUserHandler = successUserHandler;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/**", "/js/**").permitAll() //эти пути открыты для всех, белый список
                    .antMatchers("/admin/**").hasRole("ADMIN") //.hasRole("ADMIN") автоматически ищет "ROLE_ADMIN"
                    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated() //любые реквесты должны быть аутентифицированы
                .and()
                .formLogin().successHandler(successUserHandler).permitAll() //форма Spring, разрешена для всех. После атентификации переходит в соответствии с succesUserHandler
                .and()
                .logout().permitAll(); //форма Logout доступна всем

    }

    //DAO
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() { //провайдер для аутентификации через ДАО и БД
        System.out.println("daoAuthenticationProvider called");

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder); //ставим шифрование bcrypt
        provider.setUserDetailsService(userService); //добавляем userService

        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //используется для установки способа аутентификации, особенно если их несколько

        System.out.println("default inmemory called");

        auth.inMemoryAuthentication() //создаем и подключаем инмемори админа для тестирования
                .withUser("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN", "USER");

        auth.inMemoryAuthentication() //создаем и подключаем инмемори юзера для тестирования
                .withUser("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER");

        System.out.println("configure called");

        auth.authenticationProvider(daoAuthenticationProvider()); //подключаем ДАО БД аутентификацию
    }

}