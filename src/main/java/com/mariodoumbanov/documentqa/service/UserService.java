package com.mariodoumbanov.documentqa.service;

import com.mariodoumbanov.documentqa.DTO.UserLoginDTO;
import com.mariodoumbanov.documentqa.DTO.UserRegisterDTO;
import com.mariodoumbanov.documentqa.entity.Authority;
import com.mariodoumbanov.documentqa.entity.User;
import com.mariodoumbanov.documentqa.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, AuthorityService authorityService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(@Valid UserRegisterDTO userRegisterDTO) throws IllegalArgumentException {
        return this.register(userRegisterDTO.email(), userRegisterDTO.password());
    }
    @Transactional
    public User register(String email, String password){
        if (userRepository.existsUserByEmail(email)) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(email, hashedPassword);

        Authority defaultAuthority = authorityService.findAuthorityByName(User.DEFAULT_ROLE_NAME);
        user.setAuthorities(new HashSet<>(List.of(defaultAuthority)));

        user = userRepository.save(user);

        return user;
    }
    @Transactional
    public User login(@Valid UserLoginDTO userLoginDTO) throws IllegalArgumentException{
        return this.login(userLoginDTO.email(), userLoginDTO.password());
    }
    @Transactional
    public User login(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with this email does not exist.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        return user;
    }

    @Transactional
    public User updateEmail(@NotNull User user, String email) {
        if(email.equals(user.getEmail())) {
            throw new IllegalArgumentException("This email is already in for this account.");
        }
        if(userRepository.existsUserByEmail(email)) {
            throw new IllegalArgumentException("Email already in use.");
        }

        user.setEmail(email);

        return userRepository.save(user);
    }

    @Transactional
    public User updatePassword(@NotNull User user, String password, String confirmPassword) {
        if(passwordEncoder.matches(confirmPassword, user.getPassword())) {
            throw new IllegalArgumentException("Confirmation password was incorrect.");
        }
        if(password.equals(confirmPassword)) {
            throw new IllegalArgumentException("This password has already been set for this user.");
        }

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional
    public List<User> queryUsers(int limit, int offset) {
        if(limit < 0 || limit > 100) {
            limit = 10;
        }
        if(offset < 0 || offset > 100) {
            offset = 10;
        }

        PageRequest page = PageRequest.of(offset, limit);
        var pageUsers = userRepository.findAll(page);

        return pageUsers.toList();
    }
    @Transactional
    public List<User> queryUsers(String limitStr, String offsetStr) throws NumberFormatException{
        int limit = Integer.parseInt(limitStr), offset = Integer.parseInt(offsetStr);

        return queryUsers(limit, offset);
    }

    @Transactional
    public Page<User> pageUsers(int limit, int offset) {
        if(limit < 0 || limit > 100) {
            limit = 10;
        }
        if(offset < 0 || offset > 100) {
            offset = 10;
        }

        PageRequest page = PageRequest.of(offset, limit);
        return userRepository.findAll(page);
    }

    @Transactional
    public void deleteUser(int id) {
        //! Check if the user exists before deleting. 😡
        if(!userRepository.existsUserById(id)) {
            throw new IllegalArgumentException("User with this id does not exist.");
        }
        userRepository.deleteById(id);
    }
}
