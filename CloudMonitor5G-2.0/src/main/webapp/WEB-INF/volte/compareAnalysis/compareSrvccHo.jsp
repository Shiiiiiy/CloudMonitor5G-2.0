<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE专题----VoLTE对比分析----SRVCC切换失败对比</title>

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
	
	var mainTableId = 'srvccHosTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>droppingSRVCC/roadNameCallBack.action";
	}
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
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
	
	
	var testLogItemIds1 = '${testLogItemIds}';
	var compareTestLogItemIds = '${compareTestLogItemIds}';
	//存储质差路段id
	var volteHOFid;
	var hofType = 0 ;
	var cellId;
	var failId;
	
	//gisCommon会调用获取切换失败原始日志质差路段id
	function getHofTestLogItemIds(){
		return testLogItemIds1;
	}
	//gisCommon会调用获取切换失败对比日志质差路段id
	function getCompareHofTestLogItemIds(){
		return compareTestLogItemIds;
	}
	//gisCommon会调用获取切换失败类型
	function getCompareHofType(){
		return hofType;
	}
	//gisCommon会调用获取切换失败的原小区
	function getCompareHofCellId(){
		return cellId;
	}
	//gisCommon会调用获取切换失败的对比小区
	function getCompareHofFailId(){
		return failId;
	}
	//gisCommon会调用获取切换失败指标及事件图标轨迹的url
	function getCompareHOFGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCompareHof.action";
	}
	//gisCommon会调用获取切换失败源小区和目标小区渲染的url
	function getCompareHOFCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryCompareHofCellToCell.action";
	}
	
	
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var srvccHos = $("#srvccHosTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!srvccHos){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个失败事件!",'warning');
			return ;
		}
		testLogItemIds = srvccHos.recSeqNo;
		volteHOFid = srvccHos.id;
		cellId = srvccHos.cellId;
		failId = srvccHos.failId;
		if(!volteHOFid){
			$.messager.alert("提示","请勾选某个失败事件!",'warning');
			return ;
		}
		$.post("${pageContext.request.contextPath}/voiceQBR/checkDrawCellFileName.action",{roadId:srvccHos.id},
			function(result){
				//如果上次为null或者undefined,或者当前图层与上一次图层不一样
				if(!drawCellFileName||result!=drawCellFileName){
					mapIframe.window.drawCell();
				}
				drawCellFileName = result;
			}
		,"json");
		mapIframe.window.drawCompareHof();
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	
	//质差路段字段排序开关
	var isRoadSortable = false ;
	$(function(){
		<c:if test="${null == srvccHos || empty srvccHos}">
			$("#srvccHosTable").datagrid('mergeCells',{
				index: 0,
				field: 'failName',
				colspan:8
			});
		</c:if>
		initTable();
	});
	function initTable(){
	
		$.post("${pageContext.request.contextPath}/voiceCompare/doHOFAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#hofWholeTable").datagrid('loadData',result);
					$.post("${pageContext.request.contextPath}/voiceCompare/doHOFAnalysis.action",{"isCompare":true},
						function(result){
							if (result.errorMsg) {
								$.messager.alert("系统提示", result.errorMsg,'error');
							} else {
								$("#hofWholeTable").datagrid('appendRow',result.rows[0]);
							}
						}
					,"json");
				}
			}
		,"json");
	}
	/* 合并单元格 */
	function onSrvccSuccess(hofData){
		var rowLength=1;
		var lastFailId;
		for(var i=0; i<hofData.rows.length; i++){
			if(i==0){
				lastFailId=hofData.rows[i].failId;
			}else{
				//判定当前目标小区id和上一个目标小区id时候相同
				if(hofData.rows[i].failId==lastFailId){
					//相同
					rowLength=rowLength+1;
				}else{
					//不同
					$(this).datagrid('mergeCells',{
						index: i-rowLength,
						field: 'failName',
						rowspan: rowLength
					});
					$(this).datagrid('mergeCells',{
						index: i-rowLength,
						field: 'id',
						rowspan: rowLength
					});
					lastFailId=hofData.rows[i].failId;
					rowLength=1;
				}
			}
			if(i==(hofData.rows.length-1)){
				$(this).datagrid('mergeCells',{
					index: i-rowLength+1,
					field: 'failName',
					rowspan: rowLength
				});
				$(this).datagrid('mergeCells',{
					index: i-rowLength+1,
					field: 'id',
					rowspan: rowLength
				});
				lastFailId=undefined;
				rowLength=1;
			}
		}
	}
	</script>
  </head>
  <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" style="width: 100%;height: 100%;" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=24" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 质差问题路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:120px;">
  					<table id="hofWholeTable"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'指标列表',tools:'#tt'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'valueType',width:40,align:'center'"></th>
	    						<th data-options="field:'lteesrvccctrlpaneldelay',width:70,align:'center',formatter:numToFixed2Formatter">eSRVCC<br>控制面</th>
	    						<th data-options="field:'lteesrvccuserpaneldelay',width:70,align:'center',formatter:numToFixed2Formatter">eSRVCC<br>用户面</th>
	    						<th data-options="field:'ltereestabctrlpaneldelay',width:100,align:'center',formatter:numToFixed2Formatter">RLF/RE-ESTABLISH<br>控制面</th>
	    						<th data-options="field:'ltereestabuserpaneldelay',width:100,align:'center',formatter:numToFixed2Formatter">RLF/RE-ESTABLISH<br>用户面</th>
	    						<th data-options="field:'esrvccsuccessrate',width:70,align:'center',formatter:numToFixed2Formatter">eSRVCC<br>成功率</th>
	    						<th data-options="field:'ltereestabsucessrate',width:100,align:'center',formatter:numToFixed2Formatter">RLF/RE-ESTABLISH<br>成功率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    						<td></td>
	    					</tr>
	    					<tr>
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
	    			<div id="tt" >
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<!-- SRVCC切换对比-->
				<div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
					<table id="srvccHosTable" class="easyui-datagrid" data-options="title:'SRVCC切换失败问题列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:onSrvccSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'failName',width:80,align:'center',formatter:showTooltip">目标小区</th>
	    						<th data-options="field:'boxid',width:80,align:'center',formatter:showTooltip,sortable:isRoadSortable">BOXID</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center',formatter:showTooltip">道路名称</th>
	    						<th data-options="field:'failDateTime',width:80,align:'center',formatter:showTooltip">发生时间</th>
	    						<th data-options="field:'cellName',width:80,align:'center',formatter:showTooltip">源小区</th>
	    						<th data-options="field:'gsmFreq',width:80,align:'center',sortable:isRoadSortable">GSM小区<br>频点</th>
	    						<th data-options="field:'testLogType',width:80,align:'center',
	    							formatter:function(value,row,index){
										if (value==0){
											return '原始';
										}else if(value==1){
											return '对比';
										}
										return value;
									}
								">日志类型</th>
	    						<th data-options="field:'fileName',width:100,align:'center',formatter:showTooltip">日志名称</th>
	    						<!-- 记得加上经纬度,用于获取路段名称,记得加上日志id用于后台获取信令和指标 -->
	    						<th data-options="field:'recSeqNo',width:120,align:'center',hidden:true">recSeqNo</th>
	    						<th data-options="field:'beginLatitude',width:120,align:'center',hidden:true">开始纬度</th>
	    						<th data-options="field:'courseLatitude',width:120,align:'center',hidden:true">中间纬度</th>
	    						<th data-options="field:'endLatitude',width:120,align:'center',hidden:true">结束纬度</th>
	    						<th data-options="field:'beginLongitude',width:120,align:'center',hidden:true">开始经度</th>
	    						<th data-options="field:'courseLongitude',width:120,align:'center',hidden:true">中间经度</th>
	    						<th data-options="field:'endLongitude',width:120,align:'center',hidden:true">结束经度</th>
	    						<th data-options="field:'failId',width:120,align:'center',hidden:true">目标小区id</th>
	    						<th data-options="field:'cellId',width:120,align:'center',hidden:true">原小区id</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${srvccHos}" var="droppingSRVCC">
		    					<tr>
		    						<td>${droppingSRVCC.id}</td>
		    						<td>${droppingSRVCC.failName}</td>
		    						<td>${droppingSRVCC.testLogItem.boxId}</td>
		    						<td>${droppingSRVCC.m_stRoadName}</td>
		    						<td>${droppingSRVCC.failDateTime}</td>
		    						<td>${droppingSRVCC.cellName}</td>
		    						<td>${droppingSRVCC.gsmFreq}</td>
		    						<td>${droppingSRVCC.testLogType}</td>
		    						<td>${droppingSRVCC.testLogItem.fileName}</td>
		    						<td>${droppingSRVCC.testLogItem.recSeqNo}</td>
		    						<td>${droppingSRVCC.beginLatitude}</td>
		    						<td>${droppingSRVCC.courseLatitude}</td>
		    						<td>${droppingSRVCC.endLatitude}</td>
		    						<td>${droppingSRVCC.beginLongitude}</td>
		    						<td>${droppingSRVCC.courseLongitude}</td>
		    						<td>${droppingSRVCC.endLongitude}</td>
		    						<td>${droppingSRVCC.failId}</td>
		    						<td>${droppingSRVCC.cellId}</td>
		    					</tr>
	    					</c:forEach>
	    					<c:if test="${null == srvccHos}">
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
		    					</tr>
	    					</c:if>
	    					<c:if test="${null != srvccHos && empty srvccHos}">
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
					</div>
				</div>
		    </div>
    	</div>
	</body>
</html>
