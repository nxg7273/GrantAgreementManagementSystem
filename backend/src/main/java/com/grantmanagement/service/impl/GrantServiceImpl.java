package com.grantmanagement.service.impl;

import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.model.Grant;
import com.grantmanagement.repository.GrantRepository;
import com.grantmanagement.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GrantServiceImpl implements GrantService {

    private final GrantRepository grantRepository;

    @Autowired
    public GrantServiceImpl(GrantRepository grantRepository) {
        this.grantRepository = grantRepository;
    }

    @Override
    public GrantDTO createGrant(GrantDTO grantDTO) {
        Grant grant = convertToEntity(grantDTO);
        Grant savedGrant = grantRepository.save(grant);
        return convertToDTO(savedGrant);
    }

    @Override
    public Optional<GrantDTO> getGrantById(Long id) {
        return grantRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<GrantDTO> getAllGrants() {
        return grantRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GrantDTO updateGrant(Long id, GrantDTO grantDetails) {
        Optional<Grant> grant = grantRepository.findById(id);
        if (grant.isPresent()) {
            Grant existingGrant = grant.get();
            existingGrant.setTitle(grantDetails.getTitle());
            existingGrant.setDescription(grantDetails.getDescription());
            existingGrant.setAmount(grantDetails.getAmount());
            existingGrant.setLegalText(grantDetails.getLegalText());
            existingGrant.setAutoAcceptanceEnabled(grantDetails.isAutoAcceptanceEnabled());
            existingGrant.setAutoAcceptanceDays(grantDetails.getAutoAcceptanceDays());
            Grant updatedGrant = grantRepository.save(existingGrant);
            return convertToDTO(updatedGrant);
        }
        return null;
    }

    @Override
    public void deleteGrant(Long id) {
        grantRepository.deleteById(id);
    }

    @Override
    public List<GrantDTO> searchGrants(String keyword) {
        return grantRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalGrants() {
        return grantRepository.count();
    }

    @Override
    public List<GrantDTO> getGrantsByStatus(GrantDTO.GrantStatus status) {
        return grantRepository.findByStatus(Grant.GrantStatus.valueOf(status.name())).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GrantDTO convertToDTO(Grant grant) {
        return new GrantDTO(
            grant.getId(),
            grant.getTitle(),
            grant.getDescription(),
            grant.getAmount(),
            grant.getLegalText(),
            grant.isAutoAcceptanceEnabled(),
            grant.getAutoAcceptanceDays(),
            grant.getStartDate(),
            grant.getEndDate(),
            GrantDTO.GrantStatus.valueOf(grant.getStatus().name())
        );
    }

    private Grant convertToEntity(GrantDTO grantDTO) {
        Grant grant = new Grant();
        grant.setId(grantDTO.getId());
        grant.setTitle(grantDTO.getTitle());
        grant.setDescription(grantDTO.getDescription());
        grant.setAmount(grantDTO.getAmount());
        grant.setLegalText(grantDTO.getLegalText());
        grant.setAutoAcceptanceEnabled(grantDTO.isAutoAcceptanceEnabled());
        grant.setAutoAcceptanceDays(grantDTO.getAutoAcceptanceDays());
        return grant;
    }
}
