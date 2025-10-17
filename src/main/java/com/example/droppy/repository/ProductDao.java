package com.example.droppy.repository;

import com.example.droppy.domain.entity.Product;

import java.util.List;

public interface ProductDao {
    void save(Product product);
    Product findById(Long id);
    List<Product> findAll();
    List<Product> findByCompanyId(Long companyId);
    void delete(Long id);
}
