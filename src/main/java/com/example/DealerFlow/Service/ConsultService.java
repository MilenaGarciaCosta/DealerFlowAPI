package com.example.DealerFlow.Service;

import com.example.DealerFlow.Client.PythonApiClient;
import com.example.DealerFlow.Dto.ConsultResponseDto;
import com.example.DealerFlow.Dto.ConsultSummaryDto;
import com.example.DealerFlow.Dto.TopLeadsResponseDto;
import com.example.DealerFlow.Exception.PythonApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultService {

    private static final BigDecimal PERCENTAGE_MULTIPLIER = BigDecimal.valueOf(100);
    private static final int PERCENTAGE_SCALE = 4;

    private final PythonApiClient pythonApiClient;

    public ConsultService(PythonApiClient pythonApiClient) {
        this.pythonApiClient = pythonApiClient;
    }

    public ConsultResponseDto getById(long number) {
        return toResponse(pythonApiClient.getById(number));
    }

    public ConsultResponseDto getByMaintenanceId(long maintenanceId) {
        return toResponse(pythonApiClient.getByMaintenanceId(maintenanceId));
    }

    public ConsultResponseDto getByVinHash(String vinHash) {
        return toResponse(pythonApiClient.getByVinHash(vinHash));
    }

    public TopLeadsResponseDto getTopLeads(long qtf) {
        return toTopLeadsResponse(pythonApiClient.getTopLeads(qtf));
    }

    private ConsultResponseDto toResponse(JsonNode response) {
        String status = extractStatus(response);
        JsonNode data = response.get("data");

        if (data == null || !data.isObject()) {
            throw invalidResponse("the data object is missing");
        }

        ConsultSummaryDto summary = toSummary(data);
        return new ConsultResponseDto(
                status,
                summary.getId(),
                summary.getVinHash(),
                summary.getPropensityScore()
        );
    }

    private TopLeadsResponseDto toTopLeadsResponse(JsonNode response) {
        String status = extractStatus(response);
        JsonNode topLeads = response.get("top_leads");

        if (topLeads == null || !topLeads.isArray()) {
            throw invalidResponse("the top_leads array is missing");
        }

        List<ConsultSummaryDto> summaries = new ArrayList<>(topLeads.size());
        for (JsonNode lead : topLeads) {
            if (lead == null || !lead.isObject()) {
                throw invalidResponse("top_leads contains an invalid object");
            }
            summaries.add(toSummary(lead));
        }

        return new TopLeadsResponseDto(status, summaries);
    }

    private String extractStatus(JsonNode response) {
        if (response == null || !response.isObject()) {
            throw invalidResponse("the response object is missing");
        }

        JsonNode statusNode = response.get("status");
        if (statusNode == null || statusNode.isNull() || !statusNode.isTextual()
                || statusNode.textValue().isBlank()) {
            throw invalidResponse("status is missing or invalid");
        }

        return statusNode.textValue();
    }

    private ConsultSummaryDto toSummary(JsonNode data) {
        if (data == null || !data.isObject()) {
            throw invalidResponse("the result object is missing");
        }

        JsonNode idNode = data.get("ID");
        JsonNode vinHashNode = data.get("VIN_Hash");
        JsonNode scoreNode = data.get("propensity_score");

        if (idNode == null || idNode.isNull() || !idNode.isNumber()) {
            throw invalidResponse("ID is missing or invalid");
        }

        long id;
        try {
            id = idNode.decimalValue().longValueExact();
        } catch (ArithmeticException ex) {
            throw invalidResponse("ID is missing or invalid");
        }

        if (vinHashNode == null || vinHashNode.isNull() || !vinHashNode.isTextual()
                || vinHashNode.textValue().isBlank()) {
            throw invalidResponse("VIN_Hash is missing or invalid");
        }

        if (scoreNode == null) {
            throw invalidResponse("propensity_score is missing");
        }

        BigDecimal percentage = null;
        if (!scoreNode.isNull()) {
            if (!scoreNode.isNumber()) {
                throw invalidResponse("propensity_score is invalid");
            }

            percentage = scoreNode.decimalValue()
                    .multiply(PERCENTAGE_MULTIPLIER)
                    .setScale(PERCENTAGE_SCALE, RoundingMode.HALF_UP);
        }

        return new ConsultSummaryDto(
                id,
                vinHashNode.textValue(),
                percentage
        );
    }

    private PythonApiException invalidResponse(String detail) {
        return new PythonApiException(
                HttpStatus.BAD_GATEWAY,
                "Python API returned an invalid response: " + detail
        );
    }
}
