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

@ActiveProfiles("test")
class AgreementControllerTest {

    @Mock
    private AgreementService agreementService;

    @InjectMocks
    private AgreementController agreementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private AgreementDTO createSampleAgreementDTO() {
        GrantDTO grantDTO = new GrantDTO(1L, "Sample Grant", "Description", new BigDecimal("10000.00"), "Legal Text", true, 30, LocalDate.now(), LocalDate.now().plusMonths(6), GrantDTO.GrantStatus.ACTIVE);
        return new AgreementDTO(1L, grantDTO, new ParticipantDTO(1L, "John Doe", "john@example.com", "Sample Org"), Agreement.AgreementStatus.PENDING, LocalDate.now(), LocalDate.now().plusMonths(6), "Sample Content", null);
    }

    @Test
    void testCreateAgreement() {
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.createAgreement(any(AgreementDTO.class))).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.createAgreement(agreementDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetAgreementById() {
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.getAgreementById(1L)).thenReturn(Optional.of(agreementDTO));

        ResponseEntity<AgreementDTO> response = agreementController.getAgreementById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testRegenerateAgreement() {
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.regenerateAgreement(1L)).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.regenerateAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetPendingAgreements() {
        when(agreementService.getPendingAgreements()).thenReturn(2L);

        ResponseEntity<?> response = agreementController.getAllAgreements("pending");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, response.getBody());
    }

    @Test
    void testGetTotalAgreements() {
        when(agreementService.getTotalAgreements()).thenReturn(5L);

        ResponseEntity<Long> response = agreementController.getTotalAgreements();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    @Test
    void testGetAllAgreements() {
        List<AgreementDTO> agreements = Arrays.asList(createSampleAgreementDTO(), createSampleAgreementDTO());
        when(agreementService.getAllAgreements()).thenReturn(agreements);

        ResponseEntity<?> response = agreementController.getAllAgreements(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        assertEquals(2, ((List<?>) response.getBody()).size());
    }

    @Test
    void testUpdateAgreement() {
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.updateAgreement(eq(1L), any(AgreementDTO.class))).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.updateAgreement(1L, agreementDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testDeleteAgreement() {
        doNothing().when(agreementService).deleteAgreement(1L);

        ResponseEntity<Void> response = agreementController.deleteAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(agreementService).deleteAgreement(1L);
    }

    @Test
    void testSignAgreement() {
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.signAgreement(1L)).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.signAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testRejectAgreement() {
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        when(agreementService.rejectAgreement(1L)).thenReturn(agreementDTO);

        ResponseEntity<AgreementDTO> response = agreementController.rejectAgreement(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetAgreementsByParticipantId() {
        List<AgreementDTO> agreements = Arrays.asList(createSampleAgreementDTO(), createSampleAgreementDTO());
        when(agreementService.getAgreementsByParticipantId(1L)).thenReturn(agreements);

        ResponseEntity<List<AgreementDTO>> response = agreementController.getAgreementsByParticipantId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAgreementsByGrantId() {
        List<AgreementDTO> agreements = Arrays.asList(createSampleAgreementDTO(), createSampleAgreementDTO());
        when(agreementService.getAgreementsByGrantId(1L)).thenReturn(agreements);

        ResponseEntity<List<AgreementDTO>> response = agreementController.getAgreementsByGrantId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}
