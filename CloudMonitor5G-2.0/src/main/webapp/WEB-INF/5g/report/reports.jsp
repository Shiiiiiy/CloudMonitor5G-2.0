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
			$('#type0').datalist('selectRow',0);
			$('#cc').layout('panel','center').panel({content:createIframe("${pageContext.request.contextPath}/report5g/goNsaIndex.action?typeNo=0")});
		});
		/*中移动*/
		function addType0Table(rowIndex, rowData){
			var url ;
			switch(rowData.value){
			case 'nsaIndex':
				url = "${pageContext.request.contextPath}/report5g/goNsaIndex.action?typeNo=0";
			  	break;
			/* case 'ee':
				url = "${pageContext.request.contextPath}/report5g/goEe.action?typeNo=0";
			  	break;
			case 'data':
				url = "${pageContext.request.contextPath}/report5g/goData.action?typeNo=0";
				break; */
			default:
			}
			if(url){
				$('#cc').layout('panel','center').panel({content:createIframe(url)});
				$('#type1').datalist('unselectAll');
			}
			
		}
		function addType1Table(rowIndex, rowData){
			var url = "${pageContext.request.contextPath}/report5g/goTemplateExcel.action?typeNo=0&templateIds="+rowData.value+"";
			if(url){
				$('#cc').layout('panel','center').panel({content:createIframe(url)});
				$('#type0').datalist('unselectAll');
			}
			/* switch(rowData.value){
			case 'ee':
				url = "${pageContext.request.contextPath}/report5g/goEe.action?typeNo=1";
			  	break;
			case 'data':
				url = "${pageContext.request.contextPath}/report5g/goData.action?typeNo=1";
				break;
			default:
			} */
/* 			url = "${pageContext.request.contextPath}/report5g/down.action?templateIds="+rowData.value;
			$.post("${pageContext.request.contextPath}/report5g/exportKpiExcel.action?templateIds="+rowData.value+"",
				function(result){
					console.info(result);
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						
					}
				}
			,"json"); */
		}
		function addType2Table(rowIndex, rowData){
			/* var url ;
			switch(rowData.value){
			case 'ee':
				url = "${pageContext.request.contextPath}/report5g/goEe.action?typeNo=2";
			  	break;
			case 'data':
				url = "${pageContext.request.contextPath}/report5g/goData.action?typeNo=2";
				break;
			default:
			}
			if(url){
				$('#cc').layout('panel','center').panel({content:createIframe(url)});
				$('#type0').datalist('unselectAll');
				$('#type1').datalist('unselectAll');
			} */
		}		
		/* 返回 统计任务详情 */
		function goToReportInfo(){
			goToPage('${pageContext.request.contextPath}/report5g/seeInfo.action');
		}
	</script>
  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	<div id="planId" data-options="region:'west',title:'报表类型',split:false" style="width:20%;height:100%;" >
		<div id='typeName' class="easyui-accordion" data-options="border:false" style="width:100%;height:100%;">
			<div id='type0'  class="easyui-datalist" data-options="onClickRow:addType0Table" title="预定义报表" border="false" lines="true">
				<li value="nsaIndex" >NSA指标报表</li>
 				<!-- <li value="data" >5G数据业务报表</li>
				<li value="ee" >5G异常事件报表</li> -->
			</div>
			<div id='type1'  class="easyui-datalist" data-options="onClickRow:addType1Table" title="自定义报表" border="false" lines="true">
				<c:forEach items="${reportTemplate}" var="quality" varStatus="status">
				    <li value="${quality.valueField}" >${quality.textField}</li>
				</c:forEach>
			</div> 
			
<!-- 		<div id='type1'  class="easyui-datalist" data-options="valueField:'id',textField:'text',url:'${pageContext.request.contextPath}/report5g/reportTemplateList.action',onClickRow:addType1Table" title="自定义报表" border="false" lines="true">
				<li value="data" >5G数据业务报表</li>
				<li value="ee" >5G异常事件报表</li>
			</div>  -->
<!-- 			<ul id='type2' class="easyui-datalist" data-options="onClickRow:addType2Table" title="中国电信5G" border="false" lines="true">
				<li value="data" >5G数据业务报表</li>
				<li value="ee" >5G异常事件报表</li>
			</ul> -->
		</div>
	</div>
	<div id="tt3">
		<a class="icon-back" title="返回任务详情" onclick="goToReportInfo()"></a>
	</div>
  	<div id="nn" data-options="region:'center',split:false,border:false"style="width:100%;height:100%;"></div>	
  </body>
</html>
