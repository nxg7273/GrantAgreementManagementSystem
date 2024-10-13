package com.grantmanagement.service;

import com.grantmanagement.model.Grant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GrantService {
    Grant createGrant(Grant grant);
    Optional<Grant> getGrantById(Long id);
    List<Grant> getAllGrants();
    Grant updateGrant(Long id, Grant grantDetails);
    void deleteGrant(Long id);
    List<Grant> searchGrants(String keyword);
}
