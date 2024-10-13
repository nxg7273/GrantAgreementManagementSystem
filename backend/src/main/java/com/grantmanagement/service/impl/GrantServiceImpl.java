package com.grantmanagement.service.impl;

import com.grantmanagement.model.Grant;
import com.grantmanagement.repository.GrantRepository;
import com.grantmanagement.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrantServiceImpl implements GrantService {

    private final GrantRepository grantRepository;

    @Autowired
    public GrantServiceImpl(GrantRepository grantRepository) {
        this.grantRepository = grantRepository;
    }

    @Override
    public Grant createGrant(Grant grant) {
        return grantRepository.save(grant);
    }

    @Override
    public Optional<Grant> getGrantById(Long id) {
        return grantRepository.findById(id);
    }

    @Override
    public List<Grant> getAllGrants() {
        return grantRepository.findAll();
    }

    @Override
    public Grant updateGrant(Long id, Grant grantDetails) {
        Optional<Grant> grant = grantRepository.findById(id);
        if (grant.isPresent()) {
            Grant existingGrant = grant.get();
            existingGrant.setName(grantDetails.getName());
            existingGrant.setDescription(grantDetails.getDescription());
            existingGrant.setAmount(grantDetails.getAmount());
            existingGrant.setLegalText(grantDetails.getLegalText());
            return grantRepository.save(existingGrant);
        }
        return null;
    }

    @Override
    public void deleteGrant(Long id) {
        grantRepository.deleteById(id);
    }

    @Override
    public List<Grant> searchGrants(String keyword) {
        return grantRepository.findByNameContainingOrDescriptionContaining(keyword, keyword);
    }
}
