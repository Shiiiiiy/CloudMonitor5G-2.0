<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>设备组LIST页面</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
		var url;
		function centerLayoutGoToPage(title,url){
			$('#cc').layout('panel','center').panel({title:title,content:createIframe(url)});
		}
		/* 添加区域 */
		function addArea(){
			var node = $("#areaTree").tree('getSelected');
			if(!node){
				$.messager.alert('操作提示','请选择某个域','warning');
				return ; 
			}
			$("#areaInfoForm").form('clear');
			if(node.id==-1){
				$("#areaInfoDlg").dialog('setTitle','新建省级子域').dialog('open');
				url = 'terminalGroup/addProvinceGroup.action';
			}else if('Province'==node.attributes.type) {
				$("#areaInfoDlg").dialog('setTitle','【'+node.text+'】'+'新建市级子域').dialog('open');
				url = 'terminalGroup/addCityGroup.action?provinceId='+node.id;
			}else if('City'==node.attributes.type) {
				var parentNode = $("#areaTree").tree('getParent',node.target);
				$("#areaInfoDlg").dialog('setTitle','【'+parentNode.text+'】'+'新建市级子域').dialog('open');
				url = 'terminalGroup/addCityGroup.action?provinceId='+parentNode.id;
			}
		}
		/* 保存添加和编辑 */
		function saveArea(){
			$("#areaInfoForm").form('submit',{
				url:url,
				success:function(result){
					var jsonResult = $.parseJSON(result);
					if (jsonResult.errorMsg) {
						$.messager.alert("系统提示", jsonResult.errorMsg,'error');
						return;
					} else {
						$("#areaInfoDlg").dialog('close');
						$("#areaTree").tree("reload");
					}
				}
			});
		}
		/* 删除区域 */
		function delArea(){
			var node = $("#areaTree").tree('getSelected');
			if(!node||-1==node.id){
				$.messager.alert('操作提示','请选择某个域','warning');
				return ; 
			}
			$.messager.confirm("系统提示", "您确定要删除选中的内容吗?", function(r) {
				if(r){
					$.post('terminalGroup/delSelectedGroup.action',{id:node.attributes.refId},
						function(jsonResult){
							if (jsonResult.errorMsg) {
								$.messager.alert("系统提示", jsonResult.errorMsg,'error');
								return;
							} else {
								$("#areaTree").tree("reload");
								centerLayoutGoToPage('终端列表','${pageContext.request.contextPath}/terminal/terminalListUI.action');
							}
						}
					,'json');
				}
			});
		}
		/* 编辑域 */
		function editArea(){
			var node = $("#areaTree").tree('getSelected');
			if(!node||-1==node.id){
				$.messager.alert('操作提示','请选择某个域','warning');
				return ; 
			}
			url = 'terminalGroup/editTerminalGroup.action';
			$("#areaInfoDlg").dialog('setTitle','编辑【'+node.text+'】').dialog('open');
			$("#areaInfoForm").form('load',{
				name:node.text,
				id:node.attributes.refId
			});
		}
		/* 刷新域 */
		function reloadArea(){
			$("#areaTree").tree('reload');
		}
		/* 单击树节点 */
		function clickTreeNode(node){
			if(-1==node.id||'Province'==node.attributes.type){
				return ; 
			}
			centerLayoutGoToPage('【'+node.text+'】终端列表','${pageContext.request.contextPath}/terminal/terminalListUI.action?cityId='+node.attributes.refId);
		}
	</script>
  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 西,区域树信息 -->
    	<div data-options="region:'west',title:'区域',split:true,tools:'#tt3'" style="width:220px;overflow: auto;">
    		<ul id="areaTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action',lines:true,onClick:clickTreeNode"></ul>
    	</div>
    	<!-- 中,终端列表 -->
    	<div  data-options="region:'center',title:'终端列表',content:createIframe('${pageContext.request.contextPath}/terminal/terminalListUI.action')" >
    	</div>
    	
    	<!-- 添加域 -->
   		<div id="areaInfoDlg" class="easyui-dialog" style="width:400px;height:200px;padding:10px 20px" data-options="onMove:onDrag,closed:true,modal:true,buttons:'#areaInfoDlg-buttons'">
    		<form id="areaInfoForm" method="post">
				<table cellspacing="10px;">
					子域名称：
					<input id="areaName" name="name" class="easyui-textbox" required="true" />
					<input  name="id" type="hidden" />
				</table>
			</form>
   		</div>
		<div id="areaInfoDlg-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveArea();">确定</a> 
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#areaInfoDlg').dialog('close')">取消</a>
		</div>
    	<div id="tt3">
    		<shiroextend:hasAnyPermissions name="terminalgroup:add">
				<a class="icon-add" title="添加域" onclick="addArea()"></a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="terminalgroup:del">
				<a class="icon-cancel" title="删除域" onclick="delArea()"></a>
			</shiroextend:hasAnyPermissions>
			<shiroextend:hasAnyPermissions name="terminalgroup:mod">
				<a class="icon-edit" title="编辑域" onclick="editArea()"></a>
			</shiroextend:hasAnyPermissions>
			<a class="icon-reload" title="刷新域" onclick="reloadArea()"></a>
		</div>
  </body>
</html>
