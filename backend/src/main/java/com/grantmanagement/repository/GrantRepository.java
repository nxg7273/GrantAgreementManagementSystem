package com.grantmanagement.repository;

import com.grantmanagement.model.Grant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;

@Repository
public interface GrantRepository extends JpaRepository<Grant, Long> {
    List<Grant> findByTitleContainingOrDescriptionContaining(String title, String description);
    List<Grant> findByStatus(Grant.GrantStatus status);
    List<Grant> findByTitle(String title);
    List<Grant> findByAmountGreaterThan(BigDecimal amount);
}
