package com.grantmanagement.service;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Participant;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendAgreementCreatedNotification(Agreement agreement, Participant participant);
    void sendAgreementSignedNotification(Agreement agreement, Participant participant);
    void sendAgreementRejectedNotification(Agreement agreement, Participant participant);
    void sendAgreementRegeneratedNotification(Agreement agreement, Participant participant);
    void sendReminderNotification(Agreement agreement, Participant participant);
    void updateNotificationPreferences(Participant participant, boolean emailNotifications, boolean inAppNotifications);
    void sendDocumentSigningNotification(Agreement agreement);
}
