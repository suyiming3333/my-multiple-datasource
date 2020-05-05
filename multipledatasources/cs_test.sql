/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : dbone

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2020-05-05 11:54:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cs_test
-- ----------------------------
DROP TABLE IF EXISTS `cs_test`;
CREATE TABLE `cs_test` (
  `id` varchar(255) NOT NULL,
  `userid` varchar(255) DEFAULT NULL,
  `score` int(255) DEFAULT NULL,
  `classid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
