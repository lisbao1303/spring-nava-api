Execução da aplicatição (clássico Maven/SpringBoot)
- Instalar dependências:
  `mvn clean install`
- Usando o Maven:
  `mvn spring-boot:run`

Testes:

`mvn clean test`

Criando o projeto (perfil de versão):

`mvn clean package -P release -DskipTests`

Atualizar os contêineres de banco de dados (Elasticsearch e PostgreSQL):

`docker compose -f docker-compose.databases.yml up -d`

O Elasticsearch demora um tempo para iniciar após o contêiner subir
Subir o contêiner da aplicação Spring (após o Elasticsearch e PostgreSQL estarem em funcionamento):

`docker compose -f docker-compose.yml up --build -d`
