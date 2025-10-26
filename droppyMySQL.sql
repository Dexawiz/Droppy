-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema droppy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema droppy
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `droppy` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `droppy` ;

-- -----------------------------------------------------
-- Table `droppy`.`companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `droppy`.`companies` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `phone_number` VARCHAR(32) NULL DEFAULT NULL,
  `work_start` TIME NULL DEFAULT NULL,
  `work_end` TIME NULL DEFAULT NULL,
  `category` ENUM('RESTAURANT', 'GROCERY', 'PHARMACY', 'OTHER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_company_name_addr` (`name` ASC, `address` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `droppy`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `droppy`.`users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `surname` VARCHAR(100) NOT NULL,
  `role` ENUM('CUSTOMER', 'DRIVER', 'ADMIN') NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `phone_number` VARCHAR(32) NULL DEFAULT NULL,
  `card_number` VARCHAR(32) NULL DEFAULT NULL,
  `driver_status` ENUM('OFFLINE', 'IDLE', 'ON_THE_WAY', 'DELIVERING', 'UNAVAILABLE') NULL DEFAULT NULL,
  `delivery_method` ENUM('CAR', 'BIKE', 'SCOOTER', 'FOOT') NULL DEFAULT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_users_email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `droppy`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `droppy`.`orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT NOT NULL,
  `driver_id` BIGINT NULL DEFAULT NULL,
  `company_id` BIGINT NOT NULL,
  `total_price` DECIMAL(10,2) NOT NULL,
  `delivery_from_address` VARCHAR(255) NOT NULL,
  `delivery_to_address` VARCHAR(255) NOT NULL,
  `order_created_time` DATETIME NOT NULL,
  `estimated_delivery_time` DATETIME NULL DEFAULT NULL,
  `payment_method` ENUM('CASH', 'CARD', 'ONLINE') NOT NULL,
  `status` ENUM('NEW', 'ACCEPTED', 'IN_DELIVERY', 'DELIVERED', 'CANCELLED') NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `ix_orders_customer` (`customer_id` ASC) VISIBLE,
  INDEX `ix_orders_driver` (`driver_id` ASC) VISIBLE,
  INDEX `ix_orders_company` (`company_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_company`
    FOREIGN KEY (`company_id`)
    REFERENCES `droppy`.`companies` (`id`),
  CONSTRAINT `fk_orders_customer`
    FOREIGN KEY (`customer_id`)
    REFERENCES `droppy`.`users` (`id`),
  CONSTRAINT `fk_orders_driver`
    FOREIGN KEY (`driver_id`)
    REFERENCES `droppy`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `droppy`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `droppy`.`products` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `company_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_product_company_name` (`company_id` ASC, `name` ASC) VISIBLE,
  INDEX `ix_products_company` (`company_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_company`
    FOREIGN KEY (`company_id`)
    REFERENCES `droppy`.`companies` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `droppy`.`order_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `droppy`.`order_items` (
  `order_id` BIGINT NOT NULL,
  `product_id` BIGINT NOT NULL,
  `quantity` INT NOT NULL DEFAULT '1',
  `price_each` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  INDEX `ix_items_product` (`product_id` ASC) VISIBLE,
  CONSTRAINT `fk_items_order`
    FOREIGN KEY (`order_id`)
    REFERENCES `droppy`.`orders` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_items_product`
    FOREIGN KEY (`product_id`)
    REFERENCES `droppy`.`products` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
