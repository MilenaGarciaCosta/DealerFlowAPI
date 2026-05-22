package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.CarModelAnalyticsDto;
import com.example.DealerFlow.Service.CarModelDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/data")
public class CarModelDataController {

    private static final Logger log = LoggerFactory.getLogger(CarModelDataController.class);
    private final CarModelDataService carModelDataService;

    public CarModelDataController(CarModelDataService carModelDataService) {
        this.carModelDataService = carModelDataService;
    }

    @GetMapping("/{model}/{year}")
    public ResponseEntity<Object> getModelDataCount(
            @PathVariable Integer model,
            @PathVariable Integer year) {

        log.info("GET /data/{}/{}", model, year);

        CarModelAnalyticsDto result = carModelDataService.buildAnalyticsForModel(model, year);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Model or year does not exists on database"));
        }

        log.info("Returning: {}", result.getModelName());

        return ResponseEntity.ok(result);
    }
}