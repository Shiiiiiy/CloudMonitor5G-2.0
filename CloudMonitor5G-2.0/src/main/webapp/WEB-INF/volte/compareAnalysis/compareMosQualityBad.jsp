<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE专题----VoLTE对比分析----MOS质差黑点分析</title>

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
	<%@ include file="../../../taglibs/echarts.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	
	
	//路段名称回显用
	var mainTableId = 'mosBadRoadTable';
	function getRoadNameCallBackUrl(){
		return "<%=basePath%>voiceQBR/roadNameCallBack.action";
	}
	//gisCommon会调用构成mos差黑点的原始日志质差路段id
	function getMosBadId(){
		return badRoadId;
	}
	//gisCommon会调用构成mos差黑点的对比日志质差路段ids
	function getCompareMosBadIds(){
		return compareBadRoadIds;
	}
	//gisCommon会调用构成mos差黑点的黑点信息
	function getMosBadLatitude(){
		return mosBadLatitude;
	}
	//gisCommon会调用构成mos差黑点的黑点信息
	function getMosBadLongitude(){
		return mosBadLongitude;
	}
	//gisCommon会调用获取MOS黑点渲染的url
	function getCompareMosBadActionUrl(){
		return "<%=basePath%>gisSql/queryMosBad.action";
	}

	
	var mosBadRoadNumPieChart;
	$(function(){
		initTable();
	});
	window.onresize=reload;
	function reload(){
		self.window.setTimeout(function(){
			mosBadRoadNumPieChart.resize();
		}, 100);
	}
	
	function initTable(){
		$("#mosBadIndexTable0").datagrid('loading');
		$("#mosBadIndexTable1").datagrid('loading');
		$("#mosBadRoadTable").datagrid('loading');
		$.post("${pageContext.request.contextPath}/voiceCompare/doMosQualityBadAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#mosBadIndexTable0").datagrid('loaded');
					$("#mosBadIndexTable1").datagrid('loaded');
					$("#mosBadRoadTable").datagrid('loaded');
					$("#mosBadIndexTable0").datagrid('loadData',result.mosBadIndexTable0);
					$("#mosBadIndexTable1").datagrid('loadData',result.mosBadIndexTable1);
					$("#mosBadRoadTable").datagrid('loadData',result.mosBadRoadTable);
					var everyMosBadRoadNum = result.mosBadIndexTable2.rows[0];
					if(everyMosBadRoadNum){
						for(var everyMosBadRoadNumIndex = 0 ; everyMosBadRoadNumIndex < mosBadRoadNumPieItem[0].length ; everyMosBadRoadNumIndex++){
							if(everyMosBadRoadNum[mosBadRoadNumPieItem[1][everyMosBadRoadNumIndex]]){
								mosBadRoadNumPieChartOption.series[0].data.push({name:mosBadRoadNumPieItem[0][everyMosBadRoadNumIndex],value:everyMosBadRoadNum[mosBadRoadNumPieItem[1][everyMosBadRoadNumIndex]]});
							}else{
								mosBadRoadNumPieChartOption.series[0].data.push({name:mosBadRoadNumPieItem[0][everyMosBadRoadNumIndex],value:undefined});
							}
						}
					}
				}
				self.window.setTimeout(function(){
					initChart();
				}, 1000);
			}
		,"json");
	}
		var mosBadRoadNumPieItem=[['弱覆盖','干扰','邻区配置','参数配置','其他原因'],['WeakCover','Disturb','NbCell','ParamError','Other']];
		var	mosBadRoadNumPieChartOption = {
			tooltip : {trigger: 'item',formatter: "{a} <br/>{b} : {c} ({d}%)"}, 
		    legend: {orient : 'vertical',x : '30',data:mosBadRoadNumPieItem[0]},
		    toolbox: {show : true,
		    	feature : {
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true, 
		    series : [{name:'MOS差黑点原因分布',type:'pie',radius:'80%',center: ['60%', '50%'],data:['']}]
		};
		
		/* Chart路径配置 */
        require.config({
            paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
		/* 初始化图表 */
        function initChart(){
			require(
				['echarts','echarts/chart/pie'],
				function (ec) {
					mosBadRoadNumPieChart = ec.init(document.getElementById("mosBadRoadNumPieChart")); 
					mosBadRoadNumPieChart.setOption(mosBadRoadNumPieChartOption);
				}
			);
	    }
	
	var badRoadId;
	var compareBadRoadIds;
	var mosBadLatitude;
    var mosBadLongitude;
	
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		var mosBadRoadRows = $("#mosBadRoadTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!mosBadRoadRows){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个MOS差黑点路段!",'warning');
			return ;
		}
		badRoadId = mosBadRoadRows.id;
		compareBadRoadIds = mosBadRoadRows.compareIds;
		mosBadLatitude = mosBadRoadRows.mosBadLatitude;
	    mosBadLongitude = mosBadRoadRows.mosBadLongitude;
		if(!badRoadId){
			$.messager.alert("提示","请勾选某个MOS差黑点路段!",'warning');
			return ;
		}
		mapIframe.window.drawMosBad();
	}
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	
	
//easyui datagrid rows
var mosBadTableRows;
//tableRows index
var mosBadForIndex = 0;
//BMap AK
var bMapAk = 'liKpDfLP41rNnZmM1D33WljN';
//table row id
var rowIDs = [];
//table row name
var rowRoadNames = [];
/**
 * easyui datagrid data 加载完成geocode质差路的名称
 * @param tableData: datagrid data
 */
function mosTableLoadSuccess(tableData){
	mosBadTableRows = tableData.rows;
	if(mosBadTableRows&&0!=mosBadTableRows.length){
		//火狐和google浏览器无法跨域浏览
		jQuery.support.cors = true;
		mosBadGecodeRoadName(mosBadTableRows[mosBadForIndex]);
	}
}

/**
 * 
 * 调用百度地图接口geocode质差路段名称
 * @param tableRowData: datagrid data rows
 */
function mosBadGecodeRoadName(tableRowData){
	
	//路段名称为空才执行解析
	if(!tableRowData.m_stRoadName){
		 //地理编码路段经纬度优先级:1开始经纬度,2中间经纬度,3结束经纬度,4没有经纬度
		if (tableRowData.beginLatitude && tableRowData.beginLongitude
				&& 0 != tableRowData.beginLatitude && 0 != tableRowData.beginLongitude) {
			//1开始经纬度
			 $.ajax({      
		 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
		    	type:'get',     //from:1gps,3google 
		    	async : false, //默认为true 异步
		    	data:{ak:bMapAk,location:tableRowData.beginLatitude+','+tableRowData.beginLongitude},
		    	cache: true, 
		    	crossDomain: true,   
				jsonpCallback:"mosBadGeocodeSearch",
				dataType:'jsonp'
			}); 
		}else{
			if (tableRowData.courseLatitude && tableRowData.courseLongitude
					&& 0 != tableRowData.courseLatitude && 0 != tableRowData.courseLongitude) {
				//2中间经纬度
				$.ajax({      
			 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
			    	type:'get',     //from:1gps,3google 
			    	async : false, //默认为true 异步
			    	data:{ak:bMapAk,location:tableRowData.courseLatitude+','+tableRowData.courseLongitude},
			    	cache: true, 
			    	crossDomain: true,   
					jsonpCallback:"mosBadGeocodeSearch",
					dataType:'jsonp'
				}); 
			}else{
				if (tableRowData.endLatitude && tableRowData.endLongitude
						&& 0 != tableRowData.endLatitude && 0 != tableRowData.endLongitude) {
					//3结束经纬度
					$.ajax({      
				 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
				    	type:'get',     //from:1gps,3google 
				    	async : false, //默认为true 异步
				    	data:{ak:bMapAk,location:tableRowData.endLatitude+','+tableRowData.endLongitude},
				    	cache: true, 
				    	crossDomain: true,   
						jsonpCallback:"mosBadGeocodeSearch",
						dataType:'jsonp'
					}); 
				}else{
					//4没有经纬度
					//直接执行下一条
					mosBadForIndex++;
					if(mosBadForIndex < mosBadTableRows.length){
						mosBadGecodeRoadName(mosBadTableRows[mosBadForIndex]);
					}else{
						mosBadForIndex=0;
						/* $.ajax({      
					 		url:getRoadNameCallBackUrl(),
					    	type:'post', 
					    	async : true, //默认为true 异步
					    	traditional:true,
					    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
					    	cache: true, 
							dataType:'text'
						});  */
						geocodeCompareRoadName(mosBadTableRows[mosBadForIndex]);
					}
				}
			}
		}
	}else{
		//路段名称不为空直接执行下一条
		mosBadForIndex++;
		if(mosBadForIndex < mosBadTableRows.length){
			mosBadGecodeRoadName(mosBadTableRows[mosBadForIndex]);
		}else{
			mosBadForIndex=0;
			/* $.ajax({      
		 		url:getRoadNameCallBackUrl(),
		    	type:'post', 
		    	async : true, //默认为true 异步
		    	traditional:true,
		    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
		    	cache: true, 
				dataType:'text'
			});*/ 
		} 
		geocodeCompareRoadName(mosBadTableRows[mosBadForIndex]);
	}
} 

/**
 * 
 * 解析百度地图返回的地址信息,回填datagrid row,此方法会调用全局变量mainTableId,页面需要指定mainTableId变量
 * @param resultData: 百度地图gecode返回的地理化信息
 */
function mosBadGeocodeSearch(resultData){
	if (0 == resultData.status) {
		//geocode成功,解析返回数据获取地址
		if (resultData.result.addressComponent) {
			var rowIndex = $('#'+mainTableId).datagrid('getRowIndex',mosBadTableRows[mosBadForIndex]);
			var rowID = mosBadTableRows[mosBadForIndex].id;
			var rowRoadName = resultData.result.addressComponent.city + resultData.result.addressComponent.district + resultData.result.addressComponent.street;
			rowIDs.push(rowID);
			rowRoadNames.push(rowRoadName);
			if(rowIndex||0==rowIndex){
				$('#'+mainTableId).datagrid('updateRow',{
			    	index: rowIndex,
			    	row: {
			    		m_stRoadName:rowRoadName
			    	}
			    });
			}
		}
	}
	mosBadForIndex++;
	if(mosBadForIndex < mosBadTableRows.length){
		mosBadGecodeRoadName(mosBadTableRows[mosBadForIndex]);
	}else{
		mosBadForIndex=0;
		/* $.ajax({      
	 		url:getRoadNameCallBackUrl(),
	    	type:'post', 
	    	async : true, //默认为true 异步
	    	traditional:true,
	    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
	    	cache: true, 
			dataType:'text'
		});  */
		geocodeCompareRoadName(mosBadTableRows[mosBadForIndex]);
	}
}

/**
 * 
 * 调用百度地图接口geocode质差路段名称
 * @param compareTableRowData: datagrid data rows
 */
function geocodeCompareRoadName(compareTableRowData){
	
	//路段名称为空才执行解析
	if(!compareTableRowData.compareM_stRoadName){
		 //地理编码路段经纬度优先级:1开始经纬度,2中间经纬度,3结束经纬度,4没有经纬度
		if (compareTableRowData.compareBeginLatitude && compareTableRowData.compareBeginLongitude
				&& 0 != compareTableRowData.compareBeginLatitude && 0 != compareTableRowData.compareBeginLongitude) {
			//1开始经纬度
			 $.ajax({      
		 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
		    	type:'get',     //from:1gps,3google 
		    	async : false, //默认为true 异步
		    	data:{ak:bMapAk,location:compareTableRowData.compareBeginLatitude+','+compareTableRowData.compareBeginLongitude},
		    	cache: true, 
		    	crossDomain: true,   
				jsonpCallback:"compareGeocodeSearch",
				dataType:'jsonp'
			}); 
		}else{
			if (compareTableRowData.compareCourseLatitude && compareTableRowData.compareCourseLongitude
					&& 0 != compareTableRowData.compareCourseLatitude && 0 != compareTableRowData.compareCourseLongitude) {
				//2中间经纬度
				$.ajax({      
			 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
			    	type:'get',     //from:1gps,3google 
			    	async : false, //默认为true 异步
			    	data:{ak:bMapAk,location:compareTableRowData.compareCourseLatitude+','+compareTableRowData.compareCourseLongitude},
			    	cache: true, 
			    	crossDomain: true,   
					jsonpCallback:"compareGeocodeSearch",
					dataType:'jsonp'
				}); 
			}else{
				if (compareTableRowData.compareEndLatitude && compareTableRowData.compareEndLongitude
						&& 0 != compareTableRowData.compareEndLatitude && 0 != compareTableRowData.compareEndLongitude) {
					//3结束经纬度
					$.ajax({      
				 		url:'http://api.map.baidu.com/geocoder/v2/?coordtype=wgs84ll&output=json',
				    	type:'get',     //from:1gps,3google 
				    	async : false, //默认为true 异步
				    	data:{ak:bMapAk,location:compareTableRowData.compareEndLatitude+','+compareTableRowData.compareEndLongitude},
				    	cache: true, 
				    	crossDomain: true,   
						jsonpCallback:"compareGeocodeSearch",
						dataType:'jsonp'
					}); 
				}else{
					//4没有经纬度
					//直接执行下一条
					mosBadForIndex++;
					if(mosBadForIndex < mosBadTableRows.length){
						geocodeCompareRoadName(mosBadTableRows[mosBadForIndex]);
					}else{
						mosBadForIndex=0;
						$.ajax({      
					 		url:getRoadNameCallBackUrl(),
					    	type:'post', 
					    	async : true, //默认为true 异步
					    	traditional:true,
					    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
					    	cache: true, 
							dataType:'text'
						}); 
					}
				}
			}
		}
	}else{
		//路段名称不为空直接执行下一条
		mosBadForIndex++;
		if(mosBadForIndex < mosBadTableRows.length){
			geocodeCompareRoadName(mosBadTableRows[mosBadForIndex]);
		}else{
			mosBadForIndex=0;
			$.ajax({      
		 		url:getRoadNameCallBackUrl(),
		    	type:'post', 
		    	async : true, //默认为true 异步
		    	traditional:true,
		    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
		    	cache: true, 
				dataType:'text'
			}); 
		}
	}
} 
/**
 * 
 * 解析百度地图返回的地址信息,回填datagrid row,此方法会调用全局变量mainTableId,页面需要指定mainTableId变量
 * @param resultData: 百度地图gecode返回的地理化信息
 */
function compareGeocodeSearch(compareResultData){
	if (0 == compareResultData.status) {
		//geocode成功,解析返回数据获取地址
		if (compareResultData.result.addressComponent) {
			var rowIndex = $('#'+mainTableId).datagrid('getRowIndex',mosBadTableRows[mosBadForIndex]);
			var rowID = mosBadTableRows[mosBadForIndex].compareId;
			var rowRoadName = compareResultData.result.addressComponent.city + compareResultData.result.addressComponent.district + compareResultData.result.addressComponent.street;
			rowIDs.push(rowID);
			rowRoadNames.push(rowRoadName);
			if(rowIndex||0==rowIndex){
				$('#'+mainTableId).datagrid('updateRow',{
			    	index: rowIndex,
			    	row: {
			    		compareM_stRoadName:rowRoadName
			    	}
			    });
			}
		}
	}
	mosBadForIndex++;
	if(mosBadForIndex < mosBadTableRows.length){
		geocodeCompareRoadName(mosBadTableRows[mosBadForIndex]);
	}else{
		mosBadForIndex=0;
		$.ajax({      
	 		url:getRoadNameCallBackUrl(),
	    	type:'post', 
	    	async : true, //默认为true 异步
	    	traditional:true,
	    	data:{rowIDs:rowIDs,rowRoadNames:rowRoadNames},
	    	cache: true, 
			dataType:'text'
		});
	}
}

	</script>
  </head>
  <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" style="width: 100%;height: 100%;" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=23" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和MOS差黑点统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- MOS差黑点指标列表 -->
		    	<div data-options="region:'north',border:false" style="height:142px;">
  					<table id="mosBadIndexTable0"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'指标列表',tools:'#tt'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'testLogType',width:60,align:'center',formatter:
	    							function(value,row,index){
										if (0==value){
											return '原始';
										}else if(1==value){
											return '对比';
										}else if(2==value){
											return '汇总';
										}
										return value;
									}
								"></th>
	    						<th data-options="field:'volteMosAvg',width:90,align:'center',formatter:numToFixed2Formatter">VOLTE平均<br>MOS</th>
	    						<th data-options="field:'rtpDropRatio',width:80,align:'center',formatter:numToFixed2Formatter">RTP<br>丢包率</th>
	    						<th data-options="field:'rtpShake',width:80,align:'center'">RTP<br>抖动(ms)</th>
	    						<th data-options="field:'lteHoSuccRate',width:100,align:'center',formatter:numToFixed2Formatter">LTE HO<br>成功率</th>
	    						<th data-options="field:'rsrpAvg',width:80,align:'center',formatter:numToFixed2Formatter">平均<br>RSRP</th>
	    						<th data-options="field:'lteCoverRate',width:80,align:'center',formatter:numToFixed2Formatter">LTE<br>覆盖率</th>
	    						<th data-options="field:'sinrAvg',width:80,align:'center',formatter:numToFixed2Formatter">平均<br>SINR</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr><td>0</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	    					<tr><td>1</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	    					<tr><td>2</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	
				<div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
					<div class="easyui-layout" style="width:100%;height: 100%;">
				    	<!-- MOS差黑点路段列表  -->
				    	<div data-options="region:'center',border:false" style="padding:2px 0px 2px 0px;">
    						<table id="mosBadRoadTable" class="easyui-datagrid" data-options="title:'MOS差黑点路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,remoteSort:false,multiSort:true,rownumbers:true,singleSelect:true,tools:'#tt2',onLoadSuccess:mosTableLoadSuccess" >
			    				<thead>
			    					<tr>
			    						<th rowspan="2" data-options="field:'id',checkbox:true"></th>
			    						<th rowspan="2" data-options="field:'qbrType',width:60,align:'center',formatter:
			    							function(value,row,index){
												if (0==value){
													return '弱覆盖';
												}else if(1==value){
													return '干扰';
												}else if(2==value){
													return '邻区配置';
												}else if(3==value){
													return '参数配置';
												}else if(4==value){
													return '其他原因';
												}
												return value;
											}
										">质差<br>原因</th>
			    						<th colspan="4" >原始日志质差差路段信息</th>
			    						<th colspan="18" >对比日志质差差路段信息</th>
			    					</tr>
			    					<tr>
			    						<th data-options="field:'boxId',width:80,align:'center',sortable:true,formatter:showTooltip">BOXID</th>
			    						<th data-options="field:'m_stRoadName',width:80,align:'center',formatter:showTooltip">路段名称</th>
	    								<th data-options="field:'m_dbDistance',width:80,align:'center',formatter:numToFixed2Formatter,sortable:true">持续距离<br>(m)</th>
	    								<th data-options="field:'m_dbContinueTime',width:80,align:'center',formatter:numDivide1000Formatter,sortable:true">测试时间<br>(s)</th>
			    						<th data-options="field:'compareBoxId',width:80,align:'center',sortable:true,formatter:showTooltip">BOXID</th>
			    						<th data-options="field:'compareM_stRoadName',width:80,align:'center',formatter:showTooltip">路段名称</th>
	    								<th data-options="field:'compareM_dbDistance',width:80,align:'center',formatter:numToFixed2Formatter,sortable:true">持续距离<br>(m)</th>
	    								<th data-options="field:'compareM_dbContinueTime',width:80,align:'center',formatter:numDivide1000Formatter,sortable:true">测试时间<br>(s)</th>
			    						<th data-options="field:'fileName',hidden:true">测试日志名称</th>
			    						<th data-options="field:'recSeqNo',hidden:true">recSeqNo</th>
			    						<th data-options="field:'beginLatitude',hidden:true">开始纬度</th>
			    						<th data-options="field:'courseLatitude',hidden:true">中间纬度</th>
			    						<th data-options="field:'endLatitude',hidden:true">结束纬度</th>
			    						<th data-options="field:'beginLongitude',hidden:true">开始经度</th>
			    						<th data-options="field:'courseLongitude',hidden:true">中间经度</th>
			    						<th data-options="field:'endLongitude',hidden:true">结束经度</th>
			    						<th data-options="field:'compareBeginLatitude',hidden:true">开始纬度</th>
			    						<th data-options="field:'compareCourseLatitude',hidden:true">中间纬度</th>
			    						<th data-options="field:'compareEndLatitude',hidden:true">结束纬度</th>
			    						<th data-options="field:'compareBeginLongitude',hidden:true">开始经度</th>
			    						<th data-options="field:'compareCourseLongitude',hidden:true">中间经度</th>
			    						<th data-options="field:'compareEndLongitude',hidden:true">结束经度</th>
			    						<th data-options="field:'compareId',hidden:true">对比日志ID</th>
			    					</tr>
			    				</thead>
			    			</table>
			    			<div id="tt2" >
								<a href="#" style="width:50px;text-decoration:underline;" onclick="doAnalysis();" title="MOS差黑点分析">黑点分析</a>
							</div>
				    	</div>
				    	<!-- MOS差黑点路段统计 -->
				    	<div data-options="region:'south'" style="height:50%;padding:2px 0px 0px 0px;">
    						<div class="easyui-layout" style="width:100%;height: 100%;">
    							<div data-options="region:'north',border:false" style="height:95px;">
    								<table id="mosBadIndexTable1" class="easyui-datagrid" data-options="title:'MOS差黑点路段统计',scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
					    				<thead>
					    					<tr>
					    						<th data-options="field:'roadNum',width:60,align:'center'">语音质差路段<br>数量</th>
					    						<th data-options="field:'distance',width:50,align:'center',formatter:numToFixed2Formatter">总里程(米)</th>
					    						<th data-options="field:'continueTime',width:60,align:'center',formatter:numDivide1000Formatter">总测试时长(s)</th>
					    						<th data-options="field:'mosPointNumRatio',width:120,align:'center',formatter:numToFixed2Formatter">MOS采样点占总MOS采样<br>点数比例(%)</th>
					    						<th data-options="field:'cellNumRatio',width:80,align:'center',formatter:numToFixed2Formatter">涉及小区占总<br>小区比例(%)</th>
					    						<th data-options="field:'terminalNumRatio',width:90,align:'center',formatter:numToFixed2Formatter">涉及终端占总测试<br>终端比例(%)</th>
					    					</tr>
					    				</thead>
					    				<tbody>
					    					<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
					    				</tbody>
					    			</table>
    							</div>
    							<div id="mosBadRoadNumPieChart" data-options="region:'center',border:false" >
					    		</div>
    						</div>
				    	</div>
				    </div>
				</div>
		    </div>
    	</div>
	</body>
</html>
