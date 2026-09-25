package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.ConsultResponseDto;
import com.example.DealerFlow.Dto.TopLeadsResponseDto;
import com.example.DealerFlow.Exception.ConsultInputException;
import com.example.DealerFlow.Service.ConsultService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultControllerTest {

    @Mock
    private ConsultService consultService;

    @InjectMocks
    private ConsultController consultController;

    @Test
    void deveRetornarConsultaPorId() {
        ConsultResponseDto expected = response();
        when(consultService.getById(1L)).thenReturn(expected);

        ResponseEntity<ConsultResponseDto> response = consultController.getById(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void deveRetornarConsultaPorMaintenanceId() {
        ConsultResponseDto expected = response();
        when(consultService.getByMaintenanceId(143131390L)).thenReturn(expected);

        ResponseEntity<ConsultResponseDto> response = consultController.getByMaintenanceId(143131390L);

        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void deveRetornarConsultaPorVinHash() {
        ConsultResponseDto expected = response();
        when(consultService.getByVinHash("hash")).thenReturn(expected);

        ResponseEntity<ConsultResponseDto> response = consultController.getByVinHash("hash");

        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void deveRetornarTopLeads() {
        TopLeadsResponseDto expected = new TopLeadsResponseDto("success", List.of());
        when(consultService.getTopLeads(10L)).thenReturn(expected);

        ResponseEntity<TopLeadsResponseDto> response = consultController.getTopLeads(10L);

        assertThat(response.getBody()).isSameAs(expected);
    }

    @Test
    void deveRejeitarNumeroNaoPositivo() {
        assertThatThrownBy(() -> consultController.getById(0L))
                .isInstanceOf(ConsultInputException.class)
                .hasMessageContaining("greater than zero");
    }

    private ConsultResponseDto response() {
        return new ConsultResponseDto(
                "success (via database)",
                1L,
                "6ab46c8486b5724f108c8c941d49755f88892d6d84fedeb7878b9f26d28d2cb6",
                new BigDecimal("1.2735")
        );
    }
}
