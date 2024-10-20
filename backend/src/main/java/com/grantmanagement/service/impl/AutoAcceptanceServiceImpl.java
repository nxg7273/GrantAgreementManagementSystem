package com.grantmanagement.service.impl;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Grant;
import com.grantmanagement.repository.AgreementRepository;
import com.grantmanagement.service.AgreementService;
import com.grantmanagement.service.AutoAcceptanceService;
import com.grantmanagement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AutoAcceptanceServiceImpl implements AutoAcceptanceService {

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void checkAndApplyAutoAcceptance() {
        List<Agreement> pendingAgreements = agreementRepository.findByStatus(Agreement.AgreementStatus.PENDING);

        for (Agreement agreement : pendingAgreements) {
            Grant grant = agreement.getGrant();
            if (grant.isAutoAcceptanceEnabled() &&
                LocalDate.now().isAfter(agreement.getCreatedAt().plusDays(grant.getAutoAcceptanceDays()))) {

                agreement.setStatus(Agreement.AgreementStatus.ACCEPTED);
                agreement.setAcceptedAt(LocalDate.now());
                agreementRepository.save(agreement);

                agreementService.signAgreement(agreement.getId());

                notificationService.sendAgreementSignedNotification(agreement, agreement.getParticipant());
            }
        }
    }
}
