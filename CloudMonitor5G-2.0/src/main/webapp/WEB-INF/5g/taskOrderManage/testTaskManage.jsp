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
    <title>指定测试任务</title>
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
		
		$(function(){
			$('#addTer').window('close');
			initTable();
			
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
			
			$.post('${pageContext.request.contextPath}/testTaskManage/getInitUsers.action?cityId='+cityId, {},
					function(result) { 
						initSelect(result.tlList,"",'taskInitiatorSelect');
					}
			,'json');
			
			$.post('${pageContext.request.contextPath}/testTaskManage/getInitCellInfo.action?cityId='+cityId, {},
					function(result) {
						initSelect(result.lteList,"",'occupyLteCellSelect');
						initSelect(result.nrList,"",'occupyNrCellSelect');
					}
			,'json');
			
			$.post('${pageContext.request.contextPath}/testTaskManage/getInitTerminal.action?cityId='+cityId, {},
					function(result) {
						initSelect(result.tlList,"",'terminalIdSelect');
					}
			,'json');
		});
		
		var cityId = '${id}';

		/* 初始化测试计划列表 */
		function initTable(){
			$("#testTaskTable").datagrid({
				// 表头
				columns:[[
					{field:'id',checkbox:true},
					{field:'workOrderId',width:100,align:'center',title:'工单编号'}, 
					{field:'occupyNrCellName',width:100,align:'center',title:'要求占用NR小区名称'},
					{field:'occupyLteCellName',width:100,align:'center',title:'要求占用LTE小区名称'},
					{field:'testPointLon',width:100,align:'center',title:'测试点经度'},
					{field:'testPointLat',width:100,align:'center',title:'测试点纬度'},
					{field:'testLocationSkewing',width:100,align:'center',title:'测试位置偏离（米）'},
					{field:'testDate',width:80,align:'center',title:'测试时段'},
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
				url:'${pageContext.request.contextPath}/testTaskManage/doPageListJson.action?cityId='+cityId,
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
		        	}else{
		        		$('#delOprate').linkbutton('disable');
		        		$('#closeOprate').linkbutton('disable');
		        	}
		        }
			});
		}
		
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
	
		var workOrderId;
		function addTask(){
		
			if(!cityId){
				$.messager.alert('错误提示','没有城市,请选择某个城市!','error');
				return ;
			}
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/testTaskManage/queryWordkOrderId.action",
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
		/*多条件查询*/
		function pageQuery(){	
			$("#testTaskTable").datagrid('load',{
				occupyNrCellName : $("#occupyNrCell").textbox('getValue'),
				occupyLteCellName : $("#occupyLteCell").textbox('getValue'),
				boxId : $("#terminalId").numberbox('getValue'),
				workOrderState : $("#workOrderStateSelect").combobox('getValue'),
				taskInitiator : $("#taskInitiatorSelect").combobox('getValue'),
				taskTimeLimit : $("#taskTimeLimitSelect").combobox('getValue'),
				beginDate : $("#beginDate").datetimebox('getValue'),
				endDate : $("#endDate").datetimebox('getValue'),
				loctionDeparture : $("#loctionDepartureSelect").combobox('getValue')
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
					$.post("${pageContext.request.contextPath}/testTaskManage/deleteTestTask.action",{idStr:ids},
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
					$.post("${pageContext.request.contextPath}/testTaskManage/closeTestTask.action",{idStr:ids},
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
		
		function saveCQTtask(){
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
		
		function testTargetSelect(selectOption){
			console.info(selectOption);
			$('#testPlanSelect').combobox('loadData', {});//清空option选项   
			$.post("${pageContext.request.contextPath}/testTaskManage/findTestPlan.action",{boxId:selectOption.id},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						initSelectTestPlan(result.tlList,"",'testPlanSelect');
					}
				}
			,"json");	
		}
		
		/*填充测试计划下拉框（value为id值）*/
		function initSelectTestPlan(ugList,nameList,id){
			var data = [];
			for(var i = 0;i < ugList.length;i++){
				var oneUp = {};
				oneUp.id = ugList[i].id;
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
	    			要求占用NR小区：
	    			<input id="occupyNrCell" class="easyui-textbox" style="width:140px;" name="occupyNrCellName"/>
	    		</div>
	    		<div class="inputDivShow" style="width:24%">
	    			要求占用LTE小区：<input id="occupyLteCell" class="easyui-textbox" style="width:140px;" name="occupyLteCellName"/>
	    		</div>
	    		<div class="inputDivShow" >
	    			终端ID：
	    			<input id="terminalId" name="boxId" class="easyui-numberbox" data-options="validType:'length[1,24]'"  />
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
					<select id="taskTimeLimitSelect" class="easyui-combobox" name="taskTimeLimit" style="width:165px;" data-options="editable:false">
						<option value="">&nbsp;</option>
				        <option value="24小时">24小时</option>
				        <option value="48小时">48小时</option>
				        <option value="72小时">72小时</option>
				        <option value="96小时">96小时</option>
				    </select>
	    		</div>
	    		<div class="inputDivShow">
	    			位置偏离：
	    			<select id="loctionDepartureSelect" class="easyui-combobox" name="loctionDeparture" style="width:140px;" data-options="editable:false">
	    				<option value="">&nbsp;</option>
				        <option value="0">小于100米</option>
				        <option value="1">100至500米</option>
				        <option value="2">大于500米</option>
				    </select>
	    		</div>
	    		
				<div style="text-align: center;">
	    			<shiroextend:hasAnyPermissions name="CQTTask:show">
	    				<a class="easyui-linkbutton" onclick="pageQuery();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-search'" >查询</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    			<shiroextend:hasAnyPermissions name="CQTTask:add">
	    				<a class="easyui-linkbutton" onclick="addTask();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-add'" >新增任务</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    			<shiroextend:hasAnyPermissions name="CQTTask:delete">
	    				<a class="easyui-linkbutton" id="delOprate" onclick="delTask();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-remove',disabled:true" >删除任务</a>
	    			</shiroextend:hasAnyPermissions>
	    			
	    				<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;margin: 0px 10px 0px 10px;" data-options="iconCls:'icon-reload'">重置</a>
	    			
	    			<shiroextend:hasAnyPermissions name="CQTTask:close">
	    				<a class="easyui-linkbutton" id="closeOprate" onclick="closeTask();" style="width: 80px;margin: 0px 10px 0px 10px;"  data-options="disabled:true">闭环任务</a>
	    			</shiroextend:hasAnyPermissions>
		    	</div>
	    	</form>
		</div>
		
		<div id="addTer"  class="easyui-dialog" title="新增工单" closed="true" style="width:400px;height:400px;" 
			data-options="iconCls:'icon-save',resizable:true,modal:true,buttons:[{ 
					text:'保存',
					handler:function(){
						saveCQTtask();
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
			<form id="addTerForm" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/testTaskManage/saveByETG.action">
				<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 10px;padding-top: 20px;">
	    			占用LTE小区名称：<input id="occupyLteCellSelect" class="easyui-combobox"  name="occupyLteCellName" style="width:250px;"/>
	    		</div>
	    		<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 10px;">
	    			占用NR小区名称：
	    			<input id="occupyNrCellSelect" class="easyui-combobox"  name="occupyNrCellName" style="width:250px;"/>
	    		</div>
	    		<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 52px;">
	    			任务时限：
					<select id="taskTimeLimitSelect" class="easyui-combobox" name="taskTimeLimit"  data-options="required:true,editable:false" style="width:250px;">
				        <option value="24小时" >24小时</option>
				        <option value="48小时" selected="true">48小时</option>
				        <option value="72小时">72小时</option>
				        <option value="96小时">96小时</option>
				    </select>
	    		</div>
	    		<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 62px;">
	    			终端ID：
	    			<input id="terminalIdSelect" name="boxId" class="easyui-combobox" data-options="validType:'length[1,24]',required:true,editable:false,onSelect:testTargetSelect" style="width:250px;"/>
	    		</div>
	    		<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 28px;">
	    			测试计划选择：
	    			<input id="testPlanSelect" name="testPlanId" class="easyui-combobox" data-options="validType:'length[1,24]',editable:false" style="width:250px;"/>
	    		</div>
				<div style="width: 400px;height: 30px;margin-top: 5px;margin-left: 80px;">
	    			备注：<input id="comment" class="easyui-textbox"  name="comment" style="width:250px;" />
	    		</div>
			</form>
	    </div>
  </body>
</html>
