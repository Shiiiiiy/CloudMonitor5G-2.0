<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>统计分析-5G对比分析-栅格统计</title>

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
	var compareTestLogItemIds = '${compareTestLogItemIds}';

		//gisCommon会调用获取测试日志ID
		function getGridTestLogItemIds(){
			return testLogItemIds;
		}
		//gisCommon会调用获取对比测试日志ID
		function getGridCompareTestLogItemIds(){
			return compareTestLogItemIds;
		}
		//gisCommon会调用获取栅格渲染的url
		function getCompareGridActionUrl(){
			return "<%=basePath%>gisSql/queryTestLogItemGrid.action";
		}
	
	var everyIndexLatticeCellBarChart;
	$(function(){
		if(testLogItemIds){
			initTable();
		}
		window.setTimeout(function(){
			initChart();
		},2000);
	});
	
	//重新加载
	window.onresize=reload;
	function reload(){
		self.window.setTimeout(function(){
			everyIndexLatticeCellBarChart.resize();
		}, 100);
	}
 	function initTable(){
		$.post("${pageContext.request.contextPath}/compareAnalysis5g/doLatticeCellAnalysis.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					if(0!=result.testLogItemGrid0.rows.length){
						$("#latticeCellTable").datagrid('loadData',result.testLogItemGrid0);
						//everyIndexLatticeCellBarOption.series[0].data[0]=result.testLogItemGrid0.rows[0].betterNumRatio;
						//everyIndexLatticeCellBarOption.series[0].data[1]=result.testLogItemGrid0.rows[1].betterNumRatio;
						//everyIndexLatticeCellBarOption.series[0].data[2]=result.testLogItemGrid0.rows[2].betterNumRatio;
						//everyIndexLatticeCellBarOption.series[0].data[3]=result.testLogItemGrid0.rows[3].betterNumRatio;
						//everyIndexLatticeCellBarOption.series[1].data[0]=result.testLogItemGrid0.rows[0].badNumRatio;
						//everyIndexLatticeCellBarOption.series[1].data[1]=result.testLogItemGrid0.rows[1].badNumRatio;
						//everyIndexLatticeCellBarOption.series[1].data[2]=result.testLogItemGrid0.rows[2].badNumRatio;
						//everyIndexLatticeCellBarOption.series[1].data[3]=result.testLogItemGrid0.rows[3].badNumRatio;
						//去掉mos
						everyIndexLatticeCellBarOption.series[0].data[0]=result.testLogItemGrid0.rows[0].betterNumRatio;
						everyIndexLatticeCellBarOption.series[0].data[1]=result.testLogItemGrid0.rows[1].betterNumRatio;
						everyIndexLatticeCellBarOption.series[0].data[2]=result.testLogItemGrid0.rows[2].betterNumRatio;
						everyIndexLatticeCellBarOption.series[1].data[0]=result.testLogItemGrid0.rows[0].badNumRatio;
						everyIndexLatticeCellBarOption.series[1].data[1]=result.testLogItemGrid0.rows[1].badNumRatio;
						everyIndexLatticeCellBarOption.series[1].data[2]=result.testLogItemGrid0.rows[2].badNumRatio;
					}
				}
			}
		,"json");
	} 

		/* 各栅格指标变化情况占比柱状图option */
		var	everyIndexLatticeCellBarOption = {
			tooltip : {trigger: 'axis',formatter: "{b}:<br/>{a}:{c}%<br/>{a1}:{c1}%"}, 
			grid :{  x:'10%',y:'10%',x2:'10%',height:'70%'},
		    legend: {orient : 'vertical',x : 'center',y:'bottom',data:['指标提升栅格占总涉及栅格数占比','指标恶化栅格占总涉及栅格数占比']},
		    xAxis : [{type:'category',data:['RSRP', 'SINR', 'PUSCCH发射功率'],name:'指标类型',axisLabel:{interval:0}}],
		    yAxis : [{type:'value',min:0,name:'栅格占比(%)'}],
		    toolbox: {show : true,
				feature : {
		            magicType: {show : false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
			},
		    series : [
		    	//{name:'指标提升栅格占总涉及栅格数占比',barGap:'20%',barCategoryGap:'50%',itemStyle: {normal: {color:'#76b753'}},type:'bar',data:['-','-','-','-']},
		    	//{name:'指标恶化栅格占总涉及栅格数占比',barGap:'20%',barCategoryGap:'50%',itemStyle: {normal: {color:'#ea6c53'}},type:'bar',data:['-','-','-','-']}
		    	//去掉MOS
		    	{name:'指标提升栅格占总涉及栅格数占比',barGap:'20%',barCategoryGap:'50%',itemStyle: {normal: {color:'#76b753'}},type:'bar',data:['-','-','-']},
		    	{name:'指标恶化栅格占总涉及栅格数占比',barGap:'20%',barCategoryGap:'50%',itemStyle: {normal: {color:'#ea6c53'}},type:'bar',data:['-','-','-']}
		    ]
		};
		/* Chart路径配置 */
        require.config({
            paths: {echarts: '${pageContext.request.contextPath}/js/echarts-2.2.1/build/dist'}
        });
		/* 初始化图表 */
        function initChart(){
			require(
				['echarts','echarts/chart/bar'],
				function (ec) {
					everyIndexLatticeCellBarChart = ec.init(document.getElementById("everyIndexLatticeCellBarChart")); 
					everyIndexLatticeCellBarChart.setOption(everyIndexLatticeCellBarOption);
				}
			);
	    }
	
	/* 地图最大化 */
	function hiddenLayout(){
		$("#mainLayout").layout("collapse","east");
	}
	</script>
  </head>
  <body class="easyui-layout" id="mainLayout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" style="width: 100%;height: 100%;" data-options="fit:true">
    			<iframe name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=60" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和MOS差黑点统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
		    	<!-- MOS差黑点指标列表 -->
		    	<div data-options="region:'north',border:false" style="height:152px;padding:0px 0px 4px 0px;">
  					<table id="latticeCellTable"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'栅格指标变化情况',tools:'#tt'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'indexType',width:90,align:'center'">指标类型</th>
	    						<th data-options="field:'totalNum',width:90,align:'center'">总栅格数</th>
	    						<th data-options="field:'betterNum',width:80,align:'center'">提升栅格数</th>
	    						<th data-options="field:'badNum',width:80,align:'center'">恶化栅格数</th>
	    						<th data-options="field:'valueAvg',width:100,align:'center',formatter:numToFixed2Formatter">原始日志<br>指标均值</th>
	    						<th data-options="field:'compareValueAvg',width:80,align:'center',formatter:numToFixed2Formatter">对比日志<br>指标均值</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    					<tr><td>RSRP</td><td></td><td></td><td></td><td></td><td></td></tr>
	    					<tr><td>SINR</td><td></td><td></td><td></td><td></td><td></td></tr>
	    					<tr><td>PUSCCH发射功率</td><td></td><td></td><td></td><td></td><td></td></tr>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	
				<div data-options="region:'center',border:false" >
					<div id="everyIndexLatticeCellBarChart" class="easyui-panel" data-options="title:'栅格指标变化情况柱状图',fit:true">
						
					</div>
				</div>
		    </div>
    	</div>
	</body>
</html>
