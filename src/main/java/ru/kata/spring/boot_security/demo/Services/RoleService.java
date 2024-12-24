package ru.kata.spring.boot_security.demo.Services;

import ru.kata.spring.boot_security.demo.Models.Role;

public interface RoleService {
    Role findByName(String name);
}