package com.example.droppy.repository;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MemoryOrderDaoTest {

    private MemoryOrderDao dao;

    @BeforeEach
    void setUp() {
        dao = new MemoryOrderDao();
    }

    @Test
    void save_assignsIdAndReturnsSameInstance() {
        Order order = new Order();
        assertNull(order.getId());
        dao.save(order);
        assertNotNull(order.getId());
        assertSame(order, dao.findById(order.getId()));
    }

    @Test
    void save_withProvidedId_updatesSequenceForNextGeneratedId() {
        Order withId = new Order();
        withId.setId(100L);
        dao.save(withId);

        Order auto = new Order();
        dao.save(auto);

        assertNotNull(auto.getId());
        assertTrue(auto.getId() > 100L);
    }

    @Test
    void findAll_returnsSortedById() {
        Order o1 = new Order(); o1.setId(5L);
        Order o2 = new Order(); o2.setId(2L);
        Order o3 = new Order(); // will get auto id > existing

        // insert in arbitrary order
        dao.save(o1);
        dao.save(o2);
        dao.save(o3);

        var all = dao.findAll();
        var ids = all.stream().map(Order::getId).collect(Collectors.toList());

        var sorted = new ArrayList<>(ids);
        sorted.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        assertEquals(sorted, ids);
    }

    @Test
    void delete_removesOrder() {
        Order o = new Order();
        dao.save(o);
        Long id = o.getId();
        assertNotNull(dao.findById(id));
        dao.delete(id);
        assertNull(dao.findById(id));
    }

    @Test
    void findById_nullReturnsNull() {
        assertNull(dao.findById(null));
    }

//    @Test
//    void findByUserId_filtersByCustomerId() {
//        Order a = new Order(); a.setCustomerId()
//        Order b = new Order(); b.setCustomerId();
//        Order c = new Order(); c.setCustomerId();
//
//        dao.save(a);
//        dao.save(b);
//        dao.save(c);
//
//        var for1 = dao.findByUserId(1L);
//        assertEquals(2, for1.size());
//        assertTrue(for1.stream().allMatch(o -> o.getCustomerId().equals(1L)));
//    }

    @Test
    void findByStatus_filtersByStatusName() {
        Order a = new Order(); a.setStatus(OrderStatus.PENDING);
        Order b = new Order(); b.setStatus(OrderStatus.DELIVERED);
        Order c = new Order(); c.setStatus(OrderStatus.PENDING);

        dao.save(a);
        dao.save(b);
        dao.save(c);

        var pending = dao.findByStatus("PENDING");
        assertEquals(2, pending.size());
        assertTrue(pending.stream().allMatch(o -> o.getStatus() == OrderStatus.PENDING));

        // case-insensitive
        var pendingLower = dao.findByStatus("pending");
        assertEquals(2, pendingLower.size());
    }
}

