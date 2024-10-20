package com.grantmanagement.dto;

public class ParticipantDTO {
    private Long id;
    private String name;
    private String email;
    private String organization;

    // Constructor
    public ParticipantDTO(Long id, String name, String email, String organization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.organization = organization;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
}
