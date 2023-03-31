/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50733
Source Host           : 127.0.0.1:3306
Source Database       : ucan_admin

Target Server Type    : MYSQL
Target Server Version : 50733
File Encoding         : 65001

Date: 2023-03-31 14:19:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cross_permission
-- ----------------------------
DROP TABLE IF EXISTS `cross_permission`;
CREATE TABLE `cross_permission` (
  `cross_id` varchar(64) NOT NULL COMMENT '跨权限者的ID（用户ID、用户组ID、职位ID、组织ID）',
  `permission_id` varchar(64) NOT NULL COMMENT '被跨越的权限ID',
  `cross_name` varchar(30) DEFAULT NULL COMMENT '跨越者名称',
  `permission_name` varchar(30) DEFAULT NULL COMMENT '被跨越权限名称',
  `sys_code` varchar(20) NOT NULL COMMENT '系统编码',
  PRIMARY KEY (`cross_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='跨权限映射表';

-- ----------------------------
-- Records of cross_permission
-- ----------------------------

-- ----------------------------
-- Table structure for cross_role
-- ----------------------------
DROP TABLE IF EXISTS `cross_role`;
CREATE TABLE `cross_role` (
  `cross_id` varchar(64) NOT NULL COMMENT '跨角色者的ID（用户ID、用户组ID、职位ID、组织ID）',
  `role_id` varchar(64) NOT NULL COMMENT '被跨越的角色ID',
  `cross_name` varchar(30) DEFAULT NULL COMMENT '跨角色者的名称',
  `role_name` varchar(30) DEFAULT NULL COMMENT '被跨越角色名称',
  `sys_code` varchar(20) NOT NULL COMMENT '系统编码',
  PRIMARY KEY (`cross_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='跨角色映射表';

-- ----------------------------
-- Records of cross_role
-- ----------------------------

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `group_id` varchar(64) NOT NULL COMMENT '用户组ID',
  `group_name` varchar(64) NOT NULL COMMENT '用户组名称',
  `group_code` varchar(20) DEFAULT NULL COMMENT '组编码',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组（无上下级关系）';

-- ----------------------------
-- Records of group
-- ----------------------------

-- ----------------------------
-- Table structure for mutex_roles
-- ----------------------------
DROP TABLE IF EXISTS `mutex_roles`;
CREATE TABLE `mutex_roles` (
  `role_id` varchar(64) NOT NULL COMMENT '角色Id',
  `mutex_role_id` varchar(64) NOT NULL COMMENT '互斥角色Id',
  `sys_code` varchar(20) DEFAULT NULL COMMENT '系统编码',
  PRIMARY KEY (`role_id`,`mutex_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='互斥角色表';

-- ----------------------------
-- Records of mutex_roles
-- ----------------------------
INSERT INTO `mutex_roles` VALUES ('53c735852284434ca6e95fe8ea3a47e5', 'ee209a85eb494a5fa90bd5448d309736', null);

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
  `org_id` varchar(64) NOT NULL COMMENT '组织ID',
  `org_name` varchar(100) NOT NULL COMMENT '组织名称',
  `org_type` varchar(20) NOT NULL COMMENT '组织类型（1：总公司 2：分公司 3：部门）',
  `org_code` varchar(20) DEFAULT NULL COMMENT '组织编码',
  `parent_id` varchar(64) NOT NULL COMMENT '上级组织ID',
  `position` int(3) DEFAULT NULL COMMENT '节点位置，越小越靠前',
  `icon` varchar(50) DEFAULT NULL COMMENT '字体图标',
  `is_super` int(2) DEFAULT '0' COMMENT '是否是超级管理员节点（1 是 0 不是）',
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织表（有上下级关系）';

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES ('0937999fbf8a4dd093af0b6d347f1152', '财务部', '3', '', '9ffc66b087a145348e52028a0d64bf3b', '0', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('0b94c3bb856b4e2c8c8d7a7d3e1d4dec', '采购部', '3', '', '5d5b7e9476314dddb74db464380fe21e', '0', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('3e542424f7ee45d988aab05152400328', '采购部', '3', '', '9ffc66b087a145348e52028a0d64bf3b', '0', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('5d5b7e9476314dddb74db464380fe21e', '玉兔广州分公司', '2', '', '9ffc66b087a145348e52028a0d64bf3b', '5', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('62c05cad7d954a6192f791e3bdecb78c', '软件研发部', '3', '', '5d5b7e9476314dddb74db464380fe21e', '0', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('665fc1b469bc4ef6a10ea9e0e6118035', '软件研发部', '3', '', '9ffc66b087a145348e52028a0d64bf3b', '0', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('9ffc66b087a145348e52028a0d64bf3b', '玉兔公司总部', '1', '', '-1', '1', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('a02b7bc32c7d4d7cad431c941295d315', '销售部', '3', '', '5d5b7e9476314dddb74db464380fe21e', '0', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('a8023de516d0418c8f030126179b99e9', '销售部', '3', '', '9ffc66b087a145348e52028a0d64bf3b', '0', 'layui-icon layui-icon-home', '0');
INSERT INTO `organization` VALUES ('d0e1e626242f4e5fa27a7c8da0b19fc5', '超级管理员节点', '1', '', '-1', '0', 'layui-icon layui-icon-set', '1');
INSERT INTO `organization` VALUES ('eca670075bf04b69bead2b0500024aca', '财务部', '3', '', '5d5b7e9476314dddb74db464380fe21e', '0', 'layui-icon layui-icon-home', '0');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `permission_id` varchar(64) NOT NULL COMMENT '权限id',
  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(50) DEFAULT NULL COMMENT '权限编码',
  `permission_type` varchar(5) DEFAULT NULL COMMENT '权限类型（1 菜单 2按钮 3数据）',
  `parent_id` varchar(64) NOT NULL COMMENT '父权限id（如果没有父ID，则为-1）',
  `position` int(3) NOT NULL COMMENT '节点位置，越小越靠前',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` varchar(25) DEFAULT NULL COMMENT '创建日期',
  `modify_time` varchar(25) DEFAULT NULL COMMENT '修改日期',
  `url` varchar(255) DEFAULT NULL COMMENT '节点链接',
  `sys_code` varchar(20) DEFAULT NULL COMMENT '系统编码',
  `icon` varchar(50) DEFAULT NULL COMMENT '字体图标',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES ('00c0a20421a5483d8fa8030ead0acc06', '权限管理', 'Menu:PERMS_MANAGE', '1', '0cdc8d9d237042d9bdabc7ef338c5123', '4', '权限管理菜单', '2023-03-01 19:37:23', '2023-03-01 19:55:47', '/permission/permission_list', '', 'layui-icon layui-icon-auz');
INSERT INTO `permissions` VALUES ('0215d33cd06f481abd3ea6d5f5d4921c', '仪表盘', 'Menu:HOME_VIEW', '1', '-1', '0', '仪表盘', '2023-02-08 12:10:54', '2023-03-02 12:21:50', '', '', 'layui-icon layui-icon-console');
INSERT INTO `permissions` VALUES ('0cdc8d9d237042d9bdabc7ef338c5121', '角色管理', 'Menu:ROLE_MANAGE', '1', '0cdc8d9d237042d9bdabc7ef338c5123', '3', '角色管理菜单', null, '2023-03-01 19:56:05', '/role/role_list', '', 'layui-icon layui-icon-user');
INSERT INTO `permissions` VALUES ('0cdc8d9d237042d9bdabc7ef338c5123', '系统管理', 'Menu:SYS_MANAGE', '1', '-1', '2', '系统管理菜单', '2023-02-08 11:56:03', '2023-03-02 12:22:35', '', '', 'layui-icon layui-icon-set');
INSERT INTO `permissions` VALUES ('0cdc8d9d237042d9bdabc7ef338c5154', '新增角色', 'Btn:ROLE_ADD', '2', '0cdc8d9d237042d9bdabc7ef338c5121', '1', '新增角色按钮', null, '2023-03-01 19:25:16', '', '', 'layui-icon layui-icon-add-1');
INSERT INTO `permissions` VALUES ('0def24a6f1ce4244ba5c487ea9d61b59', '修改用户', 'Btn:USER_UPDATE', '2', 'caeb961545ce4c029a5517a99c09a52f', '2', '修改用户', null, '2023-03-01 20:48:45', '', '', 'layui-icon layui-icon-edit');
INSERT INTO `permissions` VALUES ('10e02c7c1045450b8ac9943d6c879ae0', '权限配置', 'Data:ROLE_PERMS_ASSIGN', '3', '0cdc8d9d237042d9bdabc7ef338c5121', '5', '角色管理-权限配置  Tab 权限数据查看与分配', '2023-03-01 20:28:36', '2023-03-01 20:31:24', '', '', 'layui-icon layui-icon-set');
INSERT INTO `permissions` VALUES ('1332e7f059c34019953a40c6f6ffbecf', '互斥角色查看', 'Data:MUTEX_ROLE_VIEW', '2', '0cdc8d9d237042d9bdabc7ef338c5121', '5', '互斥角色查看-按钮', '2023-03-01 20:36:22', '2023-03-01 20:39:35', '', '', 'layui-icon layui-icon-form');
INSERT INTO `permissions` VALUES ('17c4711fdf9a4ae8932b54dfde9a3545', '用户列表-用户详细信息', 'Btn:USER_DETAIL', '2', 'caeb961545ce4c029a5517a99c09a52f', '5', '用户列表-行内工具条-查看按钮（附带修改功能）', '2023-03-01 20:14:11', '2023-03-02 09:46:13', '', '', 'layui-icon layui-icon-form');
INSERT INTO `permissions` VALUES ('1853452ed8a14cc1940100ee772978a9', '新增权限', 'Btn:PERMS_ADD', '2', '00c0a20421a5483d8fa8030ead0acc06', '0', '新增权限（非子权限）', '2023-03-01 19:58:17', '2023-03-01 20:07:36', '', '', 'layui-icon layui-icon-add-1');
INSERT INTO `permissions` VALUES ('28a98509312a4b019097f1ede25d5ca6', '重置密码', 'Btn:USER_RESET_PASSWORD', '2', 'caeb961545ce4c029a5517a99c09a52f', '3', '重置密码为：88888888', '2023-03-31 11:23:28', '2023-03-31 11:23:58', '', '', 'layui-icon layui-icon-set');
INSERT INTO `permissions` VALUES ('31f31c7446784ca69abdda67d95b805b', '用户状态修改', 'Btn:USER_STATUS_UPD', '2', 'caeb961545ce4c029a5517a99c09a52f', '4', '用户列表-点击复选框修改用户状态', '2023-03-01 20:41:49', '2023-03-01 20:47:37', '', '', 'layui-icon layui-icon-set');
INSERT INTO `permissions` VALUES ('3a99e59a5d1041c5a6662bed88f8c7ec', '删除互斥角色', 'Btn:MUTEX_ROLE_DEL', '2', '0cdc8d9d237042d9bdabc7ef338c5121', '5', '删除互斥角色-按钮', '2023-03-01 20:39:17', null, '', '', 'layui-icon layui-icon-delete');
INSERT INTO `permissions` VALUES ('4835a1dbbba34c2082e001f3e666f67b', '删除用户', 'Btn:USER_DEL', '2', 'caeb961545ce4c029a5517a99c09a52f', '2', '', '2023-02-15 22:33:57', '2023-03-01 19:14:23', '', '', 'layui-icon layui-icon-delete');
INSERT INTO `permissions` VALUES ('637d61f187504edea954475aee25f97b', '查看 | 修改权限', 'Btn:PERMS_VIEW_UPD', '2', '00c0a20421a5483d8fa8030ead0acc06', '3', '查看权限详情，并可以修改权限信息。', '2023-03-01 20:05:33', '2023-03-01 20:07:55', '', '', 'layui-icon layui-icon-search');
INSERT INTO `permissions` VALUES ('8f38cacc82184685a338200ce14285ac', '分组用户查看', 'Data:GROUP_USER_VIEWE', '3', 'caeb961545ce4c029a5517a99c09a52f', '4', '用户管理-分组用户 Tab 内容查看', '2023-03-01 20:46:22', null, '', '', 'layui-icon layui-icon-form');
INSERT INTO `permissions` VALUES ('964bee9ea1bd4e02976904f04b7bdfe7', '个人设置', 'Menu:PERSONAL_SET', '1', '-1', '1', '基础权限- 个人信息设置与密码修改。', '2023-03-02 10:04:23', '2023-03-02 12:22:25', '/user/user_setting', '', 'layui-icon layui-icon-set');
INSERT INTO `permissions` VALUES ('9af119c5983247978647c8ba7f281731', '修改角色', 'Btn:ROLE_UPDATE', '2', '0cdc8d9d237042d9bdabc7ef338c5121', '0', '修改角色', '2023-02-26 20:54:53', '2023-03-01 19:24:12', '', '', 'layui-icon layui-icon-edit');
INSERT INTO `permissions` VALUES ('a07eec40a2104bbd9c7d8287979f05b4', '分配组织', 'Btn:USER_ASSIGN_ORG', '2', 'caeb961545ce4c029a5517a99c09a52f', '6', '用户管理-用户列表-分配组织按钮', '2023-03-01 20:18:04', '2023-03-01 20:18:34', '', '', 'layui-icon layui-icon-set');
INSERT INTO `permissions` VALUES ('a51d134580f24506a2c05c12cdd5e096', '新增用户', 'Btn:USER_ADD', '2', 'caeb961545ce4c029a5517a99c09a52f', '0', '新增用户按钮', '2023-03-01 19:13:03', '2023-03-01 19:13:39', '', '', 'layui-icon layui-icon-add-1');
INSERT INTO `permissions` VALUES ('a88e1aa09b1741678f2bef0df4098120', '删除权限', 'Btn:PERMS_DEL', '2', '00c0a20421a5483d8fa8030ead0acc06', '4', '删除权限', '2023-03-01 20:07:12', null, '', '', 'layui-icon layui-icon-delete');
INSERT INTO `permissions` VALUES ('b5fe3fa6916c434186393734f775f0cc', '新增子权限', 'Btn:PERMS_CHILD_ADD', '2', '00c0a20421a5483d8fa8030ead0acc06', '0', '新增子权限', '2023-03-01 20:00:23', '2023-03-01 20:07:46', '', '', 'layui-icon layui-icon-add-1');
INSERT INTO `permissions` VALUES ('bdd2c32ff454423f884447dc944d2896', '新增互斥角色', 'Data:MUTEX_ROLE_ADD', '2', '0cdc8d9d237042d9bdabc7ef338c5121', '5', '新增互斥角色按钮', '2023-03-01 20:37:46', null, '', '', 'layui-icon layui-icon-add-1');
INSERT INTO `permissions` VALUES ('caa14472f0944afd947bc84c00ff5ebb', '新增职位', 'Btn:USER_POST_ADD', '2', 'caeb961545ce4c029a5517a99c09a52f', '5', '用户管理-用户列表-头部【新增职位】按钮', '2023-03-01 20:21:29', null, '', '', 'layui-icon layui-icon-add-1');
INSERT INTO `permissions` VALUES ('caeb961545ce4c029a5517a99c09a52f', '用户管理', 'Menu:USER_MANAGE', '1', '0cdc8d9d237042d9bdabc7ef338c5123', '2', '用户管理菜单', '2023-02-08 12:13:01', '2023-03-01 20:48:53', '/user/user_list', '', 'layui-icon layui-icon-user');
INSERT INTO `permissions` VALUES ('e4533423d48d4792b1d504f3df5ed033', '角色分配（组织|职位）', 'Data:ROLE_ORG_POST_ASSIGN', '3', '0cdc8d9d237042d9bdabc7ef338c5121', '5', '角色管理中的Tab，将角色分配到组织和职位', '2023-03-01 20:34:13', '2023-03-01 20:34:25', '', '', 'layui-icon layui-icon-set');
INSERT INTO `permissions` VALUES ('ec961964e554421c9cf3098dd9af1af2', '删除角色', 'Btn:ROLE_DEL', '2', '0cdc8d9d237042d9bdabc7ef338c5121', '0', '删除角色按钮', '2023-02-26 21:08:48', '2023-03-01 19:23:22', '', '', 'layui-icon layui-icon-delete');

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `post_id` varchar(64) NOT NULL COMMENT '职位ID',
  `post_name` varchar(20) NOT NULL COMMENT '职位名称',
  `post_code` varchar(64) DEFAULT NULL COMMENT '职位编码',
  `parent_id` varchar(64) NOT NULL COMMENT '上级职位ID',
  `org_id` varchar(64) NOT NULL COMMENT '所属组织ID',
  `icon` varchar(50) DEFAULT NULL COMMENT '字体图标',
  `position` int(3) DEFAULT NULL COMMENT '节点位置，越小越靠前',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='职位表';

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES ('0554940219d2404c8899b56f5ae897ec', '订单管理员', '', '-1', '3e542424f7ee45d988aab05152400328', 'layui-icon layui-icon-user', '4');
INSERT INTO `post` VALUES ('188a85efef74471290669d449e5789e1', '会计', '', '-1', 'eca670075bf04b69bead2b0500024aca', 'layui-icon layui-icon-user', '3');
INSERT INTO `post` VALUES ('1f8d9dd4c9a0401e83e0dd79982890b7', 'CEO', '', '-1', '9ffc66b087a145348e52028a0d64bf3b', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('2a021458e76f4bf896a2456c57291091', '会计', '', '-1', '0937999fbf8a4dd093af0b6d347f1152', 'layui-icon layui-icon-user', '2');
INSERT INTO `post` VALUES ('2df734ef705447138f799a54134e2251', '采购部部长', '', '-1', '3e542424f7ee45d988aab05152400328', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('350ba82b46cb4c29b72929ce8476aa66', '部门经理', '', '-1', '62c05cad7d954a6192f791e3bdecb78c', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('587cf60a2ef6421c985d11e6a8a3235b', '采购工程师', '', '-1', '0b94c3bb856b4e2c8c8d7a7d3e1d4dec', 'layui-icon layui-icon-user', '3');
INSERT INTO `post` VALUES ('5b98b7fbfc9247fb871bc7a15170e61a', '财务经理', '', '-1', '0937999fbf8a4dd093af0b6d347f1152', 'layui-icon layui-icon-user', '1');
INSERT INTO `post` VALUES ('5e29deab516047659a5fc49e2f9934b7', '软件测试', '', '-1', '62c05cad7d954a6192f791e3bdecb78c', 'layui-icon layui-icon-user', '4');
INSERT INTO `post` VALUES ('62e4443d0e0d453eb3566b2cc641420b', '出纳', '', '-1', 'eca670075bf04b69bead2b0500024aca', 'layui-icon layui-icon-user', '4');
INSERT INTO `post` VALUES ('65c1018b37e54629bc05ee21a5678a7a', '采购工程师', '', '-1', '3e542424f7ee45d988aab05152400328', 'layui-icon layui-icon-user', '3');
INSERT INTO `post` VALUES ('68d0f1b23fbf4caab80a59a6fa9eac59', '软件工程师', '', '-1', '62c05cad7d954a6192f791e3bdecb78c', 'layui-icon layui-icon-user', '3');
INSERT INTO `post` VALUES ('6bd2c1239cea4b05a3506b014bbf2ecb', '架构师', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '4');
INSERT INTO `post` VALUES ('6cd4bb2c89f743e4b81c8ba68c7a9b9a', '运维', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '6');
INSERT INTO `post` VALUES ('7a9975de7b5d4e90b77d3f0648aebb4e', '软件销售推广', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '7');
INSERT INTO `post` VALUES ('80ecce66132d4c4ab819ad80cd4eb8b1', '采购部部长', '', '-1', '0b94c3bb856b4e2c8c8d7a7d3e1d4dec', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('84842e4c959b4618aee97643a477d67b', '项目总监', '', '-1', '62c05cad7d954a6192f791e3bdecb78c', 'layui-icon layui-icon-user', '2');
INSERT INTO `post` VALUES ('85b046c440804270adedb160d3c5c370', '财务审核员', '', '-1', 'eca670075bf04b69bead2b0500024aca', 'layui-icon layui-icon-user', '7');
INSERT INTO `post` VALUES ('88c0d1bf7f2349d4962214adc45e06ae', '部门副经理', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '1');
INSERT INTO `post` VALUES ('8cce953ef0be44beb95789cb7d1c340c', '项目经理', '', '-1', '62c05cad7d954a6192f791e3bdecb78c', 'layui-icon layui-icon-user', '1');
INSERT INTO `post` VALUES ('9259feda543b4bd7947554d484110c47', '资金核算人员', '', '-1', '0937999fbf8a4dd093af0b6d347f1152', 'layui-icon layui-icon-user', '4');
INSERT INTO `post` VALUES ('94c8165538b341eeb8e7cb946cbcb85f', '部门经理', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('9622ec55cf024ae5a58438ea208763c2', '财务总监', '', '-1', 'eca670075bf04b69bead2b0500024aca', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('a01b43446138406380035e9b44ce11f9', '订单管理员', '', '-1', '0b94c3bb856b4e2c8c8d7a7d3e1d4dec', 'layui-icon layui-icon-user', '4');
INSERT INTO `post` VALUES ('a863285750a44289ae4f796e42a7de9e', '项目总监', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '2');
INSERT INTO `post` VALUES ('b318934bae6e4725a8410f0b547a73cb', '采购主管', '', '-1', '0b94c3bb856b4e2c8c8d7a7d3e1d4dec', 'layui-icon layui-icon-user', '1');
INSERT INTO `post` VALUES ('b463667e989e4dc89e3c398420fdc7a1', '运维', '', '-1', '62c05cad7d954a6192f791e3bdecb78c', 'layui-icon layui-icon-user', '5');
INSERT INTO `post` VALUES ('b75429fad1e64e7687889bb21ce08bb2', '软件测试', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '5');
INSERT INTO `post` VALUES ('b8ca9478e98c4909925d3c97291e640b', '采购经理', '', '-1', '0b94c3bb856b4e2c8c8d7a7d3e1d4dec', 'layui-icon layui-icon-user', '2');
INSERT INTO `post` VALUES ('beb0fe2c46e14d258e19c4792662c4c5', '项目经理', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '3');
INSERT INTO `post` VALUES ('d5091dab641142318f4fe4bb694f4a1b', '采购主管', '', '-1', '3e542424f7ee45d988aab05152400328', 'layui-icon layui-icon-user', '1');
INSERT INTO `post` VALUES ('d75623269c744f159c9d7dce78b43776', '出纳', '', '-1', '0937999fbf8a4dd093af0b6d347f1152', 'layui-icon layui-icon-user', '3');
INSERT INTO `post` VALUES ('d7bd04120dac4027b5d5cc0ea99862f9', '财务经理', '', '-1', 'eca670075bf04b69bead2b0500024aca', 'layui-icon layui-icon-user', '1');
INSERT INTO `post` VALUES ('de8c56f7fe75469cadd4960ade356856', '软件工程师', '', '-1', '665fc1b469bc4ef6a10ea9e0e6118035', 'layui-icon layui-icon-user', '4');
INSERT INTO `post` VALUES ('e4d9a482049f410e90393885be37db1a', '财务总监', '', '-1', '0937999fbf8a4dd093af0b6d347f1152', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('ee2cecfef1de4c8fa3b26ed65400669f', '销售经理', '', '-1', 'a8023de516d0418c8f030126179b99e9', 'layui-icon layui-icon-user', '0');
INSERT INTO `post` VALUES ('ee4ac9b12a054088bb86687d50f8ddb7', '采购经理', '', '-1', '3e542424f7ee45d988aab05152400328', 'layui-icon layui-icon-user', '2');
INSERT INTO `post` VALUES ('f11db8365bff4a3bacee844073199d63', '销售经理', '', '-1', 'a02b7bc32c7d4d7cad431c941295d315', 'layui-icon layui-icon-user', '0');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `role_id` varchar(64) NOT NULL COMMENT '角色id',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_code` varchar(100) DEFAULT NULL COMMENT '角色编码',
  `parent_id` varchar(64) NOT NULL COMMENT '父角色id（如果没有父ID，则为-1）',
  `position` int(3) NOT NULL COMMENT '节点位置（超级管理员：0，其他角色最小为1）',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `create_time` varchar(25) DEFAULT NULL COMMENT '创建日期',
  `modify_time` varchar(25) DEFAULT NULL COMMENT '修改日期',
  `sys_code` varchar(20) DEFAULT NULL COMMENT '系统编码',
  `icon` varchar(50) DEFAULT NULL COMMENT '字体图标',
  `is_super` int(2) DEFAULT '0' COMMENT '是否是超级角色（1 是 0 不是 2 基础角色）',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('53c735852284434ca6e95fe8ea3a47e5', '会计', 'kuaiji', '-1', '3', '会计角色', null, null, '', 'layui-icon layui-icon-user', '0');
INSERT INTO `roles` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '超级管理员', 'role_super', '-1', '0', '这是超级管理员，默认分配所有角色和权限，禁止删除！', null, null, '', 'layui-icon layui-icon-user', '1');
INSERT INTO `roles` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '基础角色', 'role_basic', '-1', '1', '基础角色，禁止删除！该角色用于分配查看、修改个人信息，修改个人密码等基本权限，为该角色配置权限时请谨慎！', null, null, '', 'layui-icon layui-icon-user', '2');
INSERT INTO `roles` VALUES ('e06a733880da4daf8738be6846b1ca9f', '会计助理', 'kuaiji_zhuli', '53c735852284434ca6e95fe8ea3a47e5', '1', '会计助理', null, null, '', 'layui-icon layui-icon-user', '0');
INSERT INTO `roles` VALUES ('ee209a85eb494a5fa90bd5448d309736', '财务审核员', 'check_man', '-1', '4', '财务审核员角色。', null, null, '', 'layui-icon layui-icon-user', '0');
INSERT INTO `roles` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', '普通管理员', 'common_manager', '5f68648f4ee24982a629b0923cf3c1a1', '1', '普通管理员', null, null, '', 'layui-icon layui-icon-user', '0');

-- ----------------------------
-- Table structure for role_group
-- ----------------------------
DROP TABLE IF EXISTS `role_group`;
CREATE TABLE `role_group` (
  `group_id` varchar(64) NOT NULL COMMENT '用户组ID',
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `sys_code` varchar(20) NOT NULL COMMENT '系统编码',
  PRIMARY KEY (`group_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组-角色关联表';

-- ----------------------------
-- Records of role_group
-- ----------------------------

-- ----------------------------
-- Table structure for role_organization
-- ----------------------------
DROP TABLE IF EXISTS `role_organization`;
CREATE TABLE `role_organization` (
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `org_id` varchar(64) NOT NULL COMMENT '组织ID',
  `sys_code` varchar(20) DEFAULT NULL COMMENT '系统编码',
  PRIMARY KEY (`org_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织-角色关联表';

-- ----------------------------
-- Records of role_organization
-- ----------------------------
INSERT INTO `role_organization` VALUES ('53c735852284434ca6e95fe8ea3a47e5', '0937999fbf8a4dd093af0b6d347f1152', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '0937999fbf8a4dd093af0b6d347f1152', null);
INSERT INTO `role_organization` VALUES ('ee209a85eb494a5fa90bd5448d309736', '0937999fbf8a4dd093af0b6d347f1152', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '0b94c3bb856b4e2c8c8d7a7d3e1d4dec', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '3e542424f7ee45d988aab05152400328', null);
INSERT INTO `role_organization` VALUES ('53c735852284434ca6e95fe8ea3a47e5', '5d5b7e9476314dddb74db464380fe21e', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '5d5b7e9476314dddb74db464380fe21e', null);
INSERT INTO `role_organization` VALUES ('ee209a85eb494a5fa90bd5448d309736', '5d5b7e9476314dddb74db464380fe21e', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '62c05cad7d954a6192f791e3bdecb78c', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '665fc1b469bc4ef6a10ea9e0e6118035', null);
INSERT INTO `role_organization` VALUES ('53c735852284434ca6e95fe8ea3a47e5', '9ffc66b087a145348e52028a0d64bf3b', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '9ffc66b087a145348e52028a0d64bf3b', null);
INSERT INTO `role_organization` VALUES ('ee209a85eb494a5fa90bd5448d309736', '9ffc66b087a145348e52028a0d64bf3b', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', 'a02b7bc32c7d4d7cad431c941295d315', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', 'a8023de516d0418c8f030126179b99e9', null);
INSERT INTO `role_organization` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'd0e1e626242f4e5fa27a7c8da0b19fc5', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', 'd0e1e626242f4e5fa27a7c8da0b19fc5', null);
INSERT INTO `role_organization` VALUES ('53c735852284434ca6e95fe8ea3a47e5', 'eca670075bf04b69bead2b0500024aca', null);
INSERT INTO `role_organization` VALUES ('7ebf3afd23014766bba0007cb5a7250d', 'eca670075bf04b69bead2b0500024aca', null);
INSERT INTO `role_organization` VALUES ('ee209a85eb494a5fa90bd5448d309736', 'eca670075bf04b69bead2b0500024aca', null);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` varchar(64) NOT NULL COMMENT '角色id',
  `permission_id` varchar(64) NOT NULL COMMENT '权限id',
  `sys_code` varchar(30) DEFAULT NULL COMMENT '系统编码',
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('53c735852284434ca6e95fe8ea3a47e5', '0215d33cd06f481abd3ea6d5f5d4921c', null);
INSERT INTO `role_permission` VALUES ('53c735852284434ca6e95fe8ea3a47e5', '964bee9ea1bd4e02976904f04b7bdfe7', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '00c0a20421a5483d8fa8030ead0acc06', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '0215d33cd06f481abd3ea6d5f5d4921c', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '0cdc8d9d237042d9bdabc7ef338c5121', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '0cdc8d9d237042d9bdabc7ef338c5123', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '0cdc8d9d237042d9bdabc7ef338c5154', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '0def24a6f1ce4244ba5c487ea9d61b59', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '10e02c7c1045450b8ac9943d6c879ae0', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '1332e7f059c34019953a40c6f6ffbecf', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '17c4711fdf9a4ae8932b54dfde9a3545', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '1853452ed8a14cc1940100ee772978a9', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '28a98509312a4b019097f1ede25d5ca6', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '31f31c7446784ca69abdda67d95b805b', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '3a99e59a5d1041c5a6662bed88f8c7ec', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '4835a1dbbba34c2082e001f3e666f67b', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '637d61f187504edea954475aee25f97b', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '8f38cacc82184685a338200ce14285ac', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '964bee9ea1bd4e02976904f04b7bdfe7', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', '9af119c5983247978647c8ba7f281731', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'a07eec40a2104bbd9c7d8287979f05b4', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'a51d134580f24506a2c05c12cdd5e096', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'a88e1aa09b1741678f2bef0df4098120', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'b5fe3fa6916c434186393734f775f0cc', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'bdd2c32ff454423f884447dc944d2896', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'caa14472f0944afd947bc84c00ff5ebb', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'caeb961545ce4c029a5517a99c09a52f', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'e4533423d48d4792b1d504f3df5ed033', null);
INSERT INTO `role_permission` VALUES ('5f68648f4ee24982a629b0923cf3c1a1', 'ec961964e554421c9cf3098dd9af1af2', null);
INSERT INTO `role_permission` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '0215d33cd06f481abd3ea6d5f5d4921c', null);
INSERT INTO `role_permission` VALUES ('7ebf3afd23014766bba0007cb5a7250d', '964bee9ea1bd4e02976904f04b7bdfe7', null);
INSERT INTO `role_permission` VALUES ('ee209a85eb494a5fa90bd5448d309736', '0215d33cd06f481abd3ea6d5f5d4921c', null);
INSERT INTO `role_permission` VALUES ('ee209a85eb494a5fa90bd5448d309736', '964bee9ea1bd4e02976904f04b7bdfe7', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', '00c0a20421a5483d8fa8030ead0acc06', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', '0215d33cd06f481abd3ea6d5f5d4921c', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', '0cdc8d9d237042d9bdabc7ef338c5121', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', '0cdc8d9d237042d9bdabc7ef338c5123', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', '0def24a6f1ce4244ba5c487ea9d61b59', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', '964bee9ea1bd4e02976904f04b7bdfe7', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', 'a51d134580f24506a2c05c12cdd5e096', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', 'b5fe3fa6916c434186393734f775f0cc', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', 'bdd2c32ff454423f884447dc944d2896', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', 'caa14472f0944afd947bc84c00ff5ebb', null);
INSERT INTO `role_permission` VALUES ('f9f1d1e339284e9a9470ef9be83d8bfb', 'caeb961545ce4c029a5517a99c09a52f', null);

-- ----------------------------
-- Table structure for role_post
-- ----------------------------
DROP TABLE IF EXISTS `role_post`;
CREATE TABLE `role_post` (
  `post_id` varchar(64) NOT NULL COMMENT '职位ID',
  `role_id` varchar(64) NOT NULL COMMENT '角色ID',
  `sys_code` varchar(20) DEFAULT NULL COMMENT '系统编码',
  PRIMARY KEY (`post_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_post
-- ----------------------------
INSERT INTO `role_post` VALUES ('0554940219d2404c8899b56f5ae897ec', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('188a85efef74471290669d449e5789e1', '53c735852284434ca6e95fe8ea3a47e5', null);
INSERT INTO `role_post` VALUES ('188a85efef74471290669d449e5789e1', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('1f8d9dd4c9a0401e83e0dd79982890b7', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('2a021458e76f4bf896a2456c57291091', '53c735852284434ca6e95fe8ea3a47e5', null);
INSERT INTO `role_post` VALUES ('2a021458e76f4bf896a2456c57291091', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('2df734ef705447138f799a54134e2251', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('350ba82b46cb4c29b72929ce8476aa66', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('587cf60a2ef6421c985d11e6a8a3235b', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('5b98b7fbfc9247fb871bc7a15170e61a', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('5e29deab516047659a5fc49e2f9934b7', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('62e4443d0e0d453eb3566b2cc641420b', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('65c1018b37e54629bc05ee21a5678a7a', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('68d0f1b23fbf4caab80a59a6fa9eac59', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('6bd2c1239cea4b05a3506b014bbf2ecb', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('6cd4bb2c89f743e4b81c8ba68c7a9b9a', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('7a9975de7b5d4e90b77d3f0648aebb4e', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('80ecce66132d4c4ab819ad80cd4eb8b1', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('84842e4c959b4618aee97643a477d67b', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('85b046c440804270adedb160d3c5c370', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('88c0d1bf7f2349d4962214adc45e06ae', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('8cce953ef0be44beb95789cb7d1c340c', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('9259feda543b4bd7947554d484110c47', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('9259feda543b4bd7947554d484110c47', 'ee209a85eb494a5fa90bd5448d309736', null);
INSERT INTO `role_post` VALUES ('94c8165538b341eeb8e7cb946cbcb85f', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('9622ec55cf024ae5a58438ea208763c2', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('a01b43446138406380035e9b44ce11f9', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('a863285750a44289ae4f796e42a7de9e', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('b318934bae6e4725a8410f0b547a73cb', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('b463667e989e4dc89e3c398420fdc7a1', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('b75429fad1e64e7687889bb21ce08bb2', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('b8ca9478e98c4909925d3c97291e640b', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('beb0fe2c46e14d258e19c4792662c4c5', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('d5091dab641142318f4fe4bb694f4a1b', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('d75623269c744f159c9d7dce78b43776', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('d7bd04120dac4027b5d5cc0ea99862f9', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('de8c56f7fe75469cadd4960ade356856', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('e4d9a482049f410e90393885be37db1a', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('ee2cecfef1de4c8fa3b26ed65400669f', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('ee4ac9b12a054088bb86687d50f8ddb7', '7ebf3afd23014766bba0007cb5a7250d', null);
INSERT INTO `role_post` VALUES ('f11db8365bff4a3bacee844073199d63', '7ebf3afd23014766bba0007cb5a7250d', null);

-- ----------------------------
-- Table structure for system
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system` (
  `sys_id` varchar(64) NOT NULL COMMENT '系统ID',
  `sys_name` varchar(20) NOT NULL COMMENT '系统名称',
  `sys_code` varchar(20) NOT NULL COMMENT '系统编码',
  PRIMARY KEY (`sys_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统基本信息';

-- ----------------------------
-- Records of system
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '用户密码',
  `user_alias` varchar(50) DEFAULT NULL COMMENT '用户别名',
  `is_enable` int(1) NOT NULL DEFAULT '1' COMMENT '账号是否可用（1：可用  0：禁用）',
  `sex` int(1) NOT NULL DEFAULT '1' COMMENT '性别（0：女 1：男）',
  `cell_phone_number` varchar(15) DEFAULT NULL COMMENT '手机号',
  `address` varchar(100) DEFAULT NULL COMMENT '住址',
  `entry_date` varchar(25) DEFAULT NULL COMMENT '登录日期',
  `last_login` varchar(25) DEFAULT NULL COMMENT '上次登录日期',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮件',
  `is_super` int(1) DEFAULT '0' COMMENT '是否超级管理员（0：否  1：是）',
  `create_time` varchar(25) DEFAULT NULL COMMENT '创建日期',
  `modify_time` varchar(25) DEFAULT NULL COMMENT '修改日期',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('00dc3d392604422a91850d6c4f9071ec', '司马光', 'd93ae65992caf6a8751e334d0a716ad8', '司马光', '1', '1', '15678905678', '广东省广州天河区棠下街18号', null, null, 'simaguang@qq.com', '0', '2023-03-04 23:02:46', '2023-03-31 11:29:55', '总部采购部经理。');
INSERT INTO `users` VALUES ('088807739b4940b8865fc8bd3b103b2c', '小王', 'd93ae65992caf6a8751e334d0a716ad8', '王老吉', '1', '1', '15678905678', '广东省广州天河区车陂路55号', '2023-03-31 14:19:07', '2023-03-31 14:18:37', 'xiaowang@qq.com', '0', '2023-03-03 23:18:21', '2023-03-04 22:39:25', '我是公司总部会计。');
INSERT INTO `users` VALUES ('0d7990fe32794a678e56b7c1a20e054e', '成吉思汗', 'd93ae65992caf6a8751e334d0a716ad8', '可汗', '1', '1', '18976864732', '广东省广州天河区棠下街88号', null, null, 'kehan@qq.com', '0', '2023-03-04 22:47:34', null, '总部财务总监');
INSERT INTO `users` VALUES ('1246b5d25ccf436dac2a54678c8dd154', '李白', 'd93ae65992caf6a8751e334d0a716ad8', '李诗人', '1', '1', '15678905678', '广东省广州天河区车陂路52号', null, null, 'libai@qq.com', '0', '2023-03-04 23:06:18', null, '总部采购部订单管理员。');
INSERT INTO `users` VALUES ('1e14f9d41d454b4b9234256f6a70f34c', 'admin', 'd93ae65992caf6a8751e334d0a716ad8', '超级管理员', '1', '1', '18976864700', '广东省广州天河区棠下街18号', '2023-03-31 14:18:16', '2023-03-31 13:38:33', 'admin@qq.com', '1', '2023-01-03 21:16:54', '2023-03-31 12:43:28', '我是超级管理员，拥有系统的所有角色与权限！对我温柔点，否则删库跑路，后果自负');
INSERT INTO `users` VALUES ('32a1566a2c8f4b389715589b3432f228', '王昭君', 'd93ae65992caf6a8751e334d0a716ad8', '王昭君', '1', '0', '15678904567', '广东省广州天河区车陂路52号', null, null, 'wzj@qq.com', '0', '2023-03-04 23:25:38', null, '广州分公司财务部会计。');
INSERT INTO `users` VALUES ('489449077b3146f2b1653cc0df7b7e14', '陶行知', 'd93ae65992caf6a8751e334d0a716ad8', '陶行知', '1', '1', '15678905678', '广东省广州天河区车陂路55号', null, null, 'txz@qq.com', '0', '2023-03-04 23:26:47', null, '广州分公司财务部财务审核员。');
INSERT INTO `users` VALUES ('59f4a243d3014c31b69a8e94292e4174', '屈原', 'd93ae65992caf6a8751e334d0a716ad8', '屈原', '1', '1', '15678904567', '广东省广州天河区棠下街98号', '', '', 'qinshihuang@qq.com', '0', '2023-03-04 23:04:36', '2023-03-04 23:30:12', '总部采购部工程师。');
INSERT INTO `users` VALUES ('5d2abb9114ce4c5c8a8f09bfaf557659', '李时珍', 'd93ae65992caf6a8751e334d0a716ad8', '药神', '1', '1', '15678905678', '广东省广州天河区棠下街98号', null, null, 'lsz@qq.com', '0', '2023-03-04 23:23:42', null, '广州分公司研发部经理。');
INSERT INTO `users` VALUES ('63273a38df6f4889aef2a45632daac99', '诸葛亮', 'd93ae65992caf6a8751e334d0a716ad8', '亮亮', '1', '1', '18976864700', '广东省广州天河区车陂路52号', null, null, '', '0', '2023-03-04 22:59:25', null, '总部采购部主管。');
INSERT INTO `users` VALUES ('65583ec3cf374c64bd20d21fb0dd6c16', '项羽', 'd93ae65992caf6a8751e334d0a716ad8', '项羽', '1', '1', '15689876543', '广东省广州天河区棠下街98号', null, null, 'xiangyu@qq.com', '0', '2023-03-04 22:57:54', null, '总部采购部部长。');
INSERT INTO `users` VALUES ('68e6094ae89642aba71c5cd7c700e8fc', '庄子', 'd93ae65992caf6a8751e334d0a716ad8', '老铁', '1', '1', '15678905678', '广东省广州天河区棠下街98号', null, null, 'zhuangzi@qq.com', '0', '2023-03-04 23:13:53', null, '总部软件研发部副经理。');
INSERT INTO `users` VALUES ('6e276ac8bf434aec94777ee1d115a077', '武则天', 'd93ae65992caf6a8751e334d0a716ad8', '武则天', '1', '0', '15678905678', '广东省广州天河区棠下街98号', null, null, 'zetian@qq.com', '0', '2023-03-04 23:22:38', null, '采购部长');
INSERT INTO `users` VALUES ('70216fc90a2c43759bee3fd15e3923b4', '小李', 'd93ae65992caf6a8751e334d0a716ad8', '小李子', '1', '1', '18976864732', '广东省广州天河区棠下街98号', '2023-03-04 23:34:45', null, 'xiaolizi@qq.com', '0', '2023-03-04 22:53:19', null, '总部财务部资金核算人员');
INSERT INTO `users` VALUES ('8cefb913a08c4367b357273fcaaef7e1', '孔子', 'd93ae65992caf6a8751e334d0a716ad8', '老铁', '1', '1', '15678905678', '广东省广州天河区棠下街98号', null, null, 'kongzi@qq.com', '0', '2023-03-04 23:10:51', null, '总部软件研发部经理。');
INSERT INTO `users` VALUES ('b39ffea21ac14919864b8a29227a52e2', '王羲之', 'd93ae65992caf6a8751e334d0a716ad8', '老铁', '1', '1', '15678905678', '广东省广州天河区车陂路55号', null, null, 'wangxizi@qq.com', '0', '2023-03-04 23:16:09', null, '总部软件研发部项目总监。');
INSERT INTO `users` VALUES ('c7c1624ff4f545639e0020c709c41851', '张爱玲', 'd93ae65992caf6a8751e334d0a716ad8', '小玲', '1', '0', '18976864700', '广东省广州天河区棠下街18号', null, null, 'zhangailing@qq.com', '0', '2023-03-04 22:51:30', null, '总部财务部出纳。');
INSERT INTO `users` VALUES ('e085bc475e3949f49ff612d0cbe2fdc6', '刘禅', 'd93ae65992caf6a8751e334d0a716ad8', '刘禅', '1', '1', '15678905678', '广东省广州天河区棠下街98号', null, null, 'lc@qq.com', '0', '2023-03-04 23:28:16', null, '广州分公司销售部经理。');
INSERT INTO `users` VALUES ('f265ac44b6a9477d8e44bdf85c8fb83b', '陶渊明', 'd93ae65992caf6a8751e334d0a716ad8', '陶雨那么', '1', '1', '18976864732', '广东省广州天河区棠下街98号', null, null, '', '0', '2023-03-04 23:20:21', null, '总部销售经理');
INSERT INTO `users` VALUES ('f5f5155a6a264971ba446d4e410b6c54', '曹操', 'd93ae65992caf6a8751e334d0a716ad8', '曹曹', '1', '1', '18976864700', '广东省广州天河区车陂路55号', null, null, 'cc@qq.com', '0', '2023-03-04 22:48:39', null, '总部财务经理');
INSERT INTO `users` VALUES ('fd1fff9cc3b6438cb234c5136761a72b', '秦始皇', 'd93ae65992caf6a8751e334d0a716ad8', '秦始皇', '1', '1', '18976864732', '广东省广州天河区棠下街98号', null, null, 'qinshihuang@qq.com', '0', '2023-03-04 23:30:57', null, 'CEO');

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `group_id` varchar(64) NOT NULL COMMENT '组织ID',
  PRIMARY KEY (`user_id`,`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-用户组关联表';

-- ----------------------------
-- Records of user_group
-- ----------------------------

-- ----------------------------
-- Table structure for user_organization
-- ----------------------------
DROP TABLE IF EXISTS `user_organization`;
CREATE TABLE `user_organization` (
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `org_id` varchar(64) NOT NULL COMMENT '组织ID',
  `org_type` varchar(20) DEFAULT NULL COMMENT '组织类型',
  PRIMARY KEY (`user_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-组织关联表';

-- ----------------------------
-- Records of user_organization
-- ----------------------------
INSERT INTO `user_organization` VALUES ('1e14f9d41d454b4b9234256f6a70f34c', 'd0e1e626242f4e5fa27a7c8da0b19fc5', '');

-- ----------------------------
-- Table structure for user_post
-- ----------------------------
DROP TABLE IF EXISTS `user_post`;
CREATE TABLE `user_post` (
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `post_id` varchar(64) NOT NULL COMMENT '职位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户-职位关联表';

-- ----------------------------
-- Records of user_post
-- ----------------------------
INSERT INTO `user_post` VALUES ('00dc3d392604422a91850d6c4f9071ec', 'ee4ac9b12a054088bb86687d50f8ddb7');
INSERT INTO `user_post` VALUES ('088807739b4940b8865fc8bd3b103b2c', '2a021458e76f4bf896a2456c57291091');
INSERT INTO `user_post` VALUES ('0d7990fe32794a678e56b7c1a20e054e', 'e4d9a482049f410e90393885be37db1a');
INSERT INTO `user_post` VALUES ('1246b5d25ccf436dac2a54678c8dd154', '0554940219d2404c8899b56f5ae897ec');
INSERT INTO `user_post` VALUES ('32a1566a2c8f4b389715589b3432f228', '188a85efef74471290669d449e5789e1');
INSERT INTO `user_post` VALUES ('489449077b3146f2b1653cc0df7b7e14', '85b046c440804270adedb160d3c5c370');
INSERT INTO `user_post` VALUES ('59f4a243d3014c31b69a8e94292e4174', '65c1018b37e54629bc05ee21a5678a7a');
INSERT INTO `user_post` VALUES ('5d2abb9114ce4c5c8a8f09bfaf557659', '350ba82b46cb4c29b72929ce8476aa66');
INSERT INTO `user_post` VALUES ('63273a38df6f4889aef2a45632daac99', 'd5091dab641142318f4fe4bb694f4a1b');
INSERT INTO `user_post` VALUES ('65583ec3cf374c64bd20d21fb0dd6c16', '2df734ef705447138f799a54134e2251');
INSERT INTO `user_post` VALUES ('68e6094ae89642aba71c5cd7c700e8fc', '88c0d1bf7f2349d4962214adc45e06ae');
INSERT INTO `user_post` VALUES ('6e276ac8bf434aec94777ee1d115a077', '80ecce66132d4c4ab819ad80cd4eb8b1');
INSERT INTO `user_post` VALUES ('70216fc90a2c43759bee3fd15e3923b4', '9259feda543b4bd7947554d484110c47');
INSERT INTO `user_post` VALUES ('8cefb913a08c4367b357273fcaaef7e1', '94c8165538b341eeb8e7cb946cbcb85f');
INSERT INTO `user_post` VALUES ('b39ffea21ac14919864b8a29227a52e2', 'a863285750a44289ae4f796e42a7de9e');
INSERT INTO `user_post` VALUES ('c7c1624ff4f545639e0020c709c41851', 'd75623269c744f159c9d7dce78b43776');
INSERT INTO `user_post` VALUES ('e085bc475e3949f49ff612d0cbe2fdc6', 'f11db8365bff4a3bacee844073199d63');
INSERT INTO `user_post` VALUES ('f265ac44b6a9477d8e44bdf85c8fb83b', 'ee2cecfef1de4c8fa3b26ed65400669f');
INSERT INTO `user_post` VALUES ('f5f5155a6a264971ba446d4e410b6c54', '5b98b7fbfc9247fb871bc7a15170e61a');
INSERT INTO `user_post` VALUES ('fd1fff9cc3b6438cb234c5136761a72b', '1f8d9dd4c9a0401e83e0dd79982890b7');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `role_id` varchar(64) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
