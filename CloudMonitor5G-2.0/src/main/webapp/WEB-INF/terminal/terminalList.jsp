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
		function initTable(){
			$("#terminalListTable").datagrid({
				// 表头
				columns:[[
					{field:'name',width:60,align:'center',title:'终端名称',
						formatter:function(value,row,index){
							return '<a href="javaScript:void(0);" onclick="editTemrinal('+row.id+')" >'+value+'</a>';
						}
					}, 				
					{field:'boxId',width:90,align:'center',title:'终端IMEI'},
					{field:'longitude',width:90,align:'center',title:'经度'},
					{field:'latitude',width:90,align:'center',title:'维度'},
					{field:'fileName',width:90,align:'center',title:'日志名'},
					{field:'exception',width:90,align:'center',title:'当前事件'},
					{field:'testTargetStr',width:90,align:'center',title:'终端类型'},
					{field:'softwareVersion',width:165,align:'center',title:'软件版本'},
					{field:'downVersion',width:165,align:'center',title:'下发版本'}, 
					{field:'hardwareVersion',width:80,align:'center',title:'硬件版本'},
					{field:'manufacturer',width:60,align:'center',title:'厂商'},
					{field:'remark',width:60,align:'center',title:'备注'}, 
					{field:'installDateFormt',width:80,align:'center',title:'安装日期',
						formatter:function(value,row,index){
							if(!value){
								return "";
							}
							var valStr = value + "";
							return valStr.replace("T", " ");
						}
					}, 
					{field:'online',width:45,align:'center',title:'状态',
						styler:function onlineStyler(value,row,index){
							if (value){
								return 'color:green;';
							}else{
								return 'color:red;';
							}
						},formatter:function(value,row,index){
							if(value!=undefined){
								if(value){
									return '在线';
								}else{
									return '离线';
								}
							}
						}
					},
					/* {field:'enable',width:60,align:'center',title:'是否可用',
						formatter:function(value,row,index){
							if(value!=undefined){
								if(value){
									return '可用';
								}else{
									return '禁用';
								}
							}
						}
					},
					*/
					{field:'testPlanVersion',width:90,align:'center',title:'测试计划版本'}
					/* {field:'oper',width:75,align:'center',title:'操作',
						formatter:function(value,row,index){
							if(row.online!=undefined){
								if(row.online){
									return '<a href="#" onclick="restartTerminal('+row.id+','+row.name+');" title="重启终端">重启</a> <a href="#" onclick="cutTestLog('+row.id+','+row.name+');" title="切割日志">切日志</a>';
								}else{
									return '重启 切日志';
								}
							}
						}
					} */
				]],
				url:'${pageContext.request.contextPath}/terminal/doPageListJson.action?cityId=${id}',
				border:false,
				//fitColumns:true,
				pagination:true,
				singleSelect:true,
				//填满区域
				fit:true,
				//奇偶变色
				striped:true,
				rownumbers:true,
				toolbar:'#tb',
				onClickRow: function(rowIndex, rowData){
		        	var nodes = $('#terminalListTable').datagrid('getSelections');
		        	if(nodes.length > 0){
			        	$('#delOprate').linkbutton('enable');        		
		        	}else{
		        		$('#delOprate').linkbutton('disable');
		        	}
		        	if(nodes[0].testTargetStr == "5G单模块商务终端"){
		        		$('#enableOprate').linkbutton('disable');
		        	}else{
		        		$('#enableOprate').linkbutton('enable');
		        	}
	        	}
			});
		}
		/* 跳转到添加设备页面 */
		function addTerminal(){
			var cityId = '${id}';
			if(!cityId){
				$.messager.alert('错误提示','没有城市,请选择某个城市!','error');
				return ;
			}
			parent.centerLayoutGoToPage('【${name}】添加设备','${pageContext.request.contextPath}/terminal/newTerminalUI.action?cityId=${id}');
		}
		/* 跳转到编辑设备页面 */
		function editTemrinal(tid){
			var cityId = '${id}';
			if(!cityId){
				$.messager.alert('错误提示','没有城市,请选择某个城市!','error');
				return ;
			}
			parent.centerLayoutGoToPage('【${name}】编辑设备','${pageContext.request.contextPath}/terminal/editTerminalUI.action?cityId=${id}&id='+tid);
		}
		/* 多条件查询 */
		function pageQuery(){
			var testTarget = $("#testTarget").combobox('getValue');
			var online = $("#tOnline").combobox('getValue');
			var installDate = $("#installDate").datebox('getValue');
			var time3 = installDate == "" ? installDate : Date.parse(installDate);
			var boxId = $("#boxId").textbox('getValue');
			if(online!='请选择'){
				$("#terminalListTable").datagrid('load',{
					name:$("#tName").textbox('getValue'),
					terOnline : online,
					testTarget : testTarget,
					installDate : time3,
					boxId : boxId
				});	
			}else{
				$("#terminalListTable").datagrid('load',{
					name:$("#tName").textbox('getValue'),
					testTarget : testTarget,
					installDate : time3,
					boxId : boxId
				});	
			}
		}
		/* 删除终端 */
		function delTerminal(){
			var row = $('#terminalListTable').datagrid('getSelected');
			if (row) {
				/* $.messager.confirm("系统提示", "确定要删除选中车载终端？这会同时删除该终端的已发送测试计划等信息。如果该终端在线，这将会失去控制。", function(r) { */
				$.messager.confirm("系统提示", "删除以后不可恢复，确认继续？", function(r) {	
					if (r) {
						$.post('${pageContext.request.contextPath}/terminal/delTerminal.action', {id : row.id}, 
							function(result) {
								if (result.errorMsg) {
									$.messager.alert("系统提示", result.errorMsg,'error');
								} else {
									$.messager.alert("系统提示", "已成功删除这条记录!");
									$("#terminalListTable").datagrid("reload");
								}
							},
						'json');
					}
				});
			}else{
				$.messager.alert("系统提示", "请选择某个终端!");
			}
		}
		
		function enableCell(){
			var row = $('#terminalListTable').datagrid('getSelected');
			console.log(row.id);
			if (row) {
				if(row.enable){
					$.messager.confirm("系统提示", "确定禁用此终端吗?", function(r) {
						if (r) {
							$.post('${pageContext.request.contextPath}/terminal/enableTerminalOfFalse.action', {id : row.id}, 
								function(result) {
									if (result.errorMsg) {
										$.messager.alert("系统提示", result.errorMsg,'error');
									} else {
										$.messager.alert("系统提示", "禁用成功!");
									}
								},
							'json');
						}
					});
				}else{
					$.messager.alert("系统提示", "此终端已为禁用状态!");
				}
			}else{
				$.messager.alert("系统提示", "请选择某个终端!");
			}
		}
		
		/* 重启终端 */
		function restartTerminal(terminalId,terminalName){
			$.messager.confirm("系统提示", "确定要重启【"+terminalName+"】?", function(r) {
				if (r) {
					alert("重启");
				}
			});
		}
		
		/* 切割日志 */
		function cutTestLog(terminalId,terminalName){
			$.messager.confirm("系统提示", "确定要【"+terminalName+"】进行切割日志?", function(r) {
				if (r) {
					alert("切日志");
				}
			});
		}
		/* 跳转到批量导入页面 */
		function importCell(){
			goToPage('${pageContext.request.contextPath}/terminal/goImport.action');
		}
		
		/*导出*/
		function exportCell(){
			var cityId = '${id}';
			if(!cityId){
				$.messager.alert('错误提示','没有城市,请选择某个城市!','error');
				return ;
			}
			var testTarget = $("#testTarget").combobox('getValue');
			var terOnline = $("#tOnline").combobox('getValue');
			var installDate = $("#installDate").datebox('getValue');
			var time3 = installDate == "" ? installDate : Date.parse(installDate);
			var boxId = $("#boxId").textbox('getValue');
			var name = $("#tName").textbox('getValue');
			window.location= "${pageContext.request.contextPath}/terminal/downloadData.action?cityId="+cityId
					+"&testTarget="+testTarget+"&terOnline="+terOnline+"&installDate="+time3+"&boxId="+boxId+"&name="+name;
		}
	</script>
  </head>
  
  <body style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
   	<table id="terminalListTable"></table>
   	<div id="tb">
		安装日期： <input id="installDate" class="easyui-datebox" name="installDate" data-options="" style="width:150px;">
		终端名称： <input id="tName" class="easyui-textbox" name="name" data-options="prompt:'请输入终端名...'" style="width:150px;">
		BOXID： <input id="boxId" class="easyui-textbox" name="boxId" data-options="prompt:'请输入boxid...'" style="width:150px;">
		终端类型：<select id="testTarget" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="testTarget" style="width:150px;">
					<option value="">&nbsp;</option>
					<option value="0">自动LTE</option>
	    			<option value="1">LTE单模块商务终端</option>
	    			<option value="2">LTE-FI</option>
	    			<option value="3">5G单模块商务终端</option>
	    			<option value="4">PC测试软件</option>
		</select>
		终端状态：<select id="tOnline" class="easyui-combobox" data-options="editable:false,panelHeight:'auto'" name="online" style="width:150px;">
			<option value="请选择">请选择</option>
			<option value="true">在线</option>
			<option value="false">离线</option>
		</select>
		<div style="text-align: center;">
			<a href="#" class="easyui-linkbutton" style="width:80px;" onclick="pageQuery();" iconCls="icon-search" plain='true'>查询</a>
			<shiroextend:hasAnyPermissions name="terminal:add">
				<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-add',plain:true" onclick="addTerminal();">新增</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="terminal:del">
				<a id="delOprate" class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-remove',plain:true,disabled:true" onclick="delTerminal();">删除</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="terminal:import">
				<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-upload',plain:true" onclick="importCell();">导入</a>
			</shiroextend:hasAnyPermissions>    
			<shiroextend:hasAnyPermissions name="terminal:import">
				<a class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-download',plain:true" onclick="exportCell();">导出</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="terminal:enable">
				<a id="enableOprate" class="easyui-linkbutton" style="width:80px;" data-options="iconCls:'icon-no',plain:true" onclick="enableCell();">禁用</a>
			</shiroextend:hasAnyPermissions>
		</div>
	</div>
  </body>
</html>
