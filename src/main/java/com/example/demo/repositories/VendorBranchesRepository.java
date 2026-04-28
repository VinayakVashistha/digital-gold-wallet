package com.example.demo.repositories;

import com.example.demo.model.VendorBranches;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorBranchesRepository extends JpaRepository<VendorBranches, Integer> {

    Page<VendorBranches> findAll(Pageable pageable);

    Optional<VendorBranches> findById(Integer branchId);

    List<VendorBranches> findByVendorVendorId(Integer vendorId);

    List<VendorBranches> findByAddressCity(String city);

    List<VendorBranches> findByAddressState(String state);
}