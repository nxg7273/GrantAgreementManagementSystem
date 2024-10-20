package com.grantmanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.service.ParticipantService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ParticipantControllerTest {

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ParticipantDTO createSampleParticipantDTO() {
        return new ParticipantDTO(1L, "John Doe", "john@example.com", "Test Organization");
    }

    @Test
    void testCreateParticipant() {
        ParticipantDTO participantDTO = createSampleParticipantDTO();
        when(participantService.createParticipant(any(ParticipantDTO.class))).thenReturn(participantDTO);

        ResponseEntity<ParticipantDTO> response = participantController.createParticipant(participantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetParticipantById() {
        ParticipantDTO participantDTO = createSampleParticipantDTO();
        when(participantService.getParticipantById(1L)).thenReturn(Optional.of(participantDTO));

        ResponseEntity<ParticipantDTO> response = participantController.getParticipantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetAllParticipants() {
        List<ParticipantDTO> participants = Arrays.asList(createSampleParticipantDTO(), createSampleParticipantDTO());
        when(participantService.getAllParticipants()).thenReturn(participants);

        ResponseEntity<List<ParticipantDTO>> response = participantController.getAllParticipants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testUpdateParticipant() {
        ParticipantDTO participantDTO = createSampleParticipantDTO();
        when(participantService.updateParticipant(eq(1L), any(ParticipantDTO.class))).thenReturn(participantDTO);

        ResponseEntity<ParticipantDTO> response = participantController.updateParticipant(1L, participantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testDeleteParticipant() {
        doNothing().when(participantService).deleteParticipant(1L);

        ResponseEntity<Void> response = participantController.deleteParticipant(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(participantService).deleteParticipant(1L);
    }

    @Test
    void testGetParticipantsByGrantId() {
        List<ParticipantDTO> participants = Arrays.asList(createSampleParticipantDTO(), createSampleParticipantDTO());
        when(participantService.getParticipantsByGrantId(1L)).thenReturn(participants);

        ResponseEntity<List<ParticipantDTO>> response = participantController.getParticipantsByGrantId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetTotalParticipants() {
        when(participantService.getTotalParticipants()).thenReturn(5L);

        ResponseEntity<Long> response = participantController.getTotalParticipants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }
}
