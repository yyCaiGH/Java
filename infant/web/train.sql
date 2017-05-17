/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.5.20 : Database - train
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`train` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `train`;

/*Table structure for table `t_admin` */

DROP TABLE IF EXISTS `t_admin`;

CREATE TABLE `t_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '管理员名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_admin` */

/*Table structure for table `t_apply` */

DROP TABLE IF EXISTS `t_apply`;

CREATE TABLE `t_apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '报名记录',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id（注册才有）',
  `wx_account` varchar(64) DEFAULT NULL COMMENT '微信号',
  `cd_key` varchar(6) DEFAULT NULL COMMENT '验证码（缴费完成，让培训机构验证）',
  `user_name` varchar(32) NOT NULL COMMENT '报名者姓名',
  `user_phone` varchar(11) NOT NULL COMMENT '报名者电话',
  `org_id` varchar(11) NOT NULL COMMENT '报名的培训机构id',
  `create_time` datetime NOT NULL COMMENT '报名时间',
  `pay_time` datetime DEFAULT NULL COMMENT '确认付款时间（培训机构确认）',
  `user_sure_pay` tinyint(1) DEFAULT '0' COMMENT '用户确认付款',
  `org_sure_pay` tinyint(1) DEFAULT '0' COMMENT '培训机构确认付款',
  `status` int(2) NOT NULL COMMENT '状态（1：已报名，2：愿意培训，3：放弃培训）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

/*Data for the table `t_apply` */

insert  into `t_apply`(`id`,`user_id`,`wx_account`,`cd_key`,`user_name`,`user_phone`,`org_id`,`create_time`,`pay_time`,`user_sure_pay`,`org_sure_pay`,`status`) values (1,NULL,'cyy418ll','123456','蔡先生','18650346869','1','2017-02-05 00:32:07','2017-02-07 21:49:07',1,1,2),(6,NULL,'12351','123457','邻里','15880016811','2','2017-02-05 00:32:13',NULL,1,0,3),(7,NULL,'cyy418ll','123458','123','18650346861','1','2017-02-23 00:32:17',NULL,1,0,3),(8,NULL,'xxx','123459','xxxx','18650346768','1','2017-03-22 00:32:21',NULL,0,0,3),(9,NULL,'123','37D9MY','王验证','18650346868','2','2017-02-06 20:24:46','2017-02-07 01:12:22',0,1,2),(10,NULL,'11','TDPD1Z','林','18650346860','1','2017-02-07 23:15:21','2017-02-07 23:24:23',1,1,2),(11,NULL,NULL,'T0BNJR','菜12345','18650346866','3','2017-02-07 23:42:04',NULL,0,0,1),(12,NULL,'123','9SJTH3','蔡先生2号','18650346869','2','2017-02-21 10:08:55',NULL,1,0,1),(13,NULL,'123','Y2QS5R','cyy','18650346860','1','2017-02-21 23:42:49','2017-02-21 23:48:50',1,1,2),(14,NULL,NULL,'HBERPN','cyy3','18650346862','1','2017-02-21 23:57:13',NULL,0,0,3),(15,NULL,'fff','1APWZL','fff','18650346865','1','2017-02-22 00:12:51',NULL,1,0,3),(16,NULL,'11','3SW54I','林','15880016811','1','2017-02-22 23:14:29','2017-02-22 23:19:54',1,1,2),(17,NULL,NULL,'QNS3CQ','林','15880016812','1','2017-02-22 23:21:19',NULL,0,0,1);

/*Table structure for table `t_exam` */

DROP TABLE IF EXISTS `t_exam`;

CREATE TABLE `t_exam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `score` int(3) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_exam` */

/*Table structure for table `t_organization` */

DROP TABLE IF EXISTS `t_organization`;

CREATE TABLE `t_organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(32) NOT NULL COMMENT '后台登陆账号',
  `password` varchar(32) NOT NULL COMMENT '后台登陆密码',
  `name` varchar(64) NOT NULL COMMENT '培训机构名称',
  `phone` varchar(11) DEFAULT NULL COMMENT '培训机构电话',
  `province` varchar(32) DEFAULT NULL COMMENT '省',
  `city` varchar(32) DEFAULT NULL COMMENT '市',
  `area` varchar(32) DEFAULT NULL COMMENT '县/区',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `t_organization` */

insert  into `t_organization`(`id`,`account`,`password`,`name`,`phone`,`province`,`city`,`area`,`address`) values (1,'dfrz','111111','东方锐智','18888888888','福建省','福州市','闽侯区','海西园创业大厦7层'),(2,'zypx','111111','卓跃培训','18888888888','福建省','福州市','鼓楼区',NULL),(3,'dnpx','111111','达内培训','18888888888',NULL,NULL,NULL,NULL);

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '用户名称',
  `phone` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `open_id` varchar(64) DEFAULT NULL COMMENT '微信openid',
  `headimgurl` varchar(255) DEFAULT NULL COMMENT '头像地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`name`,`phone`,`open_id`,`headimgurl`) values (1,'Y_C',NULL,'ovaVsv8Fzn5qvp2SQIhYh8bxf_ww','http://wx.qlogo.cn/mmopen/ajNVdqHZLLCOicLsIrnfvHw7xMayYUMA7c7aiceoV7w43fHlxEepL33fVHPZR3HXsxENzxMn7j8EgyGwnTCXib9Yg/0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
