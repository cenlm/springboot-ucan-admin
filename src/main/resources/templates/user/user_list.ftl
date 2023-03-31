<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>用户管理</title>
<link rel="stylesheet" href="../js/layui/css/layui.css">
<link rel="stylesheet" href="../js/layui_ext/dtree/dtree.css">
<link rel="stylesheet" href="../js/layui_ext/dtree/font/dtreefont.css">
<link rel="stylesheet" href="../js/layui_ext/iconSelected/css/theme.css">
<link rel="stylesheet" href="../js/layui_ext/numberInput/css/theme.css">
<link rel="stylesheet" href="../css/dtree-skin.css">
<link rel="stylesheet" href="../css/authz-layui.css">
</head>
<body>
    <#assign contextPath="${request.contextPath}"/> 
	<div class="layui-fluid">
		<div class="layui-row" style="background-color: #F7F8FB;">
			<div class="layui-col-sm2 layui-col-md2 layui-col-lg2"
				style="font-size: 13px; z-index: 1000;">
				<div class="layui-row">
					<div
						class="layui-col-sm12 layui-col-md12 layui-col-lg12 layui-font-18"
						style="padding: 10px 10px;text-align:center;">组织架构</div>
					<input type="hidden" id="currentOrgId" name="currentOrgId" />
					<hr>
						<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"
						style="margin: 10px 60px;">
						<a href="javascript:;" id="addRootOrg" style="font-size: 14px;"><i
							class="layui-icon layui-icon-add-circle"
							style="font-size: 14px; color: #252525; margin-right: 8px;"></i><cite>新建组织</cite>
					</div>
					<hr>
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
						<ul id="org_tree" title="更多操作，请点击右键"></ul>
					</div>
					<hr>
					<div
						class="layui-col-sm12 layui-col-md12 layui-col-lg12 layui-font-18"
						style="padding: 10px 10px;"></div>

					<input type="hidden" id="currentGroupId" name="currentGroupId" />
				</div>
			</div>
			<div class="layui-col-sm2 layui-col-md2 layui-col-lg2"
				style="font-size: 13px; z-index: 999;">
				<div class="layui-row">
					<div
						class="layui-col-sm12 layui-col-md12 layui-col-lg12 layui-font-18"
						style="border-left: 1px solid #D5D5D5; padding: 10px 10px;text-align:center;">职位</div>
					<input type="hidden" id="currentPostId" name="currentPostId" />
					<hr>
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
						<ul id="post_tree" title="更多操作，请点击右键"></ul>
					</div>
				</div>
			</div>
			<div class="layui-col-sm8 layui-col-md8 layui-col-lg8">
				<div class="layui-row"
					style="border-left: 1px solid #D5D5D5; padding: 10px 10px;">
					<div
						class="layui-col-sm12 layui-col-md12 layui-col-lg12 layui-font-20">
						<span id="currentOrg"></span> <span id="currentPost"></span>
					</div>
					<div class="layui-col-sm9 layui-col-md9 layui-col-lg9"
						style="padding: 10px 10px;">
						<form class="layui-form layui-form-pane" id="searchUserForm"
							lay-filter="searchUserForm" style="margin: 10px -10px;">

							<div class="layui-form-item">
								<div class="layui-inline">
									<div class="layui-input-inline">
										<input type="text" name="username" placeholder="用户名"
											autocomplete="on" class="layui-input">
									</div>
								</div>
								<div class="layui-inline">
									<div class="layui-input-inline" style="width: 80px">
										<select name="isEnable">
											<option value="">全部</option>
											<option value="1">可用</option>
											<option value="0">禁用</option>
										</select>
									</div>
									<div class="layui-inline">
										<div class="layui-input-inline" style="width: 100px">
											<button type="button" id="searchUser"
												class="layui-btn layui-btn-radius layui-btn-normal"
												style="margin-left: 10px;">
												<i class="layui-icon layui-icon-search"
													style="font-size: 14px; color: #ffffff;"></i><cite>查询</cite>
											</button>
										</div>
									</div>
								</div>

							</div>
						</form>
					</div>
					<div class="layui-col-sm3 layui-col-md3 layui-col-lg3"
						style="padding: 10px 10px;"></div>

					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
						<div class="layui-tab layui-tab-brief"
							lay-filter="docDemoTabBrief">
							<ul class="layui-tab-title">
								<li class="layui-this">用户列表</li>
								<li>分组用户</li>
							</ul>
							<div class="layui-tab-content">
								<div class="layui-tab-item layui-show">
									<table id="user_table" lay-filter="user_table"></table>
								</div>
								<div class="layui-tab-item">
								<table id="group_users"></table>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>


		</div>
	</div>
	<!-- 头工具条模板 -->
	<script type="text/html" id="toolbarDemo">
			<div class="layui-btn-group">
                <@shiro.hasPermission name="Btn:USER_POST_ADD"><button class="layui-btn layui-btn-sm" lay-event="addPost">新增职位</button></@shiro.hasPermission>
				<@shiro.hasPermission name="Btn:USER_ADD"><button class="layui-btn layui-btn-sm" lay-event="addUser">新增用户</button></@shiro.hasPermission>
				<@shiro.hasPermission name="Btn:USER_DEL"><button class="layui-btn layui-btn-sm" lay-event="delSelectedUsers">删除选中用户</button></@shiro.hasPermission>
			</div>
		</script>
	<!-- 用户状态切换checkbox 模板 -->
	<script type="text/html" id="switchTpl">
			<@shiro.hasPermission name="Btn:USER_STATUS_UPD"><input type="checkbox" name="isEnable" value="{{d.isEnable}}" id="{{d.userId}}" lay-skin="switch" lay-text="可用|禁用"
				lay-filter="userStatus" {{ d.isEnable == 1 ? 'checked' : '' }} {{ d.isSuper == 1 ? 'disabled' : '' }}></@shiro.hasPermission>
		</script>

	<!-- 行内表格操作按钮 模板-->
	<script type="text/html" id="barDemo">
 		<div class="layui-btn-group">
			<@shiro.hasPermission name="Btn:USER_DETAIL"><a class="layui-btn  layui-btn-xs"  lay-event="detail">查看</a></@shiro.hasPermission>
			<@shiro.hasPermission name="Btn:USER_DEL"><a class="layui-btn  layui-btn-xs"  lay-event="edit">分配组织</a></@shiro.hasPermission>
			<@shiro.hasPermission name="Btn:USER_RESET_PASSWORD"><a class="layui-btn  layui-btn-xs"  lay-event="reset_password" title="重置密码为：88888888">重置密码</a></@shiro.hasPermission>
			<@shiro.hasPermission name="Btn:USER_STATUS_UPD"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del" {{ d.isSuper == 1 ? 'style=display:none;' : '' }}>删除</a></@shiro.hasPermission>
		</div>

			<!-- 这里同样支持 laytpl 语法，如：
  {{#  if(d.userStatus ==1){ }}
    <a class="layui-btn layui-btn-xs" lay-event="check">审核</a>
  {{#  } }} -->
		</script>
	<script type="text/javascript">
			var context_path = "${contextPath}";
	</script>
	<script src="../js/layui/layui.js"></script>
	<script src="../js/jquery-3.6.3.min.js"></script>
	<script src="../js/jquery-migrate-1.2.1.min.js"></script>
	<script src="../js/user_list.js"></script>

	<!-- 隐藏的表单-查看用户详情、修改用户信息 -->
	<form class="layui-form layui-form-pane" id="userDetail"
		lay-filter="userDetail" style="display: none; margin: 10px 20px;">
		<input type="text" name="userId" style="display: none;">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">用户名</label>
				<div class="layui-input-inline">
					<input type="text" name="userName" lay-verify="required"
						placeholder="请输入用户名" autocomplete="on" class="layui-input"
						style="width: 350px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">Email</label>
				<div class="layui-input-inline">
					<input type="text" name="email" lay-verify="email"
						placeholder="请输入Email" autocomplete="on" class="layui-input"
						style="width: 313px">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">手机号</label>
				<div class="layui-input-inline">
					<input type="text" name="cellPhoneNumber"
						lay-verify="phone | number" placeholder="请输入手机号" autocomplete="on"
						class="layui-input" style="width: 350px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">现居地</label>
				<div class="layui-input-inline">
					<input type="text" name="address" placeholder="请输入地址"
						autocomplete="on" class="layui-input" style="width: 313px">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">创建日期</label>
				<div class="layui-input-inline">
					<input type="text" name="createTime" lay-verify="required" disabled
						autocomplete="on" placeholder="系统自动生成" class="layui-input layui-disabled"
						style="width: 310px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">上次修改</label>
				<div class="layui-input-inline">
					<input type="text" name="modifyTime" placeholder="暂无修改" disabled
						autocomplete="on" class="layui-input layui-disabled" style="width: 313px">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">最近登录</label>
				<div class="layui-input-inline">
					<input type="text" name="entryDate" autocomplete="on"
						class="layui-input layui-disabled" style="width: 350px" placeholder="暂无纪录"
						disabled>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">更早登录</label>
				<div class="layui-input-inline">
					<input type="text" name="lastLogin" autocomplete="on"
						class="layui-input layui-disabled" style="width: 313px" placeholder="暂无纪录"
						disabled>
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">用户别名</label>
				<div class="layui-input-inline">
					<input type="text" name="userAlias" autocomplete="on"
						class="layui-input" style="width: 313px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">性别</label>
				<div class="layui-input-inline">
					<input type="radio" name="sex" value=1 title="男"> <input
						type="radio" name="sex" value=0 title="女">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">用户状态</label>
				<div class="layui-input-inline">
					<input type="checkbox" name="isEnable" lay-skin="switch"
						lay-text="可用|禁用" checked>
				</div>
			</div>
		
		</div>

		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<textarea name="remarks" placeholder="请输入内容" class="layui-textarea"
					style="width: 743px;"></textarea>
			</div>
		</div>
	</form>


	<!-- 隐藏的表单-新增用户 -->
	<form class="layui-form layui-form-pane" id="addUser"
		lay-filter="addUser" style="display: none; margin: 10px 20px;">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">用户名</label>
				<div class="layui-input-inline">
					<input type="text" name="userName" lay-verify="required"
						placeholder="请输入用户名" autocomplete="on" class="layui-input"
						style="width: 350px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">密码</label>
				<div class="layui-input-inline">
					<input type="password" name="password"
						placeholder="若不填写，则默认为 123456" autocomplete="on"
						class="layui-input" style="width: 313px">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">手机号</label>
				<div class="layui-input-inline">
					<input type="text" name="cellPhoneNumber"
						lay-verify="phone | number" placeholder="请输入手机号" autocomplete="on"
						class="layui-input" style="width: 350px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">现居地</label>
				<div class="layui-input-inline">
					<input type="text" name="address" placeholder="请输入地址"
						autocomplete="on" class="layui-input" style="width: 313px">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">用户别名</label>
				<div class="layui-input-inline">
					<input type="text" name="userAlias" autocomplete="on"
						class="layui-input" style="width: 313px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">性别</label>
				<div class="layui-input-inline">
					<input type="radio" name="sex" value=1 title="男" checked> <input
						type="radio" name="sex" value=0 title="女">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">Email</label>
				<div class="layui-input-inline">
					<input type="text" name="email" lay-verify="email"
						placeholder="请输入Email" autocomplete="on" class="layui-input"
						style="width: 310px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">用户状态</label>
				<div class="layui-input-inline">
					<input type="checkbox" name="isEnable" lay-skin="switch"
						lay-text="可用|禁用" checked>
				</div>
			</div>
		</div>

		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<textarea lay-verify="required" name="remarks" placeholder="请输入内容"
					class="layui-textarea" style="width: 743px;"></textarea>
			</div>
		</div>

	</form>

	<!-- 隐藏的表单-新增职位 -->
	<form class="layui-form layui-form-pane" id="addPost"
		lay-filter="addPost" style="display: none; margin: 10px 20px;">
		
			<div class="layui-form-item">
			<label class="layui-form-label">职位名称</label>
			<div class="layui-input-block">
				<input type="text" name="postName" autocomplete="on"
					class="layui-input" style="width: 350px" placeholder="请输入职位名称">
			</div>
		</div>
			<div class="layui-form-item">
			<label class="layui-form-label">职位图标</label>
			<div class="layui-input-block">
				<input type="text" name="icon"  value="layui-icon layui-icon-user" placeholder="请选择" maxlength="16" autocomplete="off" class="layui-input layui-select-icon"/>
			</div>
		</div>
			<div class="layui-form-item">
			<label class="layui-form-label">上级节点</label>
			<div class="layui-input-block">
				<input type="hidden" name="parentId" class="layui-input" disabled>
				<input type="text" name="parentPostName"
					class="layui-input layui-disabled" style="width: 350px" disabled>
			</div>
		</div>
		<div class="layui-form-item">

			<label class="layui-form-label">所属组织</label>
			<div class="layui-input-block">
				<input type="hidden" name="orgId" class="layui-input" disabled>
				<input type="text" name="orgName" class="layui-input layui-disabled"
					style="width: 350px" disabled>
			</div>
		</div>
	

	
		<div class="layui-form-item">
			<label class="layui-form-label">职位编码</label>
			<div class="layui-input-block">
				<input type="text" name="postCode" placeholder="请输入职位编码"
					autocomplete="on" class="layui-input" style="width: 350px">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">节点位置</label>
			<div class="layui-input-block" style="width: 350px">
				<input type="text" name="position" placeholder="节点位置"
						autocomplete="off" class="layui-input" style="width: 313px"
						 data-max="500" data-min="0" data-step="5">
			</div>
		</div>
	</form>

	<!-- 隐藏的表单-修改职位信息 -->
	<form class="layui-form layui-form-pane" id="updatePost"
		lay-filter="updatePost" style="display: none; margin: 10px 20px;">
		<div class="layui-form-item">
			<input type="hidden" name="postId" /> <label
				class="layui-form-label">职位名称</label>
			<div class="layui-input-block">
				<input type="text" name="postName" autocomplete="on"
					class="layui-input" style="width: 350px" placeholder="请输入职位名称">
			</div>
		</div>
			<div class="layui-form-item">
			<label class="layui-form-label">职位图标</label>
			<div class="layui-input-block">
				<input type="text" name="icon"  value="layui-icon layui-icon-user" placeholder="请选择" maxlength="16" autocomplete="off" class="layui-input layui-select-icon" />
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">职位编码</label>
			<div class="layui-input-block">
				<input type="text" name="postCode" placeholder="请输入职位编码"
					autocomplete="on" class="layui-input" style="width: 350px">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">上级节点</label>
			<div class="layui-input-block">
				<input type="text" name="parentPostName"
					class="layui-input layui-disabled" style="width: 350px" disabled>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">所属组织</label>
			<div class="layui-input-block">
				<input type="text" name="orgName" class="layui-input layui-disabled"
					style="width: 350px" disabled>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">节点位置</label>
			<div class="layui-input-block" style="width: 350px">
				<input type="text" name="position" placeholder="节点位置"
						autocomplete="off" class="layui-input" style="width: 313px"
						 data-max="500" data-min="0" data-step="5">
			</div>
		</div>
	</form>


	<!-- 隐藏的表单-新增组织结构 -->
	<form class="layui-form layui-form-pane" id="addOrganization"
		lay-filter="addOrganization" style="display: none; margin: 10px 20px;">
		<div class="layui-form-item" action=""
							onsubmit="return false">

			<label class="layui-form-label">组织名称</label>
			<div class="layui-input-block">
				<input type="text" name="orgName" class="layui-input"
					style="width: 350px" required
										lay-verify="required">
			</div>
		</div>
			<div class="layui-form-item">
			<label class="layui-form-label">组织图标</label>
			<div class="layui-input-block">
				<input type="text" name="icon"  value="layui-icon layui-icon-home" placeholder="请选择" maxlength="16" autocomplete="off" class="layui-input layui-select-icon" />
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">上级节点</label>
			<div class="layui-input-block">
				<input type="hidden" name="parentId" class="layui-input" disabled>
				<input type="text" name="parentOrgName"
					class="layui-input layui-disabled" style="width: 350px" disabled>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">组织类型</label>
			<div class="layui-input-block" style="width: 350px">
				<select name="orgType">
					<option value="1">总部</option>
					<option value="2">分公司</option>
					<option value="3" selected="selected">部门</option>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">组织编码</label>
			<div class="layui-input-block">
				<input type="text" name="orgCode" placeholder="请输入组织编码"
					autocomplete="on" class="layui-input" style="width: 350px">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">节点位置</label>
			<div class="layui-input-block" style="width: 350px">
				<input type="text" name="position" placeholder="节点位置"
						autocomplete="off" class="layui-input" style="width: 313px"
						 data-max="500" data-min="0" data-step="5">
			</div>
		</div>
	</form>

	<!-- 隐藏的表单-修改组织信息 -->
	<form class="layui-form layui-form-pane" id="updateOrg"
		lay-filter="updateOrg" style="display: none; margin: 10px 20px;">
		<div class="layui-form-item">
			<input type="hidden" name="orgId" class="layui-input"
				style="width: 350px"> <label class="layui-form-label">组织名称</label>
			<div class="layui-input-block">
				<input type="text" name="orgName" class="layui-input"
					style="width: 350px">
			</div>
		</div>
			<div class="layui-form-item">
			<label class="layui-form-label">组织图标</label>
			<div class="layui-input-block">
				<input type="text" name="icon"  value="layui-icon layui-icon-home" placeholder="请选择" maxlength="16" autocomplete="off" class="layui-input layui-select-icon"/>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">上级节点</label>
			<div class="layui-input-block">
				<input type="text" name="parentOrgName"
					class="layui-input layui-disabled" style="width: 350px" disabled>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">组织类型</label>
			<div class="layui-input-block" style="width: 350px">
				<select name="orgType">
					<option value="1">总部</option>
					<option value="2">分公司</option>
					<option value="3" selected="selected">部门</option>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">组织编码</label>
			<div class="layui-input-block">
				<input type="text" name="orgCode" placeholder="请输入组织编码"
					autocomplete="on" class="layui-input" style="width: 350px">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">节点位置</label>
			<div class="layui-input-block" style="width: 350px">
			<input type="text" name="position" placeholder="节点位置"
						autocomplete="off" class="layui-input" style="width: 313px"
						 data-max="500" data-min="0" data-step="5">
			</div>
		</div>
		
	
	</form>

</body>
</html>
