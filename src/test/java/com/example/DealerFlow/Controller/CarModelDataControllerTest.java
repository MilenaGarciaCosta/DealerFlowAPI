package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.CarModelAnalyticsDto;
import com.example.DealerFlow.Service.CarModelDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarModelDataControllerTest {

    @Mock
    private CarModelDataService carModelDataService;

    @InjectMocks
    private CarModelDataController carModelDataController;

    @Test
    void deveRetornarStatus200EListaDeNomesDeModelos() {
        when(carModelDataService.getAllCarModelNames()).thenReturn(List.of("RANGER", "F-150"));

        ResponseEntity<List<String>> response = carModelDataController.getAllCarModelNames();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly("RANGER", "F-150");
    }

    @Test
    void deveRetornarStatus200EAnalyticsQuandoModeloExistir() {
        CarModelAnalyticsDto mockDto = new CarModelAnalyticsDto(
                1, "RANGER", 2020, 10, 30, 15000, 40.0, 60.0, List.of()
        );
        when(carModelDataService.buildAnalyticsForModel(1, 2020)).thenReturn(mockDto);

        ResponseEntity<Object> response = carModelDataController.getModelDataCount(1, 2020);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(CarModelAnalyticsDto.class);
    }

    @Test
    void deveRetornarStatus404QuandoModeloNaoExistir() {
        when(carModelDataService.buildAnalyticsForModel(99, 2020)).thenReturn(null);

        ResponseEntity<Object> response = carModelDataController.getModelDataCount(99, 2020);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(((Map<?, ?>) response.getBody()).get("message"))
                .isEqualTo("Model or year does not exists on database");
    }
}