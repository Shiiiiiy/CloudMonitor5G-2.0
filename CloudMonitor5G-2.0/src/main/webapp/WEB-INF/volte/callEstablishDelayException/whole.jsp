<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>呼叫建立时延异常整体分析</title>

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
	function getTestLogItemIds2CEDE(){
		return testLogItemIds;
	}
	
	//gisCommon会调用获取生成测试日志下所有类型连续无线差路段轨迹的url
	function getTLI2CEDEGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCEDEGpsPoint.action";
	}
	//gisCommon会调用获取事件图标类型
	function getIconType(){
		//叫建立时延异常切换失败图标类型(见数据库表定义中事件类型定义)
		return '21';
	}
	var everyCEDNumPieChart;
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
			everyCellNumPieChart.resize();
			everyCEDNumPieChart.resize();
		}, 100);
	}
	function initTable(){
		$.post("${pageContext.request.contextPath}/callEstablish/doCallEstablishDelayException.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#wholeIndexTable0").datagrid('loadData',result.wholeRoadIndex0);
					$("#wholeIndexTable1").datagrid('loadData',result.wholeRoadIndex2);
					
					
					var everyCellNum = result.wholeRoadIndex1.rows[0];
					if(everyCellNum){
						for(var everyCellNumIndex = 0 ; everyCellNumIndex < everyCellNumPieItem[0].length ; everyCellNumIndex++){
							if(everyCellNum[everyCellNumPieItem[1][everyCellNumIndex]]){
								everyCellNumPieOption.series[0].data.push({name:everyCellNumPieItem[0][everyCellNumIndex],value:everyCellNum[everyCellNumPieItem[1][everyCellNumIndex]]});
							}else{
								everyCellNumPieOption.series[0].data.push({name:everyCellNumPieItem[0][everyCellNumIndex],value:undefined});
							}
						}
					}
					var everyCEDNum = result.wholeRoadIndex3.rows[0];
					if(everyCEDNum){
						for(var everyCEDNumIndex = 0 ; everyCEDNumIndex < everyCEDNumPieItem[0].length ; everyCEDNumIndex++){
							if(everyCEDNum[everyCEDNumPieItem[1][everyCEDNumIndex]]){
								everyCEDNumPieOption.series[0].data.push({name:everyCEDNumPieItem[0][everyCEDNumIndex],value:everyCEDNum[everyCEDNumPieItem[1][everyCEDNumIndex]]});
							}else{
								everyCEDNumPieOption.series[0].data.push({name:everyCEDNumPieItem[0][everyCEDNumIndex],value:undefined});
							}
						}
					}
				}
			}
		,"json");
	}
		var everyCellNumPieItem=[['VoLTE呼叫接入时延','VoLTE呼叫RAB建立时延','VOLTE呼叫被叫寻呼时延','VoLTE呼叫被叫媒体类型上报时延','VoLTE呼叫主叫振铃时延','其他节点'],['callIn','callRAB','callFunld','callMov','callMain','callOther']];
		var everyCEDNumPieItem=[['弱覆盖','重叠覆盖','被叫位置更新','核心网问题','其他原因'],['weakCover','overlapCover','locationUpdate','coreNet','other']];
		
		/* 节点占比饼图Option */
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
		    series : [{name:'异常节点占比',type:'pie',radius:'90%',center: ['60%', '50%'],data:['']}]
		};
		/* 原因占比饼图option */
		var	everyCEDNumPieOption = {
		   
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:everyCEDNumPieItem[0]},
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    series : [{name:'异常原因占比',type:'pie',radius:'85%',center: ['60%', '50%'],data:['']}]
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
					everyCellNumPieChart = ec.init(document.getElementById("everyCellNumPieChart")); 
					everyCellNumPieChart.setOption(everyCellNumPieOption);
					everyCEDNumPieChart = ec.init(document.getElementById("everyCEDNumPieChart")); 
					everyCEDNumPieChart.setOption(everyCEDNumPieOption);
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
    			<iframe id="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=17" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
		    	<div data-options="region:'center',border:false" >
		    		<div class="easyui-layout" style="width:100%;height: 100%;">
				    	<!--VoLTE呼叫建立各阶段时延概览 -->
				    	<div data-options="region:'north',border:false" style="height:50%;padding:2px 0px 0px 0px;">
					    	
				    		<div class="easyui-panel" data-options="fit:true">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'north',border:false" style="height:108px;">
			    						<table id="wholeIndexTable0" class="easyui-datagrid" data-options="title:'VoLTE呼叫建立各阶段时延概览',scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false,tools:'#tt'"  >
						    				<thead>
						    					<tr>
						    						<th data-options="field:'callEstablishDelay',width:60,align:'center'">呼叫建立时延</th>
						    						<th data-options="field:'invite2rrcConnectionSeutpCompleteDelay',width:80,align:'center'">INVITE->RRC连接<br>建立完成时延</th>
						    						<th data-options="field:'rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay',width:90,align:'center'">RRC连接建立完成<br>->RRC连接重<br>配置时延</th>
						    						<th data-options="field:'invite1002calledPagingDelay',width:70,align:'center'">INVITE100-><br>被叫paging时延</th>
						    						<th data-options="field:'calledInvite2calledInvite183Delay',width:80,align:'center'">被叫INVITE->被叫<br>INVITE183时延</th>
						    						<th data-options="field:'callingInvite2callingInvite180RingingDelay',width:90,align:'center'">主叫INVITE183-><br>主叫INVITE180<br>Ringing时延</th>
						    					</tr>
						    				</thead>
						    				<tbody>
						    				<tr><td></td><td></td><td></td><td></td><td></td><td></td></tr>
						    				</tbody>
						    			</table>
						    			<div id="tt" >
											<a href="#" style="width:75px;text-decoration:underline;" onclick="goToPage('${pageContext.request.contextPath}/callEstablish/downloadCallEstablishDelayExceptionExcel.action');" title="详细报表导出">详细报表导出</a>
											<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
										</div>
			    					</div>
			    					<div id="everyCEDNumPieChart" data-options="region:'center',border:false" >
			    						
			    					</div>
			    				</div>
				    		</div>
				    	</div>
				    	<!-- VoLTE语音呼叫建立时延异常统计 -->
				    	<div data-options="region:'center',border:false" style="height:50%;padding:2px 0px 0px 0px;">
				    		<div class="easyui-panel" data-options="fit:true,title:'VoLTE语音呼叫建立时延异常统计'">
				    			<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'north',border:false" style="height:76px;">
			    						<table id="wholeIndexTable1" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false">
						    				<thead>
						    					<tr>
						    						<th data-options="field:'CEDESum',width:60,align:'center'">呼叫建立时延<br>异常次数</th>
						    						<th data-options="field:'tryCallSum',width:80,align:'center'">VoLTE呼叫<br>呼叫建立尝试次数</th>
						    						<th data-options="field:'housProportion',width:60,align:'center',formatter:numToFixed2Formatter">涉及小区占比(%)</th>
						    						<th data-options="field:'terminalProportion',width:70,align:'center',formatter:numToFixed2Formatter">涉及终端数占比(%)</th>
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
    	
  </body>
</html>