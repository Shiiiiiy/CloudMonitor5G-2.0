<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>弱覆盖连续无线差路段分析</title>

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
	var mainTableId = 'weakCoverRoadTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>volteCWBR/roadNameCallBack.action";
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
	function getGpsPointCWBRId(){
		return volteqbrid;
	}
	//gisCommon会调用获取生成质差路段轨迹的url
	function getCWBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCWBRIndexGpsPoint.action";
	}
	//gisCommon会调用获取生成质差路段轨迹行驶方向的url
	function getCWBRDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryCWBRDirection.action";
	}
	//gisCommon会调用获取小区与邻区连线的url
	function CWBRCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryCWBRCellToCell.action";
	}
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var weakCoverRow = $("#weakCoverRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!weakCoverRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		testLogItemIds = weakCoverRow.recSeqNo;
		volteqbrid = weakCoverRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/volteCWBR/doWeakCoverRoadAnalysis.action",{roadId:weakCoverRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					var weakCoverRoadName = weakCoverRow.m_stRoadName;
					
					//小区详情表
					$("#weakCoverCellInfoTable").datagrid('loadData',[]);
					$("#weakCoverCellInfoTable").datagrid({
						title:weakCoverRoadName+"问题路段服务小区详情",
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
						columns:[[
							{field:'cellName',width:120,align:'center',title:'小区友好名',formatter:showTooltip}, 				
							{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
							{field:'rsrpAvg',width:65,align:'center',title:'RSRP均值(dBm)',formatter:numToFixed2Formatter,sortable:true},
							{field:'rsrpMin',width:70,align:'center',title:'RSRP最低值(dBm)',formatter:numToFixed2Formatter,sortable:true},
							{field:'distanceAvg',width:60,align:'center',title:'持续距离(m)',formatter:numToFixed2Formatter,sortable:true},
							{field:'testTime',width:55,align:'center',title:'测试时间(s)',formatter:numDivide1000Formatter,sortable:true}
						]]
					});
					$("#weakCoverCellInfoTable").datagrid('loadData',result.weakCoverRoadCellInfo);
					
					//优化建议
					$("#weakCoverAutoTable").datagrid('loadData',[]);
					if(weakCoverRow.optimizeAdvise){
						//建议加站
						if(0==weakCoverRow.optimizeAdvise){
							$("#weakCoverAutoTable").datagrid({
								title:weakCoverRoadName+"路段"+'优化建议:建议加站问题小区覆盖详情',
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
									{field:'cellId',width:80,align:'center',title:'小区CELLID',sortable:true},
									{field:'distanceAvg',width:80,align:'center',title:'和弱覆盖路段<br>距离(m)',formatter:numToFixed2Formatter,sortable:true},
									{field:'cellDistance',width:80,align:'center',title:'该小区本身站<br>间距(m)',formatter:numToFixed2Formatter,sortable:true},
									{field:'rsrpAvg',width:60,align:'center',title:'RSRP均值<br>(dBm)',formatter:numToFixed2Formatter,sortable:true}
								]]
							});
							$("#weakCoverAutoTable").datagrid('loadData',result.weakCoverRoadAdviceAddStationCellInfo);
						//反向覆盖,建议调整
						}else if(1==weakCoverRow.optimizeAdvise){
							$("#weakCoverAutoTable").datagrid({
								title:weakCoverRoadName+"路段"+'优化建议:天馈接反详情表',
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
									{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
									{field:'azimuth',width:50,align:'center',title:'工参方位角',sortable:true},
								]]
							});
							$("#weakCoverAutoTable").datagrid('loadData',result.weakCoverRoadTiankuiConnectReverseCellInfo);
						//天馈调整
						}else if(2==weakCoverRow.optimizeAdvise){
							$("#weakCoverAutoTable").datagrid({
								title:weakCoverRoadName+"路段"+'建议天馈调整小区',
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									/* {field:'adjustRank',width:40,align:'center',title:'调整<br>优先级',sortable:true}, */
									{field:'cellName',width:80,align:'center',title:'小区<br>友好名',formatter:showTooltip}, 				
									{field:'cellId',width:50,align:'center',title:'小区<br>CELLID',sortable:true},
									{field:'distanceAvg',width:80,align:'center',title:'和弱覆盖路段<br>距离(m)',formatter:numToFixed2Formatter,sortable:true},
									{field:'rsrpAvg',width:50,align:'center',title:'RSRP均值<br>(dBm)',formatter:numToFixed2Formatter,sortable:true},
									{field:'downdipAngle',width:50,align:'center',title:'工参<br>下倾角',sortable:true},
									{field:'azimuth',width:50,align:'center',title:'工参<br>方位角',sortable:true},
								]]
							});
							$("#weakCoverAutoTable").datagrid('loadData',result.weakCoverRoadTianKuiAdjustCellInfo);
						}
					}
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:weakCoverRow.id},
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
	<c:if test="${null == weakCoverRoads || empty weakCoverRoads}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == weakCoverRoads || empty weakCoverRoads}">
			$("#weakCoverRoadTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:10
			});
		</c:if>
		$("#weakCoverIndexTable").datagrid('mergeCells',{
			index: 0,
			field: 'callType',
			colspan:8
		});
		$("#weakCoverCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:6
		});
		$("#weakCoverAutoTable").datagrid('mergeCells',{
			index: 0,
			field: 'itemid1',
			/* colspan:9 */
			colspan:6
		});
	});
	
	</script>
<!-- 	<script type="text/javascript" src="http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json&ak=liKpDfLP41rNnZmM1D33WljN&callback=abcdefg"></script> -->
  </head>
  <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" style="width: 100%;height: 100%;" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=13" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 连续无线差问题路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:33%;">
  					<table id="weakCoverRoadTable" class="easyui-datagrid" data-options="title:'弱覆盖原因质差路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
	    						<th rowspan="2" data-options="field:'boxid',width:80,align:'center',sortable:isRoadSortable">BOXID</th>
	    						<th colspan="4" >连续无线差路段信息</th>
	    						<th colspan="11" >连续无线差描述</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center',formatter:showTooltip">路段名称</th>
	    						<th data-options="field:'m_dbDistance',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">持续距离<br>(m)</th>
	    						<th data-options="field:'m_dbContinueTime',width:80,align:'center',formatter:numDivide1000Formatter,sortable:isRoadSortable">测试时间<br>(s)</th>
	    						<th data-options="field:'startTime',width:80,align:'center',formatter:numDivide1000Formatter,sortable:isRoadSortable">开始时间<br>(s)</th>
	    						<th data-options="field:'rsrpAvg',width:70,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">RSRP均<br>值(dBm)</th>
	    						<th data-options="field:'rsrpMin',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">RSRP最低<br>值(dBm)</th>
	    						<th data-options="field:'optimizeAdvise',width:70,align:'center',sortable:isRoadSortable,
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '建议加站';
											}else if(1==value){
												return '<div title=反向覆盖,建议调整>反向覆盖,建议调整</div>';
											}else if(2==value){
												return '天馈调整';
											}
										}
										return value;
									}
								">优化建议</th>
	    						<th data-options="field:'fileName',width:100,align:'center',formatter:showTooltip">测试日志名称</th>
	    						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true">recSeqNo</th>
	    						<th data-options="field:'beginLatitude',width:120,align:'center',hidden:true">开始纬度</th>
	    						<th data-options="field:'courseLatitude',width:120,align:'center',hidden:true">中间纬度</th>
	    						<th data-options="field:'endLatitude',width:120,align:'center',hidden:true">结束纬度</th>
	    						<th data-options="field:'beginLongitude',width:120,align:'center',hidden:true">开始经度</th>
	    						<th data-options="field:'courseLongitude',width:120,align:'center',hidden:true">中间经度</th>
	    						<th data-options="field:'endLongitude',width:120,align:'center',hidden:true">结束经度</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${weakCoverRoads}" var="weakCoverRoad">
		    					<tr>
		    						<td>${weakCoverRoad.id}</td>
		    						<td>${weakCoverRoad.testLogItem.boxId}</td>
		    						<td>${weakCoverRoad.m_stRoadName}</td>
		    						<td>${weakCoverRoad.m_dbDistance}</td>
		    						<td>${weakCoverRoad.m_dbContinueTime}</td>
		    						<td>${weakCoverRoad.startTimeValue}</td>
		    						<td>${weakCoverRoad.rsrpAvg}</td>
		    						<td>${weakCoverRoad.rsrpMin}</td>
		    						<td>${weakCoverRoad.optimizeAdvise}</td>
		    						<td>${weakCoverRoad.testLogItem.fileName}</td>
		    						<td>${weakCoverRoad.testLogItem.recSeqNo}</td>
		    						<td>${weakCoverRoad.beginLatitude}</td>
		    						<td>${weakCoverRoad.courseLatitude}</td>
		    						<td>${weakCoverRoad.endLatitude}</td>
		    						<td>${weakCoverRoad.beginLongitude}</td>
		    						<td>${weakCoverRoad.courseLongitude}</td>
		    						<td>${weakCoverRoad.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == weakCoverRoads}">
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
	    					<c:if test="${null != weakCoverRoads && empty weakCoverRoads}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有弱覆盖质差路段</td>
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
	    			<div id="tt3" >
						<a href="#" style="width:50px;text-decoration:underline;" onclick="doAnalysis();" title="质差路段分析">路段分析</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<!-- 语音质差路段优化建议汇总 -->
		    	<div data-options="region:'center',border:false" >
					<div class="easyui-layout" style="width:100%;height: 100%;">
			
						<div data-options="region:'center',border:false">
							<div class="easyui-layout" style="width:100%;height: 100%;">
								<div data-options="region:'center',border:false" style="height:50%;padding:2px 0px 0px 0px;">
									<table id="weakCoverCellInfoTable" class="easyui-datagrid" data-options="title:'问题路段服务小区详情',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'cellName',width:120,align:'center',formatter:showTooltip">小区友好名</th>
					    						<th data-options="field:'cellId',width:50,align:'center'">小区CELLID</th>
					    						<th data-options="field:'rsrpAvg',width:65,align:'center',formatter:numToFixed2Formatter">RSRP均值(dBm)</th>
					    						<th data-options="field:'rsrpMin',width:70,align:'center',formatter:numToFixed2Formatter">RSRP最低值(dBm)</th>
					    						<th data-options="field:'distanceAvg',width:60,align:'center',formatter:numToFixed2Formatter">持续距离(m)</th>
					    						<th data-options="field:'testTime',width:55,align:'center',formatter:numDivide1000Formatter">测试时间(s)</th>
					    					</tr>
					    				</thead>
					    				<tbody>
				    						<tr>
					    						<td>未选择质差路段</td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
					    				</tbody>
					    			</table>
								</div>
								<div data-options="region:'south',border:false"  style="height:50%;padding:2px 0px 0px 0px;">
									<table id="weakCoverAutoTable" class="easyui-datagrid" data-options="title:'建议天馈调整小区',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<!-- <th data-options="field:'itemid0',width:50,align:'center'">调整<br>优先级</th> -->
					    						<th data-options="field:'itemid1',width:80,align:'center'">小区<br>友好名</th>
					    						<th data-options="field:'itemid2',width:50,align:'center'">小区<br>CELLID</th>
					    						<th data-options="field:'itemid3',width:80,align:'center'">和弱覆盖路段<br>距离(m)</th>
					    						<th data-options="field:'itemid4',width:50,align:'center'">RSRP均值<br>(dBm)</th>
					    						<th data-options="field:'itemid5',width:50,align:'center'">工参<br>下倾角</th>
					    						<th data-options="field:'itemid6',width:50,align:'center'">工参<br>方位角</th>
					    						<!-- <th data-options="field:'itemid7',width:90,align:'center'">采样点占整个质差路段<br>采样点比例(%)</th> -->
					    					</tr>
					    				</thead>
					    				<tbody>
				    						<tr>
					    						<td>未选择质差路段</td>
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
	</body>
</html>
