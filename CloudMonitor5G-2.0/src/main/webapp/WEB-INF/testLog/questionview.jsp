<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>问题索引</title>

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
	var columns=[[
		{field:'id',width:100,align:'center',title:'序号'},
		{field:'type',width:100,align:'center',title:'类别'}, 	
		{field:'evtCnName',width:100,align:'center',title:'事件'},
		{field:'time',width:160,align:'center',title:'时间戳'},
		{field:'Long',width:100,align:'center',title:'经度'},
		{field:'Lat',width:100,align:'center',title:'维度'}, 	
		{field:'logName',width:180,align:'center',title:'日志名'}
	]];
	
	$(function(){
		initHeight();
		initTable();
	});
	//重新加载
	/* window.onresize=reload;
	function reload(){
		initHeight();
	} */
	
	function add0(m){
		return m<10?'0'+m:m 
	}
	
	/* 初始化高度 */
	function initHeight(){
		var highTotal = $(document.body).outerHeight();
		$("#tableDiv").height(highTotal-30);
	}
	
	
	var testLogItemIds = "",_questionIndex = "";
	function initTable(){
		//console.log(location.href.split('?')[1])
		var conditionStr = location.href.split('?')[1];
		testLogItemIds = conditionStr.split('&')[0].split('=')[1];
		_questionIndex = conditionStr.split('&')[1].split('=')[1];
		
		//console.log(testLogItemIds+ ";" +questionIndex)
		
		/* $("#mainTable").datagrid({
			url:'${pageContext.request.contextPath}/unicomLogItem/seeQuestionviewData.action?testLogItemIds='+testLogItemIds+"&questionIndex="+_questionIndex,
			title:'问题索引',
			columns:columns,
			fit:true,
			striped:true,
			fitColumns:true
			//rownumbers:true
		}); */
		
		$.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/unicomLogItem/seeQuestionviewData.action",
            data: {
            	'testLogItemIds': testLogItemIds,
            	'questionIndex': _questionIndex
            },
            dataType: "json",
            success: function(data){
				$("#mainTable").datagrid({
					title:'问题索引',
					columns:columns,
					data: data,
					fit:true,
					striped:true,
					fitColumns:true
					//rownumbers:true
				});		
          	}
   	    }); 
		
	}
	
	
	
	
	function query(){
		var questionIdx = $("#questionIdx").combotree('tree');
		var checkNodes = questionIdx.tree('getChecked');
/* 		if(checkNodes.length < 1){// 操作前至少选中一条
			$.messager.alert("提示","请选择问题索引!",'warning');
			return ;
		} */
		var questionIdxs = [];
		for (var int = 0; int < checkNodes.length; int++) {
			if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
				questionIdxs.push(checkNodes[int].attributes.refId);
			}
		}
		var questionIdxsString = questionIdxs.join(","); 
		
		/* $("#mainTable").datagrid('load',{
			testLogItemIds:testLogItemIds+"",
			questionIndex:questionIdxsString
		}); */
		
		$.ajax({
            type: "POST",
            url: "${pageContext.request.contextPath}/unicomLogItem/seeQuestionviewData.action",
            data: {
            	'testLogItemIds': testLogItemIds,
            	'questionIndex':questionIdxsString
            },
            dataType: "json",
            success: function(data){
				$("#mainTable").datagrid('loadData',data);	
          	}
   	    }); 
	}
	
	
	function download(flag){
		var questionIdx = $("#questionIdx").combotree('tree');
		var checkNodes = questionIdx.tree('getChecked');
		var questionIdxs = [];
		for (var int = 0; int < checkNodes.length; int++) {
			if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
				questionIdxs.push(checkNodes[int].attributes.refId);
			}
		}
		var questionIdxsString = questionIdxs.join(","); 
		
		if(flag){//1
			window.location= "${pageContext.request.contextPath}/unicomLogItem/downloadQuestion.action?testLogItemIds="+testLogItemIds+"&questionIndex="+questionIdxsString;
		}else{
			window.location= "${pageContext.request.contextPath}/unicomLogItem/downloadQuestion.action?testLogItemIds="+testLogItemIds+"&questionIndex="+_questionIndex;
		}
		
	}
	
	
	
	function exportExcel(){
		window.location= "${pageContext.request.contextPath}/unicomLogItem/downloadDataOverview.action?"+location.href.split('?')[1].split('&')[0];
	}
	
	function goToUnicomLogList(){
		goToPage('${pageContext.request.contextPath}/unicomLogItem/unicomLogItemListUI.action');
	}
	
	</script>
  </head>
  
  <body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;padding:4px;top:4px;left:4px;position: absolute;">
	   
	    	<div data-options="region:'center',border:false" >
	    		<table id="mainTable"> </table>
	    	</div>
    
	    	<div data-options="region:'north',title:'筛选条件设置',onCollapse:function(){
	    		//收束的时候显示title
	    		var title = $('#ly').layout('panel','north').panel('options').title;
	    		$('.layout-expand-north .panel-title').html(title);
	    	}" style="height:120px;overflow-y:auto;">
	    			<!-- <div style="width:100%;height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;"><div class="panel-title">工程参数导入</div></div> -->
    			<div style="width:100%;height: 100%;margin:0;" >
	   				<!-- <div class="inputDivShow">开始时间
			    		<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="required:false,editable:true" />
			    	</div>
			    	<div class="inputDivShow">结束时间
			    		<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="required:false,editable:true" />
			    	</div> -->
			    
			    	<div class="inputDivShow">问题索引
			    		<select id="questionIdx" name="questionIdx"  class="easyui-combotree" data-options="width:200,panelWidth:200,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:false,url:'${pageContext.request.contextPath}/unicomLogItem/questionTree.action'">
			    		</select>
			    	</div>
			    	<table width="100%">
			    		<tr>
				    		<td width="30%" align="right"><a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a></td>
				    		<td width="5%" align="center">
								<a class="easyui-menubutton" data-options="menu:'#_type'" style="width: 100px;">下载</a>
			    				<div id="_type">
			    					<div onclick="download(0)">全部</div>
			    					<div onclick="download(1)">筛选后</div>
			    				</div>
							</td>
			    			<td width="45%" align="left"><a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;" onclick="goToUnicomLogList();" >返回</a></td>
			    		</tr>
			    	</table>
		    	</div>
   			</div>
    		
    	</div>
    	
		
		
		
  </body>
</html>
