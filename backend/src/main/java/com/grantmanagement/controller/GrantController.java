package com.grantmanagement.controller;

import com.grantmanagement.model.Grant;
import com.grantmanagement.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grants")
public class GrantController {

    private final GrantService grantService;

    @Autowired
    public GrantController(GrantService grantService) {
        this.grantService = grantService;
    }

    @PostMapping
    public ResponseEntity<Grant> createGrant(@RequestBody Grant grant) {
        Grant createdGrant = grantService.createGrant(grant);
        return ResponseEntity.ok(createdGrant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grant> getGrantById(@PathVariable Long id) {
        return grantService.getGrantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Grant>> getAllGrants() {
        List<Grant> grants = grantService.getAllGrants();
        return ResponseEntity.ok(grants);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grant> updateGrant(@PathVariable Long id, @RequestBody Grant grantDetails) {
        Grant updatedGrant = grantService.updateGrant(id, grantDetails);
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
    public ResponseEntity<List<Grant>> searchGrants(@RequestParam String keyword) {
        List<Grant> grants = grantService.searchGrants(keyword);
        return ResponseEntity.ok(grants);
    }
}
