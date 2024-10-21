package com.grantmanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.grantmanagement.dto.GrantDTO;
import com.grantmanagement.service.GrantService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActiveProfiles("test")
class GrantControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(GrantControllerTest.class);

    @Mock
    private GrantService grantService;

    @InjectMocks
    private GrantController grantController;

    @BeforeEach
    void setUp() {
        logger.info("Setting up GrantControllerTest");
        MockitoAnnotations.openMocks(this);
        logger.debug("Mocks initialized: grantService={}, grantController={}", grantService, grantController);
    }

    private GrantDTO createSampleGrantDTO() {
        logger.debug("Creating sample GrantDTO");
        GrantDTO grantDTO = new GrantDTO(1L, "Test Grant", "Description", BigDecimal.valueOf(1000), "Legal Text",
                            true, 30, LocalDate.now(), LocalDate.now().plusMonths(6), GrantDTO.GrantStatus.ACTIVE);
        logger.debug("Created sample GrantDTO: {}", grantDTO);
        return grantDTO;
    }

    @Test
    void testCreateGrant() {
        logger.info("Starting testCreateGrant");
        GrantDTO grantDTO = createSampleGrantDTO();
        when(grantService.createGrant(any(GrantDTO.class))).thenReturn(grantDTO);

        ResponseEntity<GrantDTO> response = grantController.createGrant(grantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testCreateGrant successfully");
    }

    @Test
    void testGetGrantById() {
        logger.info("Starting testGetGrantById");
        GrantDTO grantDTO = createSampleGrantDTO();
        when(grantService.getGrantById(1L)).thenReturn(Optional.of(grantDTO));

        ResponseEntity<GrantDTO> response = grantController.getGrantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testGetGrantById successfully");
    }

    @Test
    void testGetAllGrants() {
        logger.info("Starting testGetAllGrants");
        List<GrantDTO> grants = Arrays.asList(createSampleGrantDTO(), createSampleGrantDTO());
        when(grantService.getAllGrants()).thenReturn(grants);

        ResponseEntity<List<GrantDTO>> response = grantController.getAllGrants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        logger.info("Completed testGetAllGrants successfully");
    }

    @Test
    void testUpdateGrant() {
        logger.info("Starting testUpdateGrant");
        GrantDTO grantDTO = createSampleGrantDTO();
        when(grantService.updateGrant(eq(1L), any(GrantDTO.class))).thenReturn(grantDTO);

        ResponseEntity<GrantDTO> response = grantController.updateGrant(1L, grantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        logger.info("Completed testUpdateGrant successfully");
    }

    @Test
    void testDeleteGrant() {
        logger.info("Starting testDeleteGrant");
        doNothing().when(grantService).deleteGrant(1L);

        ResponseEntity<Void> response = grantController.deleteGrant(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(grantService).deleteGrant(1L);
        logger.info("Completed testDeleteGrant successfully");
    }

    @Test
    void testSearchGrants() {
        logger.info("Starting testSearchGrants");
        List<GrantDTO> grants = Arrays.asList(createSampleGrantDTO(), createSampleGrantDTO());
        when(grantService.searchGrants(anyString())).thenReturn(grants);

        ResponseEntity<List<GrantDTO>> response = grantController.searchGrants("Test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        logger.info("Completed testSearchGrants successfully");
    }

    @Test
    void testGetTotalGrants() {
        logger.info("Starting testGetTotalGrants");
        when(grantService.getTotalGrants()).thenReturn(5L);

        ResponseEntity<Long> response = grantController.getTotalGrants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
        logger.info("Completed testGetTotalGrants successfully");
    }
}
