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

.layui-tab-item {
	width: 100%;
	height: 100%;
}

.layui-tab-title {
	background-color: #F7F8FB;
	width: 100%;
}

.layui-tab-title li {
	color:#252525;
	background-color: #FAFAFA;
	border-color:#434343;
	border-left:solid;
	border-right:solid;
	border-width:1px;
	border-color: #E3E3E3;
}

.layui-this {
	font-weight: bold;
}

.layui-tab-title li .layui-tab-close:hover{
    background-color: #5B54B1;
}
</style>
</head>
<body>
  <#assign contextPath="${request.contextPath}"/> 
  <#assign principal="${principal()!'null'}"/> 

	<div class="layui-fluid">
		<!-- 头部 -->
		<div class="layui-row top-nav">
			<!-- logo或文字 -->
			<div class="layui-col-md2 layui-col-lg2"><a href="javascript:;" class="ucan_board" style="color:#ffffff;">Ucan权限管理系统</a></div>
			<!-- 其他导航信息 -->
			<div class=" layui-col-md3 layui-col-lg3 "></div>
			<div class=" layui-col-md6 layui-col-lg6">
				<ul class="layui-nav"
					style="text-align: right; background-color: transparent;">
					<!-- 小Tips：这里有没有发现，设置导航靠右对齐（或居中对齐）其实非常简单 -->
					<li class="layui-nav-item"><a href="javascript:;">欢迎 <@shiro.principal />
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
							<dd id="user_setting">
									<a href="javascript:;">个人设置</a>
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
						<li class="layui-nav-item"><a href="javascript:;" class="ucan_board"><i
								class="layui-icon layui-icon-console"
								style="font-size: 16px; color: #ffffff; margin-right: 6%;"></i><cite>仪表盘</cite></a></li>
					</@shiro.hasPermission>
					<@shiro.hasPermission name="Menu:SYS_MANAGE">
						<li class="layui-nav-item"><a href="javascript:;"><i
								class="layui-icon layui-icon-username"
								style="font-size: 16px; color: #ffffff; margin-right: 6%;"></i><cite>系统管理</cite></a>
							<dl class="layui-nav-child layui-nav-child-c">
								<@shiro.hasPermission name="Menu:USER_MANAGE">
										<dd id="user_manager">
										<a href="javascript:;">用户管理</a>
									</dd>
								</@shiro.hasPermission>
								<@shiro.hasPermission name="Menu:ROLE_MANAGE">
									<dd id="role_manager">
										<a href="javascript:;">角色管理</a>
									</dd>
								</@shiro.hasPermission>
								<@shiro.hasPermission name="Menu:PERMS_MANAGE">
									<dd>
										<dd id="permission_manager">
										<a href="javascript:;">权限管理</a>
									</dd>
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
				<div class="layui-tab layui-tab-card" lay-filter="index-content"
					lay-allowclose="true">
					<ul class="layui-tab-title"></ul>
					<div class="layui-tab-content iframe-body"></div>
				</div>
			</div>
			<!-- 网站底部 -->
		<!-- 	<div class=" layui-col-md10 layui-col-lg10 web-footer right-element">
				&copy;2023 / UI：layui v2.7.6</div>
		</div> -->
	</div>
	<script src="js/layui/layui.js"></script>
	<script src="js/jquery-3.6.3.min.js"></script>
	<script src="js/jquery-migrate-1.2.1.min.js"></script>
	<script src="js/index.js"></script>
	<script>
	  var context_path = "${contextPath}";
	  var principal = "${principal}"; 
	                function kickout_check() {
	                    if ("WebSocket" in window) {
	                        console.log("您的浏览器支持 WebSocket!");

	                        // 创建一个 websocket
	                        var ws = new WebSocket("ws://localhost:9999");
	                      //消息窗口timeOut 秒后消失
	                        var timeOut =5;
	                        ws.onopen = function() {
	                            // Web Socket 已连接上，使用 send() 方法发送数据
	                            //加 _index 后缀标识从哪个页面发送socket消息
	                            ws.send(principal+"_index");
	                            console.log("已连接到服务器");
	                           
	                        };

	                        ws.onmessage = function(evt) {
	                            var received_msg = evt.data;
	                            console.log("服务器返回数据：" + received_msg);
	                            if (received_msg == "kickout") {
	                                layer.msg("账号异地登录，已被强制下线，"+timeOut+"秒后跳转到登录页面！" , {
	                                    icon : 5,
	                                    time: timeOut*1000 
	                                });
	                                setTimeout(function() {
	                                    window.location.href = context_path + "/logout";
	                                } , timeOut*1000);
	                            }
	                        };

	                        ws.onclose = function() {
	                            // 关闭 websocket
	                            console.log("连接已关闭...");
	                        };
	                    }

	                    else {
	                        // 浏览器不支持 WebSocket
	                        alert("您的浏览器不支持 WebSocket!");
	                    }
	                }
	                kickout_check();
	</script>
</body>
</html>