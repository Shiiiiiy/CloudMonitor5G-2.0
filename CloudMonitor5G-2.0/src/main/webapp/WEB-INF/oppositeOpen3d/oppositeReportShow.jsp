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
    <title>反开3d报告查看</title>
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
					{field:'id',checkbox:true},
					{field:'siteName',width:100,align:'center',title:'SiteName'}, 
					{field:'enbId',width:100,align:'center',title:'enbId'},
					{field:'cellName',width:100,align:'center',title:'CellName'},
					{field:'localCellId',width:100,align:'center',title:'LocalCellId'},
					{field:'region',width:100,align:'center',title:'区域'},
					{field:'createReportDate',width:140,align:'center',title:'报告生成时间'},
					/* {field:'testEventAllStatus',width:140,align:'center',title:'测试项完成情况'}, */
					{field:'stationProspectRlt',width:140,align:'center',title:'基站勘察结果'},
					{field:'wirelessParamStatus',width:140,align:'center',title:'无线参数一致性'},
					{field:'noPassTestEvent',width:140,align:'center',title:'不通过的测试项'}
				]],
				url:'${pageContext.request.contextPath}/oppositeOpen3dReportCreate/doPageListJson.action',
				border:false,
				fitColumns:false,
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
				
				var testFinishCondition = $("#testFinishCondition").combobox('getValue');
				var wirelseeParamConsistency = $("#wirelseeParamConsistency").combobox('getValue');
				var noPassTestEvent = $("#noPassTestEvent").combobox('getValue');
				var siteName = $("#SiteNameQuery").textbox('getValue');
				var gnbId = $("#gnbIdQuery").numberbox('getValue');
				$("#stationLogTable").datagrid('load',{
					cityNamesStr : cityIdsString,
					startTime : startTime,
					endTime : endTime,
					testEventAllStatus : testFinishCondition,
					wirelessParamStatus : wirelseeParamConsistency,
					noPassTestEvent : noPassTestEvent,
					siteName : siteName,
					enbId : gnbId
				});
			}
		}
		
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
		}
		
		/*填充小区*/
		function getCellName(nodes){
			var data = $("#cellNameChoose").combobox('getData');
			var datas = [];
			for(var i = 0;i < nodes.length;i++){
				var oneNode = {id:nodes[i].id,text:nodes[i].cellName};
				datas.push(oneNode);
			}
			$("#cellNameChoose").combobox('loadData',datas);
			$("#cellNameChoose").combobox('setValue',nodes[0].id);
		}
		
		var arrs = ['ENBID','CELLID','SITE_NAME','CELLNAME','TAC','PCI','FREQUENCY1','LONGITUDE','LATITUDE','TILT_M','AZIMUTH','HEIGHT','CITY'];
		
		/*地图轨迹*/
		function showMap(){
			var nodes = $('#stationLogTable').datagrid('getSelections');
			var cn = '';
			for(var i = 0;i < nodes.length;i++){
				console.log(nodes[i]);
				if(cn == ''){
					cn = nodes[i].region;
				}else if(cn != nodes[i].region){
					$.messager.alert('提示','请选择同一区域小区进行展示','info');
					return;
				}
			}
			if(nodes.length > 0){
				var src = $("#mapIframe").attr("src");
				if(!src){
					$("#mapIframe").attr("src", "${pageContext.request.contextPath}/gis/default3.html?toolbarType=90");
				}
				
				$('#mapWindowId').window('open');
				cityName = nodes[0].region;
				getCellName(nodes);
				drawingMap(nodes[0].cellName,nodes[0].id);
			}else{
				$.messager.alert('提示','请选择展示地图轨迹的站点','info');
				return;
			}
			var t2 = window.setInterval(function(){
        		if(mapIframe.window.map){
        			$.ajax({
	       	             type: "GET",
	       	             url: "${pageContext.request.contextPath}/oppositeOpen3dCompletionShow/getShpName.action",
	       	             data: {'cityName':cityName,'cityId':null,'cityType':null},
	       	             dataType: "json",
	       	             success: function(data){
	       	            	 console.log(data);
	       					mapIframe.window.StationAddCellToMap(data.shpName,true,data.lon,data.lat,arrs);
	       					mapIframe.window.StationLayerManage();
	                    }
	       	        });
		        	window.clearInterval(t2);
        		}
        	},1000);
		}
		var cityName = null;
		/*绘制地图*/
		function drawingMap(cellName,id){
			var colors = [];
			var event = $("#eventChoose").combobox('getValue');
			var eventStr = "";
			if(event == 0){
				eventStr = "nrRsrp";
			}else if(event == 1){
				eventStr = "nrSinr";
			}else if(event == 2){
				eventStr = "nrMacthrputdl";
			}else if(event == 3){
				eventStr = "nrMacthrputul";
			}
			
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/oppositeOpen3dReportShow/getPoint.action",
	             data: {'cellName':cellName,'id':id,'event':event},
	             dataType: "json",
	             success: function(data){
	            	var t1 = window.setInterval(function(){
	            		if(mapIframe.window.map){
	            			if(data != null){
		            			mapIframe.window.stationEtgTralFunc(data.data,eventStr);	            				
	            			}else{
	            				mapIframe.window.stationEtgTralFunc([],eventStr);
	            			}
	    		        	window.clearInterval(t1);
	            		}
	            	},1000);
              }
	         });
			
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
					if(nodes[i].createReportDate == null || nodes[i].createReportDate == ""){
						$.messager.alert('提示',nodes[i].siteName + '没有生成报告,不能导出','info');
						return;
					}
				}
			}else{
				$.messager.alert('提示','请选择需要生成报告的站点','info');
				return;
			}
			window.location= "${pageContext.request.contextPath}/oppositeOpen3dReportShow/downloadLog.action?idsStr="+ids;
		}
		
		function cellNameChooseFunc(){
			var id = $("#cellNameChoose").combobox('getValue');
			var cellName = $("#cellNameChoose").combobox('getText');
			drawingMap(cellName,id);
		}
		
		function eventChooseFunc(){
			var event = $("#eventChoose").combobox('getValue');
			if(event == 0){
				$("#color1").html("LTE RSRP < -110");
				$("#color2").html("-110 <= LTE RSRP < -105");
				$("#color3").html("-105 <= LTE RSRP < -100");
				$("#color4").html("-100 <= LTE RSRP < -95");
				$("#color5").html("-95 <= LTE RSRP < -85");
				$("#color6").html("-85 <= LTE RSRP < -75");
				$("#color7").html("-75 <= LTE RSRP < -45");
			}else if(event == 1){
				$("#color1").html("LTE SINR < -3");
				$("#color2").html("-3 <= LTE SINR < 0");
				$("#color3").html("0 <= LTE SINR < 3");
				$("#color4").html("3 <= LTE SINR < 6");
				$("#color5").html("6 <= LTE SINR < 9");
				$("#color6").html("9 <= LTE SINR < 15");
				$("#color7").html("15 <= LTE SINR");
			}else if(event == 2){
				$("#color1").html("0 < LTE Throughput DL < 40");
				$("#color2").html("40 <= LTE Throughput DL < 120");
				$("#color3").html("120 <= LTE Throughput DL < 400");
				$("#color4").html("400 <= LTE Throughput DL < 800");
				$("#color5").html("800 <= LTE Throughput DL < 1200");
				$("#color6").html("1200 <= LTE Throughput DL < 1600");
				$("#color7").html("1600 <= LTE Throughput DL");
			}else if(event == 3){
				$("#color1").html("0 < LTE Throughput UL < 10");
				$("#color2").html("10 <= LTE Throughput UL < 20");
				$("#color3").html("20 <= LTE Throughput UL < 30");
				$("#color4").html("30 <= LTE Throughput UL < 35");
				$("#color5").html("35 <= LTE Throughput UL < 40");
				$("#color6").html("40 <= LTE Throughput UL < 200");
				$("#color7").html("200 <= LTE Throughput UL");
			}
			cellNameChooseFunc();
		}
		
		function imgClickFunc(val){
			console.log(val);
			if(val == 0){
				$("#showHide1").hide();
				$("#showHide2").show();
				$("#tuli").show();
			}else if(val == 1){
				$("#showHide1").show();
				$("#showHide2").hide();
				$("#tuli").hide();
			}
		}
		
	</script>

  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    <div data-options="region:'center',title:'单站报告列表',split:false,tools:'#tt3'"  style="width:100%;">
    	<table id="stationLogTable"></table>
    	<div id="tb">
	    	<form id="queryForm" class="esyui-form" method="post" >
	    		<div class="inputDivShow">
	    			测试完成情况：
	    			<select id="testFinishCondition" class="easyui-combobox" name="dept" style="width:140px;" data-options="editable:false" editable='false'>
				        <option value="">&nbsp;</option>
				        <option value="未完成">未完成</option>
				        <option value="有更新">有更新</option>
				        <option value="已完成">已完成</option>
				    </select>
	    		</div>
	    		<div class="inputDivShow">
	    			无线参数一致性：	    			
	    			<select id="wirelseeParamConsistency" class="easyui-combobox" name="dept" style="width:140px;" data-options="editable:false" editable='false'>
				        <option value="">&nbsp;</option>
				        <option value="Cellid不一致">Cellid不一致</option>
				        <option value="TAC不一致">TAC不一致</option>
				        <option value="PCI不一致">PCI不一致</option>
				        <option value="全部一致">全部一致</option>
				    </select>
	    		</div>
	    		<div class="inputDivShow">
	    			不通过的测试项：	    			
	    			<select id="noPassTestEvent" class="easyui-combobox" name="dept" style="width:140px;" data-options="editable:false" editable='false'>
				        <option value="">&nbsp;</option>
				        <option value="FTP测试未通过">FTP测试未通过</option>
				        <option value="接入测试未通过">接入测试未通过</option>
				        <option value="ping测试未通过">ping测试未通过</option>
				        <option value="切换测试未通过">切换测试未通过</option>
				        <option value="CSFB测试未通过">CSFB测试未通过</option>
				        <option value="Volte测试未通过">Volte测试未通过</option>
				        <option value="全部通过">全部通过</option>
				    </select>
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
	    		<div class="inputDivShow">
	    			enbId：<input id="gnbIdQuery" class="easyui-numberbox" style="width:125px;"/>
	    		</div>
				<table width="100%">
		    		<tr>
			    		<td width="40%" align="right">
				    		<shiroextend:hasAnyPermissions name="oppositeShow:query">
				    			<a class="easyui-linkbutton" onclick="pageQuery();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a>
				    		</shiroextend:hasAnyPermissions>
			    		</td>
			    		<td width="10%" align="center">
			    			<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a>
			    		</td>
		    			<td width="7%" align="left">
				    		<shiroextend:hasAnyPermissions name="oppositeShow:map">
			    			<a class="easyui-linkbutton" onclick="showMap();" id="showMapId" style="width: 80px;" data-options="iconCls:'icon-networkStructure',disabled:true" >地图轨迹</a>
				    		</shiroextend:hasAnyPermissions>
			    		</td>
		    			<td width="40%" align="left">
				    		<shiroextend:hasAnyPermissions name="oppositeShow:download">
			    			<a class="easyui-linkbutton" onclick="reportDownload();" style="width: 80px;" data-options="iconCls:'icon-excel'" >报告下载</a>
				    		</shiroextend:hasAnyPermissions>
			    		</td>
			    	</tr>
		    	</table>
	    	</form>
		</div>
	</div>
	<div id="mapWindowId" class="easyui-window" title="地图轨迹" style="width:800px;height:450px"
        data-options="iconCls:'icon-save',modal:true,resizable:false,maximizable:false">
		<div style="height: 100%;width: 100%;z-index: 90;float:left;">
			<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
		</div>
        <div style="width: 100%;z-index: 100;float:left;margin-top:-370px;height: 20px;">
        	<div style="width:28%;text-align: center;float:left;">
    			小区选择：
			    <input id="cellNameChoose" class="easyui-combobox" name="dept" data-options="valueField:'id',onSelect:cellNameChooseFunc,
			    	editable:false,textField:'text'">
    		</div>
    		<div style="width:28%;text-align: center;float:left;">
    			指标选择：	    			
    			<select id="eventChoose" class="easyui-combobox" data-options="editable:false,onSelect:eventChooseFunc" name="dept" 
    				 style="width:140px;" editable='false'>
			        <option value="0">LTE RSRP</option>
			        <option value="1">LTE SINR</option>
			        <option value="2">LTE Throughput DL</option>
			        <option value="3">LTE Throughput UL</option>
			    </select>
    		</div>
    		<div style="width:60px;height20px;text-align: center;float:left;">
				图例:
				<img id="showHide1" onclick="imgClickFunc(0)" src="${pageContext.request.contextPath}/gis/images/in.png">
				<img id="showHide2" onclick="imgClickFunc(1)" style="display: none" src="${pageContext.request.contextPath}/gis/images/out.png">
			</div>
    		<div id="tuli" style="width:33%;text-align: center;float:left;">
    			<div style="width:100%;text-align: center;float:left;">
    				<div style="width: 20%;height: 20px;background: #FF0000;float: left;"></div>
    				<div id="color1" style="width: 80%;height: 20px;float: left;">LTE RSRP &lt; -110</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #FABF8F;float: left;"></div>
    				<div id="color2" style="width: 80%;height: 20px;float: left;">-110&lt;= LTE RSRP &lt;-105</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #FFFF00;float: left;"></div>
    				<div id="color3" style="width: 80%;height: 20px;float: left;">-105&lt;= LTE RSRP &lt;-100</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #95B3D7;float: left;"></div>
    				<div id="color4" style="width: 80%;height: 20px;float: left;">-10&lt;= LTE RSRP &lt;-95</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #548DD4;float: left;"></div>
    				<div id="color5" style="width: 80%;height: 20px;float: left;">-95&lt;= LTE RSRP &lt;-90</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #92D050;float: left;"></div>
    				<div id="color6" style="width: 80%;height: 20px;float: left;">-90&lt;= LTE RSRP &lt;-85</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #00B050;float: left;"></div>
    				<div id="color7" style="width: 80%;height: 20px;float: left;">-85&lt;= LTE RSRP &lt;-80</div>
    			</div>
    		</div>
        </div>
	</div>
  </body>
</html>
