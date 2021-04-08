<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
<!-- <title>账号管理</title> -->
<title>分析账号管理</title>
<%@ include file="../../taglibs/jquery.jsp"%>
<%@ include file="../../taglibs/easyui.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.detailview.js"></script> --%>
<script>
	$.extend($.fn.validatebox.defaults.rules, {
		/*必须和某个字段相等*/
		equalTo : {
			validator : function(value, param) {
				return $(param[0]).val() == value;
			},
			message : '两次输入密码不一致'
		}
	});
	

	
	
	var url;
	/* 删除账号 */
	function deleteUser() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			if(row.powerUser){
				$.messager.alert("警告", "超级管理员不允许删除",'warning');
				return;
			}
			$.messager.confirm("系统提示", "您确定要删除这条记录吗?", function(r) {
				if (r) {
					$.post('user/delUser', {id : row.id}, 
						function(result) {
							if (result.errorMsg) {
								$.messager.alert("系统提示", result.errorMsg);
							} else {
								$.messager.alert("系统提示", "已成功删除这条记录!");
								$("#dg").datagrid("reload");
							}
						},
					'json');
				}
			});
		}else{
			$.messager.alert("系统提示", "请选择某个账号!");
		}
	}

	/* 跳转到添加账号界面 */
	function newUser() {
		$("#adddlg").dialog('open').dialog('setTitle', '添加账号');
		$('#addform').form('clear');
		$("#addUserGroupIds").combobox({url:'${pageContext.request.contextPath}/user/findUserGroups'});
	}

	/* 跳转编辑账号界面 */
	function editUser() {
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			$("#editdlg").dialog('open').dialog('setTitle', '编辑账号');
			$('#editform').form('load', row);
			$("#editUserGroupIds").combobox({url:'${pageContext.request.contextPath}/user/findUserGroups?id='+row.id}); 
			url = 'user/editUser?id=' + row.id;
		}else{
			$.messager.alert("系统提示", "请选择某个账号!");
		}
	}

	/* 保存添加 */
	function saveUser() {
		$('#addform').form('submit', {
			url : 'user/addUser',
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				var resultData =$.parseJSON(result);
				if (resultData.errorMsg) {
					$.messager.alert("系统提示", resultData.errorMsg,"error");
					return;
				} else {
					$.messager.alert("系统提示", "保存成功");
					$('#adddlg').dialog('close');
					$("#dg").datagrid("reload");
				}
			}
		});
	}

	/* 保存编辑 */
	function saveEditUser() {
		$('#editform').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				} else {
					$.messager.alert("系统提示", "保存成功");
					$('#editdlg').dialog('close');
					$("#dg").datagrid("reload");
				}
			}
		});
	}
	
	/* 跳转修改密码页面 */
	function changepwd(){
		var row = $('#dg').datagrid('getSelected');
		if (row) {
			//window.parent.changepwd();
			$("#changePasswordDlg").dialog('open').dialog('setTitle', '修改【'+row.username+'】密码');
			url = 'user/changePassword?id=' + row.id;
		}else{
			$.messager.alert("系统提示", "请选择某个账号!");
		}
		
	}
	/* 保存修改密码 */
	function changepassword() {
		$('#changePasswordForm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				} else {
					$.messager.alert("系统提示", "修改密码成功");
					$('#changePasswordDlg').dialog('close');
					$("#dg").datagrid("reload");
				}
			}
		});
	}
</script>
</head>

<body class="easyui-layout">
	<div data-options="region:'center',title:'账号信息列表',border:false" >
		<!-- 账号列表 -->
		<table id="dg" class="easyui-datagrid"
			style="width: 100%;height: 100%;"
            url="${pageContext.request.contextPath}/user/doPageListJson"
			toolbar="#toolbar" pagination="true" rownumbers="true"
			fitColumns="true" border="false" scrollbarSize="0" singleSelect="true" pageSize="20">
			<thead>
				<tr>
					<th field="id" width="50" hidden="true">编号</th>
					<th field="username" width="50">账号名</th>
					<th field="phone" width="50">手机号</th>
					<th field="email" width="50">Email</th>
					<th field="department" width="50">所属部门</th>
					<th field="jobPosition" width="50">工作职位</th>
					<th field="description" width="50">账号描述</th>
					<th field="userGroupStr" width="150">账号组</th>
				</tr>
			</thead>
		</table>
		<!-- 账号列表操作按钮 -->
		<div id="toolbar">
			<shiroextend:hasAnyPermissions name="user:add"> 
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()">添加账号</a> 
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="user:mod">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑账号</a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="user:del">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteUser()">删除账号</a> 
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="user:changepassword">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="changepwd()">重置密码</a>
			</shiroextend:hasAnyPermissions>
		</div>
		<!-- 添加账号dialog -->
		<div id="adddlg" class="easyui-dialog" data-options="onMove:onDrag"
			style="width:380px;height:350px;padding:10px 20px" closed="true"
			buttons="#adddlg-buttons">
			<form id="addform" method="post">
				<table cellspacing="4px;" style="font-size: 12px;">
					<tr>
						<td>账号名：</td>
						<td><input name="username" class="easyui-textbox"
							required="true" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>密码：</td>
						<td><input id="addPassword" name="password" type="password"
							class="easyui-textbox" required="true" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input type="password"
							class="easyui-textbox" required="true" validType="equalTo['#addPassword']" invalidMessage="两次输入密码不匹配" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>联系电话：</td>
						<td><input name="phone" validType="phone" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>Email：</td>
						<td><input name="email" class="easyui-textbox"
							validType="email" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>所属部门：</td>
						<td><input name="department" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>工作职位：</td>
						<td><input name="jobPosition" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>账号描述：</td>
						<td><input name="description" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>账号组：</td>
						<td><input id="addUserGroupIds" name="userGroupIds" required="true"  class="easyui-combobox"
							 style="width: 200px;"
							data-options="
								valueField:'id',
								editable:false,
								textField:'name',
								multiple:true
								 ">
						</td>
					</tr>
				</table>
			</form>
		</div>
		<!-- 添加账号dialog的操作按钮 -->
		<div id="adddlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-ok" onclick="saveUser()">确定</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#adddlg').dialog('close')">取消</a>
		</div>
		
		<!-- 修改账号dialog -->
		<div id="editdlg" class="easyui-dialog" data-options="onMove:onDrag"
			style="width:380px;height:300px;padding:10px 20px" closed="true"
			buttons="#editdlg-buttons">
			<form id="editform" method="post">
				<table cellspacing="4px;" style="font-size: 12px;">
					<tr>
						<td>账号名：</td>
						<td><input name="username" class="easyui-textbox"
							required="true" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>联系电话：</td>
						<td><input name="phone" validType="phone" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>Email：</td>
						<td><input name="email" class="easyui-textbox"
							validType="email" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>所属部门：</td>
						<td><input name="department" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>工作职位：</td>
						<td><input name="jobPosition" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>账号描述：</td>
						<td><input name="description" class="easyui-textbox"
							style="width: 200px;"></td>
					</tr>
					<tr>
						<td>账号组：</td>
						<td><input id="editUserGroupIds" name="userGroupIds" required="true"  class="easyui-combobox"
							 style="width: 200px;"
							data-options="
								valueField:'id',
								editable:false,
								textField:'name',
								multiple:true
								 ">
						</td>
					</tr>
				</table>
			</form>
		</div>
		<!-- 修改账号dialog的操作按钮 -->
		<div id="editdlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-ok" onclick="saveEditUser()">确定</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#editdlg').dialog('close')">取消</a>
		</div>

		
		<!-- 修改密码的dialog -->
		<div id="changePasswordDlg" class="easyui-dialog" data-options="onMove:onDrag"
			style="width:400px;height:200px;padding:10px 20px"  closed="true"
			buttons="#changePasswordDlg-buttons">
			<form id="changePasswordForm" method="post">
				<table cellspacing="10px;">
					修改密码：
					<input id="password" name="password" class="easyui-textbox"
						required="true" type="password" value="" />
					<br /> 
					<br />
					确认密码：
					<input type="password"  id="repassword"
						required="true" class="easyui-textbox"
						validType="equalTo['#password']" invalidMessage="两次输入密码不匹配" />
				</table>
			</form>
		</div>
		<!-- 修改密码的dialog的操作按钮 -->
		<div id="changePasswordDlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-ok" onclick="changepassword()">确定</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-cancel"
				onclick="javascript:$('#changePasswordDlg').dialog('close')">取消</a>
		</div>
	</div>
</body>
</html>
