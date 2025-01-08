package com.elisbao.spring_nava_api.controllers;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.esql.ElasticsearchEsqlClient;
import com.elisbao.spring_nava_api.models.dto.CepResponseDTO;
import com.elisbao.spring_nava_api.repositories.log.OperationLogRepository;
import com.elisbao.spring_nava_api.services.CepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CepService cepService;

//    @InjectMocks
//    private CepController cepController;
//
//    @BeforeEach
//    void setUp() {
//        // Inicializa o controlador com o serviço mockado
//        this.mockMvc = MockMvcBuilders.standaloneSetup(cepController).build();
//    }

    @Test
    void testBuscarCep_Success() throws Exception {
        // Arrange
        String cep = "12345678";
        CepResponseDTO mockResponse = new CepResponseDTO();
        mockResponse.setCep("12345678");
        mockResponse.setLogradouro("Rua Teste");
        mockResponse.setBairro("Bairro Teste");
        mockResponse.setLocalidade("Cidade Teste");
        mockResponse.setUf("SP");

        when(cepService.buscarCep(cep)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/cep/{cep}", cep)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("12345678"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste"))
                .andExpect(jsonPath("$.bairro").value("Bairro Teste"))
                .andExpect(jsonPath("$.localidade").value("Cidade Teste"))
                .andExpect(jsonPath("$.uf").value("SP"));

        verify(cepService, times(1)).buscarCep(cep);
    }

    @Test
    void testBuscarCep_NotFound() throws Exception {
        // Arrange
        String cep = "12345678";
        when(cepService.buscarCep(cep)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/cep/{cep}", cep)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cepService, times(1)).buscarCep(cep);
    }
}
