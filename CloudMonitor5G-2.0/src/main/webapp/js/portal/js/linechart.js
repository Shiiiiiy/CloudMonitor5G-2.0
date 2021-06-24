var MyChart = {

	Config:{
		container:"view7Div",
		syncSourceId:'view7'
	},
	Data : {
		all:[],
		currentIndex:0,
		title:['LTE测量','NR测量','LTE速率','NR速率切换'],
		col:[
			[ 'RSRP', 'SINR'],
			[ 'SS_RSRP', 'SS_SINR'],
			[ 'LTE PDCP Thrput DL(Mbps)', 'LTE PDCP Thrput UL(Mbps)'],
			[ 'NR PHY Thrput UL(Mbps)', 'NR PHY Thrput DL(Mbps)']
		]
	}
};



MyChart.fn = function (a) {

	var chartObject = {};
	var frameIndex = 0;
	var syncOther = true;

	function calcIndex(){

		var target;
		var timeArray = MyChart.Data.all.map(c=>c.time);
		timeArray.push(MyPlayer.Data.currentTime);
		timeArray.sort();
		var position =  timeArray.indexOf(MyPlayer.Data.currentTime);

		if(position == 0){
			target = 0;
		}else if(position == timeArray.length - 1){
			target = timeArray.length -2;
		}else{
		//	var before = timeArray[position-1];
		//	var after = timeArray[position+1];
			target = position;
		}

		//判断当前位置有没有值，如果两个指标都没值的话，折线图里的tooltip不变而且axisPointer会消失
		var flag = true;
		while( flag &&  target< timeArray.length -2  ){
			if(MyChart.Data.all[target][MyChart.Data.col[MyChart.Data.currentIndex][0]] ||  MyChart.Data.all[target][MyChart.Data.col[MyChart.Data.currentIndex][1]]){
				flag = false;
			}else{
				target++;
			}
		}

		frameIndex = target;
	}

	function getOption(){

		var option = {

			title:{
				text: MyChart.Data.title[MyChart.Data.currentIndex],
				left:'center'
			},

			tooltip: {
				triggerOn: "click",
				trigger:'axis',
				alwaysShowContent: true,
				snap:true,
				position: function(pt,b) {
					if(syncOther){
						var time = b[0].axisValue;
						MyPlayer.Data.currentTime = time;
						MyPlayer.fn.sync(MyChart.Config.syncSourceId);
					}
					syncOther = true;
					return [pt[0]-100, 130];
				},
				lineStyle:{
					color:"red"
				},
			},

			legend: {
				data: MyChart.Data.col[MyChart.Data.currentIndex],
				top:'7%'
			},

			xAxis: {
				type: "category",
				data: MyChart.Data.all.map(a=>a.time),

				axisPointer: {
					value: MyPlayer.Data.startTime,
					animation:true,
					lineStyle:{
						color:"red"
					},
					label:{
						show:false
					},
					handle: {
						show: false,
						size:10
					}
				},
				splitLine: {
					show: false
				},
				snap:true
			},
			yAxis: [{
				type: 'value',
				name: MyChart.Data.col[MyChart.Data.currentIndex][0],
				axisLabel: {
					formatter: '{value}'
				},
				splitLine: {
					show: false
				}
			},
				{
					type: 'value',
					name: MyChart.Data.col[MyChart.Data.currentIndex][1],
					axisLabel: {
						formatter: '{value}'
					},
					splitLine: {
						show: false
					}
				}
			],
			series: [
				{
					name: MyChart.Data.col[MyChart.Data.currentIndex][0],
					type: 'line',
					data: MyChart.Data.all.map(a=>a[MyChart.Data.col[MyChart.Data.currentIndex][0]])
				},
				{
					name: MyChart.Data.col[MyChart.Data.currentIndex][1],
					type: 'line',
					yAxisIndex: 1,
					data: MyChart.Data.all.map(a=>a[MyChart.Data.col[MyChart.Data.currentIndex][1]])
				}
			]
		}

		return option;
	}

	function setOption(options){
		if(chartObject){
			chartObject.setOption(options);
		}
	}


	return a = {
		init: function () {

			if(document.getElementById(MyChart.Config.container)){

				var d = $("<div></div>");

				var $this = this;

				$.each(MyChart.Data.title,function(k, v){

					var s = $("<span style='cursor:pointer;margin-right: 10px'><a >"+v+"</a></span>");
					s.click(function(){
						$this.change(k);
					})

					d.append(s);
				});

				$("#"+MyChart.Config.container).before(d);

				chartObject = echarts.init(document.getElementById(MyChart.Config.container));
				setOption(getOption());
			}

		},change:function(i){
			MyChart.Data.currentIndex = i;
			setOption(getOption());
		},disableClick:function(){
			var opt = {
				tooltip: {
					triggerOn: "none"
				}
			};
			setOption(opt);
		},enableClick:function(){
			var opt = {
				tooltip: {
					triggerOn: "click"
				}
			};
			setOption(opt);
		},resize:function(){
			chartObject.resize();
		},synced:function(){
			calcIndex();
			syncOther = false;
			chartObject.dispatchAction({
				type: 'showTip',
				seriesIndex: 0,
				dataIndex: frameIndex
			});
		}
	}
}();
