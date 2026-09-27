package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.DealerAnalytics;
import com.example.DealerFlow.Service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
@Tag(name = "Concessionárias", description = "Códigos de concessionárias e indicadores de desempenho.")
public class DealerController {

    private static final Logger log = LoggerFactory.getLogger(DealerController.class);

    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CanViewAnalytics')")
    @Operation(summary = "Listar códigos de concessionárias", description = "Retorna os códigos cadastrados. Requer a authority CanViewAnalytics.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Códigos retornados."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "403", description = "Usuário sem a authority CanViewAnalytics.")
        })
    public ResponseEntity<List<String>> getAllDealerCodes(){
        List<String> allDealerCodes = dealerService.getAllDealerCodes();

        return ResponseEntity.ok(allDealerCodes);
    }

    @GetMapping("/{dealerCode}")
    @PreAuthorize("hasAuthority('CanAccessDealer')")
    @Operation(summary = "Consultar análise da concessionária", description = "Retorna indicadores e principais serviços do código informado. Requer a authority CanAccessDealer.", security = @SecurityRequirement(name = "BearerAuth"))
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Análise da concessionária retornada."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "403", description = "Usuário sem a authority CanAccessDealer.")
        })
    public ResponseEntity<DealerAnalytics> getAnalytics(@PathVariable String dealerCode,
                                                        @AuthenticationPrincipal UserDetails principal) {

        log.info("GET /dealer/analytics/{}", dealerCode);

        DealerAnalytics result = dealerService.buildAnalyticsForDealer(dealerCode);

        log.info("Returning {} services for dealer {}", result.getTopServices().size(), dealerCode);

        return ResponseEntity.ok(result);
    }
}