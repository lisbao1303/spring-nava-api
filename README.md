Api para busca de CEP e cadastro de endereço para usuários autenticados.
Utiliza autenticação via token JWT (retorno Bearer {token} no header).

POST /user -> criar novo usuário.
POST /login -> obter token.

Swagger UI -> /swagger-ui/index.html
API Docs -> /v3/api-docs/

DATABASE:
Database Postgres, db:navaapidb, para armazenar dados do usuário.

LOGS:
O aspect:LoggingAspect captura dados das operações e armazena no ElasticSearch (para posteriores consultas em um Kibana por ex.) 
Para gravar dados de um endpoint nos operation-log utilizar: @LogOperation

API EXTERNA CEP:
https://viacep.com.br/ws/{cep}/json/

Execução da aplicatição (clássico Maven/SpringBoot)
- Instalar dependências:
  `mvn clean install`
- Usando o Maven:
  `mvn spring-boot:run`

Testes:

`mvn clean test`

Criando o projeto (perfil de release):

`mvn clean package -P release -DskipTests`

Atualizar os contêineres de banco de dados (Elasticsearch e PostgreSQL):

`docker compose -f docker-compose.databases.yml up -d`

O Elasticsearch demora um tempo para iniciar após o contêiner subir
Subir o contêiner da aplicação Spring (após o Elasticsearch e PostgreSQL estarem em funcionamento):

`docker compose -f docker-compose.yml up --build -d`


JENKINS: 