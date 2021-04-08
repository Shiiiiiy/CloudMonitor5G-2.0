<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>账号组管理</title>
<%@ include file="../../taglibs/jquery.jsp"%>
<%@ include file="../../taglibs/easyui.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
<script>
	/* 修改账号组 */
	function modUserGroup() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			goToPage("${pageContext.request.contextPath}/userGroup/editUserGroupUI?id="+row.id);
		}else{
			$.messager.alert("系统提示", "请选择某个账号组!");
		}
	}

	/* 删除账号组 */
	function delUserGroup(){
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			if(row.powerGroup){
				$.messager.alert("警告", "超级管理员不允许删除",'warning');
				return;
			}
			$.messager.confirm("系统提示", "您确定要删除这条记录吗?", function(r) {
				if (r) {
					$.post('userGroup/delUserGroup', {
						id : row.id
					}, function(result) {
						if (result.errorMsg) {
							$.messager.alert("系统提示", result.errorMsg);
						} else {
							$.messager.alert("系统提示", "已成功删除这条记录!");
							$("#dg").datagrid("reload");
						}
					}, 'json');
				}
			});
		}else{
			$.messager.alert("系统提示", "请选择某个账号组!");
		}
	}
	/* 格式化账号组名 */
	function formatterUsergroupName(value,row,index){
		return '<a href="${pageContext.request.contextPath}/userGroup/showUserGroupInfo?id='+row.id+'" >'+value+'</a>';
	}

</script>
</head>

<body class="easyui-layout">
	<div data-options="region:'center',title:'账号组信息列表'" style="height:120px;">
		<table id="dg" class="easyui-datagrid"
			style="width: 100%;height: 100%;"
            url="${pageContext.request.contextPath}/userGroup/doPageListJson"
			toolbar="#toolbar" pagination="true" rownumbers="true" border="false"
			fitColumns="true" singleSelect="true" pageSize="20">
			<thead>
				<tr>
					<th field="id" width="50" hidden="true">编号</th>
					<th field="name" data-options="formatter:formatterUsergroupName" width="50">账号组名</th>
					<th field="description" width="300">账号组描叙</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar">
			<shiroextend:hasAnyPermissions name="usergroup:add"> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					iconCls="icon-add" plain="true" onclick="goToPage('${pageContext.request.contextPath}/userGroup/addUserGroupUI.action');">新增账号组</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="usergroup:mod"> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					iconCls="icon-edit" plain="true" onclick="modUserGroup()">修改账号组</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="usergroup:del"> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true" onclick="delUserGroup()">删除账号组</a>
			</shiroextend:hasAnyPermissions>
		</div>
	</div>
</body>
</html>
