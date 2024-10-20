package com.grantmanagement.service;

import com.grantmanagement.model.Agreement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AgreementService {
    Agreement createAgreement(Agreement agreement);
    Agreement updateAgreement(Long id, Agreement agreement);
    Agreement getAgreementById(Long id);
    List<Agreement> getAllAgreements();
    List<Agreement> getAgreementsByParticipantId(Long participantId);
    List<Agreement> getAgreementsByGrantId(Long grantId);
    void deleteAgreement(Long id);
    Agreement signAgreement(Long id);
    Agreement rejectAgreement(Long id);
    Agreement regenerateAgreement(Long id);
}
