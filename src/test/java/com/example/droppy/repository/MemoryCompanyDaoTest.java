package com.example.droppy.repository;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.enums.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class MemoryCompanyDaoTest {

    private MemoryCompanyDao dao;

    @BeforeEach
    void setUp() {
        dao = new MemoryCompanyDao();
    }


    @Test
    void save_assignsIdAndReturnsSameInstance() {
        Company company = new Company();
        assertNull(company.getId());
        dao.save(company);
        assertNotNull(company.getId());
        assertSame(company, dao.findById(company.getId()));
    }


    @Test
    void save_withProvidedId_updatesSequenceForNextGeneratedId() {
        Company withId = new Company();
        withId.setId(100L);
        dao.save(withId);

        Company auto = new Company();
        dao.save(auto);

        assertNotNull(auto.getId());
        assertTrue(auto.getId() > 100L);
    }

    @Test
    void findAll_returnSortedByID() {
        Company c1 = new Company(); c1.setId(5L);
        Company c2 = new Company(); c2.setId(2L);
        Company c3 = new Company(); // will get auto id > existing

        // insert in arbitrary order
        dao.save(c1);
        dao.save(c2);
        dao.save(c3);

        var all = dao.findAll();
        var ids = all.stream().map(Company::getId).toList();

        var sorted = new ArrayList<>(ids);
        sorted.sort(Comparator.nullsFirst(Comparator.naturalOrder()));

        assertEquals(sorted, ids);
    }

    @Test
    void delete() {
        Company company = new Company();
        dao.save(company);
        Long id = company.getId();
        assertNotNull(dao.findById(id));

        dao.delete(id);
        assertNull(dao.findById(id));
    }

    @Test
    void findByCategory() {
        Company company1 = new Company();
        company1.setCategory(Category.PIZZA);
        dao.save(company1);

        Company company2 = new Company();
        company2.setCategory(Category.SUSHI);
        dao.save(company2);

        Company company3 = new Company();
        company3.setCategory(Category.PIZZA);
        dao.save(company3);

        var itCompanies = dao.findByCategory(Category.PIZZA);
        assertEquals(2, itCompanies.size());
        assertTrue(itCompanies.contains(company1));
        assertTrue(itCompanies.contains(company3));

        var financeCompanies = dao.findByCategory(Category.SUSHI);
        assertEquals(1, financeCompanies.size());
        assertTrue(financeCompanies.contains(company2));
    }
}