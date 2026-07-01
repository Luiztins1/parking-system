CREATE TABLE users_auths(
    id UUID NOT NULL PRIMARY KEY,
    login VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(300) NOT NULL,
    roles VARCHAR[]
);