package com.example.droppy.repository;

import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MemoryOrderDao implements OrderDao {

    private final Map<Long, Order> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public void save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order must not be null");
        }
        if (order.getId() == null) {
            long id = idSequence.incrementAndGet();
            order.setId(id);
            store.put(id, order);
        } else {
            // update existing or insert with provided id
            store.put(order.getId(), order);
            // ensure sequence won't produce duplicate smaller ids
            idSequence.updateAndGet(prev -> Math.max(prev, order.getId()));
        }
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>(store.values());
        list.sort(Comparator.comparing(Order::getId, Comparator.nullsFirst(Comparator.naturalOrder())));
        return list;
    }

    @Override
    public Order findById(Long id) {
        if (id == null) return null;
        return store.get(id);
    }

    @Override
    public void delete(Long id) {
        if (id == null) return;
        store.remove(id);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        if (userId == null) return List.of();
        return store.values().stream()
                .filter(o -> o.getCustomerId() != null && o.getCustomerId().equals(userId))
                .sorted(Comparator.comparing(Order::getId, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(String status) {
        if (status == null) return List.of();
        return store.values().stream()
                .filter(o -> o.getStatus() != null && o.getStatus().name().equalsIgnoreCase(status))
                .sorted(Comparator.comparing(Order::getId, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}
