package com.grantmanagement.controller;

import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ResponseEntity<ParticipantDTO> createParticipant(@RequestBody ParticipantDTO participantDTO) {
        ParticipantDTO createdParticipant = participantService.createParticipant(participantDTO);
        return ResponseEntity.ok(createdParticipant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Long id) {
        return participantService.getParticipantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants() {
        List<ParticipantDTO> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipantDTO> updateParticipant(@PathVariable Long id, @RequestBody ParticipantDTO participantDetails) {
        ParticipantDTO updatedParticipant = participantService.updateParticipant(id, participantDetails);
        if (updatedParticipant != null) {
            return ResponseEntity.ok(updatedParticipant);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParticipantDTO>> searchParticipants(@RequestParam String keyword) {
        List<ParticipantDTO> participants = participantService.searchParticipants(keyword);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ParticipantDTO> getParticipantByEmail(@PathVariable String email) {
        return participantService.getParticipantByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{organization}")
    public ResponseEntity<List<ParticipantDTO>> getParticipantsByOrganization(@PathVariable String organization) {
        List<ParticipantDTO> participants = participantService.getParticipantsByOrganization(organization);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalParticipants() {
        long totalParticipants = participantService.getTotalParticipants();
        return ResponseEntity.ok(totalParticipants);
    }

    @GetMapping("/grant/{grantId}")
    public ResponseEntity<List<ParticipantDTO>> getParticipantsByGrantId(@PathVariable Long grantId) {
        List<ParticipantDTO> participants = participantService.getParticipantsByGrantId(grantId);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getParticipantDashboard() {
        Map<String, Object> dashboardData = participantService.getParticipantDashboardData();
        return ResponseEntity.ok(dashboardData);
    }
}
