<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>自定义任务报表结果展示</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	$(function(){
		initTable();
	});
	var templateName ='${templateName}';
	function initTable(){
		$.messager.progress({
           title: '提示',
           text: '生成excel中...'
       	});
		$.post("${pageContext.request.contextPath}/customeLogReport/exportKpiExcel.action?templateName="+templateName+"",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					if(result.dataList){
					    var list = result.dataList;
						for(var i=0;i<list.length;i++){
						    var id = "sheetId"+i;
						    var selected = false;
						    if(i==0){
						    	selected = true;
						    }
							$('#tt').tabs('add',{
								id: id,
							    title:list[i].sheetName,
							    content:list[i].html,
							    closable:false,
							    selected:selected
							});
							$("#"+id).css("overflow","auto");
						}
						$('#tt').tabs({
							tools:[{
								text:'导出',
								iconCls:'icon-excel',
								handler:function(){
									window.location= "${pageContext.request.contextPath}/customeLogReport/downloadTemplateExcelTotal.action?templateName="+templateName;
									//goToPage("${pageContext.request.contextPath}/report5g/downloadTemplateExcelTotal.action?templateIds="+templateIds);
								}
							}]
						});
					}
					
				}
				$.messager.progress('close');
			}
		,"json");
	}
	//获取Sheet名
	function sele(title,index){
		sheetName=title;
	}
	</script>
  </head>
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
   	<div id="tt"class="easyui-tabs" data-options="fit:true,onSelect:sele">
   		
	</div>
</body>
</html>
