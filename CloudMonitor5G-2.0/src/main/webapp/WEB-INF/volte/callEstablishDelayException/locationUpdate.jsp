<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>被叫位置更新</title>

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
	<style type="text/css">
		.lines-no .datagrid-body td{
			border-right:1px dotted transparent;
			border-bottom:1px dotted transparent;
		}
	</style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	//获取及回填道路名称
	var mainTableId = 'otherRoadTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>callEstablish/roadNameCallBack.action";
	}
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//存储质差路段id
	var volteqbrid;
	//小区图层名称
	var drawCellFileName;
	//gisCommon会调用获取测试日志ID
	function getCellTestLogItemIds(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成小区SQL的url
	function getCellActionUrl(){
		return "<%=basePath%>gisSql/queryCellInfo.action";
	}
	//gisCommon会调用获取连续无线差路段ID
	function getGpsPointCEDEId(){
		return volteqbrid;
	}
	//gisCommon会调用获取生成连续无线差路段轨迹的url
	function getCEDEGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCEDEIndexGpsPoint.action";
	}
	//gisCommon会调用获取生成连续无线差路段轨迹行驶方向的url
	function getCEDEDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryCEDEDirection.action";
	}
	//gisCommon会调用获取连续无线差事件图标轨迹的url
	function getCEDEEventGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCEDEEventGpsPoint.action";
	}
	//gisCommon会调用获取事件图标类型
	function getIconType(){
		//呼叫建立时延异常图标类型(见数据库表定义中事件类型定义)
		return '21';
	}
	/*信令解码*/
	function signllingDecode(rowIndex,rowData){
		if(rowData.stream){
			//信令解码
			$.post("${pageContext.request.contextPath}/voiceQBRSign/signllingStreamDecode.action",{stream:rowData.stream,stream2:rowData.stream2},
				function(result){
					if(result.errorMsg){
						$.messager.alert('提示',result.errorMsg,'error');
					}else{
						var signllingDiv =  document.getElementById('signllingDiv'+rowData.id);
						if(!signllingDiv){
							$(document.body).prepend("<div id='signllingDiv"+rowData.id+"' style='overflow:auto;' >"+result+"</div>");
							$('#signllingDiv'+rowData.id).window({
							    title: (rowIndex+1)+'&nbsp;'+rowData.time+'&nbsp;'+rowData.name,
							    width: 600,
							    height: 400,
								minimizable:false,
							    collapsible:false,
							    modal: false,
							    onClose:function(){
							    	$('#signllingDiv'+rowData.id).window('destroy',true);
							    }
							});
						}
					}
				}
			,"json");
		}else{
			$.messager.alert('提示','解码失败:Stream Null Error','error');
		}
	}
	/* 信令datagrid的columns */
	var signallingTableColumns=[[
		{field:'time',width:60,align:'left',halign:'center',title:'Time'},
		{field:'dir',width:30,align:'left',halign:'center',title:'Dir',formatter:function(value,row,index){
			if (1==value){
				return "UL";
			}else if(2==value){
				return "DL";
			}
			return value;
		}},
		{field:'rat',width:30,align:'left',halign:'center',title:'RAT',formatter:function(value,row,index){
			if (1==value){
				return "TD";
			}else if(2==value){
				return "GSM";
			}else if(3==value){
				return "LTE";
			}
			return value;
		}},
		{field:'layer',width:40,align:'left',halign:'center',title:'Layer'},
		{field:'ch',width:40,align:'left',halign:'center',title:'CH'},
		{field:'name',width:130,align:'left',halign:'center',title:'Name',formatter:showTooltip}
	]];
	
	var lastVolteEEid;//上一次异常事件id
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var notConnectRow = $("#locationUpdateRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!notConnectRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个异常事件!",'warning');
			return ;
		}
		testLogItemIds = notConnectRow.recSeqNo;
		volteEEid = notConnectRow.id;
		if(!volteEEid){
			$.messager.alert("提示","请勾选某个异常事件!",'warning');
			return ;
		}
		var roadName = notConnectRow.m_stRoadName;
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/callEstablish/doLocationUpdate.action",{CEDEId:notConnectRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
				$("#locationUpdateIndexTable").datagrid('loadData',result.callEstablishDelayException);
					
					}
			}
		,"json");
		//本次和上一次的异常事件id不同
		if(lastVolteEEid!=volteEEid){
			$("#locationUpdateSignallingTable").datagrid('loadData',[]);
			$("#locationUpdateOppositeSignallingTable").datagrid('loadData',[]);
			$("#locationUpdateSignallingTable").datagrid({
				// 表头
				columns:signallingTableColumns,
				url:'${pageContext.request.contextPath}/voiceNotConnect/doPageListJson.action?isOpposite=false&exceptionEventId='+volteEEid,
				title:roadName+'呼叫建立时延异常信令列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				pagination:true,
				pageSize:20,
				pageList:[20,50,100,200],
				scrollbarSize:0,
				onDblClickRow:signllingDecode
			});
			$("#locationUpdateOppositeSignallingTable").datagrid({
				// 表头
				columns:signallingTableColumns,
				url:'${pageContext.request.contextPath}/voiceNotConnect/doPageListJson.action?isOpposite=true&exceptionEventId='+volteEEid,
				title:roadName+'呼叫建立时延异常对端信令列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				pagination:true,
				pageSize:20,
				pageList:[20,50,100,200],
				scrollbarSize:0,
				onDblClickRow:signllingDecode
			});
			$('#locationUpdateSignallingTable').datagrid('getPanel').addClass("lines-no");
			$('#locationUpdateOppositeSignallingTable').datagrid('getPanel').addClass("lines-no");
		}
		lastVolteEEid = volteEEid;
		
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:notConnectRow.id},
			function(result){
				//如果上次为null或者undefined,或者当前图层与上一次图层不一样
				if(!drawCellFileName||result!=drawCellFileName){
					mapIframe.window.drawCell();
				}
				drawCellFileName = result;
			}
		,"json");
		mapIframe.window.drawEE();
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	
	
	
	
	$(function(){
		
		<c:if test="${null == locationUpdates || empty locationUpdates}">
			$("#locationUpdateRoadTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:11
			});
		</c:if>
		$("#locationUpdateIndexTable").datagrid('mergeCells',{
			index: 0,
			field: 'callEstablishDelay',
			colspan:6
		});
		$("#locationUpdateSignallingTable").datagrid('mergeCells',{
			index: 0,
			field: 'time',
			colspan:6
		});
		$("#locationUpdateOppositeSignallingTable").datagrid('mergeCells',{
			index: 0,
			field: 'time',
			colspan:6
		});
	});
	
	</script>
  </head>
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
	
	
	
  <div id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=20" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 质差问题路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:34%;">
  					<table id="locationUpdateRoadTable" class="easyui-datagrid" data-options="title:'被叫位置更新问题路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
	    						<th rowspan="2" data-options="field:'boxid',width:60,align:'center',formatter:showTooltip">BOXID</th>
	    						<th colspan="4" >问题路段信息	</th>
	    						<th colspan="6" >问题描述</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'m_stRoadName',width:70,align:'center',formatter:showTooltip">路段名称</th>
	    						<th data-options="field:'m_dbDistance',width:70,align:'center',formatter:numToFixed2Formatter">持续距离<br>(m)</th>
	    						<th data-options="field:'m_dbContinueTime',width:70,align:'center',formatter:numDivide1000Formatter">测试时间<br>(s)</th>
	    						<th data-options="field:'startTime2String',width:70,align:'center',formatter:showTooltip">起呼时间</th>
	    						<th data-options="field:'cellName',width:70,align:'center'">服务小区</th>
	    						<th data-options="field:'sourceTau',width:70,align:'center'">源ATU</th>
	    						<th data-options="field:'targetTau',width:80,align:'center'">目标ATU</th>
	    						<th data-options="field:'tauRequestTime',width:90,align:'center',formatter:numDivide1000Formatter">ATU发起<br>时间</th>
	    						<th data-options="field:'fileName',width:70,align:'center',formatter:showTooltip">测试日志<br>名称</th>
	    						<th data-options="field:'peerRecSeqNo',width:70,align:'center',formatter:showTooltip">被叫测试<br>日志</th>
	    						<th data-options="field:'recSeqNo',hidden:true">recSeqNo</th>
	    						<th data-options="field:'beginLatitude',hidden:true">开始纬度</th>
	    						<th data-options="field:'courseLatitude',hidden:true">中间纬度</th>
	    						<th data-options="field:'endLatitude',hidden:true">结束纬度</th>
	    						<th data-options="field:'beginLongitude',hidden:true">开始经度</th>
	    						<th data-options="field:'courseLongitude',hidden:true">中间经度</th>
	    						<th data-options="field:'endLongitude',hidden:true">结束经度</th> 
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${locationUpdates}" var="locationUpdate">
		    					<tr>
		    						<td>${locationUpdate.id}</td>
		    						<td>${locationUpdate.testLogItem.boxId}</td>
		    						<td>${locationUpdate.m_stRoadName}</td>
		    						<td>${locationUpdate.m_dbDistance}</td>
		    						<td>${locationUpdate.m_dbContinueTime}</td>
		    						<td>${locationUpdate.startTime2String}</td>
		    						<td>${locationUpdate.cellName}</td>
		    						<td>${locationUpdate.sourceTau}</td>
		    						<td>${locationUpdate.targetTau}</td>
		    						<td>${locationUpdate.tauRequestTime}</td>
		    						<td>${locationUpdate.testLogItem.fileName}</td>
		    						<td>${locationUpdate.testLogItem.peerRecSeqNo}</td>
		    						<td>${locationUpdate.testLogItem.recSeqNo}</td>
		    						<td>${locationUpdate.beginLatitude}</td>
		    						<td>${locationUpdate.courseLatitude}</td>
		    						<td>${locationUpdate.endLatitude}</td>
		    						<td>${locationUpdate.beginLongitude}</td>
		    						<td>${locationUpdate.courseLongitude}</td>
		    						<td>${locationUpdate.endLongitude}</td>
		    						
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == locationUpdates}">
	    						<tr>
		    						<td></td>
		    						<td>未选择测试日志</td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != locationUpdates && empty locationUpdates}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有呼叫建立异常其他原因</td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						
		    					</tr>
	    					</c:if>
	    				</tbody>
	    			</table>
	    			<div id="tt3" >
						<a href="#" style="width:50px;text-decoration:underline;" onclick="doAnalysis();" title="质差路段分析">路段分析</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<!--  VoLTE呼叫建立各阶段时延概览 -->
		    	<div data-options="region:'center',border:false" >
					<div class="easyui-layout" style="width:100%;height: 100%;">
						<div data-options="region:'north',border:false" style="height:116px;padding:2px 0px 0px 0px;">
							<table id="locationUpdateIndexTable"   class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:' VoLTE呼叫建立各阶段时延概览'" >
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
						    				
							    						<tr>
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
		    	 <div data-options="region:'center',border:false" >
		    	 	<div class="easyui-layout" style="width: 100%;height: 100%;">
		    	 		<!-- 呼叫位置更新信令 -->
				<div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
					<table id="locationUpdateSignallingTable" class="easyui-datagrid" data-options="title:'信令列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'time',width:120,align:'center'">Time</th>
	    						<th data-options="field:'dir',width:50,align:'center'">Dir</th>
	    						<th data-options="field:'rat',width:65,align:'center'">RAT</th>
	    						<th data-options="field:'layer',width:70,align:'center'">Layer</th>
	    						<th data-options="field:'ch',width:60,align:'center'">CH</th>
	    						<th data-options="field:'name',width:55,align:'center'">Name</th>
	    					</tr>
	    				</thead>
	    				<tbody>
    						<tr>
	    						<td>未选择问题路段</td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    					</tr>
	    				</tbody>
	    			</table>
				</div>
				<!-- 被叫位置更新对端信令 -->
				<div data-options="region:'south',border:false"  style="height:50%;padding:2px 0px 0px 0px;">
					<table id="locationUpdateOppositeSignallingTable" class="easyui-datagrid" data-options="title:'对端信令列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'time',width:120,align:'center'">Time</th>
	    						<th data-options="field:'dir',width:50,align:'center'">Dir</th>
	    						<th data-options="field:'rat',width:65,align:'center'">RAT</th>
	    						<th data-options="field:'layer',width:70,align:'center'">Layer</th>
	    						<th data-options="field:'ch',width:60,align:'center'">CH</th>
	    						<th data-options="field:'name',width:55,align:'center'">Name</th>
	    					</tr>
	    				</thead>
	    				<tbody>
    						<tr>
	    						<td>未选择问题路段</td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    					</tr>
	    				</tbody>
	    			</table>
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
