package com.grantmanagement.service;

import com.grantmanagement.dto.GrantDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GrantService {
    GrantDTO createGrant(GrantDTO grantDTO);
    Optional<GrantDTO> getGrantById(Long id);
    List<GrantDTO> getAllGrants();
    GrantDTO updateGrant(Long id, GrantDTO grantDetails);
    void deleteGrant(Long id);
    List<GrantDTO> searchGrants(String keyword);
    long getTotalGrants();
    List<GrantDTO> getGrantsByStatus(GrantDTO.GrantStatus status);
}
