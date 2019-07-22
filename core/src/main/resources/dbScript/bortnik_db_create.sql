CREATE DATABASE IF NOT EXISTS `bortnik_db` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';
USE `bortnik_db`;

CREATE TABLE IF NOT EXISTS `bortnik_db`.`contact` (
 `contact_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `surname` VARCHAR(30) NOT NULL,
 `name` VARCHAR(30) NOT NULL,
 `patronymic` VARCHAR(30) DEFAULT NULL,
 `birthday` DATE NOT NULL,
 `gender` ENUM('male','female') NOT NULL,
 `nationality` VARCHAR(50) DEFAULT NULL,
 `marital_status` ENUM('single','married','divorced') NOT NULL,
 `website` VARCHAR(50) DEFAULT NULL,
 `email` VARCHAR(50) NOT NULL,
 `workplace` VARCHAR(50) DEFAULT NULL,
 `photo_link` VARCHAR(255) DEFAULT NULL,
 PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `bortnik_db`.`attachment` (
 `attachment_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `attachment_name` VARCHAR(100) NOT NULL,
 `attachment_link` VARCHAR(255) NOT NULL,
 `upload_date` DATE NOT NULL,
 `commentary` VARCHAR(50) DEFAULT NULL,
 `contact_id` INT(11) UNSIGNED NOT NULL,
 PRIMARY KEY (`attachment_id`),
 KEY `contact_id` (`contact_id`),
 CONSTRAINT `attachment - contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON
DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `bortnik_db`.`phone` (
 `phone_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `country_code` VARCHAR(6) NOT NULL,
 `operator_code` VARCHAR(5) NOT NULL,
 `phone_number` VARCHAR(11) NOT NULL,
 `phone_type` ENUM('home','mobile') NOT NULL DEFAULT 'mobile',
 `comment` VARCHAR(50) DEFAULT NULL,
 `contact_id` INT(11) UNSIGNED NOT NULL,
 PRIMARY KEY (`phone_id`),
 KEY `contact_id` (`contact_id`),
 CONSTRAINT `phone - contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON
DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS `bortnik_db`.`address` (
 `address_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `country` VARCHAR(50) DEFAULT NULL,
 `city` VARCHAR(50) DEFAULT NULL,
 `street` VARCHAR(100) DEFAULT NULL,
 `postcode` VARCHAR(15) DEFAULT NULL,
 `contact_id` INT(11) UNSIGNED NOT NULL,
 PRIMARY KEY (`address_id`),
 KEY `contact_id` (`contact_id`),
 CONSTRAINT `address - contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON
DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

