/**
 * @Description: 
 * @author liming.cen
 * @date 2023年1月6日 下午2:42:30
 * @Copyright: 2023 liming.cen. All rights reserved.
 */
   layui.config({
        base: '../js/'
    }).extend({
        treeTable: 'layui_ext/treeTable/treeTable',
        dtree: 'layui_ext/dtree/dtree',
        iconSelected: "layui_ext/iconSelected/js/index",
        numberInput: "layui_ext/numberInput/js/index",
    }).use(['layer', 'util','form', 'treeTable','dtree','iconSelected','numberInput'], function () {
	 var layer = layui.layer;
	 var util = layui.util;
	 var table = layui.treeTable;
	 var iconSelected = layui.iconSelected;
	 var form = layui.form;
	 var dtree = layui.dtree;
	 var numberInput = layui.numberInput;
	 //数字插件
	 numberInput.render("#addPermission input[id='position']");
	 numberInput.render("#permissionDetail input[id='position']");
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
//			                console.log("选中的图标数据", { event, data });
//			                console.log(data);
//			                $("#updateOrg input[id='icon']").val(data.icon);
			            }
			        },
			    });
			
			//弹窗全局样式
			layer.config({
			    skin: 'authz-class'
			  });
	//渲染权限表格
	var permissionTab = table.render({
		elem: '#permission_table',
		height: 'full-20', //高度最大化减去差值
		/* even: true,  */ //开启隔行背景
		toolbar: '#toolbarDemo1', //开启头部工具栏，并为其绑定左侧模板
		text: {
		        none: '<div style="padding: 18px 0;">哎呀，一条数据都没有~</div>'
		    },
		
		tree: {
	                iconIndex: 1, //折叠图标显示在第几列
	                idName: 'permissionId',  // 自定义id字段的名称
	                childName: 'children',
	                getIcon: function(d) {  // 自定义图标
		                // d是当前行的数据
		                    return '<i class="layui-icon '+d.icon+'" style="font-size: 16px; color: #433E88;padding: 5px 5px;"></i>';
		            }
	            },
		
		reqData: function(data, callback) {
	        	$.ajax({
				type: "GET",
				url: context_path + "/permission/getPermissionTreeNodes",
				//  contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 
				/*data: JSON.stringify(formData),*/
				dataType: "json",
				success: function(result) {
				    if(result.data.length==0) {
					layer.msg('没有查询到数据！', {
						icon: 2
					});
				    }else {
					
					callback(result.data);
				    }
				},
				error: function(e) {
					layer.msg('系统错误，数据查询失败！', {
						icon: 2
					});
					layer.close(formIndex);
				}

			});
	            },
		cols: [
			 //表头
				{
					field: 'permissionId',
//					templet: '#permissionIdCheckBox',  
//					title: '',
//					style: "display:none;",
					type:"numbers",
//					width: 50
				},
				{
					field: 'permissionName',
					title: '权限名称',
					sort: true,
					singleLine:true,
					width:350
				}, 
				{
					field: 'permissionCode',
					width: 200,
					align:"center",
					singleLine:true,
					title: '权限编码'
				},
				{
					field: 'permissionType',
					title: '权限类型',
					align:"center",
					width: 100,
					templet:function(d){
					    return d.permissionType=="1"?"菜单":(d.permissionType=="2"?"按钮":"数据");
					}
				},  {
					field: 'url',
					title: '地址',
					align:"center",
					singleLine:true,
					sort: false
//					width: 150
				}, {
					field: 'sysCode',
					title: '系统编码',
					align:"center",
					singleLine:true,
					width: 150
				},  {
					field: '',
					title: '操作',
					templet: '#barDemo',
					align:"center",
					singleLine:true,
					sort: false
//					width: 150
				}
		]
	});


	//修改权限信息 行工具条事件
	table.on('tool(permission_table)', function(
		obj) { 

		var data = obj.data; //获得当前行数据
		var icon = data.icon;
		var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		var tr = obj.tr; //获得当前行 tr 的 DOM 对象（如果有的话）
		if (layEvent === 'detail') { //查看/修改
		  //图标回显
			$("#permissionDetail input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("i").attr("class",icon+" layui-ext-icon-selected-icon");
			$("#permissionDetail input[name='icon']").next().find("div[class='layui-ext-icon-selected-selected-value']").find("div[class='layui-ext-icon-selected-name']").text($(iconNames).attr(icon));
			layer.open({
				type: 1,
				offset: '10px',
				area: ['800px', '560px'],
				title: '查看 / 修改权限',
				content: $("#permissionDetail"),
				shade: [0.2, '#1D1E3E'],
				btn: ['修改权限', '取消'],
				btn1: function(formIndex, layero) {
					layer.confirm('确定修改【权限信息】？', {
						icon: 3,
						title: '修改权限'
					}, function(index) {

						let formData = form.val("permissionDetail");
						$.ajax({
							type: "POST",
							url: context_path +
								"/permission/updatePermission",
//							contentType: 'application/json; charset=UTF-8',
							//  contentType 默认数据类型为表单：application/x-www-form-urlencoded; charset=UTF-8 

							data: formData,
							dataType: "json",
							success: function(result) {
								//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
								if (result.code == 0) {
									layer.msg('数据修改成功！', {
										icon: 6
									});
									//更新缓存及表格行数据
									obj.update({
										permissionName: formData
											.permissionName,
										permissionType: formData
											.permissionType,
										permissionCode: formData
											.permissionCode,
										parentId: formData
											.parentId,
										level: formData.level,
										sysCode: formData
											.sysCode,
										icon: formData
											.icon,
										url: formData
											.url
									});
									//关闭表单弹窗
									layer.close(formIndex);
								} else {
									layer.msg('数据修改失败！', {
										icon: 5
									});
								}
							},
							error: function(e) {
								layer.msg('系统错误，数据修改失败！', {
									icon: 2
								});
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
				url: context_path + "/permission/queryPermissionById",
				data: {
					permissionId: data.permissionId
				},
				dataType: "json",
				success: function(result) {
					//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
					if (result.code == 0) {
						//请求成功时处理
						form.val("permissionDetail", { //permissionDetail 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
							"permissionId": result.data.permissionId,
							"permissionName": result.data
								.permissionName, // "name": "value"
							"permissionType": result.data.permissionType,
							"permissionCode": result.data.permissionCode,
							"url": result.data.url,
							"sysCode": result.data.sysCode,
							"parentId": result.data.parentId,
							"createTime": result.data.createTime,
							"modifyTime": result.data.modifyTime,
							"position": result.data.position,
							"icon": result.data.icon,
							"remarks": result.data.remarks
						});
						let parentId =result.data.parentId;
						$("#permissionDetail input[name='parentName']").val(parentId=="-1"?"无":result.data.parentName);
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

		} else if (layEvent === 'del') { //删除权限
			layer.confirm('确定删除权限【 ' + data.permissionName + ' 】？这会导致该权限及其所有子孙权限被删除，并解除与之绑定的所有角色！', {
				icon: 3,
				title: '删除权限'
			}, function(index) {
				$.ajax({
					type: "POST",
					url: context_path + "/permission/delPermissionById",
					data: {
						permissionId: data.permissionId
					},
					dataType: "json",
					success: function(result) {
						//icon 1 橙色！  2 红色X  3 灰色？ 4  灰色锁头 5 红色难过表情 6 绿色笑脸
						if (result.code == 0) {
							layer.msg(result.msg, {
								icon: 6
							});
							//请求成功时处理
							obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
							layer.close(index);
						} else {
							layer.msg(result.msg, {
								icon: 5
							});
							layer.close(index);
						}
					},
					error: function(e) {
						layer.msg(e.responseText, {
							icon: 2
						});
						layer.close(index);
					}

				});

			});
		} else if (layEvent === 'add') { //新增子权限
		    $("#addPermission input[name='parentId']").val(data.permissionId);
		    $("#addPermission input[name='parentName']").val(data.permissionName);
				layer.open({
					type: 1,
					offset: '10px',
					area: ['800px', '530px'],
					title: '新增子权限',
					content: $("#addPermission"),
					shade: [0.2, '#1D1E3E'],
					btn: ['确认', '取消'],
					btn1: function(formIndex, layero) {
						layer.confirm('确定新增子权限？', {
							icon: 3,
							title: '新增子权限'
						}, function(index) {
							let formData = form.val("addPermission");
							if(formData.permissionName=="") {
        							    layer.msg("权限名称不能为空！", {
        								icon: 5
        							});
        							    return;
							}
							
							if(formData.permissionCode=="") {
							    layer.msg("权限编码不能为空！", {
								icon: 5
							});
							    return;
						}
							$.ajax({
								type: "POST",
								url: context_path +
									"/permission/addPermission",
								data: formData,
								dataType: "json",
								success: function(result) {
									if (result.code == 0) {
										layer.msg(result.msg, {
											icon: 6
										});
										//关闭表单弹窗
										layer.close(formIndex);
										//重置父节点数据，头部新增权限按钮用到父节点输入框数据
										 $("#addPermission input[name='parentId']").val("-1");
										 $("#addPermission input[name='parentName']").val("无");
										 $("#addPermission input[name='permissionName']").val("");
										  $("#addPermission input[name='remarks']").text("");
										 permissionTab.refresh();
									} else {
										layer.msg(result.msg, {
											icon: 5
										});
										//重置父节点数据，头部新增权限按钮用到父节点输入框数据
										 $("#addPermission input[name='parentId']").val("-1");
										 $("#addPermission input[name='parentName']").val("无");
										 $("#addPermission input[name='permissionName']").val("");
										 $("#addPermission input[name='remarks']").text("");
									}
								},
								error: function(e) {
									layer.msg('系统错误，数据修改失败！', {
										icon: 2
									});
									//重置父节点数据，头部新增权限按钮用到父节点输入框数据
									 $("#addPermission input[name='parentId']").val("-1");
									 $("#addPermission input[name='parentName']").val("无");
									 $("#addPermission input[name='permissionName']").val("");
									 $("#addPermission input[name='remarks']").text("");
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
		}
	});
	//点击头部新增按钮
	$("#addPermissionButton").click(function(){
		layer.open({
			type: 1,
			offset: '10px',
			area: ['800px', '550px'],
			title: '新增权限',
			content: $("#addPermission"),
			shade: [0.2, '#1D1E3E'],
			btn: ['提交', '取消'],
			btn1: function(index, layero) {
				var tempStatus;
				var formData = form.val('addPermission');
				if (formData.userName == "") {
					layer.msg('权限名称不能为空！', {
						icon: 5
					});
					return;
				}
			
			if(formData.permissionCode=="") {
			    layer.msg("权限编码不能为空！", {
				icon: 5
			});
			    return;
		}
				$.ajax({
					type: "POST",
					url: context_path + "/permission/addPermission",
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
							permissionTab.refresh();
							$("#addPermission input[name='permissionName']").val("");
							$("#addPermission input[name='url']").val("");
							$("#addPermission input[name='permissionCode']").val("");
							$("#addPermission input[name='sysCode']").val("");
							$("#addPermission input[name='remarks']").text("");
							
							layer.close(index);
						} else {
							layer.msg(result.msg, {
								icon: 5
							});
						}
					},
					error: function(e) {
						layer.msg('系统错误，权限添加失败！', {
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
