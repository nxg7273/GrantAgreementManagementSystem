package com.grantmanagement.service;

import com.grantmanagement.model.Participant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParticipantService {
    Participant createParticipant(Participant participant);
    Participant updateParticipant(Long id, Participant participant);
    Participant getParticipantById(Long id);
    Participant getParticipantByEmail(String email);
    List<Participant> getAllParticipants();
    void deleteParticipant(Long id);
    List<Participant> getParticipantsByGrantId(Long grantId);
}
