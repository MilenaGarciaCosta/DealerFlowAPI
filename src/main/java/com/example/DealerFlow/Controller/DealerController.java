package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.DealerAnalytics;
import com.example.DealerFlow.Service.DealerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
public class DealerController {

    private static final Logger log = LoggerFactory.getLogger(DealerController.class);

    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllDealerCodes(){
        List<String> allDealerCodes = dealerService.getAllDealerCodes();

        return ResponseEntity.ok(allDealerCodes);
    }

    @GetMapping("/{dealerCode}")
    public ResponseEntity<DealerAnalytics> getAnalytics(@PathVariable String dealerCode,
                                                        @AuthenticationPrincipal UserDetails principal) {

        log.info("GET /dealer/analytics/{}", dealerCode);

        DealerAnalytics result = dealerService.buildAnalyticsForDealer(dealerCode);

        log.info("Returning {} services for dealer {}", result.getTopServices().size(), dealerCode);

        return ResponseEntity.ok(result);
    }
}