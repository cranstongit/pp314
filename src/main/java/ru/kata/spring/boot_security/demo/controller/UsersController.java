package ru.kata.spring.boot_security.demo.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping({"/", ""})
    public ModelAndView visitUserPage(Principal principal) {

        User user = userService.findByUsername(principal.getName()); //получаем данные юзера

        ModelAndView mavUser = new ModelAndView("user"); //создаем мав
        mavUser.addObject("user", user); //добавляем пользователя в мав

        if (user == null || principal.getName() == null) { //проверка на null
            ModelAndView mavError = new ModelAndView("error");
            mavError.addObject("errorMessage", "Данные пользователя отсутствуют в БД.");
            return mavError;
        }

        return mavUser;
    }


    @GetMapping("/error")
    public ModelAndView showError(@RequestParam(name = "errorMessage", required = false) String errorMessage) {

        ModelAndView mavError = new ModelAndView("error");

        if (errorMessage != null) {
            mavError.addObject("errorMessage", errorMessage);  // добавляем параметр в модель
        } else {
            mavError.addObject("errorMessage", "Something went wrong");
        }

        return mavError;
    }

}
