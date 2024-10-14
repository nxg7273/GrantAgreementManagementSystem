package com.grantmanagement.service.impl;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Participant;
import com.grantmanagement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendAgreementCreatedNotification(Agreement agreement, Participant participant) {
        String subject = "New Grant Agreement Available";
        String body = String.format("Dear %s,\n\nA new grant agreement is available for your review. " +
                        "Please log in to the Grant Agreement Management System to view and sign the agreement.\n\n" +
                        "Grant ID: %d\nAgreement ID: %d\n\nBest regards,\nGrant Management Team",
                participant.getName(),
                agreement.getGrant().getId(),
                agreement.getId());

        sendEmail(participant.getEmail(), subject, body);
    }

    @Override
    public void sendAgreementSignedNotification(Agreement agreement, Participant participant) {
        String subject = "Grant Agreement Signed";
        String body = String.format("Dear %s,\n\nYour grant agreement has been successfully signed. " +
                        "Thank you for completing this process.\n\n" +
                        "Grant ID: %d\nAgreement ID: %d\n\nBest regards,\nGrant Management Team",
                participant.getName(),
                agreement.getGrant().getId(),
                agreement.getId());

        sendEmail(participant.getEmail(), subject, body);
    }

    @Override
    public void sendAgreementRejectedNotification(Agreement agreement, Participant participant) {
        String subject = "Grant Agreement Rejected";
        String body = String.format("Dear %s,\n\nYour grant agreement has been rejected. " +
                        "If you have any questions or concerns, please contact our support team.\n\n" +
                        "Grant ID: %d\nAgreement ID: %d\n\nBest regards,\nGrant Management Team",
                participant.getName(),
                agreement.getGrant().getId(),
                agreement.getId());

        sendEmail(participant.getEmail(), subject, body);
    }

    @Override
    public void sendAgreementRegeneratedNotification(Agreement agreement, Participant participant) {
        String subject = "Grant Agreement Regenerated";
        String body = String.format("Dear %s,\n\nYour grant agreement has been regenerated. " +
                        "Please log in to the Grant Agreement Management System to review the updated agreement.\n\n" +
                        "Grant ID: %d\nAgreement ID: %d\n\nBest regards,\nGrant Management Team",
                participant.getName(),
                agreement.getGrant().getId(),
                agreement.getId());

        sendEmail(participant.getEmail(), subject, body);
    }

    @Override
    public void sendReminderNotification(Agreement agreement, Participant participant) {
        String subject = "Grant Agreement Reminder";
        String body = String.format("Dear %s,\n\nThis is a reminder about your pending grant agreement. " +
                        "Please log in to the Grant Agreement Management System to review and take necessary actions.\n\n" +
                        "Grant ID: %d\nAgreement ID: %d\n\nBest regards,\nGrant Management Team",
                participant.getName(),
                agreement.getGrant().getId(),
                agreement.getId());

        sendEmail(participant.getEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@grantmanagementsystem.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void updateNotificationPreferences(Participant participant, boolean emailNotifications, boolean inAppNotifications) {
        participant.setEmailNotificationsEnabled(emailNotifications);
        participant.setInAppNotificationsEnabled(inAppNotifications);
        // Note: We need to save the updated participant to the database.
        // This should be done in the ParticipantService, so we'll assume it's handled there.
        // If not, we should inject ParticipantRepository here and save the participant.
    }
}
