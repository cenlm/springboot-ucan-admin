<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>角色管理</title>
<link rel="stylesheet" href="../js/layui/css/layui.css">
<link rel="stylesheet" href="../js/layui_ext/dtree/dtree.css">
<link rel="stylesheet" href="../js/layui_ext/dtree/font/dtreefont.css">
<link rel="stylesheet" href="../js/layui_ext/iconSelected/css/theme.css">
<link rel="stylesheet" href="../js/layui_ext/numberInput/css/theme.css">
<link rel="stylesheet" href="../css/dtree-skin.css">
<link rel="stylesheet" href="../css/authz-layui.css">
<script src="../js/layui/layui.js"></script>
<script src="../js/jquery-3.6.3.min.js"></script>
<script src="../js/jquery-migrate-1.2.1.min.js"></script>
<script src="../js/xss/purify-3.2.5.min.js"></script>
<script src="../js/xss/xss-scanner.js"></script>
<script src="../js/role_list.js"></script>

</head>
<body>
	<!-- 行内表格操作按钮 模板-->
	<script type="text/html" id="barDemo">
 		<div class="layui-btn-group">
			<a class="layui-btn  layui-btn-xs" style="background-color:#5AB674;" lay-event="detail">移除成员</a>
		</div>
		</script>
	<!-- 行内表格操作按钮 模板-->
	<script type="text/html" id="delMutexRelation">
 		<div class="layui-btn-group">
			<@shiro.hasPermission name="Btn:MUTEX_ROLE_DEL"><a class="layui-btn  layui-btn-xs"  lay-event="del">删除</a></@shiro.hasPermission>
		</div>
		</script>
	<script type="text/javascript">
			var context_path = "${request.contextPath}";
	</script>
	<div class="layui-fluid">

		<div class="layui-row layui-col-space10"
			style="background-color: #F7F8FB;">
			<!-- 角色树形 -->
			<div class="layui-col-sm2 layui-col-md2 layui-col-lg2"
				style="border-right: 1px solid #D5D5D5; padding-top: 15px;">
				<div class="layui-row">
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"
						style="margin: 10px 30px;">
						<a href="javascript:;" id="addRootRole" style="font-size: 14px;"><i
							class="layui-icon layui-icon-add-circle"
							style="font-size: 14px; color: #252525; margin-right: 8px;"></i><cite>新建角色</cite>
					</div>
					<hr>
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
						<div>
							<ul id="role_tree" title="更多操作，请点击右键"></ul>
						</div>
					</div>
				</div>

			</div>
			<!-- 角色成员列表 -->
			<div class="layui-col-sm10 layui-col-md10 layui-col-lg10">
				<div class="layui-row"
					style="border-left: 1px solid #D5D5D5; padding: 10px 10px;">
					<input type="hidden" id="currentRoleId" name="currentRoleId" />
					<div
						class="layui-col-sm2 layui-col-md2 layui-col-lg2 layui-font-20"
						id="currentRoleName"></div>

					<div
						class="layui-col-sm12 layui-col-md12 layui-col-lg12 layui-font-12"
						style="padding: 10px 0px;" placeholder="暂无描述信息"
						id="currentRoleRemarks"></div>
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
						<div class="layui-tab layui-tab-brief"
							lay-filter="docDemoTabBrief">
							<ul class="layui-tab-title">
								<li class="layui-this">角色成员（用户）</li>
								<li>给角色分配权限</li>
								<li>将角色分配给（组织/职位）</li>
								<li>互斥角色管理</li>

							</ul>
							<div class="layui-tab-content">
								<div class="layui-tab-item layui-show">
									<table id="user_table" lay-filter="user_table"></table>
								</div>
								<div class="layui-tab-item">
									<div class="layui-row">
										<div class="layui-col-md4 layui-col-lg4">
											<form class="layui-form" id="permissionForm"
												lay-filter="permissionForm"margin: 10px 20px;">
												<ul id="permissionTree"></ul>
											</form>
										</div>
										<div class="layui-col-md6 layui-col-lg6">
											<@shiro.hasPermission name="Data:ROLE_PERMS_ASSIGN">
												<button class="layui-btn layui-btn-radius layui-btn-normal"
													lay-submit lay-filter="updateRolePermission">提交</button>
											</@shiro.hasPermission>
										</div>
									</div>
								</div>
								<div class="layui-tab-item">
									<div class="layui-row">
										<div class="layui-col-md4 layui-col-lg4">
											<div class="layui-row">
												<div class="layui-col-md12 layui-col-lg12"
													style="margin: 10px 20px; font-size: 16px; color: #575757; font-weight: bold;">组织架构</div>
												<div class="layui-col-md12 layui-col-lg12">
													<ul id="roleToOrgTree">
													</ul>
												</div>
											</div>

										</div>
										<div class="layui-col-md4 layui-col-lg4">
											<div class="layui-row">
												<div class="layui-col-md12 layui-col-lg12"
													style="margin: 10px 20px; font-size: 16px; color: #575757; font-weight: bold;">
													<cite id="orgsPost"></cite>职位
												</div>
												<div class="layui-col-md12 layui-col-lg12">
													<ul id="roleToPostTree">
													</ul>
												</div>
											</div>

										</div>

										<div class="layui-col-md2 layui-col-lg2">
											<div class="layui-row">
												<div class="layui-col-md12 layui-col-lg12"></div>
												<div class="layui-col-md12 layui-col-lg12">
													<@shiro.hasPermission name="Data:ROLE_ORG_POST_ASSIGN">
														<button
															class="layui-btn layui-btn-radius layui-btn-normal"
															lay-submit id="updRoleOrgPostRelation">提交</button>
													</@shiro.hasPermission>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="layui-tab-item">
									<div class="layui-row">
										<div class="layui-col-md12 layui-col-lg12">
											<form class="layui-form layui-form-pane"
												id="searchMutexRolesForm" lay-filter="searchMutexRolesForm"
												style="margin: 10px -10px;">

												<div class="layui-form-item" style="margin: 5px 25px;">
													<!-- <div class="layui-inline">
														<div class="layui-input-inline">
															<input type="text" name="username" placeholder="用户名"
																autocomplete="on" class="layui-input">
														</div>
													</div> -->
													<div class="layui-inline">
														<div class="layui-input-inline" style="width: 300px">
															<div class="layui-input-inline" id="mutexRoleSelect"></div>
														</div>

														<div class="layui-inline">
															<div class="layui-input-inline" style="width: 100px">
																<button type="button" id="resetSearchCondition"
																	class="layui-btn layui-btn-radius layui-btn-normal"
																	style="margin-left: 10px;">
																	<i class="layui-icon layui-icon-refresh-3"
																		style="font-size: 12px; color: #ffffff;"></i><cite>重置</cite>
																</button>
															</div>
														</div>

														<div class="layui-inline">
															<div class="layui-input-inline"
																style="width: 100px; margin-left: -25px;">
																<button type="button" id="searchMutexRolesButton"
																	class="layui-btn layui-btn-radius layui-btn-normal"
																	style="margin-left: 10px;">
																	<i class="layui-icon layui-icon-search"
																		style="font-size: 14px; color: #ffffff;"></i><cite>查询</cite>
																</button>
															</div>
														</div>

														<div class="layui-inline">
															<div class="layui-input-inline"
																style="width: 100px; margin-left: -25px;">
																<@shiro.hasPermission name="Data:MUTEX_ROLE_ADD">
																	<button type="button" id="addMutexRolesButton"
																		class="layui-btn layui-btn-radius layui-btn-normal"
																		style="margin-left: 10px;">
																		<i class="layui-icon layui-icon-add-circle"
																			style="font-size: 14px; color: #ffffff;"></i><cite>新增</cite>
																	</button>
																</@shiro.hasPermission>
															</div>
														</div>
													</div>
												</div>
											</form>
										</div>
										<div class="layui-col-md12 layui-col-lg12">
											<table id="mutexRoleTab"></table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>


	<!-- 隐藏的表单-新增角色 -->
	<form class="layui-form layui-form-pane" id="addRole"
		lay-filter="addRole" style="display: none; margin: 10px 20px;">
		<div class="layui-form-item">

			<label class="layui-form-label">角色名称</label>
			<div class="layui-input-block">
				<input type="text" name="roleName" class="layui-input"
					style="width: 350px" placeholder="请输入角色名称">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">角色编码</label>
			<div class="layui-input-block">
				<input type="text" name="roleCode" class="layui-input"
					style="width: 350px" placeholder="请输入角色编码">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">角色图标</label>
			<div class="layui-input-block">
				<input type="text" name="icon" value="layui-icon layui-icon-user"
					placeholder="请选择" maxlength="16" autocomplete="off"
					class="layui-input layui-select-icon" />
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">上级节点</label>
			<div class="layui-input-block">
				<input type="hidden" name="parentId" class="layui-input" disabled>
				<input type="text" name="parentRoleName"
					class="layui-input layui-disabled" style="width: 350px" disabled>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">系统编码</label>
			<div class="layui-input-block">
				<input type="text" name="sysCode" autocomplete="on"
					class="layui-input" style="width: 350px" placeholder="请输入系统编码">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">节点位置</label>
			<div class="layui-input-block" style="width: 350px">
				<input type="text" name="position" placeholder="节点位置"
					autocomplete="off" class="layui-input" style="width: 313px"
					id="position" data-max="500" data-min="1" data-step="5">
			</div>
		</div>

		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<textarea name="remarks" placeholder="请输入内容" class="layui-textarea"
					style="width: 460px;"></textarea>
			</div>
		</div>
	</form>

	<!-- 隐藏的表单-修改角色 -->
	<form class="layui-form layui-form-pane" id="updateRole"
		lay-filter="updateRole" style="display: none; margin: 10px 20px;">
		<div class="layui-form-item">
			<input type="hidden" name="roleId" class="layui-input"
				style="width: 350px"> <label class="layui-form-label">角色名称</label>
			<div class="layui-input-block">
				<input type="text" name="roleName" class="layui-input"
					style="width: 350px" placeholder="请输入角色名称">
			</div>
		</div>
		
		<div class="layui-form-item">
			<label class="layui-form-label">角色编码</label>
			<div class="layui-input-block">
				<input type="text" name="roleCode" class="layui-input"
					style="width: 350px" placeholder="请输入角色编码">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">角色图标</label>
			<div class="layui-input-block">
				<input type="text" name="icon" value="layui-icon layui-icon-user"
					placeholder="请选择" maxlength="16" autocomplete="off"
					class="layui-input layui-select-icon" />
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">上级节点</label>
			<div class="layui-input-block">
				<input type="text" name="parentRoleName"
					class="layui-input layui-disabled" style="width: 350px" disabled>
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label">系统编码</label>
			<div class="layui-input-block">
				<input type="text" name="sysCode" autocomplete="on"
					class="layui-input" style="width: 350px" placeholder="请输入系统编码">
			</div>
		</div>
		<div class="layui-form-item">
			<input type="hidden" name="isSuper" /> <label
				class="layui-form-label">节点位置</label>
			<div class="layui-input-block" style="width: 350px">
				<input type="text" name="position" placeholder="节点位置"
					autocomplete="off" class="layui-input" style="width: 313px"
					id="position" data-max="500" data-min="1" data-step="5">
			</div>
		</div>

		<div class="layui-form-item layui-form-text">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-block">
				<textarea name="remarks" placeholder="请输入内容" class="layui-textarea"
					style="width: 460px;"></textarea>
			</div>
		</div>
	</form>


</body>
</html>
