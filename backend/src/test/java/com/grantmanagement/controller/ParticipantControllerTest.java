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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActiveProfiles("test")
class ParticipantControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ParticipantControllerTest.class);

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    @BeforeEach
    void setUp() {
        logger.info("Setting up ParticipantControllerTest");
        MockitoAnnotations.openMocks(this);
        logger.debug("Mocks initialized: participantService={}, participantController={}", participantService, participantController);
    }

    private ParticipantDTO createSampleParticipantDTO() {
        logger.debug("Creating sample ParticipantDTO");
        ParticipantDTO participantDTO = new ParticipantDTO(1L, "John Doe", "john@example.com", "Test Organization");
        logger.debug("Created sample ParticipantDTO: {}", participantDTO);
        return participantDTO;
    }

    @Test
    void testCreateParticipant() {
        logger.info("Starting testCreateParticipant");
        ParticipantDTO participantDTO = createSampleParticipantDTO();
        when(participantService.createParticipant(any(ParticipantDTO.class))).thenReturn(participantDTO);

        ResponseEntity<ParticipantDTO> response = participantController.createParticipant(participantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testCreateParticipant successfully");
    }

    @Test
    void testGetParticipantById() {
        logger.info("Starting testGetParticipantById");
        ParticipantDTO participantDTO = createSampleParticipantDTO();
        when(participantService.getParticipantById(1L)).thenReturn(Optional.of(participantDTO));

        ResponseEntity<ParticipantDTO> response = participantController.getParticipantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testGetParticipantById successfully");
    }

    @Test
    void testGetAllParticipants() {
        logger.info("Starting testGetAllParticipants");
        List<ParticipantDTO> participants = Arrays.asList(createSampleParticipantDTO(), createSampleParticipantDTO());
        when(participantService.getAllParticipants()).thenReturn(participants);

        ResponseEntity<List<ParticipantDTO>> response = participantController.getAllParticipants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        logger.info("Completed testGetAllParticipants successfully");
    }

    @Test
    void testUpdateParticipant() {
        logger.info("Starting testUpdateParticipant");
        ParticipantDTO participantDTO = createSampleParticipantDTO();
        when(participantService.updateParticipant(eq(1L), any(ParticipantDTO.class))).thenReturn(participantDTO);

        ResponseEntity<ParticipantDTO> response = participantController.updateParticipant(1L, participantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testUpdateParticipant successfully");
    }

    @Test
    void testDeleteParticipant() {
        logger.info("Starting testDeleteParticipant");
        doNothing().when(participantService).deleteParticipant(1L);

        ResponseEntity<Void> response = participantController.deleteParticipant(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(participantService).deleteParticipant(1L);
        logger.info("Completed testDeleteParticipant successfully");
    }

    @Test
    void testGetParticipantsByGrantId() {
        logger.info("Starting testGetParticipantsByGrantId");
        List<ParticipantDTO> participants = Arrays.asList(createSampleParticipantDTO(), createSampleParticipantDTO());
        when(participantService.getParticipantsByGrantId(1L)).thenReturn(participants);

        ResponseEntity<List<ParticipantDTO>> response = participantController.getParticipantsByGrantId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        logger.info("Completed testGetParticipantsByGrantId successfully");
    }

    @Test
    void testGetTotalParticipants() {
        logger.info("Starting testGetTotalParticipants");
        when(participantService.getTotalParticipants()).thenReturn(5L);

        ResponseEntity<Long> response = participantController.getTotalParticipants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        logger.info("Completed testGetTotalParticipants successfully");
    }
}
