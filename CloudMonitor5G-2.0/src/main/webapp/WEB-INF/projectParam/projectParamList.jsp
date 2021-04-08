<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>工程参数</title>

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
	$(function(){
		/* $("#query").bind('mouseenter',function(){
			alert("111");	
		}); */
		//$("#query").tooltip('show');
	
	});
	var ChinaMobileClickNum = 0;
	var ChinaUnicomClickNum = 0;
	var ChinaTelecomClickNum = 0;
	var PlanParamManage = 0;
/**
 * 当选中tab的时候重新加载该tab中的内容
 * @param title
 * @param index
 */
function reloadThisPageTab(title,index){
	var target = this;
	var currTab =  $(target).tabs('getSelected'); 
	var url;
	if('移动'==title){
		url = '${pageContext.request.contextPath}/projectParam/projectParamInfoUI.action?infoType=${ChinaMobile}';
		if(0==ChinaMobileClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			}); 
		}
		ChinaMobileClickNum++;
	}else if('联通'==title){
		url = '${pageContext.request.contextPath}/projectParam/projectParamInfoUI.action?infoType=${ChinaUnicom}';
		if(0==ChinaUnicomClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			}); 
		}
		ChinaUnicomClickNum++;
	} else if('电信'==title){
		url = '${pageContext.request.contextPath}/projectParam/projectParamInfoUI.action?infoType=${ChinaTelecom}';
		if(0==ChinaTelecomClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			}); 
		}
		ChinaTelecomClickNum++;
	} else if('规划工参管理'==title){
		url = '${pageContext.request.contextPath}/projectParam/PlanParamManageUI.action';
		if(0==PlanParamManage){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			}); 
		}
		PlanParamManage++;
	}
} 
	</script>
	

  </head>
  <body >
    <div class="easyui-tabs" id="tt" data-options="fit:true,plain:true,tabWidth:100,border:false,onSelect:reloadThisPageTab" >
		<div data-options="closeable:'false',title:'移动',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'联通',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'电信',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'规划工参管理',cache:false" style="padding:4px;">
		</div>
    </div>
	
  </body>
</html>
