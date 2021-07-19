<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>轨迹详情</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript">
	
	window.onLoad = function(){
		console.log(window.location.href.split('?')[1].split('=')[1])
		alert(4324)
	}
	
	
	$(function(){
		
		initHeight();
		
		showMap();
		
	});
	//重新加载
	window.onresize=reload;
	function reload(){
		initHeight();
	}
	

	
	/* 初始化高度 */
	function initHeight(){
		
	
	
		var highTotal = $(document.body).outerHeight();
		$("#tableDiv").height(highTotal-30);
	}
	
	//轨迹详情
	function showMap(){
		var ids = window.location.href.split('?')[1].split('=')[1];
		
		var src = $("#mapIframe").attr("src");
		if(!src){
			$("#mapIframe").attr("src", "${pageContext.request.contextPath}/map/GpsTrace.html");
		}

		setTimeout(function () {
			var data = {
				'logids':ids
			}
			document.getElementById('mapIframe').contentWindow.postMessage(data);
		},2000)
	/**
		$.ajax({
 	             type: "GET",
 	             url: "${pageContext.request.contextPath}/unicomLogItem/mapTrail",
 	             data: {'testLogItemIds': ids},
 	             dataType: "json",
 	             success: function(data){
 					document.getElementById('mapIframe').contentWindow.postMessage(data,'*'); 		   				
                }
   	    });
	 **/

	}
	

	
	function goToUnicomLogList(){
		goToPage('${pageContext.request.contextPath}/unicomLogItem/unicomLogItemListUI.action');
	}
	
	</script>
  </head>
  
  <body style="width: 100%;height:calc(100% - 32px);margin: 0 auto;padding: 0 auto;list-style:none;overflow:auto;">
  	<a class="easyui-linkbutton" iconCls="icon-undo" style="width:100px;margin:1px;" onclick="goToUnicomLogList();" >返回</a>
	<div style="padding-top: 4px;width:100%;">
		<iframe id ='mapIframe' name="mapIframe"  scrolling="auto" frameborder="0"  style="width:100%;height:100%;border:0;margin: 0;" ></iframe>
	</div>
  </body>
</html>
