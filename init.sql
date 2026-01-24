CREATE DATABASE IF NOT EXISTS menagerie 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

DROP USER IF EXISTS 'signalm'@'%';
DROP USER IF EXISTS 'signalm'@'localhost';

CREATE USER 'signalm'@'%' 
IDENTIFIED WITH mysql_native_password BY '128201';

CREATE USER 'signalm'@'localhost' 
IDENTIFIED WITH mysql_native_password BY 'SignalPassword123!';

GRANT ALL PRIVILEGES ON menagerie.* TO 'signalm'@'%';
GRANT ALL PRIVILEGES ON menagerie.* TO 'signalm'@'localhost';

GRANT RELOAD, PROCESS, REPLICATION CLIENT ON *.* TO 'signalm'@'localhost';

CREATE USER 'app_user'@'%' 
IDENTIFIED WITH mysql_native_password BY 'AppPassword123!';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE TEMPORARY TABLES, EXECUTE 
ON menagerie.* TO 'app_user'@'%';

FLUSH PRIVILEGES;

USE menagerie;

DROP TABLE IF EXISTS system_status;

CREATE TABLE system_status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(100),
    status VARCHAR(50),
    last_check TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
) ENGINE=InnoDB;

INSERT INTO system_status (service_name, status, details) 
VALUES 
('MySQL Database', 'ACTIVE', 'Database initialized with mysql_native_password'),
('Docker Container', 'RUNNING', 'MySQL 8.0.34 with network access configured'),
('SignalM Application', 'READY', 'User signalm has access from all hosts');

SELECT '=== INITIALIZATION COMPLETE ===' as message;
SELECT CONCAT('Database: ', SCHEMA_NAME) as info FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'menagerie';
SELECT user, host, plugin FROM mysql.user WHERE user LIKE 'signalm%' OR user LIKE 'app_user%';
SELECT COUNT(*) as tables_count FROM information_schema.tables WHERE table_schema = 'menagerie';

-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: menagerie
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups` (
  `Kod` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`Kod`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `memolist`
--

DROP TABLE IF EXISTS `memolist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `memolist` (
  `Kod` int unsigned NOT NULL AUTO_INCREMENT,
  `KodTask` int unsigned NOT NULL DEFAULT '0',
  `_Date` int NOT NULL DEFAULT '0',
  `Date` datetime DEFAULT NULL,
  `Person` int NOT NULL DEFAULT '0',
  `Memo` text NOT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `filedata` longblob,
  `is_picture` tinyint(1) NOT NULL,
  PRIMARY KEY (`Kod`),
  KEY `Task` (`KodTask`)
) ENGINE=MyISAM AUTO_INCREMENT=132111 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `ID` int unsigned NOT NULL AUTO_INCREMENT,
  `group_id` int NOT NULL DEFAULT '0',
  `surname` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '',
  `name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '',
  `patronymic` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '',
  `nick` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '',
  `mail` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '',
  `icq` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '',
  `msn` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '',
  `phone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '',
  `comp` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '',
  `Active` tinyint unsigned NOT NULL DEFAULT '1',
  `ip` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '',
  `islist` tinyint(1) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  `username` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=120 DEFAULT CHARSET=koi8r;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasklist`
--

DROP TABLE IF EXISTS `tasklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasklist` (
  `Kod` int unsigned NOT NULL AUTO_INCREMENT,
  `KodPerson` tinyint unsigned NOT NULL DEFAULT '0',
  `_DateCreate` int unsigned DEFAULT '0',
  `datebegin` datetime DEFAULT NULL,
  `datecomplite` datetime DEFAULT NULL,
  `dateend` datetime DEFAULT NULL,
  `datecreate` datetime DEFAULT NULL,
  `_DateBegin` int NOT NULL DEFAULT '0',
  `Title` varchar(100) NOT NULL DEFAULT '',
  `_DateEnd` int NOT NULL DEFAULT '0',
  `Mark` tinyint unsigned NOT NULL DEFAULT '0',
  `Owner` tinyint unsigned NOT NULL DEFAULT '0',
  `_DateComplite` int NOT NULL DEFAULT '0',
  `Complite` tinyint(1) NOT NULL DEFAULT '0',
  `Memo` mediumtext,
  `flag_done` set('0','1') DEFAULT '0',
  `Fname` char(50) DEFAULT NULL,
  `isdone` tinyint(1) NOT NULL,
  `isviewed` tinyint(1) NOT NULL,
  PRIMARY KEY (`Kod`),
  KEY `MarkDate` (`Mark`,`_DateComplite`),
  KEY `KodPerson` (`KodPerson`),
  KEY `Owner` (`Owner`)
) ENGINE=MyISAM AUTO_INCREMENT=132065 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasklist__`
--

DROP TABLE IF EXISTS `tasklist__`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tasklist__` (
  `Kod` int unsigned NOT NULL AUTO_INCREMENT,
  `KodPerson` tinyint unsigned NOT NULL DEFAULT '0',
  `_DateCreate` int unsigned DEFAULT '0',
  `datebegin` datetime DEFAULT NULL,
  `datecomplite` datetime DEFAULT NULL,
  `dateend` datetime DEFAULT NULL,
  `datecreate` datetime DEFAULT NULL,
  `_DateBegin` int NOT NULL DEFAULT '0',
  `Title` varchar(100) NOT NULL DEFAULT '',
  `_DateEnd` int NOT NULL DEFAULT '0',
  `Mark` tinyint unsigned NOT NULL DEFAULT '0',
  `Owner` tinyint unsigned NOT NULL DEFAULT '0',
  `_DateComplite` int NOT NULL DEFAULT '0',
  `Complite` tinyint(1) NOT NULL DEFAULT '0',
  `Memo` mediumtext,
  `flag_done` set('0','1') DEFAULT '0',
  `Fname` char(50) DEFAULT NULL,
  `isdone` tinyint(1) NOT NULL,
  PRIMARY KEY (`Kod`),
  KEY `MarkDate` (`Mark`,`_DateComplite`),
  KEY `KodPerson` (`KodPerson`),
  KEY `Owner` (`Owner`)
) ENGINE=MyISAM AUTO_INCREMENT=98199 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `person_id` int DEFAULT NULL,
  `role_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


-- Dump completed on 2026-01-17  1:13:53
ALTER TABLE tasklist ENGINE = InnoDB;

CREATE INDEX idx_tasklist_kodperson_isdone ON tasklist(KodPerson, isDone);
CREATE INDEX idx_tasklist_kodperson_isdone_dateend_desc
    ON tasklist(KodPerson, isDone ASC, DateEnd DESC);


