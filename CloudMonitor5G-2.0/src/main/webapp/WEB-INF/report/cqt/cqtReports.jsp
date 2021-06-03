<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix ="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>CQT报表业务</title>

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
	
	});
		/*打开视频报表页*/
		function addVideoTable(obj,a){
			var alist = document.getElementsByTagName("a");
			for(var i =0;i < alist.length;i++){
			alist[i].style.color = "#000"; //给所有a标签赋原色
			}
			obj.style.color = "#f00"; //令当前标签高亮 
			var url ;
			url = "${pageContext.request.contextPath}/cqtReport/goVideo.action?typeNo="+a+"";
			$('#cc').layout('panel','center').panel({content:createIframe(url)});
		}	
		/*打开语音报表页*/
		function addVoiceTable(obj,a){
			var alist = document.getElementsByTagName("a");
			for(var i =0;i < alist.length;i++){
			alist[i].style.color = "#000"; //给所有a标签赋原色
			}
			obj.style.color = "#f00"; //令当前标签高亮 
			var url ;
			url = "${pageContext.request.contextPath}/cqtReport/goVoice.action?typeNo="+a+"";
			$('#cc').layout('panel','center').panel({content:createIframe(url)});
		}	
		/*打开信息报表页*/
		function addDataTable(obj,a){
			var alist = document.getElementsByTagName("a");
			for(var i =0;i < alist.length;i++){
			alist[i].style.color = "#000"; //给所有a标签赋原色
			}
			obj.style.color = "#f00"; //令当前标签高亮 
			var url ;
			url = "${pageContext.request.contextPath}/cqtReport/goData.action?typeNo="+a+"";
			$('#cc').layout('panel','center').panel({content:createIframe(url)});
		}	
		
		/* 返回 统计任务详情 */
		function goToReportInfo(){
			goToPage('${pageContext.request.contextPath}/cqtReport/seeInfo.action');
		}
	</script>
  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
<div id="planId" data-options="region:'west',title:' ',split:false,tools:'#tt3'" class="easyui-accordion" style="width:20%;height:100%;">
	<div id="number" title="中国移动LTE" >
		<a id="voiceButton" class="easyui-linkbutton" style="width:100%;" onclick="addVoiceTable(this,0);"  >VOLTE语音业务报表</a>
		<a id="videoButton"  class="easyui-linkbutton" style="width:100%;" onclick="addVideoTable(this,0);"  >VOLTE视频业务报表</a>
		<a id="dataButton"  class="easyui-linkbutton" style="width:100%;" onclick="addDataTable(this,0);"  >LTE数据业务报表</a>
	</div>
	<div title="中国联通LTE">
		<a id="voiceButton"  class="easyui-linkbutton" style="width:100%;" onclick="addVoiceTable(this,1);"  >VOLTE语音业务报表</a>
		<a id="videoButton"  class="easyui-linkbutton" style="width:100%;" onclick="addVideoTable(this,1);"  >VOLTE视频业务报表</a>
		<a id="dataButton"  class="easyui-linkbutton" style="width:100%;" onclick="addDataTable(this,1);"  >LTE数据业务报表</a>
	</div>
	<div title="中国电信LTE" >
		<a id="voiceButton"  class="easyui-linkbutton" style="width:100%;" onclick="addVoiceTable(this,2);"  >VOLTE语音业务报表</a>
		<a id="videoButton"  class="easyui-linkbutton" style="width:100%;" onclick="addVideoTable(this,2);"  >VOLTE视频业务报表</a>
		<a id="dataButton"  class="easyui-linkbutton" style="width:100%;" onclick="addDataTable(this,2);"  >LTE数据业务报表</a>
	</div>
</div>
<div id="tt3">
	<a class="icon-back" title="返回任务详情" onclick="goToReportInfo()"></a>
</div>
  <div id="nn" data-options="region:'center',split:false"style="width:100%;height:100%;">
   </div>	
  </body>
</html>
