package com.grantmanagement.repository;

import com.grantmanagement.model.Participant;
import com.grantmanagement.model.Agreement;
import com.grantmanagement.model.AgreementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByNameContainingOrEmailContainingOrOrganizationContaining(String name, String email, String organization);
    Optional<Participant> findByEmail(String email);
    List<Participant> findByOrganization(String organization);
    List<Participant> findByName(String name);
    List<Participant> findByGrantId(Long grantId);

    @Query("SELECT COUNT(DISTINCT p.grant.id) FROM Participant p WHERE p.id = :participantId")
    long countDistinctGrantsByParticipantId(@Param("participantId") Long participantId);

    @Query("SELECT COUNT(a) FROM Participant p JOIN p.agreements a WHERE p.id = :participantId")
    long countAgreementsByParticipantId(@Param("participantId") Long participantId);

    @Query("SELECT COUNT(a) FROM Participant p JOIN p.agreements a WHERE p.id = :participantId AND a.status = :status")
    long countAgreementsByParticipantIdAndStatus(@Param("participantId") Long participantId, @Param("status") AgreementStatus status);

    @Query("SELECT a FROM Participant p JOIN p.agreements a WHERE p.id = :participantId ORDER BY a.createdAt DESC")
    Optional<Agreement> findLatestAgreementByParticipantId(@Param("participantId") Long participantId);
}
