package com.example.demo.repositories;

import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Users;

@Repository
public interface UsersRepoGarima extends JpaRepository<Users, Integer> {
    List<Users> findByName(String name);
}