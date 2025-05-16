package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    public String visitUserPage(Principal principal, ModelMap model) {

        String username = principal.getName();

        User user = userService.findByUsername(username);

        if (user == null || username == null) {
            model.addAttribute("errorMessage", "Данные пользователя отсутствуют в БД.");
            return "404";
        }

        model.addAttribute("user", user);

        return "user";
    }

    @GetMapping("/404")
    public String showError(ModelMap model) {

        model.addAttribute("errorMessage", "Something went wrong");

        return "404";
    }

}
