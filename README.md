# SpringBoot-Ucan-Admin

#### 介绍

该项目是基于RBAC3的权限管理系统，是ucan-admin的springboot版本。<br>

使用的技术栈:SpringBoot2.7.9 + Shiro1.11.0 + Mysql5.7 + LayUi（及LayUi第三方插件）+ FreeMarker。<br>

**功能简介**<br>

1.用户登录、认证授权、Session管理、账号登录限制（一个账号只能在一处登录）、登录失败次数限制。<br>
2.组织结构管理、职位管理、用户管理、组织分配。<br>
3.角色管理：<br>
	3.1 角色基本信息管理。<br>
	3.2 角色分层、角色互斥、角色权限继承、角色分配、权限分配、角色用户数限制（待办）等。<br>
4.权限管理。<br>

用户名：admin  密码：123456<br>
用户名：小王   密码：123456<br>

[Gitee：ucan-admin](https://gitee.com/mrcen/ucan-admin)<br>
[Github：ucan-admin](https://github.com/cenlm/ucan-admin)<br>

#### 数据库表关系图

**1.数据库表关系图：**<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/db-erd.png)<br>

**表名称说明：**<br>

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

**注：**所有数据表仅在代码逻辑上做了外键约束，数据表结构未添加外键约束，可自行在数据表添加外键约束（如需要）。<br>

**其他后续可能会用到的表：**<br>
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

**项目安装、运行步骤**

1. 在本地准备好Java 8运行环境。
2. 在本地Mysql中新建ucan_admin数据库，运行项目中的ucan_admin.sql文件。
3. 修改application-xxx.yml文件中的用户名、密码为你本地数据库的。
4. 执行UcanAdminApplication.java的main方法。


### 功能描述

**组织架构、职位、用户、角色、权限管理功能概述**<br>

a. 组织节点之间有上下级关系，如总公司、分公司、总公司部门、分公司部门等（也可以是你认为合理的任何组织架构）；<br>
b. 你可以添加任何节点的同级节点与子节点（右击弹窗操作），其中“超级管理员节点”不可删除，主要为了维护超级管理员与组织结构之间的关系。<br>
c. 任何组织架构节点可直接新增职位，职位也可以有上下级关系。<br>
d. 新增用户时，必须先选择职位，否则不允许操作（后续如果新增了其他类型的分组再进行逻辑修改）。<br>
e. 角色、权限各自的CURD操作。<br>
f. <用户-组织>、<用户-职位>、<用户-角色>、<用户-权限>关系处理逻辑：<br>
![图片](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/user-role-perm.png)<br>
g. 最后通过shiro标签或注解进行资源访问权限控制。<br>

**1. 登录模块**<br>

1.1 通过输入正确的用户名、密码进行系统登录。<br>
1.2 每个账号同一时刻只能在一处成功登录，登录成功后会把之前登录的同一个账号踢出系统。<br>
1.3 账号登录失败次数限制：<br>
    &nbsp;&nbsp;用户连续登录失败次数小于5期间，如果有一次登录成功，那么该用户登录失败次数、限制登录时长将清零；<br>
    &nbsp;&nbsp;用户连续5次登录失败时，提示"15分钟后再进行登录操作"，并开始记录限制登录时长15分钟；<br>
    &nbsp;&nbsp;用户连续10次登录失败时，提示"45分钟后再进行登录操作"，此时限制登录时长更新为45分钟；<br>
    &nbsp;&nbsp;用户连续15次登录失败时，提示"操作太频繁，请联系管理员重置密码"，限制登录时长为45分钟；<br>
    &nbsp;&nbsp;在限制登录时段内，即使用户输入了正确的用户名、密码，依旧限制用户登录操作；过了限制登录时段，用户登录失败次数、限制登录时长自动清零，用户可以再次进行登录操作。<br>
    &nbsp;&nbsp;“记住我”功能：系统Cookie有效期默认为3天，可自行到配置文件里修改。此功能可有可无。<br>
    &nbsp;&nbsp;登录成功，系统自动完成用户授权，进行资源访问控制。

**2. 仪表盘**<br>

 &nbsp;&nbsp;board.jsp 加入echarts图表及layui表格的静态页面，未做更进一步的功能开发。<br>

**3. 用户管理**<br>

3.1 包含组织架构、职位、用户的CURD、分配组织、重置密码等功能，实现对相关信息有组织的管理。<br>
&nbsp;&nbsp;a.删除组织节点的时候，与其关联的节点的资源必须先得到释放。<br>
例如，公司节点有部门节点（关联着职位），则要先删除部门节点（删除职位），部门节点关联着职位，则必须先删除职位；如果职位还分配着用户，则要先删除用户。删除组织结构节点、职位节点时，相应的<组织-角色>、<职位-角色>、<角色-权限>的映射关系会自动解除。<br>
&nbsp;&nbsp;b. 用户信息管理：包含新增用户、查看及修改用户基本信息，为用户分配组织（主要是为了通过组织批量分配角色，达到批量分配权限的目的）。<br>
&nbsp;&nbsp;c. 新增组织、职位、用户的时候，系统自动为其分配基础角色，从而达到分配基础权限的目的。<br>
&nbsp;&nbsp;d. 用户状态为“禁用”时，用户不可登录系统。<br>
&nbsp;&nbsp;e. 个人基本信息设置与密码修改。<br>

**4. 角色管理**<br>
该模块包含角色基本信息的CURD、角色成员列表、角色权限分配、角色组织(职位)分配、角色互斥管理。<br>
&nbsp;&nbsp;a. 角色有上下级关系，上级角色继承其子孙角色的所有权限，子孙角色不可越权。例如，为会计助理分配权限的时候，只能选择会计已有的权限（通过后台数据及前端代码控制复选框是否可选）。<br>
&nbsp;&nbsp;b. 将角色分配给组织、职位，从而间接达到为用户分配权限的目的。<br>
&nbsp;&nbsp;c. 互斥角色管理：例如，会计和财务审核员不能同时分配给给某一个员工，这也意味着将角色分配给组织和职位的时候，系统会自动检查已勾选的组织、职位已分配的角色与待分配的角色之间是否存在互斥关系，如果存在，则不允许此次角色分配，需要按提示进行相应角色关系处理。<br>


**5. 权限管理**<br>
权限基本信息的CURD，删除权限时会自动解除<角色-权限>映射关系。<br>

---

**业务逻辑参考：**<br>

[https://www.cnblogs.com/iceblow/p/11121362.html](https://www.cnblogs.com/iceblow/p/11121362.html)<br>
[https://juejin.cn/post/7121977695197970463](https://juejin.cn/post/7121977695197970463)<br>

#### 待办事项

1.角色用户数限制。<br>
2.权限数据动态更新。<br>
3.shiro + jwt 无状态登录认证？<br>
4.用户管理模块 新增 组织/角色“分配角色”的功能。<br>
5.角色管理模块 删除 “角色分配至‘组织/职位’”的功能。<br>
6.后台数据校验？日志管理？<br>

RBAC权限管理系统的具体业务跟具体需求有关，欢迎大家的指正与交流。<br>


#### 效果图

![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/login-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/home-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/user-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/role-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/permission-effect.png)<br>
![Image text](https://gitee.com/mrcen/ucan-admin/raw/master/src/main/webapp/imgs/setting-effect.png)<br>
