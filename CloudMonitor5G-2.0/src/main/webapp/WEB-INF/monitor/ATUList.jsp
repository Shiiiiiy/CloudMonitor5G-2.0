<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  <head>
    <title>ATU日志登陆</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
		<style type="text/css">
		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 255px;
   		}
   		.inputDivShow input{
   			width:140px;
   		}
   		.inputDivShow select{
   			width:140px;
   		}
	</style>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
		// field用于匹配远程json属性，width宽度,align居左右中对齐
		var testLogInfoColumns=[[ 
			{field:'boxId',width:60,align:'center',title:'BOX_ID'},
			{field:'loginTime',width:90,align:'center',title:'登录时间'},
			{field:'offlineTime',width:90,align:'center',title:'离线时间'},
			{field:'status',width:60,align:'center',title:'登录状态'},
			{field:'failReason',width:70,align:'center',title:'登录失败原因'},
			//{field:'offlineReason',width:70,align:'center',title:'离线原因'},
			{field:'sessionId',width:45,align:'center',title:'会话ID'},
			{field:'testPlanVersion',width:45,align:'center',title:'测试计划版本'}
		]];
		$(function(){

			/* //初始化开始时间和结束时间
			var nowDate=new Date();
			var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");		
			$("#endDate").datetimebox('setValue',dateString);
			nowDate.setDate(nowDate.getDate()-7);
			dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			$("#beginDate").datetimebox('setValue',dateString); */
			initTable();
		});
		function initTable(){
			$("#testLogInfoTable").datagrid({
				// 表头
				columns:testLogInfoColumns,
				url:'${pageContext.request.contextPath}/ATULogin/doPageListJson.action',
				title:'登陆日志列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				// 行号
				rownumbers:true,
				pagination:true,
				loadMsg:0,
				pageSize:20,
				pageList:[10,20,50,100,200,500,1000],
				scrollbarSize:0
				
			});
			
		}
		/* 多条件查询 */
		function query(){
			if($("#queryForm").form('validate')){
				//获取cityIds请求参数
				var areaTree = $("#areaTree").combotree('tree');
				var checkNodes = areaTree.tree('getChecked');
				var cityIds = [];
				for (var int = 0; int < checkNodes.length; int++) {
					if(checkNodes[int].attributes.type=='City'){
						cityIds.push(checkNodes[int].attributes.refId);
					}
				}
				var cityIdsString = cityIds.join(",");
				//获取boxIds请求参数
				var boxIds=[];
				if(cityIdsString){
					var values = $("#boxIds").combobox('getValues');
					for (var int1 = 0; int1 < values.length; int1++) {
						boxIds.push(values[int1]);
					}
				}
				var boxIdsString = boxIds.join(",");
				
				$("#testLogInfoTable").datagrid('load',{
					cityIds:cityIdsString,
					boxIds:boxIdsString,
					beginDate:$("#beginDate").datetimebox('getValue'),
					endDate:$("#endDate").datetimebox('getValue')
				});
			}
		}
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
			/* var nowDate=new Date();
			var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");		
			$("#endDate").datetimebox('setValue',dateString);
			nowDate.setDate(nowDate.getDate()-7);
			dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			$("#beginDate").datetimebox('setValue',dateString); */
			
		}
		/* 初始化BOXIDS */
		function initBoxId(newValue,oldValue){
			var areaTree = $("#areaTree").combotree('tree');
			var checkNodes = areaTree.tree('getChecked');
			var cityIds = [];
			for (var int = 0; int < checkNodes.length; int++) {
				if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
					cityIds.push(checkNodes[int].attributes.refId);
				}
			}
			var cityIdsString = cityIds.join(",");
			$("#boxIds").combobox({
				url:'${pageContext.request.contextPath}/terminalGroup/terminalTree.action?cityIds='+cityIdsString,
				method: 'post',
				panelWidth:200,
				editable:false,
				multiple:true,
				valueField:'value',
				textField:'text',
				groupField:'group'
			});
		}
		//实时刷新时间单位为毫秒
		setInterval('refreshQuery()',8000); 
		/* 刷新查询 */
		function refreshQuery(){
			$("#testLogInfoTable").datagrid('reload',null);
		}
	</script>
  </head>
  
  <body  style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	
    	<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;padding:4px;top:4px;left:4px;position: absolute;">
	   
	    	<div  data-options="region:'center',border:false" >
	    		<table id="testLogInfoTable"> </table>
	    		
	    	</div>
    
	    	<div data-options="region:'north',title:'筛选条件设置',onCollapse:function(){
	    		//收束的时候显示title
	    		var title = $('#ly').layout('panel','north').panel('options').title;
	    		$('.layout-expand-north .panel-title').html(title);
	    	}" style="height:160px;overflow-y:auto;">
    			<form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
	    			<!-- <div style="width:100%;height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;"><div class="panel-title">工程参数导入</div></div> -->
	   				
	   				<div class="inputDivShow">区域
			    		<select id="areaTree" name="cityIds"  class="easyui-combotree" data-options="panelWidth:200,onChange:initBoxId,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:false,url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action'"  >
			    		</select>
			    	</div>
			    	<div class="inputDivShow">测试终端
			    		<select id="boxIds" name="boxIds"  class="easyui-combobox"  >
			    		</select>
			    	</div>
			    	
			    	<div class="inputDivShow">在线开始时间
			    		<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="required:false,editable:false" />
			    	</div>
			    	<div class="inputDivShow">在线结束时间
			    		<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="required:false,editable:false" />
			    	</div>
			    	<table width="100%">
			    		<tr>
				    		<td width="50%" align="right"><a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a></td>
				    		<td width="50%" align="left"><a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a></td>
			    		</tr>
			    	</table>
		    	</form>
   			</div>
    		
    	</div>
    	
  </body>
</html>
