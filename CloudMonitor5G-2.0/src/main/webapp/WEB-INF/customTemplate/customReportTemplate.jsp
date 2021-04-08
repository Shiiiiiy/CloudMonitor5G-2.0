<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>自定义报表模板管理</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
	var columns=[[
				{field:'id',hidden:true}, 
				{field:'region',width:80,align:'center',title:'区域'}, 	
				{field:'templateName',width:80,align:'center',title:'模板名称'},
				{field:'importDate',width:100,align:'center',title:'导入时间',
				formatter: function(value,row,index){
					if (value){
						var time = new Date(value);
						var y = time.getFullYear();
						var m = time.getMonth()+1;
						var d = time.getDate();
						var h = time.getHours();
						var mm = time.getMinutes();
						var s = time.getSeconds();
						return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
					}
					return '-';
					
				}},
				{field:'userName',width:80,align:'center',title:'创建人',
				formatter: function(value,row,index){
					if (value){
						return value;
					}
					return '-';
					
				}}
				
			]];
	
	$(function(){
		initHeight();
		initTable();
	});
	//重新加载
	window.onresize=reload;
	function reload(){
		initHeight();
	}
	
	function add0(m){
		return m<10?'0'+m:m 
	}
	
	/* 初始化高度 */
	function initHeight(){
		var highTotal = $(document.body).outerHeight();
		$("#tableDiv").height(highTotal-30);
	}
	function initTable(){
		$("#mainTable").datagrid({
			// 表头// field用于匹配远程json属性，width宽度,align居左右中对齐
			columns:columns,
			url:'${pageContext.request.contextPath}/customrReportTemplate/doPageListJson.action',
			title:'规划工参列表',
			fitColumns:true,
			// 分页条
			pagination:true,
			pageSize:20,
			pageList:[10,20,50,100],
			//填满区域
			fit:true,
			//奇偶变色
			striped:true,
			// 行号
			rownumbers:true,
			// 工具栏 【表格上方功能按钮】
			//toolbar:'#toolbar' 
			onClickRow: function(rowIndex, rowData){
	        	var nodes = $('#mainTable').datagrid('getSelections');
	        	if(nodes.length > 0){
		        	$('#export').linkbutton('enable');
		        	$('#delectParam').linkbutton('enable');		
	        	}else{
	        		$('#export').linkbutton('disable');
	        		$('#delectParam').linkbutton('disable');		
	        	}
	        }
		});
		
	}
	
	
	/* 重置查询表单 */
	function clearForm(){
		$('#saveForm').form('clear');
		var areaNodes = $('#areaTree').tree('getChecked');
		for(var i= 0 ; i < areaNodes.length ; i++){
			$('#areaTree').tree('uncheck',areaNodes[i].target);
		}
	}
	/* 多条件查询 */
	function query(){
		var checkedNodes = $("#areaTree").tree('getChecked');
		var cityIds= [];
		for(var y = 0 ; y < checkedNodes.length ; y++){
			if(checkedNodes[y].id==-1){
				continue;
			}
			if(checkedNodes[y].attributes.type=='City'){
				cityIds.push(checkedNodes[y].text);
			}
		}
		var cityIdsString = cityIds.join(",");
		$("#mainTable").datagrid('load',{
			beginImportDate: $("#beginDate").datetimespinner('getValue'),
			endImportDate:$("#endDate").datetimespinner('getValue'),
			citNames:cityIdsString
		});
	}
	
	function exportExcel(){
		var nodes = $('#mainTable').datagrid('getSelections');
		var ids = '';
		var flag = false;
		if(nodes.length == 1){
			for(var i = 0; i < nodes.length;i++){
				if(i != 0){
					ids = ids + ',';
				}
				ids = ids + nodes[i].id;
			}
		}else{
			$.messager.alert('提示','只能选择一条模板导出','info');
			return;
		}
		window.location= "${pageContext.request.contextPath}/customrReportTemplate/downloadExcel.action?idsStr="+ids;
	}
	
	function delectParam(){
		var nodes = $('#mainTable').datagrid('getSelections');
		var ids = '';
		if(nodes.length>0){
			for(var i = 0; i < nodes.length;i++){
				if(i != 0){
					ids = ids + ',';
				}
				ids = ids + nodes[i].id;
			}
			$.messager.confirm('确认','您确认想要删除选中的模板吗？',function(r){
			    if (r){
			        $.ajax({
			             type: "GET",
			             url: "${pageContext.request.contextPath}/customrReportTemplate/delectParams.action",
			             data: {'idsStr':ids},
			             dataType: "json",
			             success: function(data){
			            	if(data.errorMsg){
			            		$.messager.alert('提示','删除失败,'+data.errorMsg,'error');
			            	}else{
			            		$.messager.alert('提示','删除成功','info'); 
			            		$("#mainTable").datagrid("reload");
			            	}
		              	 }
			     	});
			    }
			});
		}else{
			$.messager.alert('提示','请选择需要删除的模板','info');
			return;
		}
		
	}
	
	</script>
  </head>
  
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
  	<shiroextend:hasAnyPermissions name="projectparam:import">
    	<a class="easyui-linkbutton" onclick="goToPage('${pageContext.request.contextPath}/customrReportTemplate/goImport.action');" data-options="plain:false" style="width:100px;">导入</a>
	</shiroextend:hasAnyPermissions>
	<a id="query" class="easyui-menubutton" data-options="plain:false,menu:'#mm3'"  style="width:100px;">查询</a>
	<a id="export" class="easyui-linkbutton" data-options="plain:false,disabled:true"  onclick="exportExcel();" style="width:100px;">导出</a>
	<shiroextend:hasAnyPermissions name="projectparam:import">
		<a id="delectParam" class="easyui-linkbutton" data-options="plain:false,disabled:true"  onclick="delectParam();" style="width:100px;">删除</a>
	</shiroextend:hasAnyPermissions>
	<div style="padding-top: 4px;width:100%;">
		<div id="tableDiv" style="width:100%;float:left;margin-right: 10px;">
			<table id="mainTable" ></table>
		</div>
	</div>
	<div id="mm3" class="menu-content" style="background:#f0f0f0;padding:10px;text-align:left">
		<!-- <h3 style="border-bottom: 1px solid #95B8E7;color: #3366CC;font-size: 16px;font-weight:800;height:32px;padding: 0 10px;">筛选参数设置</h3> -->
    	<form id="saveForm" method="post">
	    	<table >
	    		<tr style="height:30px;width:100%; ">
	    			<td width="100px" align="right">
		    			<span class="label" style="font-size:12px;line-height:26px;">导入开始时间：</span>
					</td>
					<td width="200px" align="left">
						<input  id="beginDate" name="beginImportDate" class="easyui-datetimespinner"  data-options="showSeconds:true" style="height:26px;width:200px;"  >
					</td>
					<td width="100px" align="right">
		    			<span class="label" style="font-size:12px;line-height:26px;">结束时间：</span>
					</td>
					<td width="200px" align="left">
						<input  id="endDate" name="endImportDate" class="easyui-datetimespinner"  data-options="showSeconds:true" style="height:26px;width:200px;"  >
					</td>
	    		</tr>
	    		<tr style="height:200px;width:100%; ">
	    			<td width="100px"  align="right" valign="top">
						<span class="label" style="font-size:12px;line-height:26px;">所属域：</span>
					</td>
	    			<td width="510px" height="200px" colspan="3" align="left">
						<div class="easyui-panel" fit="true" style="overflow: auto;">
							<ul id="areaTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action',lines:true,checkbox:true"></ul>
						</div>
					</td>
	    		</tr>
	    		
	    		<!-- <tr style="height:30px;width:100%; ">
	    			<td width="100px" align="right">
		    			<span class="label" style="font-size:12px;line-height:26px;">创建人：</span>
					</td>
					<td width="500px" colspan="3" align="left">
						<input  id="userName" name="userName" style="height:26px;width:200px;" class="easyui-textbox" >
					</td>
	    		</tr> -->
	    		<tr style="height:30px;width:100%; ">
	    			<td  colspan="4" align="center">
		    			<a id="saveButton" style="width:100px;" class="easyui-linkbutton" onclick="query();" >查询</a>
						<a class="easyui-linkbutton" style="width:100px;" style="" onclick="clearForm();" >重置</a>
					</td>
	    		</tr>
	    	</table>
    	</form>
	</div>
  </body>
</html>
