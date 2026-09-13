package com.example.DealerFlow.Service;

import com.example.DealerFlow.Dto.CarModelAnalyticsDto;
import com.example.DealerFlow.Repository.CarModelDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarModelDataServiceTest {

    @Mock
    private CarModelDataRepository repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CarModelDataService carModelDataService;

    @Test
    void deveRetornarNullQuandoNaoHouverDadosParaOModeloEAno() {
        when(repository.countByModelIdAndModelYear(1, 2020)).thenReturn(0L);

        CarModelAnalyticsDto result = carModelDataService.buildAnalyticsForModel(1, 2020);

        assertThat(result).isNull();
    }

    @Test
    void deveCalcularPorcentagensCorretamenteAoConstruirAnalytics() {
        when(repository.countByModelIdAndModelYear(1, 2020)).thenReturn(100L);
        when(repository.findModeDaysLastVisit(1, 2020)).thenReturn(30);
        when(repository.findModeKMLastVisit(1, 2020)).thenReturn(15000);
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), anyInt())).thenReturn("RANGER");
        when(repository.countByModelIdAndModelYearAndIsAgendaSchedule(1, 2020, 1)).thenReturn(40L);

        CarModelAnalyticsDto result = carModelDataService.buildAnalyticsForModel(1, 2020);

        assertThat(result).isNotNull();
        assertThat(result.getModelName()).isEqualTo("RANGER");
        assertThat(result.getScheduledPercentage()).isEqualTo(40.0);
        assertThat(result.getNotScheduledPercentage()).isEqualTo(60.0);
    }

    @Test
    void deveRetornarModeloDesconhecidoQuandoOcorrerExcecaoNoBanco() {
        when(jdbcTemplate.queryForObject(anyString(), eq(String.class), anyInt()))
                .thenThrow(new EmptyResultDataAccessException(1));

        String modelName = carModelDataService.getModelNameById(999);

        assertThat(modelName).isEqualTo("Modelo Desconhecido");
    }
}