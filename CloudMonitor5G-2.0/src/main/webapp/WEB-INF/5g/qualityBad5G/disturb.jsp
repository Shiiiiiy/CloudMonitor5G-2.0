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
		return "<%=basePath%>qualityBadRoad5g/roadNameCallBack.action";
	}
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//存储质差路段id
	var volteqbrid;
	//小区图层名称
	var drawCellFileName;
	//gisCommon会调用获取测试日志ID
	function getCellTestLogItemIds(){
		if(testLogItemIds == null || testLogItemIds == ''){
			$.messager.alert("提示","请选择分析日志!",'warning');
		}
		return testLogItemIds;
	}
	//gisCommon会调用获取生成小区SQL的url
	function getCellActionUrl(){
		return "<%=basePath%>gisSql/queryCellInfo.action";
	}
	//gisCommon会调用获取质差路段ID
	function getGpsPointQBRId(){
		return volteqbrid;
	}
	//gisCommon会调用获取点与小区连线
	function getQBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryQBRIndexGpsPoint5g.action";
	}
	//gisCommon会调用获取生成质差路段轨迹行驶方向的url
	function getQBRDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryQBRDirection5g.action";
	}
	//gisCommon会调用获取小区与邻区连线的url
	function QBRCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryQBRCellToCell5g.action";
	}
	//gisCommon会调用获取质差路段ID
	function getGpsPointQBRId(){
		return volteqbrid;
	}
	//gisCommon会调用获取测试日志ID
	function getTestLogItemIds2QBR(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
	function getTLI2QBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryQualityBadRoadsPoints.action?coverType=1";
	}
	
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var disturbRow = $("#disturbRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!disturbRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		testLogItemIds = disturbRow.recSeqNo;
		volteqbrid = disturbRow.rid;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/qualityBadRoad5g/doDisturbRoadAnalysis.action",{roadId:disturbRow.rid},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					//小区详情表
					$("#disturbCellInfoTable").datagrid('loadData',[]);
					$("#disturbCellInfoTable").datagrid({
						title:"问题路段服务小区详情",
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
						columns:[[
							{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 
							{field:'cellId',width:60,align:'center',title:'小区CELLID',sortable:true},
							{field:'rsrpAvg',width:60,align:'center',title:'RSRP(dBm)',formatter:numToFixed2Formatter,sortable:true},
							{field:'rsrqAvg',width:60,align:'center',title:'RSRQ(dBm)',formatter:numToFixed2Formatter,sortable:true},
							{field:'sinrAvg',width:50,align:'center',title:'SINR',formatter:numToFixed2Formatter,sortable:true},
							{field:'beamConfig',width:50,align:'center',title:'波束配置',formatter:formatBeamInfo,sortable:true},
							{field:'highCoverRatio',width:80,align:'center',title:'过覆盖占比(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true},
							{field:'overlayCoverRatio',width:80,align:'center',title:'重叠覆盖占比(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true},
							{field:'continuedDistance',width:60,align:'center',title:'持续距离(m)',formatter:numToFixed2Formatter,sortable:true},
							{field:'testDuration',width:60,align:'center',title:'测试时间(s)',formatter:numDivide1000Formatter,sortable:true}
						]]
					});
					$("#disturbCellInfoTable").datagrid('loadData',result.disturbRoadCellInfo);
					
					//优化建议
					$("#disturbAutoTable").datagrid('loadData',[]);
					if(disturbRow.optimizeSuggest){
						//三超站点，建议整改
						if(0==disturbRow.optimizeSuggest){
							$("#disturbAutoTable").datagrid({
								title:'优化建议:三超小区详情',
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
									{field:'sampleRsrpRatio',width:50,align:'center',title:'相关采样点<br>RSRP均值',formatter:numToFixed2Formatter,sortable:true},
									{field:'sampleSinrRatio',width:50,align:'center',title:'相关采样点<br>SINR均值',formatter:numToFixed2Formatter,sortable:true},
									{field:'sanchaoType',width:70,align:'center',title:'三超类型',sortable:true
										,formatter: function(value,row,index){
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
								]]
							});
							$("#disturbAutoTable").datagrid('loadData',result.disturbRoadSanChaoCellInfo);
						//PCI调整
						}else if(1==disturbRow.optimizeSuggest){
							$("#disturbAutoTable").datagrid({
								title:'优化建议:建议天馈调整——过覆盖',
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									{field:'adjustPriority',width:40,align:'center',title:'调整优先级',formatter:showAdjustRank,sortable:true,order:'asc'}, 			
									{field:'cellName',width:40,align:'center',title:'小区友好名',formatter:showTooltip}, 				
									{field:'cellId',width:40,align:'center',title:'小区CELLID',sortable:true},
									{field:'distanceAvg',width:80,align:'center',title:'和干扰路段的距离(m)',formatter:numToFixed2Formatter,sortable:true},
									{field:'sampleRsrpMeat',width:80,align:'center',title:'相关采样点RSRP均值',formatter:numToFixed2Formatter,sortable:true},
									{field:'sampleSinrMeat',width:80,align:'center',title:'相关采样点SINR均值',formatter:numToFixed2Formatter,sortable:true},
									{field:'paramDowntilt',width:80,align:'center',title:'工参下倾角',sortable:true,order:'desc'},
									{field:'paramAzimuth',width:80,align:'center',title:'工参方位角',sortable:true}
								]]
							});
							$("#disturbAutoTable").datagrid('loadData',result.disturbRoadHighCoverCellInfo);
						//天馈调整
						}else if(2==disturbRow.optimizeSuggest){
							$("#disturbAutoTable").datagrid({
								title:'优化建议:建议天馈调整——重叠覆盖',
								fit:true,
								scrollbarSize:0,
								striped:true,
								fitColumns:true,
								remoteSort:false,
								multiSort:true,
								columns:[[
									{field:'adjustPriority',width:50,align:'center',title:'调整<br>优先级',formatter:showAdjustRank,sortable:true,order:'asc'},
									{field:'cellName',width:50,align:'center',title:'小区<br>友好名',formatter:showTooltip}, 				
									{field:'cellId',width:50,align:'center',title:'小区<br>CELLID',sortable:true},
									{field:'distanceAvg',width:80,align:'center',title:'和干扰路<br>段距离(m)',formatter:numToFixed2Formatter,sortable:true},
									{field:'overlayCoverSampleRatio',width:50,align:'center',title:'重叠覆盖采<br>样点占比(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true},
									{field:'sampleRsrpMeat',width:80,align:'center',title:'相关采样点<br>RSRP均值',formatter:numToFixed2Formatter,sortable:true},
									{field:'sampleSinrMeat',width:80,align:'center',title:'相关采样点<br>SINR均值',formatter:numToFixed2Formatter,sortable:true},
									{field:'paramDowntilt',width:50,align:'center',title:'工参<br>下倾角',sortable:true,order:'desc'},
									{field:'paramAzimuth',width:50,align:'center',title:'工参<br>方位角',sortable:true}
								]]
							});
							$("#disturbAutoTable").datagrid('loadData',result.disturbRoadOverCoverCellInfo);
						}
					}
				}
			}
		,"json");
		/* $.post("${pageContext.request.contextPath}/qualityBadRoad5g/checkDrawCellFileName.action",{roadId:disturbRow.rid},
			function(result){
				//如果上次为null或者undefined,或者当前图层与上一次图层不一样
				if(!drawCellFileName||result!=drawCellFileName){
					mapIframe.window.drawCell();
				}
				drawCellFileName = result;
			}
		,"json"); */
		// mapIframe.window.drawQBR();
		mapIframe.window.drawQuality5g(71);
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}

	/* 波数详情  */
	function formatBeamInfo(value,row,index){
		return "<a onclick='showBeamInfo(this,"+row.id+")' href='javascript:void(0)'>"+value+"</a>";
	}
	function showBeamInfo(that,cellInfoId){
		$('#beamInfoWindow').dialog('open');
		$("#beamInfoTable").datagrid({
			title:null,fit:true,scrollbarSize:0,striped:true,fitColumns:true,
			remoteSort:false,multiSort:true,border:false,
			columns:[[
				{field:'beamNo',width:50,align:'center',title:'波束编号',sortable:true},
				{field:'rsrp',width:50,align:'center',title:'RSRP(dBm)',sortable:true},
				{field:'rsrq',width:90,align:'center',title:'RSRQ(dBm)',sortable:true},
				{field:'sinr',width:80,align:'center',title:'SINR',sortable:true},
				{field:'h_fPowerAngle',width:80,align:'center',title:'水平半功率角',sortable:true},
				{field:'v_fPowerAngle',width:80,align:'center',title:'垂直半功率角',sortable:true}
			]],
			url:'${pageContext.request.contextPath}/qualityBadRoad5g/doEmbbCoverCellAnalysis.action?cellInfoId='+cellInfoId
		});
		$("#beamInfoTable").datagrid('resize');
	}
	
	// 调整优先级
	function showAdjustRank(value,row,index){
		if(value){
			if(1==value){
				return '高';
			}
			if(2==value){
				return '中';
			}
			if(3==value){
				return '低';
			}
		}
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
				field: 'roadName',
				colspan:13
			});
		</c:if>
		$("#disturbCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:10
		});
		$("#disturbAutoTable").datagrid('mergeCells',{
			index: 0,
			field: 'itemid0',
			colspan:6
		});
	});
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=70" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 质差问题路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:50%;">
  					<table id="disturbRoadTable" class="easyui-datagrid" data-options="title:'干扰路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'rid',checkbox:true"></th>
	    						<!-- <th rowspan="2" data-options="field:'id2',width:60,align:'center',formatter:showTooltip">序号</th> -->
	    						<th colspan="4" >干扰路段信息</th>
	    						<th colspan="9" >干扰路段指标</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'roadName',width:80,align:'center',formatter:showTooltip,sortable:isRoadSortable">道路名称</th>
	    						<th data-options="field:'startTimeString',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">开始时间<br>(s)</th>
	    						<th data-options="field:'testDuration',width:80,align:'center',formatter:numDivide1000Formatter,sortable:isRoadSortable">测试时长<br>(s)</th>
	    						<th data-options="field:'continuedDistance',width:80,align:'center',formatter:showTooltip,sortable:isRoadSortable">持续距离<br>(m)</th>
	    						<th data-options="field:'rsrpMean',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">RSRP均值<br>(dBm)</th>
	    						<th data-options="field:'rsrpLow',width:90,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">RSRP最低<br>值(dBm)</th>
	    						<th data-options="field:'sinrMean',width:90,align:'center'">SINR均值</th>
	    						<th data-options="field:'sinrLow',width:90,align:'center'">SINR最低<br>值</th>
	    						<th data-options="field:'highCoverRatio',width:100,align:'center',formatter:numMultiply100ToFixed2Formatter,sortable:isRoadSortable">过覆盖占<br>比(%)</th>
	    						<th data-options="field:'overlayCoverRatio',width:100,align:'center',formatter:numMultiply100ToFixed2Formatter,sortable:isRoadSortable">重叠覆盖<br>占比(%)</th>
	    						<th data-options="field:'optimizeSuggest',width:80,align:'center',sortable:isRoadSortable,
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '<div title=三超站点，建议整改>三超站点整改</div>';
											}else if(1==value){
												return '建议天馈调整——过覆盖';
											}else if(2==value){
												return '建议天馈调整——重叠覆盖';
											}
										}
										return value;
									}
	    						">优化建议</th>
	    						<th data-options="field:'logName',width:120,align:'center',formatter:showTooltip">测试日志名称</th>
	    						<!-- <th data-options="field:'rid',width:120,align:'center',hidden:true,formatter:showTooltip">rid</th> -->
	    						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true,formatter:showTooltip">recSeqNo</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${disturbRoads}" var="disturbRoad">
		    					<tr>
		    						<td>${disturbRoad.rid}</td>
		    						<%-- <td>${disturbRoad.id}</td> --%>
		    						<td>${disturbRoad.roadName}</td>
		    						<td>${disturbRoad.startTimeString}</td>
		    						<td>${disturbRoad.testDuration}</td>
		    						<td>${disturbRoad.continuedDistance}</td>
		    						<td>${disturbRoad.rsrpMean}</td>
		    						<td>${disturbRoad.rsrpLow}</td>
		    						<td>${disturbRoad.sinrMean}</td>
		    						<td>${disturbRoad.sinrLow}</td>
		    						<td>${disturbRoad.highCoverRatio}</td>
		    						<td>${disturbRoad.overlayCoverRatio}</td>
		    						<td>${disturbRoad.optimizeSuggest}</td>
		    						<td>${disturbRoad.testLogItem.fileName}</td>
		    						<%-- <td>${disturbRoad.rid}</td> --%>
		    						<td>${disturbRoad.testLogItem.recSeqNo}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == disturbRoads}">
	    						<tr>
		    						<td></td>
		    						<td>未选择测试日志</td>
		    						<!-- <td></td> -->
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
		    						<!-- <td></td> -->
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
								<div data-options="region:'center',border:false" style="height:60%;padding:2px 0px 0px 0px;">
									<table id="disturbCellInfoTable" class="easyui-datagrid" data-options="title:'小区指标列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'cellName',width:80,align:'center',formatter:showTooltip">小区友好名</th>
					    						<th data-options="field:'cellId',width:60,align:'center'">小区CELLID</th>
					    						<th data-options="field:'rsrpAvg',width:60,align:'center',formatter:numToFixed2Formatter">RSRP(dBm)</th>
					    						<th data-options="field:'rsrqAvg',width:60,align:'center',formatter:numToFixed2Formatter">RSRQ(dBm)</th>
					    						<th data-options="field:'sinrAvg',width:50,align:'center',formatter:numToFixed2Formatter">SINR</th>
					    						<th data-options="field:'beamConfig',width:50,align:'center',formatter:numToFixed2Formatter">波束配置</th>
					    						<th data-options="field:'highCoverRatio',width:80,align:'center',formatter:numMultiply100ToFixed2Formatter">过覆盖占比(%)</th>
					    						<th data-options="field:'overlayCoverRatio',width:80,align:'center',formatter:numMultiply100ToFixed2Formatter">重叠覆盖占比(%)</th>
					    						<th data-options="field:'continuedDistance',width:60,align:'center',formatter:numToFixed2Formatter">持续距离(m)</th>
					    						<th data-options="field:'testDuration',width:60,align:'center',formatter:numDivide1000Formatter">测试时间(s)</th>
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
					    						<td></td>
					    						<td></td>
					    						<td></td>
					    					</tr>
					    				</tbody>
					    			</table>
								</div>
								<div data-options="region:'south',border:false" style="height:40%;padding:2px 0px 0px 0px;">
									<table id="disturbAutoTable" class="easyui-datagrid" data-options="title:'优化建议:三超站点整改',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'itemid0',width:80,align:'center'">小区友好名</th>
					    						<th data-options="field:'itemid1',width:80,align:'center'">小区CELLID</th>
					    						<th data-options="field:'itemid2',width:80,align:'center'">和干扰路段<br>距离(m)</th>
					    						<th data-options="field:'itemid3',width:60,align:'center'">相关采样点<br>RSRP均值</th>
					    						<th data-options="field:'itemid5',width:60,align:'center'">相关采样点<br>SINR均值</th>
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
  		<div id="beamInfoWindow" class="easyui-dialog" data-options="width:500,height:200,title:'波束详情',closed:true"><table id="beamInfoTable"></table></div>
  </body>
</html>
