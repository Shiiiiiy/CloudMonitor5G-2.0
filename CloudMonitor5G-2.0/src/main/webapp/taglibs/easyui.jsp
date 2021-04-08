<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/js/easyui/themes/color.css" />
<script type="text/javascript">
$.ajaxSetup({
	complete:function(XMLHttpRequest,textStatus){
    	if(textStatus=="parsererror"){
    		$.messager.alert('提示信息', "会话超时！请重新登陆！", 'info',function(){
    			window.top.location = '${pageContext.request.contextPath}';
    		});
    	}else if(textStatus=="error"){
    		$.messager.alert('提示信息', "会话超时！请稍后再试！", 'info',function(){
    			window.top.location = '${pageContext.request.contextPath}';
    		});
    	}
	}
});
</script>
