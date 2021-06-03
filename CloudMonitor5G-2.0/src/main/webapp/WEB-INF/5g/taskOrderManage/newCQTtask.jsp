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
    <title>新增定点测试任务</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%@ include file="../../../gis/FGreportTestManage.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js"></script>
	<style type="text/css">
		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 30%;
   		}
   		.inputDivShow input{
   			width:180px;
   		}
   		.inputDivShow select{
   			width:180px;
   		}
   		
	</style>
	<script type="text/javascript">
		
		var cityId = '${cityId}';
		
		$(function(){
			$.post('${pageContext.request.contextPath}/testTaskManage/getInitCellInfo.action?cityId='+cityId, {},
					function(result) {
						initSelect(result.lteList,"",'occupyLteCell');
						initSelect(result.nrList,"",'occupyNrCell');
					}
			,'json');
			
			$.post('${pageContext.request.contextPath}/testTaskManage/getInitTerminal.action?cityId='+cityId, {},
					function(result) {
						initSelect(result.tlList,"",'terminalId');
					}
			,'json');
			
			var t1 = window.setInterval(function(){
	    		if(mapIframe.window.map){
					setTimeout("initCellLayers()",1000);  
					window.clearInterval(t1);
	    		}
	    	},1000);
	    	
		});
		
		/*填充版本下拉框*/
		function initSelect(ugList,nameList,id){
			var data = [];
			for(var i = 0;i < ugList.length;i++){
				var oneUp = {};
				oneUp.id = ugList[i].name;
				oneUp.name = ugList[i].name;
				if(nameList.indexOf(ugList[i].name)!=-1){
					oneUp.selected = true;
				}
				data.push(oneUp);
			}
		    $("#"+id+"").combobox({
				valueField: 'id', 
				textField: 'name',
				data: data,
				filter: function(q, row){
					var opts = $(this).combobox('options');
					return row[opts.textField].toLowerCase().indexOf(q.toLowerCase())>-1;
				}
		    }); 
		}
		
		function initCellLayers(){
			mapIframe.window.hideDivByDivId('BoshuAnalyseLi');
			mapIframe.window.hideDivByDivId('DisplayCleLPoint');
			mapIframe.window.hideDivByDivId('stationLine');
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/testTaskManage/queryShpname.action",
	             data: {'cityId':cityId},
	             dataType: "json",
	             success: function(data){
	             	console.info(data);
					//5g图层加载
					var arrsNr = ['GNBID','CELLID','SITENAME','CELLNAME','TAC','PCI','FREQUENCY1','LONGITUDE','LATITUDE','TILTM','AZIMUTH','HEIGHT','CITY'];
					if(data.shpName_nr != null){
						mapIframe.window.StationAddCellToMap(data.shpName_nr,true,data.lon_nr,data.lat_nr,arrsNr);
					}
					//4g图层加载
					var arrsLte = ['ENBID','CELLID','SITE_NAME','CELLNAME','TAC','PCI','FREQUENCY1','LONGITUDE','LATITUDE','TILT_M','AZIMUTH','HEIGHT','CITY'];
					if(data.shpName_lte != null){
					    var F4ShpName = data.shpName_lte.substring(0, data.shpName_lte.indexOf("."));
						mapIframe.window.F4ShpName = [F4ShpName];
						mapIframe.window.StationAddCellToMap(data.shpName_lte,true,data.lon_lte,data.lat_lte,arrsLte);
					}
					var lon = data.lon_nr;
					var lat = data.lat_nr;
					var zoom = setTimeout(function(){ 
						mapIframe.window.mapCenterAndZoom2(lon,lat);
					},2000); 
              	}
	         });
		}
		
		function saveTask(){
			
			$("#dataForm").form('submit',{
				onSubmit : function(param) {
					var isValid = $(this).form('validate');
					if(isValid){
						param.cityId = cityId;
					}
					return isValid;
				},
				success : function(result) {
					var resultData =$.parseJSON(result);
					if (resultData.errorMsg) {
						$.messager.alert("系统提示", resultData.errorMsg,"error");
						return;
					} else {
						$.messager.alert("系统提示", "保存成功","info");
						parent.centerLayoutGoToPage('【'+resultData.cityName+'】定点测试任务列表','${pageContext.request.contextPath}/testTaskManage/testTaskListUI.action?cityId='+cityId);
					}
				}
			});
		}
		
		function inputSelctLocation(lon,lat){
			$("#testPointLon").numberbox("setValue",lon);
			$("#testPointLat").numberbox("setValue",lat);
		}
		
		/* 重置表单 */
		function resetForm(){
			var workOrderId  = $("#workOrderId").val();
			$("#dataForm").form('reset');
			$("#workOrderId").val(workOrderId);
		}
		
		function getCellTestLogItemIds(){
			return "";
		}
		//gisCommon会调用获取生成小区SQL的url
		function getCellActionUrl(){
			return "<%=basePath%>gisSql/queryCellInfo.action";
		}
		
	</script>
</head>
  
<body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
		<div data-options="region:'south',border:false" style="width:100%;height:79%;margin-bottom: 5px ">
		  	<iframe id="mapIframe" name="mapIframe" src="${pageContext.request.contextPath}/gis/default.html?toolbarType=104"
	        scrolling="auto" frameborder="0"
	        style="width:100%;height:100%;border:2px;margin: 5px 5px 5px 0px;"></iframe>
	    </div>
	   	<div data-options="region:'north',border:false" style="height:20%;">
		   	<form id="dataForm" class="easyui-form" method="post" action="${pageContext.request.contextPath}/testTaskManage/saveByETG.action">
		   		<div class="inputDivShow" >
	    			占用LTE小区名称：<input id="occupyLteCell" class="easyui-combobox"  name="occupyLteCellName" style="width:250px;"/>
	    		</div>
	    		<div class="inputDivShow" >
	    			测试点经度：<input id="testPointLon" class="easyui-numberbox"  name="testPointLon" data-options="required:true,min:-180,max:180,precision:6"/>
	    		</div>
	    		<div class="inputDivShow" >
	    			终端ID：
	    			<input id="terminalId" name="boxId" class="easyui-combobox" data-options="validType:'length[1,24]',required:true,editable:false" />
	    		</div>
	    		<div class="inputDivShow">
	    			占用NR小区名称：
	    			<input id="occupyNrCell" class="easyui-combobox"  name="occupyNrCellName" style="width:250px;"/>
	    		</div>
	    		<div class="inputDivShow" >
	    			测试点纬度：<input id="testPointLat" class="easyui-numberbox"  name="testPointLat" data-options="required:true,min:-90,max:90,precision:6"/>
	    		</div>
	    		<div class="inputDivShow"> 
	    			任务时限：
					<select id="taskTimeLimitSelect" class="easyui-combobox" name="taskTimeLimit"  data-options="required:true,editable:false">
						<option value="">&nbsp;</option>
				        <option value="24小时" >24小时</option>
				        <option value="48小时" selected="true">48小时</option>
				        <option value="72小时">72小时</option>
				        <option value="96小时">96小时</option>
				    </select>
	    		</div>

				<div class="inputDivShow" >
					执行时间
					<input style="width:100px"  class="easyui-timespinner" data-options="required:true" value="00:00" name="beginTime"  id="beginTime" />-<input style="width:100px"  class="easyui-timespinner" data-options="required:true" name="endTime" value="23:59" id="endTime"  />
				</div>
				
				<div class="inputDivShow" >
	    			备注：<input id="comment" class="easyui-textbox"  name="comment"/>
	    		</div>
	    		
	    		<input id="workOrderId" name="workOrderId" type="hidden" value="${newWorkOrderid}"/>
	    		
				<div style="text-align: center;">
	    			<a class="easyui-linkbutton" onclick="saveTask();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-ok'" >保存</a>
	    			<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-reload'">重置</a>
		    	</div>
	    	</form>
	  	</div>
  </body>
</html>