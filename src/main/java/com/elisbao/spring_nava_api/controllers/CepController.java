package com.elisbao.spring_nava_api.controllers;

import com.elisbao.spring_nava_api.annotations.LogOperation;
import com.elisbao.spring_nava_api.models.dto.CepResponseDTO;
import com.elisbao.spring_nava_api.services.CepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
@Tag(name = "CEP code search ", description = "Operations related to CEP code search")
public class CepController {

    @Autowired
    private CepService cepService;


    @Operation(
            summary = "Find address by CEP",
            description = "Returns CEP code data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address found", content = @Content(schema = @Schema(implementation = CepResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Address not found")
            }
    )
    @GetMapping("/{cep}")
    @LogOperation
    public ResponseEntity<CepResponseDTO> buscarCep(@PathVariable String cep) {

        if (!cep.matches("\\d{5}-\\d{3}")) {
            return ResponseEntity.badRequest().build();
        }

        CepResponseDTO response = cepService.buscarCep(cep);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(response);
    }
}
