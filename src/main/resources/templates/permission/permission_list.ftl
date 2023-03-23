<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>权限管理</title>
<link rel="stylesheet" href="../js/layui/css/layui.css">
<link rel="stylesheet" href="../js/layui_ext/treeTable/treeTable.css">
<link rel="stylesheet" href="../js/layui_ext/iconSelected/css/theme.css">
<link rel="stylesheet" href="../js/layui_ext/numberInput/css/theme.css">
<link rel="stylesheet" href="../css/authz-layui.css">
</head>
<body>
	<div class="layui-fluid">

		<div class="layui-row">
			<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
				<div class="layui-btn-group">
					<@shiro.hasPermission name="Btn:PERMS_ADD">
					<button class="layui-btn layui-btn-sm" lay-event="addPermission"
						id="addPermissionButton">新增权限</button></@shiro.hasPermission>
				</div>
			</div>
			<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
				<table id="permission_table" lay-filter="permission_table"></table>
			</div>
			<!-- 	<div class="layui-col-sm12 layui-col-md12 layui-col-lg12" id="treeNodeTest">
				
				</div>  -->

		</div>
	</div>

	<!-- 行内表格操作按钮 模板-->
	<script type="text/html" id="barDemo">
          <div class="layui-btn-group">
			<@shiro.hasPermission name="Btn:PERMS_VIEW_UPD"><a class="layui-btn  layui-btn-xs"  lay-event="detail">查看</a></@shiro.hasPermission>
			<@shiro.hasPermission name="Btn:PERMS_ADD"><a class="layui-btn  layui-btn-xs"  lay-event="add">新增权限</a></@shiro.hasPermission>
			<@shiro.hasPermission name="Btn:PERMS_DEL"><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></@shiro.hasPermission>
          </div>
		</script>
	<script type="text/javascript">
		var context_path = "${request.contextPath}";
	</script>
	<script src="../js/layui/layui.js"></script>
	<script src="../js/jquery-3.6.3.min.js"></script>
	<script src="../js/jquery-migrate-1.2.1.min.js"></script>
	<script src="../js/permission_list.js"></script>

	<!-- 隐藏的表单-查看权限详情、修改权限信息 -->
	<form class="layui-form layui-form-pane" id="permissionDetail"
		lay-filter="permissionDetail"
		style="display: none; margin: 10px 20px;">
		<input type="text" name="permissionId" style="display: none;">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">权限名称</label>
				<div class="layui-input-inline">
					<input type="text" name="permissionName" lay-verify="required"
						placeholder="请输入权限名称" autocomplete="off" class="layui-input"
						style="width: 350px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">权限图标</label>
				<div class="layui-input-inline">
					<input type="text" name="icon" value="layui-icon layui-icon-auz"
						placeholder="请选择" maxlength="16" autocomplete="off"
						class="layui-input layui-select-icon" />
				</div>
			</div>

		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">节点链接</label>
				<div class="layui-input-inline">
					<input type="text" name="url" placeholder="请输入路径"
						autocomplete="off" class="layui-input" style="width: 220px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">权限类型</label>
				<div class="layui-input-inline" style="width: 290px">
					<select name="permissionType">
						<option value="1" selected>菜单</option>
						<option value="2">按钮</option>
						<option value="3">数据</option>
					</select>
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">上级权限</label>
				<div class="layui-input-inline">
					<input type="text" name="parentName" lay-verify="required"
						autocomplete="off" placeholder="请选择上级权限"
						class="layui-input layui-disabled" disabled style="width: 310px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">节点位置</label>
				<div class="layui-input-inline">
					<input type="text" name="position" placeholder="节点位置"
						autocomplete="off" class="layui-input" style="width: 313px"
						id="position" data-max="500" data-min="0" data-step="5">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">创建日期</label>
				<div class="layui-input-inline">
					<input type="text" name="createTime" lay-verify="required" disabled
						autocomplete="off" placeholder="系统自动生成"
						class="layui-input layui-disabled" style="width: 310px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">上次修改</label>
				<div class="layui-input-inline">
					<input type="text" name="modifyTime" placeholder="暂无修改" disabled
						autocomplete="off" class="layui-input layui-disabled"
						style="width: 313px">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">权限编码</label>
				<div class="layui-input-inline">
					<input type="text" name="permissionCode" placeholder="请输入权限编码"
						autocomplete="on" class="layui-input" style="width: 313px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">系统编码</label>
				<div class="layui-input-inline">
					<input type="text" name="sysCode" placeholder="请输入系统编码"
						autocomplete="on" class="layui-input" style="width: 310px">
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


	<!-- 隐藏的表单-新增权限 -->
	<form class="layui-form layui-form-pane" id="addPermission"
		lay-filter="addPermission" style="display: none; margin: 10px 20px;">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">权限名称</label>
				<div class="layui-input-inline">
					<input type="text" name="permissionName" lay-verify="required"
						placeholder="请输入权限名称" autocomplete="off" class="layui-input"
						style="width: 350px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">权限类型</label>
				<div class="layui-input-inline" style="width: 290px">
					<select name="permissionType">
						<option value="1" selected>菜单</option>
						<option value="2">按钮</option>
						<option value="3">数据</option>
					</select>
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">权限链接</label>
				<div class="layui-input-inline">
					<input type="text" name="url" placeholder="请输入菜单路径"
						autocomplete="off" class="layui-input" style="width: 350px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">权限图标</label>
				<div class="layui-input-inline">
					<input type="text" name="icon" value="layui-icon layui-icon-auz"
						placeholder="请选择" maxlength="16" autocomplete="off"
						class="layui-input layui-select-icon" />
				</div>
			</div>

		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">上级权限</label>
				<div class="layui-input-inline">
					<input type="hidden" name="parentId" value="-1"> <input
						type="text" name="parentName" id="parentId" lay-verify="required"
						autocomplete="off" placeholder="无"
						class="layui-input layui-disabled" disabled style="width: 310px">

				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">节点位置</label>
				<div class="layui-input-inline">
					<input type="text" name="position" placeholder="节点位置"
						autocomplete="off" class="layui-input" style="width: 313px"
						id="position" data-max="500" data-min="0" data-step="5">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">权限编码</label>
				<div class="layui-input-inline">
					<input type="text" name="permissionCode" placeholder="请输入权限编码"
						autocomplete="on" class="layui-input" style="width: 313px">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">系统编码</label>
				<div class="layui-input-inline">
					<input type="text" name="sysCode" placeholder="请输入系统编码"
						autocomplete="on" class="layui-input" style="width: 313px">
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

</body>
</html>
