package com.grantmanagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Grant;
import com.grantmanagement.model.Participant;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AgreementRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AgreementRepository agreementRepository;

    @Test
    void testFindByParticipantId() {
        Participant participant = new Participant();
        participant.setName("Test Participant");
        participant.setEmail("test@example.com");
        participant.setOrganization("Test Org");
        participant.setPhoneNumber("1234567890");
        entityManager.persist(participant);

        Grant grant = new Grant();
        grant.setTitle("Test Grant");
        grant.setDescription("Test Description");
        grant.setAmount(new BigDecimal("10000.00"));
        grant.setLegalText("Legal Text");
        grant.setAutoAcceptanceEnabled(true);
        grant.setAutoAcceptanceDays(30);
        grant.setStartDate(LocalDate.now());
        grant.setEndDate(LocalDate.now().plusMonths(6));
        grant.setStatus(Grant.GrantStatus.ACTIVE);
        grant.setGrantNumber("GR001");
        entityManager.persist(grant);

        Agreement agreement1 = new Agreement();
        agreement1.setParticipant(participant);
        agreement1.setGrant(grant);
        agreement1.setStatus(Agreement.AgreementStatus.PENDING);
        agreement1.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement1);

        Agreement agreement2 = new Agreement();
        agreement2.setParticipant(participant);
        agreement2.setGrant(grant);
        agreement2.setStatus(Agreement.AgreementStatus.PENDING);
        agreement2.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement2);

        entityManager.flush();

        List<Agreement> foundAgreements = agreementRepository.findByParticipantId(participant.getId());

        assertEquals(2, foundAgreements.size());
        assertTrue(foundAgreements.stream().allMatch(a -> a.getParticipant().getId().equals(participant.getId())));
    }

    @Test
    void testFindByGrantId() {
        Participant participant = new Participant();
        participant.setName("Test Participant");
        participant.setEmail("test@example.com");
        participant.setOrganization("Test Org");
        participant.setPhoneNumber("1234567890");
        entityManager.persist(participant);

        Grant grant = new Grant();
        grant.setTitle("Test Grant");
        grant.setDescription("Test Description");
        grant.setAmount(new BigDecimal("10000.00"));
        grant.setLegalText("Legal Text");
        grant.setAutoAcceptanceEnabled(true);
        grant.setAutoAcceptanceDays(30);
        grant.setStartDate(LocalDate.now());
        grant.setEndDate(LocalDate.now().plusMonths(6));
        grant.setStatus(Grant.GrantStatus.ACTIVE);
        grant.setGrantNumber("GR001");
        entityManager.persist(grant);

        Agreement agreement1 = new Agreement();
        agreement1.setParticipant(participant);
        agreement1.setGrant(grant);
        agreement1.setStatus(Agreement.AgreementStatus.PENDING);
        agreement1.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement1);

        Agreement agreement2 = new Agreement();
        agreement2.setParticipant(participant);
        agreement2.setGrant(grant);
        agreement2.setStatus(Agreement.AgreementStatus.PENDING);
        agreement2.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement2);

        entityManager.flush();

        List<Agreement> foundAgreements = agreementRepository.findByGrantId(grant.getId());

        assertEquals(2, foundAgreements.size());
        assertTrue(foundAgreements.stream().allMatch(a -> a.getGrant().getId().equals(grant.getId())));
    }

    @Test
    void testFindByStatus() {
        Participant participant = new Participant();
        participant.setName("Test Participant");
        participant.setEmail("test@example.com");
        participant.setOrganization("Test Org");
        participant.setPhoneNumber("1234567890");
        entityManager.persist(participant);

        Grant grant = new Grant();
        grant.setTitle("Test Grant");
        grant.setDescription("Test Description");
        grant.setAmount(new BigDecimal("10000.00"));
        grant.setLegalText("Legal Text");
        grant.setAutoAcceptanceEnabled(true);
        grant.setAutoAcceptanceDays(30);
        grant.setStartDate(LocalDate.now());
        grant.setEndDate(LocalDate.now().plusMonths(6));
        grant.setStatus(Grant.GrantStatus.ACTIVE);
        grant.setGrantNumber("GR001");
        entityManager.persist(grant);

        Agreement agreement1 = new Agreement();
        agreement1.setParticipant(participant);
        agreement1.setGrant(grant);
        agreement1.setStatus(Agreement.AgreementStatus.PENDING);
        agreement1.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement1);

        Agreement agreement2 = new Agreement();
        agreement2.setParticipant(participant);
        agreement2.setGrant(grant);
        agreement2.setStatus(Agreement.AgreementStatus.ACCEPTED);
        agreement2.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement2);

        Agreement agreement3 = new Agreement();
        agreement3.setParticipant(participant);
        agreement3.setGrant(grant);
        agreement3.setStatus(Agreement.AgreementStatus.PENDING);
        agreement3.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement3);

        entityManager.flush();

        List<Agreement> pendingAgreements = agreementRepository.findByStatus(Agreement.AgreementStatus.PENDING);

        assertEquals(2, pendingAgreements.size());
        assertTrue(pendingAgreements.stream().allMatch(a -> a.getStatus() == Agreement.AgreementStatus.PENDING));
    }
}
