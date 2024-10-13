package com.grantmanagement.repository;

import com.grantmanagement.model.Grant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrantRepository extends JpaRepository<Grant, Long> {
    List<Grant> findByNameContainingOrDescriptionContaining(String name, String description);
}
