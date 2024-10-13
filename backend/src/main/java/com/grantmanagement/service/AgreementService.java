package com.grantmanagement.service;

import com.grantmanagement.model.Agreement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AgreementService {
    Agreement createAgreement(Agreement agreement);
    Optional<Agreement> getAgreementById(Long id);
    List<Agreement> getAllAgreements();
    Agreement updateAgreement(Long id, Agreement agreementDetails);
    void deleteAgreement(Long id);
    List<Agreement> getAgreementsByParticipantId(Long participantId);
    List<Agreement> getAgreementsByGrantId(Long grantId);
    Agreement signAgreement(Long id);
    Agreement rejectAgreement(Long id);
    Agreement regenerateAgreement(Long id);
}
