<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>统计分析-5G对比分析</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<script type="text/javascript">
	var wholeClickNum = 0;
	var gridClickNum = 0;
/**
 * 当选中tab的时候重新加载该tab中的内容
 * @param title
 * @param index
 */
function reloadThisPageTab(title,index){
	var target = this;
	var currTab =  $(target).tabs('getSelected'); 
	var url;
	if('整体概览'==title){
		url = '${pageContext.request.contextPath}/compareAnalysis5g/goToWhole.action';
		if(0==wholeClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		wholeClickNum++;
	} else if('栅格对比'==title){
		url = '${pageContext.request.contextPath}/compareAnalysis5g/goToLatticeCell.action';
		if(0==gridClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		gridClickNum++;
	} 
}
	var testLogItemIds = '${testLogItemIds}';
	function goToCompareTestLogItemListPage(){
		if(testLogItemIds){
			goToPage('${pageContext.request.contextPath}/compareAnalysis5g/goToCompareTestLogItem5gListUI.action');
		}else{
			$.messager.show({
				title:'系统提示',
				msg:'原始日志未选择,请选择一批原始日志!'
			});
		}
	}
	$(function(){
		if(!testLogItemIds){
			$.messager.show({
				title:'系统提示',
				msg:'原始日志未选择,请选择一批原始日志!'
			});
		}
	});
	
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>

  </head>
  <body>
    <div class="easyui-tabs" id="tt" data-options="fit:true,plain:true,tabWidth:120,border:false,onSelect:reloadThisPageTab,tools:'#tab-tools'" >
		<div data-options="closeable:'false',title:'整体概览',cache:false,selected:true" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'栅格对比',cache:false" style="padding:4px;">
		</div>
    </div>
	<div id="tab-tools">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="goToCompareTestLogItemListPage();">对比日志选择</a>
	</div>
  </body>
</html>
