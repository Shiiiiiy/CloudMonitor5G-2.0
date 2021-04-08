<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLte异常事件----SRVCC切换失败问题分析</title>

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
	
	var mainTableId = 'droppingSRVCCsTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>droppingSRVCC/roadNameCallBack.action";
	}
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//存储质差路段id
	var volteHOFid;
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
	//gisCommon会调用获取切换失败ID
	function getGpsPointHofId(){
		return volteHOFid;
	}
	//gisCommon会调用获取切换失败事件轨迹的url
	function getHOFGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryDroppingIndexGpsPoint.action";
	}
	//gisCommon会调用获取切换失败事件图标轨迹的url
	function getHOFEventGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryDroppingEventGpsPoint.action";
	}
	//gisCommon会调用获取切换失败事件轨迹行驶方向的url
	function getHOFDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryDroppingDirection.action";
	}
	//gisCommon会调用获取切换失败事件源小区与目标小区连线的url
	function getHOFCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryDroppingCellToCell.action";
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
		var lastVolteEERid;//上一次异常事件id
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var droppingSRVCCs = $("#droppingSRVCCsTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!droppingSRVCCs){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个失败事件!",'warning');
			return ;
		}
		testLogItemIds = droppingSRVCCs.recSeqNo;
		volteHOFid = droppingSRVCCs.id;
		if(!volteHOFid){
			$.messager.alert("提示","请勾选某个失败事件!",'warning');
			return ;
		}
		//显示服务小区和邻区测量详情
		$("#areaInfoDlg").dialog('open');
		$.post("${pageContext.request.contextPath}/droppingSRVCC/haveTestLogMeasure.action",{droppingId:droppingSRVCCs.id},
				function(result){
		$("#callAndOtherCall").datagrid('loadData',result);
								}
		,"json");
		var roadName = droppingSRVCCs.m_stRoadName;
		//本次和上一次的失败事件id不同
		if(lastVolteEERid!=volteHOFid){
			$("#srvccSignallingTable").datagrid('loadData',[]);
			$("#srvccSignallingTable").datagrid({
				// 表头
				columns:signallingTableColumns,
				url:'${pageContext.request.contextPath}/droppingSRVCC/doPageListJson.action?isOpposite=false&droppingId='+volteHOFid,
				title:roadName+'SRVCC切换失败事件信令列表',
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
			$('#srvccSignallingTable').datagrid('getPanel').addClass("lines-no");
		}
		lastVolteEERid = volteHOFid;
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:droppingSRVCCs.id},
			function(result){
				//如果上次为null或者undefined,或者当前图层与上一次图层不一样
				if(!drawCellFileName||result!=drawCellFileName){
					mapIframe.window.drawCell();
				}
				drawCellFileName = result;
			}
		,"json");
		mapIframe.window.drawHOF();
		
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	
	//质差路段字段排序开关
	var isRoadSortable = true ;
	<c:if test="${null == droppingSRVCCs || empty droppingSRVCCs}">
		isRoadSortable = false;
	</c:if>
		$(function(){
		<c:if test="${null == droppingSRVCCs || empty droppingSRVCCs}">
			$("#droppingSRVCCsTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:6
			});
		</c:if>
		
		$("#srvccSignallingTable").datagrid('mergeCells',{
			index: 0,
			field: 'time',
			colspan:6
		});
	});
	
	</script>
  </head>
  <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" style="width: 100%;height: 100%;" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=11" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 质差问题路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:45%;">
  					<table id="droppingSRVCCsTable" class="easyui-datagrid" data-options="title:'SRVCC切换失败问题列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'boxid',width:80,align:'center',sortable:isRoadSortable">BOXID</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center',formatter:showTooltip">道路名称</th>
	    						<th data-options="field:'failDateTime',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">发生时间</th>
	    						<th data-options="field:'cellName',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">源小区</th>
	    						<th data-options="field:'failName',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">目标小区</th>
	    						<th data-options="field:'fileName',width:100,align:'center',formatter:showTooltip">日志名称</th>
	    						<!-- 记得加上经纬度,用于获取路段名称,记得加上日志id用于后台获取信令和指标 -->
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
	    					<c:forEach items="${droppingSRVCCs}" var="droppingSRVCC">
		    					<tr>
		    						<td>${droppingSRVCC.id}</td>
		    						<td>${droppingSRVCC.testLogItem.boxId}</td>
		    						<td>${droppingSRVCC.m_stRoadName}</td>
		    						<td>${droppingSRVCC.failDateTime}</td>
		    						<td>${droppingSRVCC.cellName}</td>
		    						<td>${droppingSRVCC.failName}</td>
		    						<td>${droppingSRVCC.testLogItem.fileName}</td>
		    						<td>${droppingSRVCC.testLogItem.recSeqNo}</td>
		    						<td>${droppingSRVCC.beginLatitude}</td>
		    						<td>${droppingSRVCC.courseLatitude}</td>
		    						<td>${droppingSRVCC.endLatitude}</td>
		    						<td>${droppingSRVCC.beginLongitude}</td>
		    						<td>${droppingSRVCC.courseLongitude}</td>
		    						<td>${droppingSRVCC.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == droppingSRVCCs}">
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
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != droppingSRVCCs && empty droppingSRVCCs}">
	    						<tr>
	    							<td></td>
		    						<td>该测试日志下没有SRVCC切换失败事件</td>
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
						<a href="#" style="width:50px;text-decoration:underline;" onclick="doAnalysis();" title="失败路段分析">路段分析</a>
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<!-- 切换失败路段优化建议汇总 -->
		    	
				<div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
					<table id="srvccSignallingTable" class="easyui-datagrid" data-options="title:'信令列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true" >
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
	    						<td>未选择SRVCC切换失败事件</td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    					</tr>
	    				</tbody>
	    			</table>
				</div>
				<div id="areaInfoDlg" class="easyui-dialog" style="width:70%;height:50%" data-options="title:'服务小区和邻区测量详情',closed:true,modal:true">
				<table id="callAndOtherCall"class="easyui-datagrid" style="width:100%;height:100%" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true">
					<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'time',width:180,align:'center',formatter:showTooltip">Time</th>
	    						<th colspan="4" >SCELL	</th>
	    						<th colspan="3" >NCELL1	</th>
	    						<th colspan="3" >NCELL2	</th>
	    						<th colspan="3" >NCELL3	</th>
	    						<th colspan="3" >NCELL4	</th>
	    						<th colspan="3" >NCELL5	</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'scellPci',width:40,align:'center',formatter:showTooltip">PCI</th>
	    						<th data-options="field:'scellEarfcn',width:40,align:'center',formatter:showTooltip">EARFCN</th>
	    						<th data-options="field:'scellRsrp',width:40,align:'center',formatter:showTooltip">RSRP</th>
	    						<th data-options="field:'scellSinr',width:40,align:'center',formatter:showTooltip">STNR</th>
	    						<th data-options="field:'ncell1Pci',width:40,align:'center',formatter:showTooltip">PCI</th>
	    						<th data-options="field:'ncell1Earfcn',width:40,align:'center',formatter:showTooltip">EARFCN</th>
	    						<th data-options="field:'ncell1Rsrp',width:40,align:'center',formatter:showTooltip">RSRP</th>
	    						<th data-options="field:'ncell2Pci',width:40,align:'center',formatter:showTooltip">PCI</th>
	    						<th data-options="field:'ncell2Earfcn',width:40,align:'center',formatter:showTooltip">EARFCN</th>
	    						<th data-options="field:'ncell2Rsrp',width:40,align:'center',formatter:showTooltip">RSRP</th>
	    						<th data-options="field:'ncell3Pci',width:40,align:'center',formatter:showTooltip">PCI</th>
	    						<th data-options="field:'ncell3Earfcn',width:40,align:'center',formatter:showTooltip">EARFCN</th>
	    						<th data-options="field:'ncell3Rsrp',width:40,align:'center',formatter:showTooltip">RSRP</th>
	    						<th data-options="field:'ncell4Pci',width:40,align:'center',formatter:showTooltip">PCI</th>
	    						<th data-options="field:'ncell4Earfcn',width:40,align:'center',formatter:showTooltip">EARFCN</th>
	    						<th data-options="field:'ncell4Rsrp',width:40,align:'center',formatter:showTooltip">RSRP</th>
	    						<th data-options="field:'ncell5Pci',width:40,align:'center',formatter:showTooltip">PCI</th>
	    						<th data-options="field:'ncell5Earfcn',width:40,align:'center',formatter:showTooltip">EARFCN</th>
	    						<th data-options="field:'ncell5Rsrp',width:40,align:'center',formatter:showTooltip">RSRP</th>
	    					</tr>
	    				</thead>
				</table>
			
   		</div>
		    </div>
    	</div>
	</body>
</html>
