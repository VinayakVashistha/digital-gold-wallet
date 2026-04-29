package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import com.example.demo.model.PhysicalGoldTransactions;

@Repository
@RepositoryRestResource(path = "physical_gold_transactions")
public interface PhysicalGoldTransactionsRepo extends JpaRepository<PhysicalGoldTransactions, Integer> {


}