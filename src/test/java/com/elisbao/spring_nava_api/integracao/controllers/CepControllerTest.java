package com.elisbao.spring_nava_api.integracao.controllers;

import com.elisbao.spring_nava_api.integracao.AbstractIntegrationTest;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.spring.EnableWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnableWireMock
class CepControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @Test
    void testBuscarCep_Success() throws Exception {
        String cep = "12345678";
        String mockResponseBody = "{ \"cep\": \"12345678\", \"logradouro\": \"Rua Teste\", \"bairro\": \"Bairro Teste\", \"localidade\": \"Cidade Teste\", \"uf\": \"SP\" }";

        // Configura o WireMock para interceptar a requisição GET
        wireMockServer.stubFor(WireMock.get("/12345678")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

        mockMvc.perform(get("/cep/{cep}", cep)
                        .headers(HEADERS)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("12345678"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste"))
                .andExpect(jsonPath("$.bairro").value("Bairro Teste"))
                .andExpect(jsonPath("$.localidade").value("Cidade Teste"))
                .andExpect(jsonPath("$.uf").value("SP"));
    }

    @Test
    void testBuscarCep_NotFound() throws Exception {
        String cep = "00000000";

        wireMockServer.stubFor(WireMock.get("/00000000")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"erro\": \"true\" }")));

        mockMvc.perform(get("/cep/{cep}", cep)
                        .headers(HEADERS)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value(nullValue()))
                .andExpect(jsonPath("$.logradouro").value(nullValue()))
                .andExpect(jsonPath("$.bairro").value(nullValue()))
                .andExpect(jsonPath("$.localidade").value(nullValue()))
                .andExpect(jsonPath("$.uf").value(nullValue()));
    }
}
