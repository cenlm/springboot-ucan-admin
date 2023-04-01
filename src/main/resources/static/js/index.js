//折叠与展开左侧导航栏
//toggle 事件依赖于jquery-migrage 插件，jquery最新版已将toggle事件移除
$(function() {
	$("#shrink-event").toggle(function() {
		//折叠事件触发的行为
	    $(this).toggleClass("layui-icon-shrink-right layui-icon-spread-left");
	    $(".left-element").toggleClass("custom_col_sm1 custom_col_md1 custom_col_lg1 layui-col-md2 layui-col-lg2");
	    $(".right-element").toggleClass("custom_col_sm11 custom_col_md11 custom_col_lg11 layui-col-md10 layui-col-lg10");
	  //隐藏箭头图标
	    $(".layui-nav-tree .layui-nav-item i[class='layui-icon layui-icon-down layui-nav-more']").css("display","none");
          //隐藏文字
	    $(".layui-nav-tree .layui-nav-item a cite").hide();
	}, function() {
	    //展开事件触发的行为
	    //显示箭头图标
	    $(".layui-nav-tree .layui-nav-item i[class='layui-icon layui-icon-down layui-nav-more']").css("display","block");
	    //显示文字
	    $(".layui-nav-tree .layui-nav-item a cite").show();
	    /*  $(".layui-nav-item i").addClass("layui-icon-down layui-nav-more");*/
	    $(this).toggleClass("layui-icon-shrink-right layui-icon-spread-left");
	    $(".left-element").toggleClass("custom_col_sm1 custom_col_md1 custom_col_lg1 layui-col-md2 layui-col-lg2");
	    $(".right-element").toggleClass("custom_col_sm11 custom_col_md11 custom_col_lg11 layui-col-md10 layui-col-lg10");
	});
	
	});


layui.use([ 'element' , 'layer' ] , function() {
    var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
    //监听导航点击
    element.on('nav(left-nav)' , function(elem) {

    });
    //主页选项卡控制
    var active = {
            boardTabAdd : function() {
        	let iframeId= "board_iframe";
        	let url = context_path + "/board";
            	//选项卡已创建，则不再进行创建操作
            	if($("li[lay-id='board']").text()!=""){
            		//切换选项卡
            		element.tabChange('index-content' , 'board');
            		//点击左边导航菜单，局部刷新iframe
            		active.refreshIframe(iframeId,url);
            		return;
            	}
                    //新增一个Tab项
                    element.tabAdd('index-content' , {
                            title : '仪表盘' , //选项卡标题
                            content : '<iframe frameborder="0" id="'+iframeId+'" style="width: 100%; height: 100%;" src="' +url+'"></iframe>' ,
                            id : 'board'
                    });
                    element.tabChange('index-content' , 'board'); //切换到：仪表盘
            } ,
            userTabAdd : function() {
        	let iframeId= "user_iframe";
        	let url = context_path + "/user/user_list";
            	if($("li[lay-id='user']").text()!=""){
            		element.tabChange('index-content' , 'user');
            		active.refreshIframe(iframeId,url);
            		return;
            	}
                    element.tabAdd('index-content' , {
                            title : '用户管理' ,
                            content : '<iframe frameborder="0" id="'+iframeId+'" style="width: 100%; height: 100%;" src="' +url+'"></iframe>' ,
                            id : 'user'
                    });
                    element.tabChange('index-content' , 'user'); //切换到：用户管理
            } ,
            roleTabAdd : function() {
        	let iframeId= "role_iframe";
        	let url = context_path + "/role/role_list";
            	if($("li[lay-id='role']").text()!=""){
            		element.tabChange('index-content' , 'role');
            		active.refreshIframe(iframeId,url);
            		return;
            	}
                    element.tabAdd('index-content' , {
                            title : '角色管理' ,
                            content : '<iframe frameborder="0" id="'+iframeId+'" style="width: 100%; height: 100%;" src="' +url+'"></iframe>' ,
                            id : 'role'
                    });
                    element.tabChange('index-content' , 'role'); //切换到：角色管理
            } ,
            permissionTabAdd : function() {
        	let iframeId= "permission_iframe";
        	let url = context_path + "/permission/permission_list";
            	if($("li[lay-id='permission']").text()!=""){
            		element.tabChange('index-content' , 'permission');
            		active.refreshIframe(iframeId,url);
            		return;
            	}
                    element.tabAdd('index-content' , {
                            title : '权限管理' ,
                            content : '<iframe frameborder="0" id="'+iframeId+'" style="width: 100%; height: 100%;" src="'+ url+'"></iframe>' ,
                            id : 'permission'
                    });
                    element.tabChange('index-content' , 'permission'); //切换到：权限管理
            },
            userSettingTabAdd : function() {
        	let iframeId= "user_setting_iframe";
        	let url = context_path + "/user/user_setting";
            	if($("li[lay-id='user_setting']").text()!=""){
            		element.tabChange('index-content' , 'permission');
            		active.refreshIframe(iframeId,url);
            		return;
            	}
                    element.tabAdd('index-content' , {
                            title : '个人设置' ,
                            content : '<iframe frameborder="0" id="'+iframeId+'" style="width: 100%; height: 100%;" src="'+ url+'"></iframe>' ,
                            id : 'user_setting'
                    });
                    element.tabChange('index-content' , 'user_setting'); //切换到：个人设置
            },
            refreshIframe: function(id,url){//局部刷新iframe
        	$("#"+id).attr("src",url);
            }
    };
            //默认显示仪表盘
            active.boardTabAdd();
        
            $(".ucan_board").click(function() {
                    active.boardTabAdd();
            });
        
            $("#user_manager").click(function() {
                    active.userTabAdd();
            });
            $("#role_manager").click(function() {
                    active.roleTabAdd();
            });
            $("#permission_manager").click(function() {
                    active.permissionTabAdd();
            });
            $("#user_setting").click(function() {
                active.userSettingTabAdd();
        });
            

});

