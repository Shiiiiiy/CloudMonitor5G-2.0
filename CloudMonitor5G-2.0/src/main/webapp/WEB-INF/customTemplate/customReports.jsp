<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix ="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>报表业务</title>

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

		$(function(){
			$('#type1').datalist('selectRow',0);
		});
		function addType1Table(rowIndex, rowData){
			var url = "${pageContext.request.contextPath}/customeLogReport/goTemplateExcel.action?templateName="+rowData.value+"";
			if(url){
				$('#cc').layout('panel','center').panel({content:createIframe(url)});
				$('#type0').datalist('unselectAll');
			}
		}
		/* 返回 自定义统计任务 */
		function goToReportInfo(){
			goToPage('${pageContext.request.contextPath}/customeLogReport/listUI.action');
		}
	</script>
  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	<div id="planId" data-options="region:'west',title:'报表类型',split:false" style="width:20%;height:100%;" >
		<div id='typeName' class="easyui-accordion" data-options="border:false" style="width:100%;height:100%;">
			<div id='type1'  class="easyui-datalist" data-options="onClickRow:addType1Table" title="自定义报表" border="false" lines="true">
				<c:forEach items="${reportTemplate}" var="quality" varStatus="status">
				    <li value="${quality.valueField}" >${quality.textField}</li>
				</c:forEach>
			</div> 
		</div>
	</div>
	<div id="tt3">
		<a class="icon-back" title="返回任务详情" onclick="goToReportInfo()"></a>
	</div>
  	<div id="nn" data-options="region:'center',split:false,border:false"style="width:100%;height:100%;"></div>	
  </body>
</html>
