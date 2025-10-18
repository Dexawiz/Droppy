package com.example.droppy.repository;

import com.example.droppy.domain.entity.User;
import java.util.List;

public interface UserDao {
    void save(User user);
    List<User> findAll();
    User findById(Long id);
    void delete(Long id);
    User create (String name, String surname, String email, String password);
    User findByEmail(String email);
}
