package com.example.demo.repositories;

import com.example.demo.model.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "vendors")
public interface VendorsRepository extends JpaRepository<Vendors, Integer> {

    // Optional custom finder
    Vendors findByVendorName(String vendorName);
}