package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.demo.model.TransactionHistory;

@RepositoryRestResource(path = "transaction_history")
public interface TransactionHistoryRepo extends JpaRepository<TransactionHistory, Integer> {

    @RestResource(path = "findByUserId", rel = "findByUserId")
    List<TransactionHistory> findByUserUserId(@Param("userId") Integer userId);

    @RestResource(path = "findByBranchId", rel = "findByBranchId")
    List<TransactionHistory> findByBranch_BranchId(@Param("branchId") Integer branchId);

    @RestResource(path = "findByTransactionStatus", rel = "findByTransactionStatus")
    List<TransactionHistory> findByTransactionStatus(@Param("status") String transactionStatus);

    @RestResource(path = "findByTransactionType", rel = "findByTransactionType")
    List<TransactionHistory> findByTransactionType(@Param("type") String transactionType);
}