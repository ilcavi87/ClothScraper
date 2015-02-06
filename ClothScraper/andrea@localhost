CREATE DATABASE  IF NOT EXISTS `cloth` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cloth`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: cloth
-- ------------------------------------------------------
-- Server version	5.6.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `idcategory` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `threadname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idcategory`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'http://shop.benetton.com/us_en/man/man/outerwear/overcoats.html','BenettonSkuEng');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `htmls`
--

DROP TABLE IF EXISTS `htmls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `htmls` (
  `idhtml` int(11) NOT NULL AUTO_INCREMENT,
  `idsku` int(11) DEFAULT NULL,
  `html` longtext,
  `entry_date` blob,
  PRIMARY KEY (`idhtml`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `htmls`
--

LOCK TABLES `htmls` WRITE;
/*!40000 ALTER TABLE `htmls` DISABLE KEYS */;
/*!40000 ALTER TABLE `htmls` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imgs`
--

DROP TABLE IF EXISTS `imgs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imgs` (
  `idimg` int(11) NOT NULL AUTO_INCREMENT,
  `idsku` int(11) DEFAULT NULL,
  `img` blob,
  `entry_date` datetime DEFAULT NULL,
  PRIMARY KEY (`idimg`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imgs`
--

LOCK TABLES `imgs` WRITE;
/*!40000 ALTER TABLE `imgs` DISABLE KEYS */;
/*!40000 ALTER TABLE `imgs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prices`
--

DROP TABLE IF EXISTS `prices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prices` (
  `idprice` int(11) NOT NULL,
  `idsku` int(11) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `currency` int(1) DEFAULT '0',
  `entry_date` datetime DEFAULT NULL,
  PRIMARY KEY (`idprice`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prices`
--

LOCK TABLES `prices` WRITE;
/*!40000 ALTER TABLE `prices` DISABLE KEYS */;
/*!40000 ALTER TABLE `prices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sizes`
--

DROP TABLE IF EXISTS `sizes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sizes` (
  `idsize` int(11) NOT NULL AUTO_INCREMENT,
  `idsku` int(11) DEFAULT NULL,
  `size` varchar(3) DEFAULT NULL,
  `avail` int(1) DEFAULT NULL,
  `entry_date` datetime DEFAULT NULL,
  PRIMARY KEY (`idsize`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sizes`
--

LOCK TABLES `sizes` WRITE;
/*!40000 ALTER TABLE `sizes` DISABLE KEYS */;
/*!40000 ALTER TABLE `sizes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skus`
--

DROP TABLE IF EXISTS `skus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skus` (
  `idsku` int(11) NOT NULL AUTO_INCREMENT,
  `idcategory` int(11) DEFAULT NULL,
  `url` text,
  `sku_code` varchar(60) DEFAULT NULL,
  `entry_date` datetime DEFAULT NULL,
  `description` text,
  `color` varchar(100) DEFAULT NULL,
  `fabric` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`idsku`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skus`
--

LOCK TABLES `skus` WRITE;
/*!40000 ALTER TABLE `skus` DISABLE KEYS */;
/*!40000 ALTER TABLE `skus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `websites`
--

DROP TABLE IF EXISTS `websites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `websites` (
  `idwebsite` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  `threadname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idwebsite`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `websites`
--

LOCK TABLES `websites` WRITE;
/*!40000 ALTER TABLE `websites` DISABLE KEYS */;
INSERT INTO `websites` VALUES (1,'http://shop.benetton.com/us_en/man.html','BenettonCategoriesEng');
/*!40000 ALTER TABLE `websites` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-12-05 15:51:34
