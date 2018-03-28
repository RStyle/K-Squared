CREATE TABLE `ksquared_db`.`LOGIN_TOKENS` ( 
	`user` VARCHAR(100) NOT NULL , 
	`token` VARCHAR(100) NOT NULL , 
	`date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`token`)
) ENGINE = InnoDB
 
 CREATE TABLE IF NOT EXISTS USERS ( 
	user VARCHAR(100) PRIMARY KEY, 
	password VARCHAR(255) NOT NULL, 
	email VARCHAR(1000), 
	status VARCHAR(30)
);


CREATE TABLE `SETS` (
  `filename` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `chapter` varchar(50) NOT NULL DEFAULT '#1',
  `user` varchar(100) NOT NULL,
  `descreption` varchar(2000) DEFAULT NULL,
  `is_public` tinyint(1) NOT NULL DEFAULT '0',
  `created_on` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `downloads` int(11) NOT NULL DEFAULT '0'
);
CREATE TABLE `ksquared_db`.`CARDS` (
	`user` varchar(100) NOT NULL ,
	`setname` varchar(100) NOT NULL ,
	`chapter` int NOT NULL,
	`fronttext` varchar(100),
	`backtext` varchar(100),
	`frontimageurl` varchar(100),
	`backimageurl` varchar(100),
	`timetolearn` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (user, setname, chapter, fronttext, backtext, frontimageurl, backimageurl),
    FOREIGN KEY (user, setname, chapter) REFERENCES SETS(user, name, chapter) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE = InnoDB;

CREATE TABLE `ksquared_db`.`ACTIVITY` (
  `user` VARCHAR(100) NOT NULL ,
  `setname` VARCHAR(100) NOT NULL ,
  `chapter` INT NOT NULL ,
  `time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
  );
  
CREATE TABLE `ksquared_db`.`STATS` (
`user` varchar(100) NOT NULL ,
`setname` varchar(100) NOT NULL ,
`chapter` int NOT NULL,
`time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user, setname, chapter) REFERENCES SETS(user, name, chapter) ON UPDATE CASCADE
)ENGINE = InnoDB;

ALTER TABLE `SETS`
  ADD PRIMARY KEY (`name`,`chapter`,`user`),
  ADD UNIQUE KEY `filename` (`filename`),
  ADD KEY `user` (`user`);
  
ALTER TABLE `SETS`
  ADD CONSTRAINT `SETS_ibfk_1` FOREIGN KEY (`user`) REFERENCES `USERS` (`user`) ON DELETE CASCADE;

ALTER TABLE `ACTIVITY`
  ADD CONSTRAINT `ACTIVITY_ibfk_1` FOREIGN KEY (`user`, `setname`, `chapter`) REFERENCES `SETS` (`user`, `name`, `chapter`);
  
  ALTER TABLE ACTIVITY DROP CONSTRAINT ACTIVITY_ibfk_1;
  
ALTER TABLE `LOGIN_TOKENS`
  ADD CONSTRAINT `LOGIN_TOKENS_ibfk_1` FOREIGN KEY (`user`) REFERENCES `USERS` (`user`) ON DELETE CASCADE;