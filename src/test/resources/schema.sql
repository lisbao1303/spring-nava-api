CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    password character varying(60)  NOT NULL,
    username character varying(100) NOT NULL
);

CREATE TABLE user_profile
(
    user_id bigint  NOT NULL,
    profile integer NOT NULL
);

CREATE TABLE useraddress
(
    id          BIGSERIAL PRIMARY KEY,
    bairro      character varying(255) NOT NULL,
    cep         character varying(8)   NOT NULL,
    complemento character varying(255),
    localidade  character varying(255) NOT NULL,
    logradouro  character varying(255) NOT NULL,
    numero      character varying(10)  NOT NULL,
    regiao      character varying(255),
    uf          character varying(2)   NOT NULL,
    user_id     bigint                 NOT NULL
);

INSERT INTO users (id, password, username)
VALUES
    (7, '$2a$10$TOrPBJqk5ZBOASaMy79OZ.765piTck1LckFiwkSNxAwGR3F.nDug2', 'test_user'),
    (2, '$2a$10$TOrPBJqk5ZBOASaMy79OZ.765piTck1LckFiwkSNxAwGR3F.nDug2', 'test_user2');

INSERT INTO user_profile (user_id, profile)
VALUES
    (7, 1),
    (2, 2);

INSERT INTO useraddress (id,
                         bairro,
                         cep,
                         complemento,
                         localidade,
                         logradouro,
                         numero,
                         regiao,
                         uf,
                         user_id)
VALUES (7,
        'Bairro Teste',
        '12345678',
        'Complemento Teste',
        'Cidade Teste',
        'Rua Teste',
        '123',
        'Região Teste',
        'SP',
        7
       );
