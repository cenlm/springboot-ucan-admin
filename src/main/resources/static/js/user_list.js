/**
 * @Description: 
 * @author liming.cen
 * @date 2023年1月6日 下午2:42:30
 * @Copyright: 2023 liming.cen. All rights reserved.
 * 
 */
layui.config({
	base: '../js/'
}).extend({
	dtree: 'layui_ext/dtree/dtree',
	iconSelected: "layui_ext/iconSelected/js/index",
	numberInput: "layui_ext/numberInput/js/index"
}).use(['table', 'form', 'element','dtree','iconSelected',"numberInput"], function() {
	var dtree = layui.dtree;
	
	var iconSelected = layui.iconSelected;
	var table = layui.table;
	var element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
	var form = layui.form;
	//首次加载完组织结构树（或点击节点）时，所需的查询条件数据（或提示信息）
	var currentOrgId = '';
	var currentOrgName = '';
	var currentPostId = '';
	var currentPostName = '';
	var form;
	var tableIns;
	var groupUserTab;
	var requestUserUrl;
	//提前获取所有图标class和名称，看源码
	var icons=iconSelected.icons;
	var iconNames= {};
	//以键值对的方式保存图标class和名称，方便数据回显时使用
	icons.forEach(item=>{
	    $(iconNames).attr(item.classList,item.name);
	});
	iconSelected.render(".layui-select-icon", {
	        event: {
	            select(event, data) {
//	                console.log("选中的图标数据", { event, data });
//	                console.log(data);
//	                $("#updateOrg input[id='icon']").val(data.icon);
	            }
	        },
	    });
	var numberInput = layui.numberInput;
	 //数字插件
	 numberInput.render("#addOrganization input[name='position']");
	 numberInput.render("#addPost input[name='position']");
	 numberInput.render("#updateOrg input[name='position']");
	 numberInput.render("#updatePost input[name='position']");
	 
	//弹窗全局样式
			layer.config({
			    skin: 'authz-class'
			  });
		
	//组织结构树的渲染
	var orgTree = dtree.render({

		elem: "#org_tree",
		checkbar: false,
		checkbarType: "no-all", // 默认就是all，其他的值为： no-all  p-casc   self  only
		toolbar: true,
		width: "100%",
		accordion: false,
		initLevel: "1",
		line: true,
		skin:"authz",
		iconfont: ["layui-icon", "dtreefont", "iconfont"],
		url: context_path + "/organization/getOrganizationTreeNodes",
		success: function(data, obj, first) {
			//根节点不允许删除操作（移除节点删除图标）
			//			$($("#org_tree div[class='layui-tree-entry'] div[class='layui-btn-group layui-tree-btnGroup']")[0]).find("i[data-type='del']").remove();
			currentOrgId = data.data[0].id;
			currentOrgName = data.data[0].title;
			//标识当前操作的是哪个组织结构
			$("#currentOrg").text(currentOrgName);
			//用于在新增职位、新增用户做判断
			$("#currentOrgId").val(currentOrgId);
			$("#currentPostId").val("");
			$("#currentGroupId").val("");
		},
		done: function(res, $ul, first) {
			//			 //显示职位信息树
			showPostTree(false);
			//显示用户列表
			showUsers("org");
			//显示分组用户表格
			renderGroupUserTab();
		},
		toolbarShow: ["add", "edit", "delete"], // 显示三个操作项
		toolbarFun: {
			addTreeNode: function(treeNode, $div) { //新增角色信息

				//从节点的额外数据获取roleId等信息
				let dataBasic = JSON.parse($($div).attr("data-basic"));
				//获取指定节点的数据
				//			    var param = dtree.getParam("role_tree", dataBasic.roleId);
				//新增组织信息
				$("#addOrganization input[name='parentId']").val(dataBasic.orgId);
				$("#addOrganization input[name='parentOrgName']").val(dataBasic.orgName);
				layer.open({
					type: 1,
					offset: '10px',
					area: ['500px', '500px'],
					title: '新增组织',
					content: $("#addOrganization"),
					shade: [0.2, '#1D1E3E'],
					btn: ['提交', '取消'],
					btn1: function(index, layero) {
						var formData = form.val('addOrganization');
						if (formData.orgName == "") {
							layer.msg('组织名称不能为空！', {
								icon: 5
							});
							return;
						}

						$.ajax({
							type: "POST",
							url: context_path + "/organization/addOrganization",
							data: formData,
							dataType: "json",
							success: function(result) {

								if (result.code == 0) {

									layer.msg(result.msg, {
										icon: 6
									});
									//成功添加组织后，清空表单数据
									form.val("addOrganization", { //addOrganization 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
										"orgName": "",
										"parentId": "",
										"parentOrgName": "",
										"orgType": "",
										"orgCode": ""
									});
									layer.close(index);
									//3秒后重载当前页面
									setTimeout(function() {
										document.location.reload()
									}, "3000");
								} else {
									layer.msg(result.msg, {
										icon: 5
									});
								}

							},
							error: function(e) {
								layer.msg(e.responseText, {
									icon: 2
								});
							}

						});

					},
					btn2: function(index, layero) {
						//return false;
					},
					cancel: function(layero, index) {
						layer.closeAll();
					}

				});

			},
			editTreeNode: function(treeNode, $div) { //修改组织节点信息
				//从节点的额外数据获取orgId等信息
				let dataBasic = JSON.parse($($div).attr("data-basic"));
				//获取指定节点的数据
				//var param = dtree.getParam("org_tree", dataBasic.orgId);
				let orgId = dataBasic.orgId;
				let parentName = dataBasic.parentName;
				let position = dataBasic.position;
				form.val("updateOrg", { //addOrganization 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
					"orgId": orgId,
					"orgName": dataBasic.orgName,
					"parentOrgName": parentName === undefined ? "无" : parentName,
					"orgType": dataBasic.orgType,
					"icon":dataBasic.icon,
					"position": dataBasic.position,
					"orgCode": dataBasic.orgCode
				});
//图标回显
$("#updateOrg input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("i").attr("class",dataBasic.icon+" layui-ext-icon-selected-icon");
$("#updateOrg input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("div[class='layui-ext-icon-selected-name']").text($(iconNames).attr(dataBasic.icon));
				layer.open({
					type: 1,
					offset: '10px',
					area: ['500px', '500px'],
					title: '修改组织信息',
					content: $("#updateOrg"),
					shade: [0.2, '#1D1E3E'],
					btn: ['提交', '取消'],
					btn1: function(index, layero) {
						var formData = form.val('updateOrg');
//						if (position === 0) {
//							formData.position = 0;
//						}
						if (formData.orgName == "") {
							layer.msg('组织名称不能为空！', {
								icon: 5
							});
							return;
						}
						$.ajax({
							type: "POST",
							url: context_path +
								"/organization/updateOrganization",
							data: formData,
							dataType: "json",
							success: function(result) {

								if (result.code == 0) {
									layer.msg(result.msg, {
										icon: 6
									});
									//更新右侧的组织提示信息
									$("#currentOrg").text(formData.orgName);
									//更新节点名称
									$($div).find("cite").text(formData
										.orgName);
									currentOrgId = formData.orgId;
									currentOrgName = formData.orgName;
									//修改节点数据，方便回显时获取最新的值
									dataBasic.orgName = formData.orgName;
									$($div).attr("data-basic", JSON
										.stringify(dataBasic));
									//显示职位信息树
									showPostTree(true);
									//显示用户列表
									showUsers("org");
									if (position != formData
										.position||dataBasic.icon!=formData.icon) { //如果节点位置或图标被修改了，则刷新整个页面
										//节点位置已更改，3秒后重载当前页面
										setTimeout(function() {
											document.location
												.reload()
										}, "3000");
									}
									layer.close(index);
								} else {
									layer.msg(result.msg, {
										icon: 5
									});

								}

							},
							error: function(e) {
								layer.msg(e.responseText, {
									icon: 2
								});
							}

						});

					},
					btn2: function(index, layero) {
						//return false;
					},
					cancel: function(layero, index) {
						layer.closeAll();
					}

				});
			},
			delTreeNode: function(treeNode, $div) {
				//从节点的额外数据获取roleId等信息
				let dataBasic = JSON.parse($($div).attr("data-basic"));
				//删除角色
				let orgId = dataBasic.orgId;
				let isSuper = dataBasic.isSuper;
				if(isSuper==1) {
				    layer.msg("超级管理员组织节点不能删除！", {
					icon: 5
				});
				    return;
				}
				
				layer.confirm('确定删除此节点？', {
					icon: 3,
					title: '删除组织'
				}, function(index) {
					$.ajax({
						type: "POST",
						url: context_path + "/organization/deleteOrganization",
						data: {
							orgId: orgId,
							isSuper: isSuper
						},
						dataType: "json",
						success: function(result) {
							if (result.code == 0) {
								layer.msg(result.msg, {
									icon: 6
								});

								//节点位置已更改，3秒后重载当前页面
								setTimeout(function() {
									document.location.reload()
								}, "3000");
							} else {
								layer.msg(result.msg, {
									icon: 5
								});
							}

						},
						error: function(e) {
							layer.msg(e.responseText, {
								icon: 2
							});
						}

					});
				});

			}
		}

	});

	dtree.on("node('org_tree')", function(obj) { //组织树节点点击事件
		let id = obj.param.nodeId;
		let name = obj.param.context;
		currentOrgId = id;
		currentOrgName = name;
		//标识当前操作的是哪个组织结构
		$("#currentOrg").text(currentOrgName);
		//用于在新增职位、新增用户做判断
		$("#currentOrgId").val(id);
		$("#currentPostId").val("");
		$("#currentGroupId").val("");
		//显示职位信息树
		showPostTree(true);
		//显示用户列表
		showUsers("org");
		
		//显示分组用户表格
		renderGroupUserTab();
		//点击组织结构节点时，职位提示信息置空（页面首次加载时显示，如果有查到职位信息）
		//点击职位信息节点时才显示
		$("#currentPost").text("");
	});

	dtree.on("node('post_tree')", function(obj) { // 职位树节点点击事件
		let id = obj.param.nodeId;
		let name = obj.param.context;
		currentPostId = id;
		currentPostName = name;
		//标识当前操作的是哪个职位节点
		$("#currentPost").text(">" + currentPostName);
		//查询用户信息
		showUsers("post");
		//用于在新增职位、新增用户做判断
		$("#currentOrgId").val("");
		$("#currentPostId").val(currentPostId);
		$("#currentGroupId").val("");
	});

	/**
	 * 职位树的渲染
	 * calledFromOrgClick 是否通过点击组织结构树来查询职位？
	 */
	function showPostTree(calledFromOrgClick) { //判断showPostTree方法的调用来源：true 组织节点点击、false 页面首次加载
		//职位信息树的渲染
		var postTree = dtree.render({
			elem: "#post_tree",
			checkbar: false,
			checkbarType: "no-all", // 默认就是all，其他的值为： no-all  p-casc   self  only
			toolbar: true,
			width: "100%",
			accordion: false,
			line: true,
		        skin:"authz",
			iconfont: ["layui-icon", "dtreefont", "iconfont"],
			url: context_path + "/post/getPostTreeNodes",
			request: {
				orgId: currentOrgId
			},
			success: function(data, obj, first) {
				//显示职位节点名称
				if (calledFromOrgClick) {
					$("#currentPost").text("");
				} else {
				        let postData = data.data[0];
				        if(postData!=undefined) {
				            currentPostId = postData.id;
					    currentPostName = postData.title;
				        }
					
				}
			},
			done: function(res, $ul, first) {
				//			
			},
			toolbarShow: ["add", "edit", "delete"], // 显示三个操作项
			toolbarFun: {
				//				addTreeNode: function(treeNode, $div) { //新增角色信息
				//				   
				//				    //从节点的额外数据获取roleId等信息
				//				    let dataBasic=JSON.parse($($div).attr("data-basic"));
				//				    //获取指定节点的数据
				////				    var param = dtree.getParam("role_tree", dataBasic.roleId);
				//					//新增角色信息
				//					$("#addRole input[name='parentId']").val(dataBasic.roleId);
				//					$("#addRole input[name='parentRoleName']").val(dataBasic.roleName);
				//					layer.open({
				//						type: 1,
				//						offset: '10px',
				//						area: ['500px', '500px'],
				//						title: '新增角色',
				//						content: $("#addRole"),
				//						shade: [0.2, '#1D1E3E'],
				//						btn: ['提交', '取消'],
				//						btn1: function(index, layero) {
				//							var formData = form.val('addRole');
				//							if (formData.roleName == "") {
				//								layer.msg('角色名称不能为空！', {
				//									icon: 5
				//								});
				//								return;
				//							}
				//	
				//	
				//							$.ajax({
				//								type: "POST",
				//								url: context_path + "/role/addRole",
				//								data: formData,
				//								dataType: "json",
				//								success: function(result) {
				//	
				//									if (result.code == 0) {
				//	
				//										layer.msg(result.msg, {
				//											icon: 6
				//										});
				//										//成功添加角色后，清空表单数据
				//										form.val("addRole", { //addRole 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
				//											"roleName": "",
				//											"parentId": "",
				//											"sysCode": "",
				//											"remarks": ""
				//										});
				//										layer.close(index);
				//										//3秒后重载当前页面
				//										setTimeout(function() {
				//											document.location.reload()
				//										}, "3000");
				//									} else {
				//										layer.msg(result.msg, {
				//											icon: 5
				//										});
				//									}
				//	
				//								},
				//								error: function(e) {
				//									layer.msg(e.responseText, {
				//										icon: 2
				//									});
				//								}
				//	
				//							});
				//	
				//						},
				//						btn2: function(index, layero) {
				//							//return false;
				//						},
				//						cancel: function(layero, index) {
				//							layer.closeAll();
				//						}
				//	
				//					});
				//	
				//				},
				editTreeNode: function(treeNode, $div) { //修改职位节点信息
					//从节点的额外数据获取postId等信息
					let dataBasic = JSON.parse($($div).attr("data-basic"));
					let postId = dataBasic.postId;
					let parentName = dataBasic.parentName;
					let position = dataBasic.position;
					let icon = dataBasic.icon;
					form.val("updatePost", { //addOrganization 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
						"postId": postId,
						"postName": dataBasic.postName,
						"parentPostName": parentName === undefined ? "无" : parentName,
						"orgName": currentOrgName,
						"postCode": dataBasic.postCode,
						"position": dataBasic.position,
						"icon":icon
					});
					//图标回显
					$("#updatePost input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("i").attr("class",icon+" layui-ext-icon-selected-icon");
					$("#updatePost input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("div[class='layui-ext-icon-selected-name']").text($(iconNames).attr(icon));
					layer.open({
						type: 1,
						offset: '50px',
						area: ['550px', '450px'],
						title: '修改职位信息',
						content: $("#updatePost"),
						shade: [0.2, '#1D1E3E'],
						btn: ['提交', '取消'],
						btn1: function(index, layero) {
							var formData = form.val('updatePost');
							if (formData.postName == "") {
								layer.msg('职位名称不能为空！', {
									icon: 5
								});
								return;
							}

							$.ajax({
								type: "POST",
								url: context_path + "/post/updatePost",
								data: formData,
								dataType: "json",
								success: function(result) {
									if (result.code == 0) {
										layer.msg(result.msg, {
											icon: 6
										});
										//更新右侧的职位提示信息
										$("#currentPost").text(">" +
											formData.postName);
										currentPostId = formData.postId;
										currentPostName = formData.postName;
										//更新节点名称
										$($div).find("cite").text(formData
											.postName);
										//修改节点数据，方便回显时获取最新的值
										dataBasic.postName = formData
											.postName;
										$($div).attr("data-basic", JSON
											.stringify(dataBasic));
										//显示用户列表
										showUsers("post");
										if (position != formData
											.position||icon!=formData.icon) { //如果节点位置或图标被修改了，则刷新整个页面
											//节点位置已更改，3秒后重载当前页面
											setTimeout(function() {
												document.location
													.reload()
											}, "3000");
										}
										//									data.icon=formData.icon;
										layer.close(index);
									} else {
										layer.msg(result.msg, {
											icon: 5
										});

									}
								},
								error: function(e) {
									layer.msg(e.responseText, {
										icon: 2
									});
								}

							});

						},
						btn2: function(index, layero) {
							//return false;
						},
						cancel: function(layero, index) {
							layer.closeAll();
						}

					});

				},
				delTreeNode: function(treeNode, $div) {
					//从节点的额外数据获取postId等信息
					let dataBasic = JSON.parse($($div).attr("data-basic"));
					//删除职位
					let postId = dataBasic.postId;
					let postName = dataBasic.postName;
					layer.confirm('确定删除【'+postName+'】？', {
						icon: 3,
						title: '删除职位'
					}, function(index) {
						$.ajax({
							type: "POST",
							url: context_path + "/post/deletePost",
							data: {
							    postId: postId
							},
							dataType: "json",
							success: function(result) {
								if (result.code == 0) {
									layer.msg(result.msg, {
										icon: 6
									});

									//节点位置已更改，3秒后重载当前页面
									setTimeout(function() {
										document.location.reload()
									}, "3000");
								} else {
									layer.msg(result.msg, {
										icon: 5
									});
								}

							},
							error: function(e) {
								layer.msg(e.responseText, {
									icon: 2
								});
							}

						});
					});

				}
			}
		});
	}

	//渲染用户表格
	function showUsers(originFrom) { //originFrom 判断请求来源(组织结构、职位)：“org”、“post”
		requestUserUrl = "/user/queryUserByOrgIdPage";
		if (originFrom === "post") {
			requestUserUrl = "/user/queryUserByPostIdPage";
		}
		layui.use(['table'], function() {
			var table = layui.table;
			form = layui.form;
//			let laytpl = layui.laytpl;
//			//自定义layui模版引擎分割符，因为原来的{{与thymeleaf冲突了
//			laytpl.config({
//			    open: '<%',
//			    close: '%>'
//			  });
			//渲染用户表格
			tableIns = table.render({
				elem: '#user_table',
				//height: 'full-20', //高度最大化减去差值
				/* even: true,  */ //开启隔行背景
				toolbar: '#toolbarDemo', //开启头部工具栏，并为其绑定左侧模板
				url: context_path + requestUserUrl,
				page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
					layout: ['count', 'first', 'prev', 'page', 'next', 'skip', 'last',
					'limit'], //自定义分页布局
					//,curr: 5 //设定初始在第 5 页

					groups: 10, //只显示 5 个连续页码
					first: '首页', //显示首页
					prev: '上一页',
					next: '下一页',
					last: '尾页', //显示尾页
					limits: [5, 10, 20, 30, 50]

				},
				request: { //自定义请求中的分页参数名称
					pageName: 'currentPage', //页码的参数名称，默认：page
					limitName: 'pageSize' //每页数据量的参数名，默认：limit

				},
				where: { //额外的请求参数
					orgId: currentOrgId,
					postId: currentPostId,
					username: '',
					isEnable: ''
				},
				parseData: function(res) { //res 即为原始返回的数据
					return {
						"code": res.code, //解析接口状态
						"msg": res.msg, //解析提示文本
						"count": res.extraData.totalCount, //解析数据长度
						"data": res.data //解析数据列表
					};
				},
				cols: [
					[ //表头
						{
							field: 'userId',
							/*  templet: '#userIdCheckBox',  */
							title: '',
							type: 'checkbox',
							width: 50
						}, {
							field: 'userName',
							title: '用户名',
							width: 130,
							sort: false
						}, {
							field: 'cellPhoneNumber',
							title: '手机号',
							width: 130
						}, {
							field: 'postName',
							title: '职位',
							sort: false,
							width: 130,
							templet:function(d){
							    if(d.postName==undefined) {//点击组织、职位节点查询时的返回数据格式
								return d.post.postName;
							    }else {//成功新增用户时的返回数据格式
								return d.postName;
							    }
							    
							}
						}, {
							field: 'sex',
							title: '性别',
							sort: false,
							width:50,
							templet:function(d){
							    return d.sex==1?'男':'女';
							}
						}, {
							field: 'isEnable',
							title: '用户状态',
							templet: '#switchTpl',
							width: 110,
							unresize: true,
							sort: true
						}, {
							field: '',
							title: '操作',
							templet: '#barDemo',
							sort: false
						}
					]
				]
			});
			
			

			var checkedObjs = [];
			//监听复选框点击
			table.on('checkbox(user_table)', function(obj) {
//				checkedObjs.push(obj);
				if (obj.type!='all'&&obj.checked) {//type='all'，说明是直接点击“全选”复选框
					//筛选出被选中的Tr（行）
					checkedObjs.push(obj);
				} else {
					checkedObjs = checkedObjs.filter(function(checkedObj) {
						return checkedObj != obj;
					});

				}

				//	    console.log(obj); //当前行的一些常用操作集合
				//	    console.log(obj.checked); //当前是否选中状态
				//	    console.log(obj.data); //选中行的相关数据
				//	    console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one
			});

			//头工具栏事件
			table.on('toolbar(user_table)', function(obj) {
				//obj.config.id：当前表格的ID ，checkStatus：当前选中行的数据，checkStatus.data是数组
				var checkStatus = table.checkStatus(obj.config.id);
//				debugger;
				if (obj.event == 'addUser') {
					//没有选择职位（或职位不存在）且不是操作用户组数据
					if ($("#currentPostId").val() == '' && $("#currentGroupId").val() ==
						'' && $("#currentOrgId").val() != '') {
						layer.alert('请先选择【' + currentOrgName + "】中的职位或新增职位后再进行操作！", {
							icon: 5
						});
						return;
					}

					layer.open({
						type: 1,
						offset: '10px',
						area: ['800px', '500px'],
						title: '新增用户',
						content: $("#addUser"),
						shade: [0.2, '#1D1E3E'],
						btn: ['提交', '取消'],
						btn1: function(index, layero) {
							var tempStatus;
							var formData = form.val('addUser');


							if (formData.userName == "") {
								layer.msg('用户名不能为空！', {
									icon: 5
								});
								layer.close(index);
								return;
							}
							if (formData.isEnable == "on") {
								tempStatus = 1;
							} else {
								tempStatus = 0;
							}
							formData.isEnable = tempStatus;
							formData.postId = currentPostId;
							formData.groupId = $("#currentGroupId").val();
							formData.type = 'post';
							$.ajax({
								type: "POST",
								url: context_path + "/user/addUser",
								/* contentType: 'application/json; charset=UTF-8',  
								   contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 
								*/
								data: formData,
								dataType: "json",
								success: function(result) {
									//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
									//console.log(result);
									if (result.code == 0) {
										layer.msg('用户添加成功！', {
											icon: 6
										});
										layer.close(index);
										//在表格中插入一条新纪录
										//获取表格当前数据
										var oldData = table.cache[
											'user_table'];
										//新行数据
										var newRow = {
											userId: result.data
												.userId,
											userName: result.data
												.userName,
											cellPhoneNumber: result
												.data
												.cellPhoneNumber,
											address: result.data
												.address,
											email: result.data
												.email,
											isEnable: result.data
												.isEnable,
											postName: result.data.post.postName,
											sex: result.data.sex,	
											createTime: result.data
												.createTime,
											remarks: result.data
												.remarks
										};
										//往旧数组头部添加一个新元素
										oldData.unshift(newRow);
										//如果原table渲染时指定了数据url
										//重载时需要设置url: null
										//使用新数据重载表格
										tableIns.reload({
											url: null,
											data: oldData
										});
										//成功添加用户后，清空用户填写的表单数据
										form.val("addUser", { //userDetail 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
											"userName": "", // "name": "value"
											"email": "",
											"cellPhoneNumber": "",
											"userAlias": "",
											"address": "",
											"password": "",
											"remarks": ""
										});
										form.render();
										

									} else {
										layer.msg(result.msg, {
											icon: 5
										});
										form.render();
//										layer.close(index);
									}

								},
								error: function(e) {
									layer.msg(e.responseText, {
										icon: 2
									});
									//系统出错，清空用户填写的表单数据
									form.val("addUser", { //userDetail 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
										"userName": "", // "name": "value"
										"email": "",
										"cellPhoneNumber": "",
										"userAlias": "",
										"address": "",
										"password": "",
										"remarks": ""
									});
									form.render();
//									layer.close(index);
								}

							});

						},
						btn2: function(index, layero) {
							//return false;
						},
						cancel: function(layero, index) {
							layer.closeAll();
						}

					});

				} else if (obj.event == 'addPost') { //新增职位
					//				  //没有选择职位（或职位不存在）且不是操作用户组数据
					//					if ($("#currentPostId").val() == '' && $("#currentGroupId").val() ==
					//						'' && $("#currentOrgId").val() != '') {
					//							layer.alert('请先选择【' + currentOrgName + "】中的职位或新增职位后再进行操作！", {icon: 5}); 
					//							return;
					//					}
					$("input[name='orgId']").val(currentOrgId);
					$("input[name='orgName']").val(currentOrgName);
					if ($("#currentPost").text() == '') {
						$("input[name='parentId']").val("-1");
						$("input[name='parentPostName']").val("无");
					} else {
						$("input[name='parentId']").val(currentPostId);
						$("input[name='parentPostName']").val(currentPostName);
					}
					layer.open({
						type: 1,
						offset: '50px',
						area: ['500px', '450px'],
						title: '新增职位',
						content: $("#addPost"),
						shade: [0.2, '#1D1E3E'],
						btn: ['提交', '取消'],
						btn1: function(index, layero) {
							var formData = form.val('addPost');


							if (formData.postName == "") {
								layer.msg('职位名称不能为空！', {
									icon: 5
								});
								layer.close(index);
								return;
							}


							$.ajax({
								type: "POST",
								url: context_path + "/post/addPost",
								/* contentType: 'application/json; charset=UTF-8',  
								   contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 
								*/
								data: formData,
								dataType: "json",
								success: function(result) {
									//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
									//console.log(result);

									if (result.code == 0) {

										layer.msg(result.msg, {
											icon: 6
										});

										//成功添加职位后，清空职位名称、职位编码
										form.val("addPost", { //addPost 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
											"orgId": "",
											"orgName": "",
											"parentId": "",
											"parentPostName": "",
											"postName": "",
											"postCode": ""

										});
										//										form.render();
										layer.close(index);
										showPostTree(false);
									} else {
										layer.msg(result.msg, {
											icon: 5
										});
										//职位添加失败后，清空表单信息
										form.val("addPost", { //addPost 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
											"orgId": "",
											"orgName": "",
											"parentId": "",
											"parentPostName": "",
											"postName": "",
											"postCode": ""

										});
										layer.close(index);
									}

								},
								error: function(e) {
									layer.msg(e.responseText, {
										icon: 2
									});
									//职位添加失败后，清空表单信息
									form.val("addPost", { //addPost 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
										"orgId": "",
										"orgName": "",
										"parentId": "",
										"parentPostName": "",
										"postName": "",
										"postCode": ""

									});
									layer.close(index);
								}

							});

						},
						btn2: function(index, layero) {
							//return false;
						},
						cancel: function(layero, index) {
							layer.closeAll();
						}

					});
				} else if (obj.event == 'delSelectedUsers') { //批量删除用户
					let selectedDatas = checkStatus.data;
					let userIds = [];
					let userNames = [];
					//是否有超级管理员
					let hasSuperUser = false;
					if (selectedDatas.length != 0) {
						selectedDatas.forEach(item => {
							//获取所有被选中的行的userId，跳过超级管理员id
						    if(item.isSuper!=1) {//筛选非超级管理员的id
							userIds.push(item.userId);
							userNames.push(item.userName);
						    }else {
							hasSuperUser =true;
						    }
						});
					}
					if (userIds.length == 0 && !hasSuperUser) {
						layer.msg('请选择要删除的用户！', {
							icon: 5
						});
						layer.close(index);
						return;
					}
					
					layer.confirm('确定删除已选中用户？', {
						icon: 3,
						title: '批量删除用户'
					}, function(index) {
						$.ajax({
							type: "POST",
							url: context_path + "/user/delUsersById",
							data: {
								userIds: userIds
							},
							dataType: "json",
							success: function(result) {
								//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
								
								if (result.code == 0) {
									layer.msg(result.msg, {
										icon: 6
									});
									
									//checkedObjs： 被选中删除的Dom对象
									checkedObjs.forEach(obj => {
										obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
										
									});
									let oldData = table.cache['user_table'];
									tableIns.reload({ //删除记录，重载表格
										url: null,
										where: { //设定异步数据接口的额外参数，任意设

										},
										page: {
											curr: 1 //重新从第 1 页开始
										},
										data: oldData
									});
									
									//处理直接点击“全选”进行批量删除的情况，因为点击“全选”时，并不能获取到tr dom对象
									$("div[lay-id='user_table'] div[class='layui-table-body layui-table-main'] tbody").children("tr").each((index,item)=>{
									    let trDomObj=$(item);
									    let userName=trDomObj.find("td[data-field='userName']").find("div").text();
									    if(userNames.indexOf(userName)!=-1){//通过匹配第二列userName来删除tr dom结构，不更新缓存数据
									       trDomObj.remove();
									    }
									 });
									
									
								} else {
									layer.msg(result.msg, {
										icon: 5
									});
								}
							},
							error: function(e) {
								layer.msg('系统错误，数据删除失败！', {
									icon: 2
								});
							}

						});

					});

				} else {
					//点击其他工具条点击事件
				}
			});


			//监听用户状态按钮操作（修改用户可用状态）
			form.on('switch(userStatus)', function(obj) {
				/*  layer.tips(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis);  */
				var checkboxId = this.id;
				/* 	debugger;  */
				//用户批量处理 var checkStatus = table.checkStatus('user_table'); //idTest 即为基础参数 id 对应的值
				//console.log(checkStatus.data[0]); //获取选中行的数据
				//console.log(checkStatus.data.length) ;//获取选中行数量，可作为是否有选中行的条件
				//console.log(checkStatus.isAll ); //表格是否全选 
				var status = obj.elem.checked == true ? 1 : 0;

				/* var userId = checkStatus.data[0].userId; */ //获取被选中的行数据，适合批量处理
				/* console.log(JSON.stringify(checkStatus)); */

				$.ajax({
					type: "POST",
					url: context_path + "/user/updateUserStatus",
					contentType: 'application/json; charset=UTF-8',
					data: JSON.stringify({
						userId: this.id,
						isEnable: status
					}),
					dataType: "json",
					success: function(result) {
						if (result.code == 0) {
							layer.tips("更新成功！用户状态已变为：" + (obj.elem.checked == true ?
								"可用" :
								"禁用"), obj.othis);
						} else {
							if (obj.elem.checked == true) { //系统出错，重新渲染复选框状态为原值
								layer.tips("更新失败！当前用户状态：禁用", obj.othis);
								$("#" + checkboxId).prop('checked', false);
								form.render('checkbox');
							} else {
								layer.tips("更新失败！当前用户状态：可用", obj.othis);
								$("#" + checkboxId).prop('checked', true);
								form.render('checkbox');
							}
						}

					},
					error: function(e) {

						if (obj.elem.checked == true) { //系统出错，重新渲染复选框状态为原值
							layer.tips("系统出错，更新失败！当前用户状态：禁用", obj.othis);
							$("#" + checkboxId).prop('checked', false);
							form.render('checkbox');

						} else {
							layer.tips("系统出错，更新失败！当前用户状态：可用", obj.othis);
							$("#" + checkboxId).prop('checked', true);
							form.render('checkbox');
						}

						/* console.log(e.status);
						console.log(e.responseText); */
					}

				});
			});

			//用户信息表格 行工具条事件
			table.on('tool(user_table)', function(
				obj) { //注：tool 是工具条事件名，user_table 是 table 原始容器的属性 lay-filter="对应的值"
				var data = obj.data; //获得当前行数据
				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
				var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）
				if (layEvent === 'detail') { //查看
					layer.open({
						type: 1,
						offset: '10px',
						area: ['800px', '500px'],
						title: '用户信息',
						content: $("#userDetail"),
						shade: [0.2, '#1D1E3E'],
						btn: ['修改信息', '取消'],
						btn1: function(formIndex, layero) {
							layer.confirm('确定修改用户信息？', {
								icon: 3,
								title: '修改信息'
							}, function(index) {

								let formData = form.val("userDetail");
								let tempIsEnable = 0;
								if (formData.isEnable == 'on') {
									tempIsEnable = 1; //对应后台数据 1 或 0
								}
								formData.isEnable = tempIsEnable;
								$.ajax({
									type: "POST",
									url: context_path +
										"/user/updateUser",
									contentType: 'application/json; charset=UTF-8',
									//  contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 

									data: JSON.stringify(formData),
									dataType: "json",
									success: function(result) {
										//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
										if (result.code == 0) {
											layer.msg(result.msg, {
												icon: 6
											});
											//更新缓存及表格行数据
											obj.update({
												userName: formData
													.userName,
												cellPhoneNumber: formData
													.cellPhoneNumber,
												address: formData
													.address,
												email: formData
													.email,
												createTime: formData
													.createTime,
												isEnable: tempIsEnable
											});

											//关闭表单弹窗
											layer.close(
												formIndex);
										} else {
											layer.msg(
												result.msg, {
												icon: 5
											});
											layer.close(
												formIndex);
										}
									},
									error: function(e) {
										layer.msg(
											e.responseText, {
												icon: 2
											});
										layer.close(formIndex);
									}

								});
							});
						},
						btn2: function(index, layero) {
							//return false;
						},
						cancel: function(layero, index) {
							layer.closeAll();
						}

					});
					//用后台数据给表单赋值
					$.ajax({
						type: "POST",
						url: context_path + "/user/queryUserById",
						data: {
							userId: data.userId
						},
						dataType: "json",
						success: function(result) {
							//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
							if (result.code == 0) {
								//请求成功时处理
								form.val("userDetail", { //userDetail 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
									"userId": result.data.userId,
									"userName": result.data
									.userName, // "name": "value"
									"email": result.data.email,
									"cellPhoneNumber": result.data
										.cellPhoneNumber,
									"address": result.data.address,
									"entryDate": result.data.entryDate,
									"lastLogin": result.data.lastLogin,
									"userAlias": result.data.userAlias,
									"sex": result.data.sex,
									"createTime": result.data.createTime,
									"modifyTime": result.data.modifyTime,
									"isEnable": result.data.isEnable,
									"remarks": result.data.remarks
								});

								//根据后台的用户状态值，重新渲染用户状态复选框
								if (result.data.isEnable == 1) {
									$("form[id='userDetail'] input[name='isEnable']")
										.prop(
											'checked', true);
								} else {
									$("form[id='userDetail'] input[name='isEnable']")
										.prop(
											'checked', false);
								}
								//如果是超级管理员，则自身的可用状态不可修改
								if (result.data.isSuper == 1) {
								    
									$("form[id='userDetail'] input[name='isEnable']")
									.attr("disabled","disabled");
								} else {
									$("form[id='userDetail'] input[name='isEnable']")
										.removeAttr("disabled");
								}

								//重新渲染性别
								if (result.data.sex == 1) {
									$("form[id='userDetail'] input[name='sex'][value=1]")
										.prop(
											'checked', true);
								} else {
									$("form[id='userDetail'] input[name='sex'][value=0]")
										.prop(
											'checked', true);
								}
								form.render('checkbox');
								form.render('radio');
							} else {
								layer.msg('后台查询失败！', {
									icon: 5
								});
							}
						},
						error: function(e) {
							layer.msg('系统错误，数据查询失败！', {
								icon: 2
							});
							layer.close(index);
						}

					});

				} else if (layEvent === 'del') { //删除
					layer.confirm('确定删除用户 【 ' + data.userName + ' 】？', {
						icon: 3,
						title: '删除用户'
					}, function(index) {
						$.ajax({
							type: "POST",
							url: context_path + "/user/delUserById",
							data: {
								userId: data.userId
							},
							dataType: "json",
							success: function(result) {
								//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
								if (result.code == 0) {
									layer.msg('数据删除成功！', {
										icon: 6
									});
									obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
									var oldData = table.cache['user_table'];
									console.log(oldData.length);
									tableIns.reload({ //删除记录，重载表格
										url: null,
										where: { //设定异步数据接口的额外参数，任意设

										},
										page: {
											curr: 1 //重新从第 1 页开始
										},
										data: oldData
									})
									//请求成功时处理

									layer.close(index);
								} else {
									layer.msg('数据删除失败！', {
										icon: 5
									});
									layer.close(index);
								}
							},
							error: function(e) {
								layer.msg('系统错误，数据删除失败！', {
									icon: 2
								});
								layer.close(index);
							}

						});

					});
				} else if (layEvent === 'edit') { //分配组织
				    	let isSuper = data.isSuper;
				    	let userId = data.userId;
					layer.open({
						type: 1, //type:0 也行
						title: "将【" + data.userName + "】分配至以下组织？（作用：通过组织给用户分配角色）",
						area: ["550px", "80%"],
						content: '<div class="layui-row">' +
							'<div class="layui-col-md12 layui-col-lg12">' +
							'<ul id="popUpOrgTree" class="dtree" data-id="0"></ul>' +
							'</div>' +
							'</div>',
						btn: ['确认提交'],
						success: function(layero, index) {
							var popUpOrgTree = dtree.render({
								//  obj: $(layero).find("#openTree1"),    如果直接用elem加载不出来，则可以使用这个方式加载jquery的DOM
								elem: "#popUpOrgTree",
								checkbar: true,
								checkbarType: "self", // 默认就是all，其他的值为： no-all  p-casc   self  only
								width: "100%",
								accordion: false,
								line: true,
								skin:"authz",
								iconfont: ["layui-icon", "dtreefont",
									"iconfont"
								],
								url: context_path +
									"/organization/getOrgTree4User",
								request: {
									userId: userId
								}, 
								done: function(result, $ul, first){
								    if(isSuper=="1") {//如果是超级管理员,则禁用节点选择，直接全部选中
									//禁用全部节点
									popUpOrgTree.setDisabledAllNodes();
									
								    }
								   
								}
							});
							

							//				        // 绑定节点的双击
							//				        dtree.on("nodedblclick('popUpOrgTree')", function(obj){
							//				          $("#roleId").val(obj.param.nodeId);
							//				          $("#roleName").val(obj.param.context);
							//				        });
						},
						yes: function(index, layero) {
						    if(isSuper=="1") {//如果是超级管理员，则不用在重新分配组织
							layer.msg("不允许给超级管理员重新分配组织！", {
								icon: 5
							});
							layer.close(index);
							return;
						    }
						        let params = dtree.getCheckbarNodesParam(
								"popUpOrgTree");
							let checkedOrgIds = [];
							params.forEach(item => {
								checkedOrgIds.push(item.nodeId);
							});
							$.ajax({
								type: "POST",
								url: context_path +
									"/organization/updateUserOrgRelation",
								data: {
									userId: userId,
									isSuper: isSuper,
									checkedOrgIds: checkedOrgIds
								},
								dataType: "json",
								success: function(result) {
									if (result.code == 0) {
										layer.msg(result.msg, {
											icon: 6
										});

										layer.close(index);
									} else {
										layer.alert(result.msg, {
											icon: 5
										});
										layer.close(index);
									}
								},
								error: function(e) {
									layer.alert(e.responseText, {
										icon: 2
									});
								}

							});
						}
					});

				}else if (layEvent === 'reset_password') {//重置密码
				    	let userId = data.userId;
				    	let userName = data.userName;
				    	layer.confirm('确定重置密码为 88888888 ？', {
						icon: 3,
						title: '重置密码'
					}, function(index) {
						$.ajax({
							type: "POST",
							url: context_path + "/user/resetPassword",
							data: {
								userId:userId,
								userName:userName
							},
							dataType: "json",
							success: function(result) {
								if (result.code == 0) {
									layer.msg(result.msg, {
										icon: 6,
										time: 6000
									});
								} else {
									layer.msg(result.msg, {
										icon: 5
									});
								}

							},
							error: function(e) {
								layer.msg(e.responseText, {
									icon: 2
								});
							}

						});
					});
				}
			});


		});
	}
	/**
	 * 渲染分组用户表格
	 */
	function renderGroupUserTab() {
		groupUserTab = table.render({
			elem: '#group_users',
			url: context_path + "/user/getUserOrgByPage",
			page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
				layout: ['count', 'first', 'prev', 'page', 'next', 'skip', 'last',
				'limit'], //自定义分页布局
				//,curr: 5 //设定初始在第 5 页

				groups: 10, //只显示 5 个连续页码
				first: '首页', //显示首页
				prev: '上一页',
				next: '下一页',
				last: '尾页', //显示尾页
				limits: [5, 10, 20, 30, 50]

			},
			request: { //自定义请求中的分页参数名称
				pageName: 'currentPage', //页码的参数名称，默认：page
				limitName: 'pageSize' //每页数据量的参数名，默认：limit

			},
			where: { //额外的请求参数
				orgId: currentOrgId
			},
			parseData: function(res) { //res 即为原始返回的数据
				return {
					"code": res.code, //解析接口状态
					"msg": res.msg, //解析提示文本
					"count": res.extraData.totalCount, //解析数据长度
					"data": res.data //解析数据列表
				};
			},
			cols: [
				[ //表头
					{
						field: 'userName',
						title: '用户名',
						templet:function(userOrg){
						    return userOrg.user.userName;
						}
					}, {
						field: 'cellPhoneNumber',
						title: '手机号',
						sort: false,
						templet:function(userOrg){
						    return userOrg.user.cellPhoneNumber;
						}
					}, {
						field: 'email',
						title: '邮箱',
						templet:function(userOrg){
						    return userOrg.user.email;
						}
					}, {
						field: 'orgName',
						title: '所属组织',
						templet:function(userOrg){
						    return userOrg.organization.orgName;
						}
					}, {
						field: 'remarks',
						title: '备注',
						sort: false,
						templet:function(userOrg){
						    return userOrg.user.remarks;
						}
					}
				]
			]
		});
	}
	//点击查询按钮,重载用户数据表格
	$("#searchUser").click(function() {
		let formData = form.val('searchUserForm');
		tableIns.reload({ //重载表格
			url: context_path + requestUserUrl,
			where: { //设定异步数据接口的额外参数，任意设
				orgId: currentOrgId,
				postId: currentPostId,
				username: formData.username,
				isEnable: formData.isEnable
			},
			page: {
				curr: 1 //重新从第 1 页开始
			}

		});
	});
	//触发Tab卡片切换事件
		var active = {
			tabChange: function() {
				//切换到指定Tab项
				element.tabChange('demo', '22'); //切换到：用户管理
			}
		};
		//新增组织信息（根节点）
		$("#addRootOrg").click(function(){
		  
			$("#addOrganization input[name='parentId']").val("-1");
			$("#addOrganization input[name='parentOrgName']").val("无");
			layer.open({
				type: 1,
				offset: '10px',
				area: ['500px', '500px'],
				title: '新增组织',
				content: $("#addOrganization"),
				shade: [0.2, '#1D1E3E'],
				btn: ['提交', '取消'],
				btn1: function(index, layero) {
					var formData = form.val('addOrganization');
					if (formData.orgName == "") {
						layer.msg('组织名称不能为空！', {
							icon: 5
						});
						return;
					}

					$.ajax({
						type: "POST",
						url: context_path + "/organization/addOrganization",
						data: formData,
						dataType: "json",
						success: function(result) {

							if (result.code == 0) {

								layer.msg(result.msg, {
									icon: 6
								});
								//成功添加组织后，清空表单数据
								form.val("addOrganization", { //addOrganization 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
									"orgName": "",
									"parentId": "",
									"parentOrgName": "",
									"orgType": "",
									"orgCode": ""
								});
								layer.close(index);
								//3秒后重载当前页面
								setTimeout(function() {
									document.location.reload()
								}, "3000");
							} else {
								layer.msg(result.msg, {
									icon: 5
								});
							}

						},
						error: function(e) {
							layer.msg(e.responseText, {
								icon: 2
							});
						}

					});

				},
				btn2: function(index, layero) {
					//return false;
				},
				cancel: function(layero, index) {
					layer.closeAll();
				}

			});
		});

});
