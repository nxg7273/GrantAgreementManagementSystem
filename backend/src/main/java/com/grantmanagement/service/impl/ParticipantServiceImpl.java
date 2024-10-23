package com.grantmanagement.service.impl;

import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.model.Participant;
import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.AgreementStatus;
import com.grantmanagement.repository.ParticipantRepository;
import com.grantmanagement.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private static final Logger log = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
        log.debug("ParticipantServiceImpl initialized with repository: {}", participantRepository);
    }

    @Override
    public ParticipantDTO createParticipant(ParticipantDTO participantDTO) {
        Participant participant = convertToEntity(participantDTO);
        Participant savedParticipant = participantRepository.save(participant);
        return convertToDTO(savedParticipant);
    }

    @Override
    public Optional<ParticipantDTO> getParticipantById(Long id) {
        return participantRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<ParticipantDTO> getAllParticipants() {
        return participantRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipantDTO updateParticipant(Long id, ParticipantDTO participantDetails) {
        Optional<Participant> participant = participantRepository.findById(id);
        if (participant.isPresent()) {
            Participant existingParticipant = participant.get();
            existingParticipant.setName(participantDetails.getName());
            existingParticipant.setEmail(participantDetails.getEmail());
            existingParticipant.setOrganization(participantDetails.getOrganization());
            Participant updatedParticipant = participantRepository.save(existingParticipant);
            return convertToDTO(updatedParticipant);
        }
        return null;
    }

    @Override
    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    @Override
    public List<ParticipantDTO> searchParticipants(String keyword) {
        return participantRepository.findByNameContainingOrEmailContainingOrOrganizationContaining(keyword, keyword, keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ParticipantDTO> getParticipantByEmail(String email) {
        return participantRepository.findByEmail(email).map(this::convertToDTO);
    }

    @Override
    public List<ParticipantDTO> getParticipantsByOrganization(String organization) {
        return participantRepository.findByOrganization(organization)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalParticipants() {
        return participantRepository.count();
    }

    @Override
    public List<ParticipantDTO> getParticipantsByGrantId(Long grantId) {
        return participantRepository.findByGrantId(grantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ParticipantDTO convertToDTO(Participant participant) {
        return new ParticipantDTO(
            participant.getId(),
            participant.getName(),
            participant.getEmail(),
            participant.getOrganization()
        );
    }

    private Participant convertToEntity(ParticipantDTO participantDTO) {
        Participant participant = new Participant();
        participant.setId(participantDTO.getId());
        participant.setName(participantDTO.getName());
        participant.setEmail(participantDTO.getEmail());
        participant.setOrganization(participantDTO.getOrganization());
        return participant;
    }

    @Override
    public Map<String, Object> getParticipantDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();

        Long currentParticipantId = getCurrentParticipantId();
        log.info("Current participant ID: {}", currentParticipantId);

        // Get total number of grants the participant is involved in
        long totalGrants = participantRepository.countDistinctGrantsByParticipantId(currentParticipantId);
        log.info("Total grants: {}", totalGrants);
        dashboardData.put("totalGrants", totalGrants);

        // Get total number of agreements for the participant
        long totalAgreements = participantRepository.countAgreementsByParticipantId(currentParticipantId);
        log.info("Total agreements: {}", totalAgreements);
        dashboardData.put("totalAgreements", totalAgreements);

        // Get number of pending agreements
        long pendingAgreements = participantRepository.countAgreementsByParticipantIdAndStatus(currentParticipantId, AgreementStatus.PENDING);
        log.info("Pending agreements: {}", pendingAgreements);
        dashboardData.put("pendingAgreements", pendingAgreements);

        // Get latest agreement details
        Optional<Agreement> latestAgreement = participantRepository.findLatestAgreementByParticipantId(currentParticipantId);
        latestAgreement.ifPresent(agreement -> {
            log.info("Latest agreement found. ID: {}, Status: {}, Date: {}", agreement.getId(), agreement.getStatus(), agreement.getCreatedAt());
            dashboardData.put("latestAgreementId", agreement.getId());
            dashboardData.put("latestAgreementStatus", agreement.getStatus());
            dashboardData.put("latestAgreementDate", agreement.getCreatedAt());
        });

        log.info("Dashboard data: {}", dashboardData);
        return dashboardData;
    }

    private Long getCurrentParticipantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Participant> participant = participantRepository.findByEmail(email);
            return participant.map(Participant::getId).orElse(null);
        }
        return null;
    }
}
