-- liquibase formatted sql

-- changeset order:create-table

CREATE TABLE IF NOT EXISTS `order_line_items`(
`id` INT NOT NULL auto_increment PRIMARY KEY,
`sku_code` VARCHAR(255) NOT NULL DEFAULT '',
`price` BIGINT NOT NULL,
`quantity` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `orders`(
`id` INT NOT NULL auto_increment PRIMARY KEY,
`number` VARCHAR(255) NOT NULL DEFAULT 'unknow',
`line_item_id` INT FOREIGN KEY REFERENCES order_line_items(id)
);