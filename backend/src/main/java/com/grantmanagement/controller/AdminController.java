package com.grantmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grantmanagement.service.AgreementService;
import com.grantmanagement.service.GrantService;
import com.grantmanagement.service.ParticipantService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private GrantService grantService;

    @Autowired
    private ParticipantService participantService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Get total number of agreements
        long totalAgreements = agreementService.getTotalAgreements();
        stats.put("totalAgreements", totalAgreements);

        // Get number of pending agreements
        long pendingAgreements = agreementService.getPendingAgreements();
        stats.put("pendingAgreements", pendingAgreements);

        // Get total number of grants
        long totalGrants = grantService.getTotalGrants();
        stats.put("totalGrants", totalGrants);

        // Get total number of participants
        long totalParticipants = participantService.getTotalParticipants();
        stats.put("totalParticipants", totalParticipants);

        return ResponseEntity.ok(stats);
    }
}
