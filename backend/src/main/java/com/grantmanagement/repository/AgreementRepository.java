package com.grantmanagement.repository;

import com.grantmanagement.model.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    List<Agreement> findByParticipantId(Long participantId);
    List<Agreement> findByGrantId(Long grantId);
}
