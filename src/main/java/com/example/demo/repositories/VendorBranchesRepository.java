package com.example.demo.repositories;

import com.example.demo.model.VendorBranches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "vendor_branches")
public interface VendorBranchesRepository extends JpaRepository<VendorBranches, Integer> {

    // ✅ Get branches by vendor ID
    @RestResource(path = "by_vendor", rel = "by_vendor")
    List<VendorBranches> findByVendorVendorId(Integer vendorId);

    // ✅ Get branches by city (via address relationship)
    @RestResource(path = "by_city", rel = "by_city")
    List<VendorBranches> findByAddressCity(String city);

    // ✅ Get branches by state
    @RestResource(path = "by_state", rel = "by_state")
    List<VendorBranches> findByAddressState(String state);
}