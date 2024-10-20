package com.grantmanagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.grantmanagement.model.Participant;
import com.grantmanagement.model.Grant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ParticipantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    void testFindByName() {
        Participant participant1 = createParticipant("John Doe", "john@example.com", "Org1", "1234567890");
        entityManager.persist(participant1);

        Participant participant2 = createParticipant("Jane Smith", "jane@example.com", "Org2", "0987654321");
        entityManager.persist(participant2);

        entityManager.flush();

        List<Participant> foundParticipants = participantRepository.findByName("John Doe");

        assertEquals(1, foundParticipants.size());
        assertEquals("John Doe", foundParticipants.get(0).getName());
    }

    @Test
    void testFindByGrantId() {
        Grant grant = createGrant("Test Grant", new BigDecimal("10000"), LocalDate.now(), LocalDate.now().plusMonths(6));
        entityManager.persist(grant);

        Participant participant1 = createParticipant("John Doe", "john@example.com", "Org1", "1234567890");
        participant1.setGrant(grant);
        entityManager.persist(participant1);

        Participant participant2 = createParticipant("Jane Smith", "jane@example.com", "Org2", "0987654321");
        participant2.setGrant(grant);
        entityManager.persist(participant2);

        Participant participant3 = createParticipant("Bob Johnson", "bob@example.com", "Org3", "1122334455");
        entityManager.persist(participant3);

        entityManager.flush();

        List<Participant> foundParticipants = participantRepository.findByGrantId(grant.getId());

        assertEquals(2, foundParticipants.size());
        assertTrue(foundParticipants.stream().allMatch(p -> p.getGrant().getId().equals(grant.getId())));
    }

    @Test
    void testFindByEmail() {
        Participant participant1 = createParticipant("John Doe", "john@example.com", "Org1", "1234567890");
        entityManager.persist(participant1);

        Participant participant2 = createParticipant("Jane Smith", "jane@example.com", "Org2", "0987654321");
        entityManager.persist(participant2);

        entityManager.flush();

        Optional<Participant> foundParticipantOptional = participantRepository.findByEmail("john@example.com");

        assertTrue(foundParticipantOptional.isPresent());
        Participant foundParticipant = foundParticipantOptional.get();
        assertEquals("John Doe", foundParticipant.getName());
        assertEquals("john@example.com", foundParticipant.getEmail());
    }

    private Participant createParticipant(String name, String email, String organization, String phoneNumber) {
        Participant participant = new Participant();
        participant.setName(name);
        participant.setEmail(email);
        participant.setOrganization(organization);
        participant.setPhoneNumber(phoneNumber);
        return participant;
    }

    private Grant createGrant(String title, BigDecimal amount, LocalDate startDate, LocalDate endDate) {
        Grant grant = new Grant();
        grant.setTitle(title);
        grant.setAmount(amount);
        grant.setStartDate(startDate);
        grant.setEndDate(endDate);
        return grant;
    }
}
