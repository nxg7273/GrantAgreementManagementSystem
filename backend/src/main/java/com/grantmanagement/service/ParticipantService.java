package com.grantmanagement.service;

import com.grantmanagement.model.Participant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ParticipantService {
    Participant createParticipant(Participant participant);
    Optional<Participant> getParticipantById(Long id);
    List<Participant> getAllParticipants();
    Participant updateParticipant(Long id, Participant participantDetails);
    void deleteParticipant(Long id);
    List<Participant> searchParticipants(String keyword);
    Optional<Participant> getParticipantByEmail(String email);
    List<Participant> getParticipantsByOrganization(String organization);
}
