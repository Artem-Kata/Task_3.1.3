package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Services.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminContoller {
    @Autowired
    private UserService userService;

    @GetMapping
    public String admin(Model model) {
        model.addAttribute("user", userService.getAllUsers());
        return "users";
    }

    @GetMapping(value = "/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
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

    @GetMapping(value = "/edit")
    public String edit(@RequestParam(value = "id") long id, Model model) {
        model.addAttribute("user", userService.getOne(id));
        return "edit";
    }

    @PostMapping(value = "/update")
    public String update(@ModelAttribute("user") User user, @RequestParam("id") long id, @RequestParam(value = "role", required = false) Set<Role> roles) {
        userService.update(id, userService.updateUser(user, roles, id));
        return "redirect:/admin";
    }
}