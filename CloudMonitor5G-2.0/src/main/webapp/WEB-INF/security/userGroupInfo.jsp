<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>添加用户组</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<style type="text/css">
	html,body{
		width: 100%;
		height:100%;
		padding: 0;
		margin: 0;
	}
		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 250px;
   		}
   		.inputDivShow input{
   			width:150px;
   		}
   		.inputDivShow select{
   			width:150px;
   		}
   		.listTitle {
		    font-family: "Microsoft Sans Serif";
		    font-size: 12px;
		    font-weight: bold;
		}
		.divLine {
		    -moz-border-bottom-colors: none;
		    -moz-border-left-colors: none;
		    -moz-border-right-colors: none;
		    -moz-border-top-colors: none;
		    border-color: -moz-use-text-color;
		    border-image: none;
		    border-style: solid;
		    border-width: 0 0 1px;
		    color: #95b8e7;
		    margin-bottom: 3px;
		    margin-top: 3px;
		    padding: 0;
		}
	</style>
	<script type="text/javascript">
		/* 保存用户组信息 */
		function saveUserGroup(url){
			$('#addform').form('submit', {
				url:url,
				onSubmit : function(param) {
					var isValid = $(this).form('validate');
					if(isValid){
						var permissionNodes = $('#permissionTree').tree('getChecked');
						var areaNodes = $('#areaTree').tree('getChecked',['checked','indeterminate']);
						if(null!=permissionNodes&&undefined!=permissionNodes&&''!=permissionNodes){
							var idArray = new Array();
							for(var i=0; i<permissionNodes.length;i++){
								if(!permissionNodes[i].children||permissionNodes[i].children.length==0){
									idArray.push(permissionNodes[i].id);
								}
							}
							// 将选中多条记录id 用","拼接为一个字符串
							var ids = idArray.join(",");
							param.permissionIds = ids;
							
							var idArray1 = new Array();
							for(var i=0; i<areaNodes.length;i++){
								idArray1.push(areaNodes[i].id);
							}
							// 将选中多条记录id 用","拼接为一个字符串
							var ids1 = idArray1.join(",");
							param.areaIds = ids1;
							return true;
						}else{
							$.messager.alert("系统提示", "<font color='red'><strong>请添加用户组权限<strong></font>");
							return false;
						}
					}
					return isValid;
				},
				success : function(result) {
					var jsonData = $.parseJSON(result);
					if (jsonData.errorMsg) {
						$.messager.alert("系统提示", jsonData.errorMsg,"error");
						return;
					} else {
						$.messager.alert("系统提示", "保存成功");
						goToPage("${pageContext.request.contextPath}/userGroup/userGroupListUI.action");
					}
				}
			});
		}
		/* tree级联check */
		function cascadeChecked(node, checked){
			var treeOptions = $("#areaTree").tree('options');
			if(treeOptions.cascadeCheck){
				return;
			}
			$("#areaTree").tree({cascadeCheck:true});
		}
	</script>
  </head>
  	
  <body class="easyui-layout">
  	<div data-options="region:'north',border:false">
	  	<table width="100%" align="center" border="0" cellpadding="0"
			cellspacing="0">
			<tr>
				<td align="center" height="25" width="151" class="tabTitle">
					<span class="listTitle" >
						<c:if test="${operType=='show'}">用户组基本内容</c:if>
						<c:if test="${operType=='add'}">添加用户组</c:if>
						<c:if test="${operType=='edit'}">修改用户组</c:if>
					</span>
				</td>
				<td class="tabBlank">
					&nbsp;
				</td>
			</tr>
		</table>
		<div class="divLine"></div>
		<form method="post" id="addform">
			<input type="hidden" name="id" value="${userGroup.id}" />
		    <div class="inputDivShow">用户组名
		   		<input name="name" value="${userGroup.name}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
		   	</div>
		    <div class="inputDivShow">用户组描叙
		   		<input  name="description" value="${userGroup.description}"  class="easyui-textbox" data-options="validType:'length[1,100]'" />
		   	</div>
	   	</form>
	   	<div class="divLine"></div>
   	</div>
   	<div data-options="region:'center',border:false" style="overflow-y:auto;">
	   	<div align="left" style="float:left;padding-left: 50px;width:40%;">
	   		<ul id="permissionTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/userGroup/findPermissions.action?id=${userGroup.id}',checkbox:true,lines:true"></ul>
	   	</div>
	   	<div align="left" style="float:left;width:40%" >
			<ul id="areaTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/userGroup/findAreaMenus.action?id=${userGroup.id}',checkbox:true,lines:true,cascadeCheck:false,onCheck:cascadeChecked"></ul>
		</div>
	</div>
	<div data-options="region:'south',border:false" style="height:40px;">
	   	<table width="100%" style="border-top:1px solid #95b8e7;">
	   		<tr valign="middle" height="35px">
	    		<td width="50%;" align="center">
	    			<c:if test="${operType=='add'}">
	    				<a iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="saveUserGroup('${pageContext.request.contextPath}/userGroup/addUserGroup');"  >确认</a>
		    		</c:if>
		    		<c:if test="${operType=='edit'}">
		    			<a iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="saveUserGroup('${pageContext.request.contextPath}/userGroup/editUserGroup');"  >确认</a>
		    		</c:if>
		    		<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;" onclick="goBack();" >返回</a>
	    		</td>
	   		</tr>
	   	</table>
  	</div>
  </body>
</html>
