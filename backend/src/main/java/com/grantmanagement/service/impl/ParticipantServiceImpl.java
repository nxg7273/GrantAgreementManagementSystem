package com.grantmanagement.service.impl;

import com.grantmanagement.model.Participant;
import com.grantmanagement.repository.ParticipantRepository;
import com.grantmanagement.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    public Optional<Participant> getParticipantById(Long id) {
        return participantRepository.findById(id);
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Participant updateParticipant(Long id, Participant participantDetails) {
        Optional<Participant> participant = participantRepository.findById(id);
        if (participant.isPresent()) {
            Participant existingParticipant = participant.get();
            existingParticipant.setName(participantDetails.getName());
            existingParticipant.setEmail(participantDetails.getEmail());
            existingParticipant.setOrganization(participantDetails.getOrganization());
            return participantRepository.save(existingParticipant);
        }
        return null;
    }

    @Override
    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    @Override
    public List<Participant> searchParticipants(String keyword) {
        return participantRepository.findByNameContainingOrEmailContainingOrOrganizationContaining(keyword, keyword, keyword);
    }

    @Override
    public Optional<Participant> getParticipantByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    @Override
    public List<Participant> getParticipantsByOrganization(String organization) {
        return participantRepository.findByOrganization(organization);
    }
}
