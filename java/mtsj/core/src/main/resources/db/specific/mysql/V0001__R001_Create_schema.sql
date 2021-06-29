-- MySQL Workbench Forward Engineering

SET
@OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET
@OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET
@OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema bootdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema bootdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bootdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE
`bootdb` ;

-- -----------------------------------------------------
-- Table `bootdb`.`BinaryObject`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`BinaryObject`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `content`
    LONGBLOB
    NULL
    DEFAULT
    NULL,
    `mimeType`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `filesize` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`UserRole`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`UserRole`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `active`
    BIT
(
    1
) NOT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`User`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `email`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `password` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `secret` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `twoFactorStatus` BIT
(
    1
) NOT NULL,
    `username` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `idRole` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FKjd0ft4x6tx0s5bofbwgb5edmc`
(
    `idRole` ASC
),
    CONSTRAINT `FKjd0ft4x6tx0s5bofbwgb5edmc`
    FOREIGN KEY
(
    `idRole`
)
    REFERENCES `bootdb`.`UserRole`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`Table`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`Table`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `alexaID`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `seatsNumber` INT NULL DEFAULT NULL,
    `tableName` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`InvitedGuest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`InvitedGuest`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `accepted`
    BIT
(
    1
) NOT NULL,
    `email` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `guestToken` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `modificationDate` DATETIME
(
    6
) NULL DEFAULT NULL,
    `idBooking` BIGINT NULL DEFAULT NULL,
    `idOrder` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FKjtcygv56myq0hgr6ps8i3tydt`
(
    `idBooking` ASC
),
    INDEX `FKhskl8tosaoe4xiddvyqsxuell`
(
    `idOrder` ASC
),
    CONSTRAINT `FKhskl8tosaoe4xiddvyqsxuell`
    FOREIGN KEY
(
    `idOrder`
)
    REFERENCES `bootdb`.`Orders`
(
    `id`
),
    CONSTRAINT `FKjtcygv56myq0hgr6ps8i3tydt`
    FOREIGN KEY
(
    `idBooking`
)
    REFERENCES `bootdb`.`Booking`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`Orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`Orders`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `orderStatus`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `paid` BIT
(
    1
) NULL DEFAULT NULL,
    `idBooking` BIGINT NULL DEFAULT NULL,
    `idHost` BIGINT NULL DEFAULT NULL,
    `idInvitedGuest` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FK3nmhg5f8u4mof3jf7qalq881f`
(
    `idBooking` ASC
),
    INDEX `FKrio26qtivi0ba0y1m6cn5iw2`
(
    `idHost` ASC
),
    INDEX `FK6g39ixtd11m1ey2i1pjkj6w3c`
(
    `idInvitedGuest` ASC
),
    CONSTRAINT `FK3nmhg5f8u4mof3jf7qalq881f`
    FOREIGN KEY
(
    `idBooking`
)
    REFERENCES `bootdb`.`Booking`
(
    `id`
),
    CONSTRAINT `FK6g39ixtd11m1ey2i1pjkj6w3c`
    FOREIGN KEY
(
    `idInvitedGuest`
)
    REFERENCES `bootdb`.`InvitedGuest`
(
    `id`
),
    CONSTRAINT `FKrio26qtivi0ba0y1m6cn5iw2`
    FOREIGN KEY
(
    `idHost`
)
    REFERENCES `bootdb`.`Booking`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`Booking`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`Booking`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `assistants`
    INT
    NULL
    DEFAULT
    NULL,
    `bookingDate`
    DATETIME
(
    6
) NULL DEFAULT NULL,
    `bookingToken` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `bookingType` INT NULL DEFAULT NULL,
    `cancelled` BIT
(
    1
) NULL DEFAULT NULL,
    `comment` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `creationDate` DATETIME
(
    6
) NULL DEFAULT NULL,
    `email` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `expirationDate` DATETIME
(
    6
) NULL DEFAULT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `needHelp` BIT
(
    1
) NOT NULL,
    `idOrder` BIGINT NULL DEFAULT NULL,
    `idTable` BIGINT NULL DEFAULT NULL,
    `idUser` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FKpv7jumy2028779fcu0rw1d28j`
(
    `idOrder` ASC
),
    INDEX `FK92imuix6bgb91i71cwyvsrrfv`
(
    `idTable` ASC
),
    INDEX `FK81lk1eknh2kyskih5b9t2e07u`
(
    `idUser` ASC
),
    CONSTRAINT `FK81lk1eknh2kyskih5b9t2e07u`
    FOREIGN KEY
(
    `idUser`
)
    REFERENCES `bootdb`.`User`
(
    `id`
),
    CONSTRAINT `FK92imuix6bgb91i71cwyvsrrfv`
    FOREIGN KEY
(
    `idTable`
)
    REFERENCES `bootdb`.`Table`
(
    `id`
),
    CONSTRAINT `FKpv7jumy2028779fcu0rw1d28j`
    FOREIGN KEY
(
    `idOrder`
)
    REFERENCES `bootdb`.`Orders`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`Category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`Category`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `description`
    VARCHAR
(
    1500
) NULL DEFAULT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `showOrder` INT NOT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`Image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`Image`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `content`
    LONGBLOB
    NULL
    DEFAULT
    NULL,
    `contentType`
    INT
    NULL
    DEFAULT
    NULL,
    `mimeType`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`Dish`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`Dish`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `description`
    VARCHAR
(
    1500
) NULL DEFAULT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `price` DECIMAL
(
    19,
    2
) NULL DEFAULT NULL,
    `idImage` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FK1n03jmsl7nwwglxkj3b4kpqjy`
(
    `idImage` ASC
),
    CONSTRAINT `FK1n03jmsl7nwwglxkj3b4kpqjy`
    FOREIGN KEY
(
    `idImage`
)
    REFERENCES `bootdb`.`Image`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`DishCategory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`DishCategory`
(
    `idDish`
    BIGINT
    NOT
    NULL,
    `idCategory`
    BIGINT
    NOT
    NULL,
    INDEX
    `FKptrcgsor0gs7x4bnprgya8ns7`
(
    `idCategory`
    ASC
),
    INDEX `FK1yurwbj17p8v4kow9ccl0flsf`
(
    `idDish` ASC
),
    CONSTRAINT `FK1yurwbj17p8v4kow9ccl0flsf`
    FOREIGN KEY
(
    `idDish`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
),
    CONSTRAINT `FKptrcgsor0gs7x4bnprgya8ns7`
    FOREIGN KEY
(
    `idCategory`
)
    REFERENCES `bootdb`.`Category`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`Ingredient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`Ingredient`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `description`
    VARCHAR
(
    1500
) NULL DEFAULT NULL,
    `name` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `price` DECIMAL
(
    19,
    2
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`DishIngredient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`DishIngredient`
(
    `idDish`
    BIGINT
    NOT
    NULL,
    `idIngredient`
    BIGINT
    NOT
    NULL,
    INDEX
    `FK4qd3ud1u5dkch9y945x3ndjsb`
(
    `idIngredient`
    ASC
),
    INDEX `FKs0r9aqqcdve0ms7hnr6xpxax3`
(
    `idDish` ASC
),
    CONSTRAINT `FK4qd3ud1u5dkch9y945x3ndjsb`
    FOREIGN KEY
(
    `idIngredient`
)
    REFERENCES `bootdb`.`Ingredient`
(
    `id`
),
    CONSTRAINT `FKs0r9aqqcdve0ms7hnr6xpxax3`
    FOREIGN KEY
(
    `idDish`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`OrderLine`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`OrderLine`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `amount`
    INT
    NULL
    DEFAULT
    NULL,
    `comment`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `idDish` BIGINT NULL DEFAULT NULL,
    `idOrder` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FKi0udpr0p4d8jj918n8qrhu4r8`
(
    `idDish` ASC
),
    INDEX `FKnkdil8u5vyy892n03xn8umphb`
(
    `idOrder` ASC
),
    CONSTRAINT `FKi0udpr0p4d8jj918n8qrhu4r8`
    FOREIGN KEY
(
    `idDish`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
),
    CONSTRAINT `FKnkdil8u5vyy892n03xn8umphb`
    FOREIGN KEY
(
    `idOrder`
)
    REFERENCES `bootdb`.`Orders`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`OrderDishExtraIngredient`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`OrderDishExtraIngredient`
(
    `idOrderLine`
    BIGINT
    NOT
    NULL,
    `idIngredient`
    BIGINT
    NOT
    NULL,
    INDEX
    `FKgl8vsoqb9o5y363dwx9bs325e`
(
    `idIngredient`
    ASC
),
    INDEX `FKa00i8gboyae585i1mklv5uvjf`
(
    `idOrderLine` ASC
),
    CONSTRAINT `FKa00i8gboyae585i1mklv5uvjf`
    FOREIGN KEY
(
    `idOrderLine`
)
    REFERENCES `bootdb`.`OrderLine`
(
    `id`
),
    CONSTRAINT `FKgl8vsoqb9o5y363dwx9bs325e`
    FOREIGN KEY
(
    `idIngredient`
)
    REFERENCES `bootdb`.`Ingredient`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`OrderedDishesPerDay`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`OrderedDishesPerDay`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `amount`
    INT
    NULL
    DEFAULT
    NULL,
    `bookingdate`
    DATETIME
(
    6
) NULL DEFAULT NULL,
    `designation` VARCHAR
(
    255
) NULL DEFAULT NULL,
    `temperature` DOUBLE NULL DEFAULT NULL,
    `idDish` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FKfsfd5v4ysupeqqqy75edw9u5v`
(
    `idDish` ASC
),
    CONSTRAINT `FKfsfd5v4ysupeqqqy75edw9u5v`
    FOREIGN KEY
(
    `idDish`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`OrderedDishesPerMonth`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`OrderedDishesPerMonth`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `amount`
    INT
    NULL
    DEFAULT
    NULL,
    `bookingdate`
    DATETIME
(
    6
) NULL DEFAULT NULL,
    `temperature` DOUBLE NULL DEFAULT NULL,
    `idDish` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FKn9p9iov10kpqytk2bs8n8qcfd`
(
    `idDish` ASC
),
    CONSTRAINT `FKn9p9iov10kpqytk2bs8n8qcfd`
    FOREIGN KEY
(
    `idDish`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`PREDICTION_ALL_FORECAST`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`PREDICTION_ALL_FORECAST`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `forecast`
    DOUBLE
    NULL
    DEFAULT
    NULL,
    `timestamp`
    INT
    NULL
    DEFAULT
    NULL,
    `IDDISH`
    BIGINT
    NULL
    DEFAULT
    NULL,
    PRIMARY
    KEY
(
    `id`
),
    INDEX `FKj4fn1cu50nlr9t5cxkujux4i`
(
    `IDDISH` ASC
),
    CONSTRAINT `FKj4fn1cu50nlr9t5cxkujux4i`
    FOREIGN KEY
(
    `IDDISH`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`PREDICTION_ALL_MODELS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`PREDICTION_ALL_MODELS`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `value`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    `IDDISH` BIGINT NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `FKhuns5c07hnud026ah4f7y74rk`
(
    `IDDISH` ASC
),
    CONSTRAINT `FKhuns5c07hnud026ah4f7y74rk`
    FOREIGN KEY
(
    `IDDISH`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`PREDICTION_FORECAST_DATA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`PREDICTION_FORECAST_DATA`
(
    `timestamp`
    INT
    NOT
    NULL,
    `holiday`
    INT
    NOT
    NULL,
    `temperature`
    DOUBLE
    NOT
    NULL,
    PRIMARY
    KEY
(
    `timestamp`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`ResetToken`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`ResetToken`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `expires`
    DATETIME
(
    6
) NULL DEFAULT NULL,
    `flag` BIT
(
    1
) NOT NULL,
    `idUser` BIGINT NULL DEFAULT NULL,
    `token` VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`RevInfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`RevInfo`
(
    `id`
    BIGINT
    NOT
    NULL,
    `timestamp`
    BIGINT
    NULL
    DEFAULT
    NULL,
    `userLogin`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`UserFavourite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`UserFavourite`
(
    `id`
    BIGINT
    NOT
    NULL,
    `modificationCounter`
    INT
    NOT
    NULL,
    `idDish`
    BIGINT
    NULL
    DEFAULT
    NULL,
    `idUser`
    BIGINT
    NULL
    DEFAULT
    NULL,
    PRIMARY
    KEY
(
    `id`
),
    INDEX `FK4ixstlq44dd43dx92qef32aot`
(
    `idDish` ASC
),
    INDEX `FK2ou2pnxulb89rkkr34yq9ehfg`
(
    `idUser` ASC
),
    CONSTRAINT `FK2ou2pnxulb89rkkr34yq9ehfg`
    FOREIGN KEY
(
    `idUser`
)
    REFERENCES `bootdb`.`User`
(
    `id`
),
    CONSTRAINT `FK4ixstlq44dd43dx92qef32aot`
    FOREIGN KEY
(
    `idDish`
)
    REFERENCES `bootdb`.`Dish`
(
    `id`
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`flyway_schema_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`flyway_schema_history`
(
    `installed_rank`
    INT
    NOT
    NULL,
    `version`
    VARCHAR
(
    50
) NULL DEFAULT NULL,
    `description` VARCHAR
(
    1500
) NOT NULL,
    `type` VARCHAR
(
    20
) NOT NULL,
    `script` VARCHAR
(
    1000
) NOT NULL,
    `checksum` INT NULL DEFAULT NULL,
    `installed_by` VARCHAR
(
    100
) NOT NULL,
    `installed_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `execution_time` INT NOT NULL,
    `success` TINYINT
(
    1
) NOT NULL,
    PRIMARY KEY
(
    `installed_rank`
),
    INDEX `flyway_schema_history_s_idx`
(
    `success` ASC
))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `bootdb`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bootdb`.`hibernate_sequence`
(
    `next_val` BIGINT NULL DEFAULT NULL
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET
SQL_MODE=@OLD_SQL_MODE;
SET
FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET
UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
