package com.grantmanagement.service;

import com.grantmanagement.dto.AgreementDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AgreementService {
    AgreementDTO createAgreement(AgreementDTO agreementDTO);
    Optional<AgreementDTO> getAgreementById(Long id);
    List<AgreementDTO> getAllAgreements();
    AgreementDTO updateAgreement(Long id, AgreementDTO agreementDetails);
    void deleteAgreement(Long id);
    List<AgreementDTO> getAgreementsByParticipantId(Long participantId);
    List<AgreementDTO> getAgreementsByGrantId(Long grantId);
    AgreementDTO signAgreement(Long id);
    AgreementDTO rejectAgreement(Long id);
    AgreementDTO regenerateAgreement(Long id);
    long getTotalAgreements();
    long getPendingAgreements();
}
