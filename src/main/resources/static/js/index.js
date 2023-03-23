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
