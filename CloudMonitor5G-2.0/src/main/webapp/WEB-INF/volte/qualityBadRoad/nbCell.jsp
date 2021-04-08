<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>邻区配置问题路段分析</title>

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
	var mainTableId = 'nbCellRoadTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>voiceQBR/roadNameCallBack.action";
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
	function getGpsPointQBRId(){
		return volteqbrid;
	}
	//gisCommon会调用获取生成质差路段轨迹的url
	function getQBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryQBRIndexGpsPoint.action";
	}
	//gisCommon会调用获取生成质差路段轨迹行驶方向的url
	function getQBRDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryQBRDirection.action";
	}
	//gisCommon会调用获取小区与邻区连线的url
	function QBRCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryQBRCellToCell.action";
	}
	
	
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var nbCellRow = $("#nbCellRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!nbCellRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		testLogItemIds = nbCellRow.recSeqNo;
		volteqbrid = nbCellRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/voiceQBR/doNbCellRoadAnalysis.action",{roadId:nbCellRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					var nbCellRoadName = nbCellRow.m_stRoadName;
					//小区详情表
					$("#nbCellCellInfoTable").datagrid('loadData',[]);
					$("#nbCellCellInfoTable").datagrid({
						title:nbCellRoadName+"问题路段邻区缺失的服务小区切换性能",
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
						columns:[[
							{field:'cellName',width:90,align:'center',title:'服务小区<br>友好名',formatter:showTooltip}, 				
							{field:'cellId',width:60,align:'center',title:'服务小区<br>CELLID',sortable:true},
							{field:'co_cellName',width:90,align:'center',title:'切换目标小区<br>友好名',formatter:showTooltip},
							{field:'co_cellId',width:60,align:'center',title:'切换目标小区<br>CELLID',sortable:true},
							{field:'co_distance',width:40,align:'center',title:'距离(m)',formatter:numToFixed2Formatter,sortable:true},
							{field:'co_requestNum',width:40,align:'center',title:'切换尝试<br>次数',sortable:true},
							{field:'co_successNum',width:40,align:'center',title:'切换成功<br>次数',sortable:true}
						]]
					});
					$("#nbCellCellInfoTable").datagrid('loadData',result.nbCellCoPerf);
					
					//lte邻区添加建议
					$("#nbCellLTEAddAdviceCellInfoTable").datagrid('loadData',[]);
					$("#nbCellLTEAddAdviceCellInfoTable").datagrid({
						title:nbCellRoadName+'路段LTE邻区添加建议',
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
   						columns:[[
   							{colspan:2,title:'邻区缺失小区'},
   							{colspan:2,title:'建议添加邻区'},
							{field:'nb_rsrpAvg',rowspan:2,width:90,align:'center',title:'对该语音质差路<br>段覆盖强度(dBm)',sortable:true}, 				
							{field:'nb_distanceAvg',rowspan:2,width:70,align:'center',title:'和该语音质差<br>路段距离(m)',sortable:true}
						],[
							{field:'cellName',width:40,align:'center',title:'友好名',formatter:showTooltip},
							{field:'cellId',width:40,align:'center',title:'CELLID',formatter:showTooltip,sortable:true},
							{field:'nb_cellName',width:40,align:'center',title:'友好名',formatter:showTooltip},
							{field:'nb_cellId',width:40,align:'center',title:'CELLID',formatter:showTooltip,sortable:true}
						]]
					});
					$("#nbCellLTEAddAdviceCellInfoTable").datagrid('loadData',result.nbCellLTEAddAdvice);
					
					//gsm邻区添加建议
					$("#nbCellGSMAddAdviceCellInfoTable").datagrid('loadData',[]);
					$("#nbCellGSMAddAdviceCellInfoTable").datagrid({
						title:nbCellRoadName+'路段GSM邻区添加建议',
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
						columns:[[
   							{colspan:2,title:'服务小区'},
							{field:'addGSMFreqNum',rowspan:2,width:40,align:'center',title:'建议添加<br>邻区频点',sortable:true}
   						],[
							{field:'cellName',width:80,align:'center',title:'友好名',formatter:showTooltip},
							{field:'cellId',width:40,align:'center',title:'CELLID',formatter:showTooltip,sortable:true}
						]]
					});
					$("#nbCellGSMAddAdviceCellInfoTable").datagrid('loadData',result.nbCellGSMAddAdvice);
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:nbCellRow.id},
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
	<c:if test="${null == nbDeficiencyRoads || empty nbDeficiencyRoads}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == nbDeficiencyRoads || empty nbDeficiencyRoads}">
			$("#nbCellRoadTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:8
			});
		</c:if>
		$("#nbCellCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:7
		});
		$("#nbCellLTEAddAdviceCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:6
		});
		$("#nbCellGSMAddAdviceCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:3
		});
	});
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=3" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 质差问题路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:33%;">
	    			<table id="nbCellRoadTable" class="easyui-datagrid" data-options="title:'邻区缺失问题路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
	    						<th rowspan="2" data-options="field:'boxid',width:60,align:'center',sortable:isRoadSortable">BOXID</th>
	    						<th colspan="4" >语音质差路段信息	</th>
	    						<th colspan="10" >语音质差描述</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'m_stRoadName',width:120,align:'center',formatter:showTooltip">路段名称</th>
	    						<th data-options="field:'m_dbDistance',width:60,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">持续距离<br>(m)</th>
	    						<th data-options="field:'m_dbContinueTime',width:90,align:'center',formatter:numDivide1000Formatter,sortable:isRoadSortable">测试时间<br>(s)</th>
	    						<th data-options="field:'firstMosTime',width:80,align:'center',formatter:showTooltip,sortable:isRoadSortable">MOS采样<br>时间点</th>
	    						<th data-options="field:'mosAvg',width:60,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">MOS均值</th>
	    						<th data-options="field:'mosType',width:60,align:'center'">MOS<br>业务类型</th>
	    						<th data-options="field:'fileName',width:120,align:'center',formatter:showTooltip">测试日志名称</th>
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
	    					<c:forEach items="${nbDeficiencyRoads}" var="nbDeficiencyRoad">
		    					<tr>
		    						<td>${nbDeficiencyRoad.id}</td>
		    						<td>${nbDeficiencyRoad.testLogItem.boxId}</td>
		    						<td>${nbDeficiencyRoad.m_stRoadName}</td>
		    						<td>${nbDeficiencyRoad.m_dbDistance}</td>
		    						<td>${nbDeficiencyRoad.m_dbContinueTime}</td>
	    							<td>${nbDeficiencyRoad.firstMosTime}</td>
		    						<td>${nbDeficiencyRoad.mosAvg}</td>
		    						<td>${nbDeficiencyRoad.mosType}</td>
		    						<td>${nbDeficiencyRoad.testLogItem.fileName}</td>
		    						<td>${nbDeficiencyRoad.testLogItem.recSeqNo}</td>
		    						<td>${nbDeficiencyRoad.beginLatitude}</td>
		    						<td>${nbDeficiencyRoad.courseLatitude}</td>
		    						<td>${nbDeficiencyRoad.endLatitude}</td>
		    						<td>${nbDeficiencyRoad.beginLongitude}</td>
		    						<td>${nbDeficiencyRoad.courseLongitude}</td>
		    						<td>${nbDeficiencyRoad.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == nbDeficiencyRoads}">
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
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != nbDeficiencyRoads && empty nbDeficiencyRoads}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有邻区质差路段</td>
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
			    <div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
  					<table id="nbCellCellInfoTable" class="easyui-datagrid" data-options="title:'邻区缺失的服务小区切换性能',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'cellName',width:90,align:'center',formatter:showTooltip">服务小区<br>友好名</th>
	    						<th data-options="field:'cellId',width:60,align:'center'">服务小区<br>CELLID</th>
	    						<th data-options="field:'co_cellName',width:90,align:'center',formatter:showTooltip">切换目标小区<br>友好名</th>
	    						<th data-options="field:'co_cellId',width:60,align:'center'">切换目标小区<br>CELLID</th>
	    						<th data-options="field:'co_distance',width:40,align:'center',formatter:numToFixed2Formatter">距离(m)</th>
	    						<th data-options="field:'co_requestNum',width:40,align:'center'">切换尝试<br>次数</th>
	    						<th data-options="field:'co_successNum',width:40,align:'center'">切换成功<br>次数</th>
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
		    	<!-- 主被叫终端位于不同制式的MOS指标 -->
		    	<div data-options="region:'south',border:false" style="height:34%;padding:2px 0px 0px 0px;">
					<div class="easyui-layout" style="width:100%;height: 100%;" >
						<div data-options="region:'center',border:false" style="width:60%;">
				   			<table id="nbCellLTEAddAdviceCellInfoTable" class="easyui-datagrid" data-options="title:'LTE邻区添加建议',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
				   				<thead>
				   					<tr>
				   						<th colspan="2" >邻区缺失小区</th>
				   						<th colspan="2" >建议添加邻区</th>
				   						<th rowspan="2" data-options="field:'nb_rsrpAvg',width:90,align:'center'" >对该语音质差路<br>段覆盖强度(dBm)</th>
				   						<th rowspan="2" data-options="field:'nb_distanceAvg',width:70,align:'center'" >和该语音质差<br>路段距离(m)</th>
				   					</tr>
				   					<tr>
				   						<th data-options="field:'cellName',width:40,align:'center',formatter:showTooltip">友好名</th>
				   						<th data-options="field:'cellId',width:40,align:'center',formatter:showTooltip">CELLID</th>
				   						<th data-options="field:'nb_cellName',width:40,align:'center',formatter:showTooltip">友好名</th>
				   						<th data-options="field:'nb_cellId',width:40,align:'center',formatter:showTooltip">CELLID</th>
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
						<div data-options="region:'east',border:false" style="width:40%;padding:0px 0px 0px 2px ;">
							<table id="nbCellGSMAddAdviceCellInfoTable" class="easyui-datagrid" data-options="title:'GSM邻区添加建议',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
				   				<thead>
				   					<tr>
				   						<th colspan="2" >服务小区</th>
				   						<th rowspan="2" data-options="field:'addGSMFreqNum',width:40,align:'center'" >建议添加<br>邻区频点</th>
				   					</tr>
				   					<tr>
				   						<th data-options="field:'cellName',width:80,align:'center',formatter:showTooltip">友好名</th>
				   						<th data-options="field:'cellId',width:40,align:'center',formatter:showTooltip">CELLID</th>
				   					</tr>
				   				</thead>
				   				<tbody>
		    						<tr>
			    						<td>未选择质差路段</td>
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
  </body>
</html>
