package com.elisbao.spring_nava_api.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAddressDTO {

    @Size(min = 9, max = 9)
    @NotBlank
    private String cep;

    @NotBlank
    @Size(max = 255)
    private String logradouro;

    @NotBlank
    @Size(max = 10)
    private String numero;

    @Size(max = 255)
    private String complemento;

    @NotBlank
    @Size(max = 255)
    private String bairro;

    @NotBlank
    @Size(max = 255)
    private String localidade;

    @NotBlank
    @Size(max = 2)
    private String uf;

    @Size(max = 255)
    private String regiao;

}
