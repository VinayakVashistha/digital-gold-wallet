package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.demo.model.TransactionHistory;

@RepositoryRestResource(path = "transaction_history")
public interface TransactionHistoryRepo extends JpaRepository<TransactionHistory, Integer> {

    // GET /api/v1/transaction_history/search/findByUserId?userId=1
    @RestResource(path = "findByUserId", rel = "findByUserId")
    List<TransactionHistory> findByUserUserId(@Param("userId") Integer userId);

    // GET /api/v1/transaction_history/search/findByTransactionStatus?status=Success
    @RestResource(path = "findByTransactionStatus", rel = "findByTransactionStatus")
    List<TransactionHistory> findByTransactionStatus(@Param("status") String transactionStatus);

    // GET /api/v1/transaction_history/search/findByTransactionType?type=Buy
    @RestResource(path = "findByTransactionType", rel = "findByTransactionType")
    List<TransactionHistory> findByTransactionType(@Param("type") String transactionType);

}