package com.example.DealerFlow.Service;

import com.example.DealerFlow.Client.PythonApiClient;
import com.example.DealerFlow.Dto.ConsultResponseDto;
import com.example.DealerFlow.Dto.TopLeadsResponseDto;
import com.example.DealerFlow.Exception.PythonApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultServiceTest {

    @Mock
    private PythonApiClient pythonApiClient;

    @InjectMocks
    private ConsultService consultService;

    @Test
    void devePreservarStatusEConverterPropensityScoreParaPercentual() {
        when(pythonApiClient.getById(1L)).thenReturn(consultResponse());

        ConsultResponseDto result = consultService.getById(1L);

        assertThat(result.getStatus()).isEqualTo("success (via database)");
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getVinHash()).isEqualTo("6ab46c8486b5724f108c8c941d49755f88892d6d84fedeb7878b9f26d28d2cb6");
        assertThat(result.getPropensityScore()).isEqualByComparingTo("1.2735");
    }

    @Test
    void deveSerializarStatusEApenasOsTresCamposDaConsulta() {
        when(pythonApiClient.getByMaintenanceId(143131390L)).thenReturn(consultResponse());

        ConsultResponseDto result = consultService.getByMaintenanceId(143131390L);

        String json = new ObjectMapper().writeValueAsString(result);

        assertThat(json)
                .contains("\"status\":\"success (via database)\"")
                .contains("\"ID\":1")
                .contains("\"VIN_Hash\":\"6ab46c8486b5724f108c8c941d49755f88892d6d84fedeb7878b9f26d28d2cb6\"")
                .contains("\"propensity_score\":1.2735")
                .doesNotContain("Country")
                .doesNotContain("ScheduleID");
    }

    @Test
    void deveRejeitarRespostaSemData() {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("status", "success");

        when(pythonApiClient.getByMaintenanceId(143131390L)).thenReturn(response);

        assertThatThrownBy(() -> consultService.getByMaintenanceId(143131390L))
                .isInstanceOf(PythonApiException.class)
                .hasMessageContaining("data object");
    }

    @Test
    void devePermitirPropensityScoreNulo() {
        ObjectNode response = consultResponse();
        ((ObjectNode) response.get("data")).putNull("propensity_score");

        when(pythonApiClient.getByVinHash("hash")).thenReturn(response);

        ConsultResponseDto result = consultService.getByVinHash("hash");

        assertThat(result.getPropensityScore()).isNull();
    }

    @Test
    void deveProcessarTodosOsTopLeads() {
        when(pythonApiClient.getTopLeads(10L)).thenReturn(topLeadsResponse());

        TopLeadsResponseDto result = consultService.getTopLeads(10L);

        assertThat(result.getStatus()).isEqualTo("success");
        assertThat(result.getTopLeads()).hasSize(2);
        assertThat(result.getTopLeads().get(0).getId()).isEqualTo(240713L);
        assertThat(result.getTopLeads().get(0).getPropensityScore())
                .isEqualByComparingTo("74.6119");
        assertThat(result.getTopLeads().get(1).getId()).isEqualTo(347127L);
        assertThat(result.getTopLeads().get(1).getPropensityScore())
                .isEqualByComparingTo("74.3288");
    }

    @Test
    void deveAceitarTopLeadsVazio() {
        ObjectNode response = JsonNodeFactory.instance.objectNode();
        response.put("status", "success");
        response.putArray("top_leads");

        when(pythonApiClient.getTopLeads(10L)).thenReturn(response);

        TopLeadsResponseDto result = consultService.getTopLeads(10L);

        assertThat(result.getTopLeads()).isEmpty();
    }

    @Test
    void deveSerializarTopLeadsComStatusEUmaPropriedadePorLead() {
        when(pythonApiClient.getTopLeads(10L)).thenReturn(topLeadsResponse());

        TopLeadsResponseDto result = consultService.getTopLeads(10L);

        String json = new ObjectMapper().writeValueAsString(result);

        assertThat(json)
                .contains("\"status\":\"success\"")
                .contains("\"top_leads\"")
                .contains("\"ID\":240713")
                .contains("\"propensity_score\":74.6119")
                .doesNotContain("Country")
                .doesNotContain("MaintenanceID");
    }

    private ObjectNode consultResponse() {
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.put("status", "success (via database)");
        ObjectNode data = root.putObject("data");
        addSummaryFields(data, 1L,
                "6ab46c8486b5724f108c8c941d49755f88892d6d84fedeb7878b9f26d28d2cb6",
                0.012735);
        return root;
    }

    private ObjectNode topLeadsResponse() {
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.put("status", "success");
        ArrayNode topLeads = root.putArray("top_leads");
        ObjectNode first = topLeads.addObject();
        addSummaryFields(first, 240713L,
                "9d15013d777bce160aa9a7307db09bc2f51d33b2d7a9abb90f69d7c0e1554444",
                0.746119);
        ObjectNode second = topLeads.addObject();
        addSummaryFields(second, 347127L,
                "a75155fe0728c6cfe54af8227ca566168d7aa65980cd69c9b3d08fc3a474d350",
                0.743288);
        return root;
    }

    private void addSummaryFields(ObjectNode data, long id, String vinHash, double score) {
        data.put("ID", id);
        data.put("Country", 0);
        data.put("VIN_Hash", vinHash);
        data.put("propensity_score", score);
    }
}
