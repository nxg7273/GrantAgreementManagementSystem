package com.grantmanagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.grantmanagement.model.Participant;
import com.grantmanagement.model.Grant;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringJUnitConfig
class ParticipantRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(ParticipantRepositoryTest.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        logger.info("Setting up test environment for ParticipantRepositoryTest");
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            logger.info("Database product name: {}", conn.getMetaData().getDatabaseProductName());
            logger.info("Database URL: {}", conn.getMetaData().getURL());
            logger.info("Database username: {}", conn.getMetaData().getUserName());
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
        logger.info("EntityManager: {}", entityManager);
        logger.info("ParticipantRepository: {}", participantRepository);
    }

    @Test
    void testFindByName() {
        logger.info("Starting testFindByName");
        Participant participant1 = createParticipant("John Doe", "john@example.com", "Org1", "1234567890");
        entityManager.persist(participant1);

        Participant participant2 = createParticipant("Jane Smith", "jane@example.com", "Org2", "0987654321");
        entityManager.persist(participant2);

        entityManager.flush();

        List<Participant> foundParticipants = participantRepository.findByName("John Doe");

        assertEquals(1, foundParticipants.size());
        assertEquals("John Doe", foundParticipants.get(0).getName());
        logger.info("Completed testFindByName");
    }

    @Test
    void testFindByGrantId() {
        logger.info("Starting testFindByGrantId");
        try {
            Grant grant = createGrant("Test Grant", new BigDecimal("10000"), LocalDate.now(), LocalDate.now().plusMonths(6));
            logger.info("Created grant: {}", grant);
            entityManager.persist(grant);
            logger.info("Persisted grant with ID: {}", grant.getId());

            Participant participant1 = createParticipant("John Doe", "john@example.com", "Org1", "1234567890");
            participant1.setGrant(grant);
            logger.info("Created participant1: {}", participant1);
            entityManager.persist(participant1);
            logger.info("Persisted participant1 with ID: {}", participant1.getId());

            Participant participant2 = createParticipant("Jane Smith", "jane@example.com", "Org2", "0987654321");
            participant2.setGrant(grant);
            logger.info("Created participant2: {}", participant2);
            entityManager.persist(participant2);
            logger.info("Persisted participant2 with ID: {}", participant2.getId());

            Participant participant3 = createParticipant("Bob Johnson", "bob@example.com", "Org3", "1122334455");
            logger.info("Created participant3: {}", participant3);
            entityManager.persist(participant3);
            logger.info("Persisted participant3 with ID: {}", participant3.getId());

            entityManager.flush();
            logger.info("Flushed entity manager");

            List<Participant> foundParticipants = participantRepository.findByGrantId(grant.getId());
            logger.info("Found participants: {}", foundParticipants);

            assertEquals(2, foundParticipants.size(), "Expected to find 2 participants");
            assertTrue(foundParticipants.stream().allMatch(p -> p.getGrant().getId().equals(grant.getId())), "All found participants should have the correct grant ID");
        } catch (Exception e) {
            logger.error("Error in testFindByGrantId", e);
            throw e;
        }
        logger.info("Completed testFindByGrantId");
    }

    @Test
    void testFindByEmail() {
        logger.info("Starting testFindByEmail");
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
        logger.info("Completed testFindByEmail");
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
        grant.setDescription("Test description for " + title);
        grant.setAmount(amount);
        grant.setLegalText("Legal text for " + title);
        grant.setAutoAcceptanceEnabled(true);
        grant.setAutoAcceptanceDays(7);
        grant.setStartDate(startDate);
        grant.setEndDate(endDate);
        grant.setStatus(Grant.GrantStatus.ACTIVE);
        grant.setGrantNumber("GR-" + System.currentTimeMillis());
        return grant;
    }
}
