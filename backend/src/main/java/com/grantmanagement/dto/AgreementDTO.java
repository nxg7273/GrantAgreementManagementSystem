package com.grantmanagement.dto;

import com.grantmanagement.model.Agreement.AgreementStatus;
import java.time.LocalDate;

public class AgreementDTO {
    private Long id;
    private GrantDTO grant;
    private ParticipantDTO participant;
    private AgreementStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String documentPath;
    private LocalDate acceptedAt;

    public AgreementDTO(Long id, GrantDTO grant, ParticipantDTO participant, AgreementStatus status,
                        LocalDate createdAt, LocalDate updatedAt, String documentPath,
                        LocalDate acceptedAt) {
        this.id = id;
        this.grant = grant;
        this.participant = participant;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.documentPath = documentPath;
        this.acceptedAt = acceptedAt;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GrantDTO getGrant() {
        return grant;
    }

    public void setGrant(GrantDTO grant) {
        this.grant = grant;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantDTO participant) {
        this.participant = participant;
    }

    public AgreementStatus getStatus() {
        return status;
    }

    public void setStatus(AgreementStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public LocalDate getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDate acceptedAt) {
        this.acceptedAt = acceptedAt;
    }
}
