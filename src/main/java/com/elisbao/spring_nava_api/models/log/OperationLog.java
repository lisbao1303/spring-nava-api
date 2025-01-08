package com.elisbao.spring_nava_api.models.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "operation-logs")
public class OperationLog {

    @Id
    private String id;
    private String operationName;  // Nome da operação
    private String method;
    private String httpMethod;
    private Object requestData;  // Dados da requisição como um Map
    private Object responseData;  // Dados da resposta como um Map
    private LocalDateTime timestamp; // Data e hora da operação
    private boolean success;       // Indica se a operação foi bem-sucedida ou falhou
    private String errorDetails;   // Detalhes de erro, se houver

}
