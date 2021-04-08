<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>VoLTE质量专题----视频质差分析</title>

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
	var pingPongClickNum=0;
	var neighbourPlotClickNum=0;
	var weakCoverClickNum = 0;
	var disturbClickNum = 0;
	var overCoverClickNum = 0;
	var patternSwitchClickNum = 0;
	var downDispatchSmallClickNum = 0;
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
		url = '${pageContext.request.contextPath}/videoQuality/goToWhole.action';
		if(0==wholeClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		wholeClickNum++;
	}else if('乒乓切换'==title){
		url = '${pageContext.request.contextPath}/videoQuality/goToPingPong.action';
		if(0==pingPongClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		//调试用暂时放开
		pingPongClickNum++;
	}else if('邻区'==title){
		url = '${pageContext.request.contextPath}/videoQuality/goToNeighbourPlot.action';
		if(0==neighbourPlotClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		//调试用暂时放开
		neighbourPlotClickNum++;
	}else if('弱覆盖'==title){
		url = '${pageContext.request.contextPath}/videoQuality/goToWeakCover.action';
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
		url = '${pageContext.request.contextPath}/videoQuality/goToDisturb.action';
		if(0==disturbClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		disturbClickNum++;
	} else if('重叠覆盖'==title){
		url = '${pageContext.request.contextPath}/videoQuality/goToOverCover.action';
		if(0==overCoverClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		overCoverClickNum++;
	}else if('模式转换'==title){
		url = '${pageContext.request.contextPath}/videoQuality/goToPatternSwitch.action';
		if(0==patternSwitchClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		patternSwitchClickNum++;
	}else if('下行调度数小'==title){
		url = '${pageContext.request.contextPath}/videoQuality/goToDownDispatchSmall.action';
		if(0==downDispatchSmallClickNum){
			$(target).tabs('update', {
				tab : currTab,
				options : {
					content : createIframe(url)
				}
			});
		}
		downDispatchSmallClickNum++;
	}  else if('其他原因'==title){
		url = '${pageContext.request.contextPath}/videoQuality/goToOther.action';
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
		<div data-options="closeable:'false',title:'乒乓切换',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'邻区',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'弱覆盖',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'干扰',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'重叠覆盖',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'模式转换',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'下行调度数小',cache:false" style="padding:4px;">
		</div>
		<div data-options="closeable:'false',title:'其他原因',cache:false" style="padding:4px;">
		</div>
    </div>
	
  </body>
</html>
