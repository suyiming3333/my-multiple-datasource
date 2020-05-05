/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : dbone

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2020-05-05 11:54:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cs_teacher
-- ----------------------------
DROP TABLE IF EXISTS `cs_teacher`;
CREATE TABLE `cs_teacher` (
  `id` varchar(32) NOT NULL,
  `teachername` varchar(255) DEFAULT NULL,
  `classid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
