package com.grantmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.model.Grant;
import com.grantmanagement.repository.GrantRepository;
import com.grantmanagement.service.impl.GrantServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrantServiceTest {

    @Mock
    private GrantRepository grantRepository;

    @InjectMocks
    private GrantServiceImpl grantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Grant createSampleGrant() {
        Grant grant = new Grant();
        grant.setId(1L);
        grant.setTitle("Sample Grant");
        grant.setDescription("Description");
        grant.setAmount(new BigDecimal("10000.00"));
        grant.setLegalText("Sample legal text");
        grant.setAutoAcceptanceEnabled(true);
        grant.setAutoAcceptanceDays(30);
        grant.setStartDate(LocalDate.now());
        grant.setEndDate(LocalDate.now().plusMonths(6));
        grant.setStatus(Grant.GrantStatus.ACTIVE);
        return grant;
    }

    @Test
    void testCreateGrant() {
        Grant grant = createSampleGrant();
        when(grantRepository.save(any(Grant.class))).thenReturn(grant);

        GrantDTO inputDto = new GrantDTO(null, "Sample Grant", "Description", new BigDecimal("10000.00"),
                "Sample legal text", true, 30, LocalDate.now(), LocalDate.now().plusMonths(6),
                GrantDTO.GrantStatus.ACTIVE);
        GrantDTO result = grantService.createGrant(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(grantRepository).save(any(Grant.class));
    }

    @Test
    void testGetGrantById() {
        Grant grant = createSampleGrant();
        when(grantRepository.findById(1L)).thenReturn(Optional.of(grant));

        Optional<GrantDTO> result = grantService.getGrantById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(grantRepository).findById(1L);
    }

    @Test
    void testGetAllGrants() {
        when(grantRepository.findAll()).thenReturn(Arrays.asList(createSampleGrant(), createSampleGrant()));
        List<GrantDTO> result = grantService.getAllGrants();
        assertEquals(2, result.size());
        verify(grantRepository).findAll();
    }

    @Test
    void testUpdateGrant() {
        Grant existingGrant = createSampleGrant();
        Grant updatedGrant = createSampleGrant();
        updatedGrant.setTitle("Updated Title");

        when(grantRepository.findById(1L)).thenReturn(Optional.of(existingGrant));
        when(grantRepository.save(any(Grant.class))).thenReturn(updatedGrant);

        GrantDTO updatedGrantDTO = new GrantDTO(1L, "Updated Title", "Description", new BigDecimal("10000.00"),
                "Sample legal text", true, 30, LocalDate.now(), LocalDate.now().plusMonths(6),
                GrantDTO.GrantStatus.ACTIVE);

        GrantDTO result = grantService.updateGrant(1L, updatedGrantDTO);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(grantRepository).findById(1L);
        verify(grantRepository).save(any(Grant.class));
    }

    @Test
    void testDeleteGrant() {
        doNothing().when(grantRepository).deleteById(1L);

        grantService.deleteGrant(1L);

        verify(grantRepository).deleteById(1L);
    }

    @Test
    void testGetGrantsByStatus() {
        Grant.GrantStatus status = Grant.GrantStatus.ACTIVE;
        when(grantRepository.findByStatus(status)).thenReturn(Arrays.asList(createSampleGrant(), createSampleGrant()));

        List<GrantDTO> result = grantService.getGrantsByStatus(GrantDTO.GrantStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(grantRepository).findByStatus(status);
    }

    @Test
    void testGetTotalGrants() {
        when(grantRepository.count()).thenReturn(5L);

        long result = grantService.getTotalGrants();

        assertEquals(5L, result);
        verify(grantRepository).count();
    }
}
