package com.example.droppy.repository;

import com.example.droppy.domain.entity.Product;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryProductDao implements ProductDao {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);


    @Override
    public void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product must not be null");
        }
        if (product.getId() == null) {
            long id = idSequence.incrementAndGet();
            product.setId(id);
            store.put(id, product);
        } else {
            // update existing or insert with provided id
            store.put(product.getId(), product);
            // ensure sequence won't produce duplicate smaller ids
            idSequence.updateAndGet(prev -> Math.max(prev, product.getId()));
        }
    }

    @Override
    public Product findById(Long id) {
        if (id == null) return null;
        return store.get(id);
    }

    @Override
    public List<Product> findAll() {
        var list = new ArrayList<>(store.values());
        list.sort(Comparator.comparing(Product::getId, Comparator.nullsFirst(Comparator.naturalOrder())));
        return list;
    }

    @Override
    public List<Product> findByCompanyId(Long companyId) {
        if (companyId == null) return List.of();
        var result = new ArrayList<Product>();
        for (Product product : store.values()) {
            if (companyId.equals(product.getCompanyId())) {
                result.add(product);
            }
        }
        result.sort(Comparator.comparing(Product::getId, Comparator.nullsFirst(Comparator.naturalOrder())));
        return result;
    }

    @Override
    public void delete(Long id) {
        if (id == null) return;
        store.remove(id);
    }
}
