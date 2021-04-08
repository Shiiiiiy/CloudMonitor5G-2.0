<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE专题----VoLTE对比分析----整体概览</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<style type="text/css">
		.divValueShow0{ 
			display: inline-block; *
			display: inline;
		    background-color: #b4dcf5;
		    margin: 2px;
		    text-align: center;
		    width:40%;
		    height:25px;
   		}
   		.divValueShow0 span{ 
			line-height: 25px;
   		}
		.divValueShow1{ 
			display: inline-block; *
			display: inline;
		    background-color: #f8c381;
		    margin: 2px;
		    text-align: center;
		    width:40%;
		    height:25px;
   		}
   		.divValueShow1 span{ 
			line-height: 25px;
   		}
	</style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
		var testLogItemIds = '${testLogItemIds}';
		var compareTestLogItemIds = '${compareTestLogItemIds}';
		
		//gisCommon会调用获取测试日志ID
		function getGpsPointTestLogItemIds(){
			return testLogItemIds;
		}
		//gisCommon会调用获取生成测试日志轨迹的url
		function getTestLogItemGpsPointActionUrl(){
			return "<%=basePath%>gisSql/queryTestLogItemGpsPoint.action";
		}
		//gisCommon会调用获取对比测试日志ID
		function getGpsPointCompareTestLogItemIds(){
			return compareTestLogItemIds;
		}
		//gisCommon会调用获取生成对比 测试日志轨迹的url
		function getCompareTestLogItemGpsPointActionUrl(){
			return "<%=basePath%>gisSql/queryCompareTestLogItemGpsPoint.action";
		}
		
		// field用于匹配远程json属性，width宽度,align居左右中对齐
		var testLogInfoColumns=[[
			{field:'valueType',width:40,align:'center'}, 				
			{field:'testTotalDistance',width:75,align:'center',title:'测试总里程<br>(KM)'}, 				
			{field:'startDate',width:130,align:'center',title:'文件开始时间'},
			{field:'endDate',width:130,align:'center',title:'文件结束时间'},
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
			{field:'valueType',width:40,align:'center'}, 
			{field:'callcompleterate',width:45,align:'center',title:'<span title="全程呼叫成功率">全程<br>呼叫<br>成功率</span>',formatter:showTooltip},
			{field:'voltemosaverage',width:45,align:'center',title:'<span title="VOLTE平均MOS">VOLTE<br>平均<br>MOS</span>',formatter:showTooltip},
			{field:'psmosover30rate',width:45,align:'center',title:'<span title="MOS>=3.0占比">MOS<br>>=3.0<br>占比</span>',formatter:showTooltip},
			{field:'psmosover358rate',width:45,align:'center',title:'<span title="MOS>=3.5占比">MOS<br>>=3.5<br>占比</span>',formatter:showTooltip},
			{field:'callconnectrate',width:45,align:'center',title:'<span title="接通率">接通<br>率</span>',formatter:showTooltip},
			{field:'calldroprate',width:45,align:'center',title:'<span title="掉话率">掉话<br>率</span>',formatter:showTooltip},
			{field:'voltecallestablishdelay',width:45,align:'center',title:'<span title="VoLTE呼叫建立时延(s)">VoLTE<br>呼叫<br>建立<br>时延(s)</span>',formatter:showTooltip},
			{field:'rtpdiscrate',width:45,align:'center',title:'<span title="RTP丢包率">RTP<br>丢包率</span>',formatter:showTooltip},
			{field:'rtpjitterdelay',width:45,align:'center',title:'<span title="RTP抖动(ms)">RTP<br>抖动(ms)</span>',formatter:showTooltip},
			{field:'imsregistersuccessrate',width:45,align:'center',title:'<span title="IMS注册成功率">IMS<br>注册<br>成功率</span>',formatter:showTooltip},
			{field:'imsregisterdelay',width:45,align:'center',title:'<span title="IMS注册时延(s)">IMS<br>注册<br>时延(s)</span>',formatter:showTooltip},
			{field:'esrvccsuccessrate',width:45,align:'center',title:'<span title="eSRVCC成功率">eSRVCC<br>成功率</span>',formatter:showTooltip},
			{field:'ltehosuccessrate',width:45,align:'center',title:'<span title="LTE HO成功率">LTE HO<br>成功率</span>',formatter:showTooltip},
			{field:'ltereestabsucessrate',width:45,align:'center',title:'<span title="重建成功率">重建<br>成功率</span>',formatter:showTooltip},
			{field:'rsrpaverage',width:45,align:'center',title:'<span title="平均RSRP">平均<br>RSRP</span>',formatter:showTooltip},
			{field:'ltecoverage110rate',width:45,align:'center',title:'<span title="LTE覆盖率">LTE<br>覆盖率</span>',formatter:showTooltip},
			{field:'sinraverage',width:45,align:'center',title:'<span title="平均SINR">平均<br>SINR</span>',formatter:showTooltip}
		]];
		$(function(){
			initTable();
		});
		function initTable(){
			$("#kpiInfoTable").datagrid({
				// 表头
				columns:kpiInfoColumns,
				url:'${pageContext.request.contextPath}/volteWhole/wholePreDoAnalysis.action',
				title:'指标列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//border:false,
				//奇偶变色
				striped:true,
				tools:"#tt",
				scrollbarSize:0,
				onLoadSuccess:ajaxLoadCompareLogKpiInfo
			});
			$("#testLogInfoTable").datagrid({
				// 表头
				columns:testLogInfoColumns,
				url:'${pageContext.request.contextPath}/volteWhole/wholePreDoAnalysis.action',
				title:'测试日志基础信息',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				scrollbarSize:0,
				onLoadSuccess:ajaxLoadCompareLogInfo
			});
			load();
			//异步加载MOS黑点信息
			$.post("${pageContext.request.contextPath}/voiceCompare/doMosQualityBadAnalysis.action",{},
				function(result){
					disLoad();
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						var distanceSum = result.mosBadIndexTable3.rows[0];
						if(distanceSum){
							var mosBadRoadDistanceSum;
							for (distanceAttr in distanceSum){
								$("#"+distanceAttr+"MosBadRoadDistanceSum").html(distanceSum[distanceAttr].toFixed(2));
								if(mosBadRoadDistanceSum){
									mosBadRoadDistanceSum+=distanceSum[distanceAttr];
								}else{
									mosBadRoadDistanceSum=distanceSum[distanceAttr];
								}
							}
							$("#MosBadRoadDistanceSum").html(mosBadRoadDistanceSum.toFixed(2));
						}
						
						var comparedistanceSum = result.mosBadIndexTable4.rows[0];
						if(comparedistanceSum){
							var comparemosBadRoadDistanceSum;
							for (comparedistanceAttr in comparedistanceSum){
								$("#compare"+comparedistanceAttr+"MosBadRoadDistanceSum").html(comparedistanceSum[comparedistanceAttr].toFixed(2));
								if(comparemosBadRoadDistanceSum){
									comparemosBadRoadDistanceSum+=comparedistanceSum[comparedistanceAttr];
								}else{
									comparemosBadRoadDistanceSum=comparedistanceSum[comparedistanceAttr];
								}
							}
							$("#compareMosBadRoadDistanceSum").html(comparemosBadRoadDistanceSum.toFixed(2));
						}
					}
				}
			,"json");
		}
		//异步加载对比日志的KPI信息
		function ajaxLoadCompareLogKpiInfo(){
			$.post("${pageContext.request.contextPath}/volteWhole/wholePreDoAnalysis.action",{"isCompare":true},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#kpiInfoTable").datagrid('appendRow',result.rows[0]);
					}
				}
			,"json");
		}
		//异步加载对比日志的基础信息
		function ajaxLoadCompareLogInfo(){
			$.post("${pageContext.request.contextPath}/volteWhole/wholePreDoAnalysis.action",{"isCompare":true},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#testLogInfoTable").datagrid('appendRow',result.rows[0]);
					}
				}
			,"json");
		}
		function downloadExcel(downloadurl){
			goToPage(downloadurl);
		}
		
		
		function hiddenLayout(){
			$("#mainLayout").layout("collapse","east");
		}
		
		//弹出加载层
		function load() { 
		    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: $("#mosBadRoadDiv").width(), height: $("#mosBadRoadDiv").height() }).appendTo("#mosBadRoadDiv"); 
		    $("<div class=\"datagrid-mask-msg\"></div>").html("加载中，请稍候。。。").appendTo("#mosBadRoadDiv").css({ display: "block", left: ($("#mosBadRoadDiv").outerWidth(true) - 150) / 2, top: ($("#mosBadRoadDiv").height() - 45) / 2 }); 
		} 
	  
		//取消加载层  
		function disLoad() { 
		    $(".datagrid-mask").remove(); 
		    $(".datagrid-mask-msg").remove(); 
		} 
	</script>
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 北,测试日志基础信息 -->
    	<div data-options="region:'north',border:false" style="height: 120px;padding:0px 0px 2px 0px;">
    		<table id="testLogInfoTable"> </table>
    	</div>
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:2px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=22" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和异常事件等统计信息 -->
    	<div data-options="region:'east',border:false" style="width:50%;padding:2px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
    			<!-- 指标列表 -->
    			<div data-options="region:'north',border:false" style="height:155px;padding:0px 0px 2px 0px;">
		    		<table  id="kpiInfoTable"></table>
		    		<div id="tt" >
						<a href="#" style="width:75px;text-decoration:underline;" onclick="downloadExcel('${pageContext.request.contextPath}/voiceCompare/downloadVolteCompareExcel.action');" title="详细报表导出">详细报表导出</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<!-- 异常事件统计信息 -->
		    	<div data-options="region:'west',border:false" style="width:33%;padding:2px 2px 0px 0px;">
		    		<div class="easyui-panel" style="padding: 5px;overflow-y: auto; " data-options="fit:true,title:'异常事件统计信息',tools:'#tt1'">
		    			<span style="line-height: 25px;">Volte语音未接通</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.notConnect?'-':exceptionEvent.notConnect}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.notConnect?'-':compareExceptionEvent.notConnect}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">Volte语音掉话</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.dropCall?'-':exceptionEvent.dropCall}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.dropCall?'-':compareExceptionEvent.dropCall}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">Volte视频未接通</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>-</span></strong></div>
		    			<div class="divValueShow1"><strong><span>-</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">Volte视频掉话</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>-</span></strong></div>
		    			<div class="divValueShow1"><strong><span>-</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">注册失败</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.imsRegistFail?'-':exceptionEvent.imsRegistFail}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.imsRegistFail?'-':compareExceptionEvent.imsRegistFail}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">CSFB失败</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.csfbFail?'-':exceptionEvent.csfbFail}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.csfbFail?'-':compareExceptionEvent.csfbFail}</span></strong></div>
		    		</div>
		    		<div id="tt1">
						<a href="#" style="width:30px;text-decoration:underline;" onclick="alertAttention();" title="导出">导出</a>
					</div>
		    	</div>
		    	<!-- MOS差黑点统计信息 -->
		    	<div id="mosBadRoadDiv" data-options="region:'center',border:false" style="width:34%;padding:2px 2px 0px 2px;">
		    		<div class="easyui-panel" style="padding: 5px;overflow-y: auto;" data-options="fit:true,title:'MOS差黑点统计',tools:'#tt2'">
		    			<span style="line-height: 25px;">MOS差黑点总里程(m)</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span id="MosBadRoadDistanceSum">-</span></strong></div>
		    			<div class="divValueShow1"><strong><span id="compareMosBadRoadDistanceSum">-</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">弱覆盖总里程(m)</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span id="WeakCoverMosBadRoadDistanceSum">-</span></strong></div>
		    			<div class="divValueShow1"><strong><span id="compareWeakCoverMosBadRoadDistanceSum">-</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">干扰总里程(m)</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span id="DisturbMosBadRoadDistanceSum">-</span></strong></div>
		    			<div class="divValueShow1"><strong><span id="compareDisturbMosBadRoadDistanceSum">-</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">邻区缺失总里程(m)</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span id="NbCellMosBadRoadDistanceSum">-</span></strong></div>
		    			<div class="divValueShow1"><strong><span id="compareNbCellMosBadRoadDistanceSum">-</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">参数错误总里程(m)</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span id="ParamErrorMosBadRoadDistanceSum">-</span></strong></div>
		    			<div class="divValueShow1"><strong><span id="compareParamErrorMosBadRoadDistanceSum">-</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">其他原因总里程(m)</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span id="OtherMosBadRoadDistanceSum">-</span></strong></div>
		    			<div class="divValueShow1"><strong><span id="compareOtherMosBadRoadDistanceSum">-</span></strong></div>
		    		</div>
		    		<div id="tt2">
						<a href="#" style="width:30px;text-decoration:underline;" onclick="alertAttention();" title="导出">导出</a>
					</div>
		    	</div>
		    	<!-- 切换情况 -->
		    	<div data-options="region:'east',border:false" style="width:33%;padding:2px 0px 0px 2px;">
		    		<div class="easyui-panel" style="padding: 5px;overflow-y: auto;" data-options="fit:true,title:'切换情况',tools:'#tt3'">
		    			<span style="line-height: 25px;">系统间srvcc切换失败</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.droppingSRVCCDao?'-':dropping.droppingSRVCCDao}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.droppingSRVCCDao?'-':compareDropping.droppingSRVCCDao}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">系统内切换失败</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.droppingIntDao?'-':dropping.droppingIntDao}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.droppingIntDao?'-':compareDropping.droppingIntDao}</span></strong></div>
		    		</div>
		    		<div id="tt3">
						<a href="#" style="width:30px;text-decoration:underline;" onclick="alertAttention();" title="导出">导出</a>
					</div>
		    	</div>
		    </div>
    	</div>
  </body>
</html>
