//package ru.kata.spring.boot_security.demo.configs;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class MvcConfig implements WebMvcConfigurer { //конфигурационный класс Spring MVC (не Security). Отвечает за настройку отображения страниц без написания контроллеров.
//    //WebMvcConfigurer - Позволяет кастомизировать поведение Spring MVC, в частности маршруты, форматтеры, ресурсы и т. д.
//    public void addViewControllers(ViewControllerRegistry registry) { //addViewControllers - Позволяет зарегистрировать простые маршруты (URI → View) без логики в контроллерах.
//        registry.addViewController("/user").setViewName("user"); //откроешь /user — отобразится шаблон user.html (в resources/templates/
//        registry.addViewController("/admin").setViewName("admin"); //аналогично user для admin
//        //все это игнорируется если используются контроллеры
//    }
//}
