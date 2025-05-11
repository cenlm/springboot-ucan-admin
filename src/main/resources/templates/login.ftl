<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>系统登录</title>
<link rel="stylesheet" href="js/layui/css/layui.css">
<style type="text/css">
body {
	height: 100vh;
	/* 加载背景图 */
	background-image: url(imgs/login.jpg);
	/* 背景图垂直、水平均居中 */
	background-position: center center;
	/* 背景图不平铺 */
	background-repeat: no-repeat;
	/* 当内容高度大于图片高度时，背景图像的位置相对于viewport固定 */
	background-attachment: fixed;
	/* 让背景图基于容器大小伸缩 */
	background-size: cover;
	/* 设置背景颜色，背景图加载过程中会显示背景色 */
	background-color: #464646;
}

#form-bg {
	width: 100%;
	height: 330px;
	background-color: #3A4E7E;
	border-radius: 20px 20px 20px 20px;
	box-shadow: 5px 3px 5px 5px #3A4E7E;
	opacity: 0.6;
	margin: 230px 0;
	/* margin: 30.5vh 0; */
	position: absolute;
}

#formHeader {
	position: absolute;
	margin-top: 290px;
	/* margin-top: 39vh; */
	width: 100%;
	left: 0px;
	background: linear-gradient(to right, #17212D, #D2DCE8);
}

#form-title {
	color: #ffffff;
	position: absolute;
	top: -45px;
	left: 0px;
	font-size: 24px;
	font-weight: bold;
	width: 100%;
	text-align: center;
	width: 100%;
	text-align: center;
	opacity: 0.95;
}

#hr-solid {
	width: 100%;
	border: 0;
	border-top: 3px solid #17212D;
	opacity: 0.6;
}

form {
	position: absolute;
	margin-top: 320px;
	/* margin-top: 44vh; */
	/* left: 20px; */
	left: 1vw;
}

input[type='text'] {
	border-radius: 10px;
}

input[type='password'] {
	border-radius: 10px;
}

#rememberMe {
	position: relative;
	/* margin-left: 200px; */
	margin-left: 13vw;
}

.layui-form-checkbox[lay-skin="primary"] span {
	color: #ffffff;
}

#submit {
	width: 100%;
	background-color: #798EC1;
}

.layui-form-checked[lay-skin="primary"] i {
	border-color: #5432C7 !important;
	background-color: #5432C7;
}
</style>
</head>
<body>
	<#assign contextPath="${request.contextPath}" />
	<div class="layui-fluid">
		<div class="layui-row">
			<div
				class="layui-col-sm4 layui-col-md4 layui-col-lg4 layui-col-md-offset4">
				<div class="layui-row">
					<div id="form-bg"></div>
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"
						id="formHeader">
						<span id="form-title">Ucan权限管理系统</span>
						<div id="hr-solid"></div>
					</div>
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">

						<form class="layui-form" lay-filter="userForm" action=""
							onsubmit="return false">
							<div class="layui-form-item">
								<label class="layui-form-label"> <i
									class="layui-icon layui-icon-username"
									style="font-size: 30px; color: #D2DCE8;"></i>
								</label>
								<div class="layui-input-block">
									<input type="text" name="username" required
										lay-verify="required" placeholder="用户名" autocomplete="on"
										class="layui-input">
								</div>
							</div>
							<div class="layui-form-item">
								<label class="layui-form-label"> <i
									class="layui-icon layui-icon-password"
									style="font-size: 30px; color: #D2DCE8;"></i>
								</label>
								<div class="layui-input-block">
									<input type="password" name="password" required
										lay-verify="required" placeholder="密码" autocomplete="off"
										class="layui-input">
								</div>
							</div>
							<div class="layui-form-item">
								<div class="layui-input-inline" id="rememberMe">
									<input type="checkbox" name="rememberMe" value="true"
										lay-skin="primary" title="记住我？">
								</div>
							</div>
							<div class="layui-form-item">
								<div class="layui-input-block">
									<button class="layui-btn layui-btn-radius layui-btn-normal"
										lay-submit lay-filter="userForm" id="submit">登录</button>
								</div>
							</div>
						</form>
					</div>
					<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"></div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/layui/layui.js"></script>
	<script src="js/jquery-3.6.3.min.js"></script>
    <script src="js/xss/purify-3.2.5.min.js"></script>
	<script src="js/xss/xss-scanner.js"></script>   
	<script src="js/login/login.js"></script>
	<script type="text/javascript">
		var contextPath = "${contextPath}";
                window.onload = function() {
	                let url = document.location.href;
	                if (url.indexOf("yes") == -1) {
		                let t = new Date();
		                window.location.href = url + "?yes=" + t.getTime();
		                if (top != self) {//如果当前窗口（登录页）不是顶层窗口，则要跳出iframe，要重新登录
			                top.document.location.reload();
		                }
	                }
                }
	</script>
</body>
</html>
