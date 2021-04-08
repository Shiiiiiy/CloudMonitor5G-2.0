<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>干扰问题路段分析</title>

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
	var mainTableId = 'disturbRoadTable';
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
		var disturbRow = $("#disturbRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!disturbRow){// 操作前至少选中一条
			$.messager.alert("提示","请请勾选某个质差路段!",'warning');
			return ;
		}
		testLogItemIds = disturbRow.recSeqNo;
		volteqbrid = disturbRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/volteCWBR/doDisturbRoadAnalysis.action",{roadId:disturbRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					var disturbRoadName = disturbRow.m_stRoadName;
					//小区详情表
					$("#disturbCellInfoTable").datagrid('loadData',[]);
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
					});
					$("#disturbCellInfoTable").datagrid('loadData',result.disturbRoadCellInfo);
					
					//优化建议
					$("#disturbAutoTable").datagrid('loadData',[]);
					if(disturbRow.optimizeAdvise){
						//三超站点，建议整改
						if(0==disturbRow.optimizeAdvise){
							$("#disturbAutoTable").datagrid({
								title:disturbRoadName+"路段"+'优化建议:三超小区详情',
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									{field:'cellName',width:120,align:'center',title:'小区友好名',formatter:showTooltip}, 				
									{field:'cellId',width:70,align:'center',title:'小区CELLID',sortable:true},
									{field:'distanceAvg',width:70,align:'center',title:'和干扰路段<br>距离(m)',formatter:numToFixed2Formatter,sortable:true},
									{field:'sinrAvg',width:50,align:'center',title:'SINR均值<br>(dBm)',formatter:numToFixed2Formatter,sortable:true},
									{field:'sanChaoType',width:70,align:'center',title:'三超类型',sortable:true,
										formatter: function(value,row,index){
											if (value){
												if(0==value){
													return '超高';
												}else if(1==value){
													return '超远';
												}else if(2==value){
													return '超近';
												}
											}
											return value;
										}
									}
									/* {field:'sampleRatio',width:120,align:'center',title:'采样点占整个质差路段<br>采样点比例(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true} */
								]]
							});
							$("#disturbAutoTable").datagrid('loadData',result.disturbRoadSanChaoCellInfo);
						//PCI调整
						}else if(1==disturbRow.optimizeAdvise){
							$("#disturbAutoTable").datagrid({
								title:disturbRoadName+"路段"+'优化建议:PCI调整详情表',
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									{field:'cellName',width:40,align:'center',title:'小区友好名',formatter:showTooltip}, 				
									{field:'cellId',width:40,align:'center',title:'小区CELLID',sortable:true},
									{field:'originalPci',width:40,align:'center',title:'原PCI',sortable:true},
									{field:'distanceAvg',width:80,align:'center',title:'和干扰路段的距离(m)',formatter:numToFixed2Formatter,sortable:true},
									/* {field:'sampleRatio',width:120,align:'center',title:'采样点占整个质差路段采样点比例(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true} */
								]]
							});
							$("#disturbAutoTable").datagrid('loadData',result.disturbRoadPCICellInfo);
						//天馈调整
						}else if(2==disturbRow.optimizeAdvise){
							var datagridTitle_0 = disturbRoadName+"路段建议天馈调整小区";
							var distanceAvgTitle_0 = "和覆盖路段<br>距离(m)";
							if(result.disturbRoadTiankuiAdjustCellInfo.rows.length){
								if(0==result.disturbRoadTiankuiAdjustCellInfo.rows[0].disturbType){
									datagridTitle_0=disturbRoadName+"路段建议加强主覆盖小区";
									distanceAvgTitle_0 = "和重叠覆盖<br>路段距离(m)";
								}else if(1==result.disturbRoadTiankuiAdjustCellInfo.rows[0].disturbType){
									datagridTitle_0=disturbRoadName+"路段建议解决过覆盖问题小区";
									distanceAvgTitle_0 = "和过叠覆盖<br>路段距离(m)";
								}
							}
							$("#disturbAutoTable").datagrid({
								title:datagridTitle_0,
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									/* {field:'adjustRank',width:50,align:'center',title:'调整<br>优先级',sortable:true}, */
									{field:'cellName',width:50,align:'center',title:'小区<br>友好名',formatter:showTooltip}, 				
									{field:'cellId',width:50,align:'center',title:'小区<br>CELLID',sortable:true},
									{field:'distanceAvg',width:80,align:'center',title:distanceAvgTitle_0,formatter:numToFixed2Formatter,sortable:true},
									{field:'rsrpAvg',width:50,align:'center',title:'RSRP均值<br>(dBm)',formatter:numToFixed2Formatter,sortable:true},
									 {field:'disturbType',width:50,align:'center',title:'干扰类型',sortable:true,
										formatter: function(value,row,index){
											if (value){
												if(0==value){
													return '重叠覆盖';
												}else if(1==value){
													return '过覆盖';
												}
											}
											return value;
										}
									}, 
									{field:'downdipAngle',width:50,align:'center',title:'工参<br>下倾角',sortable:true},
									{field:'azimuth',width:50,align:'center',title:'工参<br>方位角',sortable:true}
									/* {field:'adviceDowndipAngle',width:50,align:'center',title:'建议调整<br>下倾角',sortable:true} */
									/* {field:'sampleRatio',width:120,align:'center',title:'采样点占整个质差路段<br>采样点比例(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true} */
								]]
							});
							$("#disturbAutoTable").datagrid('loadData',result.disturbRoadTiankuiAdjustCellInfo);
						}
					}
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:disturbRow.id},
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
	<c:if test="${null == disturbRoads || empty disturbRoads}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == disturbRoads || empty disturbRoads}">
			$("#disturbRoadTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:11
			});
		</c:if>
		
		$("#disturbCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:7
		});
		$("#disturbAutoTable").datagrid('mergeCells',{
			index: 0,
			field: 'itemid0',
			/* colspan:6 */
			colspan:5
		});
	});
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=14" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 连续无线差路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:33%;">
  					<table id="disturbRoadTable" class="easyui-datagrid" data-options="title:'干扰原因连续无线差路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
	    						<th rowspan="2" data-options="field:'boxid',width:60,align:'center',formatter:showTooltip">BOXID</th>
	    						<th colspan="4" >连续无线差路段信息	</th>
	    						<th colspan="12" >连续无线差描述</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'m_stRoadName',width:100,align:'center',formatter:showTooltip,sortable:isRoadSortable">路段名称</th>
	    						<th data-options="field:'m_dbDistance',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">持续距离<br>(m)</th>
	    						<th data-options="field:'m_dbContinueTime',width:80,align:'center',formatter:numDivide1000Formatter,sortable:isRoadSortable">测试时间<br>(s)</th>
	    						<th data-options="field:'startTime',width:80,align:'center',formatter:showTooltip,sortable:isRoadSortable">开始时间</th>
	    						<th data-options="field:'sinrAvg',width:60,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">SINR均值<br>(dBm)</th>
	    						<th data-options="field:'sinrMin',width:60,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">SINR最低<br>值(dBm)</th>
	    						<th data-options="field:'optimizeAdvise',width:60,align:'center',sortable:isRoadSortable,
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '<div title=三超站点，建议整改>三超站点，建议整改</div>';
											}else if(1==value){
												return 'PCI调整';
											}else if(2==value){
												return '天馈调整';
											}
										}
										return value;
									}
	    						">优化建议</th>
	    						<th data-options="field:'overlapCoverRatio',width:60,align:'center',formatter:numMultiply100ToFixed2Formatter,sortable:isRoadSortable">重叠覆盖<br>占比(%)</th>
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
	    					<c:forEach items="${disturbRoads}" var="disturbRoad">
		    					<tr>
		    						<td>${disturbRoad.id}</td>
		    						<td>${disturbRoad.testLogItem.boxId}</td>
		    						<td>${disturbRoad.m_stRoadName}</td>
		    						<td>${disturbRoad.m_dbDistance}</td>
		    						<td>${disturbRoad.m_dbContinueTime}</td>
		    						<td>${disturbRoad.startTimeValue}</td>
		    						<td>${disturbRoad.sinrAvg}</td>
		    						<td>${disturbRoad.sinrMin}</td>
		    						<td>${disturbRoad.optimizeAdvise}</td>
		    						<td>${disturbRoad.overlapCoverRatio}</td>
		    						<td>${disturbRoad.testLogItem.fileName}</td>
		    						<td>${disturbRoad.testLogItem.recSeqNo}</td>
		    						<td>${disturbRoad.beginLatitude}</td>
		    						<td>${disturbRoad.courseLatitude}</td>
		    						<td>${disturbRoad.endLatitude}</td>
		    						<td>${disturbRoad.beginLongitude}</td>
		    						<td>${disturbRoad.courseLongitude}</td>
		    						<td>${disturbRoad.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == disturbRoads}">
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
		    						<td></td>
		    						<td></td>
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != disturbRoads && empty disturbRoads}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有干扰质差路段</td>
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
									<table id="disturbCellInfoTable" class="easyui-datagrid" data-options="title:'问题路段服务小区详情',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    					
					    						<th data-options="field:'cellName',width:100,align:'center',formatter:showTooltip">小区友好名</th>
					    						<th data-options="field:'cellId',width:50,align:'center'">小区CELLID</th>
					    						<th data-options="field:'sinrAvg',width:50,align:'center',formatter:numToFixed2Formatter">SINR均值</th>
					    						<th data-options="field:'sinrMin',width:50,align:'center',formatter:numToFixed2Formatter">SINR最低值</th>
					    						<th data-options="field:'overlapCoverRatio',width:70,align:'center',formatter:numMultiply100ToFixed2Formatter">重叠覆盖占比(%)</th>
					    						<th data-options="field:'distanceAvg',width:60,align:'center',formatter:numToFixed2Formatter">持续距离(m)</th>
					    						<th data-options="field:'testTime',width:60,align:'center',formatter:numDivide1000Formatter">测试时间(s)</th>
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
					    						<td></td>
					    					</tr>
					    				</tbody>
					    			</table>
								</div>
								<div data-options="region:'south',border:false" style="height:50%;padding:2px 0px 0px 0px;">
									<table id="disturbAutoTable" class="easyui-datagrid" data-options="title:'优化建议:三超小区详情',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'itemid0',width:80,align:'center'">小区友好名</th>
					    						<th data-options="field:'itemid1',width:80,align:'center'">小区CELLID</th>
					    						<th data-options="field:'itemid2',width:100,align:'center'">和质差部分路段<br>距离(m)</th>
					    						<th data-options="field:'itemid3',width:60,align:'center'">SINR均值<br>(dBm)</th>
					    						<th data-options="field:'itemid4',width:80,align:'center'">三超类型</th>
					    					
					    					</tr>
					    				</thead>
					    				<tbody>
				    						<tr>
					    						<td>未选择质差路段</td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    						
					    					</tr>
					    				</tbody>
					    			</table>
								</div>
								<!--  <div data-options="region:'south',border:false" style="height:50%;padding:2px 0px 0px 0px;">
									<table id="disturbPCITable" class="easyui-datagrid" data-options="title:'优化建议:PCI调整详情表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'itemid0',width:80,align:'center'">小区友好名</th>
					    						<th data-options="field:'itemid1',width:80,align:'center'">小区CELLID</th>
					    						<th data-options="field:'itemid2',width:80,align:'center'">原PCI</th>
					    						<th data-options="field:'itemid3',width:80,align:'center'">和干扰路段<br>距离(m)</th>
					    					</tr>
					    				</thead>
					    				<tbody>
				    						<tr>
					    						<td>未选择质差路段</td>
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
					    				</tbody>
					    			</table>
								</div>
								<div data-options="region:'south',border:false" style="height:50%;padding:2px 0px 0px 0px;">
									<table id="disturbGodTable" class="easyui-datagrid" data-options="title:'优化建议:天馈调整详情表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'itemid1',width:80,align:'center'">小区<br>友好名</th>
					    						<th data-options="field:'itemid2',width:50,align:'center'">小区<br>CELLID</th>
					    						<th data-options="field:'itemid3',width:80,align:'center'">和弱覆盖路段<br>距离(m)</th>
					    						<th data-options="field:'itemid4',width:80,align:'center'">干扰类型</th>
					    						<th data-options="field:'itemid5',width:50,align:'center'">RSRP均值<br>(dBm)</th>
					    						<th data-options="field:'itemid6',width:50,align:'center'">工参<br>下倾角</th>
					    						<th data-options="field:'itemid7',width:50,align:'center'">工参<br>方位角</th>
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
					    						<td></td>
					    					</tr>
					    				</tbody>
					    			</table>
								</div>-->
							</div>
						</div>
					</div>
				</div>
		    </div>
    	</div>
  </body>
</html>
