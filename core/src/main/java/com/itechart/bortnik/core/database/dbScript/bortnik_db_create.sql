CREATE DATABASE `bortnik_db` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';
CREATE TABLE `bortnik_db`.`contact` (
 `contact_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `surname` VARCHAR(30) NOT NULL,
 `name` VARCHAR(30) NOT NULL,
 `patronymic` VARCHAR(30) DEFAULT NULL,
 `birthday` DATE NOT NULL,
 `gender` ENUM('male','female') NOT NULL,
 `nationality` VARCHAR(50) NOT NULL,
 `marital_status` ENUM('single','married','divorced') NOT NULL,
 `website` VARCHAR(50) NOT NULL,
 `email` VARCHAR(50) NOT NULL,
 `workplace` VARCHAR(50) NOT NULL, PRIMARY KEY (`contact_id`),
 `photo_link` VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;
CREATE TABLE `bortnik_db`.`attachment` (
 `attachment_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `attachment_name` VARCHAR(100) NOT NULL,
 `attachment_link` VARCHAR(255) NOT NULL,
 `upload_date` DATE NOT NULL,
 `contact_id` INT(11) UNSIGNED NOT NULL, PRIMARY KEY (`attachment_id`), KEY `contact_id` (`contact_id`), CONSTRAINT `attachment - contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON
DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;
CREATE TABLE `bortnik_db`.`phone` (
 `phone_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `country_code` VARCHAR(6) NOT NULL DEFAULT '0',
 `operator_code` VARCHAR(5) NOT NULL DEFAULT '0',
 `phone_number` VARCHAR(11) NOT NULL DEFAULT '0',
 `phone_type` ENUM('home','mobile') NOT NULL DEFAULT 'mobile',
 `comment` VARCHAR(50) DEFAULT NULL,
 `contact_id` INT(11) UNSIGNED NOT NULL, PRIMARY KEY (`phone_id`), KEY `contact_id` (`contact_id`), CONSTRAINT `phone - contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON
DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;
CREATE TABLE `bortnik_db`.`address` (
 `address_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
 `country` VARCHAR(50) NOT NULL,
 `city` VARCHAR(50) NOT NULL,
 `street` VARCHAR(100) NOT NULL,
 `postcode` VARCHAR(15) NOT NULL,
 `contact_id` INT(11) UNSIGNED NOT NULL, PRIMARY KEY (`address_id`), KEY `contact_id` (`contact_id`), CONSTRAINT `address - contact` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`contact_id`) ON
DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

