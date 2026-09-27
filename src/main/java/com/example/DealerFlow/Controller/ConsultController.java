package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.ConsultResponseDto;
import com.example.DealerFlow.Dto.TopLeadsResponseDto;
import com.example.DealerFlow.Exception.ConsultInputException;
import com.example.DealerFlow.Service.ConsultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Consultas e leads", description = "Consultas de propensão e listagem dos principais leads.")
public class ConsultController {

    private final ConsultService consultService;

    public ConsultController(ConsultService consultService) {
        this.consultService = consultService;
    }

    @GetMapping("/consult/ID/{number}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consultar por ID", description = "Busca os dados de propensão pelo identificador numérico. O valor deve ser maior que zero.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro localizado."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "500", description = "Erro interno. Atualmente, entradas inválidas também são tratadas pelo handler genérico como erro interno.")
        })
    public ResponseEntity<ConsultResponseDto> getById(@PathVariable("number") Long number) {
        validatePositive(number, "number");
        return ResponseEntity.ok(consultService.getById(number));
    }

    @GetMapping("/consult/MaintenanceID/{maintenanceId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consultar por ID de manutenção", description = "Busca os dados de propensão pelo identificador numérico da manutenção, maior que zero.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro localizado."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "500", description = "Erro interno. Atualmente, entradas inválidas também são tratadas pelo handler genérico como erro interno.")
        })
    public ResponseEntity<ConsultResponseDto> getByMaintenanceId(
            @PathVariable("maintenanceId") Long maintenanceId) {
        validatePositive(maintenanceId, "maintenanceId");
        return ResponseEntity.ok(consultService.getByMaintenanceId(maintenanceId));
    }

    @GetMapping("/consult/VIN_Hash/{vinHash}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consultar por hash do VIN", description = "Busca os dados de propensão pelo hash do VIN, que não pode estar vazio.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro localizado."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "500", description = "Erro interno. Atualmente, entradas inválidas também são tratadas pelo handler genérico como erro interno.")
        })
    public ResponseEntity<ConsultResponseDto> getByVinHash(
            @PathVariable("vinHash") String vinHash) {
        validateHash(vinHash);
        return ResponseEntity.ok(consultService.getByVinHash(vinHash));
    }

    @GetMapping("/top-leads")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Listar principais leads", description = "Retorna a quantidade solicitada de leads ordenados pelo serviço de consulta. O parâmetro qtf deve ser maior que zero.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de leads retornada."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "500", description = "Erro interno. Atualmente, entradas inválidas também são tratadas pelo handler genérico como erro interno.")
        })
    public ResponseEntity<TopLeadsResponseDto> getTopLeads(@RequestParam("qtf") Long qtf) {
        validatePositive(qtf, "qtf", 200L);
        return ResponseEntity.ok(consultService.getTopLeads(qtf));
    }

    private void validatePositive(Long value, String parameter) {
        validatePositive(value, parameter, null);
    }

    private void validatePositive(Long value, String parameter, Long max) {
        if (value == null || value <= 0) {
            throw new ConsultInputException(parameter + " must be greater than zero");
        }
        if (max != null && value > max) {
            throw new ConsultInputException(parameter + " Parameter value very high");
        }
    }

    private void validateHash(String vinHash) {
        if (vinHash == null || vinHash.isBlank()) {
            throw new ConsultInputException("vinHash must not be blank");
        }
    }
}
