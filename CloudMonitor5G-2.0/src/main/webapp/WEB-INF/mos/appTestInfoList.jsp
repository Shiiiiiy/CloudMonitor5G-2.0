<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:100%;height:100%;margin: 0 auto;padding: 0 auto;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>APP测试信息列表</title>
<%@ include file="../../taglibs/jquery.jsp" %>
<%@ include file="../../taglibs/easyui.jsp" %>
<script type="text/javascript">
	var clearTask;
	$(function(){
		initHeight();
		initTable();
		flush(5);
	});
	//重新加载
	window.onresize=reload;
	function reload(){
		initHeight();
	}
	/* 初始化高度 */
	function initHeight(){
		var highTotal = $(document.body).outerHeight();
		$("#mainTableDiv").height(highTotal-85);
	}
	function initTable(){
		$("#mainTable").datagrid({
			// 表头
			columns:[[
				{field:'timestamp',width:120,align:'center',title:'时间戳'},
				/* {field:'imei',width:120,align:'center',title:'IMEI'}, */
				{field:'imsi',width:120,align:'center',title:'IMSI'},
				{field:'mkr',width:80,align:'center',title:'手机厂家'},
				{field:'model',width:80,align:'center',title:'手机型号'},
				{field:'networkType',width:60,align:'center',title:'网络类型(LTE/GSM/NR)'},
				{field:'mcc',width:60,align:'center',title:'MCC'},
				{field:'mnc',width:60,align:'center',title:'MNC'},
				{field:'nrTac',width:80,align:'center',title:'NR TAC'},
				{field:'nrArfcn',width:80,align:'center',title:'NR ARFCN'},
				{field:'nrPci',width:80,align:'center',title:'NR PCI'},
				{field:'nrRsrp',width:80,align:'center',title:'NR RSRP'},
				{field:'nrSinr',width:80,align:'center',title:'NR SINR'},
				{field:'lac_ci',width:80,align:'center',title:'LAC/CI'},
				{field:'tac_eci',width:80,align:'center',title:'TAC/ECI'},
				{field:'earfcn',width:80,align:'center',title:'EARFCN'}, 
				{field:'pci',width:60,align:'center',title:'LTE PCI'},
				/* {field:'network',width:60,align:'center',title:'信号强度',
					formatter:function(value,row,index){
						return "-";
					}
				}, */
				{field:'lteNetwork',width:80,align:'center',title:'LTE信号强度'},
				{field:'sinr',width:60,align:'center',title:'LTE SINR'},
				{field:'pingDelay',width:60,align:'center',title:'PING时延(ms)'},
				{field:'upSpeedAvg',width:90,align:'center',title:'上传平均速度(Mbps)',
					formatter:function(value,row,index){
						var rlt = value/1000*8;
						return rlt.toFixed(1);
					}
				},
				{field:'downSpeedAvg',width:90,align:'center',title:'下载平均速度(Mbps)',
					formatter:function(value,row,index){
						var rlt = value/1000*8;
						return rlt.toFixed(1);
					}
				},
				{field:'mosScore',width:60,align:'center',title:'MOS值'},
				{field:'vmos',width:60,align:'center',title:'VMOS值'},
				{field:'sQuality',width:80,align:'center',title:'视频质量分'},
				{field:'sLoading',width:100,align:'center',title:'视频始缓冲分'},
				{field:'sStalling',width:80,align:'center',title:'视频卡顿分'},
				{field:'httpDlRate',width:80,align:'center',title:'HTTP下载速率(Mbps)'},
				{field:'httpTimeDelay',width:80,align:'center',title:'HTTP浏览时延(ms)'},
				{field:'latitude',width:80,align:'center',title:'纬度'},
				{field:'longitude',width:80,align:'center',title:'经度'},
				
				{field:'province',width:80,align:'center',title:'省'},
				{field:'city',width:80,align:'center',title:'市'},
				{field:'district',width:80,align:'center',title:'区'}
				/*{field:'phoneNUmber',width:80,align:'center',title:'手机号'},
				{field:'friendRate',width:80,align:'center',title:'朋友圈打开速率'}, */
				
			]],
			url:'${pageContext.request.contextPath}/mos/doPageListJson',
			// 分页条
			pagination:true,
			pageSize:20,
			pageList:[20,50,100,200,500,1000],
			//填满区域
			fit:true,
			//奇偶变色
			striped:true,
			// 行号
			rownumbers:true,
			// 工具栏 【表格上方功能按钮】
			toolbar:'#toolbar' 
		});
	}
	
	function flush(num){
		clearInterval(clearTask);
		clearTask = setInterval(function(){
			$("#mainTable").datagrid('loaded');
			$("#mainTable").datagrid('reload',null);
			$("#mainTable").datagrid('loaded');
		}, num*1000);
	}
</script>
</head>
<body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
	
	<!-- 数据列表 -->
	<table id="mainTable" ></table>

	<div id="toolbar">
		<a href="#" class="easyui-menubutton" data-options="menu:'#refresh',iconCls:'icon-reload'">实时刷新</a>
	</div>
	<div id="refresh"  style="background:#f0f0f0;text-align:left">
		<div onclick="flush(2);">2秒</div>
		<div onclick="flush(5);">5秒</div>
		<div onclick="flush(10);">10秒</div>
		<div onclick="flush(20);">20秒</div>
		<div onclick="flush(30);">30秒</div>
		<div onclick="flush(60);">1分钟</div>
	</div>
</body>
</html>