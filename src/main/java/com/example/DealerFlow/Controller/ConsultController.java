package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.ConsultResponseDto;
import com.example.DealerFlow.Dto.TopLeadsResponseDto;
import com.example.DealerFlow.Exception.ConsultInputException;
import com.example.DealerFlow.Service.ConsultService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsultController {

    private final ConsultService consultService;

    public ConsultController(ConsultService consultService) {
        this.consultService = consultService;
    }

    @GetMapping("/consult/ID/{number}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConsultResponseDto> getById(@PathVariable("number") Long number) {
        validatePositive(number, "number");
        return ResponseEntity.ok(consultService.getById(number));
    }

    @GetMapping("/consult/MaintenanceID/{maintenanceId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConsultResponseDto> getByMaintenanceId(
            @PathVariable("maintenanceId") Long maintenanceId) {
        validatePositive(maintenanceId, "maintenanceId");
        return ResponseEntity.ok(consultService.getByMaintenanceId(maintenanceId));
    }

    @GetMapping("/consult/VIN_Hash/{vinHash}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ConsultResponseDto> getByVinHash(
            @PathVariable("vinHash") String vinHash) {
        validateHash(vinHash);
        return ResponseEntity.ok(consultService.getByVinHash(vinHash));
    }

    @GetMapping("/top-leads")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TopLeadsResponseDto> getTopLeads(@RequestParam("qtf") Long qtf) {
        validatePositive(qtf, "qtf");
        return ResponseEntity.ok(consultService.getTopLeads(qtf));
    }

    private void validatePositive(Long value, String parameter) {
        if (value == null || value <= 0) {
            throw new ConsultInputException(parameter + " must be greater than zero");
        }
    }

    private void validateHash(String vinHash) {
        if (vinHash == null || vinHash.isBlank()) {
            throw new ConsultInputException("vinHash must not be blank");
        }
    }
}
