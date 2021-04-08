<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>下行调度数小</title>

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
	var mainTableId = 'downDispatchSmallTable';
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
		var downDispatchSmallRow = $("#downDispatchSmallTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!downDispatchSmallRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个质差问题!",'warning');
			return ;
		}
		testLogItemIds = downDispatchSmallRow.recSeqNo;
		volteqbrid = downDispatchSmallRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差问题!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/videoQuality/doDownDispatchSmallAnalysis.action",{roadId:downDispatchSmallRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#downDispatchSmallAdviceTable").datagrid('loadData',result.downDispatchSmallAdjust);
					$("#downDispatchSmallKeyIndexTable").datagrid('loadData',result.downDispatchSmallKeyIndex);
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/videoQuality/checkDrawCellFileName.action",{roadId:downDispatchSmallRow.id},
			function(result){
				//如果上次为null或者undefined,或者当前图层与上一次图层不一样
				if(!drawCellFileName||result!=drawCellFileName){
					mapIframe.window.drawCell();
				}
				drawCellFileName = result;
			}
		,"json");
		//alert("laola");
		mapIframe.window.drawQBR();
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	
	//质差路段字段排序开关
	var isRoadSortable = true ;
	<c:if test="${null == downDispatchSmalls || empty downDispatchSmall}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == downDispatchSmalls || empty downDispatchSmalls}">
			$("#downDispatchSmallTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxId',
				colspan:9
			});
		</c:if>
		$("#downDispatchSmallAdviceTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:6
		});
		$("#downDispatchSmallKeyIndexTable").datagrid('mergeCells',{
			index: 0,
			field: 'videoBitRate',
			colspan:5
		});
	});
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=38" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 乒乓切换统计 -->
		    	<div data-options="region:'north',border:false" style="height:33%;">
	    			<table id="downDispatchSmallTable" class="easyui-datagrid" data-options="title:'问题列表',scrollbarSize:0,onLoadSuccess:mainTableLoadSuccess,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'boxId',width:120,align:'center',formatter:showTooltip">BOXID</th>
	    						<th data-options="field:'callType',width:60,align:'center',sortable:isRoadSortable,
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
	    						<th data-options="field:'time',width:90,align:'center',formatter:showTooltip">发生时间<br>(s)</th>
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
	    						<th data-options="field:'pdcchDlGrant',width:60,align:'center',formatter:showTooltip">PDCCH<br>DLGrant<br>Num/s</th>
	    						<th data-options="field:'optimization',width:120,align:'center',formatter:showTooltip">优化建议</th>
	    						<th data-options="field:'fileName',width:120,align:'center',formatter:showTooltip">测试日志名称</th>
	    						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true">测试日志ID</th>
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
	    					<c:forEach items="${downDispatchSmalls}" var="downDispatchSmall">
		    					<tr>
		    						<td>${downDispatchSmall.id}</td>
		    						<td>${downDispatchSmall.testLogItem.boxId}</td>
		    						<td>${downDispatchSmall.testLogItem.callType}</td>
		    						<td>${downDispatchSmall.timeValue}</td>
		    						<td>${downDispatchSmall.vmos}</td>
	    							<td>${downDispatchSmall.keyParameterCause}</td>
		    						<td>${downDispatchSmall.cellName}</td>
		    						<td>${downDispatchSmall.pdcchDlGrant}</td>
		    						<td>${downDispatchSmall.optimization}</td>
		    						<td>${downDispatchSmall.testLogItem.fileName}</td>
		    						<td>${downDispatchSmall.testLogItem.recSeqNo}</td>
		    						<td>${downDispatchSmall.latitude}</td>
		    						<td>${downDispatchSmall.latitude}</td>
		    						<td>${downDispatchSmall.latitude}</td>
		    						<td>${downDispatchSmall.longitude}</td>
		    						<td>${downDispatchSmall.longitude}</td>
		    						<td>${downDispatchSmall.longitude}</td>
		    						<td>${downDispatchSmall.m_stRoadName}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == downDispatchSmalls}">
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
	    					<c:if test="${null != downDispatchSmalls && empty downDispatchSmalls}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有下行调度数小问题</td>
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
			  
			    <div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
  					<table id="downDispatchSmallKeyIndexTable" class="easyui-datagrid" data-options="title:'关键指标',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
				   		<thead>
				   			<tr>
				   				<th data-options="field:'videoBitRate',width:40,align:'center'">视频码率</th>
				   				<th data-options="field:'videoFrameRate',width:40,align:'center'">视频帧率</th>
				   				<th data-options="field:'packLossRate',width:40,align:'center',formatter:numToFixed2Formatter">丢包率</th>
				   				<th data-options="field:'irtt',width:40,align:'center',formatter:numToFixed2Formatter">i_RTT</th>
				   				<th data-options="field:'audioBitRate',width:40,align:'center'">音频码率</th>
				   			</tr>
				   		</thead>
				   		<tbody>
		    				<tr>
			    				<td>未选择下行调度数小问题</td>
			    				<td></td>
			    				<td></td>
			    				<td></td>
			    				<td></td>
			    			</tr>
			    		</tbody>
				   	</table>
  				</div>
		    	
		    	<div data-options="region:'south',border:false" style="height:34%;padding:2px 0px 0px 0px;">
				   	
				   	<table id="downDispatchSmallAdviceTable" class="easyui-datagrid" data-options="title:'优化建议：核查小区用户数以及是否存在干扰',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'cellName',width:90,align:'center',formatter:showTooltip">小区名</th>
	    						<th data-options="field:'pci',width:60,align:'center'">PCI</th>
	    						<th data-options="field:'earfcn',width:90,align:'center'">EARFCN</th>
	    						<th data-options="field:'rsrp',width:60,align:'center',formatter:numToFixed2Formatter">RSRP(dBm)</th>
	    						<th data-options="field:'sinr',width:60,align:'center',formatter:numToFixed2Formatter">SINR(dB)</th>
	    						<th data-options="field:'toProblemDotDistance',width:40,align:'center',formatter:numToFixed2Formatter">和问题采样<br>点距离(m)</th>
	    					</tr>
	    				</thead>
    					<tbody>
    						<tr>
	    						<td>未选择下行调度数小问题</td>
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
