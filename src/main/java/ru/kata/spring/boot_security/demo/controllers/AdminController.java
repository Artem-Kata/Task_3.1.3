package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Services.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String admin(Model model, Principal principal) {
        model.addAttribute("user", userService.getAllUsers());
        model.addAttribute("admin", userService.oneUser(principal));
        return "users";
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        model.addAttribute("user", userService.oneUser(principal));
        return "oneAdmin";
    }

    @GetMapping(value = "/new")
    public String newUser(Model model, Principal principal) {
        model.addAttribute("user", new User());
        model.addAttribute("admin", userService.oneUser(principal));
        return "newUser";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user, @RequestParam(value = "role") Set<Role> roles) {
        userService.save(userService.createUser(user, roles));
        return "redirect:/admin";
    }

    @GetMapping(value = "/delete")
    public String delete(@RequestParam("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("user") User user, @RequestParam("id") long id, @RequestParam(value = "role", required = false) Set<Role> roles) {
        userService.update(id, userService.updateUser(user, roles, id));
        return "redirect:/admin";
    }
}