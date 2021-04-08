<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>干扰专项分析</title>

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
	var weakCoverClickNum = 0;
	var disturbClickNum = 0;
	var nbCellClickNum = 0;
	var paramErrorClickNum = 0;
	var coreNetClickNum = 0;
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
	if('干扰专项分析'==title){
		url = '${pageContext.request.contextPath}/qualityBadRoad5g/goToDisturb.action';
		if(0==disturbClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		disturbClickNum++;
	}
	 
}
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>

  </head>
  <body>
    <div class="easyui-tabs" id="tt" data-options="fit:true,plain:true,tabWidth:100,border:false,onSelect:reloadThisPageTab" >
		<div data-options="closeable:'false',title:'干扰专项分析',cache:false,selected:true" style="padding:4px;">
		</div>
		<!-- <div data-options="closeable:'false',title:'弱覆盖',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'干扰',cache:false" style="padding:4px;">
		</div> -->
    </div>
	
  </body>
</html>
