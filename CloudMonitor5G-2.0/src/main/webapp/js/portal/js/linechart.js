var Mychart = {
	
	Config:{
		container:"view7Div"
	},
	Data : {
		all:[],
		frameIndex:0,
		currentIndex:0,
		title:['LTE测量','NR测量','LTE速率','NR速率切换'],
		
		col:[  
			[ 'RSRP', 'SINR'],
			[ 'SS_RSRP', 'SS_SINR'],
			[ 'LTE PDCP Thrput DL(Mbps)', 'LTE PDCP Thrput UL(Mbps)'],
			[ 'NR PHY Thrput UL(Mbps)', 'NR PHY Thrput DL(Mbps)']
		],
		initValue:'',
		isInited:false,
		canSync:true
	}
};



Mychart.fn = function (a) {
	
	var chartObject = {};
	var index = 0;
	var syncFunction;
	
    return a = {
		init: function (b) {
			
			if(container =  document.getElementById(Mychart.Config.container)){

				var d = $("<div></div>");

				var $this = this;

				$.each(Mychart.Data.title,function(k,v){

					var s = $("<span style='cursor:pointer;margin-right: 10px'><a >"+v+"</a></span>");
					s.click(function(){
						$this.change(k);
					})

					d.append(s);
				});

				$("#"+Mychart.Config.container).before(d);

				chartObject = echarts.init(document.getElementById(Mychart.Config.container));
				chartObject.setOption(a.getOption());
			/**
				chartObject.getZr().on("click",function(p){
					console.log(p)
				});
			**/	
				syncFunction =  b.sync;
			}
			
        },change:function(i){
			Mychart.Data.currentIndex = i;
			chartObject.setOption(a.getOption());
		},action:function(a){
			chartObject.dispatchAction(a);
		},disableClick:function(){
			var opt = {
				tooltip: {
					triggerOn: "none"
                }
			};
			a.setOption(opt);
		},enableClick:function(){
			var opt = {
				tooltip: {
					triggerOn: "click"
                }
			};
			a.setOption(opt);
		},resize:function(){
			chartObject.resize();
		},setOption:function(options){
			if(chartObject){
				chartObject.setOption(options);
			}
		},calcIndex:function(b){
			var target;
			var timeArray = Mychart.Data.all.map(c=>c.time);
			timeArray.push(b);
			timeArray.sort();
			var position =  timeArray.indexOf(b);
		
			if(position == 0){
				target = 0;
			}else if(position == timeArray.length - 1){
				target = timeArray.length -2;
			}else{
				//var before = timeArray[position-1];
				//var after = timeArray[position+1];
				target = position +1; 
			}

			//判断当前位置有没有值，如果两个指标都没值的话，折线图里的tooltip不变而且axisPointer会消失
			var flag = true;
			while( flag &&  target< timeArray.length -2  ){
				if(Mychart.Data.all[target][Mychart.Data.col[Mychart.Data.currentIndex][0]] ||  Mychart.Data.all[target][Mychart.Data.col[Mychart.Data.currentIndex][1]]){
					flag = false;
				}else{
					target++;
				}
			}
			Mychart.Data.frameIndex = target;
			
		},play:function(){
			
			chartObject.dispatchAction({
			  type: 'showTip',
			  seriesIndex: 0,
			  dataIndex: Mychart.Data.frameIndex
			 });
		}
		
		,getTimeOption:function(time){
			var option = {
				xAxis: {
					axisPointer: {
						value:time,
					}
				}
			}
			return option;
		},getOption:function(){
			
			var option = {
				
				title:{
					text: Mychart.Data.title[Mychart.Data.currentIndex],
					left:'center'
				},
		
				tooltip: {
					triggerOn: "click",
					trigger:'axis',
					alwaysShowContent: true,
					snap:true,
					position: function(pt,b) {
						//暂停状态且有同步方法才执行同步操作
						if(!MyPlayer.Data.playingStatus && syncFunction  && Mychart.Data.canSync ){
							var time = b[0].axisValue;
							
							MyPlayer.Data.currentTime = time;
							syncFunction();
						}
						return [pt[0]-100, 130];
					},
					lineStyle:{
						color:"red"
					},
				 },
				
				legend: {
					data: Mychart.Data.col[Mychart.Data.currentIndex],
					top:'7%'
				},
				
				 xAxis: {
					type: "category",
					data: Mychart.Data.all.map(a=>a.time),
					
				
					axisPointer: {
					  value: Mychart.Data.initTime,
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
						name: Mychart.Data.col[Mychart.Data.currentIndex][0],
						axisLabel: {
							formatter: '{value}'
						},
						splitLine: {
							show: false
						}
					},
					{
						type: 'value',
						name: Mychart.Data.col[Mychart.Data.currentIndex][1],
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
						name: Mychart.Data.col[Mychart.Data.currentIndex][0],
						type: 'line',
						data: Mychart.Data.all.map(a=>a[Mychart.Data.col[Mychart.Data.currentIndex][0]])
					},
					{
						name: Mychart.Data.col[Mychart.Data.currentIndex][1],
						type: 'line',
						yAxisIndex: 1,
						data: Mychart.Data.all.map(a=>a[Mychart.Data.col[Mychart.Data.currentIndex][1]])
					}
				]
			}
			
			return option;
			
		}
    }
}();
