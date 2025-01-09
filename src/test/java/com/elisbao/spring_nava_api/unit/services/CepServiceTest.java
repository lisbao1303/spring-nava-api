package com.elisbao.spring_nava_api.unit.services;

import com.elisbao.spring_nava_api.models.dto.CepResponseDTO;
import com.elisbao.spring_nava_api.services.CepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class CepServiceTest {

    @InjectMocks
    private CepService cepService;

    @Mock
    private RestTemplate restTemplate;  // Garantindo que RestTemplate seja mockado

    @Value("${test.cep.api.url}")
    private String cepApiUrl;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);
        cepApiUrl = "http://localhost:4200/api/v1/{cep}";
        ReflectionTestUtils.setField(cepService, "cepApiUrl", "http://localhost:4200/api/v1/{cep}");
    }

    @Test
    void testBuscarCep() {
        // Arrange
        String cep = "12345678";
        CepResponseDTO mockResponse = new CepResponseDTO();
        mockResponse.setCep("12345678");
        mockResponse.setLogradouro("Rua Teste");
        mockResponse.setBairro("Bairro Teste");
        mockResponse.setLocalidade("Cidade Teste");
        mockResponse.setUf("SP");

        // Simula a resposta do RestTemplate
        when(restTemplate.getForObject(cepApiUrl.replace("{cep}", cep), CepResponseDTO.class))
                .thenReturn(mockResponse);

        // Act
        CepResponseDTO result = cepService.buscarCep(cep);

        // Assert
        assertNotNull(result);
        assertEquals("12345678", result.getCep());
        assertEquals("Rua Teste", result.getLogradouro());
        assertEquals("Bairro Teste", result.getBairro());
        assertEquals("Cidade Teste", result.getLocalidade());
        assertEquals("SP", result.getUf());

        // Verifica se o metodo getForObject foi chamado com a URL correta
        verify(restTemplate, times(1)).getForObject(cepApiUrl.replace("{cep}", cep), CepResponseDTO.class);
    }

    @Test
    void testBuscarCepWhenApiReturnsNull() {
        // Arrange
        String cep = "12345678";

        // Simula o retorno de null pelo RestTemplate
        when(restTemplate.getForObject(cepApiUrl.replace("{cep}", cep), CepResponseDTO.class))
                .thenReturn(null);

        // Act
        CepResponseDTO result = cepService.buscarCep(cep);

        // Assert
        assertNull(result);

        // Verifica se o metodo getForObject foi chamado com a URL correta
        verify(restTemplate, times(1)).getForObject(cepApiUrl.replace("{cep}", cep), CepResponseDTO.class);
    }
}
