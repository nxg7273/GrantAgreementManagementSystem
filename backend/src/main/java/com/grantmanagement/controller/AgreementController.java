package com.grantmanagement.controller;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    private final AgreementService agreementService;

    @Autowired
    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        Agreement createdAgreement = agreementService.createAgreement(agreement);
        return ResponseEntity.ok(createdAgreement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agreement> getAgreementById(@PathVariable Long id) {
        return agreementService.getAgreementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Agreement>> getAllAgreements() {
        List<Agreement> agreements = agreementService.getAllAgreements();
        return ResponseEntity.ok(agreements);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable Long id, @RequestBody Agreement agreementDetails) {
        Agreement updatedAgreement = agreementService.updateAgreement(id, agreementDetails);
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
    public ResponseEntity<List<Agreement>> getAgreementsByParticipantId(@PathVariable Long participantId) {
        List<Agreement> agreements = agreementService.getAgreementsByParticipantId(participantId);
        return ResponseEntity.ok(agreements);
    }

    @GetMapping("/grant/{grantId}")
    public ResponseEntity<List<Agreement>> getAgreementsByGrantId(@PathVariable Long grantId) {
        List<Agreement> agreements = agreementService.getAgreementsByGrantId(grantId);
        return ResponseEntity.ok(agreements);
    }

    @PostMapping("/{id}/sign")
    public ResponseEntity<Agreement> signAgreement(@PathVariable Long id) {
        Agreement signedAgreement = agreementService.signAgreement(id);
        if (signedAgreement != null) {
            return ResponseEntity.ok(signedAgreement);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Agreement> rejectAgreement(@PathVariable Long id) {
        Agreement rejectedAgreement = agreementService.rejectAgreement(id);
        if (rejectedAgreement != null) {
            return ResponseEntity.ok(rejectedAgreement);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/regenerate")
    public ResponseEntity<Agreement> regenerateAgreement(@PathVariable Long id) {
        Agreement regeneratedAgreement = agreementService.regenerateAgreement(id);
        if (regeneratedAgreement != null) {
            return ResponseEntity.ok(regeneratedAgreement);
        }
        return ResponseEntity.notFound().build();
    }
}
