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
    <title>单验报告查看</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%@ include file="../../../gis/layerManageReport.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js"></script>
	
	<style type="text/css">
		.titleDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 180px;
   		}
   		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    padding-left: 0;
		    padding-right: 0;
		     margin: 5px;
		    text-align: right;
		    width: 255px;
   		}
	</style>
	<script type="text/javascript">
		
		$(function(){
			initTable();
			$('#mapWindowId').window('close');
		});
		var city = "全部";
		
		/*填充名称下拉框*/
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
		
		function colorFunc(value,leftValue){
			if(value == 1){
				return "<div style='height:20px;width:20px;background: #00B050;margin-left:"+leftValue+"px;'></div>";
			}else if(value == 2){
				return "<div style='height:20px;width:20px;background: #FFFF00;margin-left:"+leftValue+"px;'></div>";
			}else if(value == 0){
				return "<div style='height:20px;width:20px;background: red;margin-left:"+leftValue+"px;'></div>"
			}
		}
		
		
		
		/* 初始化测试计划列表 */
		function initTable(){
			$("#stationLogTable").datagrid({
				// 表头
				columns:[[
					{field:'id',hidden:true},
					{field:'reportName',width:100,align:'center',title:'单站报告名称'}, 
					{field:'siteName',width:100,align:'center',title:'SiteName'}, 
					{field:'region',width:100,align:'center',title:'区域'},
					{field:'reportCreatDate',width:140,align:'center',title:'报告生成时间'}]],
				url:'${pageContext.request.contextPath}/stationReportShow/doPageListJson.action',
				border:false,
				singleSelect:false,
				fitColumns:true,
				pagination:true,
				pageSize:20,
				pageList:[20,50,100,200,500],
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				//singleSelect:true,
				toolbar:'#tb',
				onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
		            
		        },
		        onClickRow: function(rowIndex, rowData){
		        	var nodes = $('#stationLogTable').datagrid('getSelections');
		        	if(nodes.length > 0){
			        	$('#showMapId').linkbutton('enable');		        		
		        	}else{
		        		$('#showMapId').linkbutton('disable');
		        	}
		        }
			});
		}
	
		/* 多条件查询 */
		function pageQuery(){
			if($("#queryForm").form('validate')){
				//获取cityIds请求参数
				var areaTree = $("#areaTree").combotree('tree');
				var checkNodes = areaTree.tree('getChecked');
				var cityIds = [];
				for (var int = 0; int < checkNodes.length; int++) {
					if(checkNodes[int].attributes.type=='City'){
						cityIds.push(checkNodes[int].text);
					}
				}
				var cityIdsString = cityIds.join(",");
				
				var startTime = $("#testTimeStartQuery").datebox('getValue');
				var endTime = $("#testTimeEndQuery").datebox('getValue');
				if(startTime != null && startTime != ""){
					startTime = startTime.replace(/-/g,"")
				}
				if(endTime != null && endTime != ""){
					endTime = endTime.replace(/-/g,"")
				}
				var siteName = $("#SiteNameQuery").textbox('getValue');
				var reportName = $("#reportNameQuery").textbox('getValue');
				$("#stationLogTable").datagrid('load',{
					cityNamesStr:cityIdsString,
					startTime:startTime,
					endTime : endTime,
					siteName : siteName,
					reportName : reportName
				});
			}
		}
		
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
		}
		
		var arrs = ['GNBID','CELLID','SITENAME','CELLNAME','TAC','PCI','FREQUENCY1','LONGITUDE','LATITUDE','TILTM','AZIMUTH','HEIGHT','CITY'];
		var sitename= '';
		var cityName = '';
		
		/*地图轨迹*/
		function showMap(){
			var nodes = $('#stationLogTable').datagrid('getSelections');
			sitename= '';
			cityName = '';
			
			if(nodes.length == 1){
				sitename = nodes[0].siteName;
				cityName = nodes[0].region;
				var src = $("#mapIframe").attr("src");
				if(!src){
					$("#mapIframe").attr("src", "${pageContext.request.contextPath}/gis/default.html?toolbarType=105");
				}
				
				$('#mapWindowId').window('open');
			}else{
				$.messager.alert('提示','只能选择一个展示地图轨迹的站点','info');
				return;
			}
			
			$.post('${pageContext.request.contextPath}/stationReportShow/getInitTrailKpi.action', {},
					function(result) {
						initSelect(result.tlList,result.tlList[0].name,'eventChoose');
						var t2 = window.setInterval(function(){
		        		if(mapIframe.window.map){
				        		eventChooseFunc();
								/* drawingMap(nodes[0].cellName,nodes[0].id); */
			        			$.ajax({
				       	             type: "GET",
				       	             url: "${pageContext.request.contextPath}/stationCompletionShow/getShpName.action",
				       	             data: {'cityName':nodes[0].region,'cityId':null,'cityType':null},
				       	             dataType: "json",
				       	             success: function(data){
				       					mapIframe.window.StationAddCellToMap(data.shpName,true,data.lon,data.lat,arrs);
				       					mapIframe.window.StationLayerManage();
				                    }
				       	        });
					        	window.clearInterval(t2);
			        		}
			        	},1000);
					}
			,'json');
		}
		
		/*绘制地图*/
		function drawingMap(){
			var colors = [];
			var event = $("#eventChoose").combobox('getValue');
			
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/stationReportShow/getPoint.action",
	             //data: {'cellName':cellName,'id':id,'event':event},
	             data: {'siteName':sitename,'event':event},
	             dataType: "json",
	             success: function(data){
	            	if(data != null){
            			mapIframe.window.stationEtgTralFunc2(data.data,event,false,data.lon,data.lat,data.pciMap);
           			}else{
           				mapIframe.window.stationEtgTralFunc2([],event,false,data.lon,data.lat,data.pciMap);
           			}
              }
	         });
		}
		
		function cellNameChooseFunc(){
			//var id = $("#cellNameChoose").combobox('getValue');
			//var cellName = $("#cellNameChoose").combobox('getText');
			//drawingMap(id,cellName);
			drawingMap();
		}
		
		function eventChooseFunc(){
			var event = $("#eventChoose").combobox('getValue');
			cellNameChooseFunc();
		}
	
		
		/*报告下载*/
		function reportDownload(){
			var nodes = $('#stationLogTable').datagrid('getSelections');
			var ids = '';
			var siteNames = '';
			var flag = false;
			if(nodes.length > 0){
				for(var i = 0; i < nodes.length;i++){
					if(i != 0){
						ids = ids + ',';
						siteNames = siteNames + ',';
					}
					ids = ids + nodes[i].id;
					siteNames = siteNames + nodes[i].siteName;
				}
			}else{
				$.messager.alert('提示','请选择下载报告','info');
				return "";
			}
			window.location= "${pageContext.request.contextPath}/stationReportShow/downloadLog.action?idsStr="+ids;
		}
		
		function deleteReport(){
			var nodes = $('#stationLogTable').datagrid('getSelections');
			var ids = '';
			var siteNames = '';
			var flag = false;
			if(nodes.length > 0){
				for(var i = 0; i < nodes.length;i++){
					if(i != 0){
						ids = ids + ',';
						siteNames = siteNames + ',';
					}
					ids = ids + nodes[i].id;
					siteNames = siteNames + nodes[i].siteName;
				}
			}else{
				$.messager.alert('提示','请选择需要删除的报告','info');
				return "";
			}
			
			$.messager.confirm("系统提示", "确认删除单站报告吗？", function(result) {
				if (result) {
					$.ajax({
			             type: "GET",
			             url: "${pageContext.request.contextPath}/stationReportShow/deleteReport.action",
			             //data: {'cellName':cellName,'id':id,'event':event},
			             data: {'idsStr':ids},
			             dataType: "json",
			             success: function(data){
			            	if(data.errorMsg){
		 	             		$.messager.alert('提示',data.errorMsg,'error');
		 	             	}else{
		 	             		$.messager.alert('提示',"报告已删除",'info');
		 	             		$("#stationLogTable").datagrid("reload");
		 	             	}
		              	}
			         });
				}
			});
			
		}
		
		function getCityName(){
			return cityName;
		}
		
	</script>

  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    <div data-options="region:'center',title:'单站报告列表',split:false,tools:'#tt3'"  style="width:100%;">
    	<table id="stationLogTable"></table>
    	<div id="tb">
	    	<form id="queryForm" class="esyui-form" method="post" >
	    		<div class="inputDivShow">
	    			报告名称：<input id="reportNameQuery" class="easyui-textbox" style="width:125px;"/>
	    		</div>
	    		<div class="inputDivShow">
	    			SiteName：<input id="SiteNameQuery" class="easyui-textbox" style="width:125px;"/>
	    		</div>
	    		<div class="inputDivShow">
	    			报告生成开始日期：<input id="testTimeStartQuery" class="easyui-datebox" style="width:125px;"/>
	    		</div>
	    		<div class="inputDivShow">
	    			报告生成结束日期：<input id="testTimeEndQuery" class="easyui-datebox" style="width:125px;"/>
	    		</div>
	    		<div class="inputDivShow">
	    			区域：
	    			<select id="areaTree" name="cityIds"  class="easyui-combotree" style="width:125px;" data-options="panelWidth:200,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:false,url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action'"  >
			    	</select>
	    		</div>
				<table width="100%">
		    		<tr>
			    		<td width="35%" style="text-align:right">
			    			<shiroextend:hasAnyPermissions name="stationShow:query">
			    				<a class="easyui-linkbutton" onclick="pageQuery();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
			    		<td width="10%" style="text-align:center">
			    			<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a>
			    		</td>
		    			<td width="7%" style="text-align:left">
		    				<shiroextend:hasAnyPermissions name="stationShow:map">
			    				<a class="easyui-linkbutton" onclick="showMap();" id="showMapId" style="width: 80px;" data-options="iconCls:'icon-networkStructure',disabled:true" >地图轨迹</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
		    			<td width="7%" style="text-align:left">
		    				<shiroextend:hasAnyPermissions name="stationShow:download">
			    				<a class="easyui-linkbutton" onclick="reportDownload();" style="width: 80px;" data-options="iconCls:'icon-excel'" >报告下载</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
			    		<td width="35%" style="text-align:left">
		    				<shiroextend:hasAnyPermissions name="stationShow:download">
			    				<a class="easyui-linkbutton" onclick="deleteReport();" style="width: 80px;" data-options="iconCls:'icon-remove'" >删除</a>
			    			</shiroextend:hasAnyPermissions>
			    		</td>
			    	</tr>
		    	</table>
	    	</form>
		</div>
	</div>
	<div id="mapWindowId" class="easyui-window" title="地图轨迹" style="width:950px;height:550px"
        data-options="iconCls:'icon-save',modal:true,resizable:false,maximizable:false">
		<div style="height: 100%;width: 100%;z-index: 90;float:left;">
			<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
		</div>
        <div style="width: 100%;z-index: 100;float:left;margin-top:-470px;height: 20px;">
        	<%-- <div style="width:28%;text-align: center;float:left;">
    			小区选择：
			    <input id="cellNameChoose" class="easyui-combobox" name="dept" data-options="valueField:'id',onSelect:cellNameChooseFunc,
			    	editable:false,textField:'text',url:'${pageContext.request.contextPath}/stationReportShow/getAllCell.action'">
			    <input id="cellNameChoose" class="easyui-combobox" name="dept" data-options="valueField:'id',onSelect:cellNameChooseFunc,
			    	editable:false,textField:'text'">
    		</div> --%>
    		<div style="width:28%;text-align: center;float:left;">
    			指标选择：
    			<input id="eventChoose" class="easyui-combobox" name="eventChoose" data-options="valueField:'id',onSelect:eventChooseFunc,
			    	editable:false,textField:'text'" >
    		</div>
        </div>
	</div>
  </body>
</html>
