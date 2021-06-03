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
			//{field:'recSeqNo',checkbox:true}, // 一个对象代表一列 <th> 
			{field:'fileName',width:150,align:'center',title:'文件名',formatter:function(value,row,index){
				if(row.filelink){
					return '<a title="测试日志下载" href="${pageContext.request.contextPath}/testLogItem/downloadLog?recSeqNo='+row.recSeqNo+'" >'+value+'</a>';
				}
				return value;
			}}, 				
			{field:'startDate',width:90,align:'center',title:'开始时间'},
			{field:'endDate',width:90,align:'center',title:'结束时间'},
			{field:'testFileType',width:45,align:'center',title:'接口',
				formatter: function(value,row,index){
					if(0==value){
						return 'S1';
					}else if(1==value){
						return 'SGI';
					}
					return value;
				}
			},
			{field:'terminalGroup',width:45,align:'center',title:'区域'},
			{field:'moduleNo',width:45,align:'center',title:'通道号'},
			{field:'boxId',width:45,align:'center',title:'BOXID'},
			{field:'testFileStatus',width:75,align:'center',title:'预统计状态',
				formatter: function(value,row,index){
					if(0==value){
						return '上传成功,待统计';
					}else if(1==value){
						return '上传成功,统计中';
					}else if(2==value){
						return '上传成功,统计完成';
					}else{
						return '上传成功,统计失败';
					}
					return value;
				},
				styler: function(value,row,index){
					if(2!=value){
						return 'background-color:#34aaf1;';
					}
				}
			},
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
			//初始化开始时间和结束时间
			var nowDate=new Date();
			var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");		
			$("#endDate").datetimebox('setValue',dateString);
			nowDate.setDate(nowDate.getDate()-7);
			dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			$("#beginDate").datetimebox('setValue',dateString);
			initTable();
		});
		function initTable(){
			$("#testLogInfoTable").datagrid({
				// 表头
				columns:testLogInfoColumns,
				url:'${pageContext.request.contextPath}/networkTestLogItem/doPageListJson.action',
				title:'网络侧日志列表',
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
							window.self.location="${pageContext.request.contextPath}/networkTestLogItem/goImport.action";
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
				//获取通道号请求参数
				var galleryNos=[];
					var values = $("#galleryNo").combobox('getValues');
					for (var int2 = 0; int2 < values.length; int2++) {
						galleryNos.push(values[int2]);
					}
				var galleryNosString = galleryNos.join(",");
				$("#testLogInfoTable").datagrid('load',{
					beginDate:$("#beginDate").datetimebox('getValue'),
					endDate:$("#endDate").datetimebox('getValue'),
					cityIds:cityIdsString,
					boxIds:boxIdsString,
					galleryNo:galleryNosString,
					fileName:$("#fileName").textbox('getValue'),
					testFileStatus:$("#testFileStatus").combobox('getValue')
				});
			}
		}
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
			var nowDate=new Date();
			var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");		
			$("#endDate").datetimebox('setValue',dateString);
			nowDate.setDate(nowDate.getDate()-7);
			dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
			$("#beginDate").datetimebox('setValue',dateString);
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
	    	}" style="height:160px;overflow-y:auto;">
    			<form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >
	    			<!-- <div style="width:100%;height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;"><div class="panel-title">工程参数导入</div></div> -->
	   				<div class="inputDivShow">开始时间
			    		<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="required:true,editable:false" />
			    	</div>
			    	<div class="inputDivShow">结束时间
			    		<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="required:true,editable:false" />
			    	</div>
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
			    	<div class="inputDivShow">预统计状态
			    		<select id="testFileStatus" name="testFileStatus"  class="easyui-combobox" data-options="editable:false,multiple:false" >
			    			<option value="0">上传成功,待统计</option>
			    			<option value="1">上传成功,统计中</option>
							<option selected="selected" value="2">上传成功,统计完成</option>
			    		</select>
			    	</div>
			    	<div class="inputDivShow">通道号
			    		<select id="galleryNo" name=galleryNo  class="easyui-combobox"data-options="value:'',editable:false,multiple:true"  >
			    			<option value="0">通道0</option>
			    			<option value="1">通道1</option>
			    			<option value="2">通道2</option>
			    			<option value="3">通道3</option>
			    			<option value="4">通道4</option>
			    			<option value="5">通道5</option>
			    			<option value="6">通道6</option>
			    			<option value="7">通道7</option>
			    			<option value="8">通道8</option>
			    			<option value="9">通道9</option>
			    			
			    		</select>
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
