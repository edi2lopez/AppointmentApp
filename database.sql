
CREATE DATABASE WGUTEST;

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `addressId` int(10) NOT NULL,
  `address` varchar(50) NOT NULL,
  `address2` varchar(50) NOT NULL,
  `cityId` int(10) NOT NULL,
  `postalCode` varchar(10) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`addressId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
  `appointmentId` int(10) NOT NULL,
  `customerId` int(10) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `location` text NOT NULL,
  `contact` text NOT NULL,
  `url` varchar(255) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`appointmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `cityId` int(10) NOT NULL,
  `city` varchar(50) NOT NULL,
  `countryId` int(10) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`cityId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `countryId` int(10) NOT NULL,
  `country` varchar(50) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`countryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customerId` int(10) NOT NULL,
  `customerName` varchar(45) NOT NULL,
  `addressId` int(10) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `incrementtypes`;
CREATE TABLE `incrementtypes` (
  `incrementTypeId` int(11) NOT NULL,
  `incrementTypeDescription` varchar(45) NOT NULL,
  PRIMARY KEY (`incrementTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `reminder`;
CREATE TABLE `reminder` (
  `reminderId` int(10) NOT NULL,
  `reminderDate` datetime NOT NULL,
  `snoozeIncrement` int(11) NOT NULL,
  `snoozeIncrementTypeId` int(11) NOT NULL,
  `appointmentId` int(10) NOT NULL,
  `createdBy` varchar(45) NOT NULL,
  `createdDate` datetime NOT NULL,
  `remindercol` varchar(45) NOT NULL,
  PRIMARY KEY (`reminderId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `active` tinyint(4) NOT NULL,
  `createBy` varchar(40) NOT NULL,
  `createDate` datetime NOT NULL,
  `lastUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastUpdatedBy` varchar(50) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `user` VALUES 
	(1,'demo','demo',1,'edi',CURDATE(),NOW(),'edi'),
	(2,'edi','demo',1,'edi',CURDATE(),NOW(),'edi'),
	(3,'john','demo',1,'edi',CURDATE(),NOW(),'edi'),
	(4,'jane','demo',1,'edi',CURDATE(),NOW(),'edi');

INSERT INTO `country` VALUES 
	(1,'United States',CURDATE(),'edi',NOW(),'edi'),
	(2,'England',CURDATE(),'john',NOW(),'john');

INSERT INTO `city` VALUES 
	(1,'London',2,CURDATE(),'edi',NOW(),'edi'),
	(2,'Phoenix',1,CURDATE(),'john',NOW(),'john'),
	(3,'New York',1,CURDATE(),'edi',NOW(),'edi');
	
INSERT INTO `address` VALUES 
	(1,'11 Testing Ave','Unit 7',1,'60888','800-555-9991',CURDATE(),'edi',NOW(),'edi'),
	(2,'22 Camino Real St','',2,'74000','888-963-4444',CURDATE(),'edi',NOW(),'edi'),
	(3,'33 Other Street Blvd','Apt 18',3,'91000','748-888-7412',CURDATE(),'john',NOW(),'john');

INSERT INTO `customer` VALUES 
	(1,'John Doe',1,1,CURDATE(),'edi',NOW(),'edi'),
	(2,'Jane Perez',2,1,CURDATE(),'john',NOW(),'john');

INSERT INTO `appointment` VALUES (
	1,
	1,
	'Marketing Meeting',
	'Marketing department meeting',
	'New York Office',
	'Some Contact',
	'http://test.com',
	'2018-06-15 11:30:00',
	'2018-07-15 13:30:00',
	CURDATE(),
	'edi',
	NOW(),
	'edi'
);

INSERT INTO `appointment` (
	`appointmentId`, 
	`customerId`,
	`title`, 
	`description`, 
	`location`, 
	`contact`, 
	`url`, 
	`start`, 
	`end`, 
	`createDate`, 
	`createdBy`, 
	`lastUpdate`, 
	`lastUpdateBy`
) VALUES (
	'2', 
	'2', 
	'Accounting Meeting', 
	'Accounting Department meeting', 
	'Phoenix Office', 
	'Jane', 
	'http://example.com', 
	'2018-07-15 11:30:00', 
	'2018-06-15 13:30:00', 
	CURDATE(), 
	'John', 
	NOW(), 
	'John'
);