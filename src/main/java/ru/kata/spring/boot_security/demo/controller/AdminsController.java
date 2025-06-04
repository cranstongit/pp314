package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String visitAdminPage(Principal principal, ModelMap model) {

        model.addAttribute("getUsers", userService.findAll()); //получаем всех пользователей
        model.addAttribute("admin", userService.findByUsername(principal.getName()));

        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.findAll()); // Добавим роли

        return "admin";
    }


    @GetMapping("/404")
    public String showError(ModelMap model) {
        model.addAttribute("errorMessage", "Something went wrong");
        return "404";
    }


    @PostMapping("/newuser")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String newUser(@ModelAttribute("newUser") User user, ModelMap model) {

        try {
            userService.save(user);
        } catch (IllegalArgumentException e) { // обработка не выбранных ролей или если роли отсутствуют
            model.addAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "404";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при сохранении пользователя: " + e.getMessage());
            return "404";
        }

        return "redirect:/admin";
    }


    @PostMapping("/deleteuser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String removeUser(@PathVariable("id") long id, ModelMap model) {

        try {
            userService.delete(id);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Проблема с удалением пользователя: " + e.getMessage());
            return "404";
        }

        return "redirect:/admin";
    }


    @PostMapping("/edituser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") //второй слой защиты
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute("user") User user, ModelMap model) {

        try {
            userService.update(id, user);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "Ошибка при изменении: " + e.getMessage());
            return "404";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Ошибка при изменении: " + e.getMessage());
            return "404";
        }

        return "redirect:/admin";
    }

}
