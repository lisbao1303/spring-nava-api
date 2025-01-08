package com.elisbao.spring_nava_api.controllers;

import com.elisbao.spring_nava_api.models.dto.LoginRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Login", description = "Get JWT token")
public class SwaggerLoginController {

    @Operation(
            summary = "Login",
            description = "User authentication to obtain the JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token successfully received"),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials")
            }
    )
    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDTO loginRequest) {
        // Este metodo não faz nada, apenas documentando o endpoint para o Swagger
    }
}
