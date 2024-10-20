package com.grantmanagement.service;

import com.grantmanagement.model.Grant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GrantService {
    Grant createGrant(Grant grant);
    Grant updateGrant(Long id, Grant grant);
    Grant getGrantById(Long id);
    List<Grant> getAllGrants();
    void deleteGrant(Long id);
}
