package com.example.demo.repositories;

import com.example.demo.model.VirtualGoldHoldings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "virtualGoldHoldings", path = "virtual-gold-holdings")
public interface VirtualGoldHoldingsRepository extends JpaRepository<VirtualGoldHoldings, Integer> {

    @RestResource(path = "by-branch", rel = "by-branch")
    List<VirtualGoldHoldings> findByBranch_BranchId(Integer branchId);

}