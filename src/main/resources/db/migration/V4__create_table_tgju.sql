CREATE TABLE tgju(
    id BIGINT NOT NULL PRIMARY KEY,
    price DOUBLE PRECISION NOT NULL,
    provider_id BIGINT,
    CONSTRAINT fk_tgju FOREIGN KEY (provider_id) REFERENCES price_providers(id)
);