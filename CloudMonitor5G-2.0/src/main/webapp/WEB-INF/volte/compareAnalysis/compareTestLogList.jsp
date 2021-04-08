<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  <head>
    <title>VoLTE专题----VoLTE对比分析----测试日志列表页面</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
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
			{field:'recSeqNo',checkbox:true}, // 一个对象代表一列 <th> 
			{field:'fileName',width:150,align:'center',title:'文件名',formatter:function(value,row,index){
				if(row.filelink){
					return '<a title="测试日志下载" href="${pageContext.request.contextPath}/testLogItem/downloadLog?recSeqNo='+row.recSeqNo+'" >'+value+'</a>';
				}
				return value;
			}}, 				
			{field:'startDate',width:90,align:'center',title:'开始时间'},
			{field:'endDate',width:90,align:'center',title:'结束时间'},
			{field:'terminalGroup',width:45,align:'center',title:'区域'},
			{field:'logSource',width:60,align:'center',title:'数据来源',
				formatter: function(value1,row1,index1){
					if(0==value1||2==value1){
						return '室外';
					}else if(1==value1||3==value1){
						return '室内';
					}
					return value1;
				}
			},
			{field:'operatorName',width:45,align:'center',title:'运营商'},
			{field:'testFileStatus',width:75,align:'center',title:'预统计状态',
				formatter: function(value,row,index){
					if(0==value||1==value){
						return '统计中';
					}else if(2==value){
						return '完成';
					}else{
						return '统计失败';
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
				url:'${pageContext.request.contextPath}/testLogItem/doPageListJson.action?isFinished=true&isCompare=true',
				title:'可选对比日志列表',
				fitColumns:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				tools:'#tt3',
				// 行号
				rownumbers:true,
				pagination:true,
				pageSize:20,
				pageList:[10,20,50,100,200,500,1000],
				scrollbarSize:0,
				onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
		            if (data.rows.length > 0) {
		            	var checkAllDisable = false;
		                //循环判断操作为新增的不能选择
		                for (var i = 0; i < data.rows.length; i++) {
		                    //根据operate让某些行不可选
		                    if (2 != data.rows[i].testFileStatus) {
		                    	checkAllDisable = true;
		                        $("input[type='checkbox']")[i + 1].disabled = true;
		                    }
		                }
		                if(checkAllDisable){
		                	$(".datagrid-header-check > input[type='checkbox']").attr("disabled","disabled"); 
		                }else{
		                	$(".datagrid-header-check > input[type='checkbox']").removeAttr("disabled"); 
		                }
		            }
		        },
		        onClickRow: function(rowIndex, rowData){
		            //加载完毕后获取所有的checkbox遍历
		            $("input[type='checkbox']").each(function(index, el){
		                //如果当前的复选框不可选，则不让其选中
		                if (el.disabled == true) {
		                    $("#testLogInfoTable").datagrid('unselectRow', index - 1);
		                }
		            });
		        } 
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
				//获取数据来源请求参数
				var logSource=[];
				var logSourcevalues = $("#logSource").combobox('getValues');
				for (var int2 = 0; int2 < logSourcevalues.length; int2++) {
					logSource.push(logSourcevalues[int2]);
				}
				var logSourceString = logSource.join(",");
				//获取业务类型请求参数
				var serviceType=[];
				var serviceTypevalues = $("#serviceType").combobox('getValues');
				for (var int3 = 0; int3 < serviceTypevalues.length; int3++) {
					serviceType.push(serviceTypevalues[int3]);
				}
				var serviceTypeString = serviceType.join(",");
				//获取运营商请求参数
				var operators=[];
				var operatorsvalues = $("#operators").combobox('getValues');
				for (var int4 = 0; int4 < operatorsvalues.length; int4++) {
					operators.push(operatorsvalues[int4]);
				}
				var operatorsString = operators.join(",");
				$("#testLogInfoTable").datagrid('load',{
					beginDate:$("#beginDate").datetimebox('getValue'),
					endDate:$("#endDate").datetimebox('getValue'),
					cityIds:cityIdsString,
					//prestorecityIds:$("#prestorecityIds").combotree('getValue'),
					logSource:logSourceString,
					serviceType:serviceTypeString,
					operators:operatorsString,
					boxIds:boxIdsString,
					fileName:$("#fileName").textbox('getValue'),
					testFileStatus:$("#testFileStatus").combobox('getValue')
				});
			}
			
		}
		/* 重置表单 */
		function resetForm(){
			$("#queryForm").form('reset');
			//初始化开始时间和结束时间
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
		/* 开始分析,分析按钮 */
		function doAnalysis(){
			var testLogItemsRows = $("#testLogInfoTable").datagrid('getSelections');//获取表格中用户选中 所有数据
			if(testLogItemsRows.length==0){// 操作前至少选中一条
				$.messager.alert("提示","请选择某些日志!",'warning');
				return ;
			}
			var idArray = new Array();
			//var logSize = 0;
			for(var i=0; i<testLogItemsRows.length;i++){
				idArray.push(testLogItemsRows[i].recSeqNo);
				//logSize += testLogItemsRows[i].uploadedSize;
			}
			//去除筛选日志大小的限制
			//if(logSize>1024*1024*2){
			//	$.messager.alert("提示","所选日志大小超出[2G]限制!",'warning');
			//	return ;
			//}
			// 将选中多条记录id 用","拼接为一个字符串
			var ids = idArray.join(",");
			// 以ajax方式，发送到服务器，完成操作
			$.post("${pageContext.request.contextPath}/voiceCompare/saveCompareTestLogItem",{"compareTestLogItemIds":ids},
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						goToPage('${pageContext.request.contextPath}/voiceCompare/goToCompareAnalysis.action');
					}
				}
			,"json");
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
			    	<div class="inputDivShow">预存区域
			    		<select id="prestorecityIds" name="prestorecityIds" class="easyui-combotree" style="margin:none;" data-options="panelWidth:200,readonly:true,disabled:true,editable:false,multiple:true" >
			    		</select>
			    	</div>
			    	<div class="inputDivShow">数据来源
			    		<select id="logSource" name="logSource"  class="easyui-combobox" data-options="editable:false,multiple:true" >
			    			<option selected="selected" value="0">室外自动路测</option>
							<option value="1">室内自动路测</option>
							<option value="2">Miou室外路测</option>
							<option value="3">Miou室内测试</option>
			    		</select>
			    	</div>
			    	<div class="inputDivShow">业务类型
			    		<select id="serviceType" name="serviceType" class="easyui-combobox" data-options="editable:false,multiple:true"  >
			    			<option value="0">空闲测试</option>
							<option selected="selected" value="1">语音测试</option>
							<option value="2">VoLTE视频电话</option>
							<option value="3">网页视频业务</option>
							<option value="4">PDP/Attach</option>
							<option value="5">Ping测试</option>
							<option value="6">FTP上传/下载</option>
							<option value="7">流媒体测试</option>
							<option value="8">Http测试</option>
			    		</select>
			    	</div>
			    	<div class="inputDivShow">运营商
			    		<select id="operators" name="operators"  class="easyui-combobox" data-options="editable:false,multiple:true" >
			    			<option selected="selected" value="中国移动">中国移动</option>
							<option value="中国联通">中国联通</option>
							<option value="中国电信">中国电信</option>
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
			    			<option value="1">统计中</option>
							<option selected="selected" value="2">已完成</option>
			    		</select>
			    	</div>
			    	<table width="100%">
			    		<tr>
				    		<td width="100%" align="center">
				    			<a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a>
				    			<a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a>
				    			<a class="easyui-linkbutton" onclick="goToPage('${pageContext.request.contextPath}/voiceCompare/goToCompareAnalysis.action');" style="width: 80px;" data-options="iconCls:'icon-undo'" >返回</a>
				    		</td>
			    		</tr>
			    	</table>
		    	</form>
   			</div>
    		<div id="tt3">
				<a href="#" style="width:75px;text-decoration:underline;"  onclick="doAnalysis();" title="对比日志分析">对比日志分析</a>
			</div>
    	</div>
  </body>
</html>
