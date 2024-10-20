package com.grantmanagement.service.impl;

import com.grantmanagement.dto.AgreementDTO;
import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.dto.ParticipantDTO;
import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Grant;
import com.grantmanagement.model.Participant;
import com.grantmanagement.repository.AgreementRepository;
import com.grantmanagement.service.AgreementService;
import com.grantmanagement.service.NotificationService;
import com.grantmanagement.service.DocumentGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AgreementServiceImpl implements AgreementService {
    private static final Logger logger = LoggerFactory.getLogger(AgreementServiceImpl.class);

    private final AgreementRepository agreementRepository;
    private final NotificationService notificationService;
    private final DocumentGenerationService documentGenerationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public AgreementServiceImpl(AgreementRepository agreementRepository, NotificationService notificationService, DocumentGenerationService documentGenerationService, SimpMessagingTemplate messagingTemplate) {
        this.agreementRepository = agreementRepository;
        this.notificationService = notificationService;
        this.documentGenerationService = documentGenerationService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public AgreementDTO createAgreement(AgreementDTO agreementDTO) {
        logger.info("Creating new agreement: {}", agreementDTO);
        Agreement agreement = convertToEntity(agreementDTO);
        if (agreement.getCreatedAt() == null) {
            agreement.setCreatedAt(LocalDate.now());
        }
        Agreement savedAgreement = agreementRepository.save(agreement);
        logger.info("Agreement created successfully: {}", savedAgreement);

        try {
            notificationService.sendDocumentSigningNotification(savedAgreement);
            logger.info("Document signing notification sent for agreement: {}", savedAgreement.getId());
        } catch (Exception e) {
            logger.error("Failed to send document signing notification for agreement: {}", savedAgreement.getId(), e);
        }

        sendWebSocketMessage(savedAgreement);
        return this.convertToDTO(savedAgreement);
    }

    @Override
    public Optional<AgreementDTO> getAgreementById(Long id) {
        return agreementRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<AgreementDTO> getAllAgreements() {
        return agreementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AgreementDTO updateAgreement(Long id, AgreementDTO agreementDetails) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            updateAgreementFromDTO(existingAgreement, agreementDetails);
            generateAndSaveDocument(existingAgreement);
            checkAndApplyAutoAcceptance(existingAgreement);
            Agreement updatedAgreement = agreementRepository.save(existingAgreement);
            sendWebSocketMessage(updatedAgreement);
            return this.convertToDTO(updatedAgreement);
        }
        return null;
    }

    @Override
    public void deleteAgreement(Long id) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            String documentPath = agreement.get().getDocumentPath();
            if (documentPath != null) {
                try {
                    documentGenerationService.deleteGeneratedPDF(documentPath);
                } catch (IOException e) {
                    // Log the error, but continue with agreement deletion
                    logger.error("Error deleting PDF for agreement {}: ", id, e);
                }
            }
            agreementRepository.deleteById(id);
            sendWebSocketMessage(agreement.get());
        }
    }

    @Override
    public List<AgreementDTO> getAgreementsByParticipantId(Long participantId) {
        return agreementRepository.findByParticipantId(participantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgreementDTO> getAgreementsByGrantId(Long grantId) {
        return agreementRepository.findByGrantId(grantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AgreementDTO signAgreement(Long id) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            existingAgreement.setStatus(Agreement.AgreementStatus.ACCEPTED);
            existingAgreement = agreementRepository.save(existingAgreement);
            generateAndSaveDocument(existingAgreement);
            notificationService.sendAgreementSignedNotification(existingAgreement, existingAgreement.getParticipant());
            sendWebSocketMessage(existingAgreement);
            return this.convertToDTO(existingAgreement);
        }
        return null;
    }

    @Override
    public AgreementDTO rejectAgreement(Long id) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            existingAgreement.setStatus(Agreement.AgreementStatus.REJECTED);
            existingAgreement = agreementRepository.save(existingAgreement);
            notificationService.sendAgreementRejectedNotification(existingAgreement, existingAgreement.getParticipant());
            sendWebSocketMessage(existingAgreement);
            return this.convertToDTO(existingAgreement);
        }
        return null;
    }

    @Override
    public AgreementDTO regenerateAgreement(Long id) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            existingAgreement.setStatus(Agreement.AgreementStatus.PENDING);
            existingAgreement = agreementRepository.save(existingAgreement);
            checkAndApplyAutoAcceptance(existingAgreement);
            sendWebSocketMessage(existingAgreement);
            return this.convertToDTO(existingAgreement);
        }
        return null;
    }

    private void checkAndApplyAutoAcceptance(Agreement agreement) {
        try {
            Grant grant = agreement.getGrant();
            logger.info("Checking auto-acceptance for agreement: {}, grant: {}", agreement.getId(), grant.getId());
            if (grant.isAutoAcceptanceEnabled() &&
                LocalDate.now().isAfter(agreement.getCreatedAt().plusDays(grant.getAutoAcceptanceDays()))) {
                logger.info("Auto-acceptance criteria met. Accepting agreement: {}", agreement.getId());
                agreement.setStatus(Agreement.AgreementStatus.ACCEPTED);
                agreement.setAcceptedAt(LocalDate.now());
                agreementRepository.save(agreement);
                signAgreement(agreement.getId());
            } else {
                logger.info("Auto-acceptance criteria not met for agreement: {}", agreement.getId());
            }
        } catch (Exception e) {
            logger.error("Error in checkAndApplyAutoAcceptance for agreement: {}", agreement.getId(), e);
        }
    }

    private void generateAndSaveDocument(Agreement agreement) {
        try {
            logger.info("Generating document for agreement: {}", agreement.getId());
            byte[] pdfContent = documentGenerationService.generateAgreementPDF(agreement);
            String fileName = "agreement_" + agreement.getId() + ".pdf";
            String filePath = documentGenerationService.saveGeneratedPDF(pdfContent, fileName);
            agreement.setDocumentPath(filePath);
            logger.info("Document generated and saved for agreement: {}, path: {}", agreement.getId(), filePath);
        } catch (IOException e) {
            logger.error("Error generating and saving document for agreement: {}", agreement.getId(), e);
        }
    }

    private void sendWebSocketMessage(Agreement agreement) {
        try {
            messagingTemplate.convertAndSend("/topic/agreements", this.convertToDTO(agreement));
            logger.info("WebSocket message sent for agreement: {}", agreement.getId());
        } catch (Exception e) {
            logger.error("Error sending WebSocket message for agreement: {}", agreement.getId(), e);
        }
    }

    @Override
    public long getPendingAgreements() {
        return agreementRepository.countByStatus(Agreement.AgreementStatus.PENDING);
    }

    @Override
    public long getTotalAgreements() {
        return agreementRepository.count();
    }

    private GrantDTO convertToDTO(Grant grant) {
        return new GrantDTO(
            grant.getId(),
            grant.getTitle(),
            grant.getDescription(),
            grant.getAmount(),
            grant.getLegalText(),
            grant.isAutoAcceptanceEnabled(),
            grant.getAutoAcceptanceDays(),
            grant.getStartDate(),
            grant.getEndDate(),
            grant.getStatus() != null ? GrantDTO.GrantStatus.valueOf(grant.getStatus().name()) : null
        );
    }

    private ParticipantDTO convertToDTO(Participant participant) {
        return new ParticipantDTO(
            participant.getId(),
            participant.getName(),
            participant.getEmail(),
            participant.getOrganization()
        );
    }

    private AgreementDTO convertToDTO(Agreement agreement) {
        return new AgreementDTO(
            agreement.getId(),
            convertToDTO(agreement.getGrant()),
            convertToDTO(agreement.getParticipant()),
            agreement.getStatus(),
            agreement.getCreatedAt(),
            agreement.getUpdatedAt(),
            agreement.getDocumentPath(),
            agreement.getAcceptedAt()
        );
    }

    private Agreement convertToEntity(AgreementDTO agreementDTO) {
        Agreement agreement = new Agreement();
        agreement.setId(agreementDTO.getId());
        agreement.setGrant(convertToEntity(agreementDTO.getGrant()));
        agreement.setParticipant(convertToEntity(agreementDTO.getParticipant()));
        agreement.setStatus(agreementDTO.getStatus());
        agreement.setCreatedAt(agreementDTO.getCreatedAt());
        agreement.setAcceptedAt(agreementDTO.getAcceptedAt());
        agreement.setDocumentPath(agreementDTO.getDocumentPath());
        return agreement;
    }

    private Grant convertToEntity(GrantDTO grantDTO) {
        Grant grant = new Grant();
        grant.setId(grantDTO.getId());
        grant.setTitle(grantDTO.getTitle());
        grant.setDescription(grantDTO.getDescription());
        grant.setAmount(grantDTO.getAmount());
        grant.setLegalText(grantDTO.getLegalText());
        grant.setStartDate(grantDTO.getStartDate());
        grant.setEndDate(grantDTO.getEndDate());
        grant.setAutoAcceptanceEnabled(grantDTO.isAutoAcceptanceEnabled());
        grant.setAutoAcceptanceDays(grantDTO.getAutoAcceptanceDays());
        return grant;
    }

    private Participant convertToEntity(ParticipantDTO participantDTO) {
        Participant participant = new Participant();
        participant.setId(participantDTO.getId());
        participant.setName(participantDTO.getName());
        participant.setEmail(participantDTO.getEmail());
        participant.setOrganization(participantDTO.getOrganization());
        return participant;
    }

    private void updateAgreementFromDTO(Agreement agreement, AgreementDTO agreementDTO) {
        agreement.setGrant(convertToEntity(agreementDTO.getGrant()));
        agreement.setParticipant(convertToEntity(agreementDTO.getParticipant()));
        agreement.setStatus(agreementDTO.getStatus());
        agreement.setAcceptedAt(agreementDTO.getAcceptedAt());
        agreement.setDocumentPath(agreementDTO.getDocumentPath());
    }
}
