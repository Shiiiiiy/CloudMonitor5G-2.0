<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  <head>
    <title>网络侧日志列表页面</title>

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
		var testLogInfoColumns=[[
			{field:'recSeqNo',checkbox:true}, // 一个对象代表一列 <th> 
			{field:'fileName',width:150,align:'center',title:'文件名',formatter:function(value,row,index){
				/* if(row.filelink){
					return '<a title="测试日志下载" href="${pageContext.request.contextPath}/testLogItem/downloadLog?recSeqNo='+row.recSeqNo+'" >'+value+'</a>';
				} */
				return value;
			}}, 
			{field:'endDate',width:90,align:'center',title:'上传时间'},				
			{field:'terminalGroup',width:45,align:'center',title:'区域'},
			{field:'boxId',width:45,align:'center',title:'BOXID'},
			{field:'uploadedSize',width:75,align:'center',title:'文件大小(KB)',
				formatter: function(value2,row2,index2){
					if (value2){
						return (value2/1024).toFixed(2);
					}
					return value2;
				}
			}
		]];
		$(function(){
			initTable();
		});
		function initTable(){
			$("#testLogInfoTable").datagrid({
				// 表头
				columns:testLogInfoColumns,
				url:'${pageContext.request.contextPath}/networkTestLogItem/doPageListJson.action',
				title:'已上传日志列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				// 行号
				rownumbers:true,
				pagination:true,
				pageSize:20,
				pageList:[20,50,100,200,500,1000],
				scrollbarSize:0,
				toolbar:[
				 	// 每个对象就是一个按钮
					{id:'upload',text:'上传日志',iconCls:'icon-upload',
						handler: function(){
							window.self.location="${pageContext.request.contextPath}/logUpload/goImport.action";
						}
					}
				]
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
					fileName:$("#fileName").textbox('getValue')
				});
			}
		}
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
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
		
		function deleteLog(){
			var nodes = $('#testLogInfoTable').datagrid('getSelections');
			var ids = '';
			var siteNames = '';
			var flag = false;
			if(nodes.length > 0){
				for(var i = 0; i < nodes.length;i++){
					if(i != 0){
						ids = ids + ',';
					}
					ids = ids + nodes[i].recSeqNo;
				}
			}else{
				$.messager.alert('提示','请选择需要删除的日志','info');
				return "";
			}
			
			$.messager.confirm("系统提示", "注意:日志有可能在解析中,确认继续删除选中日志吗？", function(result) {
				if (result) {
					$.ajax({
			             type: "GET",
			             url: "${pageContext.request.contextPath}/logUpload/deleteLog.action",
			             //data: {'cellName':cellName,'id':id,'event':event},
			             data: {'idsStr':ids},
			             dataType: "json",
			             success: function(data){
			            	if(data.errorMsg){
		 	             		$.messager.alert('提示',data.errorMsg,'error');
		 	             	}else{
		 	             		$.messager.alert('提示',"日志已删除",'info');
		 	             		$("#testLogInfoTable").datagrid("reload");
		 	             	}
		              	}
			         });
				}
			});
			
		}
	</script>
  </head>
  
  <body  style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;padding:4px;top:4px;left:4px;position: absolute;">
	    	<div data-options="region:'center',border:false" >
	    		<table id="testLogInfoTable"> </table>
	    	</div>
	    	<div data-options="region:'north',title:'筛选条件设置',onCollapse:function(){
	    		//收束的时候显示title
	    		var title = $('#ly').layout('panel','north').panel('options').title;
	    		$('.layout-expand-north .panel-title').html(title);
	    	}" style="height:120px;overflow-y:auto;">
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
			    	<div class="inputDivShow">文件名
			    		<input id="fileName" name="fileName"  class="easyui-textbox" data-options="validType:'length[1,30]'"/>
			    	</div>
			    	<table width="100%">
			    		<tr>
				    		<td width="48%" align="right"><a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a></td>
				    		<td width="6%" align="right"><a class="easyui-linkbutton" onclick="deleteLog();" style="width: 80px;" data-options="iconCls:'icon-search'" >删除</a></td>
				    		<td width="48%" align="left"><a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a></td>
			    		</tr>
			    	</table>
		    	</form>
   			</div>
    	</div>
  </body>
</html>
