<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>导入工参</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	
	
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
		$(function(){
			initTable();
		});
		/* 初始化测试计划列表 */
		function initTable(){
			$("#testPlanTable").datagrid({
				// 表头
				columns:[[
					{field:'id',checkbox:true},
					{field:'name',width:80,align:'center',title:'测试计划名称',
						formatter:function(value,row,index){
							if(row.sended){
								return '<a href="#" title="查看测试计划" onclick="showTestPlan('+row.id+')" >'+value+'</a>';
							}else{
								return '<a href="#" title="编辑测试计划" onclick="editTestPlan('+row.id+');" >'+value+'</a>';
							}
							
							
						}
					}, 				
					{field:'version',width:50,align:'center',title:'测试计划版本',
						formatter:function(value,row,index){
							if(row.autoTestUnit){
								return row.autoTestUnit.version;
							}
						}
					},
					{field:'sendTime',width:60,align:'center',title:'下发时间'},
					{field:'loseTime',width:60,align:'center',title:'失效时间'},
					{field:'boxId',width:60,align:'center',title:'boxId'},
					{field:'description',width:50,align:'center',title:'注释'},
					{field:'oper',width:80,align:'center',title:'操作',
						formatter:function(value,row,index){
							if(row.sended){
								return '<a href="${pageContext.request.contextPath}/testPlan/export.action?id='+row.id+'" title="导出测试计划">导出</a> '
								+'<a href="#" onclick="saveAsTestPlan('+row.id+');" title="测试计划另存为">另存为</a>';
							}else{
								return '<a href="${pageContext.request.contextPath}/testPlan/export.action?id='+row.id+'" title="导出测试计划">导出</a> '
								+'<a href="#" onclick="saveAsTestPlan('+row.id+');" title="测试计划另存为">另存为</a> '
								+'<a href="#" onclick="sendTestPlan('+row.id+');" title="下发测试计划">下发</a> '
								+'<a href="#" onclick="editTestPlan('+row.id+');" title="编辑测试计划">编辑</a>';
							}
						}
					}
				]],
				url:'${pageContext.request.contextPath}/testPlan/doPageListJson.action?tIds=${tIds}',
				border:false,
				fitColumns:true,
				pagination:true,
				pageSize:20,
				pageList:[20,50,100,200,500],
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				toolbar:'#tb',
				onLoadSuccess: function(data){//加载完毕后获取所有的checkbox遍历
		            if (data.rows.length > 0) {
		            	var checkAllDisable = false;
		                //循环判断操作为新增的不能选择
		                for (var i = 0; i < data.rows.length; i++) {
		                    //根据operate让某些行不可选
		                    if (data.rows[i].sended) {
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
		                    $("#testPlanTable").datagrid('unselectRow', index - 1);
		                }
		            });
		        }
			});
		}
		/* 查看测试计划 */
		function showTestPlan(testPlanId){
			var terminalId = '${id}';
			if(!terminalId){
				$.messager.alert('错误提示','没有终端,请选择某个终端!','error');
				return ;
			}
			parent.centerLayoutGoToPage('【${name}】查看测试计划','${pageContext.request.contextPath}/testPlan/showTestplanContent.action?id='+testPlanId);
		}
		/* 跳转到添加测试计划界面 */
		function addTestPlan(){
			var terminalId = '${id}';
			if(!terminalId){
				$.messager.alert('错误提示','没有终端,请选择某个终端!','error');
				return ;
			}
			parent.centerLayoutGoToPage('【${name}】新增测试计划','${pageContext.request.contextPath}/testPlan/newTestPlanUI.action?terminalId=${id}');
		}
		/* 跳转到编辑测试计划界面 */
		function editTestPlan(testPlanId){
			var terminalId = '${id}';
			if(!terminalId){
				$.messager.alert('错误提示','没有终端,请选择某个终端!','error');
				return ;
			}
			parent.centerLayoutGoToPage('【${name}】编辑测试计划','${pageContext.request.contextPath}/testPlan/getTestplanContent.action?id='+testPlanId);
		}
		/* 多条件查询 */
		function pageQuery(){
			var startTime = $("#startTime").datetimebox('getValue');
			var endTime = $("#endTime").datetimebox('getValue');
			$("#testPlanTable").datagrid('load',{
				testPlanName:$("#name").textbox('getValue'),
				startTime:startTime,
				endTime:endTime,
				testPlanVersion:$("#version").numberbox('getValue')
			});	
		}
		/* 删除测试计划 */
		function delTestPlan(){
			var testPlanRows = $("#testPlanTable").datagrid('getSelections');//获取表格中用户选中 所有数据
			if(testPlanRows.length==0){// 操作前至少选中一条
				$.messager.alert("系统提示", "请选择某些测试计划!");
				return ;
			}
			$.messager.confirm("系统提示", "确定要删除选中的测试计划吗？", function(r) {
				if (r) {
					var idArray = new Array();
					for(var i=0; i<testPlanRows.length;i++){
						idArray.push(testPlanRows[i].id);
					}
					var ids = idArray.join(",");
					$.post('${pageContext.request.contextPath}/testPlan/delelteTestplan.action', {testPlanIds : ids}, 
						function(result) {
							if (result.errorMsg) {
								$.messager.alert("系统提示", result.errorMsg,'error');
							} else {
								$.messager.alert("系统提示", "已成功删除这条记录!");
								$("#testPlanTable").datagrid("reload");
							}
						},
					'json');
				}
			});
		}
		
		/*批量下发测试计划*/
		function sentSomeTestPlan(){
			var testPlanRows = $("#testPlanTable").datagrid('getSelections');//获取表格中用户选中 所有数据
			if(testPlanRows.length==0){// 操作前至少选中一条
				$.messager.alert("系统提示", "请选择某些测试计划!");
				return ;
			}
			var versionMap = {};
			var pidMap = {};
			var pidNameMap = {};
			for(var i = 0;i < testPlanRows.length;i++){
				var id = testPlanRows[i].terminalId;
				var version = testPlanRows[i].autoTestUnit.version;
				if(versionMap[id] == null){
					versionMap[id] = version;
					pidMap[id] = testPlanRows[i].id;
					pidNameMap[id] = testPlanRows[i].name;
				}else if(version > versionMap[id]){
					versionMap[id] = version;
					pidMap[id] = testPlanRows[i].id;
					pidNameMap[id] = testPlanRows[i].name;
				}
			}
			var tIds = '';
			for(var key in pidMap){
				if(tIds == ''){
					tIds = pidMap[key];
				}else{
					tIds = tIds + "," + pidMap[key];
				}
			}
			var names = '';
			for(var key in pidNameMap){
				if(names == ''){
					names = pidNameMap[key];
				}else{
					names = names + "," + pidNameMap[key];
				}
			}
			
			$.messager.confirm("系统提示", "确定要下发该测试计划吗？", function(r) {
				if (r) {
					$.post('${pageContext.request.contextPath}/testPlan/sendTestplan.action', {tIds : tIds,names:names}, 
						function(result) {
							if (result.errorMsg) {
								$.messager.alert("错误", result.errorMsg,'error');
							} else {
								$.messager.alert("成功", "测试计划已下发",'info');
								$("#testPlanTable").datagrid("reload");
							}
						},
					'json');
				}
			});
		}
		
		/* 另存为测试计划 */
		function saveAsTestPlan(testPlanId){
			$.messager.confirm("系统提示", "确定要另存为测试计划吗？", function(r) {
				if (r) {
					$.post('${pageContext.request.contextPath}/testPlan/saveAsTestplan.action', {id : testPlanId}, 
						function(result) {
							if (result.errorMsg) {
								$.messager.alert("错误", result.errorMsg,'error');
							} else {
								$.messager.alert("成功", "测试计划已另存为:<br>【"+result.testPlanName+"】",'info');
								$("#testPlanTable").datagrid("reload");
							}
						},
					'json');
				}
			});
		}
		/* 下发测试计划 */
		function sendTestPlan(testPlanId){
			$.messager.confirm("系统提示", "确定要下发该测试计划吗？", function(r) {
				if (r) {
					$.post('${pageContext.request.contextPath}/testPlan/sendTestplan.action', {id : testPlanId}, 
						function(result) {
							if (result.errorMsg) {
								$.messager.alert("错误", result.errorMsg,'error');
							} else {
								$.messager.alert("成功", "测试计划已下发",'info');
								$("#testPlanTable").datagrid("reload");
							}
						},
					'json');
				}
			});
		}
	</script>
  </head>
  
  <body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
   	<table id="testPlanTable"></table>
   	<div id="tb">
		测试计划名称：<input id="name" class="easyui-textbox" name="name" data-options="prompt:'请输入测试计划名称...'" style="width:150px;" />
		下发时间： <input id="startTime" class="easyui-datetimebox" name="uploadTime" data-options="prompt:'请输入开始时间...'" style="width:150px;">-<input id="endTime" class="easyui-datetimebox" name="uploadTime" data-options="prompt:'请输入结束时间...'" style="width:150px;">
		测试计划版本：<input id="version" class="easyui-numberbox" data-options="prompt:'请输入测试计划版本...',min:0" name="version" style="width:150px;" />
		<div>
			<a href="#" class="easyui-linkbutton" style="width:80px;" onclick="pageQuery();" iconCls="icon-search" plain='true'>查询</a>
		<shiroextend:hasAnyPermissions name="terminal:add">
			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-add',plain:true" onclick="addTestPlan();">新增</a>
		</shiroextend:hasAnyPermissions>
		<shiroextend:hasAnyPermissions name="terminal:del">
			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-remove',plain:true" onclick="delTestPlan();">删除</a>
		</shiroextend:hasAnyPermissions>
		<shiroextend:hasAnyPermissions name="terminal:del">
			<a class="easyui-linkbutton" style="width:80px;display: ${nodetype}" data-options="iconCls:'icon-download',plain:true" onclick="sentSomeTestPlan();">批量下发</a>
		</shiroextend:hasAnyPermissions>
		<%-- <shiroextend:hasAnyPermissions name="terminal:import">
			<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-upload',plain:true" onclick="alert('add')">导入</a>
		</shiroextend:hasAnyPermissions> --%>
		</div>
	</div>
  </body>
</html>
