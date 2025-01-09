package com.elisbao.spring_nava_api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = UserAddress.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAddress {
    public static final String TABLE_NAME = "useraddress";

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @Column(name = "cep", length = 9, nullable = false)
    @Size(min = 8, max = 9)
    @NotBlank
    private String cep;

    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @Column(name = "numero", length = 10, nullable = false)
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "localidade", nullable = false)
    private String localidade;

    @Column(name = "uf", length = 2, nullable = false)
    private String uf;

    @Column(name = "regiao")
    private String regiao;

}
