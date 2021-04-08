<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	    <title>VoLTE质量专题----VoLTE异常事件----VoLTE视频未接通</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<%@ include file="../../../../taglibs/jquery.jsp"%>
		<%@ include file="../../../../taglibs/easyui.jsp"%>
		<script type="text/javascript">
			var wholeClickNum = 0;
			var problemClickNum = 0;
			/**
			 * 当选中tab的时候重新加载该tab中的内容
			 * @param title
			 * @param index
			 */
			function reloadThisPageTab(title,index){
				var target = this;
				var currTab =  $(target).tabs('getSelected'); 
				var url;
				if('整体'==title){
					url = '${pageContext.request.contextPath}/videoNotConnect/goToWhole.action';
					if(0==wholeClickNum){
						$(target).tabs('update', {
							tab : currTab,
							options : {
								content : createIframe(url)
							}
						});
					}
					wholeClickNum++;
				}else if('问题分析'==title){
					url = '${pageContext.request.contextPath}/videoNotConnect/goToProblem.action';
					if(0==problemClickNum){
						$(target).tabs('update', {
							tab : currTab,
							options : {
								content : createIframe(url)
							}
						});
					}
					//调试用暂时放开
					problemClickNum++;
				} 
			}
		</script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	</head>
	<body>
		<div class="easyui-tabs" id="tt" data-options="fit:true,plain:true,tabWidth:100,border:false,onSelect:reloadThisPageTab" >
			<div data-options="closeable:'false',title:'整体',cache:false,selected:true" style="padding:4px;"></div>
			<div data-options="closeable:'false',title:'问题分析',cache:false" style="padding:4px;"></div>
		</div>
	</body>
</html>
