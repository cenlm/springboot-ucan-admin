# SpringBoot-Ucan-Admin

#### 介绍
基于RBAC3的权限管理系统。<br>
该系统是ucan-admin的springboot版本。<br>
使用的技术栈:SpringBoot2.7.9 + Shiro1.11.0 + Mysql5.7 + LayUi（及LayUi第三方插件）+ FreeMarker。<br>
基本功能：<br>
1.用户登录、认证授权、Session管理、账号登录限制（一个账号只能在一处登录）。<br>
2.组织结构管理、职位管理、用户管理、组织分配。<br>
3.角色管理：<br>
	3.1 角色基本信息管理。<br>
	3.2 角色分层、角色互斥、角色权限继承、角色分配、权限分配、角色用户数限制（待办）等。<br>
4.权限管理。<br>

用户名：admin  密码：123456<br>

Github: github.com/cenlm/springboot-ucan-admin<br>

#### 数据库表关系图

1.数据库表关系图：<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/db-erd.png)<br>

表名称说明：<br>
users: 用户表<br>
organization: 组织表（有上下级关系）<br>
post: 职位表（有上下级关系）<br>
roles: 角色表（有上下级关系）<br>
permissions: 权限表（有上下级关系）<br>
user_organization: <用户-组织>关系表<br>
user_post: <用户-职位>关系表<br>
role_organization: <角色-组织>关系表<br>
role_post: <角色-职位>关系表<br>
role_permission: <角色-权限>关系表<br>
mutex_roles: 互斥角色表<br>

注：所有数据表仅在代码逻辑上做了外键约束，数据表结构未添加外键约束，可自行在数据表添加外键约束（如需要）。<br>

其他后续可能会用到的表：<br>
group: 其他用户分组（非组织结构内人员或其他特殊情况）<br>
user_group: <用户-其他分组>关系表<br>
role_group: <角色-其他分组>关系表<br>
system: 系统编码表 （用于区分不同系统的权限信息）<br>
cross_role: 跨角色<br>
cross_permission: 跨权限<br>

#### 运行环境
1.  JDK 1.8+
2.  SpringBoot2.7.9
3.  Mybatis-3.5.9
4.  Shiro1.11.0
5.  Mysql 5.7.33
6.  Layui 2.7.6
7.  Tomcat 9
8.  Freemarker

#### 待办事项
1.角色用户数限制。<br>
2.权限数据动态更新。<br>
3.shiro + jwt 无状态登录认证？<br>
4.优化其他细节及解决未知的问题。<br>
5.用户登录失败次数限制。<br>

业务逻辑参考：<br>
https://www.cnblogs.com/iceblow/p/11121362.html<br>
https://juejin.cn/post/7121977695197970463<br>

RBAC权限管理系统的具体业务跟具体需求有关，欢迎大家的指正与交流。<br>


#### 效果图

![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/login-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/home-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/user-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/role-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/permission-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/setting-effect.png)<br>
