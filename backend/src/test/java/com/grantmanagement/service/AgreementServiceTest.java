package com.grantmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.grantmanagement.dto.AgreementDTO;
import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Grant;
import com.grantmanagement.model.Participant;
import com.grantmanagement.repository.AgreementRepository;
import com.grantmanagement.service.NotificationService;
import com.grantmanagement.service.DocumentGenerationService;
import com.grantmanagement.service.impl.AgreementServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgreementServiceTest {

    @Mock
    private AgreementRepository agreementRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private DocumentGenerationService documentGenerationService;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private AgreementServiceImpl agreementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agreementService = new AgreementServiceImpl(agreementRepository, notificationService, documentGenerationService, messagingTemplate);
    }

    private AgreementDTO createSampleAgreementDTO() {
        GrantDTO grantDTO = new GrantDTO(1L, "Sample Grant", "Description", new BigDecimal("10000.00"), "123456", true, 30, LocalDate.now(), LocalDate.now().plusMonths(6), GrantDTO.GrantStatus.ACTIVE);
        ParticipantDTO participantDTO = new ParticipantDTO(1L, "John Doe", "john@example.com", "123-456-7890");
        return new AgreementDTO(1L, grantDTO, participantDTO, Agreement.AgreementStatus.PENDING, LocalDate.now(), LocalDate.now().plusDays(30), "path/to/document.pdf", null);
    }

    private Agreement createSampleAgreement() {
        Grant grant = new Grant();
        grant.setId(1L);
        grant.setTitle("Sample Grant");
        grant.setDescription("Description");
        grant.setAmount(new BigDecimal("10000.00"));
        grant.setGrantNumber("123456");
        grant.setAutoAcceptanceEnabled(true);
        grant.setAutoAcceptanceDays(30);
        grant.setStartDate(LocalDate.now());
        grant.setEndDate(LocalDate.now().plusMonths(6));
        grant.setStatus(Grant.GrantStatus.ACTIVE);

        Participant participant = new Participant();
        participant.setId(1L);
        participant.setName("John Doe");
        participant.setEmail("john@example.com");
        participant.setPhoneNumber("123-456-7890");

        Agreement agreement = new Agreement();
        agreement.setId(1L);
        agreement.setGrant(grant);
        agreement.setParticipant(participant);
        agreement.setStatus(Agreement.AgreementStatus.PENDING);
        agreement.setCreatedAt(LocalDate.now());
        agreement.setExpiresAt(LocalDate.now().plusDays(30));
        agreement.setDocumentPath("path/to/document.pdf");

        return agreement;
    }

    @Test
    void testCreateAgreement() {
        AgreementDTO agreementDTO = createSampleAgreementDTO();
        Agreement agreement = createSampleAgreement();
        when(agreementRepository.save(any(Agreement.class))).thenReturn(agreement);

        AgreementDTO result = agreementService.createAgreement(agreementDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Agreement.AgreementStatus.PENDING, result.getStatus());
        verify(agreementRepository).save(any(Agreement.class));
        verify(notificationService).sendDocumentSigningNotification(any());
        verify(messagingTemplate).convertAndSend(eq("/topic/agreements"), any(AgreementDTO.class));
    }

    @Test
    void testGetAgreementById() {
        Agreement agreement = createSampleAgreement();
        when(agreementRepository.findById(1L)).thenReturn(Optional.of(agreement));

        Optional<AgreementDTO> result = agreementService.getAgreementById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(Agreement.AgreementStatus.PENDING, result.get().getStatus());
        verify(agreementRepository).findById(1L);
    }

    @Test
    void testGetAllAgreements() {
        Agreement agreement1 = createSampleAgreement();
        Agreement agreement2 = createSampleAgreement();
        agreement2.setId(2L);
        agreement2.setStatus(Agreement.AgreementStatus.ACCEPTED);
        when(agreementRepository.findAll()).thenReturn(Arrays.asList(agreement1, agreement2));

        List<AgreementDTO> result = agreementService.getAllAgreements();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(Agreement.AgreementStatus.PENDING, result.get(0).getStatus());
        assertEquals(2L, result.get(1).getId());
        assertEquals(Agreement.AgreementStatus.ACCEPTED, result.get(1).getStatus());
    }

    // Add more test methods for other AgreementService functionalities
    // Such as updateAgreement, deleteAgreement, signAgreement, rejectAgreement, etc.

    // Example of additional test method:
    @Test
    void testSignAgreement() {
        Agreement agreement = createSampleAgreement();
        when(agreementRepository.findById(1L)).thenReturn(Optional.of(agreement));
        when(agreementRepository.save(any(Agreement.class))).thenReturn(agreement);

        AgreementDTO result = agreementService.signAgreement(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Agreement.AgreementStatus.ACCEPTED, result.getStatus());
        verify(notificationService).sendAgreementSignedNotification(any(), any());
        verify(messagingTemplate).convertAndSend(eq("/topic/agreements"), any(AgreementDTO.class));
    }

    @Test
    void testUpdateAgreement() {
        Agreement existingAgreement = createSampleAgreement();
        when(agreementRepository.findById(1L)).thenReturn(Optional.of(existingAgreement));
        when(agreementRepository.save(any(Agreement.class))).thenReturn(existingAgreement);

        AgreementDTO updatedAgreementDTO = createSampleAgreementDTO();
        updatedAgreementDTO.setStatus(Agreement.AgreementStatus.ACCEPTED);

        AgreementDTO result = agreementService.updateAgreement(1L, updatedAgreementDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Agreement.AgreementStatus.ACCEPTED, result.getStatus());
        verify(agreementRepository).findById(1L);
        verify(agreementRepository, times(1)).save(any(Agreement.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/agreements"), any(AgreementDTO.class));
    }

    @Test
    void testDeleteAgreement() {
        Agreement agreement = createSampleAgreement();
        when(agreementRepository.findById(1L)).thenReturn(Optional.of(agreement));
        doNothing().when(agreementRepository).deleteById(1L);

        agreementService.deleteAgreement(1L);

        verify(agreementRepository).findById(1L);
        verify(agreementRepository).deleteById(1L);
        verify(messagingTemplate).convertAndSend(eq("/topic/agreements"), any(AgreementDTO.class));
    }

    @Test
    void testRejectAgreement() {
        Agreement agreement = createSampleAgreement();
        when(agreementRepository.findById(1L)).thenReturn(Optional.of(agreement));
        when(agreementRepository.save(any(Agreement.class))).thenReturn(agreement);

        AgreementDTO result = agreementService.rejectAgreement(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Agreement.AgreementStatus.REJECTED, result.getStatus());
        verify(notificationService).sendAgreementRejectedNotification(any(), any());
        verify(messagingTemplate).convertAndSend(eq("/topic/agreements"), any(AgreementDTO.class));
    }

    @Test
    void testGetAgreementsByParticipantId() {
        Long participantId = 1L;
        Agreement agreement1 = createSampleAgreement();
        Agreement agreement2 = createSampleAgreement();
        agreement2.setId(2L);
        agreement2.setStatus(Agreement.AgreementStatus.ACCEPTED);
        when(agreementRepository.findByParticipantId(participantId)).thenReturn(Arrays.asList(agreement1, agreement2));

        List<AgreementDTO> result = agreementService.getAgreementsByParticipantId(participantId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(Agreement.AgreementStatus.PENDING, result.get(0).getStatus());
        assertEquals(2L, result.get(1).getId());
        assertEquals(Agreement.AgreementStatus.ACCEPTED, result.get(1).getStatus());
        verify(agreementRepository).findByParticipantId(participantId);
    }

    @Test
    void testGetAgreementsByGrantId() {
        Long grantId = 1L;
        Agreement agreement1 = createSampleAgreement();
        Agreement agreement2 = createSampleAgreement();
        agreement2.setId(2L);
        agreement2.setStatus(Agreement.AgreementStatus.ACCEPTED);
        Agreement agreement3 = createSampleAgreement();
        agreement3.setId(3L);
        agreement3.setStatus(Agreement.AgreementStatus.REJECTED);
        when(agreementRepository.findByGrantId(grantId)).thenReturn(Arrays.asList(agreement1, agreement2, agreement3));

        List<AgreementDTO> result = agreementService.getAgreementsByGrantId(grantId);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(Agreement.AgreementStatus.PENDING, result.get(0).getStatus());
        assertEquals(2L, result.get(1).getId());
        assertEquals(Agreement.AgreementStatus.ACCEPTED, result.get(1).getStatus());
        assertEquals(3L, result.get(2).getId());
        assertEquals(Agreement.AgreementStatus.REJECTED, result.get(2).getStatus());
        verify(agreementRepository).findByGrantId(grantId);
    }

    @Test
    void testRegenerateAgreement() {
        Agreement agreement = createSampleAgreement();
        agreement.setStatus(Agreement.AgreementStatus.REJECTED);
        when(agreementRepository.findById(1L)).thenReturn(Optional.of(agreement));
        when(agreementRepository.save(any(Agreement.class))).thenReturn(agreement);

        AgreementDTO result = agreementService.regenerateAgreement(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Agreement.AgreementStatus.PENDING, result.getStatus());
        verify(agreementRepository).findById(1L);
        verify(agreementRepository).save(any(Agreement.class));
        verify(messagingTemplate).convertAndSend(eq("/topic/agreements"), any(AgreementDTO.class));
    }

    @Test
    void testGetPendingAgreements() {
        when(agreementRepository.countByStatus(Agreement.AgreementStatus.PENDING)).thenReturn(5L);
        long result = agreementService.getPendingAgreements();
        assertEquals(5L, result);
        verify(agreementRepository).countByStatus(Agreement.AgreementStatus.PENDING);
    }

    @Test
    void testGetTotalAgreements() {
        when(agreementRepository.count()).thenReturn(10L);
        long result = agreementService.getTotalAgreements();
        assertEquals(10L, result);
        verify(agreementRepository).count();
    }
}
