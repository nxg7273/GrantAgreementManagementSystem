package com.grantmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.model.Participant;
import com.grantmanagement.repository.ParticipantRepository;
import com.grantmanagement.service.impl.ParticipantServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantServiceImpl participantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ParticipantDTO createSampleParticipantDTO() {
        return new ParticipantDTO(1L, "John Doe", "john@example.com", "Test Organization");
    }

    private Participant createSampleParticipant() {
        Participant participant = new Participant();
        participant.setId(1L);
        participant.setName("John Doe");
        participant.setEmail("john@example.com");
        participant.setOrganization("Test Organization");
        return participant;
    }

    @Test
    void testCreateParticipant() {
        ParticipantDTO participantDTO = createSampleParticipantDTO();
        Participant participant = createSampleParticipant();

        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        ParticipantDTO result = participantService.createParticipant(participantDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(participantRepository).save(any(Participant.class));
    }

    @Test
    void testGetParticipantById() {
        Participant participant = createSampleParticipant();
        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        Optional<ParticipantDTO> result = participantService.getParticipantById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetAllParticipants() {
        when(participantRepository.findAll()).thenReturn(Arrays.asList(createSampleParticipant(), createSampleParticipant()));
        List<ParticipantDTO> result = participantService.getAllParticipants();
        assertEquals(2, result.size());
    }

    @Test
    void testUpdateParticipant() {
        Participant existingParticipant = createSampleParticipant();
        ParticipantDTO updatedParticipantDTO = new ParticipantDTO(1L, "New Name", "new@example.com", "New Organization");

        when(participantRepository.findById(1L)).thenReturn(Optional.of(existingParticipant));
        when(participantRepository.save(any(Participant.class))).thenReturn(existingParticipant);

        ParticipantDTO result = participantService.updateParticipant(1L, updatedParticipantDTO);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(participantRepository).save(any(Participant.class));
    }

    @Test
    void testDeleteParticipant() {
        Participant participant = createSampleParticipant();

        when(participantRepository.findById(1L)).thenReturn(Optional.of(participant));

        participantService.deleteParticipant(1L);

        verify(participantRepository).deleteById(1L);
    }

    @Test
    void testGetParticipantsByGrantId() {
        Long grantId = 1L;
        when(participantRepository.findByGrantId(grantId)).thenReturn(Arrays.asList(createSampleParticipant(), createSampleParticipant()));

        List<ParticipantDTO> result = participantService.getParticipantsByGrantId(grantId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(participantRepository).findByGrantId(grantId);
    }

    @Test
    void testGetTotalParticipants() {
        when(participantRepository.count()).thenReturn(5L);

        long result = participantService.getTotalParticipants();

        assertEquals(5L, result);
        verify(participantRepository).count();
    }
}
