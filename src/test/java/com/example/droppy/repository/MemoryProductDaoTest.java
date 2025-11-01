package com.example.droppy.repository;

import com.example.droppy.domain.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryProductDaoTest {

    private MemoryProductDao dao;

    @BeforeEach
    void setUp() {
        dao = new MemoryProductDao();
    }

    @Test
    void save_assignsIdAndReturnsSameInstance() {
        Product product = new Product();
        assertNull(product.getId());
        dao.save(product);
        assertNotNull(product.getId());
        assertSame(product, dao.findById(product.getId()));
    }


    @Test
    void save_withProvidedId_updatesSequenceForNextGeneratedId() {
        Product withId = new Product();
        withId.setId(100L);
        dao.save(withId);

        Product auto = new Product();
        dao.save(auto);

        assertNotNull(auto.getId());
        assertTrue(auto.getId() > 100L);
    }

    @Test
    void findById() {
        Product product = new Product();
        dao.save(product);
        Long id = product.getId();
        Product found = dao.findById(id);
        assertSame(product, found);
    }

    @Test
    void findAll_sortedByID() {
        Product p1 = new Product(); p1.setId(5L);
        Product p2 = new Product(); p2.setId(2L);
        Product p3 = new Product(); // will get auto id > existing

        // insert in arbitrary order
        dao.save(p1);
        dao.save(p2);
        dao.save(p3);

        var all = dao.findAll();
        var ids = all.stream().map(Product::getId).toList();

        assertEquals(3, all.size());
        assertTrue(ids.get(0) < ids.get(1));
        assertTrue(ids.get(1) < ids.get(2));
    }

//    @Test
//    void findByCompanyId() {
//        Product p1 = new Product();
//        p1.setCompanyId(1L);
//
//        Product p2 = new Product();
//        p2.setCompanyId(2L);
//
//        Product p3 = new Product();
//        p3.setCompanyId(30L);
//
//        dao.save(p1);
//        dao.save(p2);
//        dao.save(p3);
//
//        var company1Products = dao.findByCompanyId(1L);
//        assertEquals(1, company1Products.size());
//        assertTrue(company1Products.contains(p1));
//
//        var company2Products = dao.findByCompanyId(2L);
//        assertEquals(1, company2Products.size());
//        assertTrue(company2Products.contains(p2));
//
//        var company30Products = dao.findByCompanyId(30L);
//        assertEquals(1, company30Products.size());
//        assertTrue(company30Products.contains(p3));
//    }

    @Test
    void delete() {
        Product product = new Product();
        dao.save(product);
        Long id = product.getId();
        assertNotNull(dao.findById(id));

        dao.delete(id);
        assertNull(dao.findById(id));
    }

}