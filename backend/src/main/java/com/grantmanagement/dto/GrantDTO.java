package com.grantmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GrantDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private String legalText;
    private boolean autoAcceptanceEnabled;
    private int autoAcceptanceDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private GrantStatus status;

    public enum GrantStatus {
        ACTIVE, CLOSED, PENDING
    }

    // Constructor
    public GrantDTO(Long id, String title, String description, BigDecimal amount, String legalText,
                    boolean autoAcceptanceEnabled, int autoAcceptanceDays, LocalDate startDate, LocalDate endDate, GrantStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.legalText = legalText;
        this.autoAcceptanceEnabled = autoAcceptanceEnabled;
        this.autoAcceptanceDays = autoAcceptanceDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getLegalText() { return legalText; }
    public void setLegalText(String legalText) { this.legalText = legalText; }
    public boolean isAutoAcceptanceEnabled() { return autoAcceptanceEnabled; }
    public void setAutoAcceptanceEnabled(boolean autoAcceptanceEnabled) { this.autoAcceptanceEnabled = autoAcceptanceEnabled; }
    public int getAutoAcceptanceDays() { return autoAcceptanceDays; }
    public void setAutoAcceptanceDays(int autoAcceptanceDays) { this.autoAcceptanceDays = autoAcceptanceDays; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public GrantStatus getStatus() { return status; }
    public void setStatus(GrantStatus status) { this.status = status; }
}
