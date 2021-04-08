<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE质量专题----语音RTP连续丢包问题分析</title>

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
	
	var mainTableId = 'notConnectEventTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>voiceNotConnect/roadNameCallBack.action";
	}
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//存储id
	var packetId;
	var lastPacketid;//上一次id
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var notConnectRow = $("#notConnectEventTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!notConnectRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个丢包!",'warning');
			return ;
		}
		testLogItemIds = notConnectRow.recSeqNo;
		packetId = notConnectRow.id;
		if(!packetId){
			$.messager.alert("提示","请勾选某个丢包事件!",'warning');
			return ;
		}
		//本次和上一次的异常事件id不同
		if(lastPacketid!=packetId){
			$("#Table0").datagrid('loadData',[]);
			$("#Table1").datagrid('loadData',[]);
			$("#Table2").datagrid('loadData',[]);
			$("#Table3").datagrid('loadData',[]);
			$("#Table4").datagrid('loadData',[]);
			$("#Table5").datagrid('loadData',[]);
			$("#Table6").datagrid('loadData',[]);
			$.post("${pageContext.request.contextPath}/lostPacket/doProblemAnalysis.action",{packetId:packetId},
					function(result){
						if (result.errorMsg) {
							$.messager.alert("系统提示", result.errorMsg,'error');
						} else {
							$("#Table0").datagrid('loadData',result.problemIndex0.rows);
							$("#Table1").datagrid('loadData',result.problemIndex1.rows);
							$("#Table2").datagrid('loadData',result.problemIndex2.rows);
							$("#Table3").datagrid('loadData',result.problemIndex3.rows);
							$("#Table4").datagrid('loadData',result.problemIndex4.rows);
							$("#Table5").datagrid('loadData',result.problemIndex5.rows);
							$("#Table6").datagrid('loadData',result.problemIndex6.rows);
							
						}
					}
				,"json");
		lastPacketid = packetId;
	}
	}
	//报表导出共用
	function exportPublic(indexNo){
		goToPage("${pageContext.request.contextPath}/lostPacket/downloadLostPacketRTPExcel.action?indexNo="+indexNo+"");
	}
	//质差路段字段排序开关
	var isEESortable = true ;
	<c:if test="${null == lostPackets || empty lostPackets}">
		isEESortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == lostPackets || empty lostPackets}">
			$("#notConnectEventTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxId',
				colspan:9
			});
		</c:if>
		
	});
	</script>
  </head>
   <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<div data-options="region:'center',border:false" style="padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 问题列表-->
		    	<div data-options="region:'north',border:false" style="height:24%;">
  					<table id="notConnectEventTable" class="easyui-datagrid" data-options="title:'问题列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt',remoteSort:false,multiSort:true"  >
					<!--<table id="notConnectEventTable" class="easyui-datagrid" data-options="title:'问题列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  > -->
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'boxId',width:80,align:'center',formatter:showTooltip,sortable:isEESortable">BOXID</th>
	    						<th data-options="field:'isCalling',width:60,align:'center',formatter:showTooltip">主/被叫</th>
	    						<th data-options="field:'failTime',width:80,align:'center',formatter:showTooltip">发生时间</th>
	    						<th data-options="field:'lostPacketNum',width:40,align:'center',formatter:showTooltip">丢包数</th>
	    						<th data-options="field:'problemNode',width:40,align:'center',formatter:showTooltip,
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '<div title=发送端手机上行丢包>发送端手机上行丢包</div>';
											}else if(1==value){
												return '<div title=发送端S1口丢包>发送端S1口丢包</div>';
											}else if(2==value){
												return '<div title=发送端SGi口丢包>发送端SGi口丢包</div>';
											}else if(3==value){
												return '<div title=接收端SGi口丢包>接收端SGi口丢包</div>';
											}else if(4==value){
												return '<div title=接收端S1口丢包>接收端S1口丢包</div>';
											}else if(5==value){
												return '<div title=接收端Uu口丢包>接收端Uu口丢包</div>';
											}
										}
										return value;
									}
	    						">问题节点</th>
	    						<th data-options="field:'cellName',width:40,align:'center',formatter:showTooltip">服务小区</th>
	    						<th data-options="field:'m_dbRsrpValueSum',width:40,align:'center',formatter:showTooltip">RSRP均值</th>
	    						<th data-options="field:'m_dbSinrValueSum',width:40,align:'center',formatter:showTooltip">SINR均值</th>
	    						<th data-options="field:'fileName',width:100,align:'center',formatter:showTooltip">日志名称</th>
	    						<th data-options="field:'recSeqNo',width:20,align:'center',hidden:true">recSeqNo</th>
	    						<th data-options="field:'beginLatitude',width:20,align:'center',hidden:true">开始纬度</th>
	    						<th data-options="field:'courseLatitude',width:20,align:'center',hidden:true">中间纬度</th>
	    						<th data-options="field:'endLatitude',width:20,align:'center',hidden:true">结束纬度</th>
	    						<th data-options="field:'beginLongitude',width:20,align:'center',hidden:true">开始经度</th>
	    						<th data-options="field:'courseLongitude',width:20,align:'center',hidden:true">中间经度</th>
	    						<th data-options="field:'endLongitude',width:20,align:'center',hidden:true">结束经度</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${lostPackets}" var="lostPacket">
		    					<tr>
		    						<td>${lostPacket.id}</td>
		    						<td>${lostPacket.testLogItem.boxId}</td>
									<c:choose>
										<c:when test="${lostPacket.testLogItem.callType==0}">
											<td>主叫</td>
										</c:when>
										<c:when test="${lostPacket.testLogItem.callType==1}">
											<td>被叫</td>
										</c:when>
										<c:otherwise>
											<td>${lostPacket.testLogItem.callType}</td>
										</c:otherwise>
									</c:choose>
		    						<td>${lostPacket.failTimeString}</td>
		    						<td>${lostPacket.lostPacketNum}</td>
		    						<td>${lostPacket.problemNode}</td>
		    						<td>${lostPacket.cellName}</td>
		    						<td>${lostPacket.rsrp}</td>
		    						<td>${lostPacket.sinr}</td>
		    						<td>${lostPacket.testLogItem.fileName}</td>
		    						<td>${lostPacket.testLogItem.recSeqNo}</td>
		    						<td>${lostPacket.beginLatitude}</td>
		    						<td>${lostPacket.courseLatitude}</td>
		    						<td>${lostPacket.endLatitude}</td>
		    						<td>${lostPacket.beginLongitude}</td>
		    						<td>${lostPacket.courseLongitude}</td>
		    						<td>${lostPacket.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == testLogItemIds}">
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
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != lostPackets && empty lostPackets}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有RTP连续丢包事件</td>
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
						<a href="#" style="width:50px;text-decoration:underline;" onclick="doAnalysis();" title="问题分析">问题分析分析</a>
					</div>
		    	</div>
		    	
		    	<!-- 节点丢包情况 -->
				<div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
					<table id="Table0" class="easyui-datagrid" data-options="title:'节点丢包情况',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
	    				<thead>
	    					<tr>
	    						<th colspan="3" >发送端</th>
	    						<th colspan="3" >接收端</th>
	    						<th rowspan="2"data-options="field:'beginSequence',width:80,align:'center'">开始Sequence</th>
	    						<th rowspan="2" data-options="field:'endSequence',width:80,align:'center'">结束Sequence</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'sendUE',width:50,align:'center'">UE</th>
	    						<th data-options="field:'sendS1',width:50,align:'center'">S1</th>
	    						<th data-options="field:'sendSGi',width:50,align:'center'">SGi</th>
	    						<th data-options="field:'receUE',width:50,align:'center'">UE</th>
	    						<th data-options="field:'receS1',width:50,align:'center'">S1</th>
	    						<th data-options="field:'receSGi',width:50,align:'center'">SGi</th>
	    					</tr>
	    				</thead>
	    			</table>
				</div>
				<!--  RTP包 -->
				<div data-options="region:'south',border:false"  style="height:55%;padding:2px 0px 0px 0px;">
					<div class="easyui-layout" style="width:100%;height: 100%;">
						<div data-options="region:'east',border:false" style="width:50%;padding:0px 0px 0px 2px;">
			    			<div class="easyui-layout" style="width:100%;height: 100%;">
				    			<div data-options="title:'发送端手机 RTP上行包',collapsible:false,region:'north',border:false,tools:'#tt1'" style="height:33%;">
				    				<table id="Table1" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
			    						<thead>
					    					<tr>
					    						<th data-options="field:'time',width:120,align:'center'">Time</th>
					    						<th data-options="field:'ssrc',width:50,align:'center',formatter:num16Formatter">SSRC</th>
					    						<th data-options="field:'sequence',width:65,align:'center'">Sequence</th>
					    						<th data-options="field:'payloadLen',width:70,align:'center'">PayloadLen</th>
					    						<th data-options="field:'timestamp',width:60,align:'center'">RTP Timestamp</th>
					    					</tr>
			    						</thead>
			    						<tbody>
				    						<tr>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
			    						</tbody>
			    					</table>
				    			</div>
				    			<div  data-options="title:'发送端S1接口RTP上行包',collapsible:false,region:'center',border:false,tools:'#tt2'" style="padding:0px 2px 0px 0px;">
				    				<table id="Table2" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
			    						<thead>
					    					<tr>
					    						<th data-options="field:'time',width:120,align:'center'">Time</th>
					    						<th data-options="field:'ssrc',width:50,align:'center',formatter:num16Formatter">SSRC</th>
					    						<th data-options="field:'sequence',width:65,align:'center'">Sequence</th>
					    						<th data-options="field:'payloadLen',width:70,align:'center'">PayloadLen</th>
					    						<th data-options="field:'timestamp',width:60,align:'center'">RTP Timestamp</th>
					    					</tr>
			    						</thead>
			    						<tbody>
				    						<tr>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
			    						</tbody>
			    					</table>
			    					
				    			</div>
				    			<div data-options="title:'发送端SGi接口RTP上行包',collapsible:false,region:'south',border:false,tools:'#tt3'"  style="height:33%;padding:2px 0px 0px 0px;">
				    				<table id="Table3" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
			    						<thead>
					    					<tr>
					    						<th data-options="field:'time',width:120,align:'center'">Time</th>
					    						<th data-options="field:'ssrc',width:50,align:'center',formatter:num16Formatter">SSRC</th>
					    						<th data-options="field:'sequence',width:65,align:'center'">Sequence</th>
					    						<th data-options="field:'payloadLen',width:70,align:'center'">PayloadLen</th>
					    						<th data-options="field:'timestamp',width:60,align:'center'">RTP Timestamp</th>
					    					</tr>
			    						</thead>
			    						<tbody>
				    						<tr>
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
			    		<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
				    		<div class="easyui-layout" style="width:100%;height: 100%;">
				    			<div data-options="title:'接收端UE接口RTP下行包',region:'north',collapsible:false,border:false,tools:'#tt6'"  style="height:33%;padding:2px 0px 0px 0px;">
				    				<table id="Table6" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
			    						<thead>
					    					<tr>
					    						<th data-options="field:'time',width:120,align:'center'">Time</th>
					    						<th data-options="field:'ssrc',width:50,align:'center',formatter:num16Formatter">SSRC</th>
					    						<th data-options="field:'sequence',width:65,align:'center'">Sequence</th>
					    						<th data-options="field:'payloadLen',width:70,align:'center'">PayloadLen</th>
					    						<th data-options="field:'timestamp',width:60,align:'center'">RTP Timestamp</th>
					    					</tr>
			    						</thead>
			    						<tbody>
				    						<tr>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
			    						</tbody>
			    					</table>
			    					
				    			</div>
				    			<div  data-options="title:'接收端S1接口RTP下行包',collapsible:false,region:'center',border:false,tools:'#tt5'" style="padding:0px 2px 0px 0px;">
				    				<table id="Table5" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
			    						<thead>
					    					<tr>
					    						<th data-options="field:'time',width:120,align:'center'">Time</th>
					    						<th data-options="field:'ssrc',width:50,align:'center',formatter:num16Formatter">SSRC</th>
					    						<th data-options="field:'sequence',width:65,align:'center'">Sequence</th>
					    						<th data-options="field:'payloadLen',width:70,align:'center'">PayloadLen</th>
					    						<th data-options="field:'timestamp',width:60,align:'center'">RTP Timestamp</th>
					    					</tr>
			    						</thead>
			    						<tbody>
				    						<tr>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
			    						</tbody>
			    					</table>
				    			</div>
				    			<div data-options="title:'接收端SGi接口RTP下行包',collapsible:false,region:'south',border:false,tools:'#tt4'" style="height:33%;">
				    				<table id="Table4" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
			    						<thead>
					    					<tr>
					    						<th data-options="field:'time',width:120,align:'center'">Time</th>
					    						<th data-options="field:'ssrc',width:50,align:'center',formatter:num16Formatter">SSRC</th>
					    						<th data-options="field:'sequence',width:65,align:'center'">Sequence</th>
					    						<th data-options="field:'payloadLen',width:70,align:'center'">PayloadLen</th>
					    						<th data-options="field:'timestamp',width:60,align:'center'">RTP Timestamp</th>
					    					</tr>
			    						</thead>
			    						<tbody>
				    						<tr>
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
    	<div id="tt1">
			<a href="#" style="width:30px;text-decoration:underline;" onclick="exportPublic(0);" title="导出">导出</a>
		</div>
    	<div id="tt2">
			<a href="#" style="width:30px;text-decoration:underline;" onclick="exportPublic(1);" title="导出">导出</a>
		</div>
    	<div id="tt3">
			<a href="#" style="width:30px;text-decoration:underline;" onclick="exportPublic(2);" title="导出">导出</a>
		</div>
    	<div id="tt4">
			<a href="#" style="width:30px;text-decoration:underline;" onclick="exportPublic(3);" title="导出">导出</a>
		</div>
    	<div id="tt5">
			<a href="#" style="width:30px;text-decoration:underline;" onclick="exportPublic(4);" title="导出">导出</a>
		</div>
    	<div id="tt6">
			<a href="#" style="width:30px;text-decoration:underline;" onclick="exportPublic(5);" title="导出">导出</a>
		</div>
	</body>
</html>