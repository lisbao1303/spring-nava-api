package com.elisbao.spring_nava_api.integracao.controllers;

import com.elisbao.spring_nava_api.integracao.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserAddressControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void testFindAllByUser_Success() throws Exception {
        // Configura o cenário
        String expectedResponse = "[{" +
                "\"cep\":\"12345678\"," +
                "\"logradouro\":\"Rua Teste\"," +
                "\"bairro\":\"Bairro Teste\"," +
                "\"localidade\":\"Cidade Teste\"," +
                "\"uf\":\"SP\"," +
                "\"complemento\":\"Complemento Teste\"," +
                "\"numero\":\"123\"," +
                "\"regiao\":\"Região Teste\"}]";

        // Realiza a chamada e verifica o retorno
        mockMvc.perform(get("/address/user")
                        .headers(HEADERS)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @Order(2)
    void testCreateAddress_Success() throws Exception {

        String requestBody = "{" +
                "\"cep\":\"12345678\"," +
                "\"logradouro\":\"Rua Teste\"," +
                "\"bairro\":\"Bairro Teste\"," +
                "\"localidade\":\"Cidade Teste\"," +
                "\"uf\":\"SP\"," +
                "\"complemento\":\"Apto 101\"," +
                "\"numero\":\"123\"," +
                "\"regiao\":\"Zona Sul\"}";

        mockMvc.perform(post("/address")
                        .headers(HEADERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @Order(3)
    void testUpdateAddress_Success() throws Exception {

        String requestBody = "{" +
                "\"cep\":\"87654321\"," +
                "\"logradouro\":\"Rua Atualizada\"," +
                "\"bairro\":\"Bairro Atualizado\"," +
                "\"localidade\":\"Cidade Atualizada\"," +
                "\"uf\":\"RJ\"," +
                "\"complemento\":\"Apto 101\"," +
                "\"numero\":\"123\"," +
                "\"regiao\":\"Zona Sul\"}";

        Long addressId = 7L;

        mockMvc.perform(put("/address/{id}", addressId)
                        .headers(HEADERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    void testDeleteAddress_Success() throws Exception {
        Long addressId = 7L;

        mockMvc.perform(delete("/address/{id}", addressId)
                        .headers(HEADERS)
                )
                .andExpect(status().isNoContent());
    }
}
