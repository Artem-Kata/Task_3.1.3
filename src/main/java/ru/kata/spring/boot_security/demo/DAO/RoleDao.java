package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.Models.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    Role findByName(String name);
}