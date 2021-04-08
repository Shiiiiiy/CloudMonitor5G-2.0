<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>测试轨迹页面</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%@ include file="../../../gis/FGreportTestManage.jsp" %>
	<link href="${pageContext.request.contextPath}/gis/css/Style.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	$(function(){
		$('#tt').tabs({
			tools:[{
				iconCls:'icon-batchexcel',
				text:'导出',
				handler:function(){
					goToPage('${pageContext.request.contextPath}/report5g/downloadTestTrailTotal.action');
				}
			}]
		});
		var t1 = window.setInterval(function(){
    		if(mapIframe.window.map){
				mapIframe.window.hideDivByDivId('cellBoshuAnalyseButton');
				mapIframe.window.hideDivByDivId('DisplayCleLPoint');
				mapIframe.window.hideDivByDivId('stationLine');
			
				if(sheetName=="NR RSRP"){
		            paramName="nrRsrp";
		       	}else if(sheetName=="LTE RSRP"){
		       		paramName="lteRsrp";
		       	}else if(sheetName=="NR SINR"){
		       		paramName="nrSinr";
		       	}else if(sheetName=="LTE SINR"){
		       		paramName="lteSinr";
		       	}else if(sheetName=="FTP下行速率"){
		       		paramName="nrMacthrputdl";
		       	}else if(sheetName=="FTP上行速率"){
		       		paramName="nrMacthrputul";
		       	}
				$.ajax({
		                type:"GET",
		                url:"${pageContext.request.contextPath}/report5g/getGpsPointData.action",
		                data:{'allLogNames':allLogNames},
		                dataType:"json",//服务器响应的数据类型
		                success:function(data){
		                	pointList = data.pointList;
		                	mapIframe.window.stationSAMTralFunc(data.pointList,paramName);
		                	showRoadPointsData = pointList;
		                	boShuColor = paramName;
		                }
		            });
	        	window.clearInterval(t1);
	        	
	        	initTable();
    		}
    	},1000);
		
	});
	var sheetName="NR RSRP";
	var paramName="";
	var pointList=null;
	function initTable(){
		
		$.post("${pageContext.request.contextPath}/report5g/quaryTrailKpi.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#Table1").datagrid('loadData',result.rsrpKpi);
					$("#Table2").datagrid('loadData',result.rsrpKpi);
					$("#Table3").datagrid('loadData',result.sinrKpi);
					$("#Table4").datagrid('loadData',result.sinrKpi);
					$("#Table5").datagrid('loadData',result.ftpDLRateKpi);
					$("#Table6").datagrid('loadData',result.ftpULRateKpi);
				}
			}
		,"json");
	}
	//获取Sheet名
	function sele(title,index){
		sheetName=title;
		if(sheetName=="NR RSRP"){
            paramName="nrRsrp";
       	}else if(sheetName=="LTE RSRP"){
       		paramName="lteRsrp";
       	}else if(sheetName=="NR SINR"){
       		paramName="nrSinr";
       	}else if(sheetName=="LTE SINR"){
       		paramName="lteSinr";
       	}else if(sheetName=="FTP下行速率"){
       		paramName="nrMacthrputdl";
       	}else if(sheetName=="FTP上行速率"){
       		paramName="nrMacthrputul";
       	}
       	var t1 = window.setInterval(function(){
    		if(mapIframe.window.map){
				mapIframe.window.stationSAMTralFunc(pointList,paramName);
				showRoadPointsData = pointList;
            	boShuColor = paramName;
	        	window.clearInterval(t1);
    		}
    	},1000);
       	/* $.ajax({
              type:"GET",
              url:"${pageContext.request.contextPath}/report5g/getGpsPointData.action",
              data:{'allLogNames':allLogNames},
              dataType:"json",//服务器响应的数据类型
              success:function(data){
              	mapIframe.window.stationEtgTralFunc(data.pointList,paramName);
              }
		}); */
	}
	
	//存储日志id,按","分隔
	var testLogItemIds = '${ sessionScope.allTestLogItemIds}';
	//var testLogItemIds = '1974';
	var allLogNames = '${ sessionScope.allLogNames}';
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
		return null;
	}
	
	//存储质差路段id
	var embbrid;
	//gisCommon会调用获取质差路段ID
	function getGpsPointQBRId(){
		return 5;
	}
	//gisCommon会调用获取生成质差路段轨迹的url
	function getQBRGpsPointActionUrl(){
		return "<%=basePath%>gisSql/queryEmbbCoverRoadPoints.action";
	}
	
	//gisCommon会调用获取质差路段ID
	function getGpsPointCWBRId(){
		return 5;
	}
	//gisCommon会调用获取小区与邻区连线的url
	function CWBRCellToCellActionUrl(){
		return "<%=basePath%>gisSql/queryCWBRCellToCell.action";
	}
	
	function formatValue(value){
	 	if(value!=null){
		 	if(value!=0){
		 		value =  value+"%";
		 	}
	 	}
	 	return value;
	 }
	 
	</script>
  </head>
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
  	<div style="width:100%;height:60%;border:2px;margin: 5px 5px 5px 5px;">
	  	<iframe id="mapIframe" name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=102"
        scrolling="auto" frameborder="0"
        style="width:100%;height:100%;border:2px;margin: 5px 5px 5px 5px;"></iframe>
    </div>
    
    <div class="easyui-layout" style="width:100%;height:39%;">    
	   	<div id="tt"class="easyui-tabs" data-options="region:'south',fit:false,onSelect:sele" style="width:100%;height:100%;padding:4px;top:4px;left:4px;">
			<div title="NR RSRP" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<table id="Table1" class="easyui-datagrid" data-options="scrollbarSize:20,striped:true,fitColumns:true,singleSelect:true,fit:true"  >
	   				<thead>
	   					<tr>
	   						<th data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
	   						<th data-options="field:'rsrp110less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">＜-110</th>
							<th data-options="field:'rsrp110to105less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-110,-105)</th>
							<th data-options="field:'rsrp105to100less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-105,-100)</th>
							<th data-options="field:'rsrp100to95less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-100,-95)</th>
							<th data-options="field:'rsrp95to85less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-95,-85)</th>
							<th data-options="field:'rsrp85to75less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-85,-75)</th>
							<th data-options="field:'rsrp75to40less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-75,-40)</th>
	   					</tr>
	   				</thead>
	   			</table>
			</div>
			<div title="LTE RSRP" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
						<table id="Table2" class="easyui-datagrid" data-options="scrollbarSize:20,striped:true,fitColumns:false,singleSelect:true,fit:true"  >
			   				<thead>
			   					<tr>
			   						<th data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
			   						<th data-options="field:'rsrp110less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">＜-110</th>
									<th data-options="field:'rsrp110to105less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-110,-105)</th>
									<th data-options="field:'rsrp105to100less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-105,-100)</th>
									<th data-options="field:'rsrp100to95less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-100,-95)</th>
									<th data-options="field:'rsrp95to85less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-95,-85)</th>
									<th data-options="field:'rsrp85to75less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-85,-75)</th>
									<th data-options="field:'rsrp75to40less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-75,-40)</th>
			   					</tr>
			   				</thead>
			   			</table>
			</div>
			<div title="NR SINR" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
						<table id="Table3" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true,fit:true"  >
			   				<thead>
			   					<tr>
			   						<th data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
			   						<th data-options="field:'sinr3less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">＜-3</th>
									<th data-options="field:'sinr3to0less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-3,0)</th>
									<th data-options="field:'sinr0to3less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[0,3)</th>
									<th data-options="field:'sinr3to6less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[3,6)</th>
									<th data-options="field:'sinr6to9less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[6,9)</th>
									<th data-options="field:'sinr9to15less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[9,15)</th>
									<th data-options="field:'sinr15more',align:'center',formatter:function(value,row,index){ return formatValue(value);}">≥15</th>
	
			   					</tr>
			   				</thead>
			   			</table>
			</div>
			<div title="LTE SINR" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
						<table id="Table4" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,fit:true"  >
			   				<thead>
			   					<tr>
			   						<th data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
			   						<th data-options="field:'sinr3less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">＜-3</th>
									<th data-options="field:'sinr3to0less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[-3,0)</th>
									<th data-options="field:'sinr0to3less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[0,3)</th>
									<th data-options="field:'sinr3to6less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[3,6)</th>
									<th data-options="field:'sinr6to9less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[6,9)</th>
									<th data-options="field:'sinr9to15less',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[9,15)</th>
									<th data-options="field:'sinr15more',align:'center',formatter:function(value,row,index){ return formatValue(value);}">≥15</th>
			   					</tr>
			   				</thead>
			   			</table>
			</div>
			<div title="FTP下行速率" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
						<table id="Table5" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,fit:true"  >
			   				<thead>
			   					<tr>
			   						<th data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
			   						<th data-options="field:'ftp0to10dl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[0,10)</th>
									<th data-options="field:'ftp10to40dl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[10,40)</th>
									<th data-options="field:'ftp40to100dl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[40,100)</th>
									<th data-options="field:'ftp100to200dl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[100,200)</th>
									<th data-options="field:'ftp200to400dl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[200,400)</th>
									<th data-options="field:'ftp400to800dl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[400,800)</th>
									<th data-options="field:'ftp800to1600dl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[800,1600)</th>
									<th data-options="field:'ftp1600moredl',align:'center',formatter:function(value,row,index){ return formatValue(value);}">≥1600M</th>
			   					</tr>
			   				</thead>
			   			</table>
			</div>
			<div title="FTP上行速率" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
						<table id="Table6" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,fit:true"  >
			   				<thead>
			   					<tr>
			   						<th data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
			   						<th data-options="field:'ftp0to10ul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[0,1)</th>
									<th data-options="field:'ftp1to6ul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[1,6)</th>
									<th data-options="field:'ftp6to9ul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[6,9)</th>
									<th data-options="field:'ftp9to15ul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[9,15)</th>
									<th data-options="field:'ftp15to20ul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[15,20)</th>
									<th data-options="field:'ftp20to100ul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[20,100)</th>
									<th data-options="field:'ftp100to200ul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">[100,200)</th>
									<th data-options="field:'ftp200moreul',align:'center',formatter:function(value,row,index){ return formatValue(value);}">≥200M占比</th>
			   					</tr>
			   				</thead>
			   			</table>
			</div>
		</div>
	</div>
</body>
</html>
