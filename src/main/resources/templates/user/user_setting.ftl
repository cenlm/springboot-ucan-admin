<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>用户管理</title>
<link rel="stylesheet" href="../js/layui/css/layui.css">
<link rel="stylesheet" href="../css/authz-layui.css">
<style media="screen">
element.style {
	height: 292.6px;
}
</style>
</head>
<body>
	<#assign contextPath="${request.contextPath}" /> 
	<#assign principal="${principal()!'null'}" />  
	<!-- 如果后端返回null，则赋值'null' -->
    <#assign userId="${userId()!'null'}" />  
 
	<div class="layui-fluid">
		<div class="layui-row" style="background-color: #F7F8FB;"
			style="height:100vh;">
			<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"
				style="font-size: 20px; color: #5A5A5A; font-weight: bold; padding: 10px 20px;">
				<span>个人设置</span>
				<hr>
			</div>

			<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"
				style="font-size: 13px;">
				<form class="layui-form layui-form-pane" id="userDetail"
					lay-filter="userDetail" style="margin: 10px 20px;" action=""
					onsubmit="return false">
					<input type="hidden" name="userId">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">用户名</label>
							<div class="layui-input-inline" style="width: 450px;">
								<input type="text" name="userName" lay-verify="required"
									placeholder="请输入用户名" autocomplete="on" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">Email</label>
							<div class="layui-input-inline">
								<input type="text" name="email" lay-verify="email"
									placeholder="请输入Email" autocomplete="on" class="layui-input"
									style="width: 450px">
							</div>
						</div>
					</div>

					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">手机号</label>
							<div class="layui-input-inline" style="width: 450px;">
								<input type="text" name="cellPhoneNumber"
									lay-verify="phone | number" placeholder="请输入手机号"
									autocomplete="on" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">现居地</label>
							<div class="layui-input-inline">
								<input type="text" name="address" placeholder="请输入地址"
									autocomplete="on" class="layui-input" style="width: 450px">
							</div>
						</div>
					</div>



					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">用户别名</label>
							<div class="layui-input-inline" style="width: 450px;">
								<input type="text" name="userAlias" autocomplete="on"
									class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">公司/职位</label>
							<div class="layui-input-inline">
								<input type="text" name="postName" autocomplete="on"
									class="layui-input layui-disabled" style="width: 450px"
									placeholder="暂无纪录" disabled>
							</div>
						</div>
					</div>

					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">创建日期</label>
							<div class="layui-input-inline" style="width: 450px;">
								<input type="text" name="createTime" lay-verify="required"
									disabled autocomplete="on" placeholder="系统自动生成"
									class="layui-input layui-disabled">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">上次修改</label>
							<div class="layui-input-inline">
								<input type="text" name="modifyTime" placeholder="暂无修改"
									autocomplete="on" class="layui-input layui-disabled"
									style="width: 450px" disabled>
							</div>
						</div>
					</div>

					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">最近登录</label>
							<div class="layui-input-inline" style="width: 450px;">
								<input type="text" name="entryDate" autocomplete="on"
									class="layui-input layui-disabled" placeholder="暂无纪录" disabled>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">更早登录</label>
							<div class="layui-input-inline">
								<input type="text" name="lastLogin" autocomplete="on"
									class="layui-input layui-disabled" style="width: 450px"
									placeholder="暂无纪录" disabled>
							</div>
						</div>
					</div>

					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">用户状态</label>
							<div class="layui-input-inline" style="width: 450px;">
								<input type="checkbox" name="isEnable" lay-skin="switch"
									lay-text="可用|禁用" checked class="layui-disabled" disabled>
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

					<div class="layui-form-item layui-form-text">
						<label class="layui-form-label" style="width: 1140px;">备注</label>
						<div class="layui-input-inline">
							<textarea name="remarks" placeholder="请输入内容"
								class="layui-textarea" style="width: 1140px;"></textarea>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<div class="layui-input-inline">
								<button class="layui-btn layui-btn-radius layui-btn-normal"
									lay-submit lay-filter="userDetail" id="submit">提交</button>
							</div>
						</div>
					</div>
				</form>
			</div>

			<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"
				style="font-size: 20px; color: #5A5A5A; font-weight: bold; padding: 10px 20px;">
				<hr>
				<span>修改密码</span>
				<hr>
			</div>

			<div class="layui-col-sm12 layui-col-md12 layui-col-lg12"
				style="font-size: 13px;">
				<form class="layui-form layui-form-pane" id="updPassword"
					lay-filter="updPassword" style="margin: 10px 20px;" action=""
					onsubmit="return false">
					<div class="layui-form-item">
						<input type="hidden" name="userId"> <label
							class="layui-form-label">旧密码</label>
						<div class="layui-input-block">
							<input type="password" name="password" class="layui-input"
								placeholder="旧密码" required lay-verify="required"
								style="width: 1030px">
						</div>
					</div>

					<div class="layui-form-item">
						<label class="layui-form-label">新密码</label>
						<div class="layui-input-block">
							<input type="password" name="newPassword" autocomplete="off"
								class="layui-input" required lay-verify="required"
								style="width: 1030px" placeholder="新密码">
						</div>
					</div>

					<div class="layui-form-item">
						<label class="layui-form-label">确认新密码</label>
						<div class="layui-input-block">
							<input type="password" name="confirmPassword" autocomplete="off"
								class="layui-input" required lay-verify="required"
								style="width: 1030px" placeholder="再次确认新密码">
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<div class="layui-input-inline">
								<button class="layui-btn layui-btn-radius layui-btn-normal"
									lay-submit lay-filter="updPassword" id="submit">提交</button>
							</div>
						</div>
					</div>
				</form>
			</div>


		</div>
	</div>

<!-- <form action="${request.contextPath}/user/upload" method="POST" enctype="multipart/form-data">
    <input type="text" name="description" placeholder="文件描述" required>
    <input type="file" name="file" accept=".jpg,.png" required>
    <button type="submit">安全上传</button>
</form> -->

	<script src="../js/layui/layui.js"></script>
	<script src="../js/jquery-3.6.3.min.js"></script>
	<script src="../js/jquery-migrate-1.2.1.min.js"></script>
	<script src="../js/xss/purify-3.2.5.min.js"></script>
    <script src="../js/xss/xss-scanner.js"></script>  
	<script type="text/javascript">
		layui.use([ 'form' ] , function() {
	                var form = layui.form;
	                var userId = "${userId}";
	                var contextPath = "${contextPath}";
	                var principal = "${principal}";
	                /* console.log(userId); 保存在session中的userId
	                console.log(contextPath); */
	                $("#updPassword input[name='userId']").val(userId);
	                $("#userDetail input[name='userId']").val(userId);

	                //修改密码
	                form.on('submit(updPassword)' , function(data) {
		                var formData = form.val('updPassword');
		                if (formData.newPassword != formData.confirmPassword) {
			                layer.msg("两次输入的新密码不一致！" , {
				                icon : 5
			                });
			                return;
		                }

		                let requestUrl = contextPath + "/user/updatePassword";
		                $.ajax({
		                        type : "POST" ,
		                        url : requestUrl ,
		                        data : formData ,
		                        dataType : "json" ,
		                        success : function(result) {
			                        if (result.code == 0) {
				                        layer.msg(result.msg + "新密码【" + formData.newPassword + "】，" + "您需要重新登录！" , {
					                        icon : 6
				                        });
				                        setTimeout(function() {
					                        window.location.href = contextPath + "/logout";
				                        } , "5000");
			                        } else {
				                        layer.msg(result.msg , {
					                        icon : 5
				                        });
			                        }
		                        } ,
		                        error : function(e) {
			                        layer.msg(e.responseText , {
				                        icon : 2
			                        });
		                        }
		                });
		                //防止表单重复提交
		                return false;
	                });

	                function renderUserDetail() {
		                $.ajax({
		                        type : "POST" ,
		                        url : contextPath + "/user/queryUserDetail" ,
		                        data : {
		                                userId : userId ,
		                                userName : principal
		                        } ,
		                        dataType : "json" ,
		                        success : function(result) {
			                        if (result.code == 0) {
				                        var data = result.data[0];
				                        $("#updPassword input[name='userId']").val(userId);
				                        $("#userDetail input[name='userId']").val(userId);
				                        $("#userDetail input[name='userName']").val(data.userName);
				                        $("#userDetail input[name='email']").val(data.email);
				                        $("#userDetail input[name='cellPhoneNumber']").val(data.cellPhoneNumber);
				                        $("#userDetail input[name='address']").val(data.address);
				                        $("#userDetail input[name='userAlias']").val(data.userAlias);
				                        $("#userDetail input[name='postName']").val(data.post.postName);
				                        $("#userDetail input[name='createTime']").val(data.createTime);
				                        $("#userDetail input[name='modifyTime']").val(data.modifyTime);
				                        $("#userDetail input[name='entryDate']").val(data.entryDate);
				                        $("#userDetail input[name='lastLogin']").val(data.lastLogin);
				                        $("#userDetail input[name='isEnable']").val(data.isEnable);
				                        /* $("#userDetail input[type='radio']").val(data.sex); */
				                        //重新渲染性别
				                        if (data.sex == 1) {
					                        $("form[id='userDetail'] input[name='sex'][value=1]").prop('checked' , true);
				                        } else {
					                        $("form[id='userDetail'] input[name='sex'][value=0]").prop('checked' , true);
				                        }
				                        $("#userDetail textarea[name='remarks']").val(data.remarks);
				                        form.render();
				                        oldUserName = $("#userDetail input[name='userName']").val();
			                        } else {
				                        layer.msg(result.msg , {
					                        icon : 5
				                        });
			                        }
		                        } ,
		                        error : function(e) {
			                        layer.msg(e.responseText , {
				                        icon : 2
			                        });
		                        }
		                });
	                }
	                renderUserDetail();
	                var oldUserName;
	                form.on('submit(userDetail)' , function(data) {
		                let formData = form.val('userDetail');
		                $.ajax({
		                        type : "POST" ,
		                        url : contextPath + "/user/updateUser" ,
		                        contentType : 'application/json; charset=UTF-8' ,
		                        data : JSON.stringify({
		                                userId : userId ,
		                                userName : formData.userName ,
		                                email : formData.email ,
		                                cellPhoneNumber : formData.cellPhoneNumber ,
		                                address : formData.address ,
		                                userAlias : formData.userAlias ,
		                                sex : formData.sex ,
		                                remarks : formData.remarks
		                        }) ,
		                        dataType : "json" ,
		                        success : function(result) {
			                        if (result.code == 0) {
				                        if (oldUserName != formData.userName) {
					                        layer.msg(result.msg + "用户名已更改为【" + formData.userName + "】，您需要重新登录！" , {
						                        icon : 6
					                        });
					                        setTimeout(function() {
						                        window.location.href = contextPath + "/logout";
					                        } , "4000");
				                        } else {
					                        layer.msg(result.msg , {
						                        icon : 6
					                        });
				                        }

			                        } else {
				                        layer.msg(result.msg , {
					                        icon : 5
				                        });
			                        }
		                        } ,
		                        error : function(e) {
			                        layer.msg("更新失败！" , {
				                        icon : 2
			                        });
		                        }
		                });
		                //防止表单重复提交
		                return false;
	                });

                });
	</script>



</body>
</html>
