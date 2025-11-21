package com.example.droppy.repository;

import com.example.droppy.domain.entity.Address;
import com.example.droppy.domain.entity.Company;

import java.util.List;

public interface AddressDao {
    void save(Address address);
    List<Address> findAll();
    Address findById(Long id);
    List<Address> findByUserId(Long userId);
    void delete(Long id);
}