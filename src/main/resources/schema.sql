CREATE TABLE IF NOT EXISTS orders (
                                      id            UUID PRIMARY KEY,
                                      item_name     VARCHAR(255) NOT NULL,
    quantity      INTEGER      NOT NULL CHECK (quantity > 0),
    price         NUMERIC(10,2) NOT NULL CHECK (price > 0),
    status        VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMP    NOT NULL
    );