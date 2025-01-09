package com.elisbao.spring_nava_api.integracao.controllers;

import com.elisbao.spring_nava_api.integracao.AbstractIntegrationTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void testFindById_Success() throws Exception {

        mockMvc.perform(get("/user/{id}", 2L)
                        .headers(HEADERS)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("test_user2"));
    }

    @Test
    @Order(2)
    void testCreate_Success() throws Exception {

        String requestBody = "{" +
                "\"username\":\"newuser\"," +
                "\"password\":\"password123\"" +
                "}";

        mockMvc.perform(post("/user")
                        .headers(HEADERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @Order(3)
    void testUpdate_Success() throws Exception {

        String requestBody = "{" +
                "\"password\":\"newpassword123\"" +
                "}";

        mockMvc.perform(put("/user/{id}", 2L)
                        .headers(HEADERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    void testDelete_Success() throws Exception {

        mockMvc.perform(delete("/user/{id}", 2L)
                        .headers(HEADERS)
                )
                .andExpect(status().isNoContent());
    }
}
