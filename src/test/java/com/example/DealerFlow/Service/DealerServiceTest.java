package com.example.DealerFlow.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealerServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DealerService dealerService;

    @Test
    void deveRetornarVerdadeiroQuandoDealerForValido() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyString())).thenReturn(1);

        boolean isValid = dealerService.isDealerValid("DEALER_123");

        assertThat(isValid).isTrue();
    }

    @Test
    void deveRetornarFalsoQuandoDealerEstiverVazioOuNulo() {
        boolean isNullValid = dealerService.isDealerValid(null);
        boolean isEmptyValid = dealerService.isDealerValid("   ");

        assertThat(isNullValid).isFalse();
        assertThat(isEmptyValid).isFalse();
    }

    @Test
    void deveRetornarListaDeCodigosDeDealer() {
        when(jdbcTemplate.queryForList(anyString(), eq(String.class))).thenReturn(List.of("1", "2"));

        List<String> codes = dealerService.getAllDealerCodes();

        assertThat(codes).hasSize(2).contains("1", "2");
    }
}