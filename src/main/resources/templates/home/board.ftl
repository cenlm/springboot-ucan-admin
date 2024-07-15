<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<title>仪表盘</title>
<link rel="stylesheet" href="js/layui/css/layui.css">
</head>
<body>

	<div class="layui-fluid">
		<div class="layui-row">
			<div class="layui-col-sm12 layui-col-md12 layui-col-lg12">
				<div class="layui-bg-gray" style="padding: 30px;">
					<div class="layui-row layui-col-space15">
						<div class="layui-col-md6">
							<div class="layui-panel">
								<div style="padding: 25px 25px;">
									<div class="layui-row">
										<div class="layui-col-md6">
											<img alt="" src="imgs/hello.jpg" width="150" height="150">
										</div>
										<div class="layui-col-md6">
											<div class="layui-row">
												<div class="layui-col-md12" style="margin:20px 0px;">
													<span
														style="font-size: 45px; font-weight: bold; "><@shiro.principal/></span>
												</div>
												<div class="layui-col-md12" style="margin:10px 0px;">
													<span
														style="font-size: 20px; font-weight: bold;">最近登录地点：广州</span>
												</div>
											</div>
										</div>
									</div>


								</div>
							</div>
						</div>
						<div class="layui-col-md6">
							<div class="layui-card">
								<div class="layui-card-header">待办事项</div>
								<div class="layui-card-body">
									<div id="pieChart1" style="width: 550px; height: 120px;">
										<table class="layui-table">
											<colgroup>
												<col width="30">
												<col width="100">
												<col>
											</colgroup>

											<tbody>
												<tr>
													<td>事项1</td>
													<td>xxx项目需求确认及yyy项目上线前的联调。</td>

												</tr>
												<tr>
													<td>事项2</td>
													<td>zzz项目任务分配及zzz项目订单模块重构。</td>
												</tr>
												<tr>
													<td>事项3</td>
													<td>年末赚一个亿。。。</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="layui-bg-gray" style="padding: 30px;">
				<div class="layui-row layui-col-space15">
					<div class="layui-col-md6">
						<div class="layui-card">
							<div class="layui-card-header">收入统计</div>
							<div class="layui-card-body">
								<div id="pieChart" style="width: 550px; height: 400px;"></div>
							</div>
						</div>
					</div>
					<div class="layui-col-md6">
						<div class="layui-card">
							<div class="layui-card-header">Top3 部门业绩（单位：万元）</div>
							<div class="layui-card-body">
								<div id="barChart" style="width: 550px; height: 400px;"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/layui/layui.js"></script>
	<script src="js/jquery-3.6.3.min.js"></script>
	<script type="text/javascript"
		src="https://fastly.jsdelivr.net/npm/jquery"></script>
	<script type="text/javascript" src="js/echarts/echarts.min.js"></script>
	<script type="text/javascript" src="js/board/board.js"></script>
	<script type="text/javascript">
	   var contextPath="${request.contextPath}";
                board.renderPieChart();
                board.renderBarChartRace();
	</script>
</body>
</html>
