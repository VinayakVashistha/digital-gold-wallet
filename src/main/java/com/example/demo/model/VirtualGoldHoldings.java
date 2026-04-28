package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "virtual_gold_holdings")
public class VirtualGoldHoldings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer holdingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private VendorBranches branch;

    private BigDecimal quantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public VirtualGoldHoldings() {}

    // Getters & Setters
    public Integer getHoldingId() { return holdingId; }
    public void setHoldingId(Integer holdingId) { this.holdingId = holdingId; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public VendorBranches getBranch() { return branch; }
    public void setBranch(VendorBranches branch) { this.branch = branch; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}