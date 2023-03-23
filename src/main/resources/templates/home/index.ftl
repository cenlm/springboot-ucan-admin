<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>Ucan权限管理系统</title>
<link rel="stylesheet" href="js/layui/css/layui.css">
<link rel="stylesheet" href="css/index.css">
<style type="text/css">
body {
	background-color: #E8EBF4;
	/*字体在layui.css中修改*/
	/* font-family: "阿里巴巴普惠体 H";  */ /*H B M R*/
}
</style>
</head>
<body>
  <#assign contextPath="${request.contextPath}"/> 
	<div class="layui-fluid">
		<!-- 头部 -->
		<div class="layui-row top-nav">
			<!-- logo或文字 -->
			<div class="layui-col-md2 layui-col-lg2"><a href="${contextPath}/board" target="index_iframe" style="color:#ffffff;">Ucan权限管理系统</a></div>
			<!-- 其他导航信息 -->
			<div class=" layui-col-md3 layui-col-lg3 "></div>
			<div class=" layui-col-md6 layui-col-lg6">
				<ul class="layui-nav"
					style="text-align: right; background-color: transparent;">
					<!-- 小Tips：这里有没有发现，设置导航靠右对齐（或居中对齐）其实非常简单 -->
					<li class="layui-nav-item"><a href="javascript:;">欢迎 <shiro:principal />
							登录！
					</a></li>
					<li class="layui-nav-item"><a href="javascript:;">消息<span
							class="layui-badge">+99</span></a></li>

				</ul>
			</div>
			<div class="layui-col-md1 layui-col-lg1">
				<ul class="layui-nav" lay-bar="disabled"
					style="background-color: transparent;">
					<li class="layui-nav-item" lay-unselect=""><a
						href="javascript:;"><img
							src="https://img.zcool.cn/community/015ac85f110b9fa801206621387957.png@1280w_1l_2o_100sh.png"
							class="layui-nav-img"></a>
						<dl class="layui-nav-child">
							<@shiro.hasPermission name="Menu:PERSONAL_SET">
								<dd>
									<a href="${contextPath}/user/user_setting"
										target="index_iframe">个人设置</a>
								</dd>
								<hr>
							</@shiro.hasPermission>
							
							<dd style="text-align: center;">
								<a href="${contextPath}/logout">退出</a>
							</dd>
						</dl></li>
				</ul>
			</div>
		</div>

		<div class="layui-row" style="top: 60px; position: relative;">

			<!-- 左侧边垂直菜单容器 -->
			<div
				class="layui-col-md2 layui-col-lg2 left-menu-container left-element"
				style="transition-duration: 0.5s;">
				<ul class="layui-nav layui-nav-tree" lay-shrink="all"
					lay-filter="left-nav">
					<!-- 侧边导航: <ul class="layui-nav layui-nav-tree layui-nav-side"> -->
					<@shiro.hasPermission name="Menu:HOME_VIEW">
						<li class="layui-nav-item"><a href="${contextPath}/board" target="index_iframe"><i
								class="layui-icon layui-icon-console"
								style="font-size: 16px; color: #ffffff; margin-right: 6%;"></i><cite>仪表盘</cite></a></li>
					</@shiro.hasPermission>
					<@shiro.hasPermission name="Menu:SYS_MANAGE">
						<li class="layui-nav-item"><a href="javascript:;"><i
								class="layui-icon layui-icon-username"
								style="font-size: 16px; color: #ffffff; margin-right: 6%;"></i><cite>系统管理</cite></a>
							<dl class="layui-nav-child layui-nav-child-c">
								<@shiro.hasPermission name="Menu:USER_MANAGE">
									<dd>
										<a href="${contextPath}/user/user_list"
											target="index_iframe">用户管理</a>
									</dd>
								</@shiro.hasPermission>
								<@shiro.hasPermission name="Menu:ROLE_MANAGE">
									<dd>
										<a href="${contextPath}/role/role_list"
											target="index_iframe">角色管理</a>
									</dd>
								</@shiro.hasPermission>
								<@shiro.hasPermission name="Menu:PERMS_MANAGE">
									<dd>
										<a href="${contextPath}/permission/permission_list"
											target="index_iframe">权限管理</a>
									</dd>
								</@shiro.hasPermission>
							</dl></li>
					</@shiro.hasPermission>
				</ul>
			</div>
			<!-- 面包屑导航 -->
			<div
				class=" layui-col-md10 layui-col-lg10 breadcrumb-bar right-element"
				style="transition-duration: 0.5s;">
				<span class="layui-breadcrumb"><i
					class="layui-icon layui-icon-shrink-right" id="shrink-event"
					style="margin: 20px 20px; font-size: 20px;" title="侧边收缩与展开"></i> <a
					href="../index.htm" tppabs="https://www.layuiweb.com/">首页</a> <a
					href="index.htm" tppabs="https://www.layuiweb.com/demo/">演示</a> <a><cite>导航元素</cite></a>
				</span>
			</div>
			<!-- iframe -->
			<div class=" layui-col-md10 layui-col-lg10 iframe-body right-element">
				<iframe name="index_iframe" frameborder="0"
					style="width: 100%; height: 100%;" src="${contextPath}/board"></iframe>
			</div>
			<!-- 网站底部 -->
			<div class=" layui-col-md10 layui-col-lg10 web-footer right-element">
				&copy;2023 / UI：layui v2.7.6</div>
		</div>
	</div>
	<script src="js/layui/layui.js"></script>
	<script src="js/jquery-3.6.3.min.js"></script>
	<script src="js/jquery-migrate-1.2.1.min.js"></script>
	<script src="js/index.js"></script>
	<script>
		layui.use([ 'element' , 'layer' ] , function() {
	                var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
	                //监听导航点击
	                element.on('nav(left-nav)' , function(elem) {

	                });
                });
	</script>
</body>
</html>