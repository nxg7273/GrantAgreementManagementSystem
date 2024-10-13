package com.grantmanagement.controller;

import com.grantmanagement.model.Participant;
import com.grantmanagement.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        Participant createdParticipant = participantService.createParticipant(participant);
        return ResponseEntity.ok(createdParticipant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        return participantService.getParticipantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        List<Participant> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant participantDetails) {
        Participant updatedParticipant = participantService.updateParticipant(id, participantDetails);
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
    public ResponseEntity<List<Participant>> searchParticipants(@RequestParam String keyword) {
        List<Participant> participants = participantService.searchParticipants(keyword);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Participant> getParticipantByEmail(@PathVariable String email) {
        return participantService.getParticipantByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/organization/{organization}")
    public ResponseEntity<List<Participant>> getParticipantsByOrganization(@PathVariable String organization) {
        List<Participant> participants = participantService.getParticipantsByOrganization(organization);
        return ResponseEntity.ok(participants);
    }
}
