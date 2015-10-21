DROP TABLE IF EXISTS `opcat`.`Models_Data`;
DROP TABLE IF EXISTS `opcat`.`Models`;

CREATE TABLE `opcat`.`Models` (
       `ID` INT(10) NOT NULL AUTO_INCREMENT
     , `SVNID` VARCHAR(254) NOT NULL
     , `Name` VARCHAR(254) NOT NULL
     , PRIMARY KEY (`ID`)
);

CREATE TABLE `opcat`.`Models_Data` (
       `ID` INT(10) NOT NULL
     , PRIMARY KEY (`ID`)
);

ALTER TABLE `opcat`.`Models_Data`
  ADD CONSTRAINT `FK_Models_Data_1`
      FOREIGN KEY (`ID`)
      REFERENCES `opcat`.`Models` (`ID`);

