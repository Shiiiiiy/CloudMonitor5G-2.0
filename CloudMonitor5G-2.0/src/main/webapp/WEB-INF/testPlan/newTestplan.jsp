<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>新增测试计划</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/modelLock.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/command.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js"></script>
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
   		.listTitle {
		    font-family: "Microsoft Sans Serif";
		    font-size: 12px;
		    font-weight: bold;
		  	/*border-left:4px solid #54b4f8;*/
		    margin:4px 0px;
		}
		.testPlan_tabs{
			background-color: #f6f9ff;
		}
		.modulebutton{
			color: black;
			border-color: #d9d8d7;
			background: #d3e9ff;
		}
		.modulebuttonhover,.modulebutton:hover{
			color: #FFF;
			border-color: #71b0dc;
			background: #62baf9;
		}
		.inputDiv {
			zoom: 1;
			font-size: 12px;
			margin: 5px;
			width: 255px;
			text-align: right;
			display: none;
			padding-left: 0;
			padding-right: 0;
		}
	</style>
<script type="text/javascript">
	/* 验证输入是否符合IP */
	function isIP(strIP) {
	    if (!strIP){
	        return false;
	    }
	    var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g
	    if(re.test(strIP))
	    {
	        if( RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256){
	            return true;
	        }
	    }
	    return false;
	}
	/* 复写easyui的验证框,增加IP验证 */
	$.extend($.fn.validatebox.defaults.rules, {
		/*必须和某个字段相等*/
		ipTextValue : {
			validator : function(value, param) {
				return isIP(value);
			},
			message : '通讯服务器IP地址不合法'
		}
	});
	/* 日志分隔类型 */
	function typeOnSelect(typeValue){
		if(0==typeValue.value){
			$("#testTime").numberbox({required:true});
			$("#pfileSize").numberbox({required:false});
		}else if(1==typeValue.value){
			$("#pfileSize").numberbox({required:true});
			$("#testTime").numberbox({required:false});
		}
	}
	/* 保存新增或者修改的测试计划 */
	function saveOrUpdateTestPlan() {
		console.log('外层保存-----');
		$("#testPlanForm").form('submit',{
			onSubmit : function(param) {
				return $(this).form('validate');
			},
			success : function(result) {
				var resultData =$.parseJSON(result);
				if (resultData.errorMsg) {
					$.messager.alert("失败", resultData.errorMsg,"error");
					return;
				} else {
					parent.centerLayoutGoToPage('【${name}】测试计划列表','${pageContext.request.contextPath}/testPlan/testPlanListUI.action?terminalId=${id}');
				}
			}
		});
	}
	/* 返回测试计划列表 */
	function goToTestPlanList(){
		parent.centerLayoutGoToPage('【${name}】测试计划列表','${pageContext.request.contextPath}/testPlan/testPlanListUI.action?terminalId=${id}');
	}
	//以上为测试计划操作
	//以下为测试任务操作
	/* 测试任务table列 */
	var testSchemeListTableInfoColumns=[[
		{field:'name',width:60,align:'center',title:'名称',
			formatter:function(value,row,index){
				<c:if test="${!showTestPlan}">
					return value;
				</c:if>
				<c:if test="${showTestPlan}">
					return '<a title="查看测试方案" href="#" onclick=showTestSchemeContent("'+value+'"); >'+value+'</a>';
				</c:if>
			}
		}, 				
		{field:'repeat',width:60,align:'center',title:'循环次数',
			formatter:function(value,row,index){
				if(row.commandList){
					return row.commandList.repeat;
				}
			}
		},
		{field:'runOrder',width:60,align:'center',title:'执行序号'}
	]];
	/* 初始化测试任务列表 */
	function initTestSchemeTable(){
		$("#testSchemeListTable").datagrid({
			// 表头
			columns:testSchemeListTableInfoColumns,
			//idField:'channelsNo',
			url:'${pageContext.request.contextPath}/testSuit/doPageListJson.action',
			title:'',
			border:false,
			fitColumns:true,
			loadFilter : function(data){
				//过滤数据、
				var value={total:data.total,rows:[]};
				if($(".modulebuttonhover").length!=0){
					if($(".modulebuttonhover").linkbutton('options').text=="APP单站验证模块"){
						for (var i = 0; i < data.rows.length; i++) {  
								value.rows.push(data.rows[i]);
						}
					}else{
						var selectButtonOptionsText = $(".modulebuttonhover").linkbutton('options').text;
						var moduleNo = selectButtonOptionsText.substring(selectButtonOptionsText.lastIndexOf("通道")+2);
						for (var i = 0; i < data.rows.length; i++) {  
							if(data.rows[i].msNo==moduleNo){
								value.rows.push(data.rows[i]);
							}
						}
					}
				}
				return value;
			},
			//奇偶变色
			striped:true,
			scrollbarSize:0,
			toolbar:[
				<c:if test="${!showTestPlan}">
					{text:'添加',iconCls:'icon-add',handler:newTestScheme},'-',
					{text:'编辑',iconCls:'icon-edit',handler:getTestSchemeContent},'-',
					{text:'删除',iconCls:'icon-cancel',handler:deleteTestScheme}
				</c:if>
			]
		});
	}
	/* 时间条件 */
	function timeOnSelect(timeValue){
		var beginDate = $("#beginDate").datebox('getValue');
		var endDate = $("#endDate").datebox('getValue');
		var beginTime = $("#beginTime").timespinner('getValue');
		var endTime = $("#endTime").timespinner('getValue');
		if(0==timeValue.value){
			//不遵循
			$("#beginDate").datebox({required:false});
			$("#endDate").datebox({required:false});
			$("#beginTime").timespinner({required:false});
			$("#endTime").timespinner({required:false});
		}else if(1==timeValue.value){
			//遵循
			$("#beginDate").datebox({required:true});
			$("#endDate").datebox({required:true});
			$("#beginTime").timespinner({required:true});
			$("#endTime").timespinner({required:true});
		}
		//设置开始结束时间
		$("#beginDate").datebox('setValue',beginDate);
		$("#endDate").datebox('setValue',endDate);
		$("#beginTime").timespinner('setValue',beginTime);
		$("#endTime").timespinner('setValue',endTime);
	}
	
	/* 区域限制条件 */
	function regionLimitOnSelect(regionValue){
		if(0==regionValue.value){
			//不遵循
			$("#leftTopLon").textbox({required:false});
			$("#leftTopLat").textbox({required:false});
			$("#rightBottomLon").textbox({required:false});
			$("#rightBottomLat").textbox({required:false});
		}else if(1==regionValue.value){
			//遵循
			$("#leftTopLon").textbox({required:true});
			$("#leftTopLat").textbox({required:true});
			$("#rightBottomLon").textbox({required:true});
			$("#rightBottomLat").textbox({required:true});
		}
	}
	/* 查看测试任务 */
	function showTestSchemeContent(testSchemeName){
		$.post("${pageContext.request.contextPath}/testPlan/getTestSuitContent.action",{testSuitName:testSchemeName},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					// 重设测试任务表单数据 
					$("#testSuitForm").form('reset');
					var selectButtonOptionsText = $(".modulebuttonhover").linkbutton('options').text;
					//设置网络制式锁定
					var moduleType = selectButtonOptionsText.substring(0,selectButtonOptionsText.indexOf("模块"));
					if(moduleType){
						$("#modelLockCombo").combobox('loadData',modelLocks[moduleType]);
					}else{
						$("#modelLockCombo").combobox('loadData',modelLocks['TD_LTE']);
					}
					//设置测试任务的名称
					$("#testSuitForm").form('load',{
						"testSuit.name":result.testSuit.name,
						"testSuit.commandList.repeat":result.testSuit.commandList.repeat,
						"beginDate":result.testSuit.dateBeans[0].startDate,
						"endDate":result.testSuit.dateBeans[0].endDate,
						"testSuit.desc":result.testSuit.desc,
						"testSuit.enable":result.testSuit.enable,
						"testSuit.modeLock":result.testSuit.modeLock,
						"beginTime":result.testSuit.times[0].beginTime,
						"endTime":result.testSuit.times[0].endTime,
						"testSuit.timeCondition":result.testSuit.timeCondition,
						"runOrder":result.testSuit.runOrder,
						"testSuit.msNo":result.testSuit.msNo,
						"testSuit.commandList.synchronize.type":result.testSuit.commandList.synchronize.type,
					});
					$("#ly").layout('collapse','north');
					$("#testSchemeList").panel('close');
					$("#addTestScheme").panel('resize');
					$("#addTestScheme").panel('open');
					//初始化测试命令列表
					initCommandListTable();
				}
			}
		,"json");
	}
	var nowDate=new Date();
	/* 新增测试任务 */
	function newTestScheme(){
		if($(".modulebuttonhover").length==0){
			$.messager.alert('提示','没有模块信息!','error');
			return;
		}
		$.post("${pageContext.request.contextPath}/testPlan/newTestScheme.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					// 重设测试任务表单数据 
					$("#testSuitForm").form('reset');
					//设置测试任务的名称
					$("#testSuitName").textbox('setValue',result.testSuit.name);
					$("#testSuitForm").form('load',result.testSuit);
					//设置循环次数
					$("#repeat").numberbox('setValue',result.testSuit.commandList.repeat);
					//设置模块号
					var selectButtonOptionsText = $(".modulebuttonhover").linkbutton('options').text;
					var moduleNo = "";
					if("APP单站验证模块" != selectButtonOptionsText){
						moduleNo = selectButtonOptionsText.substring(selectButtonOptionsText.lastIndexOf("通道")+2);
					}else{
						moduleNo = "0";
					}
					$("#msNo").val(moduleNo);
					//设置开始结束时间
					var dateString = nowDate.Format("yyyy-MM-dd");	
					$("#beginDate").datebox('setValue',dateString);
					$("#endDate").datebox('setValue',dateString);
					//设置网络制式锁定
					var moduleType = selectButtonOptionsText.substring(0,selectButtonOptionsText.indexOf("模块"));
					console.info(moduleType);
					console.info(modelLocks);
					if(moduleType){
						$("#modelLockCombo").combobox('loadData',modelLocks[moduleType]);
					}else{
						$("#modelLockCombo").combobox('loadData',modelLocks['TD_LTE']);
					}
					$("#ly").layout('collapse','north');
					$("#testSchemeList").panel('close');
					$("#addTestScheme").panel('resize');
					$("#addTestScheme").panel('open');
					//初始化测试命令列表
					initCommandListTable();
				}
			}
		,"json");
	}
	/* 编辑测试任务 */
	function getTestSchemeContent() {
		var testSchemeRows = $("#testSchemeListTable").datagrid('getSelections');//获取表格中用户选中 所有数据
		if(testSchemeRows.length==0){// 操作前至少选中一条
			$.messager.alert("系统提示", "请选择某些测试方案!");
			return ;
		}
		if(testSchemeRows.length>1){
			$.messager.alert("系统提示", "只能选择一个测试方案!");
			return ;
		}
		$.post("${pageContext.request.contextPath}/testPlan/getTestSuitContent.action",{testSuitName:testSchemeRows[0].name},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					// 重设测试任务表单数据 
					$("#testSuitForm").form('reset');
					var selectButtonOptionsText = $(".modulebuttonhover").linkbutton('options').text;
					//设置网络制式锁定
					var moduleType = selectButtonOptionsText.substring(0,selectButtonOptionsText.indexOf("模块"));
					if(moduleType){
						$("#modelLockCombo").combobox('loadData',modelLocks[moduleType]);
					}else{
						$("#modelLockCombo").combobox('loadData',modelLocks['TD_LTE']);
					}
					//设置测试任务的名称
					$("#testSuitForm").form('load',{
						"testSuit.name":result.testSuit.name,
						"testSuit.commandList.repeat":result.testSuit.commandList.repeat,
						"beginDate":result.testSuit.dateBeans[0].startDate,
						"endDate":result.testSuit.dateBeans[0].endDate,
						"testSuit.desc":result.testSuit.desc,
						"testSuit.enable":result.testSuit.enable,
						"testSuit.modeLock":result.testSuit.modeLock,
						"beginTime":result.testSuit.times[0].beginTime,
						"endTime":result.testSuit.times[0].endTime,
						"testSuit.timeCondition":result.testSuit.timeCondition,
						"runOrder":result.testSuit.runOrder,
						"testSuit.msNo":result.testSuit.msNo,
						"testSuit.commandList.synchronize.type":result.testSuit.commandList.synchronize.type,
					});
					$("#ly").layout('collapse','north');
					$("#testSchemeList").panel('close');
					$("#addTestScheme").panel('resize');
					$("#addTestScheme").panel('open');
					//初始化测试命令列表
					initCommandListTable();
				}
			}
		,"json");
	}
	/* 保存新增或者编辑的测试任务 */
	function saveOrUpdateTestScheme() {
		console.log('新增测试任务');
		$("#testSuitForm").form('submit',{
			onSubmit : function(param) {
				var isValid = $(this).form('validate');
				if(isValid){
					return true;
				}
				return isValid;
			},
			success : function(result) {
				var resultData =$.parseJSON(result);
				if (resultData.errorMsg) {
					$.messager.alert("失败", resultData.errorMsg,"error");
					return;
				} else {
					$("#addTestScheme").panel('close');
					$("#testSchemeList").panel('resize');
					$("#testSchemeList").panel('open');
					$("#testSchemeListTable").datagrid('reload');
					//$("#ly").layout('expand','north');
				}
			}
		});
	}
	/* 删除测试任务 */
	function deleteTestScheme() {
		var testSchemeRows = $("#testSchemeListTable").datagrid('getSelections');//获取表格中用户选中 所有数据
		if(testSchemeRows.length==0){// 操作前至少选中一条
			$.messager.alert("系统提示", "请选择某些测试方案!");
			return ;
		}
		$.messager.confirm("系统提示", "确定要删除选中的测试方案吗？", function(r) {
			if (r) {
				var namewArray = new Array();
				for(var i=0; i<testSchemeRows.length;i++){
					namewArray.push(testSchemeRows[i].name);
				}
				$.ajax({      
			 		url:"${pageContext.request.contextPath}/testPlan/deleteTestSuit.action",
			    	type:'post', 
			    	async : true, //默认为true 异步
			    	traditional:true,
			    	data:{testSchemeNames:namewArray},
			    	cache: false, 
					dataType:'json',
					success:function(result) {
						if (result.errorMsg) {
							$.messager.alert("系统提示", result.errorMsg,'error');
						} else {
							$.messager.alert("系统提示", "已成功删除这些记录!");
							$("#testSchemeListTable").datagrid('reload');
						}
					}
				}); 
			}
		});
	}
	/* 返回测试任务列表 */
	function goToTestSchemeList(){
		$("#addTestScheme").panel('close');
		$("#testSchemeList").panel('resize');
		$("#testSchemeList").panel('open');
		$("#testSchemeListTable").datagrid('reload');
		$("#ly").layout('expand','north');
	}
	//以上为测试任务操作
	//以下为测试命令操作
	/* 测试命令table列 */
	var commandListTableInfoColumns=[[
  		{field:'name',width:60,align:'center',title:'名称',
			formatter:function(value,row,index){
				<c:if test="${!showTestPlan}">
					return value;
				</c:if>
				<c:if test="${showTestPlan}">
					return '<a title="查看测试命令" href="#" onclick=showCommandContent("'+value+'"); >'+value+'</a>';
				</c:if>
			}
		}, 				
		{field:'repeat',width:60,align:'center',title:'循环次数'},
  		{field:'runOrder',width:60,align:'center',title:'执行序号'}
  	]];
	/* 初始化测试命令列表 */
	function initCommandListTable(){
		 $("#commandListTable").datagrid({ 
			// 表头
			columns:commandListTableInfoColumns,
			url:'${pageContext.request.contextPath}/testCommand/doPageListJson.action',
			title:'',
			border:false,
			fitColumns:true,
			//奇偶变色
			striped:true,
			scrollbarSize:0,
			toolbar:'#commandListTableToolBar'
		}); 
	}
	/* 查看测试命令 */
	function showCommandContent(commandContentName){
		$('#addCommandDialog').dialog('refresh', '${pageContext.request.contextPath}/testSuit/getCommandContent.action?commandName='+commandContentName);
		$('#addCommandDialog').dialog('open');
		$('#addCommandDialog').dialog('setTitle','查看----'+commandContentName);
	}
	/* 新增测试命令 */
	function newCommand() {
		var commandSelectValue = $("#commandListCombobox").combobox('getValue');
		var commandSelectText = $("#commandListCombobox").combobox('getText');
		$('#addCommandDialog').dialog('refresh', '${pageContext.request.contextPath}/testSuit/newCommand.action?commandId='+commandSelectValue);
		$('#addCommandDialog').dialog('open');
		$('#addCommandDialog').dialog('setTitle','添加----'+commandSelectText);
	}
	/* 编辑测试命令 */
	function getCommandContent() {
		var testCommandRows = $("#commandListTable").datagrid('getSelections');//获取表格中用户选中 所有数据
		if(testCommandRows.length==0){// 操作前至少选中一条
			$.messager.alert("系统提示", "请选择某些测试命令!");
			return ;
		}
		if(testCommandRows.length>1){
			$.messager.alert("系统提示", "只能选择一个测试命令!");
			return ;
		}
		$('#addCommandDialog').dialog('refresh', '${pageContext.request.contextPath}/testSuit/getCommandContent.action?commandName='+testCommandRows[0].name);
		$('#addCommandDialog').dialog('open');
		$('#addCommandDialog').dialog('setTitle','编辑----'+testCommandRows[0].name);
	}
	/* 保存新增或者编辑的测试命令 */
	function saveOrUpdateTestCommand() {
		$("#commandForm").form('submit',{
			onSubmit : function(param) {
				return $(this).form('validate');
			},
			success : function(result) {
				var resultData =$.parseJSON(result);
				if (resultData.errorMsg) {
					$.messager.alert("失败", resultData.errorMsg,"error");
					return;
				} else {
					$("#addCommandDialog").dialog('close');
					$("#commandListTable").datagrid('reload');
				}
			}
		});
	}
	/* 删除测试命令 */
	function deleteCommand() {
		var testCommandRows = $("#commandListTable").datagrid('getSelections');//获取表格中用户选中 所有数据
		if(testCommandRows.length==0){// 操作前至少选中一条
			$.messager.alert("系统提示", "请选择某些测试命令!");
			return ;
		}
		$.messager.confirm("系统提示", "确定要删除选中的测试命令吗？", function(r) {
			if (r) {
				var nameArray = new Array();
				for(var i=0; i<testCommandRows.length;i++){
					nameArray.push(testCommandRows[i].name);
				}
				$.ajax({      
			 		url:"${pageContext.request.contextPath}/testSuit/deleteCommand.action",
			    	type:'post', 
			    	async : true, //默认为true 异步
			    	traditional:true,
			    	data:{commandNames:nameArray},
			    	cache: false, 
					dataType:'json',
					success:function(result) {
						if (result.errorMsg) {
							$.messager.alert("系统提示", result.errorMsg,'error');
						} else {
							$.messager.alert("系统提示", "已成功删除这些记录!");
							$("#commandListTable").datagrid('reload');
						}
					}
				}); 
			}
		});
	}
	//以上为测命令操作
	//以下为全局操作
	/* 单击终端模块按钮,显示模块下的测试任务列表 */
	function showModuleTestScheme(clickThis,moduleNo){
		$(".modulebuttonhover").removeClass("modulebuttonhover");
		$(clickThis).addClass("modulebuttonhover");
		$("#addTestScheme").panel('close');
		$("#testSchemeList").panel('resize');
		$("#testSchemeList").panel('open');
		$("#testSchemeListTable").datagrid('reload');
	}
	
	//进入测试区域输入页面
	function newGpsRegionLimit(){
		$('#mapWindowId').window('open');
		mapIframe.window.rectAngleLeftLon = $("#leftTopLon").textbox("getValue");
		mapIframe.window.rectAngleLeftLat = $("#leftTopLat").textbox("getValue");
		mapIframe.window.rectAngleRightLon = $("#rightBottomLon").textbox("getValue");
		mapIframe.window.rectAngleRightLat = $("#rightBottomLat").textbox("getValue");
		setTimeout(function(){
			mapIframe.window.showRectAngle();
		},1000);
	}
	
	var leftTopLon,leftTopLat,rightBottomLon,rightBottomLat;
	function saveGeometry(){
		if(mapIframe.window.graphicLayerRectangle == null){
			$.messager.alert("系统提示", "请划分测试区域");
			return;
		}
		leftTopLon = parseFloat(mapIframe.window.rectAngleLeftLon);
		leftTopLat = parseFloat(mapIframe.window.rectAngleLeftLat);
		rightBottomLon = parseFloat(mapIframe.window.rectAngleRightLon);
		rightBottomLat = parseFloat(mapIframe.window.rectAngleRightLat);
		$("#leftTopLon").textbox("setValue",leftTopLon.toFixed(6));
		$("#leftTopLat").textbox("setValue",leftTopLat.toFixed(6));
		$("#rightBottomLon").textbox("setValue",rightBottomLon.toFixed(6));
		$("#rightBottomLat").textbox("setValue",rightBottomLat.toFixed(6));
		$('#mapWindowId').window('close');
	}
	
	function closeGeometry(){
		$('#mapWindowId').window('close');
	}
	
	$(function(){
		initTestSchemeTable();
		$('#mapWindowId').window('close');
		//初始化测试命令下拉框
		<c:if test="${!showTestPlan}">
			var commandListComboboxArray = [];
			var cds = commandContext.commands;
			for ( var i = 0; i < cds.length; i++) {
				0==i?commandListComboboxArray.push({value:cds[i].id,text:cds[i].name,selected:true}):commandListComboboxArray.push({value:cds[i].id,text:cds[i].name});
			}
			$("#commandListCombobox").combobox('loadData',commandListComboboxArray);
		</c:if>
		//设置开始结束时间
		var dateString = nowDate.Format("yyyy-MM-dd");	
		$("#endDate").datebox('setValue',dateString);
		$('#beginDate').datebox('calendar').calendar({
			validator: function(date){
				var endDate = $.fn.datebox.defaults.parser($("#endDate").datebox('getValue'));
				return date<=endDate;
			}
		});
		$("#beginDate").datebox('setValue',dateString);
		$('#endDate').datebox('calendar').calendar({
			validator: function(date){
				var startDate = $.fn.datebox.defaults.parser($("#beginDate").datebox('getValue'));
				return date>=startDate;
			}
		});
		$("#endDate").datebox('setValue',dateString);
		
		var t2 = window.setInterval(function(){
       		if(mapIframe.window.map){
       			mapIframe.window.hideDivByDivId('EventControldis');
	        	window.clearInterval(t2);
       		}
       	},1000);
       	if(0==$("#regionLimit").combobox("getValue")){
			//不遵循
			$("#leftTopLon").textbox({required:false});
			$("#leftTopLat").textbox({required:false});
			$("#rightBottomLon").textbox({required:false});
			$("#rightBottomLat").textbox({required:false});
		}else if(1==$("#regionLimit").combobox("getValue")){
			//遵循
			$("#leftTopLon").textbox({required:true});
			$("#leftTopLat").textbox({required:true});
			$("#rightBottomLon").textbox({required:true});
			$("#rightBottomLat").textbox({required:true});
		}else{
			//不遵循
			$("#regionLimit").combobox("setValue","0");
			$("#leftTopLon").textbox({required:false});
			$("#leftTopLat").textbox({required:false});
			$("#rightBottomLon").textbox({required:false});
			$("#rightBottomLat").textbox({required:false});
		}
	});
	//模块和"模块-模块sim卡号"对
	var modulePhoneNum = [];
	<c:forEach items="${testModuls}" var="testModul" varStatus="status">
		modulePhoneNum.push({value:"${testModul.channelsNo}",text:"${testModul.channelsNo}-${testModul.simCard}"});
	</c:forEach>
</script>
</head>
  
<body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	<div id="ly" class="easyui-layout"  style="width:100%;height: 100%;">
		<div data-options="region:'north',border:false,title:'公共配置',onCollapse:function(){
	   		//收束的时候显示title
	   		var title = $('#ly').layout('panel','north').panel('options').title;
	   		$('.layout-expand-north .panel-title').html(title);
	   	}" style="height:400px;overflow-y:auto;overflow-x:hidden;" >
			<div style="padding:0px 10px;">
				<form id="testPlanForm" style="margin:0px;" method="post" action="${pageContext.request.contextPath}/testPlan/saveOrUpdate.action" >
					<div class="listTitle">测试计划内容</div>
					<div class="testPlan_tabs" >
						<div class="inputDivShow">
							<font color="red">*</font>
							测试计划名称
							<input class="easyui-textbox" data-options="required:true,readonly:true" name="testPlan.name" value="${testPlan.name}"  id="name">
						</div>
						<div class="inputDivShow">
							测试计划描述
							<input class="easyui-textbox" name="testPlan.description" value="${testPlan.description}" data-options="validType:'length[1,24]'" >
						</div>
						<div class="inputDivShow">
							<font color="red">*</font>
							下发时间
							<input class="easyui-datetimebox" <c:if test="${null==testPlan.sendDate}">value="${testPlan.planSendDate}"</c:if><c:if test="${null!=testPlan.sendDate}">value="${testPlan.sendDate}"</c:if> name="testPlan.planSendDate" data-options="required:true,editable:false" >
						</div>
						<div class="inputDivShow">
							<font color="red">*</font>
							失效时间
							<input class="easyui-datetimebox" <c:if test="${null!=testPlan.loseDate}">value="${testPlan.loseDate}"</c:if> name="testPlan.loseDate" data-options="required:true,editable:false" >
						</div>
				        <div class="inputDivShow">
				                                 测试目标
							<select class="easyui-combobox" data-options="editable:false,value:'${testPlan.autoTestUnit.generalItem.testTarget}'" name="testPlan.autoTestUnit.generalItem.testTarget">
								<option value="DT">DT</option>
								<option value="FP">FP</option>
							</select>
						</div>
						<br>
						<div class="inputDivShow" style="overflow: auto;width:350px">
							<font color="red">*</font> 测试级别
			                <input type="radio" style="width:10px" name="testPlan.level" <c:if test="${testPlan.level == 'organizationCheck'}">checked="checked"</c:if> value="organizationCheck" >组织巡检
			                <input type="radio" style="width:10px" name="testPlan.level" <c:if test="${testPlan.level == 'dailyOptimiz'}">checked="checked"</c:if> value="dailyOptimiz" >日常优化
			                <input type="radio" style="width:10px" name="testPlan.level" <c:if test="${testPlan.level == 'deviceDebug'}">checked="checked"</c:if> value="deviceDebug" >设备调试(单站验证)
						</div>
					</div>
					<div class="listTitle">网络配置</div>
					<div class="testPlan_tabs">
						<div class="inputDivShow">
							<font color="red">*</font>
							通讯服务器IP
							<input class="easyui-textbox" data-options="required:true,validType:'ipTextValue',invalidMessage:'通讯服务器IP地址不合法'" name="testPlan.autoTestUnit.netWork.portalIP" value="${testPlan.autoTestUnit.netWork.portalIP}" id="portalIP">
						</div>
						<div class="inputDivShow">
							<font color="red">*</font>
							通讯服务器端口
							<input class="easyui-numberbox" name="testPlan.autoTestUnit.netWork.portalPort" data-options="required:true,max:65536,min:0" value="${testPlan.autoTestUnit.netWork.portalPort}" id="portalPort">
						</div>
						<div class="inputDivShow">
							拨号方式
							<select class="easyui-combobox" data-options="editable:false,value:'${testPlan.autoTestUnit.netWork.useLAN}'" name="testPlan.autoTestUnit.netWork.useLAN">
								<option value="0">不使用LAN回传</option>
								<option value="1">使用LAN回传</option>
								<option value="2">PPP拨号</option>
								<option value="3">虚拟网卡拨号</option>
							</select>
						</div>
						<div class="inputDivShow">
							<font color="red">*</font>
							回传模块号
							<input class="easyui-numberbox" data-options="required:true,max:11,min:0" name="testPlan.autoTestUnit.netWork.sendData" value="${testPlan.autoTestUnit.netWork.sendData}" id="sendData">
						</div>
						<div class="inputDivShow">
							<font color="red">*</font>
							回传拨叫号码
							<input  class="easyui-textbox" data-options="required:true" name="testPlan.autoTestUnit.netWork.dialNumber" value="${testPlan.autoTestUnit.netWork.dialNumber}" id="dialNumber">
						</div>
						<div class="inputDivShow">
							接入点名称
							<select class="easyui-combobox" data-options="editable:false,value:'${testPlan.autoTestUnit.netWork.apn}'" name="testPlan.autoTestUnit.netWork.apn">
								<option value="CMNET">CMNET</option>
								<option value="CMWAP">CMWAP</option>
								<option value="CTNET">CTNET</option>
								<option value="UNINET">UNINET</option>
							</select>
						</div>
						<div  class="inputDivShow">
							拨号网络用户
							<input class="easyui-textbox" name="testPlan.autoTestUnit.netWork.dialUpUser" value="${testPlan.autoTestUnit.netWork.dialUpUser}">
						</div>
						<div class="inputDivShow">
							拨号网络密码
							<input class="easyui-textbox" name="testPlan.autoTestUnit.netWork.dialUpPassword" value="${testPlan.autoTestUnit.netWork.dialUpPassword}">
						</div>
						<!-- <div class="inputDivShow">
							登录用户名
							<input  class="easyui-textbox" name="testPlan.autoTestUnit.netWork.user" value="${testPlan.autoTestUnit.netWork.user}">
						</div>
						<div class="inputDivShow">
							登陆密码
							<input class="easyui-textbox" name="testPlan.autoTestUnit.netWork.password" value="${testPlan.autoTestUnit.netWork.password}">
						</div> -->
					</div>
					<div class="listTitle">Log文件处理</div>
					<div class="testPlan_tabs">
						<div class="inputDivShow">
							是否切割
							<select class="easyui-combobox" data-options="editable:false,value:'${testPlan.autoTestUnit.logProcess.switchLog.enable}'" name="testPlan.autoTestUnit.logProcess.switchLog.enable">
								<option value="1">需要切割</option>
								<option value="0">不需要切割</option>
							</select>
						</div>
						<div class="inputDivShow">
							切割类型
							<select class="easyui-combobox" data-options="editable:false,onSelect:typeOnSelect,value:'${testPlan.autoTestUnit.logProcess.switchLog.type}'" name="testPlan.autoTestUnit.logProcess.switchLog.type" id="type">
								<option value="0">按测试时长切割</option>
								<option value="1">按文件大小切割</option>
							</select>
						</div>
						<div class="inputDivShow">
							文件大小(KB)
							<input class="easyui-numberbox" data-options="max:65535,min:0" name="testPlan.autoTestUnit.logProcess.switchLog.pfileSize" value="${testPlan.autoTestUnit.logProcess.switchLog.pfileSize}"  id="pfileSize">
						</div>
						<div class="inputDivShow">
							测试时间(分钟)
							<input class="easyui-numberbox" data-options="max:65535,min:0,required:true" name="testPlan.autoTestUnit.logProcess.switchLog.testTime" value="${testPlan.autoTestUnit.logProcess.switchLog.testTime}" id="testTime">
						</div>
						
						<div class="inputDivShow">
							Log强行切割标记
							<select class="easyui-combobox" data-options="editable:false,value:'${testPlan.autoTestUnit.logProcess.switchLog.condition}'" name="testPlan.autoTestUnit.logProcess.switchLog.condition">
								<option value="1">完成切割</option>
								<option value="0">强行切割</option>
							</select>
						</div>
					</div>
					<span class="listTitle">区域限制</span>
					<div  class="testPlan_tabs">
						<div class="inputDivShow">
							区域限制条件
							<select class="easyui-combobox"  id="regionLimit" 
								data-options="editable:false,onSelect:regionLimitOnSelect,<c:if test="${testPlan.autoTestUnit.generalItem.gpsCondition!=null}">value:'${testPlan.autoTestUnit.generalItem.gpsCondition}'</c:if>" name="testPlan.autoTestUnit.generalItem.gpsCondition" style="width:140px">
								<option value="1" >遵循</option>
								<option value="0" selected="selected">不遵循</option>
							</select>
						</div>
						<div class="inputDivShow">
							测试区域
							<a class="easyui-linkbutton" id="mapInputRectAngle" onclick="newGpsRegionLimit()" style="width:140px;color: #000000;border-color: #71b0dc;background: #FFFFFF;" data-options="iconCls:'icon-networkStructure'" >地图输入</a>
						</div>
						<div class="inputDivShow">
							左上角经度
							<input class="easyui-textbox" name="testPlan.autoTestUnit.generalItem.leftTopLon" data-options="required:false,editable:false" maxlength="24" size="20" value="${testPlan.autoTestUnit.generalItem.leftTopLon}" id="leftTopLon">
						</div>
						<div class="inputDivShow">
							左上角纬度
							<input class="easyui-textbox" name="testPlan.autoTestUnit.generalItem.leftTopLat" data-options="required:false,editable:false" maxlength="24" size="20" value="${testPlan.autoTestUnit.generalItem.leftTopLat}" id="leftTopLat">
						</div>
						<div class="inputDivShow">
							右下角经度
							<input class="easyui-textbox" name="testPlan.autoTestUnit.generalItem.rightBottomLon" data-options="required:false,editable:false" maxlength="24" size="20" value="${testPlan.autoTestUnit.generalItem.rightBottomLon}" id="rightBottomLon">
						</div>
						<div class="inputDivShow">
							右下角纬度
							<input class="easyui-textbox" name="testPlan.autoTestUnit.generalItem.rightBottomLat" data-options="required:false,editable:false" maxlength="24" size="20" value="${testPlan.autoTestUnit.generalItem.rightBottomLat}" id="rightBottomLat">
						</div>
					</div> 
				</form>
			</div>
			<div style="width:100%;border-bottom:1px solid #95b8e7;bottom:0;position:absolute;">
		   	</div>
		</div>
		<div  data-options="region:'center',border:false,title:'测试方案'" style="overflow-y:auto;overflow-x:hidden;" >
			<div  style="padding:4px">
				<c:choose> 
					<c:when test="${testTarget!=3}">
						<c:forEach items="${testModuls}" var="testModul" varStatus="status">
							<c:choose>
								<c:when test="${status.index==0}"><a class="easyui-linkbutton modulebutton modulebuttonhover" onclick="showModuleTestScheme(this,${testModul.channelsNo});" style="width:150px;margin-bottom:4px;"  >${testModul.moduleType}模块-通道${testModul.channelsNo}</a></c:when>
								<c:otherwise><a class="easyui-linkbutton modulebutton" onclick="showModuleTestScheme(this,${testModul.channelsNo});" style="width:150px;margin-bottom:4px;"  >${testModul.moduleType}模块-通道${testModul.channelsNo}</a></c:otherwise>
							</c:choose>
				    	</c:forEach>
					</c:when>
					<c:otherwise>
						<a class="easyui-linkbutton modulebutton modulebuttonhover" onclick="showModuleTestScheme(this,${testModul.channelsNo});" style="width:150px;margin-bottom:4px;"  >APP单站验证模块</a>
					</c:otherwise>
				</c:choose>
			    <div id="testSchemeList" class="easyui-panel" style="width:100%;" data-options="title:'',border:false">
			   		<table id="testSchemeListTable" width=100% ></table>
			    </div>
			    <div id="addTestScheme" class="easyui-panel" style="width:100%;" data-options="title:'',border:false,closed:true">
			   		<div style="width:100%;border-bottom:1px solid #95b8e7;"></div>
			   		<div class="listTitle" >添加测试方案</div>
			   		<div style="height:16px;background-color:#e8f1ff;padding:2px;border:1px solid #95b8e7;"><div class="panel-title">控制策略</div></div>
			   		<form id="testSuitForm" method="post" style="width: 100%;margin:0px;" action="${pageContext.request.contextPath}/testSuit/saveOrUpdate.action">
						<div class="inputDivShow">
							<font color="red">*</font>
							名称
							<input class="easyui-textbox" name="testSuit.name" style="width:140px" <c:if test="${testTarget==3}">readonly="readonly"</c:if> data-options="required:true,validType:'length[1,24]'" id="testSuitName" />
						</div>
						<div class="inputDivShow">
							<font color="red">*</font>
							循环次数
							<input class="easyui-numberbox" name="testSuit.commandList.repeat" style="width:140px" <c:if test="${testTarget==3}">readonly="readonly"</c:if> data-options="required:true,min:1,max:65536" id="repeat" />
						</div>
						<!-- <div class="inputDivShow">
							描述
							<input class="easyui-textbox" name="testSuit.desc" data-options="validType:'length[1,24]'"  />
						</div> -->
						<div class="inputDivShow">
							是否禁用
							<select class="easyui-combobox" data-options="editable:false" style="width:140px" <c:if test="${testTarget==3}">readonly="readonly"</c:if> name="testSuit.enable">
								<option selected="selected" value="1">启用</option>
								<option value="0">禁用</option>
							</select>
						</div>
						<div class="inputDivShow">
							网络测试锁定
							<select id="modelLockCombo" class="easyui-combobox" style="width:140px" <c:if test="${testTarget==3}">readonly="readonly"</c:if> data-options="editable:false,valueField:'value',textField:'text'" name="testSuit.modeLock">
								
							</select>
						</div>
						<div class="inputDivShow">
							时间条件
							<select class="easyui-combobox"  data-options="editable:false,onSelect:timeOnSelect" name="testSuit.timeCondition" style="width:140px">
								<option value="1" >遵循</option>
								<option value="0" selected="selected">不遵循</option>
							</select>
						</div>
						<div class="inputDivShow">
							执行序号
							<input class="easyui-numberbox" name="runOrder" value="" <c:if test="${testTarget==3}">readonly="readonly"</c:if> data-options="min:0,max:999" style="width:140px" />
						</div>
						<div class="inputDivShow">
							类型
							<select class="easyui-combobox" name="testSuit.commandList.synchronize.type">
								<option value="1">并行</option>
								<option value="0" selected="selected">串行</option> 
							</select>
						</div>
						<br>
						<div class="inputDivShow" style="width:300px">
							执行日期
							<input style="width:100px"  data-options="editable:false,required:true"  class="easyui-datebox" name="beginDate"  id="beginDate">-<input style="width:100px" data-options="editable:false,required:true" class="easyui-datebox" name="endDate" id="endDate">
						</div>
						<div class="inputDivShow" style="width:300px">
							执行时间
							<input style="width:100px"  class="easyui-timespinner" data-options="required:true" value="00:00" name="beginTime"  id="beginTime" />-<input style="width:100px"  class="easyui-timespinner" data-options="required:true" name="endTime" value="23:59" id="endTime"  />
						</div>
						<!-- 测试模块号 -->
						<input type="hidden" name="testSuit.msNo" value="" id="msNo">
						<!-- 类型 -->
						<!-- <input type="hidden" name="testSuit.commandList.synchronize.type" value="0" > --> 
					</form>
					<div style="height:16px;background-color:#e8f1ff;padding:2px;border:1px solid #95b8e7;"><div class="panel-title">测试命令列表</div></div>
			    	<table id="commandListTable" width="100%" ></table>
			    	<div align="right" >
			    		<c:if test="${!showTestPlan}">
							<a class="easyui-linkbutton" iconCls="icon-ok" style="width:100px;"  onclick="saveOrUpdateTestScheme()" >保存</a>
						</c:if>
						<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;"  onclick="goToTestSchemeList()" >返回</a>
					</div>
			    </div>
			</div>
	   	</div>
	   	<div data-options="region:'south',border:false" style="height:40px;">
		   	<table width="100%" style="border-top:1px solid #95b8e7;">
		   		<tr height="35px">
		    		<td width="50%;" align="right">
		    			<c:if test="${!showTestPlan}">
		    				<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="saveOrUpdateTestPlan();"  >保存</a>
		    			</c:if>
		    		</td>
		    		<td width="50%;">
		    			<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;" onclick="goToTestPlanList();" >返回</a>
		    		</td>
		   		</tr>
		   	</table>
	  	</div>
	</div>
	
	<div id="commandListTableToolBar" align="right">
		<c:if test="${!showTestPlan}">
			测试业务:<select id="commandListCombobox" data-options="editable:false,valueField:'value',textField:'text'" style="width:300px;" class="easyui-combobox"></select>
			<a class="easyui-linkbutton" onclick="newCommand();" data-options="plain:true,iconCls:'icon-add'">添加</a>
			<a class="easyui-linkbutton" onclick="getCommandContent();" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
			<a class="easyui-linkbutton" onclick="deleteCommand();" data-options="plain:true,iconCls:'icon-cancel'">删除</a>
		</c:if>
	</div>
	<div id="addCommandDialog" class="easyui-dialog" style="width:600px;height:370px;padding:5px 0px;overflow-y:auto;" data-options="closed:true,modal:true,cache:true,buttons:'#addCommandDialog-buttons'">
    		
   	</div>
	<div id="addCommandDialog-buttons">
		<c:if test="${!showTestPlan}">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveOrUpdateTestCommand();" >确定</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#commandForm').form('reset')">重置</a>
		</c:if>
	</div>
	
	<div id="mapWindowId" class="easyui-window" title="地图输入" style="width:800px;height:450px"
        data-options="iconCls:'icon-save',modal:true,resizable:false,maximizable:false">
		<div style="width:100%;height:92%;border:2px;">
		  	<iframe id="mapIframe" name="mapIframe" src="${pageContext.request.contextPath}/gis/default3.html?toolbarType=103"
	        scrolling="auto" frameborder="0"
	        style="width:100%;height:100%;border:2px;margin: 5px 5px 5px 5px;"></iframe>
	    </div>
	   	<div style="height:7%;">
		   	<table width="100%" style="border-top:1px solid #95b8e7;">
		   		<tr height="35px">
		    		<td width="50%;" align="center">
		    			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-ok',plain:true" onclick="saveGeometry();">保存</a>
						<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-undo',plain:true" onclick="closeGeometry();">返回</a>
		    		</td>
		   		</tr>
		   	</table>
	  	</div>
	  	
	</div>
  </body>
</html>