<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>核心网原因</title>

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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	//获取及回显道路名称
	var mainTableId = 'coreNetRoadTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>callEstablish/roadNameCallBack.action";
	}
	/* rtp丢包率时间分布一维图表 */
	var rtpAndTimeChart;
	
	
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
			
		}, 100);
	}
	/* 初始化二维图填充格子的长宽 */
	function initChartsWH(){
		var widthTotal = $(document.body).outerWidth()/2*0.65;
		var heightTotal = $(document.body).outerHeight()*0.3/2*0.5;
		widthTotal = widthTotal<50?50:widthTotal;
		heightTotal = heightTotal<30?30:heightTotal;
		rtpAndRsrpScatterSymbolSize.push((widthTotal/rtpAndSinrScatterItemX.length-1).toFixed(1));
		rtpAndRsrpScatterSymbolSize.push((heightTotal/rtpAndSinrScatterItemY.length-1).toFixed(1));
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
					rtpAndTimeChart.setOption(toption);
				}
			);
			require(
				['echarts','echarts/chart/scatter'],
				function (ec) {
					rtpAndRsrpChart = ec.init(document.getElementById("rtpAndRsrpChart")); 
					rtpAndRsrpChart.setOption(roption);
					
				}
			);
	    }
        var rtpAndTimeItem=[${rtpPacketLostRatioHourChartAxis.axisCustomer}];
     	
        toption = {
        		 title : {
        		        text: '呼叫建立时延异常与时间变化图'
        		    },
        		    tooltip : {
        		        trigger: 'axis',
        		        axisPointer:{
        		            show: true,
        		            type : 'cross',
        		            lineStyle: {
        		                type : 'dashed',
        		                width : 1
        		            }
        		        },
        		        formatter : function (params) {
        		        	 return params.seriesName + ' : [ '
        	                   + params.value[0] + ', ' 
        	                   + params.value[1] + ' ]';
        		        }
        		    },
        		    legend: {
        		        data:['呼叫建立时延异常值']
        		    },
        		    toolbox: {
        		        show : true,
        		        feature : {
        		            mark : {show: true},
        		            dataZoom : {show: true},
        		            dataView : {show: true, readOnly: false},
        		            magicType : {show: true, type: ['line', 'bar']},
        		            restore : {show: true},
        		            saveAsImage : {show: true}
        		        }
        		    },
        		    calculable : true,
        		    xAxis : [
        		             {
             		            type: 'category',
             		            name:'时间区域',
             		           
             		            axisLabel : {
             		                formatter: '{value} /min'
             		            },
         
             		            boundaryGap : false,
             		            data : rtpAndTimeItem
             		        }
        		       
        		    ],
        		    yAxis : [
        		             {
             		            type: 'value',
             		            name:'时延值'
             		        }
        		    ],
        		    series : [
        		        {
        		            name:'数据',
        		            type:'line',
        		            data:${empty sumRatioHourChart?"['-']":sumRatioHourChart},
        		            markPoint : {
        		                data : [
        		                    {type : 'max', name: '最大值'},
        		                    {type : 'min', name: '最小值'}
        		                ]
        		            },
        		            markLine : {
        		                data : [
        		                    {type : 'average', name: '平均值'}
        		                ]
        		            }
        		        }
        		        
        		       
        		    ]
        	};
		
		
		/* rsrp和sinr二维图 option */
		roption = {
			    tooltip : {
			        trigger: 'item'
			    },
			    toolbox: {
			        show : true,
			        feature : {
			            mark : {show: true},
			            dataZoom : {show: true},
			            dataView : {show: true, readOnly: false},
			            restore : {show: true},
			            saveAsImage : {show: true}
			        }
			    },
			    dataRange: {
			        min: 0,
			        max: 100,
			        y: 'center',
			        text:['高','低'],          
			        color:['lightgreen','yellow'],
			        calculable : true
			    },
			    xAxis : [
			        {
			        	name:'RSRP',
			            type:'category',
			            boundaryGap:true,
			            axisLabel:{interval:0},
			            axisTick:{show:true,interval:0},
			            splitLine:{show:true,onGap:true},
			            scale : true,
			            data:rtpAndSinrScatterItemX
			        }
			    ],
			    yAxis : [
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
			    series : [
			        {
			        	name:'占比',
			        	type:'scatter',
			        	symbolSize:rtpAndRsrpScatterSymbolSize,
			        	symbol:'rectangle',
			        	data:${empty rsrpAndSinrChart?"['-']":rsrpAndSinrChart}
			        
			        }
			    ]
			};
		
		
		
	</script>

	
  </head>
  
  <body class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<div  data-options="region:'north',border:false" style="height:30%;width: 100%;padding:0px 0px 2px 0px;">
    		<table id="coreNetRoadTable" class="easyui-datagrid" data-options="title:'核心网原因问题路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true" >
   				<thead>
   					<tr>
   						<th rowspan="2" data-options="field:'id',hidden:true"></th>
   						<th rowspan="2" data-options="field:'boxId',width:60,align:'center'" >BOXID</th>
   						<th colspan="4" >问题路段信息</th>
   						<th colspan="11" >问题描述</th>
   					</tr>
   					<tr>
   						<th data-options="field:'m_stRoadName',width:150,align:'center',formatter:showTooltip">路段名称</th>
   						<th data-options="field:'m_dbDistance',width:80,align:'center',formatter:numToFixed2Formatter">持续距离(m)</th>
   						<th data-options="field:'m_dbContinueTime',width:90,align:'center',
   							formatter: function(value,row,index){
								if (value){
									return value/1000;
								}
								return value;
							}">测试时间(s)</th>
   						<th data-options="field:'startTime2String',width:90,align:'center',formatter:showTooltip">起呼时间</th>
   						<th data-options="field:'callName',width:150,align:'center',formatter:showTooltip">服务小区</th>
   						<th data-options="field:'sinrAvg',width:90,align:'center',formatter:numToFixed2Formatter">SINR均<br>值(dBm)</th>
   						<th data-options="field:'rsrpAvg',width:90,align:'center',formatter:numToFixed2Formatter">RSRP均<br>值(dBm)</th>
   						<th data-options="field:'fileName',width:120,align:'center',formatter:showTooltip">测试日志名称</th>
   						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true">recSeqNo</th>
	    				<th data-options="field:'beginLatitude',width:120,align:'center',hidden:true">开始纬度</th>
	    				<th data-options="field:'courseLatitude',width:120,align:'center',hidden:true">中间纬度</th>
	    				<th data-options="field:'endLatitude',width:120,align:'center',hidden:true">结束纬度</th>
	    				<th data-options="field:'beginLongitude',width:120,align:'center',hidden:true">开始经度</th>
	    				<th data-options="field:'courseLongitude',width:120,align:'center',hidden:true">中间经度</th>
	    				<th data-options="field:'endLongitude',width:120,align:'center',hidden:true">结束经度</th>
   					</tr>
   				</thead>
   				<tbody>
   					<c:forEach items="${coreNetworkRoads}" var="coreNetworkRoad">
    					<tr>
		    						<td>${coreNetworkRoad.id}</td>
		    						<td>${coreNetworkRoad.testLogItem.boxId}</td>
		    						<td>${coreNetworkRoad.m_stRoadName}</td>
		    						<td>${coreNetworkRoad.m_dbDistance}</td>
		    						<td>${coreNetworkRoad.m_dbContinueTime}</td>
		    						<td>${coreNetworkRoad.startTime2String}</td>
		    						<td>${coreNetworkRoad.cellName}</td>
		    						<td>${coreNetworkRoad.sinrAvg}</td>
		    						<td>${coreNetworkRoad.rsrpAvg}</td>
		    						<td>${coreNetworkRoad.testLogItem.fileName}</td>
		    						<td>${coreNetworkRoad.testLogItem.recSeqNo}</td>
		    						<td>${coreNetworkRoad.beginLatitude}</td>
		    						<td>${coreNetworkRoad.courseLatitude}</td>
		    						<td>${coreNetworkRoad.endLatitude}</td>
		    						<td>${coreNetworkRoad.beginLongitude}</td>
		    						<td>${coreNetworkRoad.courseLongitude}</td>
		    						<td>${coreNetworkRoad.endLongitude}</td>
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
    			<div  data-options="region:'west',border:false" style="width: 100%;padding:0px 2px 0px 0px;">
    				<div id="rtpAndRsrpChart" class="easyui-panel" data-options="fit:true,title:'RSRP与SINR映射'">
		    		</div>
    			</div>
    			
    		</div>
    	</div>
  </body>
</html>
