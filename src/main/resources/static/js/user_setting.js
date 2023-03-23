layui.use(['form'],
		function() {
			var
				form =
				layui.form;
			var
				userId =
				"<%=userId%>";
			var
				contextPath =
				"<%=contextPath%>";
			$("#updPassword
				input[name = 'userId']
				").val(userId);
				$("#userDetail
					input[name = 'userId']
					").val(userId);
					renderUserDetail();
					//修改密码
					form.on('submit(updPassword)',
						function(data) {
							var
								formData =
								form.val('updPassword');
							if (formData.newPassword !=
								formData.confirmPassword) {
								layer.msg("两次输入的新密码不一致！", {
									icon: 5
								});
								return;
							}

							let
								requestUrl =
								contextPath +
								"/user/updatePassword";
							$.ajax({
								type: "POST",
								url: requestUrl,
								data: formData,
								dataType: "json",
								success: function(result) {
									if (result.code ==
										0) {
										layer.msg(result.msg +
											"新密码【" +
											formData.newPassword +
											"】，" +
											"您需要重新登录！", {
												icon: 6
											});
										setTimeout(function() {
												window.location.href =
													contextPath +
													"/logout";
											},
											"5000");
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

					function renderUserDetail() {
						$.ajax({
							type: "POST",
							url: contextPath +
								"/user/queryUserDetail";,
							data: formData,
							dataType: "json",
							success: function(result) {
								if (result.code ==
									0) {
									debugger;
								} else {
									layer.msg("没有查到数据！", {
										icon: 5
									});
								}
							},
							error: function(e) {
								layer.msg("没有查到数据！", {
									icon: 2
								});
							}
						});
					}

				});
