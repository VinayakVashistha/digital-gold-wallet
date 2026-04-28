package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_branches")
public class VendorBranches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer branchId;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendors vendor;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Addresses address;

    private BigDecimal quantity;

    private LocalDateTime createdAt;

    public VendorBranches() {}

    // Getters & Setters
    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }

    public Vendors getVendor() { return vendor; }
    public void setVendor(Vendors vendor) { this.vendor = vendor; }

    public Addresses getAddress() { return address; }
    public void setAddress(Addresses address) { this.address = address; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}