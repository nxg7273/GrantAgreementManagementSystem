package com.grantmanagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.Grant;
import com.grantmanagement.model.Participant;
import com.grantmanagement.config.TestConfig;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import java.util.Properties;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class AgreementRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(AgreementRepositoryTest.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    @BeforeEach
    void setUp() {
        logger.debug("Setting up test");
        logger.debug("Entity Manager: {}", entityManager);
        logger.debug("Agreement Repository: {}", agreementRepository);

        // Log ApplicationContext information
        logger.debug("ApplicationContext: {}", applicationContext);
        logger.debug("Active profiles: {}", String.join(", ", environment.getActiveProfiles()));

        // Log EntityManagerFactory information
        EntityManagerFactory emf = entityManager.getEntityManager().getEntityManagerFactory();
        logger.debug("EntityManagerFactory: {}", emf);
        logger.debug("EntityManagerFactory properties: {}", emf.getProperties());

        // Log Hibernate configuration
        printHibernateConfiguration();

        // Log Hibernate entity mapping information
        printHibernateEntityMappings();

        // Log database connection information
        try {
            Connection connection = entityManager.getEntityManager().unwrap(Connection.class);
            logger.debug("Database URL: {}", connection.getMetaData().getURL());
            logger.debug("Database User: {}", connection.getMetaData().getUserName());
            logger.debug("Database Product Name: {}", connection.getMetaData().getDatabaseProductName());
            logger.debug("Database Product Version: {}", connection.getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            logger.error("Error getting database connection information", e);
        }

        // Check if tables exist
        try {
            entityManager.getEntityManager().createNativeQuery("SELECT 1 FROM agreements").getSingleResult();
            logger.debug("Agreements table exists");
        } catch (Exception e) {
            logger.error("Agreements table does not exist", e);
        }

        try {
            entityManager.getEntityManager().createNativeQuery("SELECT 1 FROM grants").getSingleResult();
            logger.debug("Grants table exists");
        } catch (Exception e) {
            logger.error("Grants table does not exist", e);
        }

        try {
            entityManager.getEntityManager().createNativeQuery("SELECT 1 FROM participants").getSingleResult();
            logger.debug("Participants table exists");
        } catch (Exception e) {
            logger.error("Participants table does not exist", e);
        }

        // Print database metadata
        printDatabaseMetadata();

        // Log entity scanning information
        logger.debug("Scanned entities: {}", String.join(", ", applicationContext.getBeanNamesForType(Object.class)));
    }

    private void printHibernateConfiguration() {
        try {
            EntityManagerFactory emf = entityManager.getEntityManager().getEntityManagerFactory();
            SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
            StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();

            Configuration configuration = new Configuration();
            Properties properties = new Properties();
            properties.putAll(sessionFactory.getProperties());
            configuration.setProperties(properties);

            logger.debug("Hibernate Configuration:");
            logger.debug("Dialect: {}", configuration.getProperty("hibernate.dialect"));
            logger.debug("Show SQL: {}", configuration.getProperty("hibernate.show_sql"));
            logger.debug("Format SQL: {}", configuration.getProperty("hibernate.format_sql"));
            logger.debug("HBM2DDL Auto: {}", configuration.getProperty("hibernate.hbm2ddl.auto"));
        } catch (Exception e) {
            logger.error("Error printing Hibernate configuration", e);
        }
    }

    private void printHibernateEntityMappings() {
        try {
            EntityManagerFactory emf = entityManager.getEntityManager().getEntityManagerFactory();
            SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
            StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();

            MetadataSources metadataSources = new MetadataSources(serviceRegistry);
            Metadata metadata = metadataSources.buildMetadata();

            logger.debug("Hibernate Entity Mappings:");
            for (PersistentClass persistentClass : metadata.getEntityBindings()) {
                logger.debug("Entity: {}", persistentClass.getEntityName());
                logger.debug("  Table: {}", persistentClass.getTable().getName());
                persistentClass.getPropertyIterator().forEachRemaining(property -> {
                    if (property instanceof Property) {
                        Property prop = (Property) property;
                        logger.debug("  Property: {} ({})", prop.getName(), prop.getType().getName());
                    }
                });
            }
        } catch (Exception e) {
            logger.error("Error printing Hibernate entity mappings", e);
        }
    }

    private void printDatabaseMetadata() {
        try {
            Connection connection = entityManager.getEntityManager().unwrap(Connection.class);
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            logger.debug("Database tables:");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                logger.debug("Table: {}", tableName);

                ResultSet columns = metaData.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");
                    logger.debug("  Column: {} ({})", columnName, columnType);
                }
            }
        } catch (Exception e) {
            logger.error("Error printing database metadata", e);
        }
    }

    @Test
    void testFindByParticipantId() {
        logger.debug("Starting testFindByParticipantId");
        Participant participant = new Participant();
        participant.setName("Test Participant");
        participant.setEmail("test@example.com");
        participant.setOrganization("Test Org");
        participant.setPhoneNumber("1234567890");
        entityManager.persist(participant);
        logger.debug("Persisted participant: {}", participant);

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
        logger.debug("Persisted grant: {}", grant);

        Agreement agreement1 = new Agreement();
        agreement1.setParticipant(participant);
        agreement1.setGrant(grant);
        agreement1.setStatus(Agreement.AgreementStatus.PENDING);
        agreement1.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement1);
        logger.debug("Persisted agreement1: {}", agreement1);

        Agreement agreement2 = new Agreement();
        agreement2.setParticipant(participant);
        agreement2.setGrant(grant);
        agreement2.setStatus(Agreement.AgreementStatus.PENDING);
        agreement2.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement2);
        logger.debug("Persisted agreement2: {}", agreement2);

        entityManager.flush();
        logger.debug("Flushed entity manager");

        List<Agreement> foundAgreements = agreementRepository.findByParticipantId(participant.getId());
        logger.debug("Found agreements: {}", foundAgreements);

        assertEquals(2, foundAgreements.size());
        assertTrue(foundAgreements.stream().allMatch(a -> a.getParticipant().getId().equals(participant.getId())));
        logger.debug("Assertions passed");
    }

    @Test
    void testFindByGrantId() {
        logger.debug("Starting testFindByGrantId");
        Participant participant = new Participant();
        participant.setName("Test Participant");
        participant.setEmail("test@example.com");
        participant.setOrganization("Test Org");
        participant.setPhoneNumber("1234567890");
        entityManager.persist(participant);
        logger.debug("Persisted participant: {}", participant);

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
        logger.debug("Persisted grant: {}", grant);

        Agreement agreement1 = new Agreement();
        agreement1.setParticipant(participant);
        agreement1.setGrant(grant);
        agreement1.setStatus(Agreement.AgreementStatus.PENDING);
        agreement1.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement1);
        logger.debug("Persisted agreement1: {}", agreement1);

        Agreement agreement2 = new Agreement();
        agreement2.setParticipant(participant);
        agreement2.setGrant(grant);
        agreement2.setStatus(Agreement.AgreementStatus.PENDING);
        agreement2.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement2);
        logger.debug("Persisted agreement2: {}", agreement2);

        entityManager.flush();
        logger.debug("Flushed entity manager");

        List<Agreement> foundAgreements = agreementRepository.findByGrantId(grant.getId());
        logger.debug("Found agreements: {}", foundAgreements);

        assertEquals(2, foundAgreements.size());
        assertTrue(foundAgreements.stream().allMatch(a -> a.getGrant().getId().equals(grant.getId())));
        logger.debug("Assertions passed");
    }

    @Test
    void testFindByStatus() {
        logger.debug("Starting testFindByStatus");
        Participant participant = new Participant();
        participant.setName("Test Participant");
        participant.setEmail("test@example.com");
        participant.setOrganization("Test Org");
        participant.setPhoneNumber("1234567890");
        entityManager.persist(participant);
        logger.debug("Persisted participant: {}", participant);

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
        logger.debug("Persisted grant: {}", grant);

        Agreement agreement1 = new Agreement();
        agreement1.setParticipant(participant);
        agreement1.setGrant(grant);
        agreement1.setStatus(Agreement.AgreementStatus.PENDING);
        agreement1.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement1);
        logger.debug("Persisted agreement1: {}", agreement1);

        Agreement agreement2 = new Agreement();
        agreement2.setParticipant(participant);
        agreement2.setGrant(grant);
        agreement2.setStatus(Agreement.AgreementStatus.ACCEPTED);
        agreement2.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement2);
        logger.debug("Persisted agreement2: {}", agreement2);

        Agreement agreement3 = new Agreement();
        agreement3.setParticipant(participant);
        agreement3.setGrant(grant);
        agreement3.setStatus(Agreement.AgreementStatus.PENDING);
        agreement3.setCreatedAt(LocalDate.now());
        entityManager.persist(agreement3);
        logger.debug("Persisted agreement3: {}", agreement3);

        entityManager.flush();
        logger.debug("Flushed entity manager");

        List<Agreement> pendingAgreements = agreementRepository.findByStatus(Agreement.AgreementStatus.PENDING);
        logger.debug("Found pending agreements: {}", pendingAgreements);

        assertEquals(2, pendingAgreements.size());
        assertTrue(pendingAgreements.stream().allMatch(a -> a.getStatus() == Agreement.AgreementStatus.PENDING));
        logger.debug("Assertions passed");
    }
}
