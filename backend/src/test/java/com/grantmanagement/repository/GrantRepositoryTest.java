package com.grantmanagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.grantmanagement.model.Grant;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DataJpaTest
class GrantRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(GrantRepositoryTest.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GrantRepository grantRepository;

    private Grant createTestGrant(String title, Grant.GrantStatus status, BigDecimal amount) {
        Grant grant = new Grant();
        grant.setTitle(title);
        grant.setDescription("Test description");
        grant.setAmount(amount);
        grant.setLegalText("Test legal text");
        grant.setAutoAcceptanceEnabled(true);
        grant.setAutoAcceptanceDays(30);
        grant.setStartDate(LocalDate.now());
        grant.setEndDate(LocalDate.now().plusMonths(6));
        grant.setStatus(status);
        return grant;
    }

    @Test
    void testFindByStatus() {
        logger.debug("Starting testFindByStatus");
        Grant grant1 = createTestGrant("Grant 1", Grant.GrantStatus.ACTIVE, new BigDecimal("10000.00"));
        entityManager.persist(grant1);
        logger.debug("Persisted grant1: {}", grant1);

        Grant grant2 = createTestGrant("Grant 2", Grant.GrantStatus.CLOSED, new BigDecimal("20000.00"));
        entityManager.persist(grant2);
        logger.debug("Persisted grant2: {}", grant2);

        Grant grant3 = createTestGrant("Grant 3", Grant.GrantStatus.ACTIVE, new BigDecimal("30000.00"));
        entityManager.persist(grant3);
        logger.debug("Persisted grant3: {}", grant3);

        entityManager.flush();
        logger.debug("Flushed entity manager");

        List<Grant> activeGrants = grantRepository.findByStatus(Grant.GrantStatus.ACTIVE);
        logger.debug("Found {} active grants", activeGrants.size());

        assertEquals(2, activeGrants.size());
        assertTrue(activeGrants.stream().allMatch(g -> g.getStatus() == Grant.GrantStatus.ACTIVE));
        logger.debug("testFindByStatus completed successfully");
    }

    @Test
    void testFindByTitle() {
        logger.debug("Starting testFindByTitle");
        Grant grant1 = createTestGrant("Research Grant", Grant.GrantStatus.ACTIVE, new BigDecimal("10000.00"));
        entityManager.persist(grant1);
        logger.debug("Persisted grant1: {}", grant1);

        Grant grant2 = createTestGrant("Education Grant", Grant.GrantStatus.ACTIVE, new BigDecimal("20000.00"));
        entityManager.persist(grant2);
        logger.debug("Persisted grant2: {}", grant2);

        entityManager.flush();
        logger.debug("Flushed entity manager");

        List<Grant> foundGrants = grantRepository.findByTitle("Research Grant");
        logger.debug("Found {} grants with title 'Research Grant'", foundGrants.size());

        assertEquals(1, foundGrants.size());
        assertEquals("Research Grant", foundGrants.get(0).getTitle());
        logger.debug("testFindByTitle completed successfully");
    }

    @Test
    void testFindByAmountGreaterThan() {
        logger.debug("Starting testFindByAmountGreaterThan");
        Grant grant1 = createTestGrant("Grant 1", Grant.GrantStatus.ACTIVE, new BigDecimal("10000.00"));
        entityManager.persist(grant1);
        logger.debug("Persisted grant1: {}", grant1);

        Grant grant2 = createTestGrant("Grant 2", Grant.GrantStatus.ACTIVE, new BigDecimal("5000.00"));
        entityManager.persist(grant2);
        logger.debug("Persisted grant2: {}", grant2);

        Grant grant3 = createTestGrant("Grant 3", Grant.GrantStatus.ACTIVE, new BigDecimal("15000.00"));
        entityManager.persist(grant3);
        logger.debug("Persisted grant3: {}", grant3);

        entityManager.flush();
        logger.debug("Flushed entity manager");

        List<Grant> largeGrants = grantRepository.findByAmountGreaterThan(new BigDecimal("7500.00"));
        logger.debug("Found {} grants with amount greater than 7500.00", largeGrants.size());

        assertEquals(2, largeGrants.size());
        assertTrue(largeGrants.stream().allMatch(g -> g.getAmount().compareTo(new BigDecimal("7500.00")) > 0));
        logger.debug("testFindByAmountGreaterThan completed successfully");
    }
}
