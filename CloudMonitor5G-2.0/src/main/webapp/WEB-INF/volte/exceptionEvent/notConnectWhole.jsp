<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLte专题----VoLte异常事件----未接通事件整体分析</title>

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
	function getTestLogItemIds2EE(){
		return testLogItemIds;
	}
	//gisCommon会调用获取异常事件类型
	function getEEType(){
		//异常事件类型(0语音未接通,1语音掉话,2IMS注册失败,3CSFB失败)
		return 0;
	}
	//gisCommon会调用获取事件图标类型
	function getIconType(){
		//异常事件图标类型(见数据库表定义中事件类型定义)
		return 2;
	}
	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
	function getTLI2EEGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryEEGpsPoint.action";
	}
	var notConnectSignllingPieChart;
	$(function(){
		if(testLogItemIds){
			initTable();
		}
		window.setTimeout(function(){
			initChart();
		},2000);
	});
	//重新加载
	
	window.onresize=reload;
	function reload(){
		self.window.setTimeout(function(){
			notConnectSignllingPieChart.resize();
		}, 100);
	}
	function initTable(){
		$.post("${pageContext.request.contextPath}/voiceNotConnect/doWholeAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#wholeIndexTable0").datagrid('loadData',result.wholeIndex0);
					$("#wholeIndexTable1").datagrid('loadData',result.wholeIndex1);
					var notConnectSignlling = result.wholeIndex2.rows[0];
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
	
		var notConnectSignllingPieItem=[['主叫随机接入','主叫RRC建立','被叫寻呼','被叫随机接入','被叫RRC建立','主叫QCI=1的专载建立','被叫QCI=1的专载建立','主叫Invite交互','被叫invite交互'],['signallingNode0','signallingNode1','signallingNode2','signallingNode3','signallingNode4','signallingNode5','signallingNode6','signallingNode7','signallingNode8']];
		/* 所有失败信令节点占比饼图Option */
		var	notConnectSignllingPieOption = {
		    /* title : {text: '采样点数量占比统计',x:'30'}, */
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '0',data:notConnectSignllingPieItem[0]},
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
			$("#mainLayout").layout("collapse","east");
		}
		
	</script>
	
  </head>

  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe id="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=8" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
    			<!-- 指标列表 -->
    			<div data-options="region:'north',border:false" style="height:92px;padding:0px;">
	    			<table id="wholeIndexTable0"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'指标列表',tools:'#tt'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'voltecallestablishsuccessrate',width:80,align:'center',formatter:numToFixed2Formatter">VOLTE语音<br>建立成功率</th>
	    						<th data-options="field:'voltecallattemptmocount',width:80,align:'center'">VOLTE<br>试呼次数</th>
	    						<th data-options="field:'voltecallconnectmocount',width:80,align:'center'">VOLTE<br>接通次数</th>
	    						<th data-options="field:'volteringcount',width:80,align:'center'">VOLTE<br>振铃次数</th>
	    						<th data-options="field:'voltecallestablishdelay',width:80,align:'center',formatter:numToFixed2Formatter">呼叫<br>建立时延(s)</th>
	    						<th data-options="field:'rsrpaverage',width:80,align:'center',formatter:numToFixed2Formatter">平均RSRP</th>
	    						<th data-options="field:'sinraverage',width:80,align:'center',formatter:numToFixed2Formatter">平均SINR</th>
	    						<th data-options="field:'ltecoverage110rate',width:80,align:'center',formatter:numToFixed2Formatter">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr>
		    					<td></td>
		    					<td></td>
		    					<td></td>
		    					<td></td>
		    					<td></td>
		    					<td></td>
		    					<td></td>
		    					<td></td>
	    					</tr>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<div data-options="region:'center',border:false" >
		    		<div class="easyui-layout" style="width:100%;height: 100%;">
				    	<!-- 语音未接通问题统计 -->
				    	<div data-options="region:'north',border:false" style="height:82px;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'语音未接通统计'">
	    						<table id="wholeIndexTable1" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
				    				<thead>
				    					<tr>
				    						<th data-options="field:'eventNum',width:60,align:'center'">语音未接通数量</th>
				    						<th data-options="field:'cellNumRatio',width:50,align:'center',formatter:numToFixed2Formatter">涉及小区占比</th>
				    						<th data-options="field:'terminalNumRatio',width:60,align:'center',formatter:numToFixed2Formatter">涉及终端数占比</th>
				    					</tr>
				    				</thead>
				    				<tbody>
				    					<tr><td></td><td></td><td></td></tr>
				    				</tbody>
				    			</table>
				    		</div>
				    	</div>
				    	<!-- 语音未接通信令节点饼图 -->
				    	<div data-options="region:'center',border:false" style="height:50%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" style="padding:20px;" data-options="fit:true,title:'语音未接通信令节点分步'">
		    					<div id="notConnectSignllingPieChart" style="width:100%;height:100%;" >
		    						
		    					</div>
				    		</div>
				    	</div>
				    	
				    </div>
			    </div>	
		    </div>
    	</div>
  </body>
</html>