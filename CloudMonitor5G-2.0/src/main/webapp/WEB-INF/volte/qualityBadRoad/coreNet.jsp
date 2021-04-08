<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>核心网问题质差路段分析</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%@ include file="../../../taglibs/echarts.jsp" %>
	<script type="text/javascript">
	/* rtp丢包率时间分布一维图表 */
	var rtpAndTimeChart;
	
	/* rtp丢包率和sinr二维图图表 */
	var rtpAndSinrChart;
	/* 初始化rtp丢包率和rsrp二维图图表chart的X与Y轴 */
	var rtpAndSinrScatterItemX=[];
	var rtpAndSinrScatterItemY=[];
	<c:forEach items="${rtpPacketLostRatioSinrChartAxis.xAxisList}" var="xAxis"  >
		rtpAndSinrScatterItemX.push('${xAxis}');
	</c:forEach>
	<c:forEach items="${rtpPacketLostRatioSinrChartAxis.yAxisList}" var="yAxis"  >
		rtpAndSinrScatterItemY.push('${yAxis}');
	</c:forEach>
	/* 初始化rtp丢包率和rsrp二维图图表二维图点的宽高 */
	var rtpAndSinrScatterSymbolSize =[];
	
	/* rtp丢包率和rsrp二维图图表 */
	var rtpAndRsrpChart;
	/* 初始化rtp丢包率和rsrp二维图图表chart的X与Y轴 */
	var rtpAndRsrpScatterItemX=[];
	var rtpAndRsrpScatterItemY=[];
	<c:forEach items="${rtpPacketLostRatioRsrpChartAxis.xAxisList}" var="xAxis"  >
		rtpAndRsrpScatterItemX.push('${xAxis}');
	</c:forEach>
	<c:forEach items="${rtpPacketLostRatioRsrpChartAxis.yAxisList}" var="yAxis"  >
		rtpAndRsrpScatterItemY.push('${yAxis}');
	</c:forEach>
	/* 初始化rtp丢包率和rsrp二维图图表二维图点的宽高 */
	var rtpAndRsrpScatterSymbolSize =[];
	
	$(function(){
		initChartsWH();
		initChart();
	});
	//重新加载
	window.onresize=reload;
	function reload(){
		initChartsWH();
		self.window.setTimeout(function(){
			rtpAndTimeChart.resize();
			rtpAndRsrpChart.resize();
			if(rtpAndRsrpChart.getSeries()){
				var rtpAndRsrpChartDate = rtpAndRsrpChart.getSeries()[0].data;
				rtpAndRsrpChart.setSeries([{name:'占比',type:'scatter',symbolSize:rtpAndRsrpScatterSymbolSize,symbol:'rectangle',data:rtpAndRsrpChartDate}],true);
			}
			rtpAndSinrChart.resize();
			if(rtpAndRsrpChart.getSeries()){
				var rtpAndSinrChartDate = rtpAndSinrChart.getSeries()[0].data;
				rtpAndSinrChart.setSeries([{name:'占比',type:'scatter',symbolSize:rtpAndSinrScatterSymbolSize,symbol:'rectangle',data:rtpAndSinrChartDate}],true);
			}
		}, 100);
	}
	/* 初始化二维图填充格子的长宽 */
	function initChartsWH(){
		var widthTotal = $(document.body).outerWidth()/4*0.65;
		var heightTotal = $(document.body).outerHeight()*0.35/2*0.5;
		widthTotal = widthTotal<50?50:widthTotal;
		heightTotal = heightTotal<30?30:heightTotal;
		rtpAndSinrScatterSymbolSize[0]=(widthTotal/rtpAndSinrScatterItemX.length-1).toFixed(1);
		rtpAndSinrScatterSymbolSize[1]=(heightTotal/rtpAndSinrScatterItemY.length-1).toFixed(1);
		rtpAndRsrpScatterSymbolSize[0]=(widthTotal/rtpAndRsrpScatterItemX.length-1).toFixed(1);
		rtpAndRsrpScatterSymbolSize[1]=(heightTotal/rtpAndRsrpScatterItemY.length-1).toFixed(1);
	}
		/* Chart路径配置 */
        require.config({
            paths: {echarts:'${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
		
		/* 初始化图表 */
        function initChart(){
			require(
				['echarts','echarts/chart/line','echarts/chart/bar'],
				function (ec) {
					rtpAndTimeChart = ec.init(document.getElementById("rtpAndTime")); 
					rtpAndTimeChart.setOption(rtpAndTimeOption);
				}
			);
			require(
				['echarts','echarts/chart/scatter'],
				function (ec) {
					rtpAndRsrpChart = ec.init(document.getElementById("rtpAndRsrpChart")); 
					rtpAndRsrpChart.setOption(rtpAndRsrpOption);
					rtpAndSinrChart = ec.init(document.getElementById("rtpAndSinrChart")); 
					rtpAndSinrChart.setOption(rtpAndSinrOption);
				}
			);
	    }
	    var rtpAndTimeItem=[${rtpPacketLostRatioHourChartAxis.axisCustomer}];
		/* rtp时序图option */
		var	rtpAndTimeOption = {
		    /* title : {text: '采样点数量占比统计',x:'30'}, */
			tooltip : {trigger: 'item',formatter:
				function (params) {
					return params.seriesName + '<br/>'+params.name+'时-'
					+(params.name+1)+ '时:'+params.value;
			      
		    	}
		    }, 
		    legend: {orient : 'vertical',x : 'right',y:'center',borderColor:'#cccccc',padding:[10,20,10,20],borderWidth:1,data:['RTP值']},
		    toolbox: {show : false},
		    xAxis : [{type:'category',boundaryGap:true,data:rtpAndTimeItem,name:'时间区间',axisLabel:{interval:0}}],
			yAxis : [{type:'value',name:'RTP值',scale:true}],
		    series : [{name:'RTP值',lineStyle:{color:'#276ebf'},type:'line',data:${empty rtpPacketLostRatioHourChart?"['-']":rtpPacketLostRatioHourChart}}],
		    grid :{  x:'5%',y:'12%',x2:'15%',y2:'15%'}
		};
		/* rtp和rsrp二维图 chart配置 */
		var	rtpAndRsrpOption = {
			tooltip : {
			    trigger: 'item',
			    formatter: function (params) {
		        	if (params.value.length > 1) {
						return 'RTP丢包率:' +params.value[0]  + '<br/>'
							+ 'RSRP:'+params.value[1]  + '<br/>'
							+ params.seriesName+':'+params.value[2] + '%' ;
			        }
			    }
			},
		    dataRange: {min: 0,max: 100,x:'right',y: 'center',color:['#0f243e','#17365d','#548dd4','#8db3e2','#c6d9f1'],calculable : true},
			xAxis:[
			    {
			      	name:'RTP',
			        type:'category',
			        boundaryGap:true,
			        axisLabel:{interval:0,rotate:-90},
			        axisTick:{show:true,interval:0},
			        splitLine:{show:true,onGap:true},
			        data:rtpAndRsrpScatterItemX
			    }
			],
		   	yAxis:[
		        {
		          	name:'RSRP',
		            type:'category',
		            boundaryGap:true,
		            axisLabel:{interval:0},
		            axisTick:{show:true,interval:0},
		            splitLine:{show:true,onGap:true},
		            scale : true,
		            data:rtpAndRsrpScatterItemY
		        }
		    ],
		   	animation: false,
		 	grid :{  x:'13%',y:'12%',x2:'13%',y2:'25%'},
		    series : [{name:'占比',type:'scatter',symbolSize:rtpAndRsrpScatterSymbolSize,symbol:'rectangle',data:${empty rtpPacketLostAndRsrpChart?"['-']":rtpPacketLostAndRsrpChart}}]
		};
		//['[80,100]','[-85,∞)',20.00]
		/* rtp和sinr二维图 chart配置 */
		var	rtpAndSinrOption = {
			tooltip : {
			    trigger: 'item',
			    formatter: function (params) {
		        	if (params.value.length > 1) {
						return 'RTP丢包率:' +params.value[0]  + '<br/>'
							+ 'SINR:'+params.value[1]  + '<br/>'
							+ params.seriesName+':'+params.value[2] + '%' ;
			        }
			    }
			},
		    dataRange: {min: 0,max: 100,x:'right',y: 'center',color:['#0f243e','#17365d','#548dd4','#8db3e2','#c6d9f1'],calculable : true},
			xAxis:[
			    {
			      	name:'RTP',
			        type:'category',
			        boundaryGap:true,
			        axisLabel:{interval:0,rotate:-90},
			        axisTick:{show:true,interval:0},
			        splitLine:{show:true,onGap:true},
			        data:rtpAndSinrScatterItemX
			    }
			],
		   	yAxis:[
		        {
		          	name:'SINR',
		            type:'category',
		            boundaryGap:true,
		            axisLabel:{interval:0},
		            axisTick:{show:true,interval:0},
		            splitLine:{show:true,onGap:true},
		            scale : true,
		            data:rtpAndSinrScatterItemY
		        }
		    ],
		   	animation: false,
		 	grid :{  x:'13%',y:'12%',x2:'13%',y2:'25%'},
		    series : [{name:'占比',type:'scatter',symbolSize:rtpAndSinrScatterSymbolSize,symbol:'rectangle',data:${empty rtpPacketLostAndSinrChart?"['-']":rtpPacketLostAndSinrChart}}]
		};
	</script>

	
  </head>
  
  <body class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	
    	<div  data-options="region:'north',border:false" style="height:35%;width: 100%;padding:0px 0px 2px 0px;">
    		<table class="easyui-datagrid" data-options="title:'质差路段指标表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
   				<thead>
   					<tr>
   						<th rowspan="2" data-options="field:'boxId',width:60,align:'center'" >BOXID</th>
   						<th colspan="3" >语音质差路段信息</th>
   						<th colspan="11" >指标</th>
   					</tr>
   					<tr>
   						<th data-options="field:'m_stRoadName',width:120,align:'center'">路段名称</th>
   						<th data-options="field:'m_dbDistance',width:60,align:'center'">持续距离(m)</th>
   						<th data-options="field:'m_dbContinueTime',width:90,align:'center',
   							formatter: function(value,row,index){
								if (value){
									return value/1000;
								}
								return value;
							}">测试时间(s)</th>
   						<th data-options="field:'mosAvg',width:60,align:'center'">MOS均值</th>
   						<th data-options="field:'upRBNum',width:60,align:'center'">上行RB数</th>
   						<th data-options="field:'downRBNum',width:60,align:'center'">下行RB数</th>
   						<th data-options="field:'upMSCAvg',width:50,align:'center'">上行MCS<br>均值</th>
   						<th data-options="field:'downMSCAvg',width:50,align:'center'">下行MCS<br>均值</th>
   						<th data-options="field:'upPHYSpeed',width:80,align:'center'">上行PHY速率<br>(kbps)</th>
   						<th data-options="field:'downPHYSpeed',width:80,align:'center'">下行PHY速率<br>(kbps)</th>
   						<th data-options="field:'upPDCPSpeed',width:80,align:'center'">上行PDCP速率<br>(kbps)</th>
   						<th data-options="field:'downPDCPSpeed',width:80,align:'center'">下行PDCP速率<br>(kbps)</th>
   						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true">recSeqNo</th>
   						<th data-options="field:'id',width:120,align:'center',hidden:true">ID</th>
   					</tr>
   				</thead>
   				<tbody>
   					<c:forEach items="${coreNetworkRoads}" var="coreNetworkRoad">
    					<tr>
    						<td>${coreNetworkRoad.testLogItem.boxId}</td>
    						<td>${coreNetworkRoad.m_stRoadName}</td>
    						<td>${coreNetworkRoad.m_dbDistance}</td>
    						<td>${coreNetworkRoad.m_dbContinueTime}</td>
    						<td>${coreNetworkRoad.mosAvg}</td>
    						<td>${coreNetworkRoad.upRBNum}</td>
    						<td>${coreNetworkRoad.downRBNum}</td>
    						<td>${coreNetworkRoad.upMSCAvg}</td>
    						<td>${coreNetworkRoad.downMSCAvg}</td>
    						<td>${coreNetworkRoad.upPHYSpeed}</td>
    						<td>${coreNetworkRoad.downPHYSpeed}</td>
    						<td>${coreNetworkRoad.upPDCPSpeed}</td>
    						<td>${coreNetworkRoad.downPDCPSpeed}</td>
    						<td>${coreNetworkRoad.testLogItem.recSeqNo}</td>
    						<td>${coreNetworkRoad.id}</td>
    					</tr>
   					</c:forEach>
   				</tbody>
   			</table>
    	</div>
    	<div  data-options="region:'center',border:false" style="width: 100%;padding:0px 0px 2px 0px;">
    		<div id="rtpAndTime" class="easyui-panel" data-options="fit:true,title:'时序图'">
    		</div>
    	</div>
    	<div  data-options="region:'south',border:false" style="height:35%;width: 100%;">
    		<div class="easyui-layout" style="width: 100%;height: 100%;"> 
    			<div  data-options="region:'west',border:false" style="width: 50%;padding:0px 2px 0px 0px;">
    				<div id="rtpAndRsrpChart" class="easyui-panel" data-options="fit:true,title:'RTP与RSRP映射'">
		    		</div>
    			</div>
    			<div  data-options="region:'center',border:false" style="width: 50%;">
    				<div id="rtpAndSinrChart" class="easyui-panel" data-options="fit:true,title:'RTP与SINR映射'">
		    		</div>
    			</div>
    		</div>
    	</div>
  </body>
</html>
