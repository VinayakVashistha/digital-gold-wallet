package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
public class Vendors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vendorId;

    private String vendorName;
    private String description;
    private String contactPersonName;
    private String contactEmail;
    private String contactPhone;
    private String websiteUrl;

    @Column(name = "total_gold_quantity")
    private BigDecimal totalGoldQuantity;

    @Column(name = "current_gold_price")
    private BigDecimal currentGoldPrice;

    private LocalDateTime createdAt;

    public Vendors() {}

    // Getters & Setters
    public Integer getVendorId() { return vendorId; }
    public void setVendorId(Integer vendorId) { this.vendorId = vendorId; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContactPersonName() { return contactPersonName; }
    public void setContactPersonName(String contactPersonName) { this.contactPersonName = contactPersonName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }

    public BigDecimal getTotalGoldQuantity() { return totalGoldQuantity; }
    public void setTotalGoldQuantity(BigDecimal totalGoldQuantity) { this.totalGoldQuantity = totalGoldQuantity; }

    public BigDecimal getCurrentGoldPrice() { return currentGoldPrice; }
    public void setCurrentGoldPrice(BigDecimal currentGoldPrice) { this.currentGoldPrice = currentGoldPrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}