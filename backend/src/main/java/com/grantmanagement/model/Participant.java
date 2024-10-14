package com.grantmanagement.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private boolean emailNotificationsEnabled = true;

    @Column(nullable = false)
    private boolean inAppNotificationsEnabled = true;

    @OneToMany(mappedBy = "participant")
    private Set<Agreement> agreements;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public boolean isEmailNotificationsEnabled() {
        return emailNotificationsEnabled;
    }

    public void setEmailNotificationsEnabled(boolean emailNotificationsEnabled) {
        this.emailNotificationsEnabled = emailNotificationsEnabled;
    }

    public boolean isInAppNotificationsEnabled() {
        return inAppNotificationsEnabled;
    }

    public void setInAppNotificationsEnabled(boolean inAppNotificationsEnabled) {
        this.inAppNotificationsEnabled = inAppNotificationsEnabled;
    }

    public Set<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(Set<Agreement> agreements) {
        this.agreements = agreements;
    }
}
