package com.grantmanagement.controller;

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
    public ResponseEntity<AgreementDTO> createAgreement(@RequestBody AgreementDTO agreementDTO) {
        try {
            logger.info("Received request to create agreement: {}", agreementDTO);
            AgreementDTO createdAgreement = agreementService.createAgreement(agreementDTO);
            logger.info("Agreement created successfully: {}", createdAgreement);
            return ResponseEntity.ok(createdAgreement);
        } catch (Exception e) {
            logger.error("Error creating agreement: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgreementDTO> getAgreementById(@PathVariable Long id) {
        return agreementService.getAgreementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> getAllAgreements(@RequestParam(required = false) String status) {
        if (status != null && status.equalsIgnoreCase("pending")) {
            long pendingCount = agreementService.getPendingAgreements();
            return ResponseEntity.ok(pendingCount);
        } else {
            List<AgreementDTO> agreements = agreementService.getAllAgreements();
            return ResponseEntity.ok(agreements);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgreementDTO> updateAgreement(@PathVariable Long id, @RequestBody AgreementDTO agreementDetails) {
        AgreementDTO updatedAgreement = agreementService.updateAgreement(id, agreementDetails);
        if (updatedAgreement != null) {
            return ResponseEntity.ok(updatedAgreement);
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
        List<AgreementDTO> agreements = agreementService.getAgreementsByParticipantId(participantId);
        return ResponseEntity.ok(agreements);
    }

    @GetMapping("/grant/{grantId}")
    public ResponseEntity<List<AgreementDTO>> getAgreementsByGrantId(@PathVariable Long grantId) {
        List<AgreementDTO> agreements = agreementService.getAgreementsByGrantId(grantId);
        return ResponseEntity.ok(agreements);
    }

    @PostMapping("/{id}/sign")
    public ResponseEntity<AgreementDTO> signAgreement(@PathVariable Long id) {
        AgreementDTO signedAgreement = agreementService.signAgreement(id);
        if (signedAgreement != null) {
            return ResponseEntity.ok(signedAgreement);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<AgreementDTO> rejectAgreement(@PathVariable Long id) {
        AgreementDTO rejectedAgreement = agreementService.rejectAgreement(id);
        if (rejectedAgreement != null) {
            return ResponseEntity.ok(rejectedAgreement);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/regenerate")
    public ResponseEntity<AgreementDTO> regenerateAgreement(@PathVariable Long id) {
        AgreementDTO regeneratedAgreement = agreementService.regenerateAgreement(id);
        if (regeneratedAgreement != null) {
            return ResponseEntity.ok(regeneratedAgreement);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalAgreements() {
        try {
            long totalAgreements = agreementService.getTotalAgreements();
            return ResponseEntity.ok(totalAgreements);
        } catch (Exception e) {
            logger.error("Error getting total agreements: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
