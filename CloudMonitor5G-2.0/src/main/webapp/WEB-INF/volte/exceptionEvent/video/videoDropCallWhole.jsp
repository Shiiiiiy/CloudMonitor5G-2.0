<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLte视频掉话整体分析</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../../taglibs/easyui.jsp"%>
	<%@ include file="../../../../taglibs/echarts.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
		$.ajaxSetup({async:false});
		//存储日志id,按","分隔
		var testLogItemIds = '${testLogItemIds}';
		//gisCommon会调用获取测试日志ID
		function getTestLogItemIds2EE(){
			return testLogItemIds;
		}
		//gisCommon会调用获取异常事件类型
		function getEEType(){
			//异常事件类型(0语音未接通,1语音掉话,2IMS注册失败,3CSFB失败,4视频未接通,5视频掉话)
			return 5;
		}
		//gisCommon会调用获取事件图标类型
		function getIconType(){
			//异常事件图标类型(见数据库表定义中事件类型定义)
			return '24,28';
		}
		//gisCommon会调用获取生成测试日志下所有类型质差路段轨迹的url
		function getTLI2EEGpsPointActionUrl(){
			return "<%=basePath%>gisSql/queryEEGpsPoint.action";
		}
		$(function(){
			if(testLogItemIds){
				initTable();
			}
		});
		
		function initTable(){
			$.post("${pageContext.request.contextPath}/videoDropCall/doWholeAnalysis.action",{},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#wholeIndexTable0").datagrid('loadData',result.wholeIndex0);
						$("#wholeIndexTable1").datagrid('loadData',result.wholeIndex1);
					}
				}
			,"json");
		}
		
		function hiddenLayout(){
			$("#mainLayout").layout("collapse","east");
		}
	</script>
	
  </head>
  
  <body id="mainLayout" class="easyui-layout" style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 中,地图界面 -->
    	<div  data-options="region:'center',border:false" style="padding:0px 2px 0px 0px;">
    		<div class="easyui-panel" data-options="fit:true">
    			<iframe id="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=8" scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
    		</div>
    	</div>
    	<!-- 东,指标列表和汇总统计信息 -->
    	<div data-options="region:'east',border:false" style="width:55%;padding:0px 0px 0px 2px;">
    		<div class="easyui-layout" style="width:100%;height: 100%;">
    			<!-- 指标列表 -->
    			<div data-options="region:'north',border:false" style="height:92px;padding:0px;">
	    			<table id="wholeIndexTable0"  class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,title:'指标列表',tools:'#tt'" >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'videoconnectsuccessratio',width:80,align:'center',formatter:numToFixed2Formatter">VOLTE视频主<br>叫建立成功率</th>
	    						<th data-options="field:'videoattemptmocount',width:80,align:'center'">视频主叫<br>请求次数</th>
	    						<th data-options="field:'videoconnectmocount',width:80,align:'center'">视频主叫<br>建立次数</th>
	    						<th data-options="field:'videoconnectdelay',width:100,align:'center'">视频主叫建<br>立时延(ms) </th>
	    						<th data-options="field:'videoattemptmtcount',width:100,align:'center'">视频被叫<br>请求次数 </th>
	    						<th data-options="field:'videoconnectmtcount',width:100,align:'center'">视频被叫<br>建立次数 </th>
	    						<th data-options="field:'rsrpaverage',width:80,align:'center',formatter:numToFixed2Formatter">平均RSRP</th>
	    						<th data-options="field:'sinraverage',width:80,align:'center',formatter:numToFixed2Formatter">平均SINR</th>
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
		    					<td></td>
	    					</tr>
	    				</tbody>
	    			</table>
	    			<div id="tt" >
						<a href="#" style="width:40px;text-decoration:underline;" onclick="hiddenLayout();" title="地图最大化">最小化</a>
					</div>
		    	</div>
		    	<div data-options="region:'center',border:false" >
		    		
				    		<div class="easyui-panel" data-options="fit:true,title:'视频掉话统计'">
	    						<table id="wholeIndexTable1" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true,border:false"  >
				    				<thead>
				    					<tr>
				    						<th data-options="field:'eventNum',width:60,align:'center'">视频掉话数量</th>
				    						<th data-options="field:'cellNumRatio',width:50,align:'center',formatter:numToFixed2Formatter">涉及小区占比</th>
				    						<th data-options="field:'terminalNumRatio',width:60,align:'center',formatter:numToFixed2Formatter">涉及终端数占比</th>
				    					</tr>
				    				</thead>
				    				<tbody>
				    					<tr><td></td><td></td><td></td></tr>
				    				</tbody>
				    			</table>
				    		</div>
				    	</div>
				    </div>
			    </div>	
		   
  </body>
</html>