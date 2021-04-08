<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>eMBB专题----总体分析韦恩图</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
	<div id="timeLongChart" style="width:100%;height:100%;"></div>
  </body>
</html>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highchart/highcharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/highchart/venn.js"></script>
<script type="text/javascript">
	Highcharts.chart('timeLongChart', {
		tooltip: {
			shared: true,
			formatter: function () {
				console.log(this.point);
				return this.point.name +
					':<br/><b>' + this.point.value + 'min,'+(this.point.value*100/(${timeMap.weakCoverTime}+${timeMap.overCoverTime}+${timeMap.overlappingCoverTime})).toFixed(2)+'%</b>';
			}
		},
		series: [{
			type: 'venn',
			name: '',
			data: [{
				sets: ['弱覆盖'],
				value: ${timeMap.weakCoverTime}
			}, {
				sets: ['过覆盖'],
				value: ${timeMap.overCoverTime}
			}, {
				sets: ['重叠覆盖'],
				value: ${timeMap.overlappingCoverTime}
			}, {
				sets: ['弱覆盖', '过覆盖'],
				value: ${timeMap.weakAndOverCoverTime},
				name: '弱覆盖&过覆盖'
			}, {
				sets: ['过覆盖', '重叠覆盖'],
				value: ${timeMap.overAndOverlappingCoverTime},
				name: '过覆盖&重叠覆盖'
			}, {
				sets: ['弱覆盖', '重叠覆盖'],
				value: ${timeMap.weakAndOverlappingCoverTime},
				name: '弱覆盖&重叠覆盖'
			}, {
				sets: ['弱覆盖', '过覆盖', '重叠覆盖'],
				value: ${timeMap.weakAndOverAndOverlappingCoverTime},
				name: '弱覆盖&过覆盖&重叠覆盖'
			}]
		}],
		title: {
			text: null
		}
	});
</script>
