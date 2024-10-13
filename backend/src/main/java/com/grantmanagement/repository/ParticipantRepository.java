package com.grantmanagement.repository;

import com.grantmanagement.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByNameContainingOrEmailContainingOrOrganizationContaining(String name, String email, String organization);
    Optional<Participant> findByEmail(String email);
    List<Participant> findByOrganization(String organization);
}
