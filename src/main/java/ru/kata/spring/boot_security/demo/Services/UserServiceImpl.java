package ru.kata.spring.boot_security.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.DAO.UserDao;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    @Lazy
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public User getOne(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    public void update(Long id, User user) {
        User oldUser = userDao.findById(id).get();
        oldUser.setUsername(user.getUsername());
        oldUser.setLastName(user.getLastName());
        oldUser.setAge(user.getAge());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setRoles(user.getRoles());
        userDao.save(oldUser);
    }

    @Override
    public User oneUser(Principal principal) {
        return (User) ((Authentication) principal).getPrincipal();
    }

    public Role getRole(String role) {
        return roleService.findByName(role);
    }

    @Override
    public User createUser(User user, Set<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roleSet = new HashSet<>();
        for (Role role : roles) {
            Role newRole = role;
            if (newRole != null) {
                roleSet.add(newRole);
            }
        }
        user.setRoles(roleSet);
        return user;
    }

    @Override
    public User updateUser(User user, Set<Role> roles, Long id) {
        User oldUser = getOne(id);

        // Обновление пароля
        String newPassword = user.getPassword();
        if (newPassword != null && !newPassword.isEmpty() && !passwordEncoder.matches(newPassword, oldUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            user.setPassword(oldUser.getPassword());
        }

        // Обновление ролей
        if (roles == null || roles.isEmpty()) {
            user.setRoles(oldUser.getRoles());
        } else {
            user.setRoles(new HashSet<>(roles));
        }

        return user;
    }
}