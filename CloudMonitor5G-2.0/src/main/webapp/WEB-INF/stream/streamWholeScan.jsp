<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>流媒体专题----整体概览</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<%@ include file="../../taglibs/echarts.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
		 var testLogItemIds = '${testLogItemIds}';
		 var everyCWBRNumPieChart;
		 var everyCWBRNumPieChart2;
		// field用于匹配远程json属性，width宽度,align居左右中对齐
		var testLogInfoColumns=[[
			{field:'testTotalDistance',width:75,align:'center',title:'测试总里程<br>(KM)'}, 				
			{field:'startDate',width:120,align:'center',title:'文件开始时间'},
			{field:'endDate',width:120,align:'center',title:'文件结束时间'},
			{field:'testTotalTime',width:75,align:'center',title:'测试总时长'},
			{field:'testlogcellnum',width:60,align:'center',title:'测试涉及<br>小区数'},
			{field:'testlogmmenum',width:60,align:'center',title:'测试涉及<br>MME数'},
			{field:'testTotalServiceType',width:105,align:'center',title:'测试占比最高的<br>业务类型',
				formatter:function(value,row,index){
					if (value=="0,"){
						return '空闲测试';
					}else if(value=="1,"){
						return '语音测试';
					}else if(value=="2,"){
						return 'VoLTE视频电话';
					}else if(value=="3,"){
						return 'PDP/Attach';
					}else if(value=="4,"){
						return 'Ping测试';
					}else if(value=="5,"){
						return 'FTP上传/下载';
					}else if(value=="6,"){
						return '流媒体测试';
					}else if(value=="7,"){
						return 'Http测试';
					}
					return value;
				}
			},
			{field:'testTotalTerminalGroup',width:60,align:'center',title:'主要测试<br>行政区域'},
			{field:'testlogdfreqtime',width:75,align:'center',title:'D频段测试<br>时长占比'},
			{field:'testlogffreqtime',width:75,align:'center',title:'F频段测试<br>时长占比'},
			{field:'testTotalSpeedAvg',width:60,align:'center',title:'平均车速<br>(km/h)'},
			{field:'testTotalTerminalNum',width:60,align:'center',title:'测试终端<br>数量'}
		]];
		// field用于匹配远程json属性，width宽度,align居左右中对齐
		var kpiInfoColumns=[[
			{field:'streamattemptcount',width:45,align:'center',title:'<span title="流媒体业务发起次数">流媒体业务<br>发起次数</span>',formatter:showTooltip},
			//{field:'streamsuccesscount',width:45,align:'center',title:'<span title="业务成功次数">业务成功次数</span>',formatter:showTooltip},
			{field:'streamsuccessrate',width:45,align:'center',title:'<span title="流媒体业务成功率">流媒体业<br>务成功率</span>',formatter:showTooltip},
			{field:'streamrealplaydelay',width:45,align:'center',title:'<span title="流媒体加载时延(s)">流媒体加<br>载时延(s)</span>',formatter:showTooltip},
			{field:'streampausetime',width:45,align:'center',title:'<span title="流媒体卡顿时长(s)">流媒体卡<br>顿时长(s)</span>',formatter:showTooltip},
			{field:'streampausecount',width:45,align:'center',title:'<span title="流媒体播放卡顿次数">流媒体播放<br>卡顿次数</span>',formatter:showTooltip},
			{field:'streamplayovertimerate',width:45,align:'center',title:'<span title="流媒体播放超时比例">流媒体播放<br>超时比例</span>',formatter:showTooltip},
			{field:'streamdlthrputsuccess',width:45,align:'center',title:'<span title="应用层下载速率(不含掉线)(kbps)">应用层下载<br>速率(不含掉线)<br>(kbps)</span>',formatter:showTooltip},
			{field:'streamrealplaythrput',width:45,align:'center',title:'<span title="流媒体加载速率(kbps)">流媒体加载<br>速率<br>(kbps)</span>',formatter:showTooltip},
			{field:'rsrpaverage',width:45,align:'center',title:'<span title="平均RSRP">平均<br>RSRP</span>',formatter:showTooltip},
			{field:'ltecoverage110rate',width:45,align:'center',title:'<span title="LTE覆盖率">LTE<br>覆盖率</span>',formatter:showTooltip},
			{field:'sinraverage',width:45,align:'center',title:'<span title="平均SINR">平均<br>SINR</span>',formatter:showTooltip}
		]]; 
		var kpiInfoColumns2=[[
			{title:'会话级指标',colspan:4},                 
			{title:'实时指标',colspan:4}
		],[
			{field:'strmpauseratesession',width:35,align:'center',title:'<span title="卡顿比例">卡顿比例</span>',formatter:showTooltip},
			{field:'strmbuffertimesession',width:35,align:'center',title:'<span title="初始缓冲时延(ms)">初始缓冲<br>时延(ms)</span>',formatter:showTooltip},
			{field:'strmthrputsession',width:35,align:'center',title:'<span title="视频全程感知速率(kbps)">视频全程<br>感知速率<br>(kbps)</span>',formatter:showTooltip},
			{field:'strmvmosavgsession',width:40,align:'center',title:'<span title="VMOS均值">VMOS均值</span>',formatter:showTooltip},
			{field:'strmpauseratesample',width:35,align:'center',title:'<span title="卡顿比例">卡顿比例</span>',formatter:showTooltip},
			{field:'strmbuffertimesample',width:35,align:'center',title:'<span title="初始缓冲时延(ms)">初始缓冲<br>时延(ms)</span>',formatter:showTooltip},
			{field:'strmthrputsample',width:35,align:'center',title:'<span title="视频全程感知速率(kbps)">视频全程<br>感知速率<br>(kbps)</span>',formatter:showTooltip},
			{field:'strmvmosavgsample',width:40,align:'center',title:'<span title="VMOS均值">VMOS均值</span>',formatter:showTooltip}
		]]; 
		$(function(){
			
				initTable(); 
			 window.setTimeout(function(){
				initChart();
			},2000); 
			//setTimeout(function(){mapIframe.window.drawpoints();},3000);
			//mapIframe.window.drawpoints();
		});
		window.onresize=reload;
		function reload(){
			self.window.setTimeout(function(){
				everyCWBRNumPieChart.resize();
				everyCWBRNumPieChart2.resize();
				
			}, 100);
			
		}
		 function initTable(){
			$("#kpiInfoTable").datagrid({
				// 表头
				columns:kpiInfoColumns,
				url:'${pageContext.request.contextPath}/streamWhole/streamOperationDoAnalysis.action',
				title:'流媒体业务指标',
				fitColumns:true,
				//填满区域
				fit:true,
				//border:false,
				//奇偶变色
				striped:true,
				tools:"#tt",
				scrollbarSize:0
			});
			$("#kpiInfoTable2").datagrid({
				// 表头
				columns:kpiInfoColumns2,
				url:'${pageContext.request.contextPath}/streamWhole/streamPerceptionDoAnalysis.action',
				title:'流媒体感知指标',
				fitColumns:true,
				//填满区域
				fit:true,
				//border:false,
				//奇偶变色
				striped:true,
				//tools:"#tt4",
				scrollbarSize:0
			});
			$("#testLogInfoTable").datagrid({
				// 表头
				columns:testLogInfoColumns,
				url:'${pageContext.request.contextPath}/streamWhole/wholePreDoAnalysis.action',
				title:'测试日志基础信息',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				scrollbarSize:0
			});
			
		}
		function downloadExcel(downloadurl){
			goToPage('${pageContext.request.contextPath}/streamWhole/downloadTotalExcel.action');
		} 
		//gisCommon会调用获取测试日志ID
		function getGpsPointTestLogItemIds(){
			return testLogItemIds;
		}
		//gisCommon会调用获取生成测试日志轨迹的url
		 function getTestLogItemGpsPointActionUrl(){
			return "<%=basePath%>gisSql/queryTestLogItemGpsPoint.action";
		} 
		
		function hiddenLayout(){
			$("#mainLayout").layout("collapse","east");
		}
		
		
		var everyCWBRNumPieItem=[['[1,3)','[3.5)']];
		/* VMOS值分布占比饼图option */
		var	everyCWBRNumPieOption = {
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:everyCWBRNumPieItem[0]},
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : false,
		    series : [{name:'VMOS1080P占比',type:'pie',radius:'80%',center: ['60%', '50%'],data:[]}]
		};
		var everyCWBRNumPieItem2=[['[1,2.5)','[2.5,5)']];
		/* VMOS值分布占比饼图option */
		var	everyCWBRNumPieOption2 = {
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:everyCWBRNumPieItem2[0]},
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : false,
		    series : [{name:'VMOS其他占比',type:'pie',radius:'80%',center: ['60%', '50%'],data:[]}]
		};
		
		
		
		
		/* Chart路径配置 */
        require.config({
            paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
        /* 初始化图表 */
        function initChart(){
        	$.post("${pageContext.request.contextPath}/streamWhole/wholeVmosDoAnalysis.action",{},
        			function(result){
        				if (result.errorMsg) {
        					$.messager.alert("系统提示", result.errorMsg,'error');
        				} else {
        					$("#kpiInfoTable3").datagrid('loadData',result.wholeRoadIndex0);
        					$("#kpiInfoTable4").datagrid('loadData',result.wholeRoadIndex1);
        					var everyCWBRNum = result.wholeRoadIndex0.rows;
        					if(everyCWBRNum){
        						for(var everyCWBRNumIndex = 0 ; everyCWBRNumIndex < everyCWBRNumPieItem[0].length ; everyCWBRNumIndex++){
        							for(var i=0;i<everyCWBRNum.length;i++){
        								if(everyCWBRNum[i].name==[everyCWBRNumPieItem[0][everyCWBRNumIndex]]){
            								everyCWBRNumPieOption.series[0].data.push({name:everyCWBRNumPieItem[0][everyCWBRNumIndex],value:everyCWBRNum[i].value});
            							}
        							}
        							
        						}
        					}
        					var everyCWBRNum2 = result.wholeRoadIndex1.rows;
        					if(everyCWBRNum2){
        						for(var everyCWBRNumIndex2 = 0 ; everyCWBRNumIndex2 < everyCWBRNumPieItem2[0].length ; everyCWBRNumIndex2++){
        							for(var i2=0;i2<everyCWBRNum2.length;i2++){
        								if(everyCWBRNum2[i2].name==[everyCWBRNumPieItem2[0][everyCWBRNumIndex2]]){
            								everyCWBRNumPieOption2.series[0].data.push({name:everyCWBRNumPieItem2[0][everyCWBRNumIndex2],value:everyCWBRNum2[i2].value});
            							}
        							}
        							
        						}
        					}
        				}
        				
        				require(
        						['echarts','echarts/chart/pie'],
        						function (ec) {
        							everyCWBRNumPieChart = ec.init(document.getElementById("everyCWBRNumPieChart")); 
        							everyCWBRNumPieChart.setOption(everyCWBRNumPieOption);
        						
        						}
        					);
        					require(
        						['echarts','echarts/chart/pie'],
        						function (ec) {
        							everyCWBRNumPieChart2 = ec.init(document.getElementById("everyCWBRNumPieChart2")); 
        							everyCWBRNumPieChart2.setOption(everyCWBRNumPieOption2);
        						
        						}
        					);
        				
        			}
        		,"json");
        	
			
			
	    }
	</script>
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 北,测试日志基础信息 -->
    	 <div data-options="region:'north',border:false" style="height: 97px;padding:4px 4px 2px 4px;">
    		<table id="testLogInfoTable"> </table>
    	</div>  
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:2px 2px 4px 4px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=7" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和异常事件等统计信息 -->
    	  <div data-options="region:'east',border:false" style="width:50%;padding:2px 4px 4px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">  
    			<!-- 指标列表 -->
    			  <div data-options="region:'north',border:false" style="height:18%;padding:0px 0px 2px 0px;">
		    		<table  id="kpiInfoTable"></table>
		    		<div id="tt" >
						<a href="#" style="width:75px;text-decoration:underline;" onclick="downloadExcel();" title="详细报表导出">详细报表导出</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>  
    			  <div data-options="region:'center',border:false" style="padding:0px 0px 2px 0px;">
		    		<table  id="kpiInfoTable2"></table>
		    	</div>  
    			  <div data-options="region:'south',border:false" style="height:60%;padding:0px 0px 2px 0px;">
    			  <div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
		    			<div data-options="region:'north',border:false" style="height:50%;padding:0px 0px 2px 0px;">
		    			<div class="easyui-panel" data-options="fit:true,title:'VMOS分布'">
		    				<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'west',border:false" style="width:35%;">
			    						<table id="kpiInfoTable3" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
						    				<thead>
						    					<tr>
						    						<th data-options="field:'name',width:60,align:'center'">VMOS范围</th>
						    						<th data-options="field:'value',width:50,align:'center',formatter:numToFixed2Formatter">占比</th>
						    						
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td></tr>
						    					
						    				</tbody>
						    			</table>
			    					</div>
			    					<div id="everyCWBRNumPieChart" data-options="region:'center',border:false" >
			    						
			    					</div>
			    				</div>
			    				</div>
		    		 	</div>
		    			<div data-options="region:'center',border:false" style="padding:0px 0px 2px 0px;">
		    				<div class="easyui-layout" style="width: 100%;height: 100%;overflow:hidden;" >
			    					<div data-options="region:'west',border:false" style="width:35%;">
			    						<table id="kpiInfoTable4" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
						    				<thead>
						    					<tr>
						    						<th data-options="field:'name',width:60,align:'center'">VMOS范围</th>
						    						<th data-options="field:'value',width:50,align:'center',formatter:numToFixed2Formatter">占比</th>
						    						
						    					</tr>
						    				</thead>
						    				<tbody>
						    					<tr><td></td><td></td></tr>
						    					
						    				</tbody>
						    			</table>
			    					</div>
			    					<div id="everyCWBRNumPieChart2" data-options="region:'center',border:false" >
			    						
			    					</div>
			    				</div>
		    		 	</div>
		    		</div>
		    		
		    	</div>  
		    	</div>
		    	</div>
		    	
		     
  </body>
</html>
