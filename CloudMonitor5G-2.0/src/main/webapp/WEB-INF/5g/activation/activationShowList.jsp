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
    <title>活跃度查看</title>
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
	<%@ include file="../../../taglibs/echarts.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript">
	
		
		var versionName1 = "APP" + ${versionAndUserNumber.newestAppVersion } + "版本用户数";
		var versionName2 = "Outum" + ${versionAndUserNumber.newestOutumVersion } + "版本用户数";
		var versionData1 = [${versionAndUserNumber.newestOutumUserSum },${versionAndUserNumber.newestOutumUserSumOnLine }];
		var versionData2 = [${versionAndUserNumber.otherOutumVersionSum },${versionAndUserNumber.otherOutumVersionSumOnLine }];
		var versionData3 = [${versionAndUserNumber.newestAppUserSum },${versionAndUserNumber.newestAppUserSumOnLine }];
		var versionData4 = [${versionAndUserNumber.otherAppVersionSum },${versionAndUserNumber.otherAppVersionSumOnLine }];
		//版本统计图
	    var versionCensusOpen = {
	    	title : {
	    		x: 'center',
	    		text : '软件版本统计',
	    		textAlign : 'center',
	    		textStyle: {
	    			fontSize : 14,
	    			color : '#0E2D5F'
	    			}
	    	},
    		tooltip : {
    	        trigger: 'axis',
    	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
    	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
    	        }
    	    },
    	    legend: {
    	        //data: ['APPXX版本用户数', 'App其他版本用户数','OutumXX版本用户数','Outum版本用户数'],
    	        y : 'bottom',
    	        borderColor : 'EFEFEF',
    	        borderWidth : 1,
    	        data:[
    	            {
	    				name:versionName1
		    		},
		    		{
		    			name:"App其他版本用户数"
		    		},
		    		{
		    			name:""
		    		},
		    		{
		    			name:versionName2
		    		},
		    		{
		    			name:"Outum版本用户数"
		    		}
	    		]
    	    },
    	    grid: {
    	        x : '10%',
    	        x2 : '10%',
    	        y : '10%',
    	        y2 : '42%'
    	    },
    	    xAxis:  {
    	        type: 'value'
    	    },
    	    yAxis: {
    	        type: 'category',
    	        data: ['激活','在线']
    	    },
    	    series: [
    	        {
    	            name: versionName1,
    	            type: 'bar',
    	            barWidth : 20,
    	            stack: '软件版本统计',
    	            label: {
    	                normal: {
    	                    show: true,
    	                    position: 'insideRight'
    	                }
    	            },
    	            data: versionData1
    	        },
    	        {
    	            name: 'App其他版本用户数',
    	            type: 'bar',
    	            stack: '软件版本统计',
    	            label: {
    	                normal: {
    	                    show: true,
    	                    position: 'insideRight'
    	                }
    	            },
    	            data: versionData2
    	        },
    	        {
    	            name: versionName2,
    	            type: 'bar',
    	            stack: '软件版本统计',
    	            label: {
    	                normal: {
    	                    show: true,
    	                    position: 'insideRight'
    	                }
    	            },
    	            data: versionData3
    	        },
    	        {
    	            name: 'Outum版本用户数',
    	            type: 'bar',
    	            stack: '软件版本统计',
    	            label: {
    	                normal: {
    	                    show: true,
    	                    position: 'insideRight'
    	                }
    	            },
    	            data: versionData4
    	        }
    	    ]
	    };
	    
		//终端统计图
		var terminalHour = ${terminalUsedStatistics.tList };
		var appTime = ${terminalUsedStatistics.softwareOpenTimeList };
		var appOpenNumber = ${terminalUsedStatistics.softwareOpenNumberList };
		var outumTime = ${terminalUsedStatistics.hardwareareOpenTimeList };
		var outumOpenNumber = ${terminalUsedStatistics.hardwareareOpenNumberList };
	   	var terminalCensusOpen =  {
	   			title : {
		    		x: 'center',
		    		text : '终端使用情况统计',
		    		textAlign : 'center',
		    		textStyle: {
		    			fontSize : 14,
		    			color : '#0E2D5F'
		    			},
		    	},
	   		    tooltip : {
	   		        trigger: 'axis'
	   		    },
	   		    legend: {
	   		        
	   		    	y : 'bottom',
	   		    	borderColor : 'EFEFEF',
	    	        borderWidth : 1,
	    	        data:[
	      	            {
	  	    				name:"APP使用时间均值"
	  		    		},
	  		    		{
	  		    			name:"Outom使用时间均值"
	  		    		},
	  		    		{
	  		    			name:""
	  		    		},
	  		    		{
	  		    			name:"APP启动次数"
	  		    		},
	  		    		{
	  		    			name:"Outom启动次数"
	  		    		}
	  	    		]
	   		    },
	   		    calculable : true,
	   			grid: {
	    	        x : '10%',
	    	        x2 : '10%',
	    	        y : '10%',
	    	        y2 : '37%'
	    	    },
	   		    xAxis : [
	   		        {
	   		            type : 'category',
	   		            boundaryGap : false,
	   		            data : terminalHour
	   		        }
	   		    ],
	   		    yAxis : [
	   		        {
	   		            type : 'value',
	   		         	name:"(次数)"
	   		        },{
	   		            type : 'value',
	   		         	name:"(S)"
	   		        }
	   		    ],
	   		    series : [
	   		        {
	   		            name:'APP使用时间均值',
	   		            type:'line',
	   		         	yAxisIndex: 1,
	   		            data:appTime
	   		        },
	   		        {
	   		            name:'Outom使用时间均值',
	   		            type:'line',
	   		         	yAxisIndex: 1,
	   		            data:outumTime
	   		        },
	   		        {
	   		            name:'APP启动次数',
	   		            type:'line',
	   		     		yAxisIndex: 0,
	   		            data:appOpenNumber
	   		        },
	   		        {
	   		            name:'Outom启动次数',
	   		            type:'line',
	   		     		yAxisIndex: 0,
	   		            data:outumOpenNumber
	   		        }
	   		    ]
	   		};
	    	                    
	    require.config({
	        paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
	    });
	    
	    var versionCensus;
	    var terminalCensus;
		function initChart(){
	        require(
    			['echarts','echarts/chart/bar','echarts/chart/line'],
    			function (ec) {
    				versionCensus = ec.init(document.getElementById("versionCensus")); 
					versionCensus.setOption(versionCensusOpen);
					terminalCensus = ec.init(document.getElementById("terminalCensus")); 
					terminalCensus.setOption(terminalCensusOpen);
    			}
	    	); 
		}
		var terminalNumber = ${terminalNumber};
		function table(){
			$('#testTerminalTypeCensus').datagrid({
		       // url:'datagrid_data.json',
		        fitColumns : true,
		        columns:[[
		                  {field:'appType',title:'app安装终端型号',colspan:2,align:'center',width:400,sortable:true},
		                  {field:'outumType',title:'outum安装终端型号',colspan:2,align:'center',width:400,sortable:true}
		              ],[
		                  {field:'appTypeNumber',title:'app安装型号',align:'center',width:200,sortable:true},
		                  {field:'appInstallSum',title:'安装数量',align:'center',width:200,sortable:true},
		                  {field:'outumTypeNumber',title:'outum安装型号',width:200,align:'center'},
		                  {field:'outumInstallSum',title:'安装数量',width:200,align:'center'}
		              ]],
	          	data: terminalNumber
		    });
		}
		
		var placeData1;
		var timeoutFlag;
		function getPlaceData(){
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/activationShowAction/getUserPlaceShow.action",
	             dataType: "json",
	             success: function(data){
					placeData1 = data.LonAndLat;
					//mapIframe.window.showPointOfLonAndLat(placeData1);
					timeoutFlag = setInterval(function(){
						if(mapIframe.window.showPointOfLonAndLat){
							clearInterval(timeoutFlag);
							mapIframe.window.showPointOfLonAndLat(placeData1);
						}
					},1000);
					
					//mapIframe.window.drawEmbb(81);
               }
	         });
		}
		
		
		$(function(){
			initChart();
			table();
			getPlaceData();
		});
		
		//重新绘制柱状图
		window.onresize=reload;
		function reload(){
			setTimeout(function(){
				versionCensus.resize();
				terminalCensus.resize();
			},200);
			
		}
		
		function areaTreeFunction(node){
			var cityName = '';
			var cityId = '';
			if(node.id != -2){//全部
				cityId = node.id;
				cityName = node.text;
			}
			console.log(cityName);
			console.log(cityId);
			
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/activationShowAction/getCityData.action",
	             data: {'cityName':cityName,'cityId':cityId},
	             dataType: "json",
	             success: function(data){
	            	versionName1 = "APP" + data.versionAndUserNumber.newestAppVersion  + "版本用户数";
	     			versionName2 = "Outum" + data.versionAndUserNumber.newestOutumVersion  + "版本用户数";
	     			versionData1 = [data.versionAndUserNumber.newestOutumUserSum ,data.versionAndUserNumber.newestOutumUserSumOnLine ];
	     			versionData2 = [data.versionAndUserNumber.otherOutumVersionSum ,data.versionAndUserNumber.otherOutumVersionSumOnLine ];
	     			versionData3 = [data.versionAndUserNumber.newestAppUserSum ,data.versionAndUserNumber.newestAppUserSumOnLine ];
	     			versionData4 = [data.versionAndUserNumber.otherAppVersionSum ,data.versionAndUserNumber.otherAppVersionSumOnLine ];
	     			
	     			versionCensusOpen.legend.data[0].name = versionName1;
	     			versionCensusOpen.legend.data[3].name = versionName2;
	     			versionCensusOpen.series[0].name = versionName1;
	     			versionCensusOpen.series[2].name = versionName2;
	     			
	     			versionCensusOpen.series[0].data = versionData1;
	     			versionCensusOpen.series[1].data = versionData2;
	     			versionCensusOpen.series[2].data = versionData3;
	     			versionCensusOpen.series[3].data = versionData4;
					versionCensus.setOption(versionCensusOpen); 
					
					//终端统计图
					terminalHour = data.terminalUsedStatistics.tList;
					appTime = data.terminalUsedStatistics.softwareOpenTimeList;
					appOpenNumber = data.terminalUsedStatistics.softwareOpenNumberList;
					outumTime = data.terminalUsedStatistics.hardwareareOpenTimeList;
					outumOpenNumber = data.terminalUsedStatistics.hardwareareOpenNumberList;
					terminalCensusOpen.xAxis[0].data = terminalHour;
					terminalCensusOpen.series[0].data = appTime;
					terminalCensusOpen.series[1].data = outumTime;
					terminalCensusOpen.series[2].data = appOpenNumber;
					terminalCensusOpen.series[3].data = outumOpenNumber;
					
					terminalCensus.setOption(terminalCensusOpen);
	     			
	     			terminalNumber = data.terminalNumber;
	     			$('#testTerminalTypeCensus').datagrid('loadData',terminalNumber);
	     			
	     			$('#userSum').text(data.userNumber.userSum);
	     			$('#onlineUser').text(data.userNumber.onlineUser);
                }
	         });
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
	<div data-options="region:'center',title:'活跃度统计',split:false,tools:'#tt3'"  style="width:30%;">
		<div style="height: 10%;width: 100%">
			<div  class="text" style="height: 50%;width: 47%;background-color: D6ECF7;
				text-align:center;line-height: 30px; font-size: 20px;margin-left: 2%;float:left;">总激活用户数</div>
			<div style="height: 50%;width:47%;background-color: E9F8FF;margin-left: 2%;float:left;
				text-align:center;line-height: 30px;">
				<a style = "font-size: 20px;">当前在线用户数</a>
				<%-- <a id="onlineUser" style = "font-size:20px;color:D77E5B">&nbsp;&nbsp;${userNumber.onlineUser }</a> --%>
			</div>
			<div  class="text" style="height: 50%;width: 47%;background-color: D6ECF7;
				text-align:center;line-height: 30px; font-size: 20px;margin-left: 2%;float:left;">
				<a id="userSum" style = "font-size:20px;color:D77E5B">${userNumber.userSum }</a>
			</div>
			<div  class="text" style="height: 50%;width: 47%;background-color: E9F8FF;
				text-align:center;line-height: 30px; font-size: 20px;margin-left: 2%;float:left;">
				<a id="onlineUser" style = "font-size:20px;color:D77E5B">&nbsp;&nbsp;${userNumber.onlineUser }</a>
			</div>
			<!-- <div style="height: 50%;width:47%;background-color: E9F8FF;margin-left: 2%;float:left;
				text-align:center;line-height: 30px;">
				<a style = "font-size: 20px;">峰值在线用户数</a><a style = "font-size:20px;color:D77E5B">&nbsp;&nbsp;200</a>
			</div> -->
		</div>
		<div id="versionCensus" style="height: 30%;width: 100%"></div>
		<div id="terminalCensus" style="height: 37%;width: 100%"></div>
		<div id="testTerminalTypeCensus" style="height: 23%;width: 100%"></div>
		<!-- 当前在线用户个数为:<input id="nn" type="text" class="easyui-numberbox" data-options="min:0,precision:0"> -->
	</div>
	<div data-options="region:'east',split:false,border:false"style="width:50%;height:100%;">
		<!-- 东,地图 -->
		<div id="pointColor" style="width:150px;height:150px; z-index:999;margin-left:85%;font-size: 16px;">
			<div style="width:10px;height: 10px;border-radius:5px;background-color: red;float: left;">
			</div>
			<a style="color: white;">高度活跃用户</a></br>
			<div style="width:10px;height: 10px;border-radius:5px;background-color: blue;float: left;">
			</div>
			<a style="color:white;">中度活跃用户</a></br>
			<div style="width:10px;height: 10px;border-radius:5px;background-color: yellow;float: left;">
			</div>
			<a style="color:white;">低度活跃用户</a>
		</div>
		<div id="dd" class="easyui-layout" style="width:100%;height: 100%; z-index:1" >
			<div data-options="region:'center',split:false,border:false">
				<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default3.html?toolbarType=90" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
			</div>
		</div>
	</div>
   	<div id="tt3">
		<a class="icon-reload" title="刷新终端" onclick="reloadArea()"></a>
		<a class="icon-search" title="显示已选终端实时轨迹" onclick="showTrack()"></a>
	</div>
	
  </body>
</html>
