package com.elisbao.spring_nava_api.services;

import com.elisbao.spring_nava_api.models.dto.CepResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    private final RestTemplate restTemplate;

    @Value("${cep.api.url}")
    String cepApiUrl;

    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CepResponseDTO buscarCep(String cep) {

        String url = cepApiUrl.replace("{cep}", cep);

        // Realizando a requisição HTTP e mapeando a resposta para o DTO
        return restTemplate.getForObject(url, CepResponseDTO.class);
    }
}