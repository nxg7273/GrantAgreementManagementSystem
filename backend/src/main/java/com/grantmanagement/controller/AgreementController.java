package com.grantmanagement.controller;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.dto.AgreementDTO;
import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    private static final Logger logger = LoggerFactory.getLogger(AgreementController.class);

    private final AgreementService agreementService;

    @Autowired
    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping
    public ResponseEntity<AgreementDTO> createAgreement(@RequestBody Agreement agreement) {
        try {
            logger.info("Received request to create agreement: {}", agreement);
            Agreement createdAgreement = agreementService.createAgreement(agreement);
            logger.info("Agreement created successfully: {}", createdAgreement);
            return ResponseEntity.ok(convertToDTO(createdAgreement));
        } catch (Exception e) {
            logger.error("Error creating agreement: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgreementDTO> getAgreementById(@PathVariable Long id) {
        return agreementService.getAgreementById(id)
                .map(agreement -> ResponseEntity.ok(convertToDTO(agreement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> getAllAgreements(@RequestParam(required = false) String status) {
        if (status != null && status.equalsIgnoreCase("pending")) {
            long pendingCount = agreementService.getPendingAgreements();
            return ResponseEntity.ok(pendingCount);
        } else {
            List<Agreement> agreements = agreementService.getAllAgreements();
            List<AgreementDTO> agreementDTOs = agreements.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(agreementDTOs);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgreementDTO> updateAgreement(@PathVariable Long id, @RequestBody Agreement agreementDetails) {
        Agreement updatedAgreement = agreementService.updateAgreement(id, agreementDetails);
        if (updatedAgreement != null) {
            return ResponseEntity.ok(convertToDTO(updatedAgreement));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgreement(@PathVariable Long id) {
        agreementService.deleteAgreement(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<AgreementDTO>> getAgreementsByParticipantId(@PathVariable Long participantId) {
        List<AgreementDTO> agreements = agreementService.getAgreementsByParticipantId(participantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agreements);
    }

    @GetMapping("/grant/{grantId}")
    public ResponseEntity<List<AgreementDTO>> getAgreementsByGrantId(@PathVariable Long grantId) {
        List<AgreementDTO> agreements = agreementService.getAgreementsByGrantId(grantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agreements);
    }

    @PostMapping("/{id}/sign")
    public ResponseEntity<AgreementDTO> signAgreement(@PathVariable Long id) {
        Agreement signedAgreement = agreementService.signAgreement(id);
        if (signedAgreement != null) {
            return ResponseEntity.ok(convertToDTO(signedAgreement));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<AgreementDTO> rejectAgreement(@PathVariable Long id) {
        Agreement rejectedAgreement = agreementService.rejectAgreement(id);
        if (rejectedAgreement != null) {
            return ResponseEntity.ok(convertToDTO(rejectedAgreement));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/regenerate")
    public ResponseEntity<AgreementDTO> regenerateAgreement(@PathVariable Long id) {
        Agreement regeneratedAgreement = agreementService.regenerateAgreement(id);
        if (regeneratedAgreement != null) {
            return ResponseEntity.ok(convertToDTO(regeneratedAgreement));
        }
        return ResponseEntity.notFound().build();
    }

    private AgreementDTO convertToDTO(Agreement agreement) {
        GrantDTO grantDTO = new GrantDTO(
            agreement.getGrant().getId(),
            agreement.getGrant().getName(),
            agreement.getGrant().getDescription(),
            agreement.getGrant().getAmount(),
            agreement.getGrant().getLegalText(),
            agreement.getGrant().isAutoAcceptanceEnabled(),
            agreement.getGrant().getAutoAcceptanceDays()
        );

        ParticipantDTO participantDTO = new ParticipantDTO(
            agreement.getParticipant().getId(),
            agreement.getParticipant().getName(),
            agreement.getParticipant().getEmail(),
            agreement.getParticipant().getOrganization(),
            agreement.getParticipant().isEmailNotificationsEnabled(),
            agreement.getParticipant().isInAppNotificationsEnabled()
        );

        return new AgreementDTO(
            agreement.getId(),
            grantDTO,
            participantDTO,
            agreement.getStatus(),
            agreement.getCreatedAt(),
            agreement.getUpdatedAt(),
            agreement.getDocumentPath(),
            agreement.getAcceptedAt()
        );
    }
}
