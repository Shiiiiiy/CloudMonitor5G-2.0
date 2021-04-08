<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>干扰</title>

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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	//获取及回显道路名称
	var mainTableId = 'disturbTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>videoQuality/roadNameCallBack.action";
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
	//gisCommon会调用获取质差路段ID
	function getGpsPointVQBId(){
		return volteqbrid;
	}
	//gisCommon会调用获取生成质差路段轨迹的url
	function getVQBGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryVQBIndexGpsPoint.action";
	}
	//gisCommon会调用获取生成质差路段轨迹行驶方向的url
	function getVQBDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryVQBDirection.action";
	}
	//gisCommon会调用获取小区与邻区连线的url
	function VQBCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryVQBCellToCell.action";
	}
	//gisCommon会调用获取LTE切换事件图标轨迹的URL
	function getVQBLTEGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryVQBLTEGpsPoint.action";
	}
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var disturbRow = $("#disturbTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!disturbRow){// 操作前至少选中一条
			$.messager.alert("提示","请请勾选某个质差问题!",'warning');
			return ;
		}
		testLogItemIds = disturbRow.recSeqNo;
		volteqbrid = disturbRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差问题!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/videoQuality/doDisturbAnalysis.action",{roadId:disturbRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					//小区详情表
					/* $("#disturbCellInfoTable").datagrid('loadData',[]);
					$("#disturbCellInfoTable").datagrid({
						title:disturbRoadName+"问题路段服务小区详情",
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
						columns:[[
							{field:'cellName',width:100,align:'center',title:'小区友好名',formatter:showTooltip}, 				
							{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
							{field:'sinrAvg',width:50,align:'center',title:'SINR均值',formatter:numToFixed2Formatter,sortable:true},
							{field:'sinrMin',width:50,align:'center',title:'SINR最低值',formatter:numToFixed2Formatter,sortable:true},
							{field:'overlapCoverRatio',width:70,align:'center',title:'重叠覆盖占比(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true},
							{field:'distanceAvg',width:60,align:'center',title:'持续距离(m)',formatter:numToFixed2Formatter,sortable:true},
							{field:'testTime',width:60,align:'center',title:'测试时间(s)',formatter:numDivide1000Formatter,sortable:true}
						]]
					}); */
					$("#disturbAdviceTable").datagrid('loadData',result.disturbPciOrSignalStrengthAdjust);
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/videoQuality/checkDrawCellFileName.action",{roadId:disturbRow.id},
			function(result){
				//如果上次为null或者undefined,或者当前图层与上一次图层不一样
				if(!drawCellFileName||result!=drawCellFileName){
					mapIframe.window.drawCell();
				}
				drawCellFileName = result;
			}
		,"json");
		mapIframe.window.drawQBR();
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	
	//质差路段字段排序开关
	var isRoadSortable = true ;
	<c:if test="${null == disturbs || empty disturbs}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == disturbs || empty disturbs}">
			$("#disturbTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxId',
				colspan:9
			});
		</c:if>
		
		$("#disturbAdviceTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:4
		});
	});
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=35" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 干扰问题统计 -->
		    	<div data-options="region:'north',border:false" style="height:55%;">
  					<table id="disturbTable" class="easyui-datagrid" data-options="title:'问题列表',scrollbarSize:0,onLoadSuccess:mainTableLoadSuccess,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3'"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'boxId',width:100,align:'center',formatter:showTooltip">BOXID</th>
	    						<th data-options="field:'callType',width:80,align:'center',
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '主叫';
											}else if(1==value){
												return '被叫';
											}
										}
										return value;
									}
	    						">主/被叫</th>
	    						<th data-options="field:'time',width:80,align:'center',formatter:showTooltip">发生时间</th>
	    						<th data-options="field:'vmos',width:80,align:'center',formatter:showTooltip">VMOS</th>
	    						<th data-options="field:'keyParameterCause',width:60,align:'center',
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '<div title=丢包率原因>丢包率原因</div>';
											}else if(1==value){
												return '<div title=视频码率>视频码率</div>';
											}else if(2==value){
												return '<div title=视频帧率>视频帧率</div>';
											}else if(3==value){
												return '<div title=音频码率>音频码率</div>';
											}else if(-1==value){
												return '<div title=其它原因>其它原因</div>';
											}
										}
										return value;
									}
	    						">关键参<br>数原因</th>
	    						<th data-options="field:'cellName',width:60,align:'center',formatter:showTooltip">服务小区</th>
	    						<th data-options="field:'cellPci',width:60,align:'center',formatter:showTooltip">服务小区<br>PCI</th>
	    						<th data-options="field:'optimization',width:100,align:'center',formatter:showTooltip">优化建议</th>
	    						<th data-options="field:'fileName',width:120,align:'center',formatter:showTooltip">日志名称</th>
	    						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true">日志ID</th>
	    						<th data-options="field:'beginLatitude',width:120,align:'center',hidden:true">开始纬度</th>
	    						<th data-options="field:'courseLatitude',width:120,align:'center',hidden:true">中间纬度</th>
	    						<th data-options="field:'endLatitude',width:120,align:'center',hidden:true">结束纬度</th>
	    						<th data-options="field:'beginLongitude',width:120,align:'center',hidden:true">开始经度</th>
	    						<th data-options="field:'courseLongitude',width:120,align:'center',hidden:true">中间经度</th>
	    						<th data-options="field:'endLongitude',width:120,align:'center',hidden:true">结束经度</th>
	    						<th data-options="field:'m_stRoadName',width:100,align:'center',hidden:true">路段名称</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${disturbs}" var="disturb">
		    					<tr>
		    						<td>${disturb.id}</td>
		    						<td>${disturb.testLogItem.boxId}</td>
		    						<td>${disturb.testLogItem.callType}</td>
		    						<td>${disturb.timeValue}</td>
		    						<td>${disturb.vmos}</td>
		    						<td>${disturb.keyParameterCause}</td>
		    						<td>${disturb.cellName}</td>
		    						<td>${disturb.cellPci}</td>
		    						<td>${disturb.optimization}</td>
		    						<td>${disturb.testLogItem.fileName}</td>
		    						<td>${disturb.testLogItem.recSeqNo}</td>
		    						<td>${disturb.latitude}</td>
		    						<td>${disturb.latitude}</td>
		    						<td>${disturb.latitude}</td>
		    						<td>${disturb.longitude}</td>
		    						<td>${disturb.longitude}</td>
		    						<td>${disturb.longitude}</td>
		    						<td>${disturb.m_stRoadName}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == disturbs}">
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
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != disturbs && empty disturbs}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有干扰问题</td>
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
						<a href="#" style="width:50px;text-decoration:underline;" onclick="doAnalysis();" title="分析">分析</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<!-- 干扰问题优化建议汇总 -->
		    	<div data-options="region:'center',border:false" >
					<table id="disturbAdviceTable" class="easyui-datagrid" data-options="title:'优化建议:PCI或信号强度调整',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    <thead>
					    	<tr>
					    		<th data-options="field:'cellName',width:100,align:'center',formatter:showTooltip">小区名</th>
					    		<th data-options="field:'pci',width:50,align:'center'">PCI</th>
					    		<th data-options="field:'earfcn',width:50,align:'center'">EARFCN</th>
					    		<th data-options="field:'rsrp',width:50,align:'center',formatter:numToFixed2Formatter">RSRP</th>
					    		<th data-options="field:'toProblemDotDistance',width:60,align:'center',formatter:numToFixed2Formatter">和问题采样点距离(m)</th>
					    	</tr>
					    </thead>
					   <tbody>
				    		<tr>
					    		<td>未选择干扰问题</td>
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
