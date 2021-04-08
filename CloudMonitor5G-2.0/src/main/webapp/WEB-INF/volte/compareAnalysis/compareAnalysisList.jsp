<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE专题----VoLTE对比分析</title>

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
	var mosBadClickNum = 0;
	var exceptionEventClickNum = 0;
	var srvccHOClickNum = 0;
	var systemHOClickNum = 0;
	var gridClickNum = 0;
	var otherClickNum = 0;
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
		url = '${pageContext.request.contextPath}/voiceCompare/goToWhole.action';
		if(0==wholeClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		wholeClickNum++;
	}else if('MOS差黑点'==title){
		url = '${pageContext.request.contextPath}/voiceCompare/goToMosQualityBad.action';
		if(0==mosBadClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		mosBadClickNum++;
	} else if('异常事件'==title){
		url = '${pageContext.request.contextPath}/voiceCompare/goToExceptionEvent.action';
		if(0==exceptionEventClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		exceptionEventClickNum++;
	} else if('SRVCC切换失败'==title){
		url = '${pageContext.request.contextPath}/voiceCompare/goToSrvccHo.action';
		if(0==srvccHOClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		srvccHOClickNum++;
	} else if('系统内切换失败'==title){
		url = '${pageContext.request.contextPath}/voiceCompare/goToSystemHo.action';
		if(0==systemHOClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		systemHOClickNum++;
	} else if('栅格对比'==title){
		url = '${pageContext.request.contextPath}/voiceCompare/goToLatticeCell.action';
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
			goToPage('${pageContext.request.contextPath}/voiceCompare/goToCompareTestLogItemListUI.action');
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
		<div data-options="closeable:'false',title:'MOS差黑点',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'异常事件',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'SRVCC切换失败',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'系统内切换失败',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'栅格对比',cache:false" style="padding:4px;">
		</div>
    </div>
	<div id="tab-tools">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="goToCompareTestLogItemListPage();">对比日志选择</a>
	</div>
  </body>
</html>
