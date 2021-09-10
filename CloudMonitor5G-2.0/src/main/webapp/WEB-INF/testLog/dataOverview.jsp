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
		//{field:'id',hidden:true}, 
		{field:'kk01',width:100,align:'center',title:'5G 网络覆盖率'}, 	
		{field:'kk02',width:100,align:'center',title:'5G SA时长驻留比'},
		{field:'kk03',width:100,align:'center',title:'5G NSA时长驻留比'},
		{field:'kk04',width:100,align:'center',title:'4G 时长驻留比'},
		{field:'kk05',width:100,align:'center',title:'NR业务建立成功率'}, 	
		{field:'kk06',width:100,align:'center',title:'NR 里程掉线比'},
		{field:'kk07',width:100,align:'center',title:'5G切换成功率(系统内)'},
		{field:'kk08',width:100,align:'center',title:'5G->4G重定向成功率'},
		{field:'kk09',width:100,align:'center',title:'4G->5G重定向成功率'}, 	
		{field:'kk10',width:100,align:'center',title:'SA切换用户面时延(ms)'},
		{field:'kk11',width:100,align:'center',title:'下行平均吞吐率'},
		{field:'kk12',width:100,align:'center',title:'下行低速占比（低于100Mbps）'},
		{field:'kk13',width:100,align:'center',title:'下行高速占比（高于800Mbps）'}, 	
		{field:'kk14',width:100,align:'center',title:'上行平均吞吐率'},
		{field:'kk15',width:100,align:'center',title:'上行低速占比（低于10Mbps）'},
		{field:'kk16',width:100,align:'center',title:'上行高速占比（高于160Mbps）'},
		{field:'kk17',width:100,align:'center',title:'下载成功率'}, 	
		{field:'kk18',width:100,align:'center',title:'下行平均速率'},
		{field:'kk19',width:100,align:'center',title:'下行低速占比（低于100Mbps）'},
		{field:'kk20',width:100,align:'center',title:'下行高速占比（高于800Mbps）'},
		{field:'kk21',width:100,align:'center',title:'VoLTE主叫EPS FB接通率'}, 	
		{field:'kk22',width:100,align:'center',title:'VoLTE主叫掉线率'},
		{field:'kk23',width:100,align:'center',title:'FR成功率'}
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
		//console.log(location.href.split('?')[1].split('&')[0])
		$("#mainTable").datagrid({
			// 表头// field用于匹配远程json属性，width宽度,align居左右中对齐
			//columns:columns,//url="${pageContext.request.contextPath}/unicomLogItem/seeInfoData.action"
			url:'${pageContext.request.contextPath}/unicomLogItem/seeInfoData.action?'+location.href.split('?')[1].split('&')[0],
			title:'数据概览',
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
			onClickRow: function(rowIndex, rowData){
	        	var nodes = $('#mainTable').datagrid('getSelections');
	        	if(nodes.length > 0){
		        	$('#export').linkbutton('enable');
	        	}else{
	        		$('#export').linkbutton('disable');	
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
		window.location= "${pageContext.request.contextPath}/unicomLogItem/downloadDataOverview.action?"+location.href.split('?')[1].split('&')[0];
	}
	
	function goToUnicomLogList(){
		goToPage('${pageContext.request.contextPath}/unicomLogItem/unicomLogItemListUI.action');
	}
	
	</script>
  </head>
  
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
  	<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;" onclick="goToUnicomLogList();" >返回</a>
	<a id="export" class="easyui-linkbutton" data-options="plain:false,disabled:false"  onclick="exportExcel();" style="width:100px;">导出</a>
	<div style="padding-top: 4px;width:100%;">
		<div id="tableDiv" style="width:100%;float:left;margin-right: 10px;">
			<table id="mainTable" >
				<thead frozen="true">
					<tr>
						<th field='log_name' width="160px" style="text-align:center">文件名</th>
					</tr>
				</thead>
				<thead>
					<tr>
						<th colspan="4">基础覆盖与质量</th>
						<th colspan="6">接入与移动性能</th>
						<th colspan="6">数据业务体验(FTP)</th>
						<th colspan="4">数据业务体验(部分路段百度云下载)</th>
						<th colspan="3">语音业务体验</th>
						<th colspan="11">感知</th>
					</tr>
					<tr>
						<th field='kk01' width="100px" style="text-align:center">5G 网络覆盖率</th>
						<th field='kk02' width="110px" style="text-align:center">5G SA时长驻留比</th>
						<th field='kk03' width="120px" style="text-align:center">5G NSA时长驻留比</th>
						<th field='kk04' width="100px" style="text-align:center">4G 时长驻留比</th>
						<th field='kk05' width="120px" style="text-align:center">NR业务建立成功率</th> 	
						<th field='kk06' width="100px" style="text-align:center">NR 里程掉线比</th>
						<th field='kk07' width="140px" style="text-align:center">5G切换成功率(系统内)</th>
						<th field='kk08' width="140px" style="text-align:center">5G->4G重定向成功率</th>
						<th field='kk09' width="140px" style="text-align:center">4G->5G重定向成功率</th>
						<th field='kk10' width="140px" style="text-align:center">SA切换用户面时延(ms)</th>
						<th field='kk11' width="100px" style="text-align:center">下行平均吞吐率</th>
						<th field='kk12' width="180px" style="text-align:center">下行低速占比（低于100Mbps）</th>
						<th field='kk13' width="180px" style="text-align:center">下行高速占比（高于800Mbps）</th>	
						<th field='kk14' width="100px" style="text-align:center">上行平均吞吐率</th>
						<th field='kk15' width="180px" style="text-align:center">上行低速占比（低于10Mbps）</th>
						<th field='kk16' width="180px" style="text-align:center">上行高速占比（高于160Mbps）</th>
						<th field='kk17' width="80px" style="text-align:center">下载成功率</th> 	
						<th field='kk18' width="85px" style="text-align:center">下行平均速率</th>
						<th field='kk19' width="180px" style="text-align:center">下行低速占比（低于100Mbps）</th>
						<th field='kk20' width="180px" style="text-align:center">下行高速占比（高于800Mbps）</th>
						<th field='kk21' width="160px" style="text-align:center">VoLTE主叫EPS FB接通率</th>	
						<th field='kk22' width="120px" style="text-align:center">VoLTE主叫掉线率</th>
						<th field='kk23' width="90px" style="text-align:center">FR成功率</th>
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
