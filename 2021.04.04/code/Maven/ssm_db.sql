/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.40 : Database - ssm_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ssm_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ssm_db`;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `uuid` int(10) NOT NULL AUTO_INCREMENT,
  `userName` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `realName` varchar(100) DEFAULT NULL,
  `gender` int(1) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`uuid`,`userName`,`password`,`realName`,`gender`,`birthday`) values (6,'Jockme','root','JockIsMe',1,'1980-07-25'),(7,'Jock','root','Jockme',1,'1980-07-25'),(9,'Jock','root','Jockme',1,'1980-07-25'),(10,'Jock','root','Jockme',1,'1980-07-25'),(12,NULL,NULL,'lili',1,'1994-07-16'),(13,'Jock','root','Jockme',1,'1980-07-25'),(14,'Jock','root','Jockme',1,'1980-07-25'),(15,'Jock','root','Jockme',1,'1980-07-25'),(16,'Jock','root','Jockme',1,'1980-07-25'),(17,'Jock','root','Jockme',1,'1980-07-25'),(18,'Jock','root','Jockme',1,'1980-07-25');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
