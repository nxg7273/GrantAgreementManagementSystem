package com.grantmanagement.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "grants")
public class Grant {

    @OneToMany(mappedBy = "grant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Agreement> agreements;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Lob
    @Column(nullable = false)
    private String legalText;

    @Column(nullable = false)
    private boolean autoAcceptanceEnabled;

    @Column(nullable = false)
    private int autoAcceptanceDays;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GrantStatus status;

    public enum GrantStatus {
        ACTIVE, CLOSED, PENDING
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public GrantStatus getStatus() {
        return status;
    }

    public void setStatus(GrantStatus status) {
        this.status = status;
    }

    @Column
    private String grantNumber;

    public String getGrantNumber() {
        return grantNumber;
    }

    public void setGrantNumber(String grantNumber) {
        this.grantNumber = grantNumber;
    }
}
