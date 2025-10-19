package com.example.droppy.repository;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;

import java.util.List;

public interface UserDao {
    void save(User user);
    List<User> findAll();
    User findById(Long id);
    void delete(Long id);
    void create (String name, String surname, String email, String password, Role role);
    User findByEmail(String email);
}
