package com.example.droppy.service;

import com.example.droppy.domain.entity.User;
import com.example.droppy.repository.UserDao;

public class AuthService {
    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(String name, String surname, String email, String password, String confirmPassword, boolean isADMIN) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (surname == null || surname.isEmpty()) {
            throw new IllegalArgumentException("Surname cannot be null or empty");
        }
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if(!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if(userDao.findByEmail(email) != null) {
            throw new IllegalArgumentException("User with email " + email + " already exists.");
        }


        userDao.create(name, surname, email, password);
    }

    public void login(String email, String password) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with email " + email + " does not exist.");
        }
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password.");
        }

    }

}
