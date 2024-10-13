package com.grantmanagement.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "grants")
public class Grant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String legalText;

    @Column(nullable = false)
    private boolean autoAcceptanceEnabled;

    @Column(nullable = false)
    private int autoAcceptanceDays;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLegalText() {
        return legalText;
    }

    public void setLegalText(String legalText) {
        this.legalText = legalText;
    }

    public boolean isAutoAcceptanceEnabled() {
        return autoAcceptanceEnabled;
    }

    public void setAutoAcceptanceEnabled(boolean autoAcceptanceEnabled) {
        this.autoAcceptanceEnabled = autoAcceptanceEnabled;
    }

    public int getAutoAcceptanceDays() {
        return autoAcceptanceDays;
    }

    public void setAutoAcceptanceDays(int autoAcceptanceDays) {
        this.autoAcceptanceDays = autoAcceptanceDays;
    }
}
