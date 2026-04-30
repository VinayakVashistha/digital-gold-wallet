
package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String email;
    private String name;

    private BigDecimal balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Addresses address;

    public Users() {}

    // Getters & Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Addresses getAddress() { return address; }
    public void setAddress(Addresses address) { this.address = address; }
}