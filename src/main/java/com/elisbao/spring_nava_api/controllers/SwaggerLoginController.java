package com.elisbao.spring_nava_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Login", description = "Get JWT token")
public class SwaggerLoginController {

    // Modelo para o corpo da requisição
    public static class LoginRequest {
        @NotBlank
        @Parameter(description = "User name", required = true)
        private String username;

        @NotBlank
        @Parameter(description = "Password", required = true)
        private String password;

        // Getters e Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Operation(
            summary = "Login",
            description = "User authentication to obtain the JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token successfully received"),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials")
            }
    )
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest) {
        // Este metodo não faz nada, apenas documentando o endpoint para o Swagger
    }
}
