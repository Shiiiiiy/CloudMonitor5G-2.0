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
		],
		seriesIndex:0
	}
};



MyChart.fn = function (a) {

	var chartObject = {};
	var frameIndex = 0;
	var syncOther = true;

	var lastPosition;

	function calcIndex(){

		var target;
		var timeArray = MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] ).map(c=>c.time);
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
			if(

				MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] )[target][MyChart.Data.col[MyChart.Data.currentIndex][0]]
			||  MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] )[target][MyChart.Data.col[MyChart.Data.currentIndex][1]] ){
				flag = false;

				if(MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] )[target][MyChart.Data.col[MyChart.Data.currentIndex][0]]){
					MyChart.Data.seriesIndex = 0;
				}else{
					MyChart.Data.seriesIndex = 1;
				}


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
				triggerOn: MyPlayer.Data.playingStatus ? "none" :  "click",
				trigger:'axis',
				alwaysShowContent:  MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] ).map(a=>a.time).length > 0 ? true : false,
				snap:true,
				formatter:function(params){
					var res =  params[0].name.slice(11 ,params[0].name.length);
					//	var res = new Date(params[0].name).Format("hh:mm:ss") ;
					for(var i=0;i<params.length;i++){
						if(params[i].data){
							res+= "<br>"+params[i].marker+params[i].seriesName+":"+params[i].data;
						}
					}
					return res;

				},
				position: function(pt,b) {
					var newPosition = pt[0] -68;
					if(syncOther && newPosition != lastPosition ){
						var time = b[0].axisValue;
						MyPlayer.Data.currentTime = time;
						MyPlayer.fn.sync(MyChart.Config.syncSourceId);
					}
					syncOther = true;
					lastPosition = newPosition;
					return [ newPosition , 100];
				},
				lineStyle:{
					color:"red"
				},
			},

			legend: {
				data: MyChart.Data.col[MyChart.Data.currentIndex],
				top:'11%'
			},

			xAxis: {
				type: "category",
				data: MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] ).map(a=>a.time),

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
					data: MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] ).map(a=>a[MyChart.Data.col[MyChart.Data.currentIndex][0]]),
					connectNulls:true
				},
				{
					name: MyChart.Data.col[MyChart.Data.currentIndex][1],
					type: 'line',
					yAxisIndex: 1,
					data: MyChart.Data.all.filter( a => a[MyChart.Data.col[MyChart.Data.currentIndex][0]] || a[MyChart.Data.col[MyChart.Data.currentIndex][1]] ).map(a=>a[MyChart.Data.col[MyChart.Data.currentIndex][1]]),
					connectNulls:true
				}
			]
		}

		return option;
	}

	function setOption(options){
		if(!$.isEmptyObject(chartObject)){
			chartObject.setOption(options);
		}
	}


	return a = {
		init: function () {

			if(document.getElementById(MyChart.Config.container)){

				var d = $("<div></div>");

				var $this = this;

				$.each(MyChart.Data.title,function(k, v){

					var s = $("<span style='cursor:pointer;margin-left: 10px'><a >"+v+"</a></span>");
					s.click(function(){
						$this.change(k);
					})

					d.append(s);
				});

				$("#"+MyChart.Config.container).empty(d);

				$("#"+MyChart.Config.container).append(d);

				var l = $("<div style='height:215px;overflow-x:auto' id='lineChart'> </div>");
				$("#"+MyChart.Config.container).append(l);

				chartObject = echarts.init(document.getElementById("lineChart"));
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
			if(!$.isEmptyObject(chartObject)){
				chartObject.resize();
			}
		},synced:function(){
			if(!$.isEmptyObject(chartObject)){
				calcIndex();
				syncOther = false;
				chartObject.dispatchAction({
					type: 'showTip',
					seriesIndex: MyChart.Data.seriesIndex,
					dataIndex: frameIndex
				});
			}


		}
	}
}();
