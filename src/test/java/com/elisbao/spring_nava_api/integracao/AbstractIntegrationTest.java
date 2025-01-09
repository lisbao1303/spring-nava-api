package com.elisbao.spring_nava_api.integracao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    protected HttpHeaders HEADERS;

    @Autowired
    private MockMvc mockMvc;

    // POSTGRES CONTAINER TEST
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("schema.sql")
            .withNetwork(Network.SHARED)
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    @BeforeAll
    static void beforeAll() {
        postgresContainer.start();
    }

    // ELASTICSERACH CONTAINER TEST
    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:8.8.0";

    static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
            ELASTICSEARCH_IMAGE
    )
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false")
            .withEnv("xpack.security.transport.ssl.enabled", "false")
            .withEnv("xpack.security.http.ssl.enabled", "false")
            .withNetwork(Network.SHARED)
            .withExposedPorts(9200)
            .waitingFor(Wait.forHttp("/").forPort(9200).withStartupTimeout(Duration.ofMinutes(2)));

    @DynamicPropertySource
    static void elasticProperties(DynamicPropertyRegistry registry) {
        elasticsearchContainer.start();
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);

        postgresContainer.start();
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    //

    @BeforeEach
    void setUpAuthenticationUserTest() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"test_user\", \"password\": \"password123\" }"))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", org.hamcrest.Matchers.startsWith("Bearer ")))
                .andReturn();

        String authorizationHeader = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);

        String token = authorizationHeader.substring(7);

        HEADERS = new HttpHeaders();
        HEADERS.set("Authorization", "Bearer " + token);
        HEADERS.set("Content-Type", "application/json");
    }
}