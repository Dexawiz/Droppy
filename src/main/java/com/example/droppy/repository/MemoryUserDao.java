package com.example.droppy.repository;

import com.example.droppy.domain.entity.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserDao implements UserDao {
    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        if (user.getId() == null) {
            long id = idSequence.incrementAndGet();
            user.setId(id);
            store.put(id, user);
        } else {
            // update existing or insert with provided id
            store.put(user.getId(), user);
            // ensure sequence won't produce duplicate smaller ids
            idSequence.updateAndGet(prev -> Math.max(prev, user.getId()));
        }
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>(store.values());
        list.sort(Comparator.comparing(User::getId, Comparator.nullsFirst(Comparator.naturalOrder())));
        return list;
    }

    @Override
    public User findById(Long id) {
        if (id == null) return null;
        return store.get(id);
    }

    @Override
    public void delete(Long id) {
        if (id == null) return;
        store.remove(id);
    }


    @Override
    public void create(String name, String surname, String email, String password) {
         for (User existingUser : store.values()) {
            if (existingUser.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("User with email " + email + " already exists.");
            }
         }
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        save(user);
    }

    @Override
    public User findByEmail(String email) {
        if (email == null || email.isEmpty()) return null;
        for (User user : store.values()) {
            if (email.equalsIgnoreCase(user.getEmail())) {
                return user;
            }
        }
        return null;
    }
}
