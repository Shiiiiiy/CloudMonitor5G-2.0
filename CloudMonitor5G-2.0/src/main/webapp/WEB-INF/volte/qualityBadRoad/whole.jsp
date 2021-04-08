<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	$.ajaxSetup({async:false});
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//gisCommon会调用获取测试日志ID
	function getTestLogItemIds2QBR(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
	function getTLI2QBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryQBRGpsPoint.action";
	}
	var everyQBRNumPieChart;
	var everyCellNumPieChart;
	/* 2016-04-08需求讨论去除 */
	//var everyNetworkMosPointPieChart;
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
			everyQBRNumPieChart.resize();
			everyCellNumPieChart.resize();
			/* 2016-04-08需求讨论去除 */
			//everyNetworkMosPointPieChart.resize();
		}, 100);
	}
	function initTable(){
		$.post("${pageContext.request.contextPath}/voiceQBR/doWholeRoadAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#wholeIndexTable0").datagrid('loadData',result.wholeRoadIndex0);
					$("#wholeIndexTable1").datagrid('loadData',result.wholeRoadIndex1);
					$("#wholeCellNumInfoTable").datagrid('loadData',result.wholeRoadIndex3);
					/* 2016-04-08需求讨论去除 */
					//$("#wholeIndexTable2").datagrid('loadData',result.wholeRoadIndex4);
					var everyQBRNum = result.wholeRoadIndex2.rows[0];
					if(everyQBRNum){
						for(var everyQBRNumIndex = 0 ; everyQBRNumIndex < everyQBRNumPieItem[0].length ; everyQBRNumIndex++){
							if(everyQBRNum[everyQBRNumPieItem[1][everyQBRNumIndex]]){
								everyQBRNumPieOption.series[0].data.push({name:everyQBRNumPieItem[0][everyQBRNumIndex],value:everyQBRNum[everyQBRNumPieItem[1][everyQBRNumIndex]]});
							}else{
								everyQBRNumPieOption.series[0].data.push({name:everyQBRNumPieItem[0][everyQBRNumIndex],value:undefined});
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
					/* 2016-04-08需求讨论去除 */
					/* var everyNetworkMosPoint = result.wholeRoadIndex4.rows[0];
					if(everyNetworkMosPoint){
						for(var everyNetworkMosPointIndex = 0 ; everyNetworkMosPointIndex < everyNetworkMosPointPieItem[0].length ; everyNetworkMosPointIndex++){
							if(everyNetworkMosPoint[everyNetworkMosPointPieItem[1][everyNetworkMosPointIndex]]){
								everyNetworkMosPointPieOption.series[0].data.push({name:everyNetworkMosPointPieItem[0][everyNetworkMosPointIndex],value:everyNetworkMosPoint[everyNetworkMosPointPieItem[1][everyNetworkMosPointIndex]]});
							}else{
								everyNetworkMosPointPieOption.series[0].data.push({name:everyNetworkMosPointPieItem[0][everyNetworkMosPointIndex],value:undefined});
							}
						}
					} */
				}
			}
		,"json");
	}

		/* 2016-02-24需求讨论去除核心网相关业务 */
		/* var everyQBRNumPieItem=[['弱覆盖','干扰','邻区配置','参数配置','核心网问题','其他原因'],['weakCover','disturb','nbCell','paramError','coreNetwork','otherProblem']]; */
		var everyQBRNumPieItem=[['弱覆盖','干扰','邻区配置','参数配置','其他原因'],['weakCover','disturb','nbCell','paramError','otherProblem']];
		var everyCellNumPieItem=[['天馈调整','参数调整','邻区调整','PCI调整'],['tianKuiCellNum','paramCellNum','nbCellNum','pciCellNum']];
		/* 2016-04-08需求讨论去除 */
		//var everyNetworkMosPointPieItem=[['主被叫LTE','主叫LTE,被叫2/3G','主叫2/3G,被叫LTE','主被叫2/3G'],['lteLteMosPoint','lteCsMosPoint','csLteMosPoint','csCsMosPoint']];
		/* 各质差路段数量占比饼图option */
		var	everyQBRNumPieOption = {
		    /* title : {text: '采样点数量占比统计',x:'30'}, */
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:everyQBRNumPieItem[0]},
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
		    /* title : {text: '采样点数量占比统计',x:'30'}, */
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
		
		/* 2016-04-08需求讨论去除 */
		/* 采样点数量饼图数据 */
		//var	everyNetworkMosPointPieOption = {
		    /* title : {text: '采样点数量占比统计',x:'30'}, */
			//tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    //legend: {orient : 'vertical',x : '30',data:everyNetworkMosPointPieItem[0]},
		    //toolbox: {show : true,
		    	//feature : {
		            //restore : {show: true},
		            //saveAsImage : {show: true}
		        //}
		    //},
		    //calculable : true,
		    //series : [{name:'MOS采样点占比',type:'pie',radius:'90%',center: ['60%', '50%'],data:['']}]
		//};
		
		/* Chart路径配置 */
        require.config({
            paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
		/* 初始化图表 */
        function initChart(){
			require(
				['echarts','echarts/chart/pie'],
				function (ec) {
					everyQBRNumPieChart = ec.init(document.getElementById("everyQBRNumPieChart")); 
					everyQBRNumPieChart.setOption(everyQBRNumPieOption);
					everyCellNumPieChart = ec.init(document.getElementById("everyCellNumPieChart")); 
					everyCellNumPieChart.setOption(everyCellNumPieOption);
					/* 2016-04-08需求讨论去除 */
					//everyNetworkMosPointPieChart = ec.init(document.getElementById("everyNetworkMosPointPieChart")); 
					//everyNetworkMosPointPieChart.setOption(everyNetworkMosPointPieOption);
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
    			<iframe id="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=0" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
    			<!-- 指标列表 -->
    			<div data-options="region:'north',border:false" style="height:92px;padding:0px;">
	    			<table id="wholeIndexTable0"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'指标列表',tools:'#tt'" >
	    				<thead>
	    					<!-- <tr>
	    						<th data-options="field:'callType',width:40,align:'center'"></th>
	    						<th data-options="field:'volteMosAvg',width:90,align:'center',formatter:numToFixed2Formatter">VOLTE平均<br>MOS</th>
	    						<th data-options="field:'firstBler',width:80,align:'center',formatter:numToFixed2Formatter">初始BLER</th>
	    						<th data-options="field:'residualBler',width:80,align:'center',formatter:numToFixed2Formatter">残留BLER</th>
	    						<th data-options="field:'rtpDropRatio',width:80,align:'center',formatter:numToFixed2Formatter">RTP丢包率</th>
	    						<th data-options="field:'rtpShake',width:60,align:'center'">RTP抖动<br>(ms)</th>
	    						<th data-options="field:'tdlTestTime',width:100,align:'center',formatter:numToFixed2Formatter">TDL驻网时长<br>(小时)</th>
	    						<th data-options="field:'tdsTestTime',width:100,align:'center',formatter:numToFixed2Formatter">TDS驻网时长<br>(小时)</th>
	    						<th data-options="field:'gsmTestTime',width:100,align:'center',formatter:numToFixed2Formatter">GSM驻网时长<br>(小时)</th>
	    					</tr> -->
	    					<tr>
	    						<th data-options="field:'volteMosAvg',width:90,align:'center',formatter:numToFixed2Formatter">VOLTE平均<br>MOS</th>
	    						<!-- <th ata-options="field:'psMosOver30Rate',width:100,align:'center',formatter:numToFixed2Formatter">MOS>=3.0<br>占比</th>
	    						<th data-options="field:'psMosOver35Rate',width:100,align:'center',formatter:numToFixed2Formatter">MOS>=3.5<br>占比</th> -->
	    						<th data-options="field:'rtpDropRatio',width:80,align:'center',formatter:numToFixed2Formatter">RTP<br>丢包率</th>
	    						<th data-options="field:'rtpShake',width:80,align:'center'">RTP<br>抖动(ms)</th>
	    						<th data-options="field:'lteHoSuccRate',width:100,align:'center',formatter:numToFixed2Formatter">LTE HO<br>成功率</th>
	    						<th data-options="field:'rsrpAvg',width:80,align:'center',formatter:numToFixed2Formatter">平均<br>RSRP</th>
	    						<th data-options="field:'lteCoverRate',width:80,align:'center',formatter:numToFixed2Formatter">LTE<br>覆盖率</th>
	    						<th data-options="field:'sinrAvg',width:80,align:'center',formatter:numToFixed2Formatter">平均<br>SINR</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr><!-- <td></td><td></td> --><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:75px;text-decoration:underline;" onclick="goToPage('${pageContext.request.contextPath}/voiceQBR/downloadVolteQBRExcel.action');" title="详细报表导出">详细报表导出</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<div data-options="region:'center',border:false" >
		    		<div class="easyui-layout" style="width:100%;height: 100%;">
				    	<!-- 质差问题路段统计 -->
				    	<div data-options="region:'north',border:false" style="height:50%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'质差问题路段统计'">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'north',border:false" style="height:65px;">
			    						<table id="wholeIndexTable1" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
						    				<thead>
						    					<tr>
						    						<th data-options="field:'roadNum',width:60,align:'center'">语音质差路段<br>数量</th>
						    						<th data-options="field:'distance',width:50,align:'center',formatter:numToFixed2Formatter">总里程(米)</th>
						    						<th data-options="field:'continueTime',width:60,align:'center',formatter:numDivide1000Formatter">总测试时长(s)</th>
						    						<th data-options="field:'mosPointNumRatio',width:120,align:'center',formatter:numToFixed2Formatter">MOS采样点占总MOS采样<br>点数比例(%)</th>
						    						<th data-options="field:'cellNumRatio',width:80,align:'center',formatter:numToFixed2Formatter">涉及小区占总<br>小区比例(%)</th>
						    						<th data-options="field:'terminalNumRatio',width:90,align:'center',formatter:numToFixed2Formatter">涉及终端占总测试<br>终端比例(%)</th>
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr>
						    				</tbody>
						    			</table>
			    					</div>
			    					<div id="everyQBRNumPieChart" data-options="region:'center',border:false" >
			    						
			    					</div>
			    				</div>
				    		</div>
				    	</div>
				    	<!-- 语音质差路段优化建议汇总 -->
				    	<div data-options="region:'center',border:false" style="height:50%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'语音质差路段优化建议汇总'">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'north',border:false" style="height:76px;">
			    						<table id="wholeCellNumInfoTable" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  id="kpiInfoTable">
						    				<thead>
						    					<tr>
						    						<th rowspan="2" data-options="field:'totalCellNum',width:60,align:'center'">总涉及小区数</th>
						    						<th colspan="4" >各类调整涉及小区数占总小区数比例</th>
						    					</tr>
						    					<tr>
						    						<th data-options="field:'tianKuiCellNum',width:60,align:'center'">天馈调整</th>
						    						<th data-options="field:'paramCellNum',width:60,align:'center'">参数调整</th>
						    						<th data-options="field:'nbCellNum',width:60,align:'center'">邻区调整</th>
						    						<th data-options="field:'pciCellNum',width:60,align:'center'">PCI调整</th>
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td><td></td><td></td><td></td></tr>
						    				</tbody>
						    			</table>
			    					</div>
			    					<div id="everyCellNumPieChart" data-options="region:'center',border:false">
			    						
			    					</div>
			    				</div>
				    		</div>
				    	</div>
				    	<!-- 主被叫终端位于不同制式的MOS指标 -->
				    	<!-- 2016-04-08需求讨论去除
				    	<div data-options="region:'south',border:false" style="height:35%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'主被叫终端位于不同制式的MOS指标'">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'north',border:false" style="height:90px;">
			    						<table id="wholeIndexTable2" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  id="kpiInfoTable">
						    				<thead>
						    					<tr>
						    						<th colspan="2" >主被叫都占用LTE</th>
						    						<th colspan="2" >主叫占用LTE,被叫占用2/3G</th>
						    						<th colspan="2" >主叫占用2/3G,被叫占用LTE</th>
						    						<th colspan="2" >主被叫都在2/3G</th>
						    					</tr>
						    					<tr>
						    						<th data-options="field:'lteLteCallingMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">主叫VOLTE<br>平均MOS</th>
						    						<th data-options="field:'lteLteCalledMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">被叫VOLTE<br>平均MOS</th>
						    						<th data-options="field:'lteCsCallingMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">主叫VOLTE<br>平均MOS</th>
						    						<th data-options="field:'lteCsCalledMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">被叫CS域<br>MOS均值</th>
						    						<th data-options="field:'csLteCallingMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">主叫CS域<br>MOS均值</th>
						    						<th data-options="field:'csLteCalledMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">被叫VOLTE<br>平均MOS</th>
						    						<th data-options="field:'csCsCallingMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">主叫CS域<br>MOS均值</th>
						    						<th data-options="field:'csCsCalledMosValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">被叫CS域<br>MOS均值</th>
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
			    					</div>
			    					<div id="everyNetworkMosPointPieChart" data-options="region:'center',border:false">
			    						
			    					</div>
			    				</div>
				    		</div>
				    	</div> -->
				    </div>
			    </div>	
		    </div>
    	</div>
  </body>
</html>