package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.DealerAnalyticsRequest;
import com.example.DealerFlow.Dto.DealerAnalyticsResponse;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Service.DealerService;
import com.example.DealerFlow.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dealer")
public class DealerController {

    private final DealerService dealerService;
    private final UserService userService;

    public DealerController(DealerService dealerService, UserService userService) {
        this.dealerService = dealerService;
        this.userService = userService;
    }

    @PostMapping("/analytics")
    public ResponseEntity<DealerAnalyticsResponse> getAnalytics(
            @RequestBody(required = false) DealerAnalyticsRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        List<String> dealerCodes = (request != null) ? request.getDealerCodes() : null;

        if (dealerCodes == null || dealerCodes.isEmpty()) {
            User user = userService.findByEmail(principal.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            if ("manager".equalsIgnoreCase(user.getCategory()) && user.getDealer() != null) {
                dealerCodes = List.of(user.getDealer());
            } else {
                dealerCodes = dealerService.getAllDealerCodes();
            }
        }

        DealerAnalyticsResponse response = dealerService.buildAnalytics(dealerCodes);
        return ResponseEntity.ok(response);
    }
}
