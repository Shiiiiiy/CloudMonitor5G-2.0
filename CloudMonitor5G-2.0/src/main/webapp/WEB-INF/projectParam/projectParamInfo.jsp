<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>中国移动tab页</title>

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
				{field:'provinceGroupName',width:80,align:'center',title:'一级域'}, 				
				{field:'cityGroupName',width:80,align:'center',title:'二级域'},
				{field:'userName',width:80,align:'center',title:'创建人',
				formatter: function(value,row,index){
					if (value){
						return value;
					}
					return '-';
					
				}},
				{field:'importDate',width:100,align:'center',title:'导入时间',
				formatter: function(value,row,index){
					if (value){
						return value;
					}
					return '-';
					
				}},
				{field:'is5gImport',width:100,align:'center',title:'5G',
				formatter: function(value,row,index){
					if (value){
						//return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${FG}" title="导出工参表">已导入</a>';
						return '<shiroextend:hasAnyPermissions name="projectparam:export"><a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${FG}" title="导出工参表"></shiroextend:hasAnyPermissions>已导入';
					}
					return '未导入';
					
				}},
				{field:'lteImport',width:100,align:'center',title:'TD-LTE',
				formatter: function(value,row,index){
					if (value){
						//return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${LTE}" title="导出工参表">已导入</a>';
						return '<shiroextend:hasAnyPermissions name="projectparam:export"><a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${LTE}" title="导出工参表"></shiroextend:hasAnyPermissions>已导入';
					}
					return '未导入';
					
				}},
				/* {field:'gsmImport',width:100,align:'center',title:'GSM',
				formatter: function(value,row,index){
					if (value){
						return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${GSM}" title="导出工参表">已导入</a>';
					}
					return '未导入';
					
				}}, */
				{field:'is5g5gImport',width:100,align:'center',title:'5G邻区表',
				formatter: function(value,row,index){
					if (value){
						//return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${FG_FG}" title="导出工参表">已导入</a>';
						return '<shiroextend:hasAnyPermissions name="projectparam:export"><a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${FG_FG}" title="导出工参表"></shiroextend:hasAnyPermissions>已导入';
					}
					return '未导入';
					
				}},
				{field:'isLte5GImport',width:100,align:'center',title:'5G-LTE邻区表',
				formatter: function(value,row,index){
					if (value){
						//return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${FG_LTE}" title="导出工参表">已导入</a>';
						return '<shiroextend:hasAnyPermissions name="projectparam:export"><a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${FG_LTE}" title="导出工参表"></shiroextend:hasAnyPermissions>已导入';
					}
					return '未导入';
					
				}},
				{field:'tdlImport',width:100,align:'center',title:'TDL邻区表',
				formatter: function(value,row,index){
					if (value){
						//return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${TDL_NB}" title="导出工参表">已导入</a>';
						return '<shiroextend:hasAnyPermissions name="projectparam:export"><a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${TDL_NB}" title="导出工参表"></shiroextend:hasAnyPermissions>已导入';
					}
					return '未导入';
					
				}},
				{field:'lte5GImport',width:100,align:'center',title:'LTE-5G配对小区表',
					formatter: function(value,row,index){
						if (value){
							//return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${TDL_NB}" title="导出工参表">已导入</a>';
							return '<shiroextend:hasAnyPermissions name="projectparam:export"><a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${LTE_5G}" title="导出工参表"></shiroextend:hasAnyPermissions>已导入';
						}
						return '未导入';
						
					}}/* ,
				{field:'tdlGsmImport',width:100,align:'center',title:'TDL-GSM邻区表',
				formatter: function(value,row,index){
					if (value){
						return '<a href="${pageContext.request.contextPath}/projectParam/downloadExcel?cityId='+row.id+'&infoType=${infoType}&operatorType=${TDL_GSM_NB}" title="导出工参表">已导入</a>';
					}
					return '未导入';
					
				}} */
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
	/* 初始化高度 */
	function initHeight(){
		var highTotal = $(document.body).outerHeight();
		$("#tableDiv").height(highTotal-30);
	}
	function initTable(){
		$("#mainTable").datagrid({
			// 表头// field用于匹配远程json属性，width宽度,align居左右中对齐
			columns:columns,
			url:'${pageContext.request.contextPath}/projectParam/doPageList?infoType=${infoType}',
			title:'工参记录',
			fitColumns:true,
			// 分页条
			/* pagination:true,
			pageSize:20,
			pageList:[20,50,100], */
			//填满区域
			fit:true,
			//奇偶变色
			striped:true,
			// 行号
			rownumbers:true
			// 工具栏 【表格上方功能按钮】
			//toolbar:'#toolbar' 
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
				cityIds.push(checkedNodes[y].id);
			}
		}
		var cityIdsString = cityIds.join(",");
		$("#mainTable").datagrid('load',{
			beginImportDate:$("#beginImportDate").datetimespinner('getValue'),
			endImportDate:$("#endImportDate").datetimespinner('getValue'),
			userName:$("#userName").textbox('getValue'),
			cityIds:cityIdsString
		});
	}
	</script>
  </head>
  
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
  	<shiroextend:hasAnyPermissions name="projectparam:import">
    	<a class="easyui-linkbutton" onclick="goToPage('${pageContext.request.contextPath}/projectParam/importProjectParamUI.action');" data-options="plain:false" style="width:100px;">导入</a>
	</shiroextend:hasAnyPermissions>
	<a id="query" class="easyui-menubutton" data-options="plain:false,menu:'#mm3'"  style="width:100px;">查询</a>
	<div id="tableDiv" style="padding-top: 4px;width:100%;">
		<table id="mainTable" ></table>
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
						<input  id="beginImportDate" name="beginImportDate" class="easyui-datetimespinner"  data-options="showSeconds:true" style="height:26px;width:200px;"  >
					</td>
					<td width="100px" align="right">
		    			<span class="label" style="font-size:12px;line-height:26px;">结束时间：</span>
					</td>
					<td width="200px" align="left">
						<input  id="endImportDate" name="endImportDate" class="easyui-datetimespinner"  data-options="showSeconds:true"  style="height:26px;width:200px;"  >
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
	    		
	    		<tr style="height:30px;width:100%; ">
	    			<td width="100px" align="right">
		    			<span class="label" style="font-size:12px;line-height:26px;">创建人：</span>
					</td>
					<td width="500px" colspan="3" align="left">
						<input  id="userName" name="userName" style="height:26px;width:200px;" class="easyui-textbox" >
					</td>
	    		</tr>
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
