CREATE TABLE talasea(
    id BIGINT NOT NULL PRIMARY KEY,
    price DOUBLE PRECISION NOT NULL,
    provider_id BIGINT,
    CONSTRAINT fk_talasea FOREIGN KEY (provider_id) REFERENCES price_providers(id)
);