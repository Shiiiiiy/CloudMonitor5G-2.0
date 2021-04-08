<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>专题分析----eMBB覆盖专题</title>

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
	var overCoverClickNum = 0;
	var overlappingCoverClickNum = 0;
/**
 * 当选中tab的时候重新加载该tab中的内容
 * @param title
 * @param index
 */
function reloadThisPageTab(title,index){
	var target = this;
	var currTab =  $(target).tabs('getSelected'); 
	var url;
	if('总体分析'==title){
		url = '${pageContext.request.contextPath}/embbCover5g/goToWhole.action';
		if(0==wholeClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		wholeClickNum++;
	}else if('弱覆盖专项分析'==title){
		url = '${pageContext.request.contextPath}/embbCover5g/goToWeakCover.action';
		if(0==weakCoverClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		weakCoverClickNum++;
	} else if('过覆盖专项分析'==title){
		url = '${pageContext.request.contextPath}/embbCover5g/goToOverCover.action';
		if(0==overCoverClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		overCoverClickNum++;
	} else if('重叠覆盖专项分析'==title){
		url = '${pageContext.request.contextPath}/embbCover5g/goToOverlappingCover.action';
		if(0==overlappingCoverClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		overlappingCoverClickNum++;
	} 
	 
}
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>

  </head>
  <body>
    <div class="easyui-tabs" id="tt" data-options="fit:true,plain:true,border:false,onSelect:reloadThisPageTab" >
		<div data-options="closeable:'false',title:'总体分析',cache:false,selected:true" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'弱覆盖专项分析',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'过覆盖专项分析',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'重叠覆盖专项分析',cache:false" style="padding:4px;">
		</div>
		
    </div>
	
  </body>
</html>
