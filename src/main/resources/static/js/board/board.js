var board= {
	renderPieChart:function() {
		var dom = document.getElementById('pieChart');
		    var myChart = echarts.init(dom, null, {
		      renderer: 'canvas',
		      useDirtyRect: false
		    });
		    var app = {};
		    
		    var option;

		    option = {
		  title: {
		    text: '公司收入结构',
		    subtext: '单位：万元',
		    left: 'center'
		  },
		  tooltip: {
		    trigger: 'item'
		  },
		  legend: {
		    orient: 'vertical',
		    left: 'left'
		  },
		  series: [
		    {
		      name: '收入来源',
		      type: 'pie',
		      radius: '50%',
		      data: [
		        { value: 1048, name: '软/硬件服务费' },
		        { value: 735, name: '营销推广服务费' },
		        { value: 580, name: '外包服务费' },
		        { value: 484, name: '产品销售收入' },
		        { value: 300, name: '软件专利费' }
		      ],
		      emphasis: {
		        itemStyle: {
		          shadowBlur: 10,
		          shadowOffsetX: 0,
		          shadowColor: 'rgba(0, 0, 0, 0.5)'
		        }
		      }
		    }
		  ]
		};

		    if (option && typeof option === 'object') {
		      myChart.setOption(option);
		    }

		    window.addEventListener('resize', myChart.resize);
	},
	renderBarChartRace:function(){
	    
	    var dom = document.getElementById('barChart');
	    var myChart = echarts.init(dom, null, {
	      renderer: 'canvas',
	      useDirtyRect: false
	    });
	    var app = {};
	    
	    var option;

	    option = {
	  legend: {},
	  tooltip: {},
	  dataset: {
	    source: [
	      ['product', '2020', '2021', '2022'],
	      ['销售部', 43.3, 85.8, 93.7],
	      ['采购部', 83.1, 73.4, 55.1],
	      ['软件研发部', 72.4, 53.9, 50.1]
	    ]
	  },
	  xAxis: { type: 'category' },
	  yAxis: {},
	  // Declare several bar series, each will be mapped
	  // to a column of dataset.source by default.
	  series: [{ type: 'bar' }, { type: 'bar' }, { type: 'bar' }]
	};

	    if (option && typeof option === 'object') {
	      myChart.setOption(option);
	    }

	    window.addEventListener('resize', myChart.resize);
	}
};
