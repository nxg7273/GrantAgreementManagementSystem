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
    public void sendAgreementCreationNotification(Agreement agreement) {
        Participant participant = agreement.getParticipant();
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
    public void sendAgreementSignedNotification(Agreement agreement) {
        Participant participant = agreement.getParticipant();
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
    public void sendAgreementRejectionNotification(Agreement agreement) {
        Participant participant = agreement.getParticipant();
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
    public void sendAgreementExpirationReminder(Agreement agreement) {
        Participant participant = agreement.getParticipant();
        String subject = "Grant Agreement Expiration Reminder";
        String body = String.format("Dear %s,\n\nThis is a reminder that your grant agreement is approaching its expiration date. " +
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
}
