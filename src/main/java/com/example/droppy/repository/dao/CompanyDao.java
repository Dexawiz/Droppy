package com.example.droppy.repository.dao;

import com.example.droppy.domain.entity.Company;
import com.example.droppy.domain.enums.Category;

import java.util.List;

public interface CompanyDao {
    void save(Company company);
    List<Company> findAll();
    Company findById(Long id);
    void delete(Long id);
    List<Company> findByCategory(Category category);
    Company findByName(String name );
}
