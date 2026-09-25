package com.example.DealerFlow.Client;

import com.example.DealerFlow.Exception.PythonApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.JsonNode;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.TimeoutException;

@Component
public class PythonApiClient {

    private final RestClient restClient;

    public PythonApiClient(@Qualifier("pythonApiRestClient") RestClient pythonApiRestClient) {
        this.restClient = pythonApiRestClient;
    }

    public JsonNode getById(long number) {
        return execute(restClient.get().uri("/consult/ID/{number}", number));
    }

    public JsonNode getByMaintenanceId(long maintenanceId) {
        return execute(restClient.get().uri("/consult/MaintenanceID/{maintenanceId}", maintenanceId));
    }

    public JsonNode getByVinHash(String vinHash) {
        return execute(restClient.get().uri("/consult/VIN_Hash/{vinHash}", vinHash));
    }

    public JsonNode getTopLeads(long qtf) {
        return execute(restClient.get().uri(uriBuilder -> uriBuilder
                .path("/top-leads")
                .queryParam("qtf", qtf)
                .build()));
    }

    private JsonNode execute(RestClient.RequestHeadersSpec<?> request) {
        try {
            JsonNode response = request
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                throw new PythonApiException(
                        HttpStatus.BAD_GATEWAY,
                        "Python API returned an empty response"
                );
            }

            return response;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                throw new PythonApiException(
                        HttpStatus.NOT_FOUND,
                        "Python API resource was not found",
                        ex
                );
            }

            throw new PythonApiException(
                    HttpStatus.BAD_GATEWAY,
                    "Python API returned an HTTP error",
                    ex
            );
        } catch (ResourceAccessException ex) {
            boolean timeout = isTimeout(ex);
            HttpStatus status = timeout
                    ? HttpStatus.GATEWAY_TIMEOUT
                    : HttpStatus.BAD_GATEWAY;
            String message = timeout
                    ? "Python API request timed out"
                    : "Python API is unavailable";

            throw new PythonApiException(status, message, ex);
        } catch (RestClientException ex) {
            throw new PythonApiException(
                    HttpStatus.BAD_GATEWAY,
                    "Python API returned an invalid response",
                    ex
            );
        }
    }

    private boolean isTimeout(Throwable throwable) {
        Throwable current = throwable;

        while (current != null) {
            if (current instanceof SocketTimeoutException
                    || current instanceof HttpTimeoutException
                    || current instanceof TimeoutException) {
                return true;
            }
            current = current.getCause();
        }

        return false;
    }
}
