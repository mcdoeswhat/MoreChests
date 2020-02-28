CREATE TABLE IF NOT EXISTS `chests` (
    `ChestID` INT(11) NOT NULL AUTO_INCREMENT,
	`location` VARCHAR(255) NOT NULL,
	`chest_item` VARCHAR(2550) NOT NULL,
	`items` LONGTEXT NULL DEFAULT NULL,
	PRIMARY KEY (`ChestID`),
	UNIQUE INDEX `location` (`location`)
);
