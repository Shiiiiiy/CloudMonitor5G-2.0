<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>eMBB覆盖专题----重叠覆盖专题分析</title>

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
	
	//存储日志id,按","分隔
	var testLogItemIds = '${testLogItemIds}';
	//gisCommon会调用获取测试日志ID
	function getCellTestLogItemIds(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成小区SQL的url
	function getCellActionUrl(){
		return "<%=basePath%>gisSql/queryCellInfo.action";
	}
	
	//gisCommon会调用获取测试日志ID
	function getTestLogItemIds2QBR(){
		return testLogItemIds;
	}
	//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
	function getTLI2QBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryEmbbCoverRoadsPoints.action?coverType=2";
	}
	
	//存储质差路段id
	var embbrid;
	//gisCommon会调用获取质差路段ID
	function getGpsPointQBRId(){
		return embbrid;
	}
	//gisCommon会调用获取生成质差路段轨迹的url
	function getQBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryEmbbCoverRoadPoints.action";
	}
	//gisCommon会调用获取生成质差路段轨迹行驶方向的url
	function getQBRDirectionActionUrl(){
		return "<%=basePath%>gisSql/queryEmbbCoverRoadDirection.action";
	}
	
	/* Chart路径配置 */
    require.config({
        paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
    });
	$(function(){	
		initChart();
	});
	
	//重新加载
	var roadRatePieChart;
	window.onresize=reload;
	function reload(){
		setTimeout(function(){
			roadRatePieChart.resize();
		},200);
		
	}
	
	/* 初始化图表 */
    function initChart(){
		require(
			['echarts','echarts/chart/pie','echarts/chart/bar'],
			function (ec) {
				roadRatePieChart = ec.init(document.getElementById("roadRateChart")); 
				roadRatePieChart.setOption(roadRatePieOption);
			}
		); 
		
    }
	
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}

	var roadRatePieOption = {
		    title : {show:false},
		    tooltip : {
		        trigger: 'item',
				formatter:function(params,ticket){
					if(params.percent){
						return params.name+'<br/>路段占比:'+params.percent+'%&nbsp;&nbsp;&nbsp;&nbsp;路段数:'+params.value;
					}else{
						return params.seriesName+'<br/>'+params.name+'&nbsp;&nbsp;&nbsp;&nbsp路段数:'+params.value;
					}
				}
		    },
		    grid:{x:'35%',x2:'70',y:30,y2:70},
		    legend: {
		        orient : 'horizontal',
				y:'bottom',
				itemWidth:20,
				itemGap:5,
		        data:[
					'原因:邻区缺失;建议:建议添加邻区,同时注意优化PCI',
				    '原因:小区方位角不合理;建议:调整邻区方位角',
				    '',
				    '原因:主小区波束异常;建议:优化主小区天线权值',
				    '原因:其他原因;建议:现场复测分析'
				],
				formatter:function(a,b,c,d){
					return a;
		        }
		    	//,
				//textStyle:{
				//	fontSize:8
				//}
		    },
		    toolbox: {show : false},
		    xAxis : [
	 	        {
	 	            type : 'value',
	 	          	name:'路段数统计'
	 	        }
	 	    ],
	 	    yAxis : [
	 	        {
	 	            type : 'category',
	 	            data : ['[0,60%)','[60%,80%)','[80%,100%]']
	 	        }
	 	    ],
		    series : [
		        {
		            type:'pie',
		            radius : '60%',
		            center: ['15%', '45%'],
		          	selectedOffset:10,
					selectedMode:'single',
		          	itemStyle: {
		              normal:{
		                  label: { show: false},
		                  labelLine: {show: false}
		              }
		            },
		            data:[
		                {value:${embbCoverTotalIndex.reasonRateNumList[1].total}, name:'原因:邻区缺失;建议:建议添加邻区,同时注意优化PCI'},
		                {value:${embbCoverTotalIndex.reasonRateNumList[10].total}, name:'原因:小区方位角不合理;建议:调整邻区方位角'},
		                {value:${embbCoverTotalIndex.reasonRateNumList[11].total}, name:'原因:主小区波束异常;建议:优化主小区天线权值'},
		                {value:${embbCoverTotalIndex.reasonRateNumList[9].total}, name:'原因:其他原因;建议:现场复测分析'}
		            ]
		        },
		        {
		            name:'原因:邻区缺失;建议:建议添加邻区,同时注意优化PCI',
		            type:'bar',
		            stack:'数量',
		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:[${embbCoverTotalIndex.reasonRateNumList[1].rate0t6==0?"'-'":embbCoverTotalIndex.reasonRateNumList[1].rate0t6},${embbCoverTotalIndex.reasonRateNumList[1].rate6t8==0?"'-'":embbCoverTotalIndex.reasonRateNumList[1].rate6t8},${embbCoverTotalIndex.reasonRateNumList[1].rate8t100==0?"'-'":embbCoverTotalIndex.reasonRateNumList[1].rate8t100}]
		        },
		        {
		            name:'原因:小区方位角不合理;建议:调整邻区方位角',
		            type:'bar',
		            stack:'数量',
		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:[${embbCoverTotalIndex.reasonRateNumList[10].rate0t6==0?"'-'":embbCoverTotalIndex.reasonRateNumList[10].rate0t6},${embbCoverTotalIndex.reasonRateNumList[10].rate6t8==0?"'-'":embbCoverTotalIndex.reasonRateNumList[10].rate6t8},${embbCoverTotalIndex.reasonRateNumList[10].rate8t100==0?"'-'":embbCoverTotalIndex.reasonRateNumList[10].rate8t100}]
		        },
		        {
		            name:'原因:主小区波束异常;建议:优化主小区天线权值',
		            type:'bar',
		            stack:'数量',
		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:[${embbCoverTotalIndex.reasonRateNumList[11].rate0t6==0?"'-'":embbCoverTotalIndex.reasonRateNumList[11].rate0t6},${embbCoverTotalIndex.reasonRateNumList[11].rate6t8==0?"'-'":embbCoverTotalIndex.reasonRateNumList[11].rate6t8},${embbCoverTotalIndex.reasonRateNumList[11].rate8t100==0?"'-'":embbCoverTotalIndex.reasonRateNumList[11].rate8t100}]
		        },
		        {
		            name:'原因:其他原因;建议:现场复测分析',
		            type:'bar',
		            stack:'数量',
		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:[${embbCoverTotalIndex.reasonRateNumList[9].rate0t6==0?"'-'":embbCoverTotalIndex.reasonRateNumList[9].rate0t6},${embbCoverTotalIndex.reasonRateNumList[9].rate6t8==0?"'-'":embbCoverTotalIndex.reasonRateNumList[9].rate6t8},${embbCoverTotalIndex.reasonRateNumList[9].rate8t100==0?"'-'":embbCoverTotalIndex.reasonRateNumList[9].rate8t100}]
		        }
		    ]
		};

	var embbCoverOptimizeAdviceTableColumns = [
 		[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:80,align:'center',title:'小区CELLID',sortable:true},
 			{field:'distanceAvg',width:80,align:'center',title:'和路段<br>距离(m)',formatter:numToFixed2Formatter,sortable:true},
 			{field:'cellDistance',width:80,align:'center',title:'主邻小区<br>平均距离(m)',formatter:numToFixed2Formatter,sortable:true},
 			{field:'rsrpAvg',width:60,align:'center',title:'RSRP<br>均值(dBm)',formatter:numToFixed2Formatter,sortable:true},
 			{field:'sinrAvg',width:120,align:'center',title:'SINR<br>均值',formatter:numToFixed2Formatter,sortable:true}
 		]],[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
 			{field:'pci',width:50,align:'center',title:'SC-PCI',sortable:true},
 			{field:'nb_cellName',width:90,align:'center',title:'建议添加<br>邻小区友好名',sortable:true},
 			{field:'nb_cellId',width:90,align:'center',title:'建议添加<br>邻小区CELLID',sortable:true},
 			{field:'nb_pci',width:50,align:'center',title:'NC-PCI',sortable:true},
 			{field:'distance',width:50,align:'center',title:'站间距',sortable:true}
 		]],[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
 			{field:'azimuth',width:80,align:'center',title:'工参方位角',sortable:true},
 			{field:'distance',width:90,align:'center',title:'天馈反接距离(m)',sortable:true}
 		]],[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
 			{field:'maxBeamRsrpAvg',width:80,align:'center',title:'最强波束RSRP(dBm)',sortable:true},
 			{field:'beamRsrpAvg',width:90,align:'center',title:'占用波束RSRP(dBm)',sortable:true}
 		]],[[
 			{field:'adjustRank',width:50,align:'center',title:'调整<br>优先级',formatter:showAdjustRank,sortable:true,order:'asc'}, 
 			{field:'cellName',width:80,align:'center',title:'小区<br>友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区<br>CELLID',sortable:true},
 			{field:'distanceAvg',width:80,align:'center',title:'和路段<br>距离(m)',sortable:true},
 			{field:'rsrpAvg',width:50,align:'center',title:'RSRP均值<br>(dBm)',sortable:true},
 			{field:'sinrAvg',width:50,align:'center',title:'SINR均值',sortable:true},
 			{field:'downdipAngle',width:50,align:'center',title:'工参<br>下倾角',sortable:true},
 			{field:'adviceDowndipAngle',width:50,align:'center',title:'建议调整<br>下倾角',sortable:true,order:'desc'}
 		]],[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
 			{field:'nb_cellName',width:90,align:'center',title:'待核查邻区友好名',sortable:true},
 			{field:'nb_cellId',width:90,align:'center',title:'待核查邻区CELLID',sortable:true}
 		]],[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区CELLID',sortable:true},
 			{field:'height',width:90,align:'center',title:'天线高度(m)',sortable:true}
 		]],[[
 			{field:'adjustRank',width:50,align:'center',title:'调整<br>优先级',formatter:showAdjustRank,sortable:true,order:'asc'}, 
 			{field:'cellName',width:80,align:'center',title:'小区<br>友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区<br>CELLID',sortable:true},
 			{field:'distanceAvg',width:80,align:'center',title:'和路段<br>距离(m)',sortable:true},
 			{field:'rsrpAvg',width:50,align:'center',title:'RSRP均值<br>(dBm)',sortable:true},
 			{field:'sinrAvg',width:50,align:'center',title:'SINR均值',sortable:true},
 			{field:'downdipAngle',width:50,align:'center',title:'工参<br>下倾角',sortable:true,order:'desc'}
 		]],[[
 			{field:'adjustRank',width:50,align:'center',title:'调整<br>优先级',formatter:showAdjustRank,sortable:true,order:'asc'}, 
 			{field:'cellName',width:80,align:'center',title:'小区<br>友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区<br>CELLID',sortable:true},
 			{field:'distanceAvg',width:80,align:'center',title:'和路段<br>距离(m)',sortable:true},
 			{field:'rsrpAvg',width:50,align:'center',title:'RSRP均值<br>(dBm)',sortable:true},
 			{field:'sinrAvg',width:50,align:'center',title:'SINR均值',sortable:true},
 			{field:'power',width:50,align:'center',title:'功率',sortable:true,order:'desc'}
 		]],[[
 			{field:'adjustRank',width:50,align:'center',title:'调整<br>优先级',formatter:showAdjustRank,sortable:true,order:'asc'}, 
 			{field:'cellName',width:80,align:'center',title:'小区<br>友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:50,align:'center',title:'小区<br>CELLID',sortable:true},
 			{field:'distanceAvg',width:80,align:'center',title:'和路段<br>距离(m)',sortable:true},
 			{field:'rsrpAvg',width:50,align:'center',title:'RSRP均值<br>(dBm)',sortable:true},
 			{field:'sinrAvg',width:50,align:'center',title:'SINR均值',sortable:true},
 			{field:'height',width:90,align:'center',title:'天线高度(m)',sortable:true},
 			{field:'azimuth',width:80,align:'center',title:'工参方位角',sortable:true},
 			{field:'downdipAngle',width:50,align:'center',title:'工参<br>下倾角',sortable:true,order:'desc'},
 			{field:'power',width:50,align:'center',title:'功率',sortable:true}
 		]],[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:80,align:'center',title:'小区CELLID',sortable:true},
 			{field:'beamRsrpAvg',width:90,align:'center',title:'占用波束RSRP(dBm)',sortable:true},
 			{field:'nb_cellName',width:80,align:'center',title:'邻区友好名',sortable:true},
 			{field:'nb_cellId',width:80,align:'center',title:'邻区CELLID',sortable:true},
 			{field:'nb_rsrpAvg',width:60,align:'center',title:'邻区RSRP(dBm)',sortable:true},
 			{field:'nb_azimuth',width:60,align:'center',title:'邻区方位角',sortable:true}
 		]],[[
 			{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
 			{field:'cellId',width:80,align:'center',title:'小区CELLID',sortable:true},
 			{field:'beamRsrpAvg',width:90,align:'center',title:'占用波束RSRP(dBm)',sortable:true},
 			{field:'nb_cellName',width:80,align:'center',title:'邻区友好名',sortable:true},
 			{field:'nb_cellId',width:80,align:'center',title:'邻区CELLID',sortable:true},
 			{field:'nb_rsrpAvg',width:60,align:'center',title:'邻区RSRP(dBm)',sortable:true}
 		]]
 		
 	];
 	
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
	
	/* 开始分析,分析按钮 */
	function doAnalysis(){
		/* $("#eastLayout").layout("remove","north");
		var c = $('#eastLayout');
		var p = c.layout('panel','south');
		p.panel('resize', {height:'60%'});
		c.layout('resize');
		$("#southLayout").layout("remove","west");
		$("#southLayout").layout("add",{
			region:'south',
			title:'ceshi',
			height: '50%',
			collapsible:false,
			content:$('#ceshi')
		}); */
		
		$("#n1").css('display','none');
		var c = $('#eastLayout');
		var p = c.layout('panel','south');
		p.panel('resize', {height:'50%'});
		c.layout('resize');
		$("#southLayout0").css('display','none');
		$("#southLayout1").css('display','block');
		$("#southLayout1").layout("resize");
		$("#tt2_1").css('display','inline-block');
		$("#tt2_2").css('display','inline-block');
		
		
		var embbCoverRow = $("#embbCoverTable").datagrid('getSelected');//获取表格中用户选中数据
		if(!embbCoverRow){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个路段!",'warning');
			return ;
		}
		//testLogItemIds = embbCoverRow.recSeqNo;
		if(!embbCoverRow.id){
			$.messager.alert("提示","请勾选某个路段!",'warning');
			return ;
		}
		
		embbrid = embbCoverRow.id;
		mapIframe.window.drawEmbb(81);
		
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/embbCover5g/doEmbbCoverRoadAnalysis.action",{roadId:embbCoverRow.id},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					//小区详情表
					//$("#embbCoverCellTable").datagrid('loadData',[]);
					$("#embbCoverCellTable").datagrid('loadData',result.emmCoverCell);
					//优化建议
					//$("#embbCoverOptimizeAdviceTable").datagrid('loadData',[]);
					if(embbCoverRow.reasonTypeNum){
						$("#embbCoverOptimizeAdviceTable").datagrid({
							title:null,fit:true,scrollbarSize:0,striped:true,fitColumns:true,
							remoteSort:false,multiSort:true,border:false,
							columns:embbCoverOptimizeAdviceTableColumns[embbCoverRow.reasonTypeNum]
						});
						$("#embbCoverOptimizeAdviceTable").datagrid('loadData',result.emmCoverOptimizeAdvice);
					}
				}
			}
		,"json");
	}
	
	function formatBeamInfo(value,row,index){
		return "<a onclick='showBeamInfo(this,"+row.id+")' href='javascript:void(0)'>"+value+"</a>";
	}
	
	function showBeamInfo(that,cellInfoId){
		//$("#embbCoverOptimizeAdviceTable11").datagrid('loadData',[{},{},{}]);
		$('#beamInfoWindow').dialog('open');
		$("#beamInfoTable").datagrid({
			title:null,fit:true,scrollbarSize:0,striped:true,fitColumns:true,
			remoteSort:false,multiSort:true,border:false,
			columns:[[
				{field:'beamNo',width:50,align:'center',title:'波束编号',sortable:true},
				{field:'rsrp',width:50,align:'center',title:'RSRP(dBm)',sortable:true},
				{field:'rsrq',width:90,align:'center',title:'RSRQ(dBm)',sortable:true},
				{field:'sinr',width:80,align:'center',title:'SINR',sortable:true},
				{field:'v_fPowerAngle',width:80,align:'center',title:'垂直半功率角(度)',sortable:true}, 
				{field:'h_fPowerAngle',width:80,align:'center',title:'水平半功率角(度)',sortable:true}
			]],
			url:'${pageContext.request.contextPath}/embbCover5g/doEmbbCoverCellAnalysis.action?cellInfoId='+cellInfoId
		});
		//$("#beamInfoTable").datagrid('loadData',[{},{},{}]);
		$("#beamInfoTable").datagrid('resize');
	}
	function showNBInfo(roadId){
		// 以ajax方式，发送到服务器，完成操作
		$.post("${pageContext.request.contextPath}/embbCover5g/doEmbbCoverRoadAnalysis.action",{roadId:roadId},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$('#nbInfoWindow').dialog('open');
					$("#nbInfoTable").datagrid({
						title:null,fit:true,scrollbarSize:0,striped:true,fitColumns:true,
						remoteSort:false,multiSort:true,border:false,
						columns:[[
							//{field:'cellName',width:80,align:'center',title:'小区友好名',formatter:showTooltip}, 				
							{field:'cellId',width:90,align:'center',title:'主ID',sortable:true},
							//{field:'pci',width:50,align:'center',title:'SC-PCI',sortable:true},
							//{field:'nb_cellName',width:90,align:'center',title:'建议添加<br>邻小区友好名',sortable:true},
							{field:'nb_cellId',width:90,align:'center',title:'邻小区ID',sortable:true},
							//{field:'nb_pci',width:50,align:'center',title:'NC-PCI',sortable:true},
							//{field:'distance',width:50,align:'center',title:'站间距',sortable:true}
							{field:'advice',width:50,align:'center',title:'优化建议',formatter: function(value,row,index){
								return '建议添加';
							}}
						]]
					});
					$("#nbInfoTable").datagrid('loadData',result.emmCoverOptimizeAdvice);
					$("#nbInfoTable").datagrid('resize');
					
					
				}
			}
		,"json");
	}
	/*返回*/
	function redoAnalysis(){
		$("#n1").css('display','block');
		var c = $('#eastLayout');
		var p = c.layout('panel','south');
		p.panel('resize', {height:'40%'});
		c.layout('resize');
		$("#southLayout0").css('display','block');
		$("#southLayout1").css('display','none');
		$("#southLayout0").layout("resize");
		$("#tt2_1").css('display','none');
		$("#tt2_2").css('display','none');
		
		
		$("#embbCoverTable").datagrid('unselectAll');
		var m_src = mapIframe.window.location;
		//alert(m_src);
		mapIframe.window.location = m_src;
		//$("iframe[name='mapIframe']").attr('src', url);
		
		//mapIframe.src = m_src;
		
	}
	</script>
  </head>
  <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" style="width: 100%;height: 100%;" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=80" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div  id="eastLayout" class="easyui-layout" style="width:100%;height: 100%;">
	    		<div id="n1" data-options="region:'north',border:false" style="height:113px;padding:0px 0px 4px 0px;">
					<table id="embbCoverIndexTable"   class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'总体指标统计',tools:'#tt1'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'coverNum',width:30,align:'center'">路段<br/>总数</th>
	    						<th data-options="field:'pointRate',width:45,align:'center',formatter:numToFixed2Formatter">采样点<br/>占比</th>
	    						<th data-options="field:'distance',width:45,align:'center',formatter:numToFixed2Formatter">总持续<br/>距离<br/>(m)</th>
	    						<th data-options="field:'maxDistance',width:60,align:'center',formatter:numToFixed2Formatter">最长持续<br/>距离<br/>(m)</th>
	    						<th data-options="field:'minDistance',width:60,align:'center',formatter:numToFixed2Formatter">最短持续<br/>距离<br/>(m)</th>
	    						<th data-options="field:'avgDistance',width:60,align:'center',formatter:numToFixed2Formatter">平均持续<br/>距离<br/>(m)</th>
	    						<th data-options="field:'distanceRate',width:60,align:'center',formatter:numToFixed2Formatter">持续距离<br/>占比<br/>(%)</th>
	    						<th data-options="field:'time',width:45,align:'center',formatter:numToFixed2Formatter">总测试<br/>时长<br/>(s)</th>
	    						<th data-options="field:'maxTime',width:60,align:'center',formatter:numToFixed2Formatter">最长测试<br/>时长<br/>(s)</th>
	    						<th data-options="field:'minTime',width:60,align:'center',formatter:numToFixed2Formatter">最短测试<br/>时长<br/>(s)</th>
	    						<th data-options="field:'avgTime',width:60,align:'center',formatter:numToFixed2Formatter">平均测试<br/>时长<br/>(s)</th>
	    						<th data-options="field:'timeRate',width:60,align:'center',formatter:numToFixed2Formatter">测试时长<br/>占比<br/>(%)</th>
	    						<th data-options="field:'sinr',width:45,align:'center',formatter:numToFixed2Formatter">SINR</th>
	    						<th data-options="field:'rsrp',width:45,align:'center',formatter:numToFixed2Formatter">RSRP<br/>(dBm)</th>
	    						<th data-options="field:'ftpUpSpeed',width:60,align:'center',formatter:numToFixed2Formatter">FTP上传<br/>速率<br/>(kbps)</th>
	    						<th data-options="field:'ftpDlSpeed',width:60,align:'center',formatter:numToFixed2Formatter">FTP下载<br/>速率<br/>(kbps)</th>
	    					</tr>
	    				</thead>
	    				<tbody>
    						<tr>
	    						<td>${embbCoverTotalIndex.coverNum}</td>
	    						<td>${embbCoverTotalIndex.pointRate}</td>
	    						<td>${embbCoverTotalIndex.distance}</td>
	    						<td>${embbCoverTotalIndex.maxDistance}</td>
	    						<td>${embbCoverTotalIndex.minDistance}</td>
	    						<td>${embbCoverTotalIndex.avgDistance}</td>
	    						<td>${embbCoverTotalIndex.distanceRate}</td>
	    						<td>${embbCoverTotalIndex.time}</td>
	    						<td>${embbCoverTotalIndex.maxTime}</td>
	    						<td>${embbCoverTotalIndex.minTime}</td>
	    						<td>${embbCoverTotalIndex.avgTime}</td>
	    						<td>${embbCoverTotalIndex.timeRate}</td>
	    						<td>${embbCoverTotalIndex.sinr}</td>
	    						<td>${embbCoverTotalIndex.rsrp}</td>
	    						<td>${embbCoverTotalIndex.ftpUpSpeed}</td>
	    						<td>${embbCoverTotalIndex.ftpDlSpeed}</td>
	    					</tr>
	    				</tbody>
	    			</table>
	    			<div id="tt1" >
						<a href="#" style="width:45px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
				</div>
	    		<div data-options="region:'center',border:false" style="padding:0px 0px 2px 0px;" >
	    			<table id="embbCoverTable" class="easyui-datagrid" data-options="title:'重叠覆盖路段列表',scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,remoteSort:false,multiSort:true,tools:'#tt2'"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'id',checkbox:true"></th>
	    						<th data-options="field:'roadName',width:80,align:'center',formatter:showTooltip">道路名称</th>
	    						<th data-options="field:'startTimeString',width:80,align:'center',formatter:showTooltip">开始时间</th>
	    						<th data-options="field:'continueTimeString',width:30,align:'center',formatter:numToFixed2Formatter">测试<br/>时长<br/>(s)</th>
	    						<th data-options="field:'distance',width:30,align:'center',formatter:numToFixed2Formatter">持续<br/>距离<br/>(m)</th>
	    						<th data-options="field:'rsrpAvg',width:30,align:'center',formatter:numToFixed2Formatter">RSRP<br/>均值<br/>(dBm)</th>
	    						<th data-options="field:'rsrpMin',width:45,align:'center',formatter:numToFixed2Formatter">RSRP<br/>最低值<br/>(dBm)</th>
	    						<th data-options="field:'sinrAvg',width:30,align:'center',formatter:numToFixed2Formatter">SINR<br/>均值</th>
	    						<th data-options="field:'sinrMin',width:45,align:'center',formatter:numToFixed2Formatter">SINR<br/>最低值</th>
	    						<th data-options="field:'coverRate',width:45,align:'center',formatter:numToFixed2Formatter">重叠覆盖<br/>占比<br/>（%）</th>
	    						<th data-options="field:'cellToNbCellDistanceAvg',width:60,align:'center',formatter:numToFixed2Formatter">主邻小区<br/>平均距离<br/>（m）</th>
	    						<th data-options="field:'tiankuiRevDistance',width:60,align:'center',formatter:numToFixed2Formatter">天馈反接<br/>距离<br/>（m）</th>
	    						<th data-options="field:'reasonTypeNum',width:60,align:'center',
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '距离脱离5G覆盖区域';
											}else if(1==value){
												return '<a onclick=\'showNBInfo('+row.id+')\' href=\'javascript:void(0)\'>邻区缺失</a>';
											}else if(2==value){
												return '小区反向覆盖导致';
											}else if(3==value){
												return '小区波束异常';
											}else if(4==value){
												return '其他原因';
											}else if(5==value){
												return '小区无信号';
											}else if(6==value){
												return '超高站';
											}else if(7==value){
												return '天线倾角不合适';
											}else if(8==value){
												return '下行功率分配不合理';
											}else if(9==value){
												return '其他原因';
											}else if(10==value){
												return '小区方位角不合理';
											}else if(11==value){
												return '主小区波束异常';
											}
										}
										return value;
									}">原因分析</th>
	    						<th data-options="field:'reasonTypeNum1',width:60,align:'center',
	    							formatter: function(value,row,index){
										if (value){
											if(0==value){
												return '建议加站';
											}else if(1==value){
												return '建议添加邻区';
											}else if(2==value){
												return '反向覆盖建议调整';
											}else if(3==value){
												return '天线权值优化';
											}else if(4==value){
												return '天馈调整';
											}else if(5==value){
												return '核查影响无线性能的告警';
											}else if(6==value){
												return '降低天线的高度';
											}else if(7==value){
												return '增大天线下倾角';
											}else if(8==value){
												return '减小小区功率';
											}else if(9==value){
												return '现场复测分析';
											}else if(10==value){
												return '调整邻区方位角';
											}else if(11==value){
												return '优化主小区天线权值';
											}
										}
										return value;
									}
								">优化建议</th>
	    						<th data-options="field:'fileName',width:60,align:'center',formatter:showTooltip">测试日志<br/>名称</th>
	    						<th data-options="field:'recSeqNo',hidden:true">recSeqNo</th>
	    						<th data-options="field:'beginLatitude',hidden:true">开始纬度</th>
	    						<th data-options="field:'courseLatitude',hidden:true">中间纬度</th>
	    						<th data-options="field:'endLatitude',hidden:true">结束纬度</th>
	    						<th data-options="field:'beginLongitude',hidden:true">开始经度</th>
	    						<th data-options="field:'courseLongitude',hidden:true">中间经度</th>
	    						<th data-options="field:'endLongitude',hidden:true">结束经度</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<c:forEach items="${embbCoverRoads}" var="embbCoverRoad">
	    						<tr>
		    						<td>${embbCoverRoad.id}</td>
		    						<td>${embbCoverRoad.roadName}</td>
		    						<td>${embbCoverRoad.startTimeString}</td>
		    						<td>${embbCoverRoad.continueTimeString}</td>
		    						<td>${embbCoverRoad.distance}</td>
		    						<td>${embbCoverRoad.rsrpAvg}</td>
		    						<td>${embbCoverRoad.rsrpMin}</td>
		    						<td>${embbCoverRoad.sinrAvg}</td>
		    						<td>${embbCoverRoad.sinrMin}</td>
		    						<td>${embbCoverRoad.coverRate}</td>
		    						<td>${embbCoverRoad.cellToNbCellDistanceAvg}</td>
		    						<td>${embbCoverRoad.tiankuiRevDistance}</td>
		    						<td>${embbCoverRoad.reasonTypeNum}</td>
		    						<td>${embbCoverRoad.reasonTypeNum}</td>
		    						<td>${embbCoverRoad.testLogItem.fileName}</td>
		    						<td>${embbCoverRoad.testLogItem.recSeqNo}</td>
		    						<td>${embbCoverRoad.beginLatitude}</td>
		    						<td>${embbCoverRoad.courseLatitude}</td>
		    						<td>${embbCoverRoad.endLatitude}</td>
		    						<td>${embbCoverRoad.beginLongitude}</td>
		    						<td>${embbCoverRoad.courseLongitude}</td>
		    						<td>${embbCoverRoad.endLongitude}</td>
		    					</tr>
	    					</c:forEach>
	    				</tbody>
	    			</table>
	    			<div id="tt2" >
						<a href="#" style="width:60px;text-decoration:underline;" onclick="doAnalysis();" title="重叠覆盖路段分析">确认分析</a>
						<a id="tt2_1" href="#" style="width:45px;text-decoration:underline;display:none;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
						<a id="tt2_2" href="#" style="width:30px;text-decoration:underline;display:none;" onclick="redoAnalysis();" title="返回">返回</a>
					</div>
	    		</div>
	    		<div data-options="region:'south',border:false" style="height:40%;">
	    		
	    			<div id="southLayout0" class="easyui-layout" style="width:100%;height: 100%;">
						<div data-options="region:'center',border:false" style="padding:2px 0px 0px 0px;">
							<div class='easyui-panel' data-options="fit:true,title:'重叠覆盖路段占比分析'">
								<div id="roadRateChart" style="width:100%;height:100%;">
								
								</div>
							</div>
						</div>
	    			</div>
	    			
	    			<div id="southLayout1" class="easyui-layout" style="width:100%;height: 100%;display:none;">
						<div data-options="region:'center',border:false" style="padding:2px 0px;">
							<div class='easyui-panel' data-options="fit:true,title:'覆盖小区详表'">
								<table id="embbCoverCellTable" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,rownumbers:true,singleSelect:true,remoteSort:false,multiSort:true,border:false"  >
				    				<thead>
				    					<tr>
											<th data-options="field:'cellName',width:80,align:'center',formatter:showTooltip">小区友好名</th>
				    						<th data-options="field:'cellId',width:80,align:'center',formatter:showTooltip">小区CELLID	</th>
				    						<th data-options="field:'rsrpAvg',width:30,align:'center',formatter:showTooltip">RSRP<br/>均值<br/>(dBm)</th>
				    						<th data-options="field:'rsrpMin',width:45,align:'center',formatter:numDivide1000Formatter">RSRP<br/>最低值<br/>(dBm)</th>
				    						<th data-options="field:'sinrAvg',width:30,align:'center',formatter:numToFixed2Formatter">SINR<br/>均值<br/></th>
				    						<th data-options="field:'sinrMin',width:45,align:'center',formatter:numToFixed2Formatter">SINR<br/>最低值<br/></th>
				    						<th data-options="field:'beamIndex',width:30,align:'center',formatter:formatBeamInfo">波束<br/>指标</th>
				    						<th data-options="field:'coverRate',width:45,align:'center',formatter:numToFixed2Formatter">重叠覆盖<br/>占比<br/>(%)</th>
				    						<th data-options="field:'distanceAvg',width:30,align:'center',formatter:numToFixed2Formatter">持续<br/>距离<br/>(m)</th>
				    						<th data-options="field:'distanceRate',width:60,align:'center',formatter:numToFixed2Formatter">重叠覆盖<br/>路段持续<br/>距离占比(%)</th>
				    						<th data-options="field:'testTime',width:30,align:'center',formatter:numToFixed2Formatter">测试<br/>时长<br/>(s)</th>
				    					</tr>
				    				</thead>
				    			</table>
							</div>
						</div>
						<div data-options="region:'south',border:false" style="height:40%;padding:2px 0px 0px 0px;">
							<div class='easyui-panel' data-options="fit:true,title:'优化建议详表'">
								<table id="embbCoverOptimizeAdviceTable"></table>
							</div>
						</div>
	    			</div>
	    		</div>
	    	</div>
	    </div>
	    <div id="beamInfoWindow" class="easyui-dialog" data-options="width:500,height:200,title:'波束详情',closed:true"><table id="beamInfoTable"></table></div>
		<div id="nbInfoWindow" class="easyui-dialog" data-options="width:300,height:200,title:'缺失的邻区',closed:true"><table id="nbInfoTable"></table></div>
	</body>
</html>
