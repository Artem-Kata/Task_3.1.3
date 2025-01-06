package ru.kata.spring.boot_security.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    @Lazy
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public User getOne(Long id) {
        return userDao.findById(id).get();
    }

    @Override
    @Transactional
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
    @Transactional
    public User oneUser(Principal principal) {
        return (User) ((Authentication) principal).getPrincipal();
    }

    @Override
    @Transactional
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
    @Transactional
    public User updateUser(User user, Set<Role> roles, Long id) {
        User oldUser = getOne(id);

        String newPassword = user.getPassword();
        if (newPassword != null && !newPassword.isEmpty() && !passwordEncoder.matches(newPassword, oldUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            user.setPassword(oldUser.getPassword());
        }

        if (roles == null || roles.isEmpty()) {
            user.setRoles(oldUser.getRoles());
        } else {
            user.setRoles(new HashSet<>(roles));
        }

        return user;
    }
}