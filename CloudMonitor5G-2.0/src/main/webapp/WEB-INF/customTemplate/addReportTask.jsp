<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>新增任务</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/modelLock.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/command.js" ></script>
	<style type="text/css">
		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: left;
		    width: 370px;
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
		.inputDivShow .datagrid-header {
			display:none;
		}
	</style>
<script type="text/javascript">
$(function(){
	var cityIds='${statisticeTask.cityIds}';
	if(null!=cityIds){
		$.post("${pageContext.request.contextPath}/report5g/getCityInfo.action?cityIds="+cityIds+"",
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						if(result.cityInfo){
							$("#atuTab").datagrid('loadData',result.cityInfo);
						}
					}
				}
		,"json");
	}
	var boxIds='${statisticeTask.boxIds}';
	if(boxIds){
		var boxIdsArray = boxIds.split(",");
		var list2=[];
		for (var t = 0; t < boxIdsArray.length; t++) {
			var map = {};
			map["boxId"] = boxIdsArray[t];
			list2.push(map);
		}
		$("#boxTab").datagrid('loadData',list2);
	}
	var logIds='${statisticeTask.logIds}';
	if(null!=logIds){
		$.post("${pageContext.request.contextPath}/report5g/getTestLogItem.action?logIds="+logIds+"",
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						if(result.testLog){
							$("#logTab").datagrid('loadData',result.testLog);
						}
					}
				}
			,"json");
	}
 	var templateIds='${statisticeTask.templateIds}';
	if(null!=templateIds){
		$.post("${pageContext.request.contextPath}/report5g/getReportTemplate.action?templateIds="+templateIds+"",
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						if(result.reportTemplate){
							$("#templateTab").datagrid('loadData',result.reportTemplate);
						}
					}
				}
			,"json");
	}
	
	var timee='${statisticeTask.beginDate}';
	if(timee==null||timee.length==0){
		//初始化开始时间和结束时间
		var nowDate=new Date();
		var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");	
		//var dateString = nowDate.Format("yyyy-MM-dd");	
		 $("#endDate").datetimebox('setValue',dateString);
		$('#beginDate').datebox('calendar').calendar({
			validator: function(date){
				var endDate = $.fn.datebox.defaults.parser($("#endDate").datebox('getValue'));
				return date<=endDate;
			}
		});
		nowDate.setDate(nowDate.getDate()-7);
		dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
		$("#beginDate").datetimebox('setValue',dateString);
		$('#endDate').datebox('calendar').calendar({
			validator: function(date){
				var startDate = $.fn.datebox.defaults.parser($("#beginDate").datebox('getValue'));
				return date>=startDate;
			}
		});
		//$("#endDate").datetimebox('setValue',dateString); 
		
		/* $("#endDate").datetimebox('setValue',dateString);
		nowDate.setDate(nowDate.getDate()-7);
		dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
		$("#beginDate").datetimebox('setValue',dateString); */
	}
});
	/* 保存新增或者修改的统计任务 */
	var dpage = '${dPage}';
	function saveReport() {
		$('#saveButton').linkbutton('disable');
		$("#reportForm").form('submit',{
			onSubmit : function(param) {
				/* if($('#atuTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择区域!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}else if($('#boxTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择测试终端!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}else if($('#logTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择Log文件!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}else if($('#templateTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择报表模板!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				} */
				//var fileName = $("#fileName").textbox('getValue');
				var prov = $("#prov").combobox('getValue');
				var city = $("#city").combobox('getValue');
				
				/* if(fileName.trim() == ""){
					$.messager.alert('系统提示','请输入文件名!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}else  */if(prov.trim() == ""){
					$.messager.alert('系统提示','请选择省!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}else if(city.trim() == ""){
					$.messager.alert('系统提示','请选择市!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}else if($('#logTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择Log文件!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}else if($('#templateTab').datagrid('getRows').length==0){
					$.messager.alert('系统提示','请选择报表模板!','warning');
					$('#saveButton').linkbutton('enable');
					return false;
				}
				
				
				return $(this).form('validate');
			},
			success : function(result) {
				var resultData = eval('(' + result + ')');
				if (resultData.errorMsg) {
					$.messager.alert("失败", resultData.errorMsg,"error");
				} else {
					$.messager.alert("系统提示","下发任务成功","info");
					goToTestPlanList();
				}
				$('#saveButton').linkbutton('enable');
			}
		});
	}
	/* 返回 统计任务列表 */
	function goToTestPlanList(){
		goToPage('${pageContext.request.contextPath}/customeLogReport/listUI.action');
	}
	//打开添加域窗口
	function addATU(){
		$('#winATU').window('open');
		//$('#areaTree').tree('expandAll');
		//$('#areaTree').combotree('tree').tree("expandAll")
		 //$("#areaTree").tree('showPanel');
	}
	//打开添加终端窗口且加载信息
	function addBox(){
		var city=$("#atuTab").datagrid('getData');
		var rows1 = $("#atuTab").datagrid('getRows');
		if(rows1.length==0||city.rows[0].cityId===""){
			$.messager.alert('系统提示','请选择区域!','warning');
			return false;
		}
		$('#winBox').window('open');
		var checkNodes = $("#areaTree").tree('getChecked');
		var cityIds = [];
		for (var int = 0; int < checkNodes.length; int++) {
			if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
				cityIds.push(checkNodes[int].attributes.refId);
			}
		}
		var cityIdsString = cityIds.join(",");
		$.post("${pageContext.request.contextPath}/report5g/terminalTree.action?cityIds="+cityIdsString+"",
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#boxTab2").datagrid('loadData',result.boxInfo);
					}
				}
			,"json");
	}
	//打开添加日志
	function addLog(){
		
	    obj = document.getElementsByName("testRanks");
	    if(obj.length==0||obj[0].value===""){
	    	$.messager.alert('系统提示','请选择测试级别!','warning');
			return false;
	    }
	    var check_val = [];
	    for(k in obj){
	        if(obj[k].checked)
	            check_val.push(obj[k].value);
	    }
	    var testRankString=check_val.join(",");
		 if(testRankString===""){
	    	$.messager.alert('系统提示','请选择测试级别!','warning');
			return false;
	    }
	    
	    var prov = $("#prov").combobox('getValue');
	    if(prov.trim() == ""){
			$.messager.alert('系统提示','请选择省!','warning');
			return false;
		}
	    
		var city = $("#city").combobox('getValue');
	    if(city.trim() == ""){
			$.messager.alert('系统提示','请选择市!','warning');
			return false;
		}
	    
		//var city=$("#atuTab").datagrid('getData');
		/* var rows1 = $("#atuTab").datagrid('getRows');
		if(rows1.length==0||city.rows[0].cityId===""){
			$.messager.alert('系统提示','请选择区域!','warning');
			return false;
		} */
		
		
		/* var cityIds=[];
		for (var i = 0; i < rows1.length; i++) {
		 cityIds.push(city.rows[i].cityId);
		}
		var cityIdsString = cityIds.join(","); */
		
		/* var box=$("#boxTab").datagrid('getData');
		var rows2 = $("#boxTab").datagrid('getRows'); 
		if(rows2.length==0||box.rows[0].boxId===""){
			$.messager.alert('系统提示','请选择测试终端!','warning');
			return false;
		}
		var boxIds=[];

		for (var i = 0; i < rows2.length; i++) {
			boxIds.push(box.rows[i].boxId);
		}
		var boxIdsString = boxIds.join(","); */
		var filename = $("#fileName").textbox('getValue');
		
		var beginDate = $('#beginDate').datebox('getValue');
		if(beginDate===""){
			$.messager.alert('系统提示','请选择开始时间!','warning');
			return false;
		}
		var endDate = $('#endDate').datebox('getValue');
		if(endDate===""){
			$.messager.alert('系统提示','请选择结束时间!','warning');
			return false;
		}
		$('#winLog').window('open');
		
		$.post("${pageContext.request.contextPath}/report5g/testUnicomLogItem.action?prov="+prov+"&city="+city+"&beginDate="+beginDate+"&endDate="+endDate+"&filename="+filename,
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#logTab2").datagrid('loadData',result.testLog);
						$('#logTab2').datagrid('checkAll');	
					}
				}
			,"json");
	}
	
	//打开添加报告模板
	function addTemplate(){
		/* var city=$("#atuTab").datagrid('getData');
		var rows1 = $("#atuTab").datagrid('getRows');
		if(rows1.length==0||city.rows[0].cityId===""){
			$.messager.alert('系统提示','请选择区域!','warning');
			return false;
		} */
		/* var cityIds=[];
		for (var i = 0; i < rows1.length; i++) {
		 	cityIds.push(city.rows[i].cityId);
		}
		var cityIdsString = cityIds.join(","); */
		
		var type = $("#templateType").combobox('getValue');//0:通用，1:分析
		
		$('#winTemplate').window('open');
		console.info("这里报表必须包含 '指标报表_CQT模板' OR '指标报表_DT模板' ")
		$.post("${pageContext.request.contextPath}/report5g/reportTemplate.action?reportType=" + type,
				function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#templateTab2").datagrid('loadData',result.reportTemplate);
						$('#templateTab2').datagrid('checkAll');
					}
				}
			,"json");
	}
	
	/*
	添加选择项，方法共用
	*/
	function add(i){
		if(i==1){
			//var checkNodes = $("#areaTree").tree('getChecked');
			//var cityIds = [];
			//var names=[];
			/* for (var int = 0; int < checkNodes.length; int++) {
				if(checkNodes[int].attributes && checkNodes[int].attributes.type=='City'){
					cityIds.push(checkNodes[int].attributes.refId);
					names.push(checkNodes[int].text);
				}
			} */
			//回填域值
			//[{cityId:cityIds[0]},{},{}]
			var list=[];
			var list1=[];
			for (var t = 0; t < cityIds.length; t++) {
				var map = {};
				map["name"]=names[t];
				map["cityId"] = cityIds[t];
				list1.push(cityIds[t]);
				list.push(map);
			}
			var listString=list1.join(",");
			//$("#cityIds").value=listString; 
			$("#cityIds").val(listString);
			$("#atuTab").datagrid('loadData',list);
			$('#winATU').window('close');
		}else if(i==2){
			//回填终端boxID
			var values=$('#boxTab2').datagrid('getSelections');	
			if(0!=values.length){
				var list2=[];
				for (var c = 0; c < values.length; c++) {
					list2.push(values[c].boxId);
				}
				var list2String=list2.join(",");
				$("#boxIds").val(list2String);
				//$("#boxIds").value=list2String; 
			}
			$("#boxTab").datagrid('loadData',values);
			$('#winBox').window('close');
		}else if(i == 3){
			//回填LogID
			var valuess=$('#logTab2').datagrid('getSelections');
			if(0!=valuess.length){
				var list3=[];
				for (var l = 0; l < valuess.length; l++) {
					list3.push(valuess[l].id);
				}
				var list3String=list3.join(",");
				//$("#logIds").textbox('setValue',list3String);
				//$("#logIds")[0].value=list3String; 
				$("#logIds").val(list3String);
			}
			$("#logTab").datagrid('loadData',valuess);
			$('#winLog').window('close');
		}else if(i == 4){
			//回填模板id
			var valuess=$('#templateTab2').datagrid('getSelections');
			if(0!=valuess.length){
				var list3=[];
				for (var l = 0; l < valuess.length; l++) {
					list3.push(valuess[l].id);
				}
				var list3String=list3.join(",");
				//$("#logIds").textbox('setValue',list3String);
				//$("#logIds")[0].value=list3String; 
				$("#templateIds").val(list3String);
			}
			$("#templateTab").datagrid('loadData',valuess);
			$('#winTemplate').window('close');
		}
	}
	/*删除选择项，方法共用*/
	function del(i){
		if(i==1){
			var rows=$('#atuTab').datagrid('getSelections');
			if(rows.length!=0){
				for (var u = 0; u< rows.length;u++) {
					var index=$('#atuTab').datagrid('getRowIndex',rows[u]) ;
					$('#atuTab').datagrid('deleteRow',index) ;
				}
		        var rows = $('#atuTab').datagrid('getRows');//获取当前页的数据行  
		        if(0!=rows.length){
		        var list1 = []; 
		        for (var i = 0; i < rows.length; i++) {  
		        	list1.push(rows[i]['cityId']) ; //获取指定列  
		        }  
		        var list1String=list1.join(",");
		        $("#cityIds").val(list1String);
		        }
			}
			
		}else if(i==2){
			var rows=$('#boxTab').datagrid('getSelections');
			if(rows.length!=0){
				for (var u = 0; u< rows.length;u++) {
					var index=$('#boxTab').datagrid('getRowIndex',rows[u]) ;
					$('#boxTab').datagrid('deleteRow',index) ;
				}
				 var rows = $('#boxTab').datagrid('getRows');//获取当前页的数据行 
				 if(0!=rows.length){
			        var list2 = [];  
			        for (var i = 0; i < rows.length; i++) {  
			        	list2.push(rows[i]['boxId']) ; //获取指定列  
			        }  
			        var list2String=list2.join(",");
			        $("#boxIds").val(list2String);
			}
			}
		}else if(i==3){
			var rows=$('#logTab').datagrid('getSelections');
			if(rows.length!=0){
				for (var u = 0; u< rows.length;u++) {
					var index=$('#logTab').datagrid('getRowIndex',rows[u]) ;
					$('#logTab').datagrid('deleteRow',index) ;
				}
				var rows = $('#logTab').datagrid('getRows');//获取当前页的数据行  
				 if(0!=rows.length){
			        var list3 = [];  
			        for (var i = 0; i < rows.length; i++) {  
			        	list3.push(rows[i]['id']) ; //获取指定列  
			        }  
			        var list3String=list3.join(",");
			        $("#logIds").val(list3String);
				}
			}
		}else if(i==4){
			var rows=$('#templateTab').datagrid('getSelections');
			if(rows.length!=0){
				for (var u = 0; u< rows.length;u++) {
					var index=$('#templateTab').datagrid('getRowIndex',rows[u]) ;
					$('#templateTab').datagrid('deleteRow',index) ;
				}
				var rows = $('#templateTab').datagrid('getRows');//获取当前页的数据行  
				if(0!=rows.length){
			        var list3 = [];  
			        for (var i = 0; i < rows.length; i++) {  
			        	list3.push(rows[i]['id']) ; //获取指定列  
			        }  
			        var list3String=list3.join(",");
			        $("#templateIds").val(list3String);
				}
			}
		}
	}
	/*展开节点*/
	function open(node,data){
		 $("#areaTree").tree('expandAll');//展开所有节点 
	}
	/*清空选择共用方法*/
	function clean(i){
		if(i==1){
			$("#areaTree").tree('reload');
		}else if(i==2){
			$('#boxTab2').datagrid('uncheckAll');
		}else if(i==3){
			$('#logTab2').datagrid('uncheckAll');
		}else if(i==4){
			$('#templateTab2').datagrid('uncheckAll');
		}
	}
	
	
	$(function(){
		$('#prov').combobox({
			url:'${pageContext.request.contextPath}/unicomLogItem/provInput',
			cache:false,
			paneHeight:'auto',
			valueField:'value',
			textField:'label',
			onSelect: function(rec){
				$('#city').combobox({
					url:'${pageContext.request.contextPath}/unicomLogItem/cityInput?prov='+rec.value,
					cache:false,
					paneHeight:'auto',
					valueField:'value',
					textField:'label'
				})
			}
		})
		
	
	})
	
	
	
</script>
</head>
  
<body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	 <div id="ly" class="easyui-layout"  style="width:100%;height: 100%;">
		<div data-options="region:'center',border:false,title:'新增任务，带*为必填项'"style="overflow-y:auto;overflow-x:hidden;" >
			<div style="padding:0px 10px;">
				<form id="reportForm" style="margin:0px;" method="post" action="${pageContext.request.contextPath}/report5g/addUnicomReportTask.action" >
					<!-- <div class="listTitle">统计任务内容</div> -->
					<div class="testPlan_tabs">
						<div class="inputDivShow">
							<font color="red">*</font>
							统计任务名称
							<input class="easyui-textbox" style="width:200px;" data-options="required:true,readonly:true" name="statisticeTaskRequest.name" <c:if test="${null==statisticeTask.name}">value="<域名>-<开始时间>-<结束时间>"</c:if>value="${statisticeTask.name}"  id="name">
						</div>
						<br>
						<div class="inputDivShow">
							<font color="red">*</font>
							开始时间
							<input id="beginDate" class="easyui-datetimebox" <c:if test="${null!=statisticeTask.beginDate}">value="${statisticeTask.beginDate}"</c:if> name="statisticeTaskRequest.beginDate" data-options="required:true,editable:false" >
						</div>
						<div class="inputDivShow">
							<font color="red">*</font>
							结束时间
							<input id="endDate"class="easyui-datetimebox" <c:if test="${null!=statisticeTask.endDate}">value="${statisticeTask.endDate}"</c:if> name="statisticeTaskRequest.endDate" data-options="required:true,editable:false" >
						</div>
						<div class="inputDivShow">
							<!-- <font color="red">*</font> -->
							文件名
							<input id="fileName" class="easyui-textbox" name="fileName" data-options="required:false,validType:'length[1,30]'"/>
						</div>
						<br>
						<div class="inputDivShow" style="overflow: auto;width:90%">
							<font color="red"></font> 测试级别
			                <input class="testRank" type="checkbox" name="testRanks"  value="1" >组织巡检&nbsp;&nbsp;
			                <input class="testRank" type="checkbox" name="testRanks" <c:if test="${statisticeTask.testRank == null}">checked="checked"</c:if> value="2" >日常优化&nbsp;&nbsp;
			                <input class="testRank" type="checkbox" name="testRanks"  value="3" >设备调试&nbsp;&nbsp;
						</div>
						<br>
						<input class="collectType" type="hidden" name="collectTypes" value="6" >
						<%-- <div class="inputDivShow" style="overflow: auto;width:90%">
							<font color="red"></font> 汇总方式
			                <input class="collectType" type="checkbox" name="collectTypes"  value="1" >全国&nbsp;&nbsp;
			                <input class="collectType" type="checkbox" name="collectTypes"  value="2" >省级&nbsp;&nbsp;
			                <input class="collectType" type="checkbox" name="collectTypes"  value="3" >地市&nbsp;&nbsp;
			                <input class="collectType" type="checkbox" name="collectTypes"  value="4" >BOXID&nbsp;&nbsp;
			                <input class="collectType" type="checkbox" name="collectTypes"  value="5" >测试计划&nbsp;&nbsp;
			                <input class="collectType" type="checkbox" name="collectTypes" <c:if test="${statisticeTask.collectType == null}">checked="checked"</c:if> value="6" >Log文件&nbsp;&nbsp;
						</div> --%>
					</div>
					<script type="text/javascript">
						var collectTypes='${statisticeTask.collectType}';
						if(collectTypes){
							var collectTypesArray = collectTypes.split(",");
							for ( var collectType in collectTypesArray) {
								$(".collectType[value='"+collectTypesArray[collectType]+"']").attr("checked","checked");
							}
						}
						
						var testRanks='${statisticeTask.testRank}';
						if(testRanks){
							var testRanksArray = testRanks.split(",");
							for ( var testRank in testRanksArray) {
								$(".testRank[value='"+testRanksArray[testRank]+"']").attr("checked","checked");
							}
						}
					</script>
					<br>
					<!-- <div class="listTitle">统计区域以及文件汇总</div> -->
					<div class="testPlan_tabs">
					
						<div class="inputDivShow">
							<div style="height:8px;">
								<font color="red">*</font>省
							</div>
							<br>
				    		<select id="prov" name="prov"  class="easyui-combobox" style="width:278px;" data-options="editable:true,multiple:false" >
				    		</select>
				    	</div>
				    	<div class="inputDivShow">
				    		<div style="height:8px;">	
				    			<font color="red">*</font>市
				    		</div>
				    		<br>
				    		<select id="city" name="city"  class="easyui-combobox" style="width:278px;" data-options="editable:true,multiple:false" >
				    			<option selected="selected" value="">全部</option>
				    		</select>
				    	</div>
					
						<!-- <div class="inputDivShow"><font color="red">*</font>区域
							<br>
							<table  id="atuTab" class="easyui-datagrid" style="width:280px;height:100px">
	    			    		<thead>
									<tr>
										<th data-options="field:'cityId',hidden:true"></th>
										<th data-options="field:'name',width:280"></th>
									</tr>
						    	</thead>
	    			    	</table>
	    			    	<a iconCls="icon-add" class="easyui-linkbutton" style="width:138px;margin:2px 2px 2px 0;" onclick="addATU();" >添加</a><a iconCls="icon-cancel" class="easyui-linkbutton" style="width:138px;margin:2px 0px 2px 2px;" onclick="del(1);" >删除</a>
	    				</div> -->
	    				
	    			    <!-- <div class="inputDivShow"><font color="red">*</font>测试终端
	    			    	<br>
	    			    	<table  id="boxTab"class="easyui-datagrid" style="width:280px;height:100px">
	    			    		<thead>
									<tr>
										<th data-options="field:'boxId',width:280"></th>
									</tr>
						    	</thead> 
	    			    	</table>
				    		<a iconCls="icon-add" class="easyui-linkbutton" style="width:138px;margin:2px 2px 2px 0;" onclick="addBox();" >添加</a><a iconCls="icon-cancel" class="easyui-linkbutton" style="width:138px;margin:2px 0px 2px 2px;" onclick="del(2);" >删除</a>
				    	</div> -->
	    				<br>
	    				
				    	<div class="inputDivShow">
				    		<div style="height:8px;">
				    			<font color="red">*</font>Log文件
			    			</div>
				    	<br>
				    		<table  id="logTab"class="easyui-datagrid" style="width:280px;height:100px">
	    			    		<thead>
									<tr>
										<th data-options="field:'id',hidden:true"></th>
										<th data-options="field:'fileName',width:276"></th>
									</tr>
						    	</thead>
	    			    	</table>
	    			    	<a iconCls="icon-add" class="easyui-linkbutton" style="width:138px;margin:2px 2px 2px 0;" onclick="addLog();" >添加</a><a iconCls="icon-cancel" class="easyui-linkbutton" style="width:138px;margin:2px 0px 2px 2px;" onclick="del(3);" >删除</a>
				    	</div>
				    	
				    	<div class="inputDivShow">
				    		<div style="height:8px;">
				    			<font color="red">*</font>报表模板
						    	<select id="templateType" name="reportType" class="easyui-combobox" style="width:223px;" data-options="editable:true,multiple:false">
					    			<option selected="selected" value="0">通用型模板</option>
					    			<option value="1">分析型模板</option>
					    		</select>
				    		</div>
				    		<br>
				    		<table id="templateTab"class="easyui-datagrid" style="width:280px;height:100px;">
	    			    		<thead>
									<tr>
										<th data-options="field:'id',hidden:true"></th>
										<th data-options="field:'fileName',width:276"></th>
									</tr>
						    	</thead>
	    			    	</table>
	    			    	<a iconCls="icon-add" class="easyui-linkbutton" style="width:138px;margin:2px 2px 2px 0;" onclick="addTemplate();" >添加</a><a iconCls="icon-cancel" class="easyui-linkbutton" style="width:138px;margin:2px 0px 2px 2px;" onclick="del(4);" >删除</a>
				    	</div>
      				</div>
       				<input  type="hidden"  name="statisticeTaskRequest.id" value="${statisticeTask.id}"  id="id">
					<input  type="hidden"  name="statisticeTaskRequest.cityIds" value="${statisticeTask.cityIds}"  id="cityIds">
					<input  type="hidden"  name="statisticeTaskRequest.boxIds" value="${statisticeTask.boxIds}"  id="boxIds">
					<input  type="hidden"  name="statisticeTaskRequest.logIds" value="${statisticeTask.logIds}"  id="logIds">
					<input  type="hidden"  name="statisticeTaskRequest.templateIds" value="${statisticeTask.templateIds}"  id="templateIds">
					<input  type="hidden"  name="statisticeTaskRequest.createrName" value="${statisticeTask.createrName}"  id="createrName">
				</form> 
			</div>
		</div>
		
		
		<div id="winATU" class="easyui-dialog" buttons="#atu-buttons"title="添加ATU域" style="width:600px;height:400px;overflow: auto;" data-options="onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true">
	    	<ul id="areaTree" name="statisticeTaskRequest.cityIds"  class="easyui-tree" multiple="multiple" data-options="onLoadSuccess:open,editable:false,onlyLeafCheck:true,multiple:true,lines:true,checkbox:true,url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action'"  ></ul>
	   		<div id="atu-buttons">
	   			<a iconCls="icon-ok" class="easyui-linkbutton" onclick="add(1);"  >确定</a>
	   			<a class="easyui-linkbutton" iconCls="icon-undo" onclick="clean(1);" >取消</a>
	    	</div>
	    </div>

	    
	    <div id="winBox"class="easyui-dialog" buttons="#box-buttons"  title="添加测试终端" style=" width:600px;height:400px;overflow: auto;position:relative;"data-options="onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true">
    		<table  id="boxTab2" class="easyui-datagrid" data-options="fit:true,border:false,scrollbarSize:0,fit:true,striped:true,fitColumns:true" >
 				<thead>
					<tr>
						<th data-options="field:'',checkbox:true" style="width:5%;"  ></th>
						<th data-options="field:'boxId',align:'center'" style="width:35%;">BOXID</th>
						<th data-options="field:'testTarget',align:'center'" style="width:30%;">终端类型</th>
						<th data-options="field:'atuName',align:'center'" style="width:30%;">测试域</th>
					</tr>
		    	</thead>
			</table>
			<div id="box-buttons">
   				<a iconCls="icon-ok" class="easyui-linkbutton" onclick="add(2);"  >确定</a>
   				<a class="easyui-linkbutton" iconCls="icon-undo" onclick="clean(2);" >取消</a>
     		</div>
     	</div>
	    <div id="winLog" class="easyui-dialog"  title="添加Log文件" style="width:600px;height:400px;"data-options="buttons:'#log-buttons',onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true">
	    	<table  id="logTab2"class="easyui-datagrid" data-options="fit:true,border:false,scrollbarSize:0,fit:true,striped:true,fitColumns:true" >
 				<thead>
					<tr>
						<th data-options="field:'',checkbox:true" style="width:5%;" ></th>
						<th data-options="field:'id',hidden:true">ID</th>
						<th data-options="field:'fileName',align:'center'"  style="width:95%;">文件名</th>
					</tr>
		    	</thead>
			</table>
			<div id="log-buttons">
				<a iconCls="icon-ok" class="easyui-linkbutton" onclick="add(3);"  >确定</a>
   				<a class="easyui-linkbutton" iconCls="icon-undo" onclick="clean(3);" >取消</a>
     		</div>
        </div>
        
        <div id="winTemplate" class="easyui-dialog"  title="添加报表模板" style="width:600px;height:400px;"data-options="buttons:'#template-buttons',onMove:onDrag,collapsible:false,minimizable:false,maximizable:false,modal:true,closed:true">
	    	<table  id="templateTab2"class="easyui-datagrid" data-options="fit:true,border:false,scrollbarSize:0,fit:true,striped:true,fitColumns:true" >
 				<thead>
					<tr>
						<th data-options="field:'',checkbox:true" style="width:5%;" ></th>
						<th data-options="field:'id',hidden:true">ID</th>
						<th data-options="field:'fileName',align:'center'"  style="width:95%;">文件名</th>
					</tr>
		    	</thead>
			</table>
			<div id="template-buttons">
				<a iconCls="icon-ok" class="easyui-linkbutton" onclick="add(4);"  >确定</a>
   				<a class="easyui-linkbutton" iconCls="icon-undo" onclick="clean(4);" >取消</a>
     		</div>
        </div>
        
	   	<div data-options="region:'south',border:false" style="height:40px;">
		   	<table width="100%" style="border-top:1px solid #95b8e7;">
		   		<tr height="35px">
		    		<td width="50%;" align="right">
		    			<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="saveReport();"  >保存</a>
		    		</td>
		    		<td width="50%;">
		    			<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;" onclick="goToTestPlanList();" >返回</a>
		    		</td>
		   		</tr>
		   	</table>
	  	</div>
	</div> 
  </body>
</html>