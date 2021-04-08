<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE质量专题----语音RTP连续丢包整体分析</title>

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
	$.ajaxSetup({async:false});
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//gisCommon会调用获取测试日志ID
	function getTestLogItemIds2LP(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成测试日志下所有连续丢包路段轨迹的url
	function getTLI2LPGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryLPGpsPoint.action";
	}
	var notConnectSignllingPieChart;
	$(function(){
		if(testLogItemIds){
			initTable();
		}
		window.setTimeout(function(){
			initChart();
		},2000);
		setTimeout(function(){mapIframe.window.drawLostPacket();},2000);
	});
	//重新加载
	
	window.onresize=reload;
	function reload(){
		self.window.setTimeout(function(){
			notConnectSignllingPieChart.resize();
		}, 100);
	}
	function initTable(){
		$.post("${pageContext.request.contextPath}/lostPacket/doWholeAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#wholeIndexTable0").datagrid('loadData',result.wholeIndex0);
					var notConnectSignlling = result.wholeIndex1.rows[0];
					if(notConnectSignlling){
						for(var notConnectSignllingIndex = 0 ; notConnectSignllingIndex < notConnectSignllingPieItem[0].length ; notConnectSignllingIndex++){
							if(notConnectSignlling[notConnectSignllingPieItem[1][notConnectSignllingIndex]]){
								notConnectSignllingPieOption.series[0].data.push({name:notConnectSignllingPieItem[0][notConnectSignllingIndex],value:notConnectSignlling[notConnectSignllingPieItem[1][notConnectSignllingIndex]]});
							}else{
								notConnectSignllingPieOption.series[0].data.push({name:notConnectSignllingPieItem[0][notConnectSignllingIndex],value:undefined});
							}
						}
					}
				}
			}
		,"json");
	}
	
		var notConnectSignllingPieItem=[['发送端手机上行丢包','发送端S1口丢包','发送端SGi口丢包','接收端SGi口丢包','接收端S1口丢包','接收端Uu口丢包'],['node1','node2','node3','node4','node5','node6']];
		/* 所有失败信令节点占比饼图Option */
		var	notConnectSignllingPieOption = {
		    /* title : {text: '采样点数量占比统计',x:'30'}, */
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '0',data:notConnectSignllingPieItem[0]},
		    color:[
		           '#ff00ff', '#ff0000', '#feff00', '#00ff00',
		           '#00feff', '#0000ff' 
		       ],
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true, 
		    series : [{name:'信令节点分布',type:'pie',radius:'60%',center: ['60%', '50%'],data:['']}]
		};
		
		/* Chart路径配置 */
        require.config({
            paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
		/* 初始化图表 */
        function initChart(){
			require(
				['echarts','echarts/chart/pie'],
				function (ec) {
					notConnectSignllingPieChart = ec.init(document.getElementById("notConnectSignllingPieChart")); 
					notConnectSignllingPieChart.setOption(notConnectSignllingPieOption);
				}
			);
	    }
		function hiddenLayout(){
			$("#sonLayout").layout("collapse","east");
		}
		
	</script>
	
  </head>

  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div id="sonLayout" class="easyui-layout" style="width:100%;height: 100%;">
    			<!-- 中,地图界面 -->
    			<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
	    			<div class="easyui-panel" data-options="fit:true">
	    				<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=5" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
	    			</div>
    			</div>
    			<!-- 语音未接通信令节点饼图 -->
    			<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
	    			<div class="easyui-panel" style="padding:20px;" data-options="fit:true,title:'语音RTP连续丢包节点分布',tools:'#tt'">
				   	    <div id="notConnectSignllingPieChart" style="width:100%;height:100%;" >
				    	</div>
					</div>
				</div>
    		</div>
    	</div>
    	<div id="tt" >
			<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
		</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	
    			<!-- 指标列表 -->
    			<div data-options="region:'north',border:false" style="height:92px;padding:0px;">
	    			<table id="wholeIndexTable0"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'语音RTP连续丢包问题路段汇总'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'problemNumber',width:80,align:'center'">问题数量</th>
	    						<th data-options="field:'testTime',width:80,align:'center',formatter:numDivide1000Formatter">总测试时长(s)</th>
	    						<!-- <th data-options="field:'MosAvg',width:80,align:'center'">MOS均值</th> -->
	    						<th data-options="field:'mainCallNumber',width:80,align:'center'">主叫个数</th>
	    						<th data-options="field:'callsNumber',width:80,align:'center'">被叫个数</th>
	    						<th data-options="field:'succeedRelevance',width:80,align:'center'">成功关联网络侧问题数</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr>
		    					<td></td>
		    					<td></td>
		    					<!-- <td></td> -->
		    					<td></td>
		    					<td></td>
		    					<td></td>
	    					</tr>
	    				</tbody>
	    			</table>
		    	</div>
  </body>
</html>