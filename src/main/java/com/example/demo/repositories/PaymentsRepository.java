package com.example.demo.repositories;

import com.example.demo.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "payments")
public interface PaymentsRepository extends JpaRepository<Payments, Integer> {

    @RestResource(path = "findByUserid", rel = "findByUserid")
    List<Payments> findByUser_UserId(Integer userId);

}