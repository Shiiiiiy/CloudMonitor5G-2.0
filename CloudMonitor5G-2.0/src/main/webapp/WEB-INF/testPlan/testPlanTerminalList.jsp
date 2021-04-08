<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>测试计划_设备树LIST页面</title>

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

		/* 刷新域 */
		function reloadTerminalTree(){
			$("#terminalTree").tree('reload');
		}
		/* 单击树节点 */
		function clickTreeNode(node){
			var nodetype = null;
			if(node.attributes.type == 'Terminal'){
				nodetype = 'ter';
			}else{
				nodetype = 'city';
			}
			var ids = '';
			ids = getTreeTerId(node,'');
			if(ids != null && ids != ''){
				centerLayoutGoToPage('【'+node.text+'】测试计划列表','${pageContext.request.contextPath}/testPlan/testPlanListUI.action?tIds='+ids+'&nodetype='+nodetype);
			}
			/* if(node.attributes&&'Terminal'==node.attributes.type){
				centerLayoutGoToPage('【'+node.text+'】测试计划列表','${pageContext.request.contextPath}/testPlan/testPlanListUI.action?terminalId='+ids);
			} */
		}
		/*获取树下所有设备id*/
		function getTreeTerId(node,ids){
			if(node.children){
				for(var i = 0; i < node.children.length;i++){
					ids = getTreeTerId(node.children[i],ids);
				}
			}else if(node.attributes && 'Terminal'==node.attributes.type){
				if(ids == '' || ids == null){
					ids = node.attributes.refId;
				}else{
					ids = ids + "," + node.attributes.refId;
				}
			}
			return ids;
		}
	</script>
  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 西,终端树信息 -->
    	<div data-options="region:'west',title:'终端',split:true,tools:'#tt3'" style="width:220px;overflow: auto;">
    		<ul id="terminalTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/terminalGroup/testPlanTerminalTree.action',lines:true,onClick:clickTreeNode"></ul>
    	</div>
    	<!-- 中,测试计划列表 -->
    	<div  data-options="region:'center',title:'测试计划列表',content:createIframe('${pageContext.request.contextPath}/testPlan/testPlanListUI.action')" >
    	</div>
    	<div id="tt3">
			<a class="icon-reload" title="刷新终端树" onclick="reloadTerminalTree()"></a>
		</div>
  </body>
</html>
