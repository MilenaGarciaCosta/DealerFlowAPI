package com.example.DealerFlow.Client;

import com.example.DealerFlow.Exception.PythonApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PythonApiClientTest {

    private static final String RESPONSE = """
            {
              "status": "success (via database)",
              "data": {
                "ID": 1,
                "VIN_Hash": "6ab46c8486b5724f108c8c941d49755f88892d6d84fedeb7878b9f26d28d2cb6",
                "propensity_score": 0.012735
              }
            }
            """;

    private MockRestServiceServer server;
    private PythonApiClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://python-api.test");
        server = MockRestServiceServer.bindTo(builder).build();
        client = new PythonApiClient(builder.build());
    }

    @AfterEach
    void verifyRequests() {
        server.verify();
    }

    @Test
    void deveChamarEndpointDeId() {
        server.expect(requestTo("http://python-api.test/consult/ID/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(RESPONSE, MediaType.APPLICATION_JSON));

        assertThat(client.getById(1L).get("data").get("ID").asLong()).isEqualTo(1L);
    }

    @Test
    void deveChamarEndpointDeMaintenanceId() {
        server.expect(requestTo("http://python-api.test/consult/MaintenanceID/143131390"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(RESPONSE, MediaType.APPLICATION_JSON));

        assertThat(client.getByMaintenanceId(143131390L).get("data").get("ID").asLong()).isEqualTo(1L);
    }

    @Test
    void deveChamarEndpointDeVinHash() {
        String hash = "6ab46c8486b5724f108c8c941d49755f88892d6d84fedeb7878b9f26d28d2cb6";
        server.expect(requestTo(URI.create("http://python-api.test/consult/VIN_Hash/" + hash)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(RESPONSE, MediaType.APPLICATION_JSON));

        assertThat(client.getByVinHash(hash).get("data").get("ID").asLong()).isEqualTo(1L);
    }

    @Test
    void deveChamarEndpointDeTopLeadsComQueryParameter() {
        server.expect(requestTo(URI.create("http://python-api.test/top-leads?qtf=10")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(RESPONSE, MediaType.APPLICATION_JSON));

        assertThat(client.getTopLeads(10L).get("data").get("ID").asLong()).isEqualTo(1L);
    }

    @Test
    void deveMapearNotFoundDoPythonApi() {
        server.expect(requestTo("http://python-api.test/consult/ID/1"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThatThrownBy(() -> client.getById(1L))
                .isInstanceOf(PythonApiException.class)
                .satisfies(exception -> assertThat(((PythonApiException) exception).getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND));
    }
}
