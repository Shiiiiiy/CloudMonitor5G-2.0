<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>参数错误质差路段分析</title>

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
	//获取及回填道路名称
	var mainTableId = 'paramErrorRoadTable';
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
	
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var paramErrorRow = $("#paramErrorRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!paramErrorRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		testLogItemIds = paramErrorRow.recSeqNo;
		volteqbrid = paramErrorRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/voiceQBR/doParamErrorRoadAnalysis.action",{roadId:paramErrorRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					var paramErrorRoadName = paramErrorRow.m_stRoadName;
					//小区详情表
					$("#paramErrorCellInfoTable").datagrid('loadData',[]);
					$("#paramErrorCellInfoTable").datagrid({
						title:paramErrorRoadName+"问题路段优化建议列表",
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
	    				columns:[[
   							{colspan:2,title:'参数问题服务小区'},
   							{colspan:4,title:'问题参数'},
   						],[
							{field:'cellName',width:100,align:'center',title:'友好名',formatter:showTooltip},
							{field:'cellId',width:50,align:'center',title:'CELLID',sortable:true},
							{field:'paramName',width:80,align:'center',title:'参数名',formatter:showTooltip},
							{field:'signallingName',width:40,align:'center',title:'所处位置',formatter:showTooltip},
							{field:'originalValue',width:40,align:'center',title:'原始值',sortable:true},
							{field:'templateValue',width:40,align:'center',title:'参数模板值',sortable:true}
						]]
					});
					$("#paramErrorCellInfoTable").datagrid('loadData',result.paramErrorOptimizeAdvice);
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:paramErrorRow.id},
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
	<c:if test="${null == paramErrorRoads || empty paramErrorRoads}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == paramErrorRoads || empty paramErrorRoads}">
			$("#paramErrorRoadTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:8
			});
		</c:if>
		$("#paramErrorCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			colspan:6
		});
	});
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=4" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 质差问题路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:50%;">
  					<table id="paramErrorRoadTable" class="easyui-datagrid" data-options="title:'参数问题路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
	    						<th rowspan="2" data-options="field:'boxid',width:60,align:'center',formatter:showTooltip,sortable:isRoadSortable">BOXID</th>
	    						<th colspan="4" >语音质差路段信息</th>
	    						<th colspan="10" >路段指标描述</th>
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
	    					<c:forEach items="${paramErrorRoads}" var="paramErrorRoad">
		    					<tr>
		    						<td>${paramErrorRoad.id}</td>
		    						<td>${paramErrorRoad.testLogItem.boxId}</td>
		    						<td>${paramErrorRoad.m_stRoadName}</td>
		    						<td>${paramErrorRoad.m_dbDistance}</td>
		    						<td>${paramErrorRoad.m_dbContinueTime}</td>
		    						<td>${paramErrorRoad.firstMosTime}</td>
		    						<td>${paramErrorRoad.mosAvg}</td>
		    						<td>${paramErrorRoad.mosType}</td>
		    						<td>${paramErrorRoad.testLogItem.fileName}</td>
		    						<td>${paramErrorRoad.testLogItem.recSeqNo}</td>
		    						<td>${paramErrorRoad.beginLatitude}</td>
		    						<td>${paramErrorRoad.courseLatitude}</td>
		    						<td>${paramErrorRoad.endLatitude}</td>
		    						<td>${paramErrorRoad.beginLongitude}</td>
		    						<td>${paramErrorRoad.courseLongitude}</td>
		    						<td>${paramErrorRoad.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == paramErrorRoads}">
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
	    					<c:if test="${null != paramErrorRoads && empty paramErrorRoads}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有参数错误质差路段</td>
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
  					<table id="paramErrorCellInfoTable" class="easyui-datagrid" data-options="title:'优化建议列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
	    				<thead>
	    					<tr>
	    						<th colspan="2" >参数问题服务小区</td>
	    						<th colspan="4" >问题参数</td>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'cellName',width:100,align:'center',formatter:showTooltip">友好名</td>
	    						<th data-options="field:'cellId',width:50,align:'center'">CELLID</td>
	    						<th data-options="field:'paramName',width:80,align:'center',formatter:showTooltip">参数名</td>
	    						<th data-options="field:'signallingName',width:40,align:'center',formatter:showTooltip">所处位置</td>
	    						<th data-options="field:'originalValue',width:40,align:'center'">原始值</td>
	    						<th data-options="field:'templateValue',width:40,align:'center'">参数模板值</td>
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
  </body>
</html>
