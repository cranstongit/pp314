package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler { //обработчик успешной аутентификации, который определяет, куда перенаправлять пользователя после входа.
    //AuthenticationSuccessHandler - Позволяет переопределить поведение после успешной авторизации.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, //onAuthenticationSuccess - Вызывается Spring Security, когда пользователь успешно вошёл в систему
                                        HttpServletResponse httpServletResponse, //HttpServletRequest и HttpServletResponse — стандартные веб-объекты сервлета
                                        Authentication authentication) throws IOException { // содержит данные об аутентифицированном пользователе, включая роли. Spring Security использует объект Authentication, пользователя авторизованной сессии.
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities()); //authorityListToSet(...) преобразует список в Set, authentication.getAuthorities() возвращает список ролей пользователя (ROLE_USER, ROLE_ADMIN)
        // AuthorityUtils - вспомогательный класс Spring Security, который упрощает работу с объектами GrantedAuthority
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/user");
        } else {
            httpServletResponse.sendRedirect("/");
        }
    }
}