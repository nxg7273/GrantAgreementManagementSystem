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

@ActiveProfiles("test")
class GrantControllerTest {

    @Mock
    private GrantService grantService;

    @InjectMocks
    private GrantController grantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private GrantDTO createSampleGrantDTO() {
        return new GrantDTO(1L, "Test Grant", "Description", BigDecimal.valueOf(1000), "Legal Text",
                            true, 30, LocalDate.now(), LocalDate.now().plusMonths(6), GrantDTO.GrantStatus.ACTIVE);
    }

    @Test
    void testCreateGrant() {
        GrantDTO grantDTO = createSampleGrantDTO();
        when(grantService.createGrant(any(GrantDTO.class))).thenReturn(grantDTO);

        ResponseEntity<GrantDTO> response = grantController.createGrant(grantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetGrantById() {
        GrantDTO grantDTO = createSampleGrantDTO();
        when(grantService.getGrantById(1L)).thenReturn(Optional.of(grantDTO));

        ResponseEntity<GrantDTO> response = grantController.getGrantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetAllGrants() {
        List<GrantDTO> grants = Arrays.asList(createSampleGrantDTO(), createSampleGrantDTO());
        when(grantService.getAllGrants()).thenReturn(grants);

        ResponseEntity<List<GrantDTO>> response = grantController.getAllGrants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testUpdateGrant() {
        GrantDTO grantDTO = createSampleGrantDTO();
        when(grantService.updateGrant(eq(1L), any(GrantDTO.class))).thenReturn(grantDTO);

        ResponseEntity<GrantDTO> response = grantController.updateGrant(1L, grantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testDeleteGrant() {
        doNothing().when(grantService).deleteGrant(1L);

        ResponseEntity<Void> response = grantController.deleteGrant(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(grantService).deleteGrant(1L);
    }

    @Test
    void testSearchGrants() {
        List<GrantDTO> grants = Arrays.asList(createSampleGrantDTO(), createSampleGrantDTO());
        when(grantService.searchGrants(anyString())).thenReturn(grants);

        ResponseEntity<List<GrantDTO>> response = grantController.searchGrants("Test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetTotalGrants() {
        when(grantService.getTotalGrants()).thenReturn(5L);

        ResponseEntity<Long> response = grantController.getTotalGrants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }
}
