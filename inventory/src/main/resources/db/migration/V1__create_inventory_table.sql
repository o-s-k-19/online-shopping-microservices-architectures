DROP TABLE IF EXISTS inventories;

CREATE TABLE inventories (
    id BIGINT NOT NULL,
    sku_code TEXT NOT NULL,
    quantity BIGINT NOT NULL,
    PRIMARY KEY (id)
);
