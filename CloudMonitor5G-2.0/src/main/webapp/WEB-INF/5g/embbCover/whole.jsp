<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLte专题整体分析</title>

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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">

	$(function(){	
		initChart();
		displayVenn();
	});
	//重新加载
	var everyQBRNumPieChart;
	window.onresize=reload;
	function reload(){
		setTimeout(function(){
			everyQBRNumPieChart.resize();
		},200);
		
	}

		var	everyQBRNumPieOption = {
		  	title:{
		  		text:'持\n续\n距\n离\n(m)',
		  		y:'center',
		  		padding:20
		  	},
		  	color: [ 
	  	       '#7fb2e8', '#434341', '#90ec7d', '#f6a45d' 
	  	   	],
		    tooltip : {trigger: 'axis'},
		    legend: {
		    	y:'bottom',
		    	padding:10,
		        data:['持续距离','最长持续距离','最短持续距离','平均持续距离']
		    },
		    grid:{y2:110},
		    toolbox: {show : false},
		    xAxis : [{
	            type : 'category',
	            data : ['弱覆盖','过覆盖','重叠覆盖','弱覆盖&\n过覆盖','弱覆盖&\n重叠覆盖','过覆盖&\n重叠覆盖','弱覆盖&\n过覆盖&\n重叠覆盖'],
	            axisLabel:{rotate:45}
		    }],
		    yAxis : [{
	        	name:'距离(m)',
	            type : 'value'
		    }],
		    series : [{
	            name:'持续距离',
	            type:'bar',
	            data:[${distanceMap.weakCoverDistance},${distanceMap.overCoverDistance},${distanceMap.overlappingCoverDistance},${distanceMap.weakAndOverCoverDistance},${distanceMap.weakAndOverlappingCoverDistance},${distanceMap.overAndOverlappingCoverDistance},${distanceMap.weakAndOverAndOverlappingCoverDistance}]
	        },
	        {
	            name:'最长持续距离',
	            type:'bar',
	            data:[${distanceMaxMap.weakCoverMaxDistance},${distanceMaxMap.overCoverMaxDistance},${distanceMaxMap.overlappingCoverMaxDistance},${distanceMaxMap.weakAndOverCoverMaxDistance},${distanceMaxMap.weakAndOverlappingCoverMaxDistance},${distanceMaxMap.overAndOverlappingCoverMaxDistance},${distanceMaxMap.weakAndOverAndOverlappingCoverMaxDistance}]
	        },
	        {
	            name:'最短持续距离',
	            type:'bar',
	            data:[${distanceMinMap.weakCoverMinDistance},${distanceMinMap.overCoverMinDistance},${distanceMinMap.overlappingCoverMinDistance},${distanceMinMap.weakAndOverCoverMinDistance},${distanceMinMap.weakAndOverlappingCoverMinDistance},${distanceMinMap.overAndOverlappingCoverMinDistance},${distanceMinMap.weakAndOverAndOverlappingCoverMinDistance}]
	        },
	        {
	            name:'平均持续距离',
	            type:'bar',
	            data:[${distanceAvgMap.weakCoverAvgDistance},${distanceAvgMap.overCoverAvgDistance},${distanceAvgMap.overlappingCoverAvgDistance},${distanceAvgMap.weakAndOverCoverAvgDistance},${distanceAvgMap.weakAndOverlappingCoverAvgDistance},${distanceAvgMap.overAndOverlappingCoverAvgDistance},${distanceAvgMap.weakAndOverAndOverlappingCoverAvgDistance}]
		        
			}]
		};
		
		/* Chart路径配置 */
        require.config({
            paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
		/* 初始化图表 */
        function initChart(){
			require(
				['echarts','echarts/chart/bar'],
				function (ec) {
					everyQBRNumPieChart = ec.init(document.getElementById("distanceChart")); 
					everyQBRNumPieChart.setOption(everyQBRNumPieOption);
				}
			); 
	    }
		
		//显示韦恩图
		function displayVenn(){
			var url = '${pageContext.request.contextPath}/embbCover5g/goToWholeVenn.action';
			$('#timeLongChart').panel({
				content : createIframe(url)
			});
		}
	</script>
	
  </head>
  <body id="mainLayout" class="easyui-layout"  style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
	<div data-options="region:'north',border:false" style="height:243px;">
		<div class="easyui-layout" style="width:100%;height: 100%;">
			<div data-options="region:'west',border:false" style="width:25%;padding:0px 2px 2px 0px;">
				<div class='easyui-panel' data-options="fit:true,title:'路段总数'">
					<table style="width:100%;height:100%;border-spacing:4px;">
						<tr style="width:100%;height:33.3%;">
							<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/weakCover.png" />弱覆盖</td>
							<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${roadNumMap.weakCoverNum}</td>
						</tr>
						<tr style="width:100%;height:33.4%;">
							<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overCover.png" />过覆盖</td>
							<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${roadNumMap.overCoverNum}</td>
						</tr>
						<tr style="width:100%;height:33.3%;">
							<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overlappingCover.png" />重叠覆盖</td>
							<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${roadNumMap.overlappingCoverNum}</td>
						</tr>
					</table>
				</div>
			</div>
	    	<div data-options="region:'center',border:false" style="width:50%;">
    			<div class="easyui-layout" style="width:100%;height: 100%;">
    				<div data-options="region:'west',border:false" style="width:50%;padding:0px 2px 2px 2px;">
    					<div class='easyui-panel' data-options="fit:true,title:'SINR'">
    						<table style="width:100%;height:100%;border-spacing:4px;">
								<tr style="width:100%;height:33.3%;">
									<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/weakCover.png" />弱覆盖</td>
									<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${sinrAvgMap.weakCoverSinrAvg}</td>
								</tr>
								<tr style="width:100%;height:33.4%;">
									<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overCover.png" />过覆盖</td>
									<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${sinrAvgMap.overCoverSinrAvg}</td>
								</tr>
								<tr style="width:100%;height:33.3%;">
									<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overlappingCover.png" />重叠覆盖</td>
									<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${sinrAvgMap.overlappingCoverAvg}</td>
								</tr>
							</table>
    					</div>
    				</div>
    				<div data-options="region:'center',border:false" style="width:50%;padding:0px 2px 2px 2px;">
    					<div class='easyui-panel' data-options="fit:true,title:'FTP上传速率'">
    						<table style="width:100%;height:100%;border-spacing:4px;">
								<tr style="width:100%;height:33.3%;">
									<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/weakCover.png" />弱覆盖</td>
									<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${ftpUpSpeedMap.weakCoverFtpUpSpeed}kbps</td>
								</tr>
								<tr style="width:100%;height:33.4%;">
									<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overCover.png" />过覆盖</td>
									<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${ftpUpSpeedMap.overCoverFtpUpSpeed}kbps</td>
								</tr>
								<tr style="width:100%;height:33.3%;">
									<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overlappingCover.png" />重叠覆盖</td>
									<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${ftpUpSpeedMap.overlappingCoverFtpUpSpeed}kbps</td>
								</tr>
							</table>
    					</div>
    				</div>
    			</div>
	    	</div>
	    	<div data-options="region:'east',border:false" style="width:25%;padding:0px 0px 2px 2px;">
    			<div class='easyui-panel' data-options="fit:true,title:'FTP下载速率'">
    				<table style="width:100%;height:100%;border-spacing:4px;">
						<tr style="width:100%;height:33.3%;">
							<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/weakCover.png" />弱覆盖</td>
							<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${ftpDlSpeedMap.weakCoverFtpDlSpeed}kbps</td>
						</tr>
						<tr style="width:100%;height:33.4%;">
							<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overCover.png" />过覆盖</td>
							<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${ftpDlSpeedMap.overCoverFtpDlSpeed}kbps</td>
						</tr>
						<tr style="width:100%;height:33.3%;">
							<td style="width:70%;background-color:#e9f8ff;text-align:center;color:#3b507d;"><img style="width:61px;height:61px;vertical-align:middle;margin-right:15px;" src="${pageContext.request.contextPath}/images/overlappingCover.png" />重叠覆盖</td>
							<td style="background-color:#e9f8ff;text-align:center;font-weight:bold;color:#3b507d;">${ftpDlSpeedMap.overlappingCoverFtpDlSpeed}kbps</td>
						</tr>
					</table>
    			</div>
	    	</div>
		</div>
	</div>
	<div  data-options="region:'center',border:false" >
   		<div class="easyui-layout" style="width:100%;height: 100%;">
   			<div data-options="region:'west',border:false" style="width:50%;padding:2px 2px 0px 0px;">
				<div class='easyui-panel' data-options="fit:true,title:'持续距离'">
					<div id="distanceChart" style="width:100%;height:100%;">
					
					</div>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="width:50%;padding:2px 0px 0px 2px;">
				<div id="timeLongChart" class='easyui-panel' data-options="fit:true,title:'测试时长'">
				</div>
			</div>
   		</div>
   	</div>
  </body>
</html>
