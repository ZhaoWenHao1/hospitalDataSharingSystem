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

 Date: 17/06/2021 14:46:47
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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权限表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模拟数据表' ROW_FORMAT = Dynamic;

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

SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE `record`(
    `id` bigint(11) NOT NULL COMMENT 'record_id',
    `hash_data` varchar(255) DEFAULT NULL COMMENT '数据hash',
    `src_chain` varchar(255) DEFAULT NULL,
    `user` varchar(255) DEFAULT NULL,
    `dst_chain` varchar(255) DEFAULT NULL,
    `data_id` varchar(255) DEFAULT NULL,
    `type_tx` varchar(255) DEFAULT NULL,
    `this_tx_id` varchar(255) DEFAULT NULL,
    `last_tx_id` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE    
)ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
