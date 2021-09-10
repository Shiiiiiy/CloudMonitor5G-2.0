<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>业务详单</title>

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
			//columns:columns,//url="${pageContext.request.contextPath}/unicomLogItem/seeInfoData.action"
			url:'${pageContext.request.contextPath}/knowFeeling/seeInfo.action?taskId=${taskId}',
			title:'业务详单',
			//fitColumns:true,
			// 分页条
			//pagination:true,
			pageSize:20,
			pageList:[10,20,50,100,500,1000],
			//填满区域
			fit:true,
			//奇偶变色
			striped:true,
			// 行号
			rownumbers:true,
			// 工具栏 【表格上方功能按钮】
			//toolbar:'#toolbar'
			/**
			onClickRow: function(rowIndex, rowData){
	        	var nodes = $('#mainTable').datagrid('getSelections');
	        	if(nodes.length > 0){
		        	$('#export').linkbutton('enable');
	        	}else{
	        		$('#export').linkbutton('disable');	
	        	}
	        }
			 **/
		});
		
	}

	
	function exportExcel(){
		window.location= "${pageContext.request.contextPath}/knowFeeling/downloadDataOverview.action?taskId=${taskId}";
	}
	
	function goListPage(){
		goToPage('${pageContext.request.contextPath}/knowFeeling/listUI.action');
	}
	
	</script>
  </head>
  
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
  	<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;" onclick="goListPage();" >返回</a>
	<a id="export" class="easyui-linkbutton" data-options="plain:false,disabled:false"  onclick="exportExcel();" style="width:100px;">导出</a>
	<div style="padding-top: 4px;width:100%;">
		<div id="tableDiv" style="width:100%;float:left;margin-right: 10px;">
			<table id="mainTable" >
				<thead frozen="true">
					<tr>
						<th field='file_name' width="120px" style="text-align:center">日志名</th>
						<th field='area' width="50px" style="text-align:center">区域</th>
						<th field='operator_name' width="60x" style="text-align:center">运营商</th>
						<th field='traffic' width="70px" style="text-align:center">业务类型</th>
						<th field='starttime' width="150px" style="text-align:center">开始时间</th>
						<th field='endtime' width="150px" style="text-align:center">结束时间</th>
					</tr>
				</thead>
				<thead>
					<tr>
						<th colspan="9">物理层</th>
						<th colspan="7">链路层</th>
						<th colspan="4">应用层</th>
					</tr>
					<tr>
						<th field='kf01' width="90px" style="text-align:center">网络制式</th>
						<th field='kf02' width="90px" style="text-align:center">LTE RSRP</th>
						<th field='kf03' width="90px" style="text-align:center">LTE SINR</th>
						<th field='kf04' width="90px" style="text-align:center">NR RSRP</th>
						<th field='kf05' width="90px" style="text-align:center">NR SINR</th>
						<th field='kf06' width="140px" style="text-align:center">LTE RRC连接建立成功率</th>
						<th field='kf07' width="140px" style="text-align:center">NR RRC连接建立成功率</th>
						<th field='kf08' width="100px" style="text-align:center">LTE 切换成功率</th>
						<th field='kf09' width="100px" style="text-align:center">NR 切换成功率</th>
						<th field='kf11' width="100px" style="text-align:center">DNS 解析时延</th>
						<th field='kf12' width="100px" style="text-align:center">DNS 解析成功率</th>
						<th field='kf13' width="100px" style="text-align:center">TCP 建链时延</th>
						<th field='kf14' width="100px" style="text-align:center">TCP 建链成功率</th>
						<th field='kf15' width="100px" style="text-align:center">TCP 重传率</th>
						<th field='kf16' width="120px" style="text-align:center">HTTP 响应时延</th>
						<th field='kf17' width="120px" style="text-align:center">HTTP 响应成功率</th>
						<th field='kf21' width="80px" style="text-align:center">文本加载时延</th>
						<th field='kf22' width="80px" style="text-align:center">图片加载时延</th>
						<th field='kf23' width="80px" style="text-align:center">视频加载时延</th>
						<th field='kf24' width="80px" style="text-align:center">音频加载时延</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
  </body>
</html>
