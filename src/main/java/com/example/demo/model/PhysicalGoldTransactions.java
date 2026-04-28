package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "physical_gold_transactions")
public class PhysicalGoldTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private VendorBranches branch;

    private BigDecimal quantity;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id")
    private Addresses deliveryAddress;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public PhysicalGoldTransactions() {}

    // Getters & Setters
    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public VendorBranches getBranch() { return branch; }
    public void setBranch(VendorBranches branch) { this.branch = branch; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public Addresses getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(Addresses deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}