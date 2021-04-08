<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLte专题-连续无线差整体分析</title>

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
	function getTestLogItemIds2CWBR(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
	function getTLI2CWBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCWBRGpsPoint.action";
	}
	var everyCWBRNumPieChart;
	var everyCellNumPieChart;
	
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
			everyCWBRNumPieChart.resize();
			everyCellNumPieChart.resize();
		}, 100);
	}
	function initTable(){
		$.post("${pageContext.request.contextPath}/volteCWBR/doWholeRoadAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#wholeIndexTable0").datagrid('loadData',result.wholeRoadIndex0);
					$("#wholeIndexTable1").datagrid('loadData',result.wholeRoadIndex1);
					$("#wholeCellNumInfoTable").datagrid('loadData',result.wholeRoadIndex3);
					var everyCWBRNum = result.wholeRoadIndex2.rows[0];
					if(everyCWBRNum){
						for(var everyCWBRNumIndex = 0 ; everyCWBRNumIndex < everyCWBRNumPieItem[0].length ; everyCWBRNumIndex++){
							if(everyCWBRNum[everyCWBRNumPieItem[1][everyCWBRNumIndex]]){
								everyCWBRNumPieOption.series[0].data.push({name:everyCWBRNumPieItem[0][everyCWBRNumIndex],value:everyCWBRNum[everyCWBRNumPieItem[1][everyCWBRNumIndex]]});
							}else{
								everyCWBRNumPieOption.series[0].data.push({name:everyCWBRNumPieItem[0][everyCWBRNumIndex],value:undefined});
							}
						}
					}
					var everyCellNum = result.wholeRoadIndex3.rows[0];
					if(everyCellNum){
						for(var everyCellNumIndex = 0 ; everyCellNumIndex < everyCellNumPieItem[0].length ; everyCellNumIndex++){
							if(everyCellNum[everyCellNumPieItem[1][everyCellNumIndex]]){
								everyCellNumPieOption.series[0].data.push({name:everyCellNumPieItem[0][everyCellNumIndex],value:everyCellNum[everyCellNumPieItem[1][everyCellNumIndex]]});
							}else{
								everyCellNumPieOption.series[0].data.push({name:everyCellNumPieItem[0][everyCellNumIndex],value:undefined});
							}
						}
					}
					
				}
			}
		,"json");
	}

	
		
		var everyCWBRNumPieItem=[['弱覆盖','干扰','邻区配置','其他原因'],['weakCover','disturb','nbCell','otherProblem']];
		var everyCellNumPieItem=[['天馈调整','邻区调整','PCI调整'],['tianKuiCellNum','nbCellNum','pciCellNum']];
		/* 各质差路段数量占比饼图option */
		var	everyCWBRNumPieOption = {
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:everyCWBRNumPieItem[0]},
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    series : [{name:'质差路段占比',type:'pie',radius:'90%',center: ['60%', '50%'],data:['']}]
		};
		/* 所有质差路段各问题小区占比饼图Option */
		var	everyCellNumPieOption = {
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:everyCellNumPieItem[0]},
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    series : [{name:'问题小区占比',type:'pie',radius:'90%',center: ['60%', '50%'],data:['']}]
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
					everyCWBRNumPieChart = ec.init(document.getElementById("everyCWBRNumPieChart")); 
					everyCWBRNumPieChart.setOption(everyCWBRNumPieOption);
					everyCellNumPieChart = ec.init(document.getElementById("everyCellNumPieChart")); 
					everyCellNumPieChart.setOption(everyCellNumPieOption);
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
    			<iframe id="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=12" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
    			<!-- 指标列表 -->
    			<div data-options="region:'north',border:false" style="height:92px;padding:0px;">
	    			<table id="wholeIndexTable0"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:true,title:'指标列表',tools:'#tt'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'volteMosAvg',width:90,align:'center',formatter:numToFixed2Formatter">VOLTE平均<br>MOS</th>
	    						<th data-options="field:'distanceThreshold',width:120,align:'center',formatter:numToFixed2Formatter">连续无线差<br>距离门限(m)</th>
	    						<th data-options="field:'mileageProportion',width:120,align:'center',formatter:numToFixed2Formatter">连续无线差<br>里程占比(%)</th>
	    						<th data-options="field:'rtpDropRatio',width:80,align:'center',formatter:numToFixed2Formatter">RTP<br>丢包率</th>
	    						<th data-options="field:'rsrpAvg',width:80,align:'center',formatter:numToFixed2Formatter">平均<br>RSRP</th>
	    						<th data-options="field:'sinrAvg',width:80,align:'center',formatter:numToFixed2Formatter">平均<br>SINR</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:75px;text-decoration:underline;" onclick="goToPage('${pageContext.request.contextPath}/volteCWBR/downloadVolteContinueWirelessBadRoadExcel.action');" title="详细报表导出">详细报表导出</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<div data-options="region:'center',border:false" >
		    		<div class="easyui-layout" style="width:100%;height: 100%;">
				    	<!-- 质差问题路段统计 -->
				    	<div data-options="region:'north',border:false" style="height:50%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'连续无线差路段汇总'">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'north',border:false" style="height:65px;">
			    						<table id="wholeIndexTable1" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
						    				<thead>
						    					<tr>
						    						<th data-options="field:'roadNum',width:60,align:'center'">连续无线差<br>路段数量</th>
						    						<th data-options="field:'distance',width:50,align:'center',formatter:numToFixed2Formatter">总里程(米)</th>
						    						<th data-options="field:'continueTime',width:60,align:'center',formatter:numDivide1000Formatter">总测试时长(s)</th>
						    						<th data-options="field:'cellNumRatio',width:80,align:'center',formatter:numToFixed2Formatter">涉及小区占总<br>小区比例(%)</th>
						    						<th data-options="field:'terminalNumRatio',width:90,align:'center',formatter:numToFixed2Formatter">涉及终端占总测试<br>终端比例(%)</th>
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td><td></td><td></td><td></td></tr>
						    				</tbody>
						    			</table>
			    					</div>
			    					<div id="everyCWBRNumPieChart" data-options="region:'center',border:false" >
			    						
			    					</div>
			    				</div>
				    		</div>
				    	</div>
				    	<!-- 语音质差路段优化建议汇总 -->
				    	<div data-options="region:'center',border:false" style="height:50%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'连续无线差优化建议汇总'">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'north',border:false" style="height:76px;">
			    						<table id="wholeCellNumInfoTable" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  id="kpiInfoTable">
						    				<thead>
						    					<tr>
						    						<th rowspan="2" data-options="field:'totalCellNum',width:60,align:'center'">总涉及小区数</th>
						    						<th colspan="3" >各类调整涉及小区数占总小区数比例</th>
						    					</tr>
						    					<tr>
						    						<th data-options="field:'tianKuiCellNum',width:60,align:'center'">天馈调整</th>
						    						<th data-options="field:'nbCellNum',width:60,align:'center'">邻区调整</th>
						    						<th data-options="field:'pciCellNum',width:60,align:'center'">PCI调整</th>
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td><td></td><td></td></tr>
						    				</tbody>
						    			</table>
			    					</div>
			    					<div id="everyCellNumPieChart" data-options="region:'center',border:false">
			    						
			    					</div>
			    				</div>
				    		</div>
				    	</div>
				    	
				    </div>
			    </div>	
		    </div>
    	</div>
  </body>
</html>