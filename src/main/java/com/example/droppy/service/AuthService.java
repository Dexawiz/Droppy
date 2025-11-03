package com.example.droppy.service;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import com.example.droppy.repository.HibernateUserDao;
import com.example.droppy.repository.UserDao;
import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.Getter;

public class AuthService {
    @Getter
    private  UserDao userDao;
    @Getter
    private User currentUser;


    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void register(String name, String surname, String email, String password, String confirmPassword, Role role) {

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

        // assign default role if none provided
        Role assignedRole = role == null ? Role.CUSTOMER : role;

        // hash password with bcrypt before storing
        String bcryptHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        userDao.create(name, surname, email, bcryptHash, assignedRole);

    }

    public User login(String email, String password) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with email " + email + " does not exist.");
        }
        String storedHash = user.getPassword();
        if (storedHash == null || storedHash.isEmpty()) {
            throw new IllegalArgumentException("Invalid password.");
        }

        boolean verified = false;

        // if stored value looks like bcrypt hash (starts with $2), verify with bcrypt
        if (storedHash.startsWith("$2")) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash);
            verified = result.verified;
        } else {
            // backward compatibility: stored plain password
            if (storedHash.equals(password)) {
                verified = true;
                // re-hash with bcrypt and persist
                String bcryptHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                user.setPassword(bcryptHash);
                try {
                    userDao.save(user);
                } catch (Exception e) {
                    // best-effort: log and continue (user still can login this session)
                    e.printStackTrace();
                }
            }
        }

        if (!verified) {
            throw new IllegalArgumentException("Invalid password.");
        }

        currentUser = user;
        return user;
    }

    public void logout() {
        currentUser = null;
    }
}
