package com.example.droppy.repository;

import com.example.droppy.domain.entity.User;
import com.example.droppy.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUserDaoTest {

    private MemoryUserDao dao;

    @BeforeEach
    void setUp() {
        dao = new MemoryUserDao();
    }

    @Test
    void save_assignsIdAndReturnsSameInstance() {
        User user = new User();
        assertNull(user.getId());
        dao.save(user);
        assertNotNull(user.getId());
        assertSame(user, dao.findById(user.getId()));
    }

    @Test
    void save_withProvidedId_updatesSequenceForNextGeneratedId() {
        User withId = new User();
        withId.setId(100L);
        dao.save(withId);

        User auto = new User();
        dao.save(auto);

        assertNotNull(auto.getId());
        assertTrue(auto.getId() > 100L);
    }

    @Test
    void findAll_returnsSortedById() {
        User u1 = new User(); u1.setId(5L);
        User u2 = new User(); u2.setId(2L);
        User u3 = new User(); // will get auto id > existing

        // insert in arbitrary order
        dao.save(u1);
        dao.save(u2);
        dao.save(u3);

        List<User> all = dao.findAll();
        List<Long> ids = all.stream().map(User::getId).collect(Collectors.toList());

        // verify ascending order (nulls first if any)
        List<Long> sorted = new ArrayList<>(ids);
        sorted.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertEquals(sorted, ids);
    }

    @Test
    void delete_removesUser() {
        User u = new User();
        dao.save(u);
        Long id = u.getId();
        assertNotNull(dao.findById(id));
        dao.delete(id);
        assertNull(dao.findById(id));
    }

    @Test
    void findById_nullReturnsNull() {
        assertNull(dao.findById(null));
    }

    @Test
    void findByEmail_existingAndNonExisting() {
        User u = new User();
        u.setEmail("123@123");
        dao.save(u);
        assertSame(u, dao.findByEmail("123@123"));
        assertNull(dao.findByEmail("nonexistent@123"));
    }

    @Test
    void create_preventsDuplicateEmails() {
        // use a concrete role for test
        Role role = Role.CUSTOMER;
        dao.create("Name1", "Surname1", "dup@dup", "pass1", role);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            dao.create("Name2", "Surname2", "dup@dup", "pass2", role);
        });
        String expectedMessage = "User with email dup@dup already exists.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
