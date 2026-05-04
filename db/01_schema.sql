-- Table: game
CREATE TABLE game
(
    id           bigserial      NOT NULL,
    title        varchar(100)   NOT NULL,
    description  text           NOT NULL,
    price        decimal(10, 2) NOT NULL,
    release_date date           NOT NULL,
    image_url    varchar(512)   NOT NULL,
    create_at    timestamp      NOT NULL,
    updated_at   timestamp      NOT NULL,
    CONSTRAINT game_pk PRIMARY KEY (id)
);

-- Table: game_genre
CREATE TABLE game_genre
(
    genre_id bigint NOT NULL,
    game_id  bigint NOT NULL,
    CONSTRAINT game_genre_pk PRIMARY KEY (genre_id, game_id)
);

-- Table: game_platform
CREATE TABLE game_platform
(
    platform_id bigint NOT NULL,
    game_id     bigint NOT NULL,
    CONSTRAINT game_platform_pk PRIMARY KEY (platform_id, game_id)
);

-- Table: genre
CREATE TABLE genre
(
    id   bigserial   NOT NULL,
    name varchar(50) NOT NULL,
    CONSTRAINT genre_pk PRIMARY KEY (id)
);

-- Table: person
CREATE TABLE person
(
    id         bigserial    NOT NULL,
    password   varchar(255) NOT NULL,
    first_name varchar(20)  NOT NULL,
    last_name  varchar(20)  NOT NULL,
    email      varchar(60)  NOT NULL,
    role       varchar(20)  NOT NULL,
    CONSTRAINT AK_0 UNIQUE (email) NOT DEFERRABLE INITIALLY IMMEDIATE,
    CONSTRAINT person_pk PRIMARY KEY (id)
);

-- Table: platform
CREATE TABLE platform
(
    id   bigserial   NOT NULL,
    name varchar(50) NOT NULL,
    CONSTRAINT platform_pk PRIMARY KEY (id)
);

-- Table: refresh_token
CREATE TABLE refresh_token
(
    id         bigserial    NOT NULL,
    token      varchar(512) NOT NULL,
    person_id  bigint       NOT NULL,
    created_at timestamp    NOT NULL,
    expires_at timestamp    NOT NULL,
    CONSTRAINT refresh_token_pk PRIMARY KEY (id)
);
