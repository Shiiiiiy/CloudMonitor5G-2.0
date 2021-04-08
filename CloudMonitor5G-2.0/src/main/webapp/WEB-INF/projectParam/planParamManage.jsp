<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>规划参数管理页</title>

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
				{field:'operatorType',width:80,align:'center',title:'制式'},
				{field:'userName',width:80,align:'center',title:'创建人',
				formatter: function(value,row,index){
					if (value){
						return value;
					}
					return '-';
					
				}},
				{field:'planSheetName',width:80,align:'center',title:'规划表名称'},
				{field:'importDate',width:100,align:'center',title:'导入时间',
				formatter: function(value,row,index){
					if (value){
						value = value.replace("T", " ");
						return value;
					}
					return '-';
					
				}},
				{field:'totalCellNum',width:80,align:'center',title:'总小区数'}
				
			]];
			
	var columnsParams5G=[[
				{field:'id',hidden:true}, 
				{field:'city',width:80,align:'center',title:'Region'},
				{field:'gnbId',width:80,align:'center',title:'*gNB ID'},
				{field:'cellId',width:80,align:'center',title:'*CELLID'},
				{field:'siteName',width:80,align:'center',title:'*SiteName'},
				{field:'cellName',width:80,align:'center',title:'*CellName'},
				{field:'localCellID',width:80,align:'center',title:'*LocalCellId'},
				{field:'tac',width:100,align:'center',title:'TAC'},
				{field:'pci',width:100,align:'center',title:'*PCI'},
				{field:'frequency1',width:100,align:'center',title:'Frequency1'},
				{field:'lon',width:80,align:'center',title:'*Longitude'},
				{field:'lat',width:80,align:'center',title:'*Latitude'},
				{field:'tiltM',width:100,align:'center',title:'Tilt M'},
				{field:'tiltE',width:100,align:'center',title:'Tilt E'},
				{field:'azimuth',width:100,align:'center',title:'Azimuth'},
				{field:'height',width:80,align:'center',title:'Height'}, 				
				{field:'aauModel',width:80,align:'center',title:'AAU型号'},
				{field:'nrFrequency',width:80,align:'center',title:'NR频率'},
				{field:'cellBroadband',width:80,align:'center',title:'小区带宽'},
				{field:'specialRatio',width:80,align:'center',title:'特殊子帧配比'},
				{field:'upAndDownRatio',width:80,align:'center',title:'上下行比例'},
				{field:'ssbWaveInterval',width:100,align:'center',title:'SSB子载波间隔'},
				{field:'rootSeq',width:100,align:'center',title:'根序列'},
				{field:'antennaManufacturer',width:100,align:'center',title:'天线厂家'},
				{field:'cellTRx',width:100,align:'center',title:'小区TRx'},
				{field:'frameStructure',width:100,align:'center',title:'帧结构'},
				{field:'p_a',width:100,align:'center',title:'P-a'},
				{field:'p_b',width:100,align:'center',title:'P-b'},
				{field:'pdcchSymbol',width:100,align:'center',title:'pdcch符号数'}
				
			]];
			
	var columnsParams4G=[[
				{field:'id',hidden:true}, 
				{field:'region',width:80,align:'center',title:'区域'}, 				
				{field:'mcc',width:80,align:'center',title:'MCC'},
				{field:'mnc',width:80,align:'center',title:'MNC'},
				{field:'enbId',width:80,align:'center',title:'eNodeB-ID'},
				{field:'siteName',width:80,align:'center',title:'*SiteName'},
				{field:'cellName',width:80,align:'center',title:'*CellName'},
				{field:'cellId',width:80,align:'center',title:'*CellID'},
				{field:'localCellId',width:80,align:'center',title:'*LocalCellID'},
				{field:'reverseOpen3d',width:80,align:'center',title:'*是否反开3d'},
				{field:'tac',width:100,align:'center',title:'TAC'},
				{field:'broadBand',width:100,align:'center',title:'带宽'},
				{field:'frequencyDl',width:80,align:'center',title:'Frequency_DL'},
				{field:'pci',width:80,align:'center',title:'*PCI'},
				{field:'longitude',width:100,align:'center',title:'*Longitude'},
				{field:'latitude',width:100,align:'center',title:'*Latitude'},
				{field:'high',width:100,align:'center',title:'High'},
				{field:'beamWidth',width:80,align:'center',title:'BeamWidth'}, 				
				{field:'azimuth',width:80,align:'center',title:'*Azimuth'},
				{field:'tilt_m',width:80,align:'center',title:'tilt_m'},
				{field:'tilt_e',width:80,align:'center',title:'tilt_e'},
				{field:'type',width:80,align:'center',title:'Type'},
				{field:'earfcn',width:80,align:'center',title:'*Earfcn'},
				{field:'subFrameConfig',width:80,align:'center',title:'子帧配置'},
				{field:'specialSubFrameConfig',width:100,align:'center',title:'特殊子帧配置'},
				{field:'rs_epre',width:100,align:'center',title:'RS EPRE'},
				{field:'p_a',width:100,align:'center',title:'p-a'},
				{field:'p_b',width:100,align:'center',title:'p-b'},
				{field:'rootSeq',width:100,align:'center',title:'根序列'},
				{field:'aauModel',width:80,align:'center',title:'AAU型号'},
				{field:'antennaManufacturer',width:100,align:'center',title:'天线厂家'},
				{field:'frameStructure',width:100,align:'center',title:'帧结构'},
				{field:'pdcchSymbol',width:100,align:'center',title:'pdcch符号数'}
				
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
		$("#tableDiv_showParam_5g").height(highTotal-30);
		$("#tableDiv_showParam_4g").height(highTotal-30);
	}
	function initTable(){
		$("#mainTable").datagrid({
			// 表头// field用于匹配远程json属性，width宽度,align居左右中对齐
			columns:columns,
			url:'${pageContext.request.contextPath}/projectParam/doPageListJson.action',
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
		        	$('#preview').linkbutton('enable');	
		        	$('#delectParam').linkbutton('enable');		
	        	}else{
	        		$('#export').linkbutton('disable');
	        		$('#preview').linkbutton('disable');
	        		$('#delectParam').linkbutton('disable');		
	        	}
	        }
		});
		
	}
	
	function initShowParam_5G(id,operatorType){
		$("#showParamTable_5g").datagrid({
			// 表头// field用于匹配远程json属性，width宽度,align居左右中对齐
			columns:columnsParams5G,
			url:'${pageContext.request.contextPath}/projectParam/doPageListJson.action?idsStr='+id+'&operatorType='+operatorType,
			title:'规划工参详情',
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
	        	
	        }
		});
	}
	
	function initShowParam_4G(id,operatorType){
		$("#showParamTable_4g").datagrid({
			// 表头// field用于匹配远程json属性，width宽度,align居左右中对齐
			columns:columnsParams4G,
			url:'${pageContext.request.contextPath}/projectParam/doPageListJson.action?idsStr='+id+'&operatorType='+operatorType,
			title:'规划工参详情',
			// 分页条
			pagination:true,
			pageSize:20,
			pageList:[10,20,50,100,500],
			//填满区域
			fit:true,
			//奇偶变色
			striped:true,
			// 行号
			rownumbers:true,
			// 工具栏 【表格上方功能按钮】
			//toolbar:'#toolbar' 
			onClickRow: function(rowIndex, rowData){
	        	
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
		var operatorType= 'planParamExpotExcel';
		if(nodes.length == 1){
			for(var i = 0; i < nodes.length;i++){
				if(i != 0){
					ids = ids + ',';
					operatorType = operatorType + ',';
				}
				ids = ids + nodes[i].id;
			}
		}else{
			$.messager.alert('提示','只能选择一条工参记录导出','info');
			return;
		}
		window.location= "${pageContext.request.contextPath}/projectParam/downloadExcel.action?idsStr="+ids+"&operatorType="+operatorType;
	}
	
	function previewExcel(){
			var nodes = $('#mainTable').datagrid('getSelections');
			var ids = '';
			var flag = false;
			var operatorType= 'planParamExpotExcel';
			if(nodes.length == 1){
				if(nodes[0].operatorType == "5G"){
					$('#tableDiv_showParam_5g').show();
					$('#tableDiv_showParam_4g').hide();
					initShowParam_5G(nodes[0].id,"5G");
				}else {
					$('#tableDiv_showParam_4g').show();
					$('#tableDiv_showParam_5g').hide();
					initShowParam_4G(nodes[0].id,"4G");
				}
				$('#previewExit').linkbutton('enable');
				$('#tableDiv').width("40%");
				$('#mainTable').datagrid('hideColumn','userName');
				$('#mainTable').datagrid('hideColumn','importDate');
				$('#mainTable').datagrid('hideColumn','totalCellNum');
				$('#mainTable').datagrid('resize');
			}else{
				$.messager.alert('提示','只能选择一条工参记录查看','info');
				return;
			}
	}
	
	
	function previewExcelExit(){
		$('#previewExit').linkbutton('disable');
		$('#tableDiv').width("100%");
		$('#tableDiv_showParam_5g').hide();
		$('#tableDiv_showParam_4g').hide();
		$('#mainTable').datagrid('showColumn','userName');
		$('#mainTable').datagrid('showColumn','importDate');
		$('#mainTable').datagrid('showColumn','totalCellNum');
		$('#mainTable').datagrid('resize');
	}
	
	function delectParam(){
		var nodes = $('#mainTable').datagrid('getSelections');
		var ids = '';
		var flag = false;
		var operatorType= 'planParamDeleteCell';
		var model= 'planParamDeleteCell';
		if(nodes.length == 1){
			for(var i = 0; i < nodes.length;i++){
				if(i != 0){
					ids = ids + ',';
					operatorType = operatorType + ',';
				}
				ids = ids + nodes[i].id;
			}
			$.messager.confirm('确认','您确认想要删除选中工参吗？',function(r){
			    if (r){
			        $.ajax({
			             type: "GET",
			             url: "${pageContext.request.contextPath}/projectParam/delectParams.action",
			             data: {'idsStr':ids,'operatorType':operatorType},
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
			$.messager.alert('提示','一次只能删除一条区域工参','info');
			return;
		}
		
	}
	
	</script>
  </head>
  
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
  	<shiroextend:hasAnyPermissions name="projectparam:import">
    	<a class="easyui-linkbutton" onclick="goToPage('${pageContext.request.contextPath}/projectParam/importPlanManageParamUI.action');" data-options="plain:false" style="width:100px;">导入</a>
	</shiroextend:hasAnyPermissions>
	<a id="query" class="easyui-menubutton" data-options="plain:false,menu:'#mm3'"  style="width:100px;">查询</a>
	<a id="export" class="easyui-linkbutton" data-options="plain:false,disabled:true"  onclick="exportExcel();" style="width:100px;">导出</a>
	<a id="preview" class="easyui-linkbutton" data-options="plain:false,disabled:true"  onclick="previewExcel();" style="width:100px;">预览模式</a>
	<a id="previewExit" class="easyui-linkbutton" data-options="plain:false,disabled:true"  onclick="previewExcelExit();" style="width:100px;">退出预览模式</a>
	<shiroextend:hasAnyPermissions name="projectparam:import">
		<a id="delectParam" class="easyui-linkbutton" data-options="plain:false,disabled:true"  onclick="delectParam();" style="width:100px;">删除</a>
	</shiroextend:hasAnyPermissions>
	<div style="padding-top: 4px;width:100%;">
		<div id="tableDiv" style="width:100%;float:left;margin-right: 10px;">
			<table id="mainTable" ></table>
		</div>
		<div id="tableDiv_showParam_5g" style="width:58%;float:left;display:none">
			<table id="showParamTable_5g" ></table>
		</div>
		<div id="tableDiv_showParam_4g" style="width:58%;float:left;display:none">
			<table id="showParamTable_4g" ></table>
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
