package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping({"/", ""})
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public ModelAndView adminPage(Principal principal) {

        ModelAndView mavAdmin = new ModelAndView("admin");

        mavAdmin.addObject("getUsers", userService.findAll()); //получаем всех пользователей
        mavAdmin.addObject("admin", userService.findByUsername(principal.getName()));
        mavAdmin.addObject("newUser", new User());
        mavAdmin.addObject("allRoles", roleService.findAll()); // Добавим роли

        return mavAdmin;
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


    @PostMapping("/newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView newUser(@ModelAttribute("newUser") User user) {

        try {
            userService.save(user);
        } catch (IllegalArgumentException e) {
            ModelAndView mavError = new ModelAndView("error"); // имя шаблона ошибки
            mavError.addObject("errorMessage", "Ошибка с ролями: " + e.getMessage());
            return mavError;
        } catch (Exception e) {
            ModelAndView mavError = new ModelAndView("error"); // имя шаблона ошибки
            mavError.addObject("errorMessage", "Ошибка: " + e.getMessage());
            return mavError;
        }

        return new ModelAndView("redirect:/admin");
    }


    @PostMapping("/deleteuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public ModelAndView removeUser(@RequestParam("id") long id) {

        try {
            userService.delete(id);
        } catch (EntityNotFoundException e) {
            ModelAndView mavError = new ModelAndView("error");
            mavError.addObject("errorMessage", "Проблема с удалением пользователя: " + e.getMessage());
            return mavError;
        }

        return new ModelAndView("redirect:/admin");
    }


    @PostMapping("/edituser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public ModelAndView updateUser(@RequestParam("id") long id, @ModelAttribute("user") User user) {

        try {
            userService.update(id, user);
        } catch (EntityNotFoundException e) {
            ModelAndView mavError = new ModelAndView("error");
            mavError.addObject("errorMessage", "Ошибка при изменении: " + e.getMessage());
            return mavError;
        } catch (RuntimeException e) {
            ModelAndView mavError = new ModelAndView("error");
            mavError.addObject("errorMessage", "Ошибка при изменении: " + e.getMessage());
            return mavError;
        }

        return new ModelAndView("redirect:/admin");
    }

}
