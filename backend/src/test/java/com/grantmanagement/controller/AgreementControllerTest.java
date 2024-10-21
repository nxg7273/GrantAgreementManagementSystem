package com.grantmanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.grantmanagement.controller.AgreementController;
import com.grantmanagement.dto.AgreementDTO;
import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.model.Agreement;
import com.grantmanagement.service.AgreementService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActiveProfiles("test")
class AgreementControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(AgreementControllerTest.class);

    @Mock
    private AgreementService agreementService;

    @InjectMocks
    private AgreementController agreementController;

    @BeforeEach
    void setUp() {
        logger.info("Setting up AgreementControllerTest");
        MockitoAnnotations.openMocks(this);
        logger.debug("Mocks initialized: agreementService={}, agreementController={}", agreementService, agreementController);
    }

    private AgreementDTO createSampleAgreementDTO() {
        logger.debug("Creating sample AgreementDTO");
        GrantDTO grantDTO = new GrantDTO(1L, "Sample Grant", "Description", new BigDecimal("10000.00"), "Legal Text", true, 30, LocalDate.now(), LocalDate.now().plusMonths(6), GrantDTO.GrantStatus.ACTIVE);
        AgreementDTO agreementDTO = new AgreementDTO(1L, grantDTO, new ParticipantDTO(1L, "John Doe", "john@example.com", "Sample Org"), Agreement.AgreementStatus.PENDING, LocalDate.now(), LocalDate.now().plusMonths(6), "Sample Content", null);
        logger.debug("Created sample AgreementDTO: {}", agreementDTO);
        return agreementDTO;
    }

    @Test
    void testCreateAgreement() {
        logger.info("Starting testCreateAgreement");
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.createAgreement(any(AgreementDTO.class))).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.createAgreement(agreementDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testCreateAgreement successfully");
    }

    @Test
    void testGetAgreementById() {
        logger.info("Starting testGetAgreementById");
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.getAgreementById(1L)).thenReturn(Optional.of(agreementDTO));

        ResponseEntity<AgreementDTO> response = agreementController.getAgreementById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testGetAgreementById successfully");
    }

    @Test
    void testRegenerateAgreement() {
        logger.info("Starting testRegenerateAgreement");
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.regenerateAgreement(1L)).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.regenerateAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testRegenerateAgreement successfully");
    }

    @Test
    void testGetPendingAgreements() {
        logger.info("Starting testGetPendingAgreements");
        when(agreementService.getPendingAgreements()).thenReturn(2L);

        ResponseEntity<?> response = agreementController.getAllAgreements("pending");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, response.getBody());
        logger.info("Completed testGetPendingAgreements successfully");
    }

    @Test
    void testGetTotalAgreements() {
        logger.info("Starting testGetTotalAgreements");
        when(agreementService.getTotalAgreements()).thenReturn(5L);

        ResponseEntity<Long> response = agreementController.getTotalAgreements();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        logger.info("Completed testGetTotalAgreements successfully");
    }

    @Test
    void testGetAllAgreements() {
        logger.info("Starting testGetAllAgreements");
        List<AgreementDTO> agreements = Arrays.asList(createSampleAgreementDTO(), createSampleAgreementDTO());
        when(agreementService.getAllAgreements()).thenReturn(agreements);

        ResponseEntity<?> response = agreementController.getAllAgreements(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals(2, ((List<?>) response.getBody()).size());
        logger.info("Completed testGetAllAgreements successfully");
    }

    @Test
    void testUpdateAgreement() {
        logger.info("Starting testUpdateAgreement");
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.updateAgreement(eq(1L), any(AgreementDTO.class))).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.updateAgreement(1L, agreementDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testUpdateAgreement successfully");
    }

    @Test
    void testDeleteAgreement() {
        logger.info("Starting testDeleteAgreement");
        doNothing().when(agreementService).deleteAgreement(1L);

        ResponseEntity<Void> response = agreementController.deleteAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(agreementService).deleteAgreement(1L);
        logger.info("Completed testDeleteAgreement successfully");
    }

    @Test
    void testSignAgreement() {
        logger.info("Starting testSignAgreement");
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.signAgreement(1L)).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.signAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testSignAgreement successfully");
    }

    @Test
    void testRejectAgreement() {
        logger.info("Starting testRejectAgreement");
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.rejectAgreement(1L)).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.rejectAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testRejectAgreement successfully");
    }

    @Test
    void testGetAgreementsByParticipantId() {
        logger.info("Starting testGetAgreementsByParticipantId");
        List<AgreementDTO> agreements = Arrays.asList(createSampleAgreementDTO(), createSampleAgreementDTO());
        when(agreementService.getAgreementsByParticipantId(1L)).thenReturn(agreements);

        ResponseEntity<List<AgreementDTO>> response = agreementController.getAgreementsByParticipantId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        logger.info("Completed testGetAgreementsByParticipantId successfully");
    }

    @Test
    void testGetAgreementsByGrantId() {
        logger.info("Starting testGetAgreementsByGrantId");
        List<AgreementDTO> agreements = Arrays.asList(createSampleAgreementDTO(), createSampleAgreementDTO());
        when(agreementService.getAgreementsByGrantId(1L)).thenReturn(agreements);

        ResponseEntity<List<AgreementDTO>> response = agreementController.getAgreementsByGrantId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        logger.info("Completed testGetAgreementsByGrantId successfully");
    }
}
