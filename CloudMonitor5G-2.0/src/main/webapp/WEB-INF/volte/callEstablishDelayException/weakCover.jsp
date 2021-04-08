<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>弱覆盖原因</title>

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
		return "<%=basePath%>callEstablish/roadNameCallBack.action";
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
	//gisCommon会调用获取连续无线差路段ID
	function getGpsPointCEDEId(){
		return volteqbrid;
	}
	//gisCommon会调用获取生成连续无线差路段轨迹的url
	function getCEDEGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCEDEIndexGpsPoint.action";
	}
	//gisCommon会调用获取生成连续无线差路段轨迹行驶方向的url
	function getCEDEDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryCEDEDirection.action";
	}
	//gisCommon会调用获取连续无线差事件图标轨迹的url
	function getCEDEEventGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryCEDEEventGpsPoint.action";
	}
	//gisCommon会调用获取事件图标类型
	function getIconType(){
		//呼叫建立时延异常图标类型(见数据库表定义中事件类型定义)
		return '21';
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
		$.post("${pageContext.request.contextPath}/callEstablish/doWeakCoverRoadAnalysis.action",{CEDEId:weakCoverRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					var weakCoverRoadName = weakCoverRow.m_stRoadName;
					$("#weakCoverIndexTable").datagrid('loadData',[]);
					$("#weakCoverIndexTable").datagrid('loadData',result.callEstablishDelayException);
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
									/* {field:'sampleRatio',width:120,align:'center',title:'采样点占整个质差路段<br>采样点比例(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true} */
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
								/* title:weakCoverRoadName+"路段"+'优化建议:天馈调整详情表', */
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
									/* {field:'adviceDowndipAngle',width:50,align:'center',title:'建议调整<br>下倾角',sortable:true} */
									/* {field:'sampleRatio',width:90,align:'center',title:'采样点占整个质差路段<br>采样点比例(%)',formatter:numMultiply100ToFixed2Formatter,sortable:true} */
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
	<c:if test="${null == weakCovers || empty weakCovers}">
		isRoadSortable = false;
	</c:if>
	$(function(){
		<c:if test="${null == weakCovers || empty weakCovers}">
			$("#weakCoverRoadTable").datagrid('mergeCells',{
				index: 0,
				field: 'boxid',
				colspan:10
			});
			
		</c:if>
		/*$("#weakCoverIndexTable").datagrid('mergeCells',{
			index: 0,
			field: 'callType',
			colspan:8
		});
		*/
		$("#weakCoverAutoTable").datagrid('mergeCells',{
			index: 0,
			field: 'itemid1',
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
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=18" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- 呼叫建立时延异常路段统计 -->
		    	<div data-options="region:'north',border:false" style="height:50%;">
  					<table id="weakCoverRoadTable" class="easyui-datagrid" data-options="title:'弱覆盖原因路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,tools:'#tt3',onLoadSuccess:mainTableLoadSuccess,remoteSort:false,multiSort:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
	    						<th rowspan="2" data-options="field:'boxid',width:80,align:'center',sortable:isRoadSortable">BOXID</th>
	    						<th colspan="4" >问题路段信息</th>
	    						<th colspan="11" >问题描述</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center',formatter:showTooltip">路段名称</th>
	    						<th data-options="field:'m_dbDistance',width:80,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">持续距离<br>(m)</th>
	    						<th data-options="field:'m_dbContinueTime',width:80,align:'center',formatter:numDivide1000Formatter,sortable:isRoadSortable">测试时间<br>(s)</th>
	    						<th data-options="field:'startTime2String',width:80,align:'center',formatter:showTooltip,sortable:isRoadSortable">起呼时间</th>
	    						<th data-options="field:'cellName',width:80,align:'center',formatter:showTooltip">服务小区</th>
	    						<th data-options="field:'rsrpAvg',width:70,align:'center',formatter:numToFixed2Formatter,sortable:isRoadSortable">RSRP均<br>值(dBm)</th>
	    						
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
	    					<c:forEach items="${weakCovers}" var="weakCoverRoad">
		    					<tr>
		    						<td>${weakCoverRoad.id}</td>
		    						<td>${weakCoverRoad.testLogItem.boxId}</td>
		    						<td>${weakCoverRoad.m_stRoadName}</td>
		    						<td>${weakCoverRoad.m_dbDistance}</td>
		    						<td>${weakCoverRoad.m_dbContinueTime}</td>
		    						<td>${weakCoverRoad.startTime2String}</td>
		    						<td>${weakCoverRoad.cellName}</td>
		    						<td>${weakCoverRoad.rsrpAvg}</td>
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
	    					<c:if test="${null == weakCovers}">
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
	    					<c:if test="${null != weakCovers && empty weakCovers}">
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
		    	<!--  VoLTE呼叫建立各阶段时延概览 -->
		    	<div data-options="region:'center',border:false" >
					<div class="easyui-layout" style="width:100%;height: 100%;">
						<div data-options="region:'north',border:false" style="height:115px;padding:2px 0px 0px 0px;">
							<table id="weakCoverIndexTable"   class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:' VoLTE呼叫建立各阶段时延概览'" >
			    				<thead>
						    					<tr>
						    						<th data-options="field:'callEstablishDelay',width:60,align:'center'">呼叫建立时延</th>
						    						<th data-options="field:'invite2rrcConnectionSeutpCompleteDelay',width:80,align:'center'">INVITE->RRC连接<br>建立完成时延</th>
						    						<th data-options="field:'rrcConnectionSeutpComplete2rrcConnectionReconfigurationDelay',width:90,align:'center'">RRC连接建立完成<br>->RRC连接重<br>配置时延</th>
						    						<th data-options="field:'invite1002calledPagingDelay',width:70,align:'center'">INVITE100-><br>被叫paging时延</th>
						    						<th data-options="field:'calledInvite2calledInvite183Delay',width:80,align:'center'">被叫INVITE->被叫<br>INVITE183时延</th>
						    						<th data-options="field:'callingInvite2callingInvite180RingingDelay',width:90,align:'center'">主叫INVITE183-><br>主叫INVITE180<br>Ringing时延</th>
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
							    					</tr>
							    				
						    </tbody>
			    			</table>
						</div>
								<div data-options="region:'south',border:false"  style="height:73%;padding:2px 0px 0px 0px;">
									<table id="weakCoverAutoTable" class="easyui-datagrid" data-options="title:'调整详情表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true" >
					    				 
					    				<thead>
					    					<tr>
					    						<th data-options="field:'itemid0',width:50,align:'center'">调整<br>优先级</th> 
					    						<th data-options="field:'itemid1',width:80,align:'center'">小区<br>友好名</th>
					    						<th data-options="field:'itemid2',width:50,align:'center'">小区<br>CELLID</th>
					    						<th data-options="field:'itemid3',width:80,align:'center'">和问题路段<br>距离(m)</th>
					    						<th data-options="field:'itemid4',width:50,align:'center',formatter:numToFixed2Formatter">RSRP均值<br>(dBm)</th>
					    						<th data-options="field:'itemid5',width:50,align:'center'">工参<br>下倾角</th>
					    						<th data-options="field:'itemid6',width:50,align:'center'">工参<br>方位角</th>
					    						
					    					</tr>
					    				</thead>
					    				<tbody>
				    						<c:if test="${null == weakCovers}">
							    						<tr>
								    						<td></td>
								    						<td>未选择测试日志</td>
								    						<td></td>
								    						<td></td>
								    						<td></td>
								    						<td></td>
								    						<td></td>
								    					</tr>
							    				</c:if>
							    				<c:if test="${null != weakCovers && empty weakCovers}">
							    						<tr>
							    							<td></td>
								    						<td>该路段下没有优化建议</td>
								    						<td></td>
								    						<td></td>
								    						<td></td>
								    						<td></td>
								    						<td></td>
								    					</tr>
							    				</c:if>
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
