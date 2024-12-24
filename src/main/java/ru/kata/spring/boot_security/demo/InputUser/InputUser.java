package ru.kata.spring.boot_security.demo.InputUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.DAO.RoleDao;
import ru.kata.spring.boot_security.demo.DAO.UserDao;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class InputUser {
    private final UserDao userRepository;
    private final RoleDao roleRepository;

    @Autowired
    public InputUser(UserDao userRepository, RoleDao roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    User admin = new User("admin", "adminov", (byte) 30, "$2a$12$Y03tCK62VmaaZLeWlU8cnelB.m/Y4LOMgnC24UpxsJzOv2UE/Uc0K");
    User user = new User("user", "userov", (byte) 30, "$2a$12$oQKP9KHR5an3eHvvK2sGcOEW5Z0zomeLv2mopUN5DqOCJ7u5R9qCa");

    Role roleAdmin = new Role("ROLE_ADMIN");
    Role roleUser = new Role("ROLE_USER");
    Set<Role> setAdmin = new HashSet<>();
    Set<Role> setUser = new HashSet<>();

    @PostConstruct
    public void initializationTable() {
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);

        setAdmin.add(roleAdmin);
        admin.setRoles(setAdmin);
        userRepository.save(admin);

        setUser.add(roleUser);
        user.setRoles(setUser);
        userRepository.save(user);
    }
}