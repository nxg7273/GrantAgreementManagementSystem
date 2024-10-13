package com.grantmanagement.service.impl;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Grant;
import com.grantmanagement.repository.AgreementRepository;
import com.grantmanagement.service.AgreementService;
import com.grantmanagement.service.NotificationService;
import com.grantmanagement.service.DocumentGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.io.IOException;

@Service
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final NotificationService notificationService;
    private final DocumentGenerationService documentGenerationService;

    @Autowired
    public AgreementServiceImpl(AgreementRepository agreementRepository, NotificationService notificationService, DocumentGenerationService documentGenerationService) {
        this.agreementRepository = agreementRepository;
        this.notificationService = notificationService;
        this.documentGenerationService = documentGenerationService;
    }

    @Override
    public Agreement createAgreement(Agreement agreement) {
        agreement = agreementRepository.save(agreement);
        checkAndApplyAutoAcceptance(agreement);
        return agreement;
    }

    @Override
    public Optional<Agreement> getAgreementById(Long id) {
        return agreementRepository.findById(id);
    }

    @Override
    public List<Agreement> getAllAgreements() {
        return agreementRepository.findAll();
    }

    @Override
    public Agreement updateAgreement(Long id, Agreement agreementDetails) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            existingAgreement.setStatus(agreementDetails.getStatus());
            existingAgreement.setDocumentPath(agreementDetails.getDocumentPath());
            existingAgreement.setGrant(agreementDetails.getGrant());
            existingAgreement.setParticipant(agreementDetails.getParticipant());
            existingAgreement = agreementRepository.save(existingAgreement);
            generateAndSaveDocument(existingAgreement);
            checkAndApplyAutoAcceptance(existingAgreement);
            return existingAgreement;
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
                    e.printStackTrace();
                }
            }
            agreementRepository.deleteById(id);
        }
    }

    @Override
    public List<Agreement> getAgreementsByParticipantId(Long participantId) {
        return agreementRepository.findByParticipantId(participantId);
    }

    @Override
    public List<Agreement> getAgreementsByGrantId(Long grantId) {
        return agreementRepository.findByGrantId(grantId);
    }

    @Override
    public Agreement signAgreement(Long id) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            existingAgreement.setStatus("SIGNED");
            existingAgreement = agreementRepository.save(existingAgreement);
            generateAndSaveDocument(existingAgreement);
            notificationService.sendAgreementSignedNotification(existingAgreement);
            return existingAgreement;
        }
        return null;
    }

    @Override
    public Agreement rejectAgreement(Long id) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            existingAgreement.setStatus("REJECTED");
            existingAgreement = agreementRepository.save(existingAgreement);
            notificationService.sendAgreementRejectionNotification(existingAgreement);
            return existingAgreement;
        }
        return null;
    }

    @Override
    public Agreement regenerateAgreement(Long id) {
        Optional<Agreement> agreement = agreementRepository.findById(id);
        if (agreement.isPresent()) {
            Agreement existingAgreement = agreement.get();
            existingAgreement.setStatus("REGENERATED");
            existingAgreement = agreementRepository.save(existingAgreement);
            checkAndApplyAutoAcceptance(existingAgreement);
            return existingAgreement;
        }
        return null;
    }

    private void checkAndApplyAutoAcceptance(Agreement agreement) {
        Grant grant = agreement.getGrant();
        if (grant.isAutoAcceptanceEnabled() &&
            LocalDateTime.now().isAfter(agreement.getCreatedAt().plusDays(grant.getAutoAcceptanceDays()))) {
            agreement.setStatus("ACCEPTED");
            agreement.setAcceptedAt(LocalDateTime.now());
            agreementRepository.save(agreement);
            signAgreement(agreement.getId());
        }
    }

    private void generateAndSaveDocument(Agreement agreement) {
        try {
            byte[] pdfContent = documentGenerationService.generateAgreementPDF(agreement);
            String fileName = "agreement_" + agreement.getId() + ".pdf";
            String filePath = documentGenerationService.saveGeneratedPDF(pdfContent, fileName);
            agreement.setDocumentPath(filePath);
            agreementRepository.save(agreement);
        } catch (IOException e) {
            // Log the error and handle it appropriately
            e.printStackTrace();
        }
    }
}
