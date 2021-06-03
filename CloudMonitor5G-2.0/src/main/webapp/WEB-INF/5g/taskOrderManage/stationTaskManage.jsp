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
    <title>单验任务</title>
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
		    width: 23%;
   		}
	</style>
	<script type="text/javascript">
		
		var lteList,nrList;
		//var lteSitNameMap,nrSitNameMap
		
		$(function(){
			$('#addTer').window('close');
			$('#mapWindowId').window('close');
			initTable();
			
			$.post('${pageContext.request.contextPath}/stationReportCreatTask/getInitTerminal.action?cityId='+cityId, {},
					function(result) {
						initSelect(result.tlList,"",'terminalId');
					}
			,'json');
			
			$.post('${pageContext.request.contextPath}/stationReportCreatTask/getInitCellInfo.action?cityId='+cityId, {},
					function(result) {
						lteList = result.lteList;
						nrList = result.nrList;
						//nrSitNameMap = result.nrSitNameMap;
						//lteSitNameMap = result.lteSitNameMap;
						initSelect(nrList,"",'siteNameInput');
					}
			,'json');
			
			$.post('${pageContext.request.contextPath}/testTaskManage/getInitUsers.action?cityId='+cityId, {},
					function(result) { 
						initSelect(result.tlList,"",'taskInitiatorSelect');
					}
			,'json');
			
			//设置开始结束时间
			$('#beginDate').datebox('calendar').calendar({
				validator: function(date){
					var endDate = $.fn.datebox.defaults.parser($("#endDate").datebox('getValue'));
					return date<=endDate;
				}
			});
			$('#endDate').datebox('calendar').calendar({
				validator: function(date){
					var startDate = $.fn.datebox.defaults.parser($("#beginDate").datebox('getValue'));
					return date>=startDate;
				}
			});
			
		});
		
		var cityId = '${id}';

		/* 初始化测试计划列表 */
		function initTable(){
			$("#testTaskTable").datagrid({
				// 表头
				columns:[[
					{field:'id',checkbox:true},
					{field:'workOrderId',width:100,align:'center',title:'工单编号'}, 
					{field:'siteName',width:100,align:'center',title:'站点名称'},
					{field:'taskNetworkType',width:100,align:'center',title:'站点制式',
						formatter:function(value,row,index){
								if(value == "0"){
								 	return 'NR单验'
								}else if(value == "1"){
									return '反开3d';
								}else {
									return value;
								}
						}
					},
					{field:'testPointLon',width:100,align:'center',title:'站点经度'},
					{field:'testPointLat',width:100,align:'center',title:'站点纬度'},
					{field:'region',width:100,align:'center',title:'区域'},
					{field:'boxId',width:80,align:'center',title:'终端ID'},
					{field:'taskInitiator',width:80,align:'center',title:'任务发起人'},
					{field:'taskCreatTime',width:100,align:'center',title:'任务发起时间'},
					{field:'taskTimeLimit',width:80,align:'center',title:'任务时限',},
					{field:'workOrderState',width:80,align:'center',title:'工单状态',
						formatter:function(value,row,index){
							if(value == "010"){
							 	return '待测试'
							}else if(value == "100"){
								return '已完结';
							}else if(value == "110"){
								return '已拒绝';
							}else if(value == "101"){
								return '已提交';
							}else {
								return value;
							}
						}
					},
					{field:'comment',width:80,align:'center',title:'备注'}
				]],
				url:'${pageContext.request.contextPath}/stationReportCreatTask/doPageListJson.action?cityId='+cityId,
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
		        	var nodes = $('#testTaskTable').datagrid('getSelections');
		        	if(nodes.length > 0){
			        	$('#delOprate').linkbutton('enable');
			        	$('#closeOprate').linkbutton('enable');
			        	$('#downLoadTask').linkbutton('enable');
			        	$('#testTrail').linkbutton('enable');        		
		        	}else{
		        		$('#delOprate').linkbutton('disable');
		        		$('#closeOprate').linkbutton('disable');
		        		$('#downLoadTask').linkbutton('disable');
			        	$('#testTrail').linkbutton('disable');
		        	}
		        }
			});
		}
		
		/*填充站点名称下拉框*/
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
	
		var workOrderId =null;
		function addTask(){
			if(!cityId){
				$.messager.alert('错误提示','没有城市,请选择某个城市!','error');
				return ;
			}
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/stationReportCreatTask/queryWordkOrderId.action",
	             data: {'cityId':cityId},
	             dataType: "json",
	             success: function(data){
	             	console.info(data);
	             	var title = '【${name}】新增工单"'+ data.workid +'"';
	             	workOrderId = data.workid;
	             	$('#addTer').window({
				        title: title
				    });
				    $('#addTerForm').form('reset');
	             	$('#addTer').window('open');
              	}
	         });
		}
		
		function saveStationtask(){
			$("#addTerForm").form('submit',{
				onSubmit : function(param) {
					var isValid = $(this).form('validate');
					if(isValid){
						param.cityId = cityId;
						param.workOrderId = workOrderId;
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
						$('#addTer').window('close');
						$("#testTaskTable").datagrid("reload");
					}
				}
			});
		
		}
		
		/*多条件查询*/
		function pageQuery(){	
			$("#testTaskTable").datagrid('load',{
				siteName : $("#siteNameQuery").textbox('getValue'),
				taskNetworkType : $("#taskNetworkTypeQuery").textbox('getValue'),
				boxId : $("#terminalId").numberbox('getValue'),
				workOrderState : $("#workOrderStateSelect").combobox('getValue'),
				taskInitiator : $("#taskInitiatorSelect").combobox('getValue'),
				taskTimeLimit : $("#taskTimeLimitSelect").combobox('getValue'),
				beginDate : $("#beginDate").datetimebox('getValue'),
				endDate : $("#endDate").datetimebox('getValue'),
			});
		}
		
		function delTask(){
			var ids = '';
			var moduleTableRows = $("#testTaskTable").datagrid('getSelections');//获取表格中用户选中 所有数据
			if(moduleTableRows.length < 1){// 操作前至少选中一条
				$.messager.alert("提示","请至少选择一个测试工单!",'warning');
				return ;
			}
			for(var i = 0; i < moduleTableRows.length;i++){
				ids = ids + moduleTableRows[i].id;
				if(i != moduleTableRows.length-1){
					ids = ids + ',';
				}
			}
			$.messager.confirm("系统提示", "您确定要删除选中的测试工单吗?", function(r) {
				if (r) {
					$.post("${pageContext.request.contextPath}/stationReportCreatTask/deleteTestTask.action",{idStr:ids},
						function(result){
							if (result.errorMsg) { 
								$.messager.alert("系统提示", result.errorMsg,'error');
							} else {
								$.messager.alert("系统提示", "已成功删除!",'info');
								$("#testTaskTable").datagrid("reload");
							}
						}
					,"json");	
				}
			});
		}
		
		function closeTask(){
			var ids = '';
			var moduleTableRows = $("#testTaskTable").datagrid('getSelections');//获取表格中用户选中 所有数据
			if(moduleTableRows.length < 1){// 操作前至少选中一条
				$.messager.alert("提示","请至少选择一个测试工单!",'warning');
				return ;
			}
			for(var i = 0; i < moduleTableRows.length;i++){
				ids = ids + moduleTableRows[i].id;
				if(i != moduleTableRows.length-1){
					ids = ids + ',';
				}
			}
			$.messager.confirm("系统提示", "您确定要删除选中的测试工单吗?", function(r) {
				if (r) {
					$.post("${pageContext.request.contextPath}/stationReportCreatTask/closeTestTask.action",{idStr:ids},
						function(result){
							if (result.errorMsg) {
								$.messager.alert("系统提示", result.errorMsg,'error');
							} else {
								$.messager.alert("系统提示", "已成功闭环!",'info');
								$("#testTaskTable").datagrid("reload");
							}
						}
					,"json");	
				}
			});
		}
		
		function testTargetSelect(selectOption){
			$('#siteNameInput').combobox('loadData', {});//清空option选项   
			switch(selectOption.value){
				case '0':
					initSelect(nrList,"",'siteNameInput');
					break;
				case '1':
					initSelect(lteList,"",'siteNameInput');
					break;
			}
		}
		
		/*报告下载*/
		function reportTaskDownload(){
			var nodes = $('#testTaskTable').datagrid('getSelections');
			var boxId = "";
			var workOrderId = "";
			if(nodes.length ==1){
				for(var i = 0; i < nodes.length;i++){
					console.info(nodes[i].workOrderState);
					if(nodes[i].workOrderState == null || nodes[i].workOrderState == "010" || nodes[i].workOrderState == "110" ){
						$.messager.alert('提示',nodes[i].siteName + '没有生成任务报告,不能导出','info');
						return ;
					}
					boxId = nodes[i].boxId;
					workOrderId = nodes[i].workOrderId;
				}
			}else{
				$.messager.alert('提示','只能选择一个任务下载','info');
				return "";
			}
			window.location= "${pageContext.request.contextPath}/stationReportCreatTask/getDownload.action?boxId="+boxId+'&workOrderId='+workOrderId;
		}
		
		var netType = '';
		var selectWorkId = '';
		function testTrail(){
			netType = '';
			selectWorkId = '';
			if(!cityId){
				$.messager.alert('错误提示','没有城市,请选择某个城市!','error');
				return ;
			}
			var nodes = $('#testTaskTable').datagrid('getSelections');
			if(nodes.length>1){
				$.messager.alert('提示','请只选择一个工单任务进行展示','info');
				return;
			}
			
			for(var i = 0;i < nodes.length;i++){
				if(netType==""){
					netType =  nodes[i].taskNetworkType;
					selectWorkId = nodes[i].workOrderId;
				}else if(netType!=nodes[i].taskNetworkType){
					$.messager.alert('提示','请选择同一制式进行展示','info');
					return;
				}
			}
			
			var src = $("#mapIframe").attr("src");
			if(!src){
				$("#mapIframe").attr("src", "${pageContext.request.contextPath}/gis/default3.html?toolbarType=90");
			}
			if(netType=="0"){
				$("#mapWindowId").panel({
			        title: "单验地图轨迹",
			    });
			}else if(netType=="1"){
				$("#mapWindowId").panel({
			        title: "反开3d地图轨迹",
			    });
			}
			$('#mapWindowId').window('open');
			cityName = nodes[0].region;
			getCellName(nodes);
			
			var t2 = window.setInterval(function(){
        		if(mapIframe.window.map){
        			$.ajax({
	       	             type: "GET",
	       	             url: "${pageContext.request.contextPath}/stationCompletionShow/getShpName.action",
	       	             data: {'cityName':cityName,'cityId':null,'cityType':null},
	       	             dataType: "json",
	       	             success: function(data){
	       					mapIframe.window.StationAddCellToMap(data.shpName,true,data.lon,data.lat,[]);
	       					mapIframe.window.StationLayerManage();
	                    }
	       	        });
		        	window.clearInterval(t2);
        		}
        	},1000);
		}
		
		
		/*填充小区*/
		function getCellName(nodes){
			var datas = [];
			var siteNames = "";
			for(var i = 0;i < nodes.length;i++){
				if(i == (nodes.length-1)){
					siteNames = siteNames + nodes[i].siteName;
				}else{
					siteNames = siteNames + nodes[i].siteName + ","; 
				}
			}
			console.info("siyeName:"+siteNames);
			$.post('${pageContext.request.contextPath}/stationReportCreatTask/getCellInfoBySiteName.action?siteNames='+siteNames+"&taskNetworkType="+netType, {},
					function(result) { 
						var ugList = result.tlList;
						console.info("ugList:"+ugList);
						for(var i = 0;i < ugList.length;i++){
							var oneNode = {id:ugList[i].id,text:ugList[i].name};
							datas.push(oneNode);
						}
						$("#cellNameChoose").combobox('loadData',datas);
						$("#cellNameChoose").combobox('setValue',ugList[0].id);
						if(netType=="0"){
							drawingNrMap(ugList[0].name,ugList[0].id);
						}else if(netType=="1"){
							drawingLteMap(ugList[0].name,ugList[0].id);
						}
					}
			,'json');
		}
		
		var cityName = null;
		/*绘制地图*/
		function drawingLteMap(cellName,id){
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
	             url: "${pageContext.request.contextPath}/stationReportCreatTask/getLtePoint.action",
	             data: {'cellName':cellName,'id':id,'event':event,'workOrderId':selectWorkId},
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
		
		/*绘制地图*/
		function drawingNrMap(cellName,id){
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
	             url: "${pageContext.request.contextPath}/stationReportCreatTask/getNrPoint.action",
	             data: {'cellName':cellName,'id':id,'event':event,'workOrderId':selectWorkId},
	             dataType: "json",
	             success: function(data){
	            	var t1 = window.setInterval(function(){
	            		if(mapIframe.window.map){
	    		        	window.clearInterval(t1);
	            			if(data != null){
		            			mapIframe.window.stationEtgTralFunc(data.data,eventStr);
	            			}else{
	            				mapIframe.window.stationEtgTralFunc([],eventStr);
	            			}
	            		}
	            	},1000);
              }
	         });
		}
		
		function cellNameChooseFunc(){
			var id = $("#cellNameChoose").combobox('getValue');
			var cellName = $("#cellNameChoose").combobox('getText');
			if(netType=="0"){
				drawingNrMap(cellName,id);
			}else if(netType=="1"){
				drawingLteMap(cellName,id);
			}
		}
		
		function eventChooseFunc(){
			var event = $("#eventChoose").combobox('getValue');
			if(event == 0){
				$("#color1").html("RSRP < -110");
				$("#color2").html("-110 <= RSRP < -105");
				$("#color3").html("-105 <= RSRP < -100");
				$("#color4").html("-100 <= RSRP < -95");
				$("#color5").html("-95 <= RSRP < -85");
				$("#color6").html("-85 <= RSRP < -75");
				$("#color7").html("-75 <= RSRP < -45");
			}else if(event == 1){
				$("#color1").html("SINR < -3");
				$("#color2").html("-3 <= SINR < 0");
				$("#color3").html("0 <= SINR < 3");
				$("#color4").html("3 <= SINR < 6");
				$("#color5").html("6 <= SINR < 9");
				$("#color6").html("9 <= SINR < 15");
				$("#color7").html("15 <= SINR");
			}else if(event == 2){
				$("#color1").html("0 < Throughput DL < 40");
				$("#color2").html("40 <= Throughput DL < 120");
				$("#color3").html("120 <= Throughput DL < 400");
				$("#color4").html("400 <= Throughput DL < 800");
				$("#color5").html("800 <= Throughput DL < 1200");
				$("#color6").html("1200 <= Throughput DL < 1600");
				$("#color7").html("1600 <= Throughput DL");
			}else if(event == 3){
				$("#color1").html("0 < Throughput UL < 10");
				$("#color2").html("10 <= Throughput UL < 20");
				$("#color3").html("20 <= Throughput UL < 30");
				$("#color4").html("30 <= Throughput UL < 35");
				$("#color5").html("35 <= Throughput UL < 40");
				$("#color6").html("40 <= Throughput UL < 200");
				$("#color7").html("200 <= Throughput UL");
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
		
		
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
		}

		
	</script>

  </head>
  
   <body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<table id="testTaskTable"></table>
    	<div id="tb">
	    	<form id="queryForm" class="easyui-form" method="post" >
	    		<div class="inputDivShow">
	    			制式选择：
	    			<select id="taskNetworkTypeQuery" class="easyui-combobox" name="taskNetworkType" style="width:140px;" data-options="editable:false">
				        <option value="">&nbsp;</option>
				        <option value="0">NR单验</option>
				        <option value="1">反开3d</option>
				    </select>
	    		</div>
	    		<div class="inputDivShow" style="width:24%">
	    			站点名称：<input id="siteNameQuery" class="easyui-textbox" name="siteName"/>
	    		</div>
	    		<div class="inputDivShow" >
	    			终端ID：
	    			<input id="terminalIdQuery" name="boxId" class="easyui-numberbox" style="width:140px;" data-options="validType:'length[1,24]'"  />
	    		</div>
	    		
	    		<div class="inputDivShow">
	    			工单状态：
	    			<select id="workOrderStateSelect" class="easyui-combobox" name="workOrderState" style="width:140px;" data-options="editable:false">
				        <option value="">&nbsp;</option>
				        <option value="010">待测试</option>
				        <option value="100">已完结</option>
				        <option value="110">已拒绝</option>
				        <option value="101">已提交</option>
				    </select>
	    		</div>
	    		<div class="inputDivShow">
	    			任务发起人：
	    			<input id="taskInitiatorSelect" class="easyui-textbox" style="width:140px;" name="taskInitiator"/>
	    		</div>

	    		<div class="inputDivShow" style="width:24%">
	    			任务发起时间：
	    			<input style="width:100px"  data-options="editable:false"  class="easyui-datetimebox" name="beginDate"  id="beginDate">-<input style="width:100px" data-options="editable:false" class="easyui-datetimebox" name="endDate" id="endDate">
	    		</div>

	    		<div class="inputDivShow">
	    			任务时限：
					<select id="taskTimeLimitSelect" class="easyui-combobox" name="taskTimeLimit" style="width:140px;" data-options="editable:false">
						<option value="">&nbsp;</option>
				        <option value="24小时">24小时</option>
				        <option value="48小时">48小时</option>
				        <option value="72小时">72小时</option>
				        <option value="36小时">96小时</option>
				    </select>
	    		</div>
	    		
				<div style="text-align: center;">
	    			<shiroextend:hasAnyPermissions name="stationTask:show">
	    				<a class="easyui-linkbutton" onclick="pageQuery();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-search'" >查询</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    			<shiroextend:hasAnyPermissions name="stationTask:add">
	    				<a class="easyui-linkbutton" onclick="addTask();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-add'" >新增任务</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    			<shiroextend:hasAnyPermissions name="stationTask:delete">
	    				<a class="easyui-linkbutton" id="delOprate" onclick="delTask();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-remove',disabled:true" >删除任务</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    				<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-reload'">重置</a>
	    			
	    			<shiroextend:hasAnyPermissions name="stationTask:close">
	    				<a class="easyui-linkbutton" id="closeOprate" onclick="closeTask();" style="width: 80px;margin: 0px 10px 0px 10px;"  data-options="disabled:true">闭环任务</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    			<shiroextend:hasAnyPermissions name="stationTask:download">
	    				<a class="easyui-linkbutton"  id="downLoadTask" onclick="reportTaskDownload();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="disabled:true">下载任务</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    			<shiroextend:hasAnyPermissions name="stationTask:map">
	    				<a class="easyui-linkbutton" id="testTrail" onclick="testTrail();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="disabled:true">测试轨迹</a>
		    		</shiroextend:hasAnyPermissions>
		    	</div>
	    	</form>
		</div>
		
		<div id="addTer"  class="easyui-dialog" title="新增工单" closed="true" style="width:400px;height:400px;" 
		data-options="iconCls:'icon-save',resizable:true,modal:true,buttons:[{ 
				text:'保存',
				handler:function(){
					saveStationtask();
				}},{
				text:'重置',
				handler:function(){
					$('#addTerForm').form('reset');
				}},{
				text:'关闭',
				handler:function(){
					$('#addTerForm').form('reset');
					$('#addTer').window('close');
				}}]">
		<form id="addTerForm" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/stationReportCreatTask/saveByETG.action">
			<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 30px;padding-top: 20px;">
       		 	任务制式：
    			<select id="taskNetworkTypeInput" class="easyui-combobox" name="taskNetworkType" style="width:250px;" data-options="required:true,editable:false,onSelect:testTargetSelect">
			        <option value="0">NR单验</option>
			        <option value="1">反开3d</option>
			    </select>
			</div>
			<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 30px;">
	       		 站点名称： <input id="siteNameInput" class="easyui-combobox"  name="siteName" style="width:250px;" data-options="required:true"/>
			</div>
			<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 30px;">
    			终端ID：&nbsp;&nbsp;&nbsp;
    			<input id="terminalId" name="boxId" class="easyui-combobox" style="width:250px;" data-options="validType:'length[1,24]',required:true,editable:false" />
    		</div>
			<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 30px;">
	       		 任务时限：
				<select id="taskTimeLimitSelect" class="easyui-combobox" name="taskTimeLimit"  style="width:250px;" data-options="required:true,editable:false">
			        <option value="24小时" >24小时</option>
			        <option value="48小时" selected="true">48小时</option>
			        <option value="72小时">72小时</option>
			        <option value="96小时">96小时</option>
			    </select>
			</div>
			<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 30px;">
	       		 备注：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="comment" class="easyui-textbox"  name="comment" style="width:250px;"/>		
			</div>
		</form>
    </div>
    
    <div id="mapWindowId" class="easyui-window" closed="true" title="单验地图轨迹" style="width:800px;height:450px"
        data-options="iconCls:'icon-save',modal:true,resizable:false,maximizable:false">
		<div style="height: 100%;width: 100%;z-index: 90;float:left;">
			<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
		</div>
        <div style="width: 100%;z-index: 100;float:left;margin-top:-370px;height: 20px;">
        	<div style="width:28%;text-align: center;float:left;">
    			小区选择：
			    <%-- <input id="cellNameChoose" class="easyui-combobox" name="dept" data-options="valueField:'id',onSelect:cellNameChooseFunc,
			    	editable:false,textField:'text',url:'${pageContext.request.contextPath}/stationReportShow/getAllCell.action'"> --%>
			    <input id="cellNameChoose" class="easyui-combobox" name="dept" data-options="valueField:'id',onSelect:cellNameChooseFunc,
			    	editable:false,textField:'text'">
    		</div>
    		<div style="width:28%;text-align: center;float:left;">
    			指标选择：	    			
    			<select id="eventChoose" class="easyui-combobox" data-options="editable:false,onSelect:eventChooseFunc" name="dept" 
    				 style="width:140px;" editable='false'>
			        <option value="0">RSRP</option>
			        <option value="1">SINR</option>
			        <option value="2">Throughput DL</option>
			        <option value="3">Throughput UL</option>
			    </select>
    		</div>
    		<div style="width:60px;height20px;text-align: center;float:left;">
				图例:
				<img id="showHide1" onclick="imgClickFunc(0)" src="${pageContext.request.contextPath}/gis/images/in.png">
				<img id="showHide2" onclick="imgClickFunc(1)" style="display: none" src="${pageContext.request.contextPath}/gis/images/out.png">
			</div>
    		<div id="tuli" style="width:33%;text-align: center;float:left;display: none;">
    			<div style="width:100%;text-align: center;float:left;">
    				<div style="width: 20%;height: 20px;background: #FF0000;float: left;"></div>
    				<div id="color1" style="width: 80%;height: 20px;float: left;">RSRP &lt; -110</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #FABF8F;float: left;"></div>
    				<div id="color2" style="width: 80%;height: 20px;float: left;">-110&lt;= RSRP &lt;-105</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #FFFF00;float: left;"></div>
    				<div id="color3" style="width: 80%;height: 20px;float: left;">-105&lt;= RSRP &lt;-100</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #95B3D7;float: left;"></div>
    				<div id="color4" style="width: 80%;height: 20px;float: left;">-10&lt;= RSRP &lt;-95</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #548DD4;float: left;"></div>
    				<div id="color5" style="width: 80%;height: 20px;float: left;">-95&lt;= RSRP &lt;-90</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #92D050;float: left;"></div>
    				<div id="color6" style="width: 80%;height: 20px;float: left;">-90&lt;= RSRP &lt;-85</div>
    			</div>
    			<div style="width:100%;text-align: center;float:left;margin-top:3px;">
    				<div style="width: 20%;height: 20px;background: #00B050;float: left;"></div>
    				<div id="color7" style="width: 80%;height: 20px;float: left;">-85&lt;= RSRP &lt;-80</div>
    			</div>
    		</div>
        </div>
	</div>
	
  </body>
</html>
