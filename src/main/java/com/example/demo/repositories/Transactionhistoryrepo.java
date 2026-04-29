package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TransactionHistory;

public interface Transactionhistoryrepo extends JpaRepository<TransactionHistory,Integer> {
	
		List<TransactionHistory> findByUserId(Integer userId);
		List<TransactionHistory> findByTransactionStatus(String status);
		List<TransactionHistory> findByTransactionType(String type);
	
}
