<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>统计分析-5G对比分析-整体概览界面</title>

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
			{field:'testTotalDistance',width:75,align:'center',title:'测试总里程(KM)'}, 				
			{field:'startDate',width:130,align:'center',title:'文件开始时间'},
			{field:'endDate',width:130,align:'center',title:'文件结束时间'},
			{field:'testTotalTime',width:75,align:'center',title:'测试总时长'},
			{field:'testlogcellnum',width:60,align:'center',title:'测试涉及小区数'},
			{field:'testTotalServiceType',width:105,align:'center',title:'测试占比最高的业务类型',
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
			{field:'testTotalTerminalGroup',width:60,align:'center',title:'主要测试行政区域'},
			{field:'testTotalSpeedAvg',width:60,align:'center',title:'平均车速(km/h)'}
		]];
		// field用于匹配远程json属性，width宽度,align居左右中对齐
		var kpiInfoColumns=[[
			{field:'valueType',width:40,align:'center'}, 
			{field:'rsrpaverage',width:40,align:'center',title:'平均<br>RSRP',formatter:showTooltip},
			{field:'rsrpedge',width:40,align:'center',title:'边缘<br>RSRP',formatter:showTooltip},
			{field:'ltecoverage110rate',width:70,align:'center',title:'5G覆盖率<br>(RSRP>-110<br>and SINR>-3)',formatter:showTooltip},
			{field:'rsrpweakdistancerate',width:70,align:'center',title:'RSRP<br>连续弱覆盖<br>里程占比',formatter:showTooltip},
			{field:'uetxpoweroverdistrate',width:70,align:'center',title:'连续UE<br>高发射功率<br>里程占比',formatter:showTooltip},
			{field:'sinraverage',width:40,align:'center',title:'平均<br>SINR',formatter:showTooltip},
			{field:'sinredge',width:40,align:'center',title:'边缘<br>SINR',formatter:showTooltip},
			{field:'sinrsign3rate',width:60,align:'center',title:'SINR-3以上<br>占比',formatter:showTooltip},
			{field:'sinrweaksign3distrate',width:90,align:'center',title:'连续SINR<br>质差里程<br>占比(SINR小于-3)(%)',formatter:showTooltip},
			{field:'overlapcover3distrate',width:80,align:'center',title:'重叠覆盖<br>里程占比<br>(重叠覆盖度>=3)(%)',formatter:showTooltip},
			{field:'maxbeamrsrpaverage',width:70,align:'center',title:'最强波束<br>RSRP均值',formatter:showTooltip},
			{field:'maxbeamsinraverage',width:70,align:'center',title:'最强波束<br>SINR均值',formatter:showTooltip},
			{field:'maxbeamrate',width:70,align:'center',title:'接入最强<br>波束占比',formatter:showTooltip},
			{field:'fourbeamrate',width:60,align:'center',title:'四波束<br>占比',formatter:showTooltip},
			{field:'eightbeamrate',width:60,align:'center',title:'八波束<br>占比',formatter:showTooltip}
		]];
		$(function(){
			initTable();
		});
		function initTable(){
			$("#kpiInfoTable").datagrid({
				// 表头
				columns:kpiInfoColumns,
				url:'${pageContext.request.contextPath}/compareAnalysis5g/wholePreDoAnalysis.action',
				title:'指标列表',
				fitColumns:true,
				nowrap:false,
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
				url:'${pageContext.request.contextPath}/compareAnalysis5g/wholePreDoAnalysis.action',
				title:'测试日志基础信息',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				scrollbarSize:0,
				onLoadSuccess:ajaxLoadCompareLogInfo
			});
			//load();
			//setTimeout(function(){
			//	disLoad();
			//},10000);
			//异步加载MOS黑点信息
			/* $.post("${pageContext.request.contextPath}/voiceCompare/doMosQualityBadAnalysis.action",{},
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
			,"json"); */
		}
		//异步加载对比日志的KPI信息
		function ajaxLoadCompareLogKpiInfo(){
			$.post("${pageContext.request.contextPath}/compareAnalysis5g/wholePreDoAnalysis.action",{"isCompare":true},
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
			$.post("${pageContext.request.contextPath}/compareAnalysis5g/wholePreDoAnalysis.action",{"isCompare":true},
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
    	<div data-options="region:'north',border:false" style="height: 108px;padding:0px 0px 2px 0px;">
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
    			<div data-options="region:'north',border:false" style="height:138px;padding:0px 0px 2px 0px;">
		    		<table  id="kpiInfoTable"></table>
		    		<div id="tt" >
						<a href="#" style="width:75px;text-decoration:underline;" onclick="downloadExcel('${pageContext.request.contextPath}/compareAnalysis5g/download5gCompareExcel.action');" title="详细报表导出">详细报表导出</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
				<!-- FTP总掉线率
				FTP下载掉线率
				5G-FTP下载掉线率
				TDL-FTP下载掉线率
				FTP上传掉线率
				5G-FTP上传掉线率
				TDL-FTP上传掉线率-->
		    	<div data-options="region:'west',border:false" style="width:25%;padding:2px 2px 0px 0px;">
		    		<div class="easyui-panel" style="padding: 5px;overflow-y: auto; " data-options="fit:true,title:'数据业务异常事件统计信息',tools:'#tt1'">
		    			<span style="line-height: 25px;">FTP总掉线率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.totaldroprate?'-':exceptionEvent.totaldroprate}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.totaldroprate?'-':compareExceptionEvent.totaldroprate}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">FTP下载掉线率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.ftpdropratedl?'-':exceptionEvent.ftpdropratedl}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.ftpdropratedl?'-':compareExceptionEvent.ftpdropratedl}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">5G-FTP下载掉线率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.ftpdropratedlfg?'-':exceptionEvent.ftpdropratedlfg}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.ftpdropratedlfg?'-':compareExceptionEvent.ftpdropratedlfg}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">TDL-FTP下载掉线率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.ftpdropratedltdl?'-':exceptionEvent.ftpdropratedltdl}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.ftpdropratedltdl?'-':compareExceptionEvent.ftpdropratedltdl}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">FTP上传掉线率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.ftpdroprateul?'-':exceptionEvent.ftpdroprateul}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.ftpdroprateul?'-':compareExceptionEvent.ftpdroprateul}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">5G-FTP上传掉线率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.ftpdroprateulfg?'-':exceptionEvent.ftpdroprateulfg}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.ftpdroprateulfg?'-':compareExceptionEvent.ftpdroprateulfg}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">TDL-FTP上传掉线率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.ftpdroprateultdl?'-':exceptionEvent.ftpdroprateultdl}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.ftpdroprateultdl?'-':compareExceptionEvent.ftpdroprateultdl}</span></strong></div>
		    		</div>
		    	</div>
		    	
		    	<div data-options="region:'center',border:false" style="width:50%;">
		    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    		<!--RRC连接尝试次数
		    		RRC恢复尝试次数
		    		RRC连接失败率
					RRC恢复失败率
					RRC重建尝试次数
					RRC重建失败率-->
		    			<div data-options="region:'center',border:false" style="width:50%;padding:2px 2px 0px 2px;">
		    				<div class="easyui-panel" style="padding: 5px;overflow-y: auto;width:40%;" data-options="fit:true,title:'接入类异常事件统计信息',tools:'#tt2'">
				    			<span style="line-height: 25px;">RRC连接尝试次数</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rrcsetupreqnum?'-':exceptionEvent.rrcsetupreqnum}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rrcsetupreqnum?'-':compareExceptionEvent.rrcsetupreqnum}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RRC恢复尝试次数</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rrcresumereqnum?'-':exceptionEvent.rrcresumereqnum}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rrcresumereqnum?'-':compareExceptionEvent.rrcresumereqnum}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RRC连接失败率</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rrcsetupbadrate?'-':exceptionEvent.rrcsetupbadrate}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rrcsetupbadrate?'-':compareExceptionEvent.rrcsetupbadrate}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RRC恢复失败率</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rrcresumebadrate?'-':exceptionEvent.rrcresumebadrate}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rrcresumebadrate?'-':compareExceptionEvent.rrcresumebadrate}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RRC重建尝试次数</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rrcrebuildreqnum?'-':exceptionEvent.rrcrebuildreqnum}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rrcrebuildreqnum?'-':compareExceptionEvent.rrcrebuildreqnum}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RRC重建失败率</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rrcrebuildbadrate?'-':exceptionEvent.rrcrebuildbadrate}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rrcrebuildbadrate?'-':compareExceptionEvent.rrcrebuildbadrate}</span></strong></div>
				    		</div> 
		    			</div>
		    			
		    			  <!--切换尝试次数
						切换成功次数
						切换失败率
						RNA更新尝试次数
						RNA更新成功次数
						RNA更新失败率-->
		    			<div data-options="region:'east',border:false" style="width:50%;padding:2px 2px 0px 2px;">
		    				<div class="easyui-panel" style="padding: 5px;overflow-y: auto;width:40%;" data-options="fit:true,title:'移动类异常事件统计信息',tools:'#tt2'">
				    			<span style="line-height: 25px;">切换尝试次数</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.hoattemptcount?'-':exceptionEvent.hoattemptcount}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.hoattemptcount?'-':compareExceptionEvent.hoattemptcount}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">切换成功次数</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.hosucccount?'-':exceptionEvent.hosucccount}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.hosucccount?'-':compareExceptionEvent.hosucccount}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">切换失败率</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.hobadrate?'-':exceptionEvent.hobadrate}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.hobadrate?'-':compareExceptionEvent.hobadrate}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RNA更新尝试次数</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rnaureqnum?'-':exceptionEvent.rnaureqnum}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rnaureqnum?'-':compareExceptionEvent.rnaureqnum}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RNA更新成功次数</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rnausuccnum?'-':exceptionEvent.rnausuccnum}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rnausuccnum?'-':compareExceptionEvent.rnausuccnum}</span></strong></div>
				    			<br>
				    			<span style="line-height: 25px;">RNA更新失败率</span>
				    			<br>
				    			<div class="divValueShow0"><strong><span>${null==exceptionEvent.rnaubadrate?'-':exceptionEvent.rnaubadrate}</span></strong></div>
		    					<div class="divValueShow1"><strong><span>${null==compareExceptionEvent.rnaubadrate?'-':compareExceptionEvent.rnaubadrate}</span></strong></div>
				    		</div> 
		    			</div>
		    		</div>
		    		
		    	</div>
		    	<!--PDU会话建立尝试次数
				PDU会话建立失败率
				PDU会话重建尝试次数
				PDU会话重建失败率
				PDU会话修改尝试次数
				PDU会话修改失败率
				PDU会话释放尝试次数
				PDU会话释放失败率 -->
		    	<div data-options="region:'east',border:false" style="width:25%;padding:2px 0px 0px 2px;">
		    		 <div class="easyui-panel" style="padding: 5px;overflow-y: auto;" data-options="fit:true,title:'会话类异常事件统计信息',tools:'#tt3'">
		    			<span style="line-height: 25px;">PDU会话建立尝试次数</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pdubuildreqnum?'-':dropping.pdubuildreqnum}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pdubuildreqnum?'-':compareDropping.pdubuildreqnum}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">PDU会话建立失败率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pdubuildbadrate?'-':dropping.pdubuildbadrate}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pdubuildbadrate?'-':compareDropping.pdubuildbadrate}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">PDU会话重建尝试次数</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pdurebuildreqnum?'-':dropping.pdurebuildreqnum}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pdurebuildreqnum?'-':compareDropping.pdurebuildreqnum}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">PDU会话重建失败率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pdurebuildbadrate?'-':dropping.pdurebuildbadrate}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pdurebuildbadrate?'-':compareDropping.pdurebuildbadrate}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">PDU会话修改尝试次数</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pduureqnum?'-':dropping.pduureqnum}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pduureqnum?'-':compareDropping.pduureqnum}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">PDU会话修改失败率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pduubadrate?'-':dropping.pduubadrate}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pduubadrate?'-':compareDropping.pduubadrate}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">PDU会话释放尝试次数</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pdureleasereqnum?'-':dropping.pdureleasereqnum}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pdureleasereqnum?'-':compareDropping.pdureleasereqnum}</span></strong></div>
		    			<br>
		    			<span style="line-height: 25px;">PDU会话释放失败率</span>
		    			<br>
		    			<div class="divValueShow0"><strong><span>${null==dropping.pdureleasebadrate?'-':dropping.pdureleasebadrate}</span></strong></div>
		    			<div class="divValueShow1"><strong><span>${null==compareDropping.pdureleasebadrate?'-':compareDropping.pdureleasebadrate}</span></strong></div>
		    		</div>
		    	</div>
		    </div>
    	</div>
  </body>
</html>
