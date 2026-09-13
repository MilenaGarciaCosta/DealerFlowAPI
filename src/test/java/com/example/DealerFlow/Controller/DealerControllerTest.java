package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.DealerAnalytics;
import com.example.DealerFlow.Service.DealerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealerControllerTest {

    @Mock
    private DealerService dealerService;

    @InjectMocks
    private DealerController dealerController;

    @Test
    void deveRetornarListaDeCodigosComStatus200() {
        when(dealerService.getAllDealerCodes()).thenReturn(List.of("1", "2"));

        ResponseEntity<List<String>> response = dealerController.getAllDealerCodes();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly("1", "2");
    }

    @Test
    void deveRetornarAnalyticsComStatus200() {
        UserDetails mockPrincipal = mock(UserDetails.class);
        DealerAnalytics mockAnalytics = new DealerAnalytics("1", List.of());

        when(dealerService.buildAnalyticsForDealer(anyString())).thenReturn(mockAnalytics);

        ResponseEntity<DealerAnalytics> response = dealerController.getAnalytics("1", mockPrincipal);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getDealerCode()).isEqualTo("1");
    }
}