package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.DealerAnalytics;
import com.example.DealerFlow.Service.DealerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dealer")
public class DealerController {

    private static final Logger log = LoggerFactory.getLogger(DealerController.class);

    private final DealerService dealerService;

    public DealerController(DealerService dealerService) {
        this.dealerService = dealerService;
    }

    @GetMapping("/analytics/{dealerCode}")
    public ResponseEntity<DealerAnalytics> getAnalytics(@PathVariable String dealerCode) {

        log.info("GET /dealer/analytics/{}", dealerCode);

        DealerAnalytics result = dealerService.buildAnalyticsForDealer(dealerCode);

        log.info("Returning {} services for dealer {}", result.getTopServices().size(), dealerCode);

        return ResponseEntity.ok(result);
    }
}
