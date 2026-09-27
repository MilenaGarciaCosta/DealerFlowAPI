package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.CarModelAnalyticsDto;
import com.example.DealerFlow.Service.CarModelDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/car-data")
@Tag(name = "Dados de veículos", description = "Catálogo de modelos e análises por modelo e ano.")
public class CarModelDataController {

    private static final Logger log = LoggerFactory.getLogger(CarModelDataController.class);
    private final CarModelDataService carModelDataService;

    public CarModelDataController(CarModelDataService carModelDataService) {
        this.carModelDataService = carModelDataService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CanAccessCarModelData')")
    @Operation(summary = "Listar modelos de veículos", description = "Retorna os nomes dos modelos disponíveis. Requer a authority CanAccessCarModelData.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Modelos retornados."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "403", description = "Usuário sem a authority CanAccessCarModelData.")
        })
    public ResponseEntity<List<String>> getAllCarModelNames(){
        List<String> allCarModelNames = carModelDataService.getAllCarModelNames();

        return ResponseEntity.ok(allCarModelNames);
    }

    @GetMapping("/{model}/{year}")
    @PreAuthorize("hasAuthority('CanAccessCarModelData')")
    @Operation(summary = "Consultar análise de modelo", description = "Retorna métricas agregadas para o identificador de modelo e o ano informados. Requer a authority CanAccessCarModelData. Retorna 404 quando o modelo ou ano não existe.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Análise do modelo retornada."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "403", description = "Usuário sem a authority CanAccessCarModelData."),
            @ApiResponse(responseCode = "404", description = "Modelo ou ano não encontrado.")
        })
    public ResponseEntity<Object> getModelDataCount(
            @PathVariable Integer model,
            @PathVariable Integer year) {

        log.info("GET /data/{}/{}", model, year);

        CarModelAnalyticsDto result = carModelDataService.buildAnalyticsForModel(model, year);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Model or year does not exists on database"));
        }

        log.info("Returning car data for car model {}", result.getModelName());

        return ResponseEntity.ok(result);
    }
}