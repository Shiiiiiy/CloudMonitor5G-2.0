<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE质量专题----VoLTE语音质差</title>

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
	if('整体'==title){
		url = '${pageContext.request.contextPath}/voiceQBR/goToWhole.action';
		if(0==wholeClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		wholeClickNum++;
	}else if('弱覆盖'==title){
		url = '${pageContext.request.contextPath}/voiceQBR/goToWeakCover.action';
		if(0==weakCoverClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		//调试用暂时放开
		weakCoverClickNum++;
	} else if('干扰'==title){
		url = '${pageContext.request.contextPath}/voiceQBR/goToDisturb.action';
		if(0==disturbClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		disturbClickNum++;
	} else if('邻区配置'==title){
		url = '${pageContext.request.contextPath}/voiceQBR/goToNbCell.action';
		if(0==nbCellClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		nbCellClickNum++;
	} else if('参数配置'==title){
		url = '${pageContext.request.contextPath}/voiceQBR/goToParamError.action';
		if(0==paramErrorClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		paramErrorClickNum++;
	} else if('核心网问题'==title){
		url = '${pageContext.request.contextPath}/voiceQBR/goToCoreNet.action';
		if(0==coreNetClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		coreNetClickNum++;
	} else if('其他原因'==title){
		url = '${pageContext.request.contextPath}/voiceQBR/goToOther.action';
		if(0==otherClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		otherClickNum++;
	}
	 
}
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>

  </head>
  <body>
    <div class="easyui-tabs" id="tt" data-options="fit:true,plain:true,tabWidth:100,border:false,onSelect:reloadThisPageTab" >
		<div data-options="closeable:'false',title:'整体',cache:false,selected:true" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'弱覆盖',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'干扰',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'邻区配置',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'参数配置',cache:false" style="padding:4px;">
		</div>
		<!-- 2016-02-24需求讨论去除核心网问题tab -->
		<!-- <div data-options="closeable:'false',title:'核心网问题',cache:false" style="padding:4px;">
		</div> -->
		<div data-options="closeable:'false',title:'其他原因',cache:false" style="padding:4px;">
		</div>
    </div>
	
  </body>
</html>
