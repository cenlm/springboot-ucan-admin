/**
 * @Description: 
 * @author liming.cen
 * @date 2023年1月6日 下午2:42:30
 * @Copyright: 2023 liming.cen. All rights reserved.
 */

layui.config({
	base: '../js/'
}).extend({
	dtree: 'layui_ext/dtree/dtree',
	iconSelected: "layui_ext/iconSelected/js/index",
	 numberInput: "layui_ext/numberInput/js/index",
}).use(['dtree', 'table', 'form', 'element','iconSelected',"numberInput"], function() {
	var dtree = layui.dtree;
	var iconSelected = layui.iconSelected;
	var table = layui.table;
	var element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
	var form = layui.form;
	//首次进行角色成员查询时，所需的roleId
	var currentRoleId = '';
	var currentRoleName = '';
	var currentRoleParentId = '';
	var currentOrgId = '';
	var currentOrgName = '';
	var currentPostId = '';
	var currentPostName = '';
	//父节点的parentId，用于角色的权限树回显判断
	var grandpaRoleId = '';
	var roleRemarks = '';
	//当前角色拥有的权限Id
	var currentRolesPerIds;
	//角色成员表格
	var tableIns;
	//互斥角色表格
	var mutexRolesTab;
	//角色的权限树
	var permissionTree4Role;
	var numberInput = layui.numberInput;
	 //数字插件
	 numberInput.render("#updateRole input[name='position']");
	 numberInput.render("#addRole input[name='position']");
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
//		                console.log("选中的图标数据", { event, data });
//		                console.log(data);
//		                $("#updateOrg input[id='icon']").val(data.icon);
		            }
		        },
		    });
		//弹窗全局样式
		layer.config({
		    skin: 'authz-class'
		  });
	//角色树的渲染
	var dTree = dtree.render({
		elem: "#role_tree",
		checkbar: false,
		checkbarType: "no-all", // 默认就是all，其他的值为： no-all  p-casc   self  only
		toolbar: true,
		width: "100%",
		accordion: false,
		line: true,
		skin:"authz",
		iconfont: ["layui-icon", "dtreefont", "iconfont"],
		url: context_path + "/role/getRoleTreeNodes", 
		success: function(data, obj, first) { //异步加载成功
			//		    console.log(data);
			//		    console.log(obj);
			//		    console.log(first);
			//		        debugger;
			currentRoleId = data.data[0].basicData.roleId;
			currentRoleName = data.data[0].basicData.roleName;
			currentRoleParentId = data.data[0].basicData.parentId;
			roleRemarks = data.data[0].basicData.remarks;
			//			console.log(roleRemarks);
			//标识当前操作的是哪个角色信息
			$("#currentRoleName").text(currentRoleName);
			$("#currentRoleRemarks").text((roleRemarks === undefined || roleRemarks === "") ?
				'暂无说明信息' : roleRemarks);
			$("#currentRoleId").val(currentRoleId);


		},
		done: function(res, $ul, first) { //异步加载完成
			//查询角色成员
			showRoleUsers();
			//渲染角色的权限信息树
			renderPermissionTree();
			//<角色-组织>树渲染
			renderRoleToOrgTree();
			//互斥角色列表渲染
			renderMutexRoleTab();
		},
		toolbarShow: ["add", "edit", "delete"], // 显示三个操作项
		toolbarFun: {
			addTreeNode: function(treeNode, $div) { //新增角色信息

				//从节点的额外数据获取roleId等信息
				let dataBasic = JSON.parse($($div).attr("data-basic"));
				//获取指定节点的数据
				//			    var param = dtree.getParam("role_tree", dataBasic.roleId);
				//新增角色信息
				$("#addRole input[name='parentId']").val(dataBasic.roleId);
				$("#addRole input[name='parentRoleName']").val(dataBasic.roleName);
				layer.open({
					type: 1,
					offset: '10px',
					area: ['500px', '550px'],
					title: '新增角色',
					content: $("#addRole"),
					shade: [0.2, '#1D1E3E'],
					btn: ['提交', '取消'],
					btn1: function(index, layero) {
						let formData = form.val('addRole');
						if (formData.roleName == "") {
							layer.msg('角色名称不能为空！', {
								icon: 5
							});
							return;
						}
						if (formData.roleCode == "") {
							layer.msg('角色编码不能为空！', {
								icon: 5
							});
							return;
						}

						$.ajax({
							type: "POST",
							url: context_path + "/role/addRole",
							data: formData,
							dataType: "json",
							success: function(result) {

								if (result.code == 0) {

									layer.msg(result.msg, {
										icon: 6
									});
									//成功添加角色后，清空表单数据
									form.val("addRole", { //addRole 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
										"roleName": "",
										"parentId": "",
										"sysCode": "",
										"remarks": ""
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
			editTreeNode: function(treeNode, $div) { //修改角色节点名称
				//从节点的额外数据获取roleId等信息
				let dataBasic = JSON.parse($($div).attr("data-basic"));
				let roleId = dataBasic.roleId;
				let roleName = dataBasic.roleName;
				let roleCode = dataBasic.roleCode;
				let parentName = dataBasic.parentName;
				let position = dataBasic.position;
				let isSuper =  dataBasic.isSuper;
				let icon = dataBasic.icon;
				form.val("updateRole", { //updateRole 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
					"roleId": roleId,
					"roleName": roleName,
					"roleCode": roleCode,
					"parentRoleName": parentName === undefined ? "无" : parentName,
					"position": position,
					"isSuper": isSuper,
					"remarks": dataBasic.remarks,
					"sysCode": dataBasic.sysCode,
					"icon": icon,
					
				});
				//图标回显
				$("#updateRole input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("i").attr("class",icon+" layui-ext-icon-selected-icon");
				$("#updateRole input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("div[class='layui-ext-icon-selected-name']").text($(iconNames).attr(icon));
				layer.open({
					type: 1,
					offset: '10px',
					area: ['550px', '550px'],
					title: '修改角色信息',
					content: $("#updateRole"),
					shade: [0.2, '#1D1E3E'],
					btn: ['提交', '取消'],
					btn1: function(index, layero) {
						var formData = form.val('updateRole');
						if (position === 0) { //超级管理员，则始终是0
							formData.position = 0;
						}
						if (formData.roleName == "") {
							layer.msg('角色名称不能为空！', {
								icon: 5
							});
							return;
						}
						
						if (formData.roleCode == "") {
							layer.msg('角色编码不能为空！', {
								icon: 5
							});
							return;
						}

						$.ajax({
							type: "POST",
							url: context_path + "/role/updateRole",
							data: formData,
							dataType: "json",
							success: function(result) {
								if (result.code == 0) {

									layer.msg(result.msg, {
										icon: 6
									});
									layer.close(index);
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
			delTreeNode: function(treeNode, $div) {
				//从节点的额外数据获取roleId等信息
				let dataBasic = JSON.parse($($div).attr("data-basic"));
				if(dataBasic.isSuper==1) {
				    layer.msg("不允许删除【"+dataBasic.roleName+"】角色！", {
					icon: 5
				});
				    return;
				}
				
				if(dataBasic.isSuper==2) {
				    layer.msg("不允许删除基础角色！", {
					icon: 5
				});
				    return;
				}
				
				//删除角色
				let roleId = dataBasic.roleId;
				let position = dataBasic.position;
				layer.confirm('确定删除？这会导致【' + dataBasic.roleName + '】及其所有子孙角色与组织架构、职位、权限、互斥角色信息解绑', {
					icon: 3,
					title: '删除角色'
				}, function(index) {
					$.ajax({
						type: "POST",
						url: context_path + "/role/deleteRole",
						data: {
							roleId: roleId,
							position: position
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
	var roleToOrgTree;
	var roleToPostTree;
	//角色管理模块：卡片组织架构树
	function renderRoleToOrgTree() {

		roleToOrgTree = dtree.render({
			elem: "#roleToOrgTree",
			checkbar: true,
			checkbarType: "all", // 默认就是all，其他的值为： no-all  p-casc   self  only
			width: "100%",
			accordion: false,
			line: true,
			skin:"authz",
			iconfont: ["layui-icon", "dtreefont", "iconfont"],
			url: context_path + "/organization/getRoleToOrgTree",
			request: {
				roleId: currentRoleId
			},
			success: function(data, obj, first) {
				//根节点不允许删除操作（移除节点删除图标）
				//			$($("#org_tree div[class='layui-tree-entry'] div[class='layui-btn-group layui-tree-btnGroup']")[0]).find("i[data-type='del']").remove();
				currentOrgId = data.data[0].id;
				currentOrgName = data.data[0].title;
				//标识当前操作的是哪个组织结构
				$("#orgsPost").text("【" + currentOrgName + "】");
				//用于在新增职位、新增用户做判断
				$("#currentOrgId").val(currentOrgId);
				$("#currentPostId").val("");
				$("#currentGroupId").val("");
			},
			done: function(res, $ul, first) {
				//显示职位信息树
				showPostTree(false);

			},
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
								url: context_path +
									"/organization/addOrganization",
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
											document.location
												.reload()
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
					//			    var param = dtree.getParam("org_tree", dataBasic.orgId);
					let orgId = dataBasic.orgId;
					let parentName = dataBasic.parentName;
					let position = dataBasic.position;
					form.val("updateOrg", { //addOrganization 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
						"orgId": orgId,
						"orgName": dataBasic.orgName,
						"parentOrgName": parentName === undefined ? "无" : parentName,
						"orgType": dataBasic.orgType,
						"position": dataBasic.position,
						"orgCode": dataBasic.orgCode
					});


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
							if (position === 0) {
								formData.position = 0;
							}
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
										$("#currentOrg").text(formData
											.orgName);
										//更新节点名称
										$($div).find("cite").text(formData
											.orgName);
										currentOrgId = formData.orgId;
										currentOrgName = formData.orgName;
										//修改节点数据，方便回显时获取最新的值
										dataBasic.orgName = formData
											.orgName;
										$($div).attr("data-basic", JSON
											.stringify(dataBasic));
										//显示职位信息树
										showPostTree(true);
										//显示用户列表
										showUsers("org");
										if (position != formData
											.position
										) { //如果节点位置被修改了，则刷新整个页面
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
				}
			}

		});
	}


	/**
	 * 卡片职位树的渲染
	 * calledFromOrgClick 是否通过点击组织结构树来查询职位？
	 */
	function showPostTree(calledFromOrgClick) { //判断showPostTree方法的调用来源：true 组织节点点击、false 页面首次加载
		//职位信息树的渲染
		roleToPostTree = dtree.render({
			elem: "#roleToPostTree",
			checkbar: true,
			checkbarType: "self", // 默认就是all，其他的值为： no-all  p-casc   self  only
			width: "100%",
			accordion: false,
			line: true,
			skin:"authz",
			iconfont: ["layui-icon", "dtreefont", "iconfont"],
			url: context_path + "/post/getRoleToPostTree",
			request: {
				orgId: currentOrgId,
				roleId: currentRoleId
			},
			success: function(data, obj, first) {
				//显示职位节点名称
				if (calledFromOrgClick) {
					$("#currentPost").text("");
				} else {
//					currentPostId = data.data[0].id;
//					currentPostName = data.data[0].title;
					   let postData = data.data[0];
					        if(postData!=undefined) {
					            currentPostId = postData.id;
						    currentPostName = postData.title;
					        }
				}
			},
			done: function(res, $ul, first) {}
		});
	}


	dtree.on("node('role_tree')", function(obj) { //触发节点点击事件
		//父节点的parentId，用于角色的权限树回显判断
		grandpaRoleId = obj.parentParam.parentId;
		//节点被点击时触发的事件
		let nodeId = obj.param.nodeId;
		let name = obj.param.context;
		let remarks = obj.param.basicData.remarks;
		currentRoleName = name;
		currentRoleParentId = obj.param.basicData.parentId;
		roleRemarks = remarks;
		//标识当前操作的是哪个角色信息
		$("#currentRoleName").text(currentRoleName);
		$("#currentRoleRemarks").text((roleRemarks === undefined || roleRemarks === "") ?
			'暂无说明信息' : roleRemarks);
		$("#currentRoleId").val(nodeId);

		tableIns.reload({ //重载角色成员表格
			url: context_path + "/role/queryRoleUsersByPage",
			where: { //设定异步数据接口的额外参数，任意设
				roleId: nodeId
			},
			page: {
				curr: 1 //重新从第 1 页开始
			}

		});
		
		currentRoleId = nodeId;
		currentRoleParentId = obj.param.basicData.parentId;
		renderPermissionTree();
		renderRoleToOrgTree();
		
		mutexRolesTab.reload({ //重载互斥角色表格
			url: context_path + "/role/getMutexRolesByPage",
			where: { //设定异步数据接口的额外参数，任意设
				roleId: nodeId
			},
			page: {
				curr: 1 //重新从第 1 页开始
			}

		});


	});
        //重载互斥角色查询下拉树参数
	$("#resetSearchCondition").click(function(){
	    dtree.reload("mutexRoleSelect",{
		    url: context_path + "/role/getRoleTreeNodes",
		    selectTips: "--------------------请选择角色---------------------"
		  });
	    });
	
	dtree.on("node('roleToOrgTree')", function(obj) { //组织树节点点击事件
		let id = obj.param.nodeId;
		let name = obj.param.context;
		currentOrgId = id;
		currentOrgName = name;
		//标识当前操作的是哪个组织结构
		$("#orgsPost").text("【" + currentOrgName + "】");
		//用于在新增职位、新增用户做判断
		$("#currentOrgId").val(id);
		$("#currentPostId").val("");
		$("#currentGroupId").val("");
		//显示职位信息树
		showPostTree(true);
		//		//显示用户列表
		//		showUsers("org");
		//点击组织结构节点时，职位提示信息置空（页面首次加载时显示，如果有查到职位信息）
		//点击职位信息节点时才显示
		$("#currentPost").text("");
	});
	
	dtree.renderSelect({
	    elem: "#mutexRoleSelect",
	    skin:"authz",
	    url:context_path + "/role/getRoleTreeNodes",
	    selectTips: "--------------------请选择角色---------------------"
	  });

	//分配角色到组织/职位
	$("#updRoleOrgPostRelation").click(function() {
		//获取组织树已选中的节点参数数据
		let checkedOrgParams = roleToOrgTree.getCheckbarNodesParam();
		let checkedOrgIds = [];
		if (checkedOrgParams.length > 0) { //筛选出orgId
			checkedOrgParams.forEach(item => {
				checkedOrgIds.push(item.nodeId);
			});
		}
		//获取职位树已选中的节点参数数据
		let checkedPostParams = roleToPostTree.getCheckbarNodesParam();
		let checkedPostIds = [];
		if (checkedPostParams.length > 0) { //筛选出postId
			checkedPostParams.forEach(item => {
				checkedPostIds.push(item.nodeId);
			});
		}
		layer.confirm('确定将角色【' + currentRoleName + '】分配至组织、职位？', {
			icon: 3,
			title: '角色分配'
		}, function(index) {
			$.ajax({
				type: "POST",
				url: context_path + "/role/updateRoleOrgPostRelation",
				data: {
				    	orgId:currentOrgId,
					roleId: currentRoleId,
					checkedOrgIds: checkedOrgIds,
					checkedPostIds:checkedPostIds
				},
				dataType: "json",
				success: function(result) {
					if (result.code == 0) {
					    layer.alert(result.msg, {
						icon: 6
					});
					} else {
					    layer.alert(result.msg, {
						icon: 5
					});
					}
					layer.close(index);
				},
				error: function(e) {
//					layer.msg(e.responseText, {
//						icon: 2
//					});
					    layer.alert(e.responseText, {
						icon:2
					});
				}

			});
		});
	});
	
	//触发Tab卡片切换事件
	var active = {
		tabChange: function() {
			//切换到指定Tab项
			element.tabChange('demo', '22'); //切换到：用户管理
		}
	};

	//“权限设置”面板中的权限树，对应角色拥有的权限打勾
	function showSelectedPermission() {

		$.ajax({
			type: "POST",
			url: context_path + "/role/getPermissionIdsByRoleId",
			data: {
				roleId: currentRoleId,
				parentId: currentRoleParentId
			},
			dataType: "json",
			success: function(result) {
				//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
				if (result.code == 0) {
					currentRolesPerIds = result.data;
					currentRolesPerIds.forEach(id => {
						//					    console.log(id);
						dtree.click(permissionTree4Role, id);
					});

					//					tree.setChecked('permissionTree', result.data);
					form.render('checkbox');
				} else {
					currentRolesPerIds = result.data;
				}
			},
			error: function(e) {
				console.log("系统出错！");
			}

		});
	}
	
	//更新<角色-权限>信息
	form.on('submit(updateRolePermission)', function(data) {
		//保存遍历后得到的权限Id
		let checkedIds = [];
		//获得选中的节点
		let checkData = dtree.getCheckbarNodesParam("permissionTree");
		if (checkData.length > 0) {
			getAllIds(checkData, checkedIds);
		}
		layer.confirm('确定调整【' + currentRoleName + '】的权限？如果有权限被撤销，将导致该节点的所有子孙节点的部分权限会被撤销，原因：子角色不能越权。', {
			icon: 3,
			title: '角色权限调整'
		}, function(index) {
			//角色重新分配权限并渲染节点复选框
			$.ajax({
				type: "POST",
				url: context_path + "/role/updateRolePermissionRelation",
				data: {
					roleId: currentRoleId,
					checkedIds: checkedIds
				},
				dataType: "json",
				success: function(result) {
					if (result.code == 0) {
						layer.msg(result.msg, {
							icon: 6
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




		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});

	
	
//	form.on('submit(updateRolePermission)', function(data) {
//		//保存遍历后得到的权限Id
//		let checkedIds = [];
//		//获得选中的节点
//		let checkData = dtree.getCheckbarNodesParam("permissionTree");
//		if (checkData.length > 0) {
//			getAllIds(checkData, checkedIds);
//		}
//		layer.confirm('确定调整【' + currentRoleName + '】的权限？如果有权限被撤销，将导致该节点的所有子孙节点的部分权限会被撤销，原因：子角色不能越权。', {
//			icon: 3,
//			title: '角色权限调整'
//		}, function(index) {
//			//角色重新分配权限并渲染节点复选框
//			$.ajax({
//				type: "POST",
//				url: context_path + "/role/updateRolePermissionRelation",
//				data: {
//					roleId: currentRoleId,
//					checkedIds: checkedIds
//				},
//				dataType: "json",
//				success: function(result) {
//					if (result.code == 0) {
//						layer.msg(result.msg, {
//							icon: 6
//						});
//					} else {
//						layer.msg(result.msg, {
//							icon: 5
//						});
//					}
//				},
//				error: function(e) {
//					layer.msg(e.responseText, {
//						icon: 2
//					});
//				}
//
//			});
//		});
//		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
//	});

	//通过递归的方式，从已选节点的Json数据中获取所有要提交的权限Id
	function getAllIds(checkData, idContainer) {
		if (checkData.length > 0) {
			checkData.forEach(item => {
				idContainer.push(item.nodeId);
				//				let children = item.children;
				//				//如果当前节点有子节点
				//				if (children.length > 0) {
				//					getAllIds(children, idContainer);
				//				}
			});
		}
	}
	//新建根角色
	$("#addRootRole").click(function() {
		$("#addRole input[name='parentId']").val("-1");
		$("#addRole input[name='parentRoleName']").val("无");

		layer.open({
			type: 1,
			offset: '10px',
			area: ['550px', '550px'],
			title: '新增角色',
			content: $("#addRole"),
			shade: [0.2, '#1D1E3E'],
			btn: ['提交', '取消'],
			btn1: function(index, layero) {
				var formData = form.val('addRole');
				if (formData.roleName == "") {
					layer.msg('角色名称不能为空！', {
						icon: 5
					});
					return;
				}
				
				if (formData.roleCode == "") {
					layer.msg('角色编码不能为空！', {
						icon: 5
					});
					return;
				}


				$.ajax({
					type: "POST",
					url: context_path + "/role/addRole",
					data: formData,
					dataType: "json",
					success: function(result) {

						if (result.code == 0) {

							layer.msg(result.msg, {
								icon: 6
							});
							//成功添加角色后，清空表单数据
							form.val("addRole", { //addRole 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
								"roleName": "",
								"parentId": "",
								"sysCode": "",
								"remarks": ""
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

	//	// 树节点文字被点击的时候，修改文字样式
	//	$("#role_tree").click(function(e) { // 在页面任意位置点击而触发此事件
	//		if ($(e.target).attr('class') === "t-click ") { // 防止因为点击展开按钮把已选中的样式取消
	//			$(".t-click").removeClass("tree-txt-active"); // 移除点击样式
	//			$(e.target).addClass("tree-txt-active"); // e.target表示被点击的目标
	//		}
	//	});

	//查询角色成员
	function showRoleUsers() {
		//渲染用户表格
		tableIns = table.render({
			elem: '#user_table',
			//height: 'full-20', //高度最大化减去差值
			/* even: true,  */ //开启隔行背景
			toolbar: '#toolbarDemo', //开启头部工具栏，并为其绑定左侧模板
			url: context_path + "/role/queryRoleUsersByPage",
			page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
				layout: ['count', 'first', 'prev', 'page', 'next', 'skip', 'last', 'limit'], //自定义分页布局
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
				roleId: currentRoleId
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
						sort: true,
						templet:function(userRole){
						    return userRole.user.userName;
						}
					},  {
						field: 'sex',
						title: '性别',
						templet:function(userRole){
						    return userRole.user.sex==1?'男':'女';
						}
					},{
						field: 'cellPhoneNumber',
						title: '手机号',
						templet:function(userRole){
						    return userRole.user.cellPhoneNumber;
						}
					}, {
						field: 'email',
						title: '邮箱',
						sort: false,
						templet:function(userRole){
						    return userRole.user.email;
						}
					}, {
						field: 'isSuper',
						title: '备注',
						templet:function(userRole){
						    return userRole.user.isSuper==1?'超级管理员':'普通用户';
						}
					}
//					, {
//						field: '',
//						title: '操作',
//						templet: '#barDemo',
//						sort: false
//					}
				]
			]

		});

		//标识当前操作的是哪个角色信息
		$("#currentRoleName").text(currentRoleName);
		$("#currentRoleRemarks").text((roleRemarks === undefined || roleRemarks === "") ? '暂无说明信息' :
			roleRemarks);


		var checkedObjs = [];
		//监听复选框点击
		table.on('checkbox(user_table)', function(obj) {
			checkedObjs.push(obj);
			/* console.log("长度："+checkedObjs.length);*/
			if (obj.checked) {
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
			if (obj.event == 'addUser') {
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
									//在表格中插入一条新纪录
									//获取表格当前数据
									var oldData = table.cache['user_table'];
									//新行数据
									var newRow = {
										userId: result.data.userId,
										userName: result.data.userName,
										cellPhoneNumber: result.data
											.cellPhoneNumber,
										address: result.data.address,
										email: result.data.email,
										isEnable: result.data.isEnable,
										isSuper: result.data.isSuper,
										createTime: result.data.createTime,
										remarks: result.data.remarks
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
									if (result.data.isSuper == 1) {
										$("input[id='" + result.data.userId +
											"']").prop(
											'disabled', true);
									} else {
										$("input[id='" + result.data.userId +
											"']").prop(
											'disabled', false);
									}
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
									layer.close(index);
								} else {
									layer.msg('用户添加失败！', {
										icon: 5
									});
									//用户添加失败，清空用户填写的表单数据
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
									layer.close(index);
								}
							},
							error: function(e) {
								layer.msg('系统错误，用户添加失败！', {
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
				//批量删除用户
			} else if (obj.event == 'delSelectedUsers') {
				var selectedDatas = checkStatus.data;
				var userIds = [];
				if (selectedDatas.length != 0) {
					selectedDatas.forEach(item => {
						//获取所有被选中的行的userId
						userIds.push(item.userId);
					});
				}
				if (userIds.length == 0) {
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
						/* contentType: 'application/json; charset=UTF-8',  
						   contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 
						*/
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
								/*var oldData = table.cache['user_table'];
								tableIns.reload({//删除记录，重载表格
								    url:null,
								    where: { //设定异步数据接口的额外参数，任意设
								    
								    }
								    ,page: {
								      curr: 1 //重新从第 1 页开始
								    },
								    data:oldData
								  });*/

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
						layer.tips("更新成功！用户状态已变为：" + (obj.elem.checked == true ? "可用" :
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
								url: context_path + "/user/updateUser",
								contentType: 'application/json; charset=UTF-8',
								//  contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 

								data: JSON.stringify(formData),
								dataType: "json",
								success: function(result) {
									//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
									if (result.code == 0) {
										layer.msg('数据修改成功！', {
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
										//修改用户为超级管理员后，表格中的用户状态将变为不可点击
										if (formData.isSuper == 1) {
											$("input[id=" + formData
												.userId + "]").prop(
												'disabled', true);
										} else {
											$("input[id=" + formData
												.userId + "]").prop(
												'disabled', false);
											$("input[id=" + formData
													.userId + "]")
												.next().removeClass(
													"layui-checkbox-disabled"
												);
											$("input[id=" + formData
													.userId + "]")
												.next().removeClass(
													"layui-disabled");
										}
										form.render('checkbox');

										//关闭表单弹窗
										layer.close(formIndex);
									} else {
										layer.msg('数据修改失败！', {
											icon: 5
										});
										layer.close(formIndex);
									}
								},
								error: function(e) {
									layer.msg('系统错误，数据修改失败！', {
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
					/* contentType: 'application/json; charset=UTF-8',  
					   contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 
					*/
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
								"userName": result.data.userName, // "name": "value"
								"email": result.data.email,
								"cellPhoneNumber": result.data.cellPhoneNumber,
								"address": result.data.address,
								"entryDate": result.data.entryDate,
								"lastLogin": result.data.lastLogin,
								"userAlias": result.data.userAlias,
								"isSuper": result.data.isSuper,
								"sex": result.data.sex,
								"createTime": result.data.createTime,
								"modifyTime": result.data.modifyTime,
								"isEnable": result.data.isEnable,
								"remarks": result.data.remarks
							});

							//console.log(data.isEnable);
							//根据后台的用户状态值，重新渲染用户状态复选框
							if (result.data.isEnable == 1) {
								$("form[id='userDetail'] input[name='isEnable']").prop(
									'checked', true);
							} else {
								$("form[id='userDetail'] input[name='isEnable']").prop(
									'checked', false);
							}

							if (result.data.isSuper == 1) {
								$("form[id='userDetail'] input[name='isEnable']").prop(
									'disabled', true);
							} else {
								$("form[id='userDetail'] input[name='isEnable']").prop(
									'disabled', false);
							}
							//重新渲染性别
							if (result.data.sex == 1) {
								$("form[id='userDetail'] input[name='sex'][value=1]").prop(
									'checked', true);
							} else {
								$("form[id='userDetail'] input[name='sex'][value=0]").prop(
									'checked', true);
							}
							//重新渲染“是否超级管理员”
							if (result.data.isSuper == 1) {
								$("form[id='userDetail'] input[name='isSuper'][value=1]")
									.prop(
										'checked', true);
							} else {
								$("form[id='userDetail'] input[name='isSuper'][value=0]")
									.prop(
										'checked', true);
							}
							form.render('checkbox');
							form.render('radio');
						} else {
							layer.msg('后台查询失败！', {
								icon: 5
							});
							layer.close(index);
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
						/* contentType: 'application/json; charset=UTF-8',  
						   contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 
						*/
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
			} else if (layEvent === 'edit') { //编辑

				//同步更新缓存对应的值
				/*  obj.update({
				   username: '123'
				   ,title: 'xxx'
				 }); */
			}
		});

	}
	//渲染互斥角色表格
	function renderMutexRoleTab() {
		
		mutexRolesTab = table.render({
			elem: '#mutexRoleTab',
			//height: 'full-20', //高度最大化减去差值
			url: context_path + "/role/getMutexRolesByPage",
			page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
				layout: ['count', 'first', 'prev', 'page', 'next', 'skip', 'last', 'limit'], //自定义分页布局
				//,curr: 5 //设定初始在第 5 页

				groups: 10, //只显示 5 个连续页码
				first: '首页', //显示首页
				prev: '上一页',
				next: '下一页',
				last: '尾页', //显示尾页
				limits: [10, 20, 30, 50]

			},
			request: { //自定义请求中的分页参数名称
				pageName: 'currentPage', //页码的参数名称，默认：page
				limitName: 'pageSize' //每页数据量的参数名，默认：limit

			},
			where: { //额外的请求参数
				roleId: ''
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
					field: 'roleId',
					hide: true,
					sort: true
				},
					{
						field: 'roleName',
						title: '角色名称',
						sort: true
					}, 
					{
						field: 'mutexRoleId',
						hide: true,
						sort: true
					},
					{
						field: 'mutexRoleName',
						title: '互斥角色'
					}, {
						field: 'sysCode',
						title: '系统编码',
						sort: false
						
					}, {
						field: '',
						title: '操作',
						templet: '#delMutexRelation',
						sort: false
					}
				]
			]

		});
		
		//点击查询按钮,重载互斥角色数据表格
		$("#searchMutexRolesButton").click(function() {
			let formData = form.val('searchMutexRolesForm');
			mutexRolesTab.reload({ //重载表格
				url: context_path + "/role/getMutexRolesByPage",
				where: { //设定异步数据接口的额外参数，任意设
				    roleId:formData.mutexRoleSelect_select_nodeId
				},
				page: {
					curr: 1 //重新从第 1 页开始
				}

			});
		});
		
		//点击互斥角色 新增 按钮
		$("#addMutexRolesButton").click(function(){
		    layer.open({
		      type: 1,  //type:0 也行
		      title: "新增互斥角色",
		      area: ["800px", "80%"],
		      content: '<div class="layui-row">'+
				'<div class="layui-col-md12 layui-col-lg12">'+
			'<div class="layui-inline">'+
				'<div class="layui-input-inline" style="margin:20px 50px;">'+
					'<input type="hidden" name="roleId" id="roleId" />'+
					'<input type="text" name="roleName" placeholder="请选择互斥角色1"'+
						'class="layui-input layui-disabled" autocomplete="off" style="width:300px;" id="roleName" disabled>'+
				'</div>'+
				'<div class="layui-input-inline" style="margin:20px 10px;">'+
					'<input type="hidden" name="mutexRoleId" id="mutexRoleId" />'+
					'<input type="text" name="mutexRoleName" placeholder="请选择互斥角色2"'+
						'class="layui-input layui-disabled" autocomplete="off" id="mutexRoleName" style="width:300px;" disabled>'+
				'</div>'+
			'</div>'+
		'</div>'+
		'<div class="layui-col-md6 layui-col-lg6">'+
			'<ul id="role1" class="dtree" data-id="0"></ul>'+
		'</div>'+
		'<div class="layui-col-md6 layui-col-lg6">'+
			'<ul id="role2" class="dtree" data-id="0"></ul>'+
		'</div>'+
	'</div>',
		      btn: ['确认提交'],
		      success: function(layero, index){
		        var roleTree1 = dtree.render({
		        //  obj: $(layero).find("#openTree1"),    如果直接用elem加载不出来，则可以使用这个方式加载jquery的DOM
		          elem: "#role1",
		          width: "100%",
				accordion: false,
				line: true,
				skin:"authz",
				iconfont: ["layui-icon", "dtreefont", "iconfont"],
		          url: context_path +  "/role/getRoleTreeNodes",
		        });
		        var roleTree2 = dtree.render({
			          elem: "#role2",
			          width: "100%",
					accordion: false,
					line: true,
					skin:"authz",
					iconfont: ["layui-icon", "dtreefont", "iconfont"],
			          url: context_path +  "/role/getRoleTreeNodes",
			        });
		        // 绑定节点的双击
		        dtree.on("nodedblclick('role1')", function(obj){
		          $("#roleId").val(obj.param.nodeId);
		          $("#roleName").val(obj.param.context);
		        });
		        // 绑定节点的双击
		        dtree.on("nodedblclick('role2')", function(obj){
		          $("#mutexRoleId").val(obj.param.nodeId);
		          $("#mutexRoleName").val(obj.param.context);
		        });
		      },
		      yes: function(index, layero) {
//		        var param = dtree.getNowParam("role1"); // 获取当前选中节点
		        let roleId=$("#roleId").val();
		        let mutexRoleId=$("#mutexRoleId").val();
		        if(roleId==""||mutexRoleId=="") {
		            layer.msg('请选择互斥角色！', {
				icon: 5
			});
			return;
		        }else if(roleId==mutexRoleId) {
		            layer.msg('互斥角色不能相同！', {
		       				icon: 5
		       			});
		       			return;
		        }
		        $.ajax({
				type: "POST",
				url: context_path + "/role/addMutexRole",
				data: {
					roleId: roleId,
					mutexRoleId:mutexRoleId
				},
				dataType: "json",
				success: function(result) {
					if (result.code == 0) {
						layer.msg(result.msg, {
							icon: 6
						});
						mutexRolesTab.reload({ //重载表格
							url: context_path + "/role/getMutexRolesByPage",
							where: { //设定异步数据接口的额外参数，任意设
							    roleId:""
							},
							page: {
								curr: 1 //重新从第 1 页开始
							}
						});

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
		      }
		    });
		  });


		//互斥信息表格 行工具条事件
		table.on('tool(mutexRoleTab)', function(
			obj) { //注：tool 是工具条事件名，mutexRoleTab 是 table 原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）
			if (layEvent === 'del') { //删除
				layer.confirm('删除 【 ' + data.roleName + '--'+data.mutexRoleName+' 】互斥角色？', {
					icon: 3,
					title: '删除互斥角色'
				}, function(index) {
					$.ajax({
						type: "POST",
						url: context_path + "/role/deleteMutexRole",
						data: {
							roleId: data.roleId,
							mutexRoleId:data.mutexRoleId
						},
						dataType: "json",
						success: function(result) {
							if (result.code == 0) {
								layer.msg('数据删除成功！', {
									icon: 6
								});
								obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
								var oldData = table.cache['mutexRolesTab'];

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
			} 
		});

	
	}
	
	//渲染“角色管理==>权限设置”中的权限树
	function renderPermissionTree() {
		//角色的权限树的渲染
		permissionTree4Role = dtree.render({
			elem: "#permissionTree",
			checkbar: true,
			checkbarType: "self", // 默认就是all，其他的值为： no-all  p-casc   self  only
			toolbar: false,
			width: "100%",
			accordion: false,
			line: true,
			skin:"authz",
			iconfont: ["layui-icon", "dtreefont", "iconfont"],
			request: {
				roleId: currentRoleId,
				parentId: currentRoleParentId,
				grandpaRoleId: grandpaRoleId
			},
			url: context_path + "/permission/getPermissionTree4Role",
			success: function(data, obj, first) {},
			done: function(res, $ul, first) { //异步加载完成

			}
		});
	}
	
//点击任何位置关闭角色下拉树
$("body").on("click", function(event){
  $("div[dtree-id][dtree-select]").removeClass("layui-form-selected");
  $("div[dtree-id][dtree-card]").removeClass("dtree-select-show layui-anim layui-anim-upbit");
});

});
