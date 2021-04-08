<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>反开3D测试进度查看</title>
    <style type="text/css">
    #dd{position: relative;}

	#pointColor{position: absolute;}
    </style>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist/echarts.min.js"></script>
		
	<style type="text/css">
		table
		  {
		  /* border-collapse:collapse; */
		  }
		
		table,th, td
		  {
		  border: 1px solid black;
		  }
	</style>
	
	<script type="text/javascript">
	
	var userNums = eval(${userNumber})[0];
	var terminalData = [userNums.activeNUm ,userNums.onlineNum,userNums.offlineNum]; 
		
	    
		//本周单验报告质量分析
		//本周单验报告质量分析
		var reportAnalysisData = [{value: 0, name: '测试中小区'},{value: 0,name: '测试完成小区'},
			{value: 0, name: '单验通过小区'}];

	   	var reportAnalysisCensusOpen =  {
	   			title: {
	   				/*  x: 'center',
		    		text : '终端使用情况统计',
		    		textAlign : 'center',
		    		textStyle: {
		    			fontSize : 14,
		    			color : '#0E2D5F'
		    			},  */
	   		    },
	   		    tooltip: {
	   		        trigger: 'item',
	   		        formatter: '',
	   		     	show:true
	   		    },
	   		 	color: ['#C00000','#4BACC6', '#92D050'],
	   		    legend: {
	   		    	orient: 'vertical',
	   		        right: '5%',
	   		        top:'20%',
	   		        data: ['测试中小区', '测试完成小区', '单验通过小区']
	   		    },
	   		    grid: {
  					left: '20',
				    top: '20',
				    right: '20',
				    bottom: '40'
				},
	   		    series: [
	   		        {
	   		            name: '测试小区展示',
	   		            type: 'pie',
	   		            radius: '80%',
	   		            center: ['40%','50%'],
	   		            data: reportAnalysisData,
	   		            hoveranimation:true,
	   		            emphasis: {
	   		                itemStyle: {
	   		                    shadowBlur: 10,
	   		                    shadowOffsetX: 0,
	   		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
	   		                }
	   		            }
	   		        }
	   		    ]
	   	};
	    	                    
	    var reportAnalysissCensus;
		function initChart(){
			reportAnalysissCensus = echarts.init(document.getElementById("reportAnalysissCensus")); 
			reportAnalysissCensus.setOption(reportAnalysisCensusOpen);
		}
		var userNumber = ${userNumber};
		
		var placeData1;
		var timeoutFlag;

		$(function(){
			$.messager.alert("提示","请选择需要展示进度的区域!",'warning');
			initChart();
		});
		
		//重新绘制柱状图
		window.onresize=reload;
		function reload(){
			setTimeout(function(){
				//terminalCensus.resize();
				reportAnalysissCensus.resize();
			},200);
			
		}
		var testingList;
		var testFinishList;
		var allTestFinishList;
		
		function drawCell(val){
			if(val == 1){
				mapIframe.window.StationCompletionLayerManage(testingList);
			}else if(val == 2){
				mapIframe.window.StationCompletionLayerManage(testFinishList);
			}else if(val == 3){
				mapIframe.window.StationCompletionLayerManage(allTestFinishList);
			}
		}
		
		function areaTreeFunction(node){
			var cityName = '';
			var cityId = '';
			var cityType='';
			if(node.id != -2){//全部
				cityId = node.attributes.refId;
				cityName = node.text;
				cityType = node.attributes.type;
			}

			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/oppositeOpen3dCompletionShow/getShpName.action",
	             data: {'cityName':cityName,'cityId':cityId,'cityType':cityType},
	             dataType: "json",
	             beforeSend:function(){
	                $.messager.progress({ 
					    title: '提示', 
					    msg: '查询中……', 
					    text: '',
					    interval: 200
					});
	            },
	            success: function(data){
	            	 //给饼图赋值
	        		reportAnalysisData = [{value: data.testing, name: '测试中小区'},{value: data.testFinish,name: '测试完成小区'},
	        			{value: data.allTestFinish, name: '单验通过小区'}];
	        		reportAnalysisCensusOpen.series[0].data = reportAnalysisData;
	        		reportAnalysissCensus.setOption(reportAnalysisCensusOpen);
	        		//给表格赋值
	        		$("#testing").html(data.testing);
	        		$("#testFinish").html(data.testFinish);
	        		$("#allTestFinish").html(data.allTestFinish);
	        		var arrs = ['ENBID','CELLID','SITE_NAME','CELLNAME','TAC','PCI','FREQUENCY1','LONGITUDE','LATITUDE','TILT_M','AZIMUTH','HEIGHT','CITY'];
					mapIframe.window.StationAddCellToMap(data.shpName,true,data.lon,data.lat,arrs);
					testingList = data.testingList;
					testFinishList = data.testFinishList;
					allTestFinishList = data.allTestFinishList;
					mapIframe.window.StationCompletionLayerManage([]);
					
					lon = data.lon;
					lat = data.lat;
					var zoom = setTimeout(mapCenterAndZoom2,2000);
              	},
                complete : function () {
		             $.messager.progress('close');
		        }
	         });
		}
		
		var lon = null;
		var lat = null;
		function mapCenterAndZoom2(){
			mapIframe.window.mapCenterAndZoom2(lon,lat);
		}
		
		function queryCell(){
			var nodes = $('#areaTree').tree('getSelected');
			if(nodes == null){
				$.messager.alert("提示","请选择需要展示的单验进度区域!",'warning');
				return ;
			}
			var cellName = $('#queryCellName').combobox('getText');
			if(cellName == null || cellName == ''){
				$.messager.alert("提示","请输入需要查询的小区名",'warning');
				return ;
			}
			$.ajax({
	        	type: "GET",
	        	url: "${pageContext.request.contextPath}/oppositeOpen3dCompletionShow/queryCellLonAndLat.action",
	        	data: {'cellName':cellName,'cityName':nodes.text},
	        	dataType: "json",
	        	beforeSend:function(){
              		$.messager.progress({
				       title: '提示', 
				       msg: '查询中……', 
				       text: '',
				       interval: 200
				    });
	            },
	            success: function(data){
	            	if(data != null){
	            		$('#queryCellName').combobox({
	            		    data: data,
	            		    valueField: 'id',
	            		    textField: 'text'
	            		})
	            	}
					
             	},
               	complete : function(){
		            $.messager.progress('close');
		       	}
	       });
		}
		
		function centerCell(){
			var lonAndLat = $('#queryCellName').combobox('getValue');
			console.log(lonAndLat);
			var strs = lonAndLat.split(",");
			mapIframe.window.mapCenterAndZoom2(parseFloat(strs[0]),parseFloat(strs[1]));
		}
		
	</script>
  </head>
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	<div data-options="region:'west',title:'区域选择',split:true,tools:'#tt3'" style="width:20%;overflow: auto;">
    	<ul id="areaTree" class="easyui-tree"  data-options="onClick:areaTreeFunction,url:'${pageContext.request.contextPath}/activationShowAction/terminalGroupTree.action',lines:true,
    	formatter:function(node){
	    	return node.text;
    	}"></ul>
    </div>
	<div data-options="region:'east',split:false,border:false"style="width:80%;height:100%;">
		<!-- 东,地图 -->
		<div id="dd" class="easyui-layout" style="width:100%;height: 70%; z-index:1" >
			<div data-options="region:'center',split:false,border:false">
				<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default3.html?toolbarType=90" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
			</div>
		</div>
		<div class="easyui-layout" style="width:100%;height: 30%; z-index:1" >
			<div style="height: 100%;width: 40%;float:left;">
				<select id="queryCellName" class="easyui-combobox" name="dept" style="width:200px;" data-options="onSelect:centerCell">
			    </select>
			    <a id="btn" href="#" class="easyui-linkbutton" onclick="queryCell()">查询</a>
				<table style="width: 100%;border-collapse:collapse;margin-top:10%;" >
					<tr style="text-align: center;">
						<td style="width: 60%;">测试中小区</td>
						<td id="testing" onclick="drawCell(1)" style="cursor:pointer"></td>
					</tr>
					<tr style="text-align: center;">
						<td>测试完成小区</td>
						<td id="testFinish" onclick="drawCell(2)" style="cursor:pointer"></td>
					</tr>
					<tr style="text-align: center;">
						<td>单验通过小区</td>
						<td id="allTestFinish" onclick="drawCell(3)" style="cursor:pointer"></td>
					</tr>
				</table>
			</div>
			<div id="reportAnalysissCensus" style="height: 100%;width: 60%;float:left;"></div>
		</div>
	</div>
   	<div id="tt3">
		<a class="icon-reload" title="刷新终端" onclick="reloadArea()"></a>
		<a class="icon-search" title="显示已选终端实时轨迹" onclick="showTrack()"></a>
	</div>
	
  </body>
</html>
