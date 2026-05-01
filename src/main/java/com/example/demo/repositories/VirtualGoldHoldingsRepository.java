package com.example.demo.repositories;

import com.example.demo.model.VirtualGoldHoldings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "virtualGoldHoldings", path = "virtual-gold-holdings")
public interface VirtualGoldHoldingsRepository extends JpaRepository<VirtualGoldHoldings, Integer> {

    // GET /virtual-gold-holdings/search/by-branch?branchId=1
    @RestResource(path = "by-branch", rel = "by-branch")
    List<VirtualGoldHoldings> findByBranch_BranchId(@Param("branchId") Integer branchId);

    // GET /virtual-gold-holdings/search/by-user?userId=1
    @RestResource(path = "by-user", rel = "by-user")
    List<VirtualGoldHoldings> findByUser_UserId(@Param("userId") Integer userId);
}