/*
Navicat MySQL Data Transfer

Source Server         : root(3307)
Source Server Version : 50719
Source Host           : localhost:3307
Source Database       : ncsp

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-03-10 12:39:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `reward_info`
-- ----------------------------
DROP TABLE IF EXISTS `reward_info`;
CREATE TABLE `reward_info` (
  `ID` varchar(32) NOT NULL,
  `money` decimal(15,0) DEFAULT NULL,
  `reward_time` varchar(20) DEFAULT NULL,
  `video_id` varchar(32) DEFAULT NULL,
  `record_time` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reward_info
-- ----------------------------
INSERT INTO `reward_info` VALUES ('0', '100', '2017-12-25 00:00:00', 'abc', '2017-12-25 00:00:00');

-- ----------------------------
-- Table structure for `sys_commondatas`
-- ----------------------------
DROP TABLE IF EXISTS `sys_commondatas`;
CREATE TABLE `sys_commondatas` (
  `ID_` varchar(32) NOT NULL,
  `ATTRIBUTE_` varchar(100) DEFAULT NULL,
  `DESC_` varchar(500) DEFAULT NULL,
  `EXTEND_` varchar(300) DEFAULT NULL,
  `LABEL_` varchar(200) NOT NULL,
  `NAME_` varchar(100) DEFAULT NULL,
  `ORDER_` int(11) DEFAULT NULL,
  `VALUE_` varchar(200) DEFAULT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_commondatas
-- ----------------------------
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69b052450009', null, '系统公共模块配置，包括：用户管理、部门管理、资源管理、基础数据管理等等。', null, '系统公共模块管理', 'systemPublicModule', '0', 'systemPublicModule', null);
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bb8c71a0000', null, '(自动生成请勿修改);该节点用于维护不同项目的登录界面和主界面的风格以及信息，请在项目信息维护界面修改此目录下配置\");', null, '项目信息维护', 'projectInfo', '8', 'projectInfo', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9be9fa1b0001', null, '请将自定义页面存放在/pages/login目录下', null, '自定义登录页面URL', 'customLoginPageURL', '0', 'login/default/index.jsp', '40285c813e9b658c013e9bb8c71a0000');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bea550c0002', null, '保存主界面头部以及底部自定义信息', null, '主界面配置信息', 'mainPageConfig', '1', 'mainPageConfig', '40285c813e9b658c013e9bb8c71a0000');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9beb23f50004', null, '头部图片URL', null, '头部图片', 'mainHeadBackImg', '0', '/static/images/themes/blue/main/headbg.png', '40285c813e9b658c013e9bea550c0002');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9beb84e30005', null, '底部项目信息', null, '底部项目信息', 'mainFootInfo', '1', '浙江网络投诉平台体验版', '40285c813e9b658c013e9bea550c0002');
INSERT INTO `sys_commondatas` VALUES ('40285c813eaaec97013eaaee1f300000', null, '标题', null, '标题', 'mainTitle', '2', '浙江网络投诉平台体验版', '40285c813e9b658c013e9bea550c0002');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9beaaafe0003', null, '默认登录界面配置信息，值:背景图片位置。属性:文字信息。备注:位置信息', null, '默认登录界面配置信息', 'defaultLoginConfig', '2', 'defaultLoginConfig', '40285c813e9b658c013e9bb8c71a0000');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bebe0e40006', null, '1-1  312*148', null, '位置1配置', 'loginPagePosition1', '0', '/pages/login/default/images/chinaMobile.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bee606f0007', null, '1-2  312*148', null, '位置2配置', 'loginPagePosition2', '1', '/pages/login/default/images/landScape.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9beeeffd0008', '中国移动通信集团<br>浙江有限公司', '1-3  150*148', null, '位置3配置', 'loginPagePosition3', '2', '/pages/login/default/images/purplishredBg_oneSixth.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bef40130009', null, '1-4  150*148', null, '位置4配置', 'loginPagePosition4', '3', '/pages/login/default/images/greenBg_oneSixth.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9befbfff000a', null, '2-1  150*148', null, '位置5配置', 'loginPagePosition5', '4', '/pages/login/default/images/servicer.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bf00be8000b', null, '2-2  474*148', null, '位置6配置', 'loginPagePosition6', '5', '/pages/login/default/images/logo_aHalf.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bf0b15b000c', null, '2-3  312*308', null, '位置7配置', 'loginPagePosition7', '6', '/pages/login/default/images/mainRight.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bf10f0f000d', null, '3-1  150*148', null, '位置8配置', 'loginPagePosition8', '7', '/pages/login/default/images/flower.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bf18464000e', '杭州东方通信软件<br>技术有限公司<br>产品与技术服务', '3-2  150*148', null, '位置9配置', 'loginPagePosition9', '8', '/pages/login/default/images/purplishredBg_oneSixth.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bf1e39a000f', null, '3-3  312*148', null, '位置10配置', 'loginPagePosition10', '9', '/pages/login/default/images/greenBg_oneThird.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813e9b658c013e9bf230b80010', null, '1440*900', null, '登录界面背景图片', 'loginPageBackImg', '10', '/pages/login/default/images/background.jpg', '40285c813e9b658c013e9beaaafe0003');
INSERT INTO `sys_commondatas` VALUES ('40285c813ebf64f5013ebf66edd60000', null, '国际化相关配置', null, '国际化', 'i18n', '9', 'i18n', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('40285c813ebf64f5013ebf68052a0001', null, null, null, '国际化地区', 'i18n_locals', '0', 'i18n_locals', '40285c813ebf64f5013ebf66edd60000');
INSERT INTO `sys_commondatas` VALUES ('40285c813ebf64f5013ebf68cc350002', null, null, null, '简体中文', 'zh_CN', '0', 'zh_CN', '40285c813ebf64f5013ebf68052a0001');
INSERT INTO `sys_commondatas` VALUES ('40285c813ebf64f5013ebf699c050003', null, null, null, '英文', 'en_US', '1', 'en_US', '40285c813ebf64f5013ebf68052a0001');
INSERT INTO `sys_commondatas` VALUES ('40285c813ebf64f5013ebf6a2e840004', null, null, null, '当前语言地区', 'current_local', '1', 'zh_CN', '40285c813ebf64f5013ebf66edd60000');
INSERT INTO `sys_commondatas` VALUES ('8a88049c3c6a6b46013c6b27ab50004d', null, '配置远程应用对应的服务器。', null, '远程应用服务器', 'remoteServer', '6', 'remoteServer', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8a88049c3c6a6b46013c6b289d76004f', null, '网投投诉应用平台远程服务器地址配置。', null, '网投投诉应用平台', 'ecip', '0', 'http://10.8.132.201:8080/ecip/', '8a88049c3c6a6b46013c6b27ab50004d');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69bc26a9000e', null, '角色管理相关配置', null, '角色管理', 'roleMng', '0', 'roleMng', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69bcde470010', null, '所有地市', null, '地市', 'region', '0', 'region', '8a8804cf3b6988e3013b69bc26a9000e');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addf8fb7a0005', null, null, null, '杭州', 'hz', '0', '01', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addf92b890007', null, null, null, '湖州', 'hh', '1', '02', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addf952020009', null, null, null, '嘉兴', 'jx', '2', '03', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addf97d52000b', null, null, null, '绍兴', 'sx', '3', '04', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addf9e412000d', null, null, null, '宁波', 'nb', '4', '05', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addfa651e000f', null, null, null, '金华', 'jh', '5', '06', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addfa9b950011', null, null, null, '衢州', 'qz', '6', '07', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addfaca030013', null, null, null, '台州', 'tz', '7', '08', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addfafd240015', null, null, null, '舟山', 'zs', '8', '09', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addfb5ee70017', null, null, null, '温州', 'wz', '9', '10', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804493addef02013addfb809c0019', null, null, null, '丽水', 'ls', '10', '11', '8a8804cf3b6988e3013b69bcde470010');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69dbf66d0012', null, '当前超级管理员角色名称', null, '当前超级管理员角色', 'currentAdminName', '1', 'dxadmin', '8a8804cf3b6988e3013b69bc26a9000e');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e0b0610015', null, '用户管理相关配置信息。', null, '用户管理', 'userMng', '1', 'userMng', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e3119d0017', null, null, null, '用户级别', 'userLevel', '0', 'userLevel', '8a8804cf3b6988e3013b69e0b0610015');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e43d16001d', null, null, null, '超级管理员', 'userlevel1', '2', '1', '8a8804cf3b6988e3013b69e3119d0017');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e48e32001f', null, null, null, '普通管理员', 'userlevel2', '1', '2', '8a8804cf3b6988e3013b69e3119d0017');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e4c6e60021', null, null, null, '一般操作人员', 'userlevel3', '0', '3', '8a8804cf3b6988e3013b69e3119d0017');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e347e60019', null, null, null, '性别', 'sex', '1', 'sex', '8a8804cf3b6988e3013b69e0b0610015');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e572fa0023', null, null, null, '女', 'female', '1', '0', '8a8804cf3b6988e3013b69e347e60019');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e5aaba0025', null, null, null, '男', 'male', '0', '1', '8a8804cf3b6988e3013b69e347e60019');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e38a2a001b', null, null, null, '账号是否可用', 'accountEnabled', '2', 'accountEnabled', '8a8804cf3b6988e3013b69e0b0610015');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e632020027', null, null, null, '可用', 'enabled', '0', '1', '8a8804cf3b6988e3013b69e38a2a001b');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e6718b0029', null, null, null, '不可用', 'disabled', '1', '0', '8a8804cf3b6988e3013b69e38a2a001b');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e7af61002b', null, '日志管理相关配置信息。', null, '日志管理', 'logMng', '2', 'logMng', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e82a90002d', null, null, null, '操作状态', 'operateStatus', '0', 'operateStatus', '8a8804cf3b6988e3013b69e7af61002b');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e86eec002f', null, null, null, '成功', 'success', '0', '1', '8a8804cf3b6988e3013b69e82a90002d');
INSERT INTO `sys_commondatas` VALUES ('8a8804cf3b6988e3013b69e8a5b20031', null, null, null, '失败', 'failure', '1', '0', '8a8804cf3b6988e3013b69e82a90002d');
INSERT INTO `sys_commondatas` VALUES ('8ac6d84c3b6f72b5013b6f737b230000', null, null, null, '鉴权配置管理', 'authConfig', '5', 'authConfig', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8ac6d84c3b6f72b5013b6f73c7e40002', null, null, null, '有效时间间隔', 'timeInterval', '0', '2', '8ac6d84c3b6f72b5013b6f737b230000');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939b837b40139b84ad1b60006', null, '公告通知配置数据', null, '公告通知配置数据', 'notificationData', '4', 'notificationData', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8a88044939d2b4030139d2babbe90008', null, '公告通知是否置顶.', null, '公告通知是否置顶', 'notificationIsTop', '2', 'notificationIsTop', '8ac6d85939b837b40139b84ad1b60006');
INSERT INTO `sys_commondatas` VALUES ('8a88044939d2b4030139d2bb1feb000a', null, '.', null, '置顶', 'Yes', '0', 'Y', '8a88044939d2b4030139d2babbe90008');
INSERT INTO `sys_commondatas` VALUES ('8a88044939d2b4030139d2bb69fb000c', null, '.', null, '不置顶', 'No', '0', 'N', '8a88044939d2b4030139d2babbe90008');
INSERT INTO `sys_commondatas` VALUES ('8a88044939ddbc1e0139ddbdc4cb0000', null, '连接公告通知sftp配置数据.', null, '连接公告通知sftp配置数据', 'notificationSftpProperties', '3', 'notificationSftpProperties', '8ac6d85939b837b40139b84ad1b60006');
INSERT INTO `sys_commondatas` VALUES ('8a88044939ddbc1e0139ddbf0a680003', null, '.', null, '用户名', 'username', '0', 'ecis', '8a88044939ddbc1e0139ddbdc4cb0000');
INSERT INTO `sys_commondatas` VALUES ('8a88044939ddbc1e0139ddbf6dd90005', null, '.', null, '密码', 'password', '0', 'ecis123!@#', '8a88044939ddbc1e0139ddbdc4cb0000');
INSERT INTO `sys_commondatas` VALUES ('8a88044939ddbc1e0139ddbfde8c0008', null, '.', null, 'ip地址', 'ip', '0', '10.8.132.109', '8a88044939ddbc1e0139ddbdc4cb0000');
INSERT INTO `sys_commondatas` VALUES ('8a88044939e81c680139e81f57570000', null, '.', null, '目录', 'dir', '3', '/home/ecis', '8a88044939ddbc1e0139ddbdc4cb0000');
INSERT INTO `sys_commondatas` VALUES ('8a88044939e81c680139e81fbac20002', null, '.', null, '端口', 'port', '3', '22', '8a88044939ddbc1e0139ddbdc4cb0000');
INSERT INTO `sys_commondatas` VALUES ('8a8804493a06b36c013a06b439550000', null, null, null, '消息级别', 'notificationLevel', '4', 'notificationLevel', '8ac6d85939b837b40139b84ad1b60006');
INSERT INTO `sys_commondatas` VALUES ('8a8804493a06b36c013a06b596560002', null, null, null, '紧急消息', 'emergent', '0', '1', '8a8804493a06b36c013a06b439550000');
INSERT INTO `sys_commondatas` VALUES ('8a8804493a06b36c013a06b64a240004', null, null, null, '普通消息', 'ordinary', '0', '2', '8a8804493a06b36c013a06b439550000');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939b837b40139b84c1b460008', null, '公告通知类型', null, '公告通知类型', 'notificationType', '0', 'notificationType', '8ac6d85939b837b40139b84ad1b60006');
INSERT INTO `sys_commondatas` VALUES ('8a8804d039e687630139e68cd3c30001', null, '.', null, '告警消息', 'warnMsg', '2', '02', '8ac6d85939b837b40139b84c1b460008');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939b837b40139b84dc915000a', null, null, null, '通知消息', 'notificationMsg', '0', '01', '8ac6d85939b837b40139b84c1b460008');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939b837b40139b84ecb24000e', null, '.', null, '公告消息', 'announcementMsg', '0', '03', '8ac6d85939b837b40139b84c1b460008');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939be1c840139be1e4bf90001', null, '公告通知状态.', null, '公告通知状态', 'notificationStatus', '2', 'notificationStatus', '8ac6d85939b837b40139b84ad1b60006');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939be1c840139be1f5fd50003', null, '.', null, '未发布', 'unPublished', '0', '0', '8ac6d85939be1c840139be1e4bf90001');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939be1c840139be2006e60005', null, '.', null, '生效', 'using', '0', '1', '8ac6d85939be1c840139be1e4bf90001');
INSERT INTO `sys_commondatas` VALUES ('8ac6d85939be1c840139be217e8f0007', null, '.', null, '终止', 'stopped', '0', '2', '8ac6d85939be1c840139be1e4bf90001');
INSERT INTO `sys_commondatas` VALUES ('8a880449402dfeee01402e02351e0000', null, '系统主题属性存放主题图标名称。/static/images/themes/icons目录', null, '系统主题', 'sys_themes', '12', 'sys_themes', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8a880449402dfeee01402e0345dd0001', 'theme_thumb_10.jpg', null, null, '默认蓝色主题', 'theme_blue', '0', 'blue', '8a880449402dfeee01402e02351e0000');
INSERT INTO `sys_commondatas` VALUES ('8a880449402dfeee01402e03d06d0002', 'theme_thumb_11.jpg', null, null, '灰色主题', 'theme_gray', '2', 'gray', '8a880449402dfeee01402e02351e0000');
INSERT INTO `sys_commondatas` VALUES ('8a880449402dfeee01402e07a1580003', 'theme_thumb_13.jpg', null, null, '高对比主题', 'theme_accessibility', '3', 'access', '8a880449402dfeee01402e02351e0000');
INSERT INTO `sys_commondatas` VALUES ('8a88044b403380af0140338b9d490000', 'theme_thumb_13.jpg', null, null, 'Metro风格', 'theme_neptune', '4', 'neptune', '8a880449402dfeee01402e02351e0000');
INSERT INTO `sys_commondatas` VALUES ('8a88044940187f6c014018f2e4050000', null, '资源管理相关配置', null, '资源管理', 'resourceMng', '2', 'resourceMng', '8a8804cf3b6988e3013b69b052450009');
INSERT INTO `sys_commondatas` VALUES ('8a88044940187f6c014018f4042c0001', null, '属性值存放相对于根目录的目录名称根目录:/static/images/common/appicons', null, '图标类型', 'resourceIconTypes', '0', 'resourceIconTypes', '8a88044940187f6c014018f2e4050000');
INSERT INTO `sys_commondatas` VALUES ('8a88044940187f6c014018f499e90002', '/', null, null, '默认图标', 'base_appicon', '0', 'base_appicon', '8a88044940187f6c014018f4042c0001');
INSERT INTO `sys_commondatas` VALUES ('8a88044940187f6c014018f52a880003', '/custom', null, null, '自定义图标', 'custom_appicon', '1', 'custom_appicon', '8a88044940187f6c014018f4042c0001');
INSERT INTO `sys_commondatas` VALUES ('8a880449402809e201402814dfea0000', '/custom2', null, null, '自定义图标2', 'custom_application2', '2', 'custom_application2', '8a88044940187f6c014018f4042c0001');

-- ----------------------------
-- Table structure for `sys_conf`
-- ----------------------------
DROP TABLE IF EXISTS `sys_conf`;
CREATE TABLE `sys_conf` (
  `ID_` varchar(32) NOT NULL,
  `MODULE_` varchar(50) NOT NULL,
  `SUB_MODULE` varchar(50) NOT NULL,
  `NAME_` varchar(50) NOT NULL,
  `VALUE_` varchar(2000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_conf
-- ----------------------------
INSERT INTO `sys_conf` VALUES ('601D7C27AB6424D0DA21E822AF780D80', 'startup', 'default', 'showMenuNum', '5');
INSERT INTO `sys_conf` VALUES ('4E8F31765DF43E591DCBE186EB67AFF5', 'startup', 'default', 'tabsNum', '10');
INSERT INTO `sys_conf` VALUES ('17DB00AA85BDD02999235BE1ACFCB291', 'startup', 'default', 'menuExpandDepth', '0');
INSERT INTO `sys_conf` VALUES ('A9BDCFF421D2C23D0B1FC91C1DC618EF', 'startup', 'default', 'loginPageTheme', 'default');
INSERT INTO `sys_conf` VALUES ('5CAF6A3B9D117EC70EF71D71475E9A05', 'startup', 'default', 'isShowNotice', '0');
INSERT INTO `sys_conf` VALUES ('4B1405E066BA70E5A690EF645EC3FC03', 'startup', 'default', 'showDesktopInWindow', '1');
INSERT INTO `sys_conf` VALUES ('AC8D987A148CF062383A3177358C7B96', 'startup', 'default', 'desktopInfo', '{\"id\":\"desktop_iframe\",\"name\":\"desktop_iframe\",\"src\":\"personaldesktop/desktop.jsp\"}');
INSERT INTO `sys_conf` VALUES ('457B7438D88A1B3DF1063425B1BB813F', 'startup', 'default', 'defaultPortalInfo', '{\"id\":\"default_page\",\"name\":\"default_iframe\",\"nameCn\":\"个人桌面\",\"src\":\"/pages/personaldesktop/desktop.jsp\",\"closeable\":1}');
INSERT INTO `sys_conf` VALUES ('F2775A40D0676D17DF3993FACDC698C0', 'startup', 'default', 'defaultPageSize', '20');
INSERT INTO `sys_conf` VALUES ('5C9398E9196BE6CEC9008E539D2617AC', 'startup', 'default', 'enableColumnHide', '1');
INSERT INTO `sys_conf` VALUES ('8263E090DFE62C86B9833FF9AA8F0221', 'startup', 'default', 'isShowHistroy', '0');
INSERT INTO `sys_conf` VALUES ('E4AA46677577855323861C7827DCB69B', 'startup', 'default', 'floderStrong', '1');
INSERT INTO `sys_conf` VALUES ('C6E73CE1218BFD946E2F17A54126FF11', 'startup', 'default', 'titleWordNum', '5');
INSERT INTO `sys_conf` VALUES ('1BFDD45113AEC2C3E2E2C7118AA1E5B7', 'startup', 'default', 'isShowSubNavi', '1');
INSERT INTO `sys_conf` VALUES ('3838391798FEF2F321E0812F3801D80D', 'startup', 'default', 'showUserGuide', '0');
INSERT INTO `sys_conf` VALUES ('D778C9BDE65E820A4431D620D5B434CE', 'startup', 'default', 'mainMenusNum', '5');
INSERT INTO `sys_conf` VALUES ('06A0C2AF66B4231EFED28BA0D3BFE991', 'startup', 'default', 'menuQuickSearch', '1');
INSERT INTO `sys_conf` VALUES ('596C5513360FE0634DE08C878539419B', 'startup', 'default', 'theme', 'neptune');
INSERT INTO `sys_conf` VALUES ('44868E6144AE62B43E7E11AE0D5961CD', 'startup', 'default', 'requestOnlineUserInterval', '0');
INSERT INTO `sys_conf` VALUES ('4E06C239BC661F0C3AC0A36C79C61489', 'startup', 'default', 'timeBeforeMenuAutoHide', '150');

-- ----------------------------
-- Table structure for `sys_department`
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `ID_` varchar(32) NOT NULL,
  `DESC_` varchar(200) DEFAULT NULL,
  `LOGIN_MODE` varchar(1) DEFAULT NULL,
  `NAME_` varchar(20) NOT NULL,
  `NAME_CN` varchar(50) NOT NULL,
  `ORDER_` int(11) NOT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO `sys_department` VALUES ('20090625160755252', '平台开发厂商', null, 'eastcom_sw', '狼烟丰起', '0', null);

-- ----------------------------
-- Table structure for `sys_desktop_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_desktop_resource`;
CREATE TABLE `sys_desktop_resource` (
  `ORDER_` int(11) NOT NULL,
  `DESKTOP_ID` varchar(32) NOT NULL,
  `RESOURCE_ID` varchar(32) NOT NULL,
  `ID_` varchar(32) NOT NULL,
  `NAME_` varchar(100) NOT NULL,
  `PARENT_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_desktop_resource
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_gadgets`
-- ----------------------------
DROP TABLE IF EXISTS `sys_gadgets`;
CREATE TABLE `sys_gadgets` (
  `ID_` varchar(32) NOT NULL,
  `CATEGORY_` varchar(1) NOT NULL,
  `CLASS_NAME` varchar(100) NOT NULL,
  `CLASS_URL` varchar(100) NOT NULL,
  `CREATE_TIME` varchar(24) NOT NULL,
  `DEFAULT_HEIGHT` varchar(10) NOT NULL,
  `DESC_` varchar(255) NOT NULL,
  `IMAGE_URL` varchar(100) NOT NULL,
  `NAME_` varchar(100) NOT NULL,
  `SCROLLING_` varchar(1) NOT NULL,
  `SMALL_IMAGE_URL` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_gadgets
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_logs`
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE `sys_logs` (
  `ID_` varchar(32) NOT NULL,
  `APP_HOST` varchar(16) NOT NULL,
  `HOST_` varchar(160) DEFAULT NULL,
  `LOG_LEVEL` varchar(1) DEFAULT NULL,
  `MESSAGE_` varchar(2000) DEFAULT NULL,
  `MODULE_CODE` varchar(32) DEFAULT NULL,
  `OPERATE_TYPE` varchar(2) DEFAULT NULL,
  `QRYUSED_TIME` varchar(10) DEFAULT NULL,
  `RECORD_TIME` varchar(24) DEFAULT NULL,
  `USERNAME_` varchar(32) DEFAULT NULL,
  `AUDIT_ID` varchar(20) DEFAULT NULL,
  `AUTH_FLAG` varchar(1) DEFAULT NULL,
  `OPT_REASON` varchar(500) DEFAULT NULL,
  `ORDER_ID_` varchar(32) DEFAULT NULL,
  `OUTER_USER_` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_logs
-- ----------------------------
INSERT INTO `sys_logs` VALUES ('4913F9F8E88D71AB1C06691406C52BBD', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '51', '2017-10-04 09:32:13', 'unknown', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8E3C1508D5A1E0E60232CB19F6F981DA', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '53', '2017-10-04 09:44:28', 'unknown', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A988B7EDE510FE133575D12AE9188203', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '54', '2017-10-04 09:51:03', 'unknown', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2AE9413BE5F3B01E581952AF498D4D16', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '617', '2017-10-04 10:49:19', 'unknown', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2020270BF04F6B1406DCE3689E27DD2E', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '509', '2017-10-04 10:49:19', 'unknown', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('CB6F85E73595B7C3EC523422002121AC', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '405', '2017-10-04 10:49:19', 'unknown', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8FFA3FE9A5D4DBDEF0C7C1E786397BAC', '192.168.0.222', '192.168.0.222', '1', '操作类型:[修改],参数:[用户名:root,中文名:超级管理员,电子邮箱:,手机号码:13912345678,用户级别:超级管理员,性别:1],目标:[修改],结果:[成功!]', 'unconfigured', '08', '611', '2017-10-04 10:49:41', 'unknown', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('DFABEC3E09559DD228292F6F6C25712A', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:CEEEE3E7DDE60FDD9FFEC181FB73A96A,登录入口:本系统', 'login', '09', '34', '2017-10-04 10:50:01', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A6DEA0CE6227029A1E2B967C3EA3506C', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '21', '2017-10-04 10:50:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('ABD3C338561C733996653C3C01B5BED6', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '21', '2017-10-04 10:50:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EEC3A2BF271A06013928F54323B6E199', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '16', '2017-10-04 10:50:46', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('09E6185DE23781499C325FD8D0D0B40D', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '15', '2017-10-04 10:50:56', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('586DE24EC752C77F521B6B343B0BB2F2', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)退出登录!SESSIONID:CEEEE3E7DDE60FDD9FFEC181FB73A96A', 'logout', '09', '7', '2017-10-04 10:52:38', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('0DC4391BD78F4648133DCC71B69000ED', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:3F65940704F81585F4111D470E5E8D5D,登录入口:本系统', 'login', '09', '3', '2017-10-04 10:52:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EE81C75439F1FFCA2AA132FF8A2F9A16', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '14', '2017-10-04 10:52:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3DD646B7FBA276E29D5207FA167E6A68', '127.0.0.1', '127.0.0.1', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '25', '2017-10-04 10:53:03', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7B5EAE5A51E7D9798EE5AC482FD00BBA', '127.0.0.1', '127.0.0.1', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '26', '2017-10-04 10:53:06', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6D7D3863E1DD2733B3CC09FBBF3D4582', '127.0.0.1', '127.0.0.1', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '28', '2017-10-04 10:53:07', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E18D2CA4F06E6D6BAFE09586EA5FB442', '127.0.0.1', '127.0.0.1', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '29', '2017-10-04 10:53:08', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('678A7B1CE1E2E898137F838D0B1F2805', '127.0.0.1', '127.0.0.1', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '28', '2017-10-04 10:53:17', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('19211810A376239DF560497BEBA87FBA', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '33', '2017-10-04 10:53:45', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7E7CA72A119D3FE5912220EFCC516227', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:A8028181EB6E9F9570D1723FC94BE65C,登录入口:本系统', 'login', '09', '10', '2017-10-04 10:59:33', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EEB87FF114EFC5B2B346D5C21C597403', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '109', '2017-10-04 10:59:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('DB417A7492F6EAD5576E29F9E5A8B89F', '192.168.0.222', '192.168.0.222', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:日志管理,系统日志查询,个人公告管理,系统基础数据管理,系统数据维护,新增,修改,删除,查询,Portal管理,Portal工具管理,查找,添加,删除,修改,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '89', '2017-10-04 11:08:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E3ACB207B9C5927A42E4C977E344792C', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '25', '2017-10-04 11:08:28', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('962AF6A766E69A0F18B8188F2EB46E49', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '16', '2017-10-04 11:09:25', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D2BF0E90BC541D7758C9987D9F1E329E', '192.168.0.222', '192.168.0.222', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:用户管理,添加帐户,编辑账户,角色授权,部门授权,启用/停用账户,删除账户,查询账户详情,批量新增,部门管理,新增,修改,删除,资源管理,新增资源,修改资源,删除资源,系统公告管理,新增公告,编辑公告,生效/终止公告,删除公告,控制面板,Redis Keys 管理,系统选项,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '85', '2017-10-04 11:09:48', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B81BDDD9D5BA70F989636831B442F5E2', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '14', '2017-10-04 11:09:51', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9678089FFF7EC9EFDECFD23A06497926', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:A8028181EB6E9F9570D1723FC94BE65C,登录入口:本系统', 'login', '09', '3', '2017-10-04 11:09:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C144AE42F7FD89F217CC469C079F86D4', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '15', '2017-10-04 11:09:56', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3DD17C874357A8B4B7222C71929FC2CA', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '39', '2017-10-04 11:09:59', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D9F594E4269412CF0B1FBEE49BFE7830', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '17', '2017-10-04 11:10:06', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('163D9551C9927B85272B7E9B379C7ED8', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-10-04 11:10:28', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('AFC5385F82FC6AF22026074732E885C8', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '23', '2017-10-04 11:10:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('73130622F0BDDB17512D3BF624BF7C53', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '27', '2017-10-04 11:10:39', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FD7438D206E7C1634EE57DC5D198DEBB', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '20', '2017-10-04 11:10:41', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6B4EBB76EC36D27AFC15F1837D91817A', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '18', '2017-10-04 11:10:53', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2466157138F759CDAD28CACE2F8B8974', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[是否全部:true,关键字:null,类型:null,开始时间:null,结束时间:null],目标:[查询],结果条数:[0],结果:[成功!]', 'unconfigured', '01', '51', '2017-10-04 11:10:58', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A9EB261F22765B9285592C675333078A', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[是否全部:true,关键字:null,类型:null,开始时间:null,结束时间:null],目标:[查询],结果条数:[0],结果:[成功!]', 'unconfigured', '01', '19', '2017-10-04 11:11:07', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('807BBC687CD8191305D754F2939F94C5', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{node=root, sort=[{\"property\":\"order\",\"direction\":\"ASC\"}], name=, _dc=1507086759736}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'baseinfo', '01', '52', '2017-10-04 11:12:39', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('10B72214E00AA1C41AA8569D62ABD082', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)退出登录!SESSIONID:A8028181EB6E9F9570D1723FC94BE65C', 'logout', '09', '6', '2017-10-04 11:13:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('96B01B1E56EBEAE1ECB14618B3E96B21', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:7CD49FBC32B8015D85ADC0A04E5A1EAE,登录入口:本系统', 'login', '09', '4', '2017-10-04 11:13:21', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3BF9FF6FC9AD4526F4E9E2C3D08022C3', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '22', '2017-10-04 11:13:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('473055E8A55EAE84848A33D223F825D5', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '17', '2017-10-04 11:13:30', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FD0C3EA8E3346D45ED0E6C0BAE7662EF', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2017-10-04 11:13:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EDF3C92307ACBCDAB0D7FBC7660843AD', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-10-04 11:13:41', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D037CEFD978FA9B266414D7F344AFEFA', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2017-10-04 11:13:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('453237B7693BEF1BB643816BDBB2D613', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-10-04 11:13:47', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5391D6B6074CDEC52450E1D2CE5CCF18', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '20', '2017-10-04 11:13:57', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EC4D16FC49B918E6B85194AAA80AF27B', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '12', '2017-10-04 11:14:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('50A47ACA997E6F6A050C34FDFA32F889', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '24', '2017-10-04 11:14:04', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('027A00D150DE6FB00025BEC57B073D95', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '12', '2017-10-04 11:14:14', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2ED6EBEB3B247E0EA13C8A580F675A22', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '28', '2017-10-04 11:14:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D5A282365F1B033940AAF748FBE948ED', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '18', '2017-10-04 11:14:24', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('AC134E3A264554700CB62735397F47C4', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2017-10-04 11:15:03', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EA3EAFB0D7BFD44D7311B1F390A42D86', '192.168.0.222', '192.168.0.222', '1', '操作类型:[更新],参数:[{menuExpandDepth=0, titleWordNum=5, isShowSubNavi=1, portalId=default_page, desktopSrc=personaldesktop/desktop.jsp, timeBeforeMenuAutoHide=150, type=startup, portalPageSrc=/pages/personaldesktop/desktop.jsp, closeable=1, enableColumnHide=1, mainMenusNum=5, showMenuNum=5, requestOnlineUserInterval=0, isShowNotice=0, portalName=default_iframe, showUserGuide=0, desktopId=desktop_iframe, theme=_ALL_, menuQuickSearch=1, desktopName=desktop_iframe, floderStrong=0, showDesktopInWindow=1, portalNameCn=个人桌面, userGuideName=用户指南.docx, isShowHistroy=1, defaultPageSize=20, tabsNum=10}],目标:[更新],自定义信息:[成功!],结果:[null]', 'unconfigured', '03', '87', '2017-10-04 11:15:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('1950D4BA21F7DB5D8E84D16994932FF3', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '12', '2017-10-04 11:15:35', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C188AFDBDE7F78B81C412597C9328F3F', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '26', '2017-10-04 11:15:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('09A7C876F1B32417294A3FF3D9FC95A3', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '12', '2017-10-04 11:16:15', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('129301988A1DFAC3160551BC361FD400', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-10-04 11:16:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F85CA6EB2FA90BD037C18CD2C69F2F13', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '20', '2017-10-04 11:16:38', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('1FD35110EB25F290E41F5F6718ED7BCE', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-10-04 11:17:26', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D73EA63AFD6084DE80D00A8560B29107', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-10-04 11:18:18', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A91CCCFAAAFCB733D7D0FE3FAAAD0B0C', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '24', '2017-10-04 11:18:21', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B270531F57D83D9DB8B0EC9EDEA86B32', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '450', '2017-10-04 11:18:26', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C82C3B5F533C9D65531A4802CC701262', '127.0.0.1', '127.0.0.1', '1', '操作类型:[查询],参数:[开始时间:null,结束时间:null,模块名称:null,操作状态:null,IP地址:null,用户级别:null,用户名称:null,操作内容:null],目标:[查询],结果条数:[65],结果:[成功!]', 'unconfigured', '01', '43', '2017-10-04 11:20:16', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E006E6733BFEBCE8C831E7EDEFACA563', '127.0.0.1', '127.0.0.1', '1', '操作类型:[查询],参数:[{limit=100, sort=[{\"property\":\"groupTime\",\"direction\":\"DESC\"}], start=0, page=1, readFlag=unRead, _dc=1507087230229, group=[{\"property\":\"groupTime\",\"direction\":\"DESC\"}]}],目标:[查询],结果条数:[0],自定义信息:[成功],结果:[null]', 'unconfigured', '01', '64', '2017-10-04 11:20:30', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9C23113B42D733C2104E9F28287DA208', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '28', '2017-10-04 11:24:50', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('00853A0BE501A8A37C340750EE1ED648', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)退出登录!SESSIONID:7CD49FBC32B8015D85ADC0A04E5A1EAE', 'logout', '09', '12', '2017-10-04 11:45:33', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6FF02A918FA924FFCA8D334F777B853B', '192.168.0.222', '192.168.0.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:931BF2C8689D5F0DB358976723D06C10,登录入口:本系统', 'login', '09', '572', '2017-10-04 13:58:09', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9204A1FF713757F04C0C1D8DA998BF4D', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '22', '2017-10-04 13:58:10', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('870E171E5831C37EBEB3D907B30F0787', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '25', '2017-10-04 13:59:14', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('228795E23C6BF03515ED42654BC7714C', '192.168.0.222', '192.168.0.222', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '38', '2017-10-04 13:59:20', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C5E450F4E3AE2A6F600CF209E565F579', '192.168.11.223', '192.168.11.223', '1', '用户:超级管理员(root)登录成功!SESSIONID:986DA8C6B078A5638D07858E65E42F41,登录入口:本系统', 'login', '09', '7', '2017-12-21 22:18:47', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('80CBC72D04844ACE1F1B998B46484207', '192.168.11.223', '192.168.11.223', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '35', '2017-12-21 22:18:49', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E6AFC01A8701A28190A521178CD17CA0', '192.168.11.223', '192.168.11.223', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '45', '2017-12-21 22:19:17', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('35F06D04BEED8ECF9B371D6CCA6D8D72', '192.168.11.223', '192.168.11.223', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '18', '2017-12-21 22:19:31', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('46BB7C47704FDF6534CA60DA113AFA54', '192.168.11.222', '192.168.11.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:5C6B3364556B3DD21B0A4E99F1F9A902,登录入口:本系统', 'login', '09', '8', '2017-12-22 14:47:47', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E63EDF37450A53C40D1439DE04B098E2', '192.168.11.222', '192.168.11.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '24', '2017-12-22 14:47:48', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('4AEBF8AA30EEE8798C5DE5B0CF1DF4F3', '192.168.11.222', '192.168.11.222', '1', '用户:超级管理员(root)退出登录!SESSIONID:5C6B3364556B3DD21B0A4E99F1F9A902', 'logout', '09', '0', '2017-12-22 15:09:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B6B84149E51933C0BEC5168308DF545B', '192.168.11.222', '192.168.11.222', '1', '用户:超级管理员(root)登录成功!SESSIONID:0D3FB8948D976C9E775F1876BC094EC1,登录入口:本系统', 'login', '09', '3', '2017-12-22 17:32:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('60C71698E6B037A6149963B1311A3E8F', '192.168.11.222', '192.168.11.222', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '42', '2017-12-22 17:32:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D1DC621A5105433ABA7EABE6A6D5E61F', '192.168.11.222', '192.168.11.222', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '54', '2017-12-22 17:32:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5BEEB4587E3EC8B660A90CC41D14E24C', '192.168.11.222', '192.168.11.222', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '20', '2017-12-22 17:33:00', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5D8EF527F072361AC5526652A0CFD18C', '192.168.11.102', '192.168.11.102', '1', '用户:超级管理员(root)登录成功!SESSIONID:D5E0FB6D2848619930D0A83394DCAD22,登录入口:本系统', 'login', '09', '8', '2017-12-23 23:47:42', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FD0748F9248C0FDFB7C808788CA22CE3', '192.168.11.102', '192.168.11.102', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '32', '2017-12-23 23:47:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('1185D7D7EB40135E7E2EBD6505CB64EF', '192.168.11.102', '192.168.11.102', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '44', '2017-12-23 23:48:28', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('939519E4A82FD33E3A521FBF0C340942', '192.168.11.102', '192.168.11.102', '1', '用户:超级管理员(root)登录成功!SESSIONID:D25B4F4F7D87414E6DCD030302E60F07,登录入口:本系统', 'login', '09', '6', '2017-12-24 00:06:46', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('977336380E59D0BE8E7A9507349724EF', '192.168.11.102', '192.168.11.102', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '28', '2017-12-24 00:06:48', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2B3D048FE275F56A5842C1739F3932B8', '192.168.11.102', '192.168.11.102', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '43', '2017-12-24 00:06:54', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D0443E777D511113EF63E33E09E38CF0', '192.168.11.102', '192.168.11.102', '1', '用户:超级管理员(root)退出登录!SESSIONID:D25B4F4F7D87414E6DCD030302E60F07', 'logout', '09', '6', '2017-12-24 00:06:59', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A3C0A4542C7DA32EF3CA36EF6DF29ACD', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:001ABC809543A717E67AA98C02FDD0C5,登录入口:本系统', 'login', '09', '13', '2017-12-25 10:34:09', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F0088AAFD8FDAB7F1652DAB9A0F284B7', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '18', '2017-12-25 10:34:10', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7F92FD1929916E4862A8DCF5A951CE3D', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '33', '2017-12-25 10:34:27', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('53C7F9E9BECF1420C74328306269AEB2', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '17', '2017-12-25 10:34:35', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('070027AC9FA2F7995DD1A761D885CA63', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)退出登录!SESSIONID:001ABC809543A717E67AA98C02FDD0C5', 'logout', '09', '7', '2017-12-25 10:55:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('CF78DCD335CE2C857399928E0996E971', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:6686D8CBAF724D6C0C4FD745741A0632,登录入口:本系统', 'login', '09', '3', '2017-12-25 14:17:11', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('26C5B0ED1EBA52A6EB58BCFD1C78823A', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '16', '2017-12-25 14:17:11', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('638D6292AEA2908A3B48E7B9DEF633CF', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '28', '2017-12-25 14:17:30', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8A35C0E8A60036C87CA9AEEE7EBC1253', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '30', '2017-12-25 14:17:57', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('68EAD9BFA326B4423F1B7A517BD4F7BA', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '30', '2017-12-25 14:17:59', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('921D2EA76BC56CF23E15840839ED6F3A', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '23', '2017-12-25 14:18:08', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2824E0FB3B744970683559749083F79A', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '27', '2017-12-25 14:18:11', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('0DB94FCC144531137D5788A375ACA6AB', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '24', '2017-12-25 14:18:24', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('BBED9A92D96A9BBB8F8C864DFE7EF479', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '25', '2017-12-25 14:18:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('DA22217FDC761266D30DEC2AF5E00F98', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '27', '2017-12-25 14:18:36', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('36FCCD85BFE5CF949AF888A25B9E8DEF', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '25', '2017-12-25 14:24:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D212C00535790F5F7051AA58D47605F8', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:3A86E0836F29739083B1530C6C2E8881,登录入口:本系统', 'login', '09', '10', '2017-12-25 15:45:11', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('ACEBDC8E4BAF5F0699822B7962C6CBA0', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '22', '2017-12-25 15:45:12', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E7ECD3A9A820853E6009D2B633F053DC', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '28', '2017-12-25 15:47:25', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2C56864AEA10CFA02754E72DB905C77D', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[开始时间:null,结束时间:null,模块名称:null,操作状态:null,IP地址:null,用户级别:null,用户名称:null,操作内容:null],目标:[查询],结果条数:[110],结果:[成功!]', 'unconfigured', '01', '45', '2017-12-25 15:47:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3DF885980A654B1FB7C671C988FD914C', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '9', '2017-12-25 15:47:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('360C619FF6066730F36AF268EA1967E3', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{limit=100, sort=[{\"property\":\"groupTime\",\"direction\":\"DESC\"}], page=1, start=0, readFlag=unRead, _dc=1514188066134, group=[{\"property\":\"groupTime\",\"direction\":\"DESC\"}]}],目标:[查询],结果条数:[0],自定义信息:[成功],结果:[null]', 'unconfigured', '01', '85', '2017-12-25 15:47:46', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EE1728234D665BA3BD03BB6ACB8D8D17', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{node=root, sort=[{\"property\":\"order\",\"direction\":\"ASC\"}], name=, _dc=1514188070521}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'baseinfo', '01', '76', '2017-12-25 15:47:50', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A09F00D0C550BC826756527E300F8D2C', '192.168.11.100', '192.168.11.100', '1', '新增资源【应用功能】成功！', 'resourcemng', '02', '42', '2017-12-25 16:27:35', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C3E6A2969899D63D684BBC91AA4C5B20', '192.168.11.100', '192.168.11.100', '1', '新增资源【打赏详细信息查看】成功！', 'resourcemng', '02', '31', '2017-12-25 16:28:15', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('98830FBC963CDAE7CE329EC2A96A2695', '192.168.11.100', '192.168.11.100', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:应用功能,打赏详细信息查看,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '56', '2017-12-25 16:28:28', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('597727D3A64E81220F1D3C41487C3D7F', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-12-25 16:28:33', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('93A5FEEDB7507ADDF11B8286E99AB44D', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:3A86E0836F29739083B1530C6C2E8881,登录入口:本系统', 'login', '09', '2', '2017-12-25 16:28:39', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7B7629522818DBC68AFF2F2D136A9D77', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2017-12-25 16:28:39', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('85985A6D261D0578FBBB183D8FD923E5', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '30', '2017-12-25 16:30:20', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6FC0C8A0EEFC50B369966279E9CEB5C7', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)退出登录!SESSIONID:3A86E0836F29739083B1530C6C2E8881', 'logout', '09', '0', '2017-12-25 16:50:58', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2D3798780533441E2899B445E44F32E9', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:1DCB6A57971FADBFCBE61A443E39560D,登录入口:本系统', 'login', '09', '6', '2017-12-25 17:00:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('039A8F52789BD7223C8035578AC84449', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '10', '2017-12-25 17:00:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7D68DCAC5822E70E17028EBF8C786314', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '19', '2017-12-25 17:09:50', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('BB517E96D1C96F9E92CFFDD8E18FB09F', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-12-25 17:10:27', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2FB2DFC7D28DEC091919D0C28F106C0B', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-12-25 17:12:30', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D10F7AB049341E144515D58384DF0CFA', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '23', '2017-12-25 17:31:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5F65D110EFAF0B4CF2B007F031089500', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '23', '2017-12-25 17:31:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('4A2C3595F4D790C90A1EF8BE2FF9F8EF', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[是否全部:true,关键字:null,类型:null,开始时间:null,结束时间:null],目标:[查询],结果条数:[0],结果:[成功!]', 'unconfigured', '01', '70', '2017-12-25 17:31:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('50D69535D50B43EF3F0A18E30AD12F6E', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{node=root, sort=[{\"property\":\"order\",\"direction\":\"ASC\"}], name=, _dc=1514194296018}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'baseinfo', '01', '21', '2017-12-25 17:31:36', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('04FB3C3DAD6E814E755EDA3B39C07999', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{limit=100, sort=[{\"property\":\"groupTime\",\"direction\":\"DESC\"}], page=1, start=0, readFlag=unRead, _dc=1514194298744, group=[{\"property\":\"groupTime\",\"direction\":\"DESC\"}]}],目标:[查询],结果条数:[0],自定义信息:[成功],结果:[null]', 'unconfigured', '01', '29', '2017-12-25 17:31:38', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8A5CECDF8F41D4F18A5833935359A8D8', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[开始时间:null,结束时间:null,模块名称:null,操作状态:null,IP地址:null,用户级别:null,用户名称:null,操作内容:null],目标:[查询],结果条数:[132],结果:[成功!]', 'unconfigured', '01', '31', '2017-12-25 17:31:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E5E3781E444CFB208E9F2854CF12E1D5', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:A480EFC57EEABB4941853DDA59794E8E,登录入口:本系统', 'login', '09', '0', '2017-12-25 17:51:17', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('23A5DF6F1AA508D3436B211AB76D65D4', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '0', '2017-12-25 17:51:17', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('39D7D7A17B6E13FA569BB473D1A076FB', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:9A5FB4244E483F6902A23FFE0469AC81,登录入口:本系统', 'login', '09', '12', '2017-12-26 09:38:59', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('448337FB8BF3A8114ADD686C1E2E02C1', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '28', '2017-12-26 09:39:01', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('34A258F1107F1C1B6317A4F94A59E214', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:742711CE520878EAA4EF832434F2AA19,登录入口:本系统', 'login', '09', '4', '2017-12-26 09:41:57', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('1CBF9422AA8678E70E7C3A9C13FF3948', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '24', '2017-12-26 09:41:57', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A701653DB2908420C7D9A23D162F10F2', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '8', '2017-12-26 09:43:51', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C083905FD55D894290801B15F206CE95', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '12', '2017-12-26 09:44:56', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('59A603BABDDA1C7CB33C8D4C59F6CD21', '192.168.11.100', '192.168.11.100', '1', '操作类型:[更新],参数:[{menuExpandDepth=0, titleWordNum=5, isShowSubNavi=1, portalId=default_page, desktopSrc=personaldesktop/desktop.jsp, timeBeforeMenuAutoHide=150, type=startup, portalPageSrc=/pages/personaldesktop/desktop.jsp, closeable=1, enableColumnHide=1, mainMenusNum=5, showMenuNum=5, requestOnlineUserInterval=0, isShowNotice=0, portalName=default_iframe, showUserGuide=0, desktopId=desktop_iframe, theme=neptune, menuQuickSearch=1, desktopName=desktop_iframe, floderStrong=0, showDesktopInWindow=1, portalNameCn=个人桌面, userGuideName=用户指南.docx, tabsNum=10, defaultPageSize=20, isShowHistroy=1}],目标:[更新],自定义信息:[成功!],结果:[null]', 'unconfigured', '03', '36', '2017-12-26 09:45:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('26BAC448CDE0B8CED04756FA2C4ABC04', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:8B3D88FA6894B1011CFD71151B2823F0,登录入口:本系统', 'login', '09', '9', '2017-12-26 12:10:20', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('685F5722E303DF4562612894E2D739AD', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '29', '2017-12-26 12:10:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9CA38C4E2D75B22BCFC672091095B2C8', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)退出登录!SESSIONID:8B3D88FA6894B1011CFD71151B2823F0', 'logout', '09', '10', '2017-12-26 12:32:11', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F5295433D4D422264D7E74E8433588D2', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:2D7AA416B6ECDD7CF631EB7FA355D7AE,登录入口:本系统', 'login', '09', '8', '2017-12-26 13:07:54', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B8CB0ABDFAB67A122CE2CCC3E52A9E21', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '18', '2017-12-26 13:07:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('855E418259E9B66C892C9C634783A18A', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:A88FA74CE917890CA5A4E27883979C91,登录入口:本系统', 'login', '09', '10', '2017-12-26 13:11:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('61DD3EAA69D05513AFBE500A7E45E7FE', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '36', '2017-12-26 13:11:04', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('0794FD97D0EF4909755C99C1A6590F5E', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:DEB19EEA8BFA96AF767679577D486967,登录入口:本系统', 'login', '09', '10', '2017-12-26 13:24:05', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A7A40115F1F9E86D5C9F90B89FFB2439', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '30', '2017-12-26 13:24:06', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('501F66C8BD0C982E7C0646569E4EEEA9', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)退出登录!SESSIONID:DEB19EEA8BFA96AF767679577D486967', 'logout', '09', '9', '2017-12-26 13:45:48', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('19E7751AB91ADF5229A6FFD5E1E745D5', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:308C4C6CDB785B46929D2671BB7A2C8B,登录入口:本系统', 'login', '09', '5', '2017-12-26 16:22:17', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('501C7E5B46037C68D0E4D6386BCE0032', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2017-12-26 16:22:18', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('38286FB2C901EC838AFA8A5F003ACDFB', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:35BC827DB69BA9CC6386FAE36E6DAF2E,登录入口:本系统', 'login', '09', '8', '2017-12-26 17:02:35', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3B9884D50E189A53B3A94A8F9D1B2D92', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '33', '2017-12-26 17:02:37', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('44492C1FC230BA38EB38E58F89D83494', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:352CDAB8C008CE3985F415FDEB4E12D9,登录入口:本系统', 'login', '09', '7', '2017-12-26 17:06:52', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C0AF6940E93BE307ECEF6B2926740A55', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '35', '2017-12-26 17:06:53', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('64D86F7A495043993CD3FE4416EDD288', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:95D4D84E1C7A855075B26423163DD0EF,登录入口:本系统', 'login', '09', '9', '2017-12-26 17:20:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('32F7E45E0527CC8BFAB4BD2F584B19C3', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '32', '2017-12-26 17:20:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('420230895B2AE96E7DCA0395D82F243A', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:B31785A67D09C3B0366B77CCCE540AC7,登录入口:本系统', 'login', '09', '8', '2017-12-26 17:22:54', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A30F4B2CCAE46DCDE3AC9B678BF41CBD', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:B31785A67D09C3B0366B77CCCE540AC7,登录入口:本系统', 'login', '09', '8', '2017-12-26 17:22:54', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('84A5333B06677846C09DF0BA4FA8029F', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '29', '2017-12-26 17:22:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('783AD233A4E60EACD50A47ADC1C79497', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:EE24A783151BF7AEAD2869ADB8B655DF,登录入口:本系统', 'login', '09', '12', '2017-12-26 17:29:17', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('59B6E8BAB554D84C5B2FA67F35844731', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '23', '2017-12-26 17:29:18', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('38438AECFD2471D6942C98358F9F7AE0', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '18', '2017-12-26 17:31:11', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('47D77C39BAAFB1E35D9BA352B7AA945A', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:054F52BF25FD37D793880C54D62FE1A8,登录入口:本系统', 'login', '09', '10', '2017-12-26 17:34:13', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FD72918ED919F4466CD522F8BE6B9C25', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '37', '2017-12-26 17:34:14', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('AF1DC7ECDF774A59851B884AFF19A7A1', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:F4976C736DD723BD05B836036205621E,登录入口:本系统', 'login', '09', '8', '2017-12-26 17:39:27', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('0D777033EEF660C3DA97C90A85DEF28D', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '45', '2017-12-26 17:39:28', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8DD528AD7E734CE3CB3C161859D5F917', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:1ED2621C83FED440F7629A7EC783A673,登录入口:本系统', 'login', '09', '9', '2017-12-26 17:42:13', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5651010A41539850769CD3D5AE434843', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '24', '2017-12-26 17:42:14', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A6AD2933190FF730A2C9A638D7A81799', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:32DA3BD49BA8EF3698240347B0976108,登录入口:本系统', 'login', '09', '9', '2017-12-26 17:45:10', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8499662292BB440246B58B506E41E9DE', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '29', '2017-12-26 17:45:11', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E10699CC5519FF5FD25198893A41316A', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:01C33758D14CBB92E64B414F08AD3782,登录入口:本系统', 'login', '09', '7', '2017-12-26 17:47:18', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FCC0DF01535B50AE65A34C08960D93F8', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '31', '2017-12-26 17:47:19', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('4F2BB6546A53312C4871C584225694B8', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:25320D12E147BA3DC616D0E1B8A1E4FB,登录入口:本系统', 'login', '09', '6', '2017-12-26 17:53:50', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('4CC6FDD73A6F4D13D49E2F78D2E01AC7', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '23', '2017-12-26 17:53:51', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F7A7EB369DC49555B7D05DC9E46895B8', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:4F218A7AB42AA4D65973138F2FFE2015,登录入口:本系统', 'login', '09', '14', '2017-12-26 17:55:06', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('530A34FB4259564D9200AB84D895EF67', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '22', '2017-12-26 17:55:08', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F688D1046F9891EE2C57429BAC0D6D6F', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:CB449C318420EEAB1204F278CCE69B2C,登录入口:本系统', 'login', '09', '9', '2017-12-26 17:57:33', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('BC28669C7EEF3377BA72CAF7975AF380', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '26', '2017-12-26 17:57:35', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('009E0DE37930DC7AB372E7B26727AE8B', '192.168.11.100', '192.168.11.100', '1', '用户:超级管理员(root)登录成功!SESSIONID:37061E5029E213858F5E5F322616A145,登录入口:本系统', 'login', '09', '10', '2017-12-26 18:00:24', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('101BAEBE7AB33C012E8EA16E0CF124FB', '192.168.11.100', '192.168.11.100', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '31', '2017-12-26 18:00:26', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('77A263E2DA542E672552E90BDA91FB0A', '192.168.11.102', '192.168.11.100', '1', '用户:超级管理员(root)退出登录!SESSIONID:37061E5029E213858F5E5F322616A145', 'logout', '09', '8', '2017-12-26 21:10:51', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('282D4794FD372D869657A3EB45CA1EC1', '192.168.11.179', '192.168.11.179', '1', '用户:超级管理员(root)登录成功!SESSIONID:F7CA3274BEDF3C72AF4CC22742DC02D6,登录入口:本系统', 'login', '09', '4', '2018-01-16 18:33:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F7BF3D9D114A0FCD24D1DAAB755C20AF', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '16', '2018-01-16 18:33:46', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D69091D69D3EE447F550121A2768E4FD', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '59', '2018-01-16 18:35:36', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B2591849435C730F1A64153294576C3F', '192.168.11.179', '192.168.11.179', '1', '用户:超级管理员(root)退出登录!SESSIONID:F7CA3274BEDF3C72AF4CC22742DC02D6', 'logout', '09', '9', '2018-01-16 18:56:16', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5AD01F14A812FC3B5A0D9BCCCEE2CA34', '192.168.11.179', '192.168.11.179', '1', '用户:超级管理员(root)登录成功!SESSIONID:D8A40A6BF51C3DE5361F0297334D4B45,登录入口:本系统', 'login', '09', '7', '2018-01-16 19:51:37', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('03DDF1850BFB114E3C87899C8DB1F9D6', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '18', '2018-01-16 19:51:37', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A03DA346F0C096FB9F1362326558770A', '192.168.11.179', '192.168.11.179', '1', '新增资源【特殊提现】成功！', 'resourcemng', '02', '73', '2018-01-16 20:18:01', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6BC59AC99751B285B86F47B252E25946', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '24', '2018-01-16 20:18:12', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5A2E5F239912D8D7AB9F2C80944D9826', '192.168.11.179', '192.168.11.179', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:特殊提现,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '70', '2018-01-16 20:18:25', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D3FD801F4BCB8F7673964339B3E2D052', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '14', '2018-01-16 20:18:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('AF98D1F7419ABDF87EC49C0AB6CBC087', '192.168.11.179', '192.168.11.179', '1', '用户:超级管理员(root)登录成功!SESSIONID:D8A40A6BF51C3DE5361F0297334D4B45,登录入口:本系统', 'login', '09', '4', '2018-01-16 20:18:35', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B8135A02E90D597B628E6384CA8B68BF', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '14', '2018-01-16 20:18:36', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A7B7FFAFC62207466F791F18CA53D20E', '192.168.11.179', '192.168.11.179', '1', '用户:超级管理员(root)登录成功!SESSIONID:5B56A43F93CAC43056C92EEB651BD166,登录入口:本系统', 'login', '09', '3', '2018-01-16 20:19:57', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('981FD63716EC917EC30D3C977CFC2301', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '16', '2018-01-16 20:19:58', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A99FA3049BB9096417B007ECA87C8C72', '192.168.11.179', '192.168.11.179', '1', '用户:超级管理员(root)登录成功!SESSIONID:5B56A43F93CAC43056C92EEB651BD166,登录入口:本系统', 'login', '09', '2', '2018-01-16 20:21:06', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6678B30DD6EF3641480297A36E20806B', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2018-01-16 20:21:07', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9055E442D038A54C7EC5AC79B5DE60A1', '192.168.11.179', '192.168.11.179', '1', '新增资源【合伙人查看】成功！', 'resourcemng', '02', '28', '2018-01-16 20:34:41', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('392146B7AEBD153DFFDA3259273D0C97', '192.168.11.179', '192.168.11.179', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:合伙人查看,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '32', '2018-01-16 20:35:28', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C535043E6CB801A3383B556627A0583F', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2018-01-16 20:35:37', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3DB2FA2D005F8DD92CB91FC85B2CFD1F', '192.168.11.179', '192.168.11.179', '1', '用户:超级管理员(root)登录成功!SESSIONID:5B56A43F93CAC43056C92EEB651BD166,登录入口:本系统', 'login', '09', '2', '2018-01-16 20:35:42', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('340CF486CF69CFD74C950DF65D92FBA2', '192.168.11.179', '192.168.11.179', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '10', '2018-01-16 20:35:43', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FD14AAE4AD35AD42CC37EFFE6A272A82', '192.168.11.103', '192.168.11.179', '1', '用户:超级管理员(root)退出登录!SESSIONID:5B56A43F93CAC43056C92EEB651BD166', 'logout', '09', '41', '2018-01-16 22:15:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('834556F8E81E03565ECAB16B2A703E34', '192.168.11.103', '192.168.11.103', '1', '用户:超级管理员(root)登录成功!SESSIONID:135AB6037CDD57664ED88903F0DC3278,登录入口:本系统', 'login', '09', '5', '2018-01-16 23:28:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('639C79F36A9467BFF425599365F4E70D', '192.168.11.103', '192.168.11.103', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '17', '2018-01-16 23:28:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('93E7A227BC93B42188EBC01E9FE787B3', '192.168.11.103', '192.168.11.103', '1', '新增资源【代理用户】成功！', 'resourcemng', '02', '30', '2018-01-16 23:29:12', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F54491A9F7957C1D7EB742B9160668B6', '192.168.11.103', '192.168.11.103', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:代理用户,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '31', '2018-01-16 23:29:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('37A3B178A2F96DB9DE2B7598325A2044', '192.168.11.103', '192.168.11.103', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2018-01-16 23:29:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('C1ECC3632196245212EE0004A5BF071C', '192.168.11.103', '192.168.11.103', '1', '用户:超级管理员(root)登录成功!SESSIONID:135AB6037CDD57664ED88903F0DC3278,登录入口:本系统', 'login', '09', '3', '2018-01-16 23:29:33', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8173DC1877FDE339AB10ADF4C6C3D381', '192.168.11.103', '192.168.11.103', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '9', '2018-01-16 23:29:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('97C0126B0964919DA9723FAF88BB0C35', '192.168.11.103', '192.168.11.103', '1', '用户:超级管理员(root)退出登录!SESSIONID:135AB6037CDD57664ED88903F0DC3278', 'logout', '09', '29', '2018-01-16 23:49:56', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('10A27F225DFEB53E69EEFD09F0C2D619', '192.168.11.205', '192.168.11.205', '1', '用户:超级管理员(root)登录成功!SESSIONID:41798B321B279F1D17602F48FC2758DA,登录入口:本系统', 'login', '09', '5', '2018-01-17 18:54:28', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3ABB8C548C48833D25611F83CDCD6155', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '21', '2018-01-17 18:54:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('BA46D13D52A8729107084ABFB68CC548', '192.168.11.205', '192.168.11.205', '1', '新增资源【上传用户】成功！', 'resourcemng', '02', '46', '2018-01-17 18:55:32', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7976CCFB0F35EFE743FA2185D51ABB1B', '192.168.11.205', '192.168.11.205', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:上传用户,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '39', '2018-01-17 18:55:42', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A687D784B37090E1E543F08B897E8C2C', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '16', '2018-01-17 18:55:46', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A5EF1C2E4BA78CC48C0011A150E07726', '192.168.11.205', '192.168.11.205', '1', '用户:超级管理员(root)登录成功!SESSIONID:41798B321B279F1D17602F48FC2758DA,登录入口:本系统', 'login', '09', '3', '2018-01-17 18:55:54', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('507E2C55151B95F2EDBB81490F69FC2E', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2018-01-17 18:55:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8CC185BA0D922AB69624B8003D744CF2', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '27', '2018-01-17 18:59:36', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('754DEFE8A331F4CE5E80E9FC1D88E239', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '15', '2018-01-17 19:00:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5E46C00C30B34616DACF88984C83408C', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '21', '2018-01-17 19:00:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E3B1942A1C243685EE6748E8153442F7', '192.168.11.205', '192.168.11.205', '1', '用户:超级管理员(root)退出登录!SESSIONID:41798B321B279F1D17602F48FC2758DA', 'logout', '09', '9', '2018-01-17 19:21:16', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D74577E82C7E3DDB0D6BC589D24C6BFB', '192.168.11.205', '192.168.11.205', '1', '用户:超级管理员(root)登录成功!SESSIONID:746F3CE59CA15D9FF959D73EA89F7FA3,登录入口:本系统', 'login', '09', '5', '2018-01-17 20:24:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('681BE6208296A535DD8D37FCAD47D080', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2018-01-17 20:24:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6EB442CD59749451F413DE4E0A4A8F0D', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '23', '2018-01-17 20:24:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A76C76EF3AFDC37E02685D8E3931AFDD', '192.168.11.205', '192.168.11.205', '1', '操作类型:[修改],参数:[名称:eastcom_sw,描述:平台开发厂商,中文名:狼烟丰起],目标:[名称:eastcom_sw,描述:平台开发厂商,中文名:狼烟丰起],结果:[成功!]', 'departmentmng', '03', '40', '2018-01-17 20:25:04', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D3F3400F8B385ABCB35A08E70831675D', '192.168.11.205', '192.168.11.205', '1', '操作类型:[修改],参数:[名称:null,中文名称:狼烟丰起admin,详情:],目标:[修改],结果:[成功!]', 'rolemng', '03', '33', '2018-01-17 20:25:39', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('84BB4E3C25A716DA86D3546A9D32FBED', '192.168.11.205', '192.168.11.205', '1', '操作类型:[新增],参数:[名称:partner,中文名称:合伙人,详情:],目标:[新增],结果:[成功!]', 'rolemng', '02', '21', '2018-01-17 20:26:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('329AADBE06131C922818F463FFEDA4F7', '192.168.11.205', '192.168.11.205', '1', '操作类型:[新增],参数:[名称:agency,中文名称:代理,详情:],目标:[新增],结果:[成功!]', 'rolemng', '02', '24', '2018-01-17 20:26:14', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D497C6C2DA049BF9109F593992A09C04', '192.168.11.205', '192.168.11.205', '1', '操作类型:[新增],参数:[名称:uploader,中文名称:上传用户,详情:],目标:[新增],结果:[成功!]', 'rolemng', '02', '22', '2018-01-17 20:26:35', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('0F8B040FADAB4EACDF7DC1CFBB05C089', '192.168.11.205', '192.168.11.205', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:,删除资源:Portal管理,Portal工具管理,查找,添加,删除,修改,个人公告管理,系统公告管理,新增公告,编辑公告,生效/终止公告,删除公告],目标:[授权],结果:[成功!]', 'rolemng', '08', '92', '2018-01-17 20:29:20', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8E4CBA03876A0BBDD705CB9DF51F10DE', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '14', '2018-01-17 20:29:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('81DA72C09D7F1A7F3600B5E9CF5058F3', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[开始时间:null,结束时间:null,模块名称:null,操作状态:null,IP地址:null,用户级别:null,用户名称:null,操作内容:null],目标:[查询],结果条数:[236],结果:[成功!]', 'unconfigured', '01', '78', '2018-01-17 20:29:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D6D3CCFBD300797074D40B53A335AEA2', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '8', '2018-01-17 20:35:15', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('6BC70E4F7442CE595FACE2F6163EE9B1', '192.168.11.205', '192.168.11.205', '1', '操作类型:[更新],参数:[{menuExpandDepth=0, titleWordNum=5, isShowSubNavi=1, portalId=default_page, desktopSrc=personaldesktop/desktop.jsp, timeBeforeMenuAutoHide=150, type=startup, portalPageSrc=/pages/personaldesktop/desktop.jsp, closeable=1, enableColumnHide=1, mainMenusNum=5, showMenuNum=5, requestOnlineUserInterval=0, isShowNotice=0, portalName=default_iframe, showUserGuide=0, desktopId=desktop_iframe, theme=neptune, menuQuickSearch=1, desktopName=desktop_iframe, floderStrong=1, showDesktopInWindow=1, portalNameCn=个人桌面, userGuideName=用户指南.docx, isShowHistroy=0, defaultPageSize=20, tabsNum=10}],目标:[更新],自定义信息:[成功!],结果:[null]', 'unconfigured', '03', '58', '2018-01-17 20:35:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('04CA6CEBEBD2D1DF22AB5741C183585D', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '10', '2018-01-17 20:35:47', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E2B0EB083B8779B69E531B90965EF458', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '10', '2018-01-17 20:36:27', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('498817887A89038B154D9E158DB50249', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '18', '2018-01-17 20:37:05', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FEA401E8EB3A3102CA9978845177DF04', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '30', '2018-01-17 20:37:08', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('11EEC1A87DC4BA50C4DCC920F21503F8', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '14', '2018-01-17 20:37:19', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D54A92188DD4BFAAC347F960B56C82CA', '192.168.11.205', '192.168.11.205', '1', '操作类型:[查询],参数:[{}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2018-01-17 20:38:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('93DD8B5A033F99A285117A4CA7DC6FA0', '192.168.11.103', '192.168.11.205', '1', '用户:超级管理员(root)退出登录!SESSIONID:746F3CE59CA15D9FF959D73EA89F7FA3', 'logout', '09', '8', '2018-01-17 23:29:46', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('905504515B308A4738DD3750B521814B', '192.168.11.101', '192.168.11.101', '1', '用户:超级管理员(root)登录成功!SESSIONID:EC79A449DBFC19E1A3EF5B4468021582,登录入口:本系统', 'login', '09', '8', '2018-01-29 23:35:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('43206B8E6EC94D65A799802B4ED4720A', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '22', '2018-01-29 23:35:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('EE5FE94251C388BB52FF778BA4CA9C8C', '192.168.11.101', '192.168.11.101', '1', '新增资源【影视库管理】成功！', 'resourcemng', '02', '35', '2018-01-29 23:43:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9179147727F9983B39CFF64047DD4696', '192.168.11.101', '192.168.11.101', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:影视库管理,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '34', '2018-01-29 23:44:08', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7DADC3127092E8CE1A71997ED60E08A9', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '15', '2018-01-29 23:44:13', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('CA48A0AD271BFDCA88DEA34B54C9D861', '192.168.11.101', '192.168.11.101', '1', '用户:超级管理员(root)登录成功!SESSIONID:EC79A449DBFC19E1A3EF5B4468021582,登录入口:本系统', 'login', '09', '6', '2018-01-29 23:44:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('4512825FE9E46704CB3E5FC61044AEEC', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '10', '2018-01-29 23:44:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A3A0060DC5427A2D4EB501695F56349F', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2018-01-29 23:46:45', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('524D9A99C8B2FF44C644F0DF0F2FABD4', '192.168.11.101', '192.168.11.101', '1', '新增资源【域名管理】成功！', 'resourcemng', '02', '29', '2018-01-29 23:50:51', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('091F66DCB203D6B6C95041BE77BDCD8E', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '14', '2018-01-29 23:50:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('8C363B95925715B0D7BB12400E665EE8', '192.168.11.101', '192.168.11.101', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:域名管理,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '27', '2018-01-29 23:51:15', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FDCF9A545710BD1B093859161674E26E', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '13', '2018-01-29 23:51:21', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('93209BACAA65A871D650BEAC2A358295', '192.168.11.101', '192.168.11.101', '1', '用户:超级管理员(root)登录成功!SESSIONID:EC79A449DBFC19E1A3EF5B4468021582,登录入口:本系统', 'login', '09', '3', '2018-01-29 23:51:33', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('343AD54337995AB2CB05034FAC5562D7', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '12', '2018-01-29 23:51:34', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('828A9170D0EC1A576D9D90FD506F5E4D', '192.168.11.101', '192.168.11.101', '1', '新增资源【IP库管理】成功！', 'resourcemng', '02', '33', '2018-01-29 23:55:17', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('37410C4BCE6D89884848B9CFCB6F15F3', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '17', '2018-01-29 23:55:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('A113B9210C7AD3CF41FCAC07AE21D6F2', '192.168.11.101', '192.168.11.101', '1', '操作类型:[授权],参数:[名称:dxadmin,新增资源:IP库管理,删除资源:],目标:[授权],结果:[成功!]', 'rolemng', '08', '24', '2018-01-29 23:55:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('881AF46E1E2BCFC2ACCB53AE5891A93C', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '11', '2018-01-29 23:55:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('99F01150D9BAB1F92C456894D826BC04', '192.168.11.101', '192.168.11.101', '1', '用户:超级管理员(root)登录成功!SESSIONID:EC79A449DBFC19E1A3EF5B4468021582,登录入口:本系统', 'login', '09', '2', '2018-01-29 23:55:56', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('731889BC63A744A617088E4CE48D7D56', '192.168.11.101', '192.168.11.101', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '8', '2018-01-29 23:55:56', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('41F05556410B658D713BCF208E165B10', '192.168.11.101', '192.168.11.101', '1', '用户:超级管理员(root)退出登录!SESSIONID:EC79A449DBFC19E1A3EF5B4468021582', 'logout', '09', '16', '2018-01-31 23:31:44', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('973B0B6E98D0ABDD6617230BACB6E3AA', '192.168.43.86', '192.168.43.86', '1', '用户:超级管理员(root)登录成功!SESSIONID:E18D9F4BD909ED5BB0DF696F4EFDE2F4,登录入口:本系统', 'login', '09', '6', '2018-02-16 15:24:52', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('69928E3EEF09E46543DD05A25518A22C', '192.168.43.86', '192.168.43.86', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '45', '2018-02-16 15:24:55', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('81AC46CAD9228699D7B433114B13364B', '192.168.43.86', '192.168.43.86', '1', '用户:超级管理员(root)退出登录!SESSIONID:E18D9F4BD909ED5BB0DF696F4EFDE2F4', 'logout', '09', '0', '2018-02-16 15:46:23', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2701F6A4E55E0F974D47A08BDEA84D84', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)登录成功!SESSIONID:65BFA74073E311BB04405782324CA0D2,登录入口:本系统', 'login', '09', '8', '2018-04-04 20:00:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('2EA383E64747EFE86E60BB431F0CEF59', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '25', '2018-04-04 20:00:24', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('1D9D062C5D420DC95E2075D897666086', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '48', '2018-04-04 20:00:56', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9E286F3BD49895C18C73FED33BAC5888', '192.168.0.123', '192.168.0.123', '1', '操作类型:[修改],参数:[名称:null,中文名称:管理员,详情:],目标:[修改],结果:[成功!]', 'rolemng', '03', '32', '2018-04-04 20:01:22', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('368A532E147CCD92B718F53BE660DA9B', '192.168.0.123', '192.168.0.123', '1', '操作类型:[修改],参数:[用户名:root,中文名:超级管理员,电子邮箱:,手机号码:13912345678,用户级别:超级管理员,性别:1],目标:[修改],结果:[成功!]', 'unconfigured', '08', '77', '2018-04-04 20:01:48', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('ACB74D1768C7D82DEE6FBB4ACEA9E44E', '192.168.0.123', '192.168.0.123', '1', '操作类型:[修改],参数:[用户名:root,中文名:超级管理员,电子邮箱:,手机号码:13912345678,用户级别:超级管理员,性别:1],目标:[修改],结果:[成功!]', 'unconfigured', '08', '59', '2018-04-04 20:02:14', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('E6E88989B5FCB3B385D41A85ED0B11C6', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '35', '2018-04-04 20:02:21', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('1B97DF69BB753B985E6B434FAB944CE4', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '17', '2018-04-04 20:19:33', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3E9A8DB2E1E480FA9412B597B8EA8705', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '12', '2018-04-04 20:23:06', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('15F3D772EFACC86D05F54E6FBB87B997', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)退出登录!SESSIONID:65BFA74073E311BB04405782324CA0D2', 'logout', '09', '30', '2018-04-04 20:43:37', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('F1B4799ED695F0820200F035FB24124B', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)登录成功!SESSIONID:DB2FDA89ABDB1E72A1C1674A0698294C,登录入口:本系统', 'login', '09', '5', '2018-04-04 21:39:39', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FCF1BD80F73E0103F2DF3CC72C4BE0F0', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '17', '2018-04-04 21:39:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('FC2613B13D196A886B3B26CB5400A3CE', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)退出登录!SESSIONID:DB2FDA89ABDB1E72A1C1674A0698294C', 'logout', '09', '45', '2018-04-04 22:00:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('7D893446DFB7470DFC544AC88D741248', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)登录成功!SESSIONID:68283DA16BAEBA40E79B2F3FA1620BAE,登录入口:本系统', 'login', '09', '14', '2018-04-04 22:11:01', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('0ADB87EFCFAEFD282D82A4273BA88C32', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '35', '2018-04-04 22:11:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('AE7A4D071EA4569521C559F93E38F3D0', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[名称:,],目标:[查询],结果:[成功!]', 'unconfigured', '01', '18', '2018-04-04 22:11:38', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('9A995CFC170120F3BFE50C5B8A3D5208', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '25', '2018-04-04 22:11:52', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('D10EE46D67F66A8E4F3A054AC5A15892', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)退出登录!SESSIONID:68283DA16BAEBA40E79B2F3FA1620BAE', 'logout', '09', '38', '2018-04-04 22:33:42', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B41D731057BABE280D93246BAAF0E40C', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)登录成功!SESSIONID:B21F8DDCCC0DB824C0A11C3FA04BD5D0,登录入口:本系统', 'login', '09', '13', '2018-04-05 10:01:38', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('877FCEC6215BA9BFA3FAA64149E20668', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '31', '2018-04-05 10:01:40', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('3E137E38BC89DF59AD0BE5B6694004D8', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '45', '2018-04-05 10:03:02', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('BE1112C8591B93615AA9F283405FB1EE', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[关键字:null],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '50', '2018-04-05 10:15:05', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B0BA68D80C61AF339962F85AEBC9705A', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)退出登录!SESSIONID:B21F8DDCCC0DB824C0A11C3FA04BD5D0', 'logout', '09', '40', '2018-04-05 10:35:25', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('B843F6A26CAD057D0F6EA1F44DBA5971', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)登录成功!SESSIONID:574A9658DC6F30FEF8BD7758F9D88830,登录入口:本系统', 'login', '09', '10', '2018-04-13 03:52:27', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('5DEA6431FE887F2BCBAE28660332C1B1', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[{type=startup}],目标:[查询],结果条数:[1],自定义信息:[成功!],结果:[null]', 'unconfigured', '01', '36', '2018-04-13 03:52:29', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('361AD8B34BC84C34222DE193FC02C12A', '192.168.0.123', '192.168.0.123', '1', '操作类型:[查询],参数:[关键字:],目标:[查询],结果条数:[1],结果:[成功!]', 'unconfigured', '01', '41', '2018-04-13 03:53:01', 'root', null, null, null, null, null);
INSERT INTO `sys_logs` VALUES ('DE04BA90E1BDF47F59E614AB25518CFF', '192.168.0.123', '192.168.0.123', '1', '用户:超级管理员(root)退出登录!SESSIONID:574A9658DC6F30FEF8BD7758F9D88830', 'logout', '09', '123', '2018-04-13 04:22:10', 'root', null, null, null, null, null);

-- ----------------------------
-- Table structure for `sys_logs_stat`
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs_stat`;
CREATE TABLE `sys_logs_stat` (
  `ID_` varchar(32) NOT NULL,
  `MODULE_CODE` varchar(50) NOT NULL,
  `TIME_TYPE` varchar(1) NOT NULL,
  `TIME_ID` varchar(10) NOT NULL,
  `USERNAME_` varchar(50) NOT NULL,
  `OPT_TIMES` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_logs_stat
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_model_deploy`
-- ----------------------------
DROP TABLE IF EXISTS `sys_model_deploy`;
CREATE TABLE `sys_model_deploy` (
  `ID_` varchar(32) NOT NULL,
  `MODELID` varchar(32) NOT NULL,
  `DEPLOYER` varchar(50) NOT NULL,
  `DEPLOY_TIME` varchar(24) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_model_deploy
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_notification`
-- ----------------------------
DROP TABLE IF EXISTS `sys_notification`;
CREATE TABLE `sys_notification` (
  `ID_` varchar(32) NOT NULL,
  `CONTENT_` varchar(3000) NOT NULL,
  `EFFECT_ENDTIME` varchar(24) NOT NULL,
  `EFFECT_STARTTIME` varchar(24) NOT NULL,
  `FILE_` varchar(1000) NOT NULL,
  `FILE_REAL` varchar(1000) NOT NULL,
  `ISTOP` varchar(1) NOT NULL,
  `LEVEL_` varchar(2) NOT NULL,
  `PUBLISH_TIME` varchar(24) NOT NULL,
  `STATUS_` varchar(1) NOT NULL,
  `TITLE_` varchar(100) NOT NULL,
  `TYPE_` varchar(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_notification
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_notification_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_notification_log`;
CREATE TABLE `sys_notification_log` (
  `ID_` varchar(32) NOT NULL,
  `NOTIFICATION_ID` varchar(32) NOT NULL,
  `VIEW_IPADDRESS` varchar(32) NOT NULL,
  `VIEW_TIME` varchar(32) NOT NULL,
  `VIEW_USERID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_notification_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_notification_rel`
-- ----------------------------
DROP TABLE IF EXISTS `sys_notification_rel`;
CREATE TABLE `sys_notification_rel` (
  `ID_` varchar(32) NOT NULL,
  `REL_ID` varchar(255) NOT NULL,
  `REL_TYPE` varchar(1) NOT NULL,
  `NOTIFICATION_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_notification_rel
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_portal`
-- ----------------------------
DROP TABLE IF EXISTS `sys_portal`;
CREATE TABLE `sys_portal` (
  `ID_` varchar(32) NOT NULL,
  `COLUMN_WIDTH` varchar(100) NOT NULL,
  `DESC_` varchar(255) NOT NULL,
  `IS_DEFAULT` varchar(1) NOT NULL,
  `NAME_` varchar(100) NOT NULL,
  `ORDER_` varchar(1) NOT NULL,
  `USER_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_portal
-- ----------------------------
INSERT INTO `sys_portal` VALUES ('D2A2D17BACBC358E7C08F0DD6E215324', '50,50', 'this is root\'s user portal!', '1', '默认Portal', '0', '000B2D2EB2100F0A51859E5397448D00');

-- ----------------------------
-- Table structure for `sys_portlet_conf`
-- ----------------------------
DROP TABLE IF EXISTS `sys_portlet_conf`;
CREATE TABLE `sys_portlet_conf` (
  `ID_` varchar(32) NOT NULL,
  `COLUMN_INT` varchar(1) NOT NULL,
  `GADGETS_ID` varchar(32) NOT NULL,
  `HEIGHT_` varchar(10) NOT NULL,
  `PORTAL_ID` varchar(32) NOT NULL,
  `POSITIONSEQ_` varchar(1) NOT NULL,
  `REFRESH_TIME` varchar(10) NOT NULL,
  `COLUMN_NUMBER` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_portlet_conf
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_rescource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_rescource`;
CREATE TABLE `sys_rescource` (
  `ID_` varchar(32) NOT NULL,
  `CREATE_TIME` varchar(24) NOT NULL,
  `CREATOR_` varchar(20) DEFAULT NULL,
  `IMAGE_` varchar(255) DEFAULT NULL,
  `IS_SHOWDESKTOP` varchar(1) NOT NULL,
  `IS_WEBPAGE` varchar(1) NOT NULL,
  `KIND_` varchar(1) NOT NULL,
  `LOCATION_` varchar(255) DEFAULT NULL,
  `NAME_` varchar(50) NOT NULL,
  `NAME_CN` varchar(100) NOT NULL,
  `ORDER_` int(11) NOT NULL,
  `REMARKS_` varchar(255) DEFAULT NULL,
  `STATUS_` varchar(1) NOT NULL,
  `PARENT_ID` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_rescource
-- ----------------------------
INSERT INTO `sys_rescource` VALUES ('8a88049c39b3d2140139b45e37c60000', '2013-01-28 10:21:36', 'root', 'system_management.png', '0', '0', '0', null, 'manager', '系统管理', '1', null, '1', null);
INSERT INTO `sys_rescource` VALUES ('8a8804493c032b34013c035f6a900001', '2013-01-28 10:21:36', 'root', null, '0', '0', '0', 'welcome.jsp', 'PortalManager', 'Portal管理', '3', null, '1', '8a88049c39b3d2140139b45e37c60000');
INSERT INTO `sys_rescource` VALUES ('297e39ad3bdb58b4013bdb68b2320003', '2013-01-28 10:21:36', 'root', null, '0', '1', '0', '/pages/portal/managerPartal.jsp', 'gadgetsManager', 'Portal工具管理', '0', null, '1', '8a8804493c032b34013c035f6a900001');
INSERT INTO `sys_rescource` VALUES ('297e39ad3bdb713c013bdb75b70a0000', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/portal/queryPortalPage', 'search', '查找', '0', null, '1', '297e39ad3bdb58b4013bdb68b2320003');
INSERT INTO `sys_rescource` VALUES ('297e39ad3bdf8001013bdfa1c0c70000', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/portal/addPortalFunction', 'addPortal', '添加', '1', null, '1', '297e39ad3bdb58b4013bdb68b2320003');
INSERT INTO `sys_rescource` VALUES ('297e39ad3be04b42013be04cead80000', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/portal/deletePortalFunction', 'deletePortal', '删除', '2', null, '1', '297e39ad3bdb58b4013bdb68b2320003');
INSERT INTO `sys_rescource` VALUES ('297e39ad3be09597013be0996d900000', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/portal/editPortalFunction', 'updatePortal', '修改', '3', null, '1', '297e39ad3bdb58b4013bdb68b2320003');
INSERT INTO `sys_rescource` VALUES ('8a88049c39b3d2140139b45ef6070001', '2013-01-28 10:21:36', 'root', null, '0', '0', '0', 'welcome.html', 'securityMgr', '安全管理', '0', null, '1', '8a88049c39b3d2140139b45e37c60000');
INSERT INTO `sys_rescource` VALUES ('003000000000000000000000000000', '2013-01-28 10:21:36', 'root', 'Role_Management.png', '1', '1', '0', '/pages/sysmng/security/rolemng/roleMng.jsp', 'rolemng', '角色管理', '1', null, '1', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c38939c0006', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveRole?opt=0', 'add', '添加角色', '0', '保存添加角色', '1', '003000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c393be80007', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveRole?opt=1', 'edit', '编辑角色', '1', '编辑角色信息', '1', '003000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c3a8fef0008', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/updateRoleResourceRange', 'resourceAccredit', '资源授权', '2', '对角色进行资源授权', '1', '003000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c3b91de0009', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/updateRoleRegionRange', 'regionAccredit', '区域授权', '3', '对角色进行区域授权', '1', '003000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c3c0892000a', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/deleteRole', 'delete', '删除角色', '4', '删除角色', '1', '003000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('004000000000000000000000000000', '2013-01-28 10:21:36', 'root', 'user.png', '1', '1', '0', '/pages/sysmng/security/usermng/userMng.jsp', 'usermng', '用户管理', '0', null, '1', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c2e34590001', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveUser.action?opt=1', 'edit', '编辑账户', '1', '编辑账户信息', '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c32dcfe0002', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/changeUserRoleRange', 'roleAccredit', '角色授权', '2', '对账户进行角色授权', '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c34205a0003', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/changeUserDeptRange', 'deptAccredit', '部门授权', '3', '对账户进行部门授权', '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c358bc50004', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/setAccountEnabled', 'enable/disable', '启用/停用账户', '4', '对账户进行启用/停用操作', '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c3671c90005', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/deleteUser', 'delete', '删除账户', '5', '删除账户', '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804493b3fbeea013b3fc2ac200005', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/querySingleUserInfo', 'queryAccountDetail', '查询账户详情', '7', '查询单个账户详情', '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('4028e38a533f446701533f45e2c10000', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/batchSaveUser', 'batchSave', '批量新增', '8', null, '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804dc3b2ac9c8013b2bf516ff0004', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveUser.action?opt=0', 'add', '添加帐户', '0', '保存添加帐号', '1', '004000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('005000000000000000000000000000', '2013-01-28 10:21:36', 'root', 'resource_management.png', '1', '1', '0', '/pages/sysmng/security/resourcemng/resourceMng.jsp', 'resourcemng', '资源管理', '3', null, '1', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3b6d42013b3b868b9f0003', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveResource?opt=0', 'add', '新增资源', '0', null, '1', '005000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3b6d42013b3b86e65e0004', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveResource?opt=1', 'update', '修改资源', '1', null, '1', '005000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3b6d42013b3b8770890005', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/deleteResource', 'delete', '删除资源', '2', null, '1', '005000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('031000000000000000000000000000', '2013-01-28 10:21:36', 'root', 'division_management.png', '1', '1', '0', '/pages/sysmng/security/departmentmng/departmentMng.jsp', 'departmentmng', '部门管理', '2', null, '1', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3bbfdd013b3bc77c030006', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveDepartment?opt=0', 'add', '新增', '0', null, '1', '031000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3bbfdd013b3bc7d9110008', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/saveDepartment?opt=1', 'update', '修改', '1', null, '1', '031000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3bbfdd013b3bc89353000a', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/security/deleteDepartment', 'delete', '删除', '2', null, '1', '031000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a88044939f5ee140139f5f5b8f30001', '2013-01-28 10:21:36', 'root', 'system_notice.png', '1', '1', '0', '/pages/notify/notificationMng.jsp', 'sysNotificationmng', '系统公告管理', '4', '系统公告管理', '1', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c3e8390000c', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/notification/saveNotification.action?opt=0', 'add', '新增公告', '0', '新增公告', '1', '8a88044939f5ee140139f5f5b8f30001');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c3f07e1000d', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/notification/saveNotification.action?opt=1', 'edit', '编辑公告', '1', '编辑公告信息', '1', '8a88044939f5ee140139f5f5b8f30001');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c447a6c000e', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/notification/changeStatus', 'enable/disable', '生效/终止公告', '2', '对公告进行生效/终止操作', '1', '8a88044939f5ee140139f5f5b8f30001');
INSERT INTO `sys_rescource` VALUES ('8a8804493b2c2b9b013b2c44f5ed000f', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/notification/removeNotifications', 'delete', '删除公告', '3', '删除公告', '1', '8a88044939f5ee140139f5f5b8f30001');
INSERT INTO `sys_rescource` VALUES ('8ac6d8533e680c37013e68103cb60000', '2013-05-03 09:44:54', 'root', null, '0', '0', '0', 'welcome.html', 'controlpanel', '控制面板', '5', '系统控制面板包括redis初始化，keys管理等。', '1', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_rescource` VALUES ('8ac6d8533e680c37013e6813329f0001', '2013-05-03 09:48:08', 'root', null, '0', '1', '0', '/pages/controlpanel/controlpanel.jsp', 'sysoptions', '系统选项', '1', null, '1', '8ac6d8533e680c37013e68103cb60000');
INSERT INTO `sys_rescource` VALUES ('8ac6d8533e680c37013e681db10f0003', '2013-05-03 09:59:36', 'root', null, '0', '1', '0', '/pages/redismng/redismng.jsp', 'redismng', 'Redis Keys 管理', '0', null, '1', '8ac6d8533e680c37013e68103cb60000');
INSERT INTO `sys_rescource` VALUES ('8a88049c39b3d2140139b45f54090002', '2013-01-28 10:21:36', 'root', null, '0', '0', '0', 'welcome.html', 'logMgr', '日志管理', '1', null, '1', '8a88049c39b3d2140139b45e37c60000');
INSERT INTO `sys_rescource` VALUES ('038000000000000000000000000000', '2013-01-28 10:21:36', 'root', 'system_journal.png', '1', '1', '0', '/pages/log/sysLogMng.jsp', 'syslog', '系统日志查询', '0', null, '1', '8a88049c39b3d2140139b45f54090002');
INSERT INTO `sys_rescource` VALUES ('8a8804493a101765013a1096820b0004', '2013-01-28 10:21:36', 'root', 'notify.png', '1', '1', '0', '/pages/notify/userNotifications.jsp', 'notificationLog', '个人公告管理', '1', null, '1', '8a88049c39b3d2140139b45f54090002');
INSERT INTO `sys_rescource` VALUES ('8a88049c39b3d2140139b48b9934003b', '2013-01-28 10:21:36', 'root', null, '0', '0', '0', 'welcome.jsp', 'baseDataManager', '系统基础数据管理', '2', null, '1', '8a88049c39b3d2140139b45e37c60000');
INSERT INTO `sys_rescource` VALUES ('020000000000000000000000000000', '2013-01-28 10:21:36', 'root', 'data_maintenance.png', '1', '1', '0', '/pages/baseinfo/sysCommonMng.jsp', 'baseinfo', '系统数据维护', '5', null, '1', '8a88049c39b3d2140139b48b9934003b');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3bb4ae013b3bbda54a0006', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/saveSysCommon?opt=0', 'add', '新增', '0', null, '1', '020000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3bb4ae013b3bbe0c300008', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/saveSysCommon?opt=1', 'update', '修改', '1', null, '1', '020000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3bb4ae013b3bbedcb6000a', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/deleteSysCommon', 'delete', '删除', '2', null, '1', '020000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('8a8804e13b3bb4ae013b3bbedcb6000b', '2013-01-28 10:21:36', 'root', null, '0', '0', '1', '/sysmng/asynchronizeGetSysCommon', 'query', '查询', '3', null, '1', '020000000000000000000000000000');
INSERT INTO `sys_rescource` VALUES ('004000000000000000000000000000', '2013-01-28 10:21:36', 'root', 'user.png', '1', '1', '0', '/pages/sysmng/security/usermng/userMng.jsp', 'usermng', '用户管理', '0', null, '1', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_rescource` VALUES ('40288be4608ca1bb01608cc8c3290000', '2017-12-25 16:27:35', 'root', '', '1', '0', '0', 'wecome.jsp', 'appMng', '应用功能', '2', '', '1', null);
INSERT INTO `sys_rescource` VALUES ('40288be4608ca1bb01608cc95e640001', '2017-12-25 16:28:15', 'root', '', '0', '1', '0', '/pages/rewardMng/rewardMng.jsp', 'rewardMng', '打赏详细信息查看', '0', '', '1', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_rescource` VALUES ('40288b3360fe87cc0160fee7a2be0000', '2018-01-16 20:18:01', 'root', '', '1', '1', '0', '/pages/app/specialWithdraw/specialWithdraw.jsp', 'specialWithdraw', '特殊提现', '1', '', '1', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_rescource` VALUES ('40288b3360fe87cc0160fef6e3780001', '2018-01-16 20:34:41', 'root', '', '0', '1', '0', '/pages/app/partnerMng/partnerMng.jsp', 'partnerMng', '合伙人查看', '2', '', '1', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_rescource` VALUES ('40288b3360fe87cc0160ff96a9460002', '2018-01-16 23:29:11', 'root', '', '1', '1', '0', '/pages/app/agencyMng/agencyMng.jsp', 'agencyMng', '代理用户', '3', '', '1', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_rescource` VALUES ('40288b3360fe87cc016103c278d00003', '2018-01-17 18:55:32', 'root', '', '1', '1', '0', '/pages/app/uploader/uploader.jsp', 'uploadMng', '上传用户', '4', '', '1', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_rescource` VALUES ('40288b3360fe87cc01614296a6030008', '2018-01-29 23:43:44', 'root', '', '1', '1', '0', '/pages/app/videoMng/videoMng.jsp', 'videoMng', '影视库管理', '5', '', '1', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_rescource` VALUES ('40288b3360fe87cc0161429d27d50009', '2018-01-29 23:50:51', 'root', '', '0', '1', '0', '/pages/app/domainMng/domainMng.jsp', 'domainMng', '域名管理', '6', '', '1', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_rescource` VALUES ('40288b3360fe87cc016142a139cc000a', '2018-01-29 23:55:17', 'root', '', '1', '1', '0', '/pages/app/ipMng/ipMng.jsp', 'ipMng', 'IP库管理', '7', '', '1', '40288be4608ca1bb01608cc8c3290000');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `ID_` varchar(32) NOT NULL,
  `CREATE_TIME` varchar(24) NOT NULL,
  `CREATOR_` varchar(32) NOT NULL,
  `DESCRIPTION_` varchar(255) DEFAULT NULL,
  `NAME_` varchar(32) NOT NULL,
  `NAME_CN` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '2010-09-07 09:55:17', 'ezhoudan', '', 'dxadmin', '管理员');
INSERT INTO `sys_role` VALUES ('40288b3360fe87cc0161041556060004', '2018-01-17 20:26:02', 'root', '', 'partner', '合伙人');
INSERT INTO `sys_role` VALUES ('40288b3360fe87cc0161041585720005', '2018-01-17 20:26:14', 'root', '', 'agency', '代理');
INSERT INTO `sys_role` VALUES ('40288b3360fe87cc01610415d4e60006', '2018-01-17 20:26:35', 'root', '', 'uploader', '上传用户');

-- ----------------------------
-- Table structure for `sys_roleregion`
-- ----------------------------
DROP TABLE IF EXISTS `sys_roleregion`;
CREATE TABLE `sys_roleregion` (
  `ID_` varchar(32) NOT NULL,
  `REGION` varchar(2) NOT NULL,
  `ROLE_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_roleregion
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_roleresource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_roleresource`;
CREATE TABLE `sys_roleresource` (
  `ROLE_ID` varchar(32) NOT NULL,
  `RESOURCE_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_roleresource
-- ----------------------------
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '003000000000000000000000000000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c38939c0006');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c393be80007');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c3a8fef0008');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c3b91de0009');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c3c0892000a');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a88049c39b3d2140139b45e37c60000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a88049c39b3d2140139b45ef6070001');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a88049c39b3d2140139b45f54090002');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '038000000000000000000000000000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a88049c39b3d2140139b48b9934003b');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '020000000000000000000000000000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3bb4ae013b3bbda54a0006');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3bb4ae013b3bbe0c300008');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3bb4ae013b3bbedcb6000a');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3bb4ae013b3bbedcb6000b');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '004000000000000000000000000000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804dc3b2ac9c8013b2bf516ff0004');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c2e34590001');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c32dcfe0002');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c34205a0003');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c358bc50004');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b2c2b9b013b2c3671c90005');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804493b3fbeea013b3fc2ac200005');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '4028e38a533f446701533f45e2c10000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '031000000000000000000000000000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3bbfdd013b3bc77c030006');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3bbfdd013b3bc7d9110008');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3bbfdd013b3bc89353000a');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '005000000000000000000000000000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3b6d42013b3b868b9f0003');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3b6d42013b3b86e65e0004');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8a8804e13b3b6d42013b3b8770890005');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8ac6d8533e680c37013e68103cb60000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8ac6d8533e680c37013e681db10f0003');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '8ac6d8533e680c37013e6813329f0001');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288be4608ca1bb01608cc8c3290000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288be4608ca1bb01608cc95e640001');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288b3360fe87cc0160fee7a2be0000');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288b3360fe87cc0160fef6e3780001');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288b3360fe87cc0160ff96a9460002');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288b3360fe87cc016103c278d00003');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288b3360fe87cc01614296a6030008');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288b3360fe87cc0161429d27d50009');
INSERT INTO `sys_roleresource` VALUES ('8ac6f2ce2aafa1fc012ae9e7813626b3', '40288b3360fe87cc016142a139cc000a');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `ID_` varchar(32) NOT NULL,
  `ACCOUNT_ENABLED` varchar(1) NOT NULL,
  `ACCOUNT_CREATE_TIME` varchar(24) NOT NULL,
  `ACCOUNT_EXPIRED_ENDTIME` varchar(24) DEFAULT NULL,
  `ACCOUNT_EXPIRED_STARTTIME` varchar(24) DEFAULT NULL,
  `AUDIT_ALARMMSG_RECV_TYPE` varchar(8) DEFAULT NULL,
  `AUDIT_TEAM` varchar(20) DEFAULT NULL,
  `CATEGORY_` varchar(1) NOT NULL,
  `CREATOR_` varchar(20) NOT NULL,
  `EMAIL_` varchar(50) DEFAULT NULL,
  `FIXED_NO` varchar(20) DEFAULT NULL,
  `FULLNAME` varchar(30) DEFAULT NULL,
  `ISSHOWMAXTAB` varchar(1) NOT NULL,
  `LAST_LOGIN_TIME` varchar(24) DEFAULT NULL,
  `LAST_LOGIN_IP` varchar(16) DEFAULT NULL,
  `MOBILE_NO` varchar(20) DEFAULT NULL,
  `NAME_PINYIN` varchar(100) DEFAULT NULL,
  `OLD_PASSWORD` varchar(240) DEFAULT NULL,
  `OWNER_` varchar(20) DEFAULT NULL,
  `PASSWORD_` varchar(80) NOT NULL,
  `PWD_EXPIRED_DAYS` int(11) NOT NULL,
  `PWD_MODIFY_TIME` varchar(24) DEFAULT NULL,
  `SEX_` varchar(1) NOT NULL,
  `TIMES_` int(11) NOT NULL,
  `USERLEVEL` varchar(1) NOT NULL,
  `USERNAME` varchar(20) NOT NULL,
  `VERSION_` int(11) DEFAULT NULL,
  `XTHEME_` varchar(10) DEFAULT NULL,
  `CITY_` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('000B2D2EB2100F0A51859E5397448D00', '1', '2012-07-25 14:09:21', '', '', null, null, '0', 'admin', '', '', '超级管理员', '0', '2018-04-13 03:52:27', '192.168.0.123', '13912345678', 'caojiguanliyuanyunchaojiguanliyuanyun|cjgly', 'dec167b9428fe2a36f198e7fc14e9b71b7c60744|541bab598edd98dda173f2c23f61dcd099626f6d|1c6e1aa19de16cd7f2e49568f991e5b5245608f2', '', '24cf272e85d71bdd434a7e9eeabbc66af682ae6e', '90', '2018-04-04 20:02:14', '1', '907', '1', 'root', '0', 'neptune', '');

-- ----------------------------
-- Table structure for `sys_userdepartment`
-- ----------------------------
DROP TABLE IF EXISTS `sys_userdepartment`;
CREATE TABLE `sys_userdepartment` (
  `USER_ID` varchar(32) NOT NULL,
  `DEPT_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_userdepartment
-- ----------------------------
INSERT INTO `sys_userdepartment` VALUES ('000B2D2EB2100F0A51859E5397448D00', '20090625160755252');

-- ----------------------------
-- Table structure for `sys_userdesktop`
-- ----------------------------
DROP TABLE IF EXISTS `sys_userdesktop`;
CREATE TABLE `sys_userdesktop` (
  `ID_` varchar(32) NOT NULL,
  `CREATE_TIME` varchar(24) NOT NULL,
  `ORDER_` int(11) NOT NULL,
  `USER_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_userdesktop
-- ----------------------------
INSERT INTO `sys_userdesktop` VALUES ('4028805e5ee552bb015ee561a86e0000', '2017-10-04 11:15:36', '0', '000B2D2EB2100F0A51859E5397448D00');
INSERT INTO `sys_userdesktop` VALUES ('4028805e5ee552bb015ee561c1de0001', '2017-10-04 11:15:42', '1', '000B2D2EB2100F0A51859E5397448D00');
INSERT INTO `sys_userdesktop` VALUES ('40288b3360fe87cc0161041e76980007', '2018-01-17 20:36:00', '1', '000B2D2EB2100F0A51859E5397448D00');

-- ----------------------------
-- Table structure for `sys_usernotification_deleted`
-- ----------------------------
DROP TABLE IF EXISTS `sys_usernotification_deleted`;
CREATE TABLE `sys_usernotification_deleted` (
  `ID_` varchar(32) NOT NULL,
  `NOTIFICATION_ID` varchar(32) NOT NULL,
  `USER_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_usernotification_deleted
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_userrole`
-- ----------------------------
DROP TABLE IF EXISTS `sys_userrole`;
CREATE TABLE `sys_userrole` (
  `USER_ID` varchar(32) NOT NULL,
  `ROLE_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_userrole
-- ----------------------------
INSERT INTO `sys_userrole` VALUES ('000B2D2EB2100F0A51859E5397448D00', '8ac6f2ce2aafa1fc012ae9e7813626b3');

-- ----------------------------
-- Table structure for `sys_user_depart_range`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_depart_range`;
CREATE TABLE `sys_user_depart_range` (
  `USER_ID` varchar(32) NOT NULL,
  `DEPT_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_depart_range
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user_online_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_online_log`;
CREATE TABLE `sys_user_online_log` (
  `ID_` varchar(32) NOT NULL,
  `SESSION_ID` varchar(50) NOT NULL,
  `USER_NAME` varchar(50) NOT NULL,
  `START_TIME` decimal(18,0) NOT NULL,
  `CLIENT_IP` varchar(25) NOT NULL,
  `START_TIME_STR` varchar(25) NOT NULL,
  `END_TIME` decimal(18,0) NOT NULL,
  `END_TIME_STR` varchar(25) NOT NULL,
  `LIVE_TIME` decimal(18,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_online_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user_online_log_d`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_online_log_d`;
CREATE TABLE `sys_user_online_log_d` (
  `ID_` varchar(32) NOT NULL,
  `TIME_ID` varchar(25) NOT NULL,
  `USER_NAME` varchar(50) NOT NULL,
  `LIVE_TIME` decimal(18,0) NOT NULL,
  `LOGIN_COUNT` decimal(18,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_online_log_d
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_user_req_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_req_log`;
CREATE TABLE `sys_user_req_log` (
  `ID_` varchar(32) NOT NULL,
  `SESSION_ID` varchar(50) NOT NULL,
  `USER_NAME` varchar(50) NOT NULL,
  `REQ_URL` varchar(200) NOT NULL,
  `CLIENT_IP` varchar(20) NOT NULL,
  `SERVER_IP` varchar(20) NOT NULL,
  `REQ_TIME` decimal(18,0) NOT NULL,
  `REQ_TIME_STR` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_req_log
-- ----------------------------
