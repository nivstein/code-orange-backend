CREATE SCHEMA `codeorange`;

CREATE TABLE `codeorange`.`recorded_locations` (
  `entry_id` INT NOT NULL AUTO_INCREMENT,
  `event_id` VARCHAR(45) NULL,
  `country` VARCHAR(45) NULL,
  `device_id` VARCHAR(45) NULL,
  `from_timestamp` BIGINT NULL,
  `to_timestamp` BIGINT NULL,
  `lat` DOUBLE NULL,
  `lon` DOUBLE NULL,
  `radius` DOUBLE NULL,
  `patient_status` VARCHAR(45) NULL,
  `received_timestamp` BIGINT NULL,
  PRIMARY KEY (`entry_id`),
  INDEX `IDX_EVENT` (`event_id` ASC),
  INDEX `IDX_COUNTRY` (`country` ASC),
  INDEX `IDX_PATIENT_STATUS` (`patient_status` ASC),
  INDEX `IDX_RECEIVED` (`received_timestamp` ASC));

CREATE TABLE `codeorange`.`imported_locations_moh` (
  `entry_id` INT NOT NULL AUTO_INCREMENT,
  `event_id` VARCHAR(45) NULL,
  `country` VARCHAR(45) NULL,
  `device_id` VARCHAR(45) NULL,
  `from_timestamp` BIGINT NULL,
  `to_timestamp` BIGINT NULL,
  `lat` DOUBLE NULL,
  `lon` DOUBLE NULL,
  `radius` DOUBLE NULL,
  `patient_status` VARCHAR(45) NULL,
  `received_timestamp` BIGINT NULL,
  PRIMARY KEY (`entry_id`),
  INDEX `IDX_EVENT` (`event_id` ASC),
  INDEX `IDX_COUNTRY` (`country` ASC),
  INDEX `IDX_PATIENT_STATUS` (`patient_status` ASC),
  INDEX `IDX_RECEIVED` (`received_timestamp` ASC));
