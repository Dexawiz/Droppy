package com.example.droppy.repository;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.entity.Order;
import com.example.droppy.domain.enums.Category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryCompanyDao implements CompanyDao{

    private final Map<Long, Company> store = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public void save(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("company must not be null");
        }
        if (company.getId() == null) {
            long id = idSequence.incrementAndGet();
            company.setId(id);
            store.put(id, company);
        } else {
            // update existing or insert with provided id
            store.put(company.getId(), company);
            // ensure sequence won't produce duplicate smaller ids
            idSequence.updateAndGet(prev -> Math.max(prev, company.getId()));
        }
    }

    @Override
    public List<Company> findAll() {
        List<Company> list = new ArrayList<>(store.values());
        list.sort(Comparator.comparing(Company::getId, Comparator.nullsFirst(Comparator.naturalOrder())));
        return list;
    }

    @Override
    public Company findById(Long id) {
        if (id == null) return null;
        return store.get(id);
    }

    @Override
    public void delete(Long id) {
        if (id == null) return;
        store.remove(id);
    }

    @Override
    public List<Company> findByCategory(Category category) {
        if (category == null) return List.of();
        List<Company> result = new ArrayList<>();
        for (Company company : store.values()) {
            if (category.equals(company.getCategory())) {
                result.add(company);
            }
        }
        result.sort(Comparator.comparing(Company::getId, Comparator.nullsFirst(Comparator.naturalOrder())));
        return result;
    }
}
