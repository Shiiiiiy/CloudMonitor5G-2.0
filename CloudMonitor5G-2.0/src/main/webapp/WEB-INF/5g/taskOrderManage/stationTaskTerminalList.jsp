<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>单验测试任务计划_设备树LIST页面</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript">
		var url;
		function centerLayoutGoToPage(title,url){
			$('#cc').layout('panel','center').panel({title:title,content:createIframe(url)});
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
			centerLayoutGoToPage('【'+node.text+'】单验测试任务列表','${pageContext.request.contextPath}/stationReportCreatTask/testTaskListUI.action?cityId='+node.attributes.refId);
		}
	</script>
  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
    	<!-- 西,区域树信息 -->
    	<div data-options="region:'west',title:'区域',split:true,tools:'#tt3'" style="width:220px;overflow: auto;">
    		<ul id="areaTree" class="easyui-tree" data-options="url:'${pageContext.request.contextPath}/terminalGroup/terminalGroupTree.action',lines:true,onClick:clickTreeNode"></ul>
    	</div>
    	<!-- 中,终端列表 -->
    	<div  data-options="region:'center',title:'单验测试任务列表',content:createIframe('${pageContext.request.contextPath}/stationReportCreatTask/testTaskListUI.action')" >
    	</div>
   
    	<div id="tt3">
			<a class="icon-reload" title="刷新域" onclick="reloadArea()"></a>
		</div>
  </body>
</html>
