package com.grantmanagement.service;

import com.grantmanagement.dto.ParticipantDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface ParticipantService {
    ParticipantDTO createParticipant(ParticipantDTO participantDTO);
    Optional<ParticipantDTO> getParticipantById(Long id);
    List<ParticipantDTO> getAllParticipants();
    ParticipantDTO updateParticipant(Long id, ParticipantDTO participantDetails);
    void deleteParticipant(Long id);
    List<ParticipantDTO> searchParticipants(String keyword);
    Optional<ParticipantDTO> getParticipantByEmail(String email);
    List<ParticipantDTO> getParticipantsByOrganization(String organization);
    long getTotalParticipants();
    List<ParticipantDTO> getParticipantsByGrantId(Long grantId);
    Map<String, Object> getParticipantDashboardData();
}
