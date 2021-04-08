<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE专题----VoLTE对比分析----异常事件对比</title>

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
		var mainTableId = 'eventTable';
		function getRoadNameCallBackUrl(){
			return "<%=basePath%>voiceNotConnect/roadNameCallBack.action";
		}
		//路段名称回显用
		var mainTableId1 = 'compareEventTable';
		function getRoadNameCallBackUrl1(){
			return "<%=basePath%>voiceNotConnect/roadNameCallBack.action";
		}
	
		//存储异常事件路段id
		var volteEEid;
		//存储对比异常事件路段id
		var volteCompareEEid;
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
			var eventRow = $("#eventTable").datagrid('getSelected');//获取表格中用户选中数据
			if(!eventRow){// 操作前至少选中一条
				$.messager.alert("提示","请勾选某个异常事件!",'warning');
				return ;
			}
			volteEEid = eventRow.id;
			if(!volteEEid){
				$.messager.alert("提示","请勾选某个异常事件!",'warning');
				return ;
			}
			var roadName = eventRow.m_stRoadName;
			//本次和上一次的异常事件id不同
			if(lastVolteEEid!=volteEEid){
				$("#eventSignallingTable").datagrid('loadData',[]);
				$("#eventSignallingTable").datagrid({
					// 表头
					columns:signallingTableColumns,
					url:'${pageContext.request.contextPath}/voiceNotConnect/doPageListJson.action?isOpposite=false&exceptionEventId='+volteEEid,
					title:roadName+'异常事件信令流程',
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
				$('#eventsignallingTable').datagrid('getPanel').addClass("lines-no");
			}
			lastVolteEEid = volteEEid;
		}
		
		var lastVolteCompareEEid;//上一次对比异常事件id
		/* 开始对比分析,对比分析按钮 */
		function doCompareAnalysis(){
			var compareEventRow = $("#compareEventTable").datagrid('getSelected');//获取表格中用户选中数据
			if(!compareEventRow){// 操作前至少选中一条
				$.messager.alert("提示","请勾选某个异常事件!",'warning');
				return ;
			}
			volteCompareEEid = compareEventRow.id;
			if(!volteCompareEEid){
				$.messager.alert("提示","请勾选某个异常事件!",'warning');
				return ;
			}
			var compareRoadName = compareEventRow.m_stRoadName;
			//本次和上一次的异常事件id不同
			if(lastVolteCompareEEid!=volteCompareEEid){
				$("#compareEventSignallingTable").datagrid('loadData',[]);
				$("#compareEventSignallingTable").datagrid({
					// 表头
					columns:signallingTableColumns,
					url:'${pageContext.request.contextPath}/voiceNotConnect/doPageListJson.action?isOpposite=false&exceptionEventId='+volteCompareEEid,
					title:compareRoadName+'异常事件信令流程',
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
				$('#compareEventSignallingTable').datagrid('getPanel').addClass("lines-no");
			}
			lastVolteCompareEEid = volteCompareEEid;
		}
		//异常事件字段排序开关
		var isEESortable = true ;
		<c:if test="${null == events || empty events}">
			isEESortable = false;
		</c:if>
		var isCompareEESortable = true;
		<c:if test="${null == compareEvents || empty compareEvents}">
			isCompareEESortable = false;
		</c:if>
		$(function(){
			<c:if test="${null == events || empty events}">
				$("#eventTable").datagrid('mergeCells',{
					index: 0,
					field: 'boxid',
					colspan:9
				});
			</c:if>
			<c:if test="${null == compareEvents || empty compareEvents}">
				$("#compareEventTable").datagrid('mergeCells',{
					index: 0,
					field: 'boxid',
					colspan:9
				});
			</c:if>
			
			$("#eventSignallingTable").datagrid('mergeCells',{
				index: 0,
				field: 'time',
				colspan:6
			});
			$("#compareEventSignallingTable").datagrid('mergeCells',{
				index: 0,
				field: 'time',
				colspan:6
			});
		});
	</script>
  </head>
  <body class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,异常事件及异常事件信令列表 -->
    	<div  data-options="region:'center',border:false" >
    		<div class="easyui-layout" style="width:100%;height: 100%;">
				<div data-options="region:'north',border:false" style="height:50%;padding:0px 2px 4px 0px;">
					<table id="eventTable" class="easyui-datagrid" data-options="title:'原始日志异常事件',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'boxid',width:80,align:'center',formatter:showTooltip,sortable:isEESortable">BOXID</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center',formatter:showTooltip">路段名称</th>
	    						<th data-options="field:'cellId',width:70,align:'center',formatter:showTooltip">服务小区<br>CELLID</th>
	    						<th data-options="field:'cellName',width:70,align:'center',formatter:showTooltip">服务小区</th>
	    						<th data-options="field:'eventType',width:70,align:'center',
									formatter: function(value,row,index){
										if(0==value){
											return '<div title=语音未接通 >语音未接通</div>';
										}else if(1==value){
											return '<div title=语音掉话 >语音掉话</div>';
										}else if(2==value){
											return '<div title=注册失败 >注册失败</div>';
										}else if(3==value){
											return '<div title=CSFB失败 >CSFB失败</div>';
										}
										return null;
									}
								">事件类型</th>
	    						<th data-options="field:'rsrpAvg',width:50,align:'center',formatter:numToFixed2Formatter,sortable:isEESortable">RSRP</th>
	    						<th data-options="field:'sinrAvg',width:50,align:'center',formatter:numToFixed2Formatter,sortable:isEESortable">SINR</th>
	    						<th data-options="field:'failDateTime',width:80,align:'center',formatter:showTooltip">异常事件<br>时间</th>
	    						<th data-options="field:'fileName',width:100,align:'center',formatter:showTooltip">测试Log名称</th>
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
	    					<c:forEach items="${events}" var="event">
		    					<tr>
		    						<td>${event.id}</td>
		    						<td>${event.testLogItem.boxId}</td>
		    						<td>${event.m_stRoadName}</td>
		    						<td>${event.cellId}</td>
		    						<td>${event.cellName}</td>
		    						<td>${event.eventType}</td>
		    						<td>${event.rsrpAvg}</td>
		    						<td>${event.sinrAvg}</td>
		    						<td>${event.failDateTime}</td>
		    						<td>${event.testLogItem.fileName}</td>
		    						<td>${event.testLogItem.recSeqNo}</td>
		    						<td>${event.beginLatitude}</td>
		    						<td>${event.courseLatitude}</td>
		    						<td>${event.endLatitude}</td>
		    						<td>${event.beginLongitude}</td>
		    						<td>${event.courseLongitude}</td>
		    						<td>${event.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == events}">
	    						<tr>
		    						<td></td>
		    						<td>未选择原始日志</td>
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
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != events && empty events}">
	    						<tr>
	    							<td></td>
		    						<td>该原始日志下没有异常事件</td>
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
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:25px;text-decoration:underline;" onclick="doAnalysis();" title="异常事件分析">分析</a>
					</div>
				</div>
				<div data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
					<table id="eventSignallingTable" class="easyui-datagrid" data-options="title:'信令流程',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
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
	    						<td>未选择异常事件</td>
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
    	<!-- 东,对比异常事件及对比异常事件信令列表 -->
    	<div data-options="region:'east',border:false" style="width:50%;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
				<div data-options="region:'north',border:false" style="height:50%;padding:0px 0px 4px 2px;">
					<table id="compareEventTable" class="easyui-datagrid" data-options="title:'对比日志异常事件',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#compareTt',onLoadSuccess:mainTableLoadSuccess1,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'boxid',width:80,align:'center',formatter:showTooltip,sortable:isCompareEESortable">BOXID</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center',formatter:showTooltip">路段名称</th>
	    						<th data-options="field:'cellId',width:70,align:'center',formatter:showTooltip">服务小区<br>CELLID</th>
	    						<th data-options="field:'cellName',width:70,align:'center',formatter:showTooltip">服务小区</th>
	    						<th data-options="field:'eventType',width:70,align:'center',
									formatter: function(value,row,index){
										if(0==value){
											return '<div title=语音未接通 >语音未接通</div>';
										}else if(1==value){
											return '<div title=语音掉话 >语音掉话</div>';
										}else if(2==value){
											return '<div title=注册失败 >注册失败</div>';
										}else if(3==value){
											return '<div title=CSFB失败 >CSFB失败</div>';
										}
										return null;
									}
								">事件类型</th>
	    						<th data-options="field:'rsrpAvg',width:50,align:'center',formatter:numToFixed2Formatter,sortable:isCompareEESortable">RSRP</th>
	    						<th data-options="field:'sinrAvg',width:50,align:'center',formatter:numToFixed2Formatter,sortable:isCompareEESortable">SINR</th>
	    						<th data-options="field:'failDateTime',width:80,align:'center',formatter:showTooltip">异常事件<br>时间</th>
	    						<th data-options="field:'fileName',width:100,align:'center',formatter:showTooltip">测试Log名称</th>
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
	    					<c:forEach items="${compareEvents}" var="compareEvent">
		    					<tr>
		    						<td>${compareEvent.id}</td>
		    						<td>${compareEvent.testLogItem.boxId}</td>
		    						<td>${compareEvent.m_stRoadName}</td>
		    						<td>${compareEvent.cellId}</td>
		    						<td>${compareEvent.cellName}</td>
		    						<td>${compareEvent.eventType}</td>
		    						<td>${compareEvent.rsrpAvg}</td>
		    						<td>${compareEvent.sinrAvg}</td>
		    						<td>${compareEvent.failDateTime}</td>
		    						<td>${compareEvent.testLogItem.fileName}</td>
		    						<td>${compareEvent.testLogItem.recSeqNo}</td>
		    						<td>${compareEvent.beginLatitude}</td>
		    						<td>${compareEvent.courseLatitude}</td>
		    						<td>${compareEvent.endLatitude}</td>
		    						<td>${compareEvent.beginLongitude}</td>
		    						<td>${compareEvent.courseLongitude}</td>
		    						<td>${compareEvent.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == compareEvents}">
	    						<tr>
		    						<td></td>
		    						<td>未选择对比日志</td>
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
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != compareEvents && empty compareEvents}">
	    						<tr>
	    							<td></td>
		    						<td>该对比日志下没有异常事件</td>
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
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    				</tbody>
	    			</table>
	    			<div id="compareTt" >
						<a href="#" style="width:25px;text-decoration:underline;" onclick="doCompareAnalysis();" title="异常事件分析">分析</a>
					</div>
				</div>
				<div data-options="region:'center',border:false" style="padding:0px 0px 0px 2px;">
					<table id="compareEventSignallingTable" class="easyui-datagrid" data-options="title:'信令流程',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
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
	    						<td>未选择异常事件</td>
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
	</body>
</html>
