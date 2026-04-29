package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import com.example.demo.model.PhysicalGoldTransactions;

@Repository
@RepositoryRestResource(path = "physical_gold_transactions")
public interface PhysicalGoldTransactionsRepo extends JpaRepository<PhysicalGoldTransactions, Integer> {
	@RestResource(path = "findByUserId", rel = "findByUserId")
    List<PhysicalGoldTransactions> findByUser_UserId(@Param("userId") Integer userId);

    // GET /api/v1/physical_gold_transactions/search/findByBranchId?branchId=3
    @RestResource(path = "findByBranchId", rel = "findByBranchId")
    List<PhysicalGoldTransactions> findByBranch_BranchId(@Param("branchId") Integer branchId);

}