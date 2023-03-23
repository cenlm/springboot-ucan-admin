layui.use(['form'], function() {
	var form = layui.form;
	form.render("checkbox");
	form.on('submit(userForm)', function(data) {
		var formData = form.val('userForm');
		let requestUrl = contextPath + "/login";

		$.ajax({
			type: "POST",
			url: requestUrl,
			data: formData,
			dataType: "json",
			success: function(result) {
				if (result.code == 0) {
					layer.msg(result.msg, {
						icon: 6,
						offset: [$(window).height() - 480, $(window).width() - 890]
					});
					setTimeout(function() {
						window.location.href = contextPath + "/index";
					}, "3000");
				} else {
					layer.msg(result.msg, {
						icon: 5,
						offset: [$(window).height() - 480, $(window).width() - 890]
					});
				}
			},
			error: function(e) {
				layer.msg(e.responseText, {
					icon: 2,
					offset: [$(window).height() - 480, $(window).width() - 890]
				});
			}
		});
	});
});
