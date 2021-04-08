<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>其他问题质差路段分析</title>

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
		var otherRow = $("#otherRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!otherRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		testLogItemIds = otherRow.recSeqNo;
		volteqbrid = otherRow.id;
		if(!volteqbrid){
			$.messager.alert("提示","请勾选某个质差路段!",'warning');
			return ;
		}
		
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/volteCWBR/doOtherRoadAnalysis.action",{roadId:otherRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					var otherRoadName = otherRow.m_stRoadName;
					//小区详情表
					$("#otherCellInfoTable").datagrid('loadData',[]);
					$("#otherCellInfoTable").datagrid({
						title:otherRoadName+"问题路段小区详情列表",
						scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,
						columns:[[
							{field:'cellName',width:90,align:'center',title:'小区友好名',formatter:showTooltip}, 				
							{field:'cellId',width:50,align:'center',title:'CELLID',sortable:true},
							{field:'rsrpAvg',width:70,align:'center',title:'RSRP均值(dBm)',formatter:numToFixed2Formatter,sortable:true},
							{field:'sinrAvg',width:40,align:'center',title:'SINR均值',formatter:numToFixed2Formatter,sortable:true}
						]]
					});
					$("#otherCellInfoTable").datagrid('loadData',result.otherRoadCellInfo);
				}
			}
		,"json");
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:otherRow.id},
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
		
	/* 隐藏信令panel */
	function hiddenSignallingPanel(){
		$("#bb").toggle();
		$("#aa").toggle();
	}
	
	/* 显示信令panel */
	var lastVolteQBRid;//上一次路段id
	function showSignallingPanel(){
		$("#bb").toggle();
		$("#aa").toggle();
		var signllingUrl = '';
		if(volteqbrid){
			signllingUrl = '${pageContext.request.contextPath}/volteCWBR/doPageListJson.action?roadId='+volteqbrid;
		}
		//没有上一次的路段id以及本次和上一次的路段id不同
		if(!lastVolteQBRid||lastVolteQBRid!=volteqbrid){
			$("#signllingPanel").datagrid({
				// 表头
				columns:[[
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
				]],
				url:signllingUrl,
				title:'信令流程',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				pagination:true,
				pageSize:200,
				pageList:[100,200,500,1000],
				scrollbarSize:0,
				onClickRow:signllingDecode
			});
			$('#signllingPanel').datagrid('getPanel').addClass("lines-no");
		}
		lastVolteQBRid = volteqbrid;
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
							$(document.body).prepend("<div id='signllingDiv"+rowData.id+"' style='overflow-y:auto;' >"+result+"</div>");
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
	
	//质差路段字段排序开关
	var isRoadSortable = true ;
	<c:if test="${null == otherRoads || empty otherRoads}">
		isRoadSortable = false;
	</c:if>
	
	$(function(){
		//页面加载完成隐藏信令panel
		$("#bb").toggle();
		
		<c:if test="${null == otherRoads || empty otherRoads}">
			$("#otherRoadTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:6
			});
		</c:if>
		$("#otherIndexTable").datagrid('mergeCells',{
			index: 0,
			field: 'mosAvg',
			colspan:9
		});
		$("#otherCellInfoTable").datagrid('mergeCells',{
			index: 0,
			field: 'cellName',
			/* colspan:6 */
			colspan:4
		});
	});
	
	</script>
  </head>
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
	<div id="aa" onclick="showSignallingPanel();" style="z-index:999;width:16px;height:60px;top:36%;right:0;position: absolute;border: 1px solid #95b8e7;background-color:#e7f0ff">
		<div style="margin-top:22px;width:16px;height:16px;background:url(${pageContext.request.contextPath}/js/easyui/themes/default/images/menu_arrows.png) no-repeat -48px 0px;"></div>
	</div>
	
	<div id="bb" style="z-index:888;width:600px;height:80%;top:0;right:0;position: absolute;">
		<div onclick="hiddenSignallingPanel();" style="z-index:999;width:16px;height:60px;top:45%;right:500px;position: absolute;border-top: 1px solid #95b8e7;border-left: 1px solid #95b8e7;border-bottom: 1px solid #95b8e7;background-color:#e7f0ff">
			<div style="margin-top:22px;width:16px;height:16px;background:url(${pageContext.request.contextPath}/js/easyui/themes/default/images/menu_arrows.png) no-repeat -32px 0px;"></div>
		</div>
		<div style="width:500px;height:100%;right:0;position: absolute;">
			<table id="signllingPanel"></table>
		</div>
	</div>
  <div id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=16" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 连续无线差路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:48%;">
  					<table id="otherRoadTable" class="easyui-datagrid" data-options="title:'其他原因质差路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
	    						<th rowspan="2" data-options="field:'boxid',width:60,align:'center',formatter:showTooltip,sortable:isRoadSortable">BOXID</th>
	    						<th colspan="4" >连续无线差路段信息	</th>
	    						<th colspan="10" >连续无线差描述</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'m_stRoadName',width:120,align:'center',formatter:showTooltip">路段名称</th>
	    						<th data-options="field:'m_dbDistance',width:60,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">持续距离<br>(m)</th>
	    						<th data-options="field:'m_dbContinueTime',width:90,align:'center',formatter:numDivide1000Formatter,sortable:isRoadSortable">测试时间<br>(s)</th>
	    						<th data-options="field:'startTime',width:80,align:'center',formatter:showTooltip,sortable:isRoadSortable">开始时间</th>
	    						<th data-options="field:'rsrpAvg',width:60,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">RSRP均值<br>(dBm)</th>
	    						<th data-options="field:'sinrAvg',width:60,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">SINR均值<br>(dBm)</th>
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
	    					<c:forEach items="${otherRoads}" var="otherRoad">
		    					<tr>
		    						<td>${otherRoad.id}</td>
		    						<td>${otherRoad.testLogItem.boxId}</td>
		    						<td>${otherRoad.m_stRoadName}</td>
		    						<td>${otherRoad.m_dbDistance}</td>
		    						<td>${otherRoad.m_dbContinueTime}</td>
		    						<td>${otherRoad.startTimeValue}</td>
		    						<td>${otherRoad.rsrpAvg}</td>
		    						<td>${otherRoad.sinrAvg}</td>
		    						<td>${otherRoad.testLogItem.fileName}</td>
		    						<td>${otherRoad.testLogItem.recSeqNo}</td>
		    						<td>${otherRoad.beginLatitude}</td>
		    						<td>${otherRoad.courseLatitude}</td>
		    						<td>${otherRoad.endLatitude}</td>
		    						<td>${otherRoad.beginLongitude}</td>
		    						<td>${otherRoad.courseLongitude}</td>
		    						<td>${otherRoad.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == otherRoads}">
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
	    					<c:if test="${null != otherRoads && empty otherRoads}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有其他质差路段</td>
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
		    	 <div data-options="region:'center',border:false" style="padding:4px 0px 0px 0px;" >
					<table id="otherCellInfoTable" class="easyui-datagrid" data-options="title:'小区详情列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
		   				<thead>
		   					<tr>
		   						<th data-options="field:'cellName',width:90,align:'center',formatter:showTooltip">小区友好名</th>
		   						<th data-options="field:'cellId',width:50,align:'center'">CELLID</th>
		   						<th data-options="field:'rsrpAvg',width:70,align:'center',formatter:numToFixed2Formatter">RSRP均值(dBm)</th>
		   						<th data-options="field:'sinrAvg',width:40,align:'center',formatter:numToFixed2Formatter">SINR均值</th>
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
		    </div>
    	</div>
  </div>
  </body>
</html>
