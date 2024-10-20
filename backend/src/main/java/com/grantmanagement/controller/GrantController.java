package com.grantmanagement.controller;

import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/grants")
public class GrantController {

    private static final Logger logger = LoggerFactory.getLogger(GrantController.class);
    private final GrantService grantService;

    @Autowired
    public GrantController(GrantService grantService) {
        this.grantService = grantService;
    }

    @PostMapping
    public ResponseEntity<GrantDTO> createGrant(@RequestBody GrantDTO grantDTO) {
        GrantDTO createdGrant = grantService.createGrant(grantDTO);
        return ResponseEntity.ok(createdGrant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrantDTO> getGrantById(@PathVariable Long id) {
        return grantService.getGrantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<GrantDTO>> getAllGrants() {
        try {
            logger.info("Starting getAllGrants method");
            logger.info("Calling grantService.getAllGrants()");
            List<GrantDTO> grants = grantService.getAllGrants();
            logger.info("Successfully fetched {} grants", grants.size());
            for (GrantDTO grant : grants) {
                logger.debug("Grant: {}", grant);
            }
            logger.info("Returning ResponseEntity with grants");
            return ResponseEntity.ok(grants);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all grants", e);
            logger.error("Exception details: ", e);
            logger.error("Exception message: {}", e.getMessage());
            logger.error("Exception cause: {}", e.getCause());
            logger.error("Stack trace: ", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrantDTO> updateGrant(@PathVariable Long id, @RequestBody GrantDTO grantDetails) {
        GrantDTO updatedGrant = grantService.updateGrant(id, grantDetails);
        if (updatedGrant != null) {
            return ResponseEntity.ok(updatedGrant);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrant(@PathVariable Long id) {
        grantService.deleteGrant(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GrantDTO>> searchGrants(@RequestParam String keyword) {
        List<GrantDTO> grants = grantService.searchGrants(keyword);
        return ResponseEntity.ok(grants);
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalGrants() {
        Long totalGrants = grantService.getTotalGrants();
        return ResponseEntity.ok(totalGrants);
    }
}
