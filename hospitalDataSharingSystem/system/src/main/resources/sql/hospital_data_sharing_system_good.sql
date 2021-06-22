/*
 Navicat Premium Data Transfer

 Source Server         : 区块链55
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 211.69.198.55:3306
 Source Schema         : hospital_data_sharing_system

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 22/06/2021 16:13:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel`  (
  `id` bigint(11) NOT NULL COMMENT 'channelID',
  `channel_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'channel名称',
  `hospital_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'channel对应的医院名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel
-- ----------------------------
INSERT INTO `channel` VALUES (1, 'channel1', '部门A');
INSERT INTO `channel` VALUES (2, 'channel2', '部门B');

-- ----------------------------
-- Table structure for channel_authority
-- ----------------------------
DROP TABLE IF EXISTS `channel_authority`;
CREATE TABLE `channel_authority`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'channel权限id',
  `channel_id` bigint(11) NOT NULL COMMENT 'channelId',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `authority_key` bigint(11) NOT NULL COMMENT '权限字段 1代表添加权限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 153 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel_authority
-- ----------------------------
INSERT INTO `channel_authority` VALUES (137, 1, 139, 1);
INSERT INTO `channel_authority` VALUES (140, 1, 142, 2);
INSERT INTO `channel_authority` VALUES (141, 1, 142, 1);
INSERT INTO `channel_authority` VALUES (149, 1, 142, 1);
INSERT INTO `channel_authority` VALUES (150, 1, 142, 1);
INSERT INTO `channel_authority` VALUES (151, 2, 138, 1);
INSERT INTO `channel_authority` VALUES (152, 2, 145, 1);

-- ----------------------------
-- Table structure for channel_data_authority
-- ----------------------------
DROP TABLE IF EXISTS `channel_data_authority`;
CREATE TABLE `channel_data_authority`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'channel_data_authority_id',
  `user_id` bigint(20) NOT NULL COMMENT '发送者或拉取者id',
  `data_id` bigint(20) NOT NULL COMMENT '文件id',
  `channel_id` bigint(20) NOT NULL COMMENT 'channelID',
  `type` int(11) NOT NULL COMMENT '权限类型：1-push，2-pull',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of channel_data_authority
-- ----------------------------
INSERT INTO `channel_data_authority` VALUES (4, 142, 9, 2, 1);
INSERT INTO `channel_data_authority` VALUES (12, 142, 36, 1, 1);
INSERT INTO `channel_data_authority` VALUES (13, 142, 3, 2, 1);
INSERT INTO `channel_data_authority` VALUES (14, 142, 37, 2, 1);
INSERT INTO `channel_data_authority` VALUES (15, 142, 48, 2, 2);
INSERT INTO `channel_data_authority` VALUES (16, 140, 14, 2, 2);
INSERT INTO `channel_data_authority` VALUES (17, 142, 52, 2, 1);

-- ----------------------------
-- Table structure for data_authority
-- ----------------------------
DROP TABLE IF EXISTS `data_authority`;
CREATE TABLE `data_authority`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户权限id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `data_sample_id` bigint(20) NOT NULL COMMENT '模拟数据id',
  `authority_key` bigint(100) NOT NULL DEFAULT -1 COMMENT '用户权限 1代表查看文件 2代表修改文件 3代表删除文件 4 代表下载文件',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `data_sample_id`(`data_sample_id`) USING BTREE,
  CONSTRAINT `data_authority_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `data_authority_ibfk_2` FOREIGN KEY (`data_sample_id`) REFERENCES `data_sample` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_authority
-- ----------------------------
INSERT INTO `data_authority` VALUES (1, 140, 37, 1);
INSERT INTO `data_authority` VALUES (2, 140, 37, 2);
INSERT INTO `data_authority` VALUES (13, 142, 37, 4);
INSERT INTO `data_authority` VALUES (14, 142, 37, 1);
INSERT INTO `data_authority` VALUES (16, 142, 37, 2);
INSERT INTO `data_authority` VALUES (18, 140, 37, 4);
INSERT INTO `data_authority` VALUES (19, 142, 9, 1);
INSERT INTO `data_authority` VALUES (20, 142, 9, 2);
INSERT INTO `data_authority` VALUES (21, 142, 12, 4);
INSERT INTO `data_authority` VALUES (22, 142, 12, 1);
INSERT INTO `data_authority` VALUES (23, 142, 3, 1);
INSERT INTO `data_authority` VALUES (24, 142, 3, 2);
INSERT INTO `data_authority` VALUES (26, 142, 52, 1);
INSERT INTO `data_authority` VALUES (27, 142, 52, 2);
INSERT INTO `data_authority` VALUES (28, 142, 52, 4);
INSERT INTO `data_authority` VALUES (29, 145, 43, 1);
INSERT INTO `data_authority` VALUES (30, 145, 43, 2);
INSERT INTO `data_authority` VALUES (31, 145, 43, 4);
INSERT INTO `data_authority` VALUES (32, 145, 10, 1);
INSERT INTO `data_authority` VALUES (33, 145, 10, 4);
INSERT INTO `data_authority` VALUES (34, 145, 11, 1);
INSERT INTO `data_authority` VALUES (35, 145, 10, 2);
INSERT INTO `data_authority` VALUES (36, 145, 14, 1);
INSERT INTO `data_authority` VALUES (37, 145, 20, 2);
INSERT INTO `data_authority` VALUES (38, 145, 46, 2);
INSERT INTO `data_authority` VALUES (39, 145, 46, 1);
INSERT INTO `data_authority` VALUES (40, 145, 48, 1);
INSERT INTO `data_authority` VALUES (41, 145, 49, 2);
INSERT INTO `data_authority` VALUES (42, 145, 53, 1);
INSERT INTO `data_authority` VALUES (43, 145, 49, 1);
INSERT INTO `data_authority` VALUES (44, 145, 42, 1);
INSERT INTO `data_authority` VALUES (45, 145, 39, 1);
INSERT INTO `data_authority` VALUES (46, 145, 38, 1);
INSERT INTO `data_authority` VALUES (47, 145, 36, 1);
INSERT INTO `data_authority` VALUES (48, 145, 46, 4);
INSERT INTO `data_authority` VALUES (49, 145, 48, 4);
INSERT INTO `data_authority` VALUES (50, 145, 48, 2);
INSERT INTO `data_authority` VALUES (51, 145, 53, 2);
INSERT INTO `data_authority` VALUES (52, 145, 53, 4);
INSERT INTO `data_authority` VALUES (53, 145, 54, 1);
INSERT INTO `data_authority` VALUES (54, 145, 54, 2);
INSERT INTO `data_authority` VALUES (55, 145, 55, 1);
INSERT INTO `data_authority` VALUES (56, 145, 55, 2);
INSERT INTO `data_authority` VALUES (57, 142, 56, 1);
INSERT INTO `data_authority` VALUES (58, 142, 56, 2);

-- ----------------------------
-- Table structure for data_sample
-- ----------------------------
DROP TABLE IF EXISTS `data_sample`;
CREATE TABLE `data_sample`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `channel_id` bigint(11) NULL DEFAULT NULL COMMENT '该文件所属的channelID',
  `data_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `data_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `data_size` double NULL DEFAULT NULL COMMENT '文件大小',
  `mongo_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'mongoDB中的id',
  `origin_user_id` bigint(11) NULL DEFAULT NULL COMMENT '所属人id，对应user_id',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modified_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `shared_count` int(11) NULL DEFAULT 0 COMMENT '共享次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模拟数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_sample
-- ----------------------------
INSERT INTO `data_sample` VALUES (3, 1, '病历0X9527', '.txt文件', 0, '606bc69e16ed1d1281bb7c0f', 142, '2021-03-04 15:31:20', '2021-03-04 15:31:20', 0);
INSERT INTO `data_sample` VALUES (9, 1, 'YX_28237', '.txt文件', 0, '606bc69e16ed1d1281bb7c0f', 142, '2021-01-15 17:15:22', '2021-01-19 11:06:20', 2);
INSERT INTO `data_sample` VALUES (10, 2, '病历0X0081', '.txt文件', 9, '606bc69e16ed1d1281bb7c0f', 145, '2021-03-04 10:25:38', '2021-03-04 10:25:38', 0);
INSERT INTO `data_sample` VALUES (11, 2, '病历0X5318', '.txt文件', 1694, '606bc69e16ed1d1281bb7c0f', 145, '2021-03-04 11:01:58', '2021-03-04 11:01:58', 0);
INSERT INTO `data_sample` VALUES (12, 1, '病历0X3271', '.txt文件', 1694, '606bc69e16ed1d1281bb7c0f', 142, '2021-03-04 11:45:29', '2021-03-04 11:45:29', 0);
INSERT INTO `data_sample` VALUES (13, 1, '病历0X6712', '.txt文件', 0, '606bc69e16ed1d1281bb7c0f', 142, '2021-03-04 12:05:24', '2021-03-04 12:06:04', 0);
INSERT INTO `data_sample` VALUES (14, 2, '病历0X6712', '.txt文件', 0, '606bc69e16ed1d1281bb7c0f', 145, '2021-03-04 12:08:08', '2021-03-04 12:09:01', 0);
INSERT INTO `data_sample` VALUES (15, 1, 'BC_UJ812', '.txt文件', 9, '606bc69e16ed1d1281bb7c0f', 142, '2021-03-04 14:37:48', '2021-03-04 14:37:48', 0);
INSERT INTO `data_sample` VALUES (16, 2, 'HY_83271', '.txt文件', 9, '606bc69e16ed1d1281bb7c0f', 145, '2021-03-04 14:38:46', '2021-03-04 14:38:46', 0);
INSERT INTO `data_sample` VALUES (17, 1, 'HY_72731', '.txt文件', 0, '606bc69e16ed1d1281bb7c0f', 142, '2021-03-04 15:28:29', '2021-03-04 15:28:29', 0);
INSERT INTO `data_sample` VALUES (18, 1, '病历0X8237', '.txt文件', 0, '606bc69e16ed1d1281bb7c0f', 142, '2021-03-04 15:30:18', '2021-03-04 15:30:18', 0);
INSERT INTO `data_sample` VALUES (20, 2, '病历0X7123', '.txt文件', 18, '606bc69e16ed1d1281bb7c0f', 145, '2021-03-22 15:10:10', '2021-03-22 15:10:10', 0);
INSERT INTO `data_sample` VALUES (35, 2, 'vbox.txt', '.txt文件', 1694, '606bc69e16ed1d1281bb7c0f', 145, '2021-03-22 18:47:37', '2021-03-22 18:47:37', 0);
INSERT INTO `data_sample` VALUES (36, 2, '病历0X3271', '.txt文件', 18, '606bc69e16ed1d1281bb7c0f', 145, '2021-03-22 18:47:53', '2021-03-22 18:47:53', 0);
INSERT INTO `data_sample` VALUES (37, 1, 'DSC_6563', '.txt文件', 1, '606bc69e16ed1d1281bb7c0f', 142, '2021-03-22 21:45:35', '2021-05-12 11:28:18', 0);
INSERT INTO `data_sample` VALUES (38, 2, 'fabric1.4实验指导手册.doc', '.doc文件', 2298368, '606bc69e16ed1d1281bb7c0f', 145, '2021-04-19 12:53:21', '2021-04-19 12:53:21', 0);
INSERT INTO `data_sample` VALUES (39, 2, 'fabric1.4实验指导手册.doc', '.doc文件', 2298368, '606bc69e16ed1d1281bb7c0f', 145, '2021-04-19 12:53:58', '2021-04-19 12:53:58', 0);
INSERT INTO `data_sample` VALUES (40, 1, 'react目录文件.png', '.png文件', 117308, '606bc69e16ed1d1281bb7c0f', 140, '2021-04-19 15:08:39', '2021-04-19 15:08:39', 0);
INSERT INTO `data_sample` VALUES (41, 1, 'git.png', '.png文件', 44328, '606bc69e16ed1d1281bb7c0f', 140, '2021-04-19 15:11:00', '2021-04-19 15:11:00', 0);
INSERT INTO `data_sample` VALUES (42, 2, 'fabric1.4实验指导手册.doc', '.doc文件', 2298368, '606bc69e16ed1d1281bb7c0f', 145, '2021-04-26 16:22:57', '2021-04-26 16:22:57', 0);
INSERT INTO `data_sample` VALUES (43, 2, '病历0X9527_copy', '.txt文件', 1413, '60ab874225a6e479f44e1dcc', 142, '2021-05-24 19:00:19', '2021-05-24 19:00:19', 0);
INSERT INTO `data_sample` VALUES (46, 2, '病历0X9527_copy', '.txt文件', 1413, '60acf64902cd6473edd96cc5', 145, '2021-05-25 21:06:18', '2021-05-25 21:06:18', 0);
INSERT INTO `data_sample` VALUES (48, 2, 'YX_28237_copy', '.txt文件', 1413, '60acfaf705a90549fd086b33', 145, '2021-05-25 21:26:16', '2021-05-25 21:26:16', 0);
INSERT INTO `data_sample` VALUES (49, 2, 'YX_28237_copy', '.txt文件', 1413, '60acfcb605a90549fd086b34', 145, '2021-05-25 21:33:42', '2021-05-25 21:33:42', 0);
INSERT INTO `data_sample` VALUES (52, 1, '病历_0616.txt', '.txt文件', 0, '60cb00d5761be8059575b060', 142, '2021-06-17 15:59:17', '2021-06-17 16:17:58', 1);
INSERT INTO `data_sample` VALUES (53, 2, '病历_0616.txt_copy', '.txt文件', 53, '60cb0a871842050b306508aa', 145, '2021-06-17 16:40:40', '2021-06-17 16:40:40', 0);
INSERT INTO `data_sample` VALUES (54, 2, '病历_患者xx.txt', '.txt文件', 0, '60cb0c8fa18742580f71a869', 145, '2021-06-17 16:49:19', '2021-06-17 16:49:19', 0);
INSERT INTO `data_sample` VALUES (55, 2, 'CTxxxx01.txt', '.txt文件', 0, '60cb0d54a18742580f71a86a', 145, '2021-06-17 16:52:36', '2021-06-17 19:49:20', 0);
INSERT INTO `data_sample` VALUES (56, 2, '报告TJ0723.txt', '.txt文件', 0, '60cb56b5cc6458508c57e9e9', 142, '2021-06-17 22:05:41', '2021-06-17 22:06:24', 0);

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'record_id',
  `hash_data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据hash',
  `src_chain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dst_chain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type_tx` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `this_tx_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `last_tx_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, 'hash', NULL, NULL, NULL, '1', NULL, NULL, NULL, '2021-06-23 15:37:55');
INSERT INTO `record` VALUES (2, 'hash', NULL, NULL, NULL, '1', NULL, NULL, NULL, '2021-06-09 15:38:05');
INSERT INTO `record` VALUES (3, NULL, NULL, NULL, NULL, '1', NULL, NULL, NULL, '2021-06-25 15:38:12');
INSERT INTO `record` VALUES (4, '-370320174', 'channel1', 'org3_user', 'channel2', '52', 'add', '416235556', '0', '2021-06-17 15:59:17');
INSERT INTO `record` VALUES (5, '-369176797', 'channel1', 'org3_user', 'channel2', '52', 'read', '423096889', '416235556', '2021-06-17 16:08:47');
INSERT INTO `record` VALUES (6, '-369176797', 'channel1', 'org3_user', 'channel2', '52', 'read', '422768618', '423096889', '2021-06-17 16:12:03');
INSERT INTO `record` VALUES (7, '-369176797', 'channel1', 'org3_user', 'channel2', '52', 'read', '422771028', '422768618', '2021-06-17 16:12:05');
INSERT INTO `record` VALUES (8, '-369176797', 'channel1', 'org3_user', 'channel2', '52', 'read', '423607378', '422771028', '2021-06-17 16:17:18');
INSERT INTO `record` VALUES (9, '-303071427', 'channel1', 'org3_user', 'channel2', '52', 'modify', '423123493', '423607378', '2021-06-17 16:17:58');
INSERT INTO `record` VALUES (10, '1306293962', 'channel1', 'org3_user', 'channel1', '52', 'download', '422532739', '423123493', '2021-06-17 16:34:20');
INSERT INTO `record` VALUES (11, '1306293962', 'channel1', 'org3_user', 'channel1', '52', 'read', '422551517', '422532739', '2021-06-17 16:34:38');
INSERT INTO `record` VALUES (12, '931844766', 'channel1', 'org3_user', 'channel1', '3', 'read', '421381817', '0', '2021-06-17 16:38:06');
INSERT INTO `record` VALUES (13, '1539112840', 'channel1', 'org3_user', 'channel1', '9', 'read', '420995002', '0', '2021-06-17 16:38:11');
INSERT INTO `record` VALUES (14, '1361629513', 'channel1', 'org3_user', 'channel1', '12', 'read', '420669181', '0', '2021-06-17 16:38:14');
INSERT INTO `record` VALUES (15, '887284868', 'channel2', 'org5_user', 'channel2', '53', 'read', '420645259', '0', '2021-06-17 16:47:39');
INSERT INTO `record` VALUES (16, '887284868', 'channel2', 'org5_user', 'channel2', '53', 'read', '420662285', '420645259', '2021-06-17 16:47:57');
INSERT INTO `record` VALUES (17, '887284868', 'channel2', 'org5_user', 'channel2', '53', 'read', '420667508', '420662285', '2021-06-17 16:48:02');
INSERT INTO `record` VALUES (18, '-605810720', 'channel2', 'org5_user', 'channel2', '54', 'add', '421465746', '0', '2021-06-17 16:49:19');
INSERT INTO `record` VALUES (19, '452218951', 'channel2', 'org5_user', 'channel2', '55', 'add', '421204416', '0', '2021-06-17 16:52:36');
INSERT INTO `record` VALUES (20, '451850748', 'channel2', 'org5_user', 'channel2', '55', 'read', '435854913', '421204416', '2021-06-17 19:49:04');
INSERT INTO `record` VALUES (21, '887284868', 'channel2', 'org5_user', 'channel2', '53', 'read', '435729879', '420667508', '2021-06-17 19:49:09');
INSERT INTO `record` VALUES (22, '451850748', 'channel2', 'org5_user', 'channel2', '55', 'read', '435867844', '435854913', '2021-06-17 19:49:17');
INSERT INTO `record` VALUES (23, '1077450817', 'channel2', 'org5_user', 'channel2', '55', 'modify', '435870941', '435867844', '2021-06-17 19:49:20');
INSERT INTO `record` VALUES (24, '-1493159370', 'channel1', 'org3_user', 'channel2', '56', 'add', '443724419', '0', '2021-06-17 22:05:41');
INSERT INTO `record` VALUES (25, '-1493304553', 'channel1', 'org3_user', 'channel2', '56', 'read', '443731135', '443724419', '2021-06-17 22:05:48');
INSERT INTO `record` VALUES (26, '1717271980', 'channel1', 'org3_user', 'channel2', '56', 'modify', '443768292', '443731135', '2021-06-17 22:06:24');

-- ----------------------------
-- Table structure for shared_data_authority
-- ----------------------------
DROP TABLE IF EXISTS `shared_data_authority`;
CREATE TABLE `shared_data_authority`  (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '分享记录id',
  `share_user_id` int(255) NULL DEFAULT NULL COMMENT '分享者id',
  `shared_user_id` int(255) NULL DEFAULT NULL COMMENT '被分享者id',
  `shared_data_id` int(255) NULL DEFAULT NULL COMMENT '分享文件id',
  `authority_key` int(255) NULL DEFAULT NULL COMMENT '权限key 1代表查看',
  `accept_or_not` int(255) NULL DEFAULT NULL COMMENT '是否同意，同意为1，不同意为0，刚开始为不同意',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shared_data_authority
-- ----------------------------
INSERT INTO `shared_data_authority` VALUES (34, 132, 131, 9, 1, 1);
INSERT INTO `shared_data_authority` VALUES (35, 138, 137, 14, 1, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `channel_id` int(11) NULL DEFAULT NULL COMMENT '该用户所在channel的id',
  `fabric_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'fabric 用户id',
  `is_admin` int(255) NULL DEFAULT NULL COMMENT '1代表是管理员，0代表不是管理员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 147 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (139, 'org2_admin', 'admin', 1, 'org2_admin', 1);
INSERT INTO `user` VALUES (140, 'org2_user', '123', 1, 'org2_user', 0);
INSERT INTO `user` VALUES (141, 'org3_admin', 'admin', 1, 'org3_admin', 1);
INSERT INTO `user` VALUES (142, 'org3_user', '123', 1, 'org3_admin', 0);
INSERT INTO `user` VALUES (143, 'org4_admin', 'admin', 2, 'org4_admin', 1);
INSERT INTO `user` VALUES (144, 'org5_admin', 'admin', 2, 'org5_admin', 1);
INSERT INTO `user` VALUES (145, 'org5_user', '123', 2, 'org5_user', 0);
INSERT INTO `user` VALUES (146, 'org1_admin', 'admin', 0, 'org1_admin', 1);

-- ----------------------------
-- Table structure for user_channel_role
-- ----------------------------
DROP TABLE IF EXISTS `user_channel_role`;
CREATE TABLE `user_channel_role`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `channel_id` int(11) NOT NULL COMMENT 'channelID',
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该用户在该channel的角色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_channel_role
-- ----------------------------
INSERT INTO `user_channel_role` VALUES (1, 100, 1, '普通用户');
INSERT INTO `user_channel_role` VALUES (2, 1001, 1, 'da');

SET FOREIGN_KEY_CHECKS = 1;
