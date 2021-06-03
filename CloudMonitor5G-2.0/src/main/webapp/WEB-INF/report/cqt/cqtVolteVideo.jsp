<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Volte视频业务</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@ include file="../../../taglibs/jquery.jsp"%>
<%@ include file="../../../taglibs/easyui.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/common.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/gisCommon.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/geocode.js"></script>
<script type="text/javascript">
$(function(){
	$('#tt').tabs({
		tools:[{
			text:'导出当前页',
			iconCls:'icon-excel',
			handler:function(){
				goToPage("${pageContext.request.contextPath}/cqtReport/downloadOneSheetVolteVideoTotalExcel.action?sheetName="+sheetName+"");
			}
		},{
			iconCls:'icon-batchexcel',
			text:'批量导出',
			handler:function(){
				goToPage('${pageContext.request.contextPath}/cqtReport/downloadVolteVideoTotalExcel.action');
			}
		}]
	});
	initTable();
});
//获取要隐藏的列名
function getField(i){
	if(i==1){
			return "country";
		}else if(i==2){
			return "province";
		}else if(i==3){
			return "testLogTerminalGroup";
		}else if(i==4){
			return "boxId";
		}else if(i==5){
			return "testPlay";
		}else if(i==6){
			return "testLogFileName";
		}else if(i==7){
			return "floorName";
		}else if(i==8){
			return "keypointno";
		}else if(i==9){
			return "floorno";
		}
	}
var sheetName="KPI统计";
var collectTypes="3,6";
function initTable(){
	$.post("${pageContext.request.contextPath}/cqtReport/quaryVideoKpi.action",{},
		function(result){
			if (result.errorMsg) {
				$.messager.alert("系统提示", result.errorMsg,'error');
			} else {
				collectTypes=result.collectTypes;
				if(collectTypes){
					for(var i=1;i<10;i++){
						var s=collectTypes.indexOf(i);
						if(s==-1){
							var valu=getField(i);
							$('#Table1').datagrid('hideColumn',valu);
							$('#Table2').datagrid('hideColumn',valu);
							$('#Table3').datagrid('hideColumn',valu);
						}
					}
			}
				$("#Table1").datagrid('loadData',result.info.statisticeKpi);
				$("#Table2").datagrid('loadData',result.info.qualityKpi);
				$("#Table3").datagrid('loadData',result.info.resourceKpi);
				$('#beg1').textbox('setValue', result.startDate);
				$('#end1').textbox('setValue', result.endDate);
				$("#beg1").textbox('readonly',true);//设置只读
				$("#end1").textbox('readonly',true);//设置只读
				$('#beg2').textbox('setValue', result.startDate);
				$('#end2').textbox('setValue', result.endDate);
				$("#beg2").textbox('readonly',true);//设置只读
				$("#end2").textbox('readonly',true);//设置只读
				$('#beg3').textbox('setValue', result.startDate);
				$('#end3').textbox('setValue', result.endDate);
				$("#beg3").textbox('readonly',true);//设置只读
				$("#end3").textbox('readonly',true);//设置只读
			}
		}
	,"json");
}
//获取Sheet名
function sele(title,index){
	sheetName=title;
}
</script>
</head>
<body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
		<div id="tt"class="easyui-tabs" style="width:100%;height:100%;"data-options="fit:true,onSelect:sele">
			<div title="KPI统计" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg1"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end1" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table1" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
   						<th rowspan="2" data-options="field:'country',width:80,align:'center',formatter:showTooltip">国名</th>
	    				<th rowspan="2" data-options="field:'province',width:80,align:'center',formatter:showTooltip">省名</th>
	    				<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    				<th rowspan="2" data-options="field:'boxId',width:80,align:'center',formatter:showTooltip">BOXID</th>
	    				<th rowspan="2" data-options="field:'testPlay',width:80,align:'center',formatter:showTooltip">测试计划</th>
	    				<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    				<th rowspan="2" data-options="field:'floorName',width:80,align:'center',formatter:showTooltip">楼宇</th>
	    				<th rowspan="2" data-options="field:'keypointno',width:80,align:'center',formatter:showTooltip">场景</th>
	    				<th rowspan="2" data-options="field:'floorno',width:80,align:'center',formatter:showTooltip">楼层</th>
   						<th colspan="17" >KPI统计</th>
   						<th colspan="3" >语音指标</th>
   						<th colspan="3" >视频指标</th>
   						<th colspan="1" ></th>
						</tr>
						<tr>
						 <th data-options="field:'videoattemptmocount',width:110,align:'center',formatter:showTooltip">视频主叫请求次数</th>
						 <th data-options="field:'videoconnectmocount',width:110,align:'center',formatter:showTooltip">视频主叫建立次数</th>
						 <th data-options="field:'videodropmocount',width:110,align:'center',formatter:showTooltip">视频主叫掉话次数</th>
						 <th data-options="field:'videoconnectsuccessratio',width:130,align:'center',formatter:showTooltip">视频主叫建立成功率</th>
						 <th data-options="field:'videoconnectdelay',width:150,align:'center',formatter:showTooltip">视频主叫建立时延(ms)</th>
						 <th data-options="field:'videoattemptmtcount',width:110,align:'center',formatter:showTooltip">视频被叫请求次数</th>
						 <th data-options="field:'videoconnectmtcount',width:110,align:'center',formatter:showTooltip">视频被叫建立次数</th>
						 <th data-options="field:'videodropmtcount',width:110,align:'center',formatter:showTooltip">视频被叫掉话次数</th>
						 <th data-options="field:'videodropratio',width:110,align:'center',formatter:showTooltip">视频掉线率</th>
						 <th data-options="field:'ltehoattemptcount',width:160,align:'center',formatter:showTooltip">LTE业务网内切换</br>请求次数</th>
						 <th data-options="field:'ltehosuccesscount',width:160,align:'center',formatter:showTooltip">LTE业务网内切换</br>成功次数</th>
						 <th data-options="field:'ltehosuccessratio',width:110,align:'center',formatter:showTooltip">LTE业务网内切换</br>成功率</th>
						 <th data-options="field:'ltehodelay',width:180,align:'center',formatter:showTooltip">LTE系统内切换中断</br>时延-控制面(ms)</th>
						 <th data-options="field:'ltehouserpaneldelay',width:180,align:'center',formatter:showTooltip">LTE系统内切换中断</br>时延-用户面(ms)</th>
						 <th data-options="field:'audiomosaverage',width:130,align:'center',formatter:showTooltip">语音MOS平均值</th>
						 <th data-options="field:'audiomosover30rate',width:160,align:'center',formatter:showTooltip">语音MOS>=3占比</th>
						 <th data-options="field:'mosover35rate',width:160,align:'center',formatter:showTooltip">语音MOS>=3.5占比</th>
						 <th data-options="field:'audiortpjitter',width:110,align:'center',formatter:showTooltip">语音抖动率(ms)</th>
						 <th data-options="field:'audiortpbler',width:110,align:'center',formatter:showTooltip">语音丢包率</th>
						 <th data-options="field:'audiortpblerlowsinr',width:110,align:'center',formatter:showTooltip">语音质差丢包占比</th>
						 <th data-options="field:'videortpjitter',width:110,align:'center',formatter:showTooltip">视频抖动率(ms)</th>
						 <th data-options="field:'videortpbler',width:110,align:'center',formatter:showTooltip">视频丢包率</th>
						 <th data-options="field:'videortpblerlowsinr',width:110,align:'center',formatter:showTooltip">视频质差丢包占比</th>
						 <th data-options="field:'videoconnectmocount',width:110,align:'center',formatter:showTooltip">视频主叫建立次数</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					  <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
					</tr>
					</tbody>
				</table>
			</div>
			<div title="质量类" style="width:100%;height:100%;">
			<a>开始时间：<input id="beg2"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end2" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table2" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
   						<th rowspan="2" data-options="field:'country',width:80,align:'center',formatter:showTooltip">国名</th>
	    				<th rowspan="2" data-options="field:'province',width:80,align:'center',formatter:showTooltip">省名</th>
	    				<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    				<th rowspan="2" data-options="field:'boxId',width:80,align:'center',formatter:showTooltip">BOXID</th>
	    				<th rowspan="2" data-options="field:'testPlay',width:80,align:'center',formatter:showTooltip">测试计划</th>
	    				<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    				<th rowspan="2" data-options="field:'floorName',width:80,align:'center',formatter:showTooltip">楼宇</th>
	    				<th rowspan="2" data-options="field:'keypointno',width:80,align:'center',formatter:showTooltip">场景</th>
	    				<th rowspan="2" data-options="field:'floorno',width:80,align:'center',formatter:showTooltip">楼层</th>
   						<th colspan="4" >覆盖指标</th>
   						<th colspan="5" >干扰指标</th>
   						<th colspan="4" >速率指标</th>
						</tr>
						<tr>
						 <th data-options="field:'videotesttimetotal',width:110,align:'center',formatter:showTooltip">测试总时长（s）</th>
						 <th data-options="field:'rsrpweakdistancerate',width:110,align:'center',formatter:showTooltip">弱覆盖里程占比（＜-110）</th>
						 <th data-options="field:'videotestdistance',width:110,align:'center',formatter:showTooltip">视频电话测试总里程（km）</th>
						 <th data-options="field:'rsrpaverage',width:130,align:'center',formatter:showTooltip">平均RSRP</th>
						 <th data-options="field:'sinraverage',width:150,align:'center',formatter:showTooltip">平均SINR</th>
						 <th data-options="field:'sinrsign3rate',width:110,align:'center',formatter:showTooltip">SINR 3以上占比</th>
						 <th data-options="field:'sinrweaksign3distrate',width:110,align:'center',formatter:showTooltip">质差里程占比</th>
						 <th data-options="field:'dlbleraverage',width:110,align:'center',formatter:showTooltip">下行BLER</th>
						 <th data-options="field:'ulbleraverage',width:110,align:'center',formatter:showTooltip">上行BLER</th>
						 <th data-options="field:'audiopdcpulthr',width:160,align:'center',formatter:showTooltip">语音上行PDCP层平均速率(kbps)</th>
						 <th data-options="field:'audiopdcpdlthr',width:160,align:'center',formatter:showTooltip">语音下行PDCP层平均速率(kbps)</th>
						 <th data-options="field:'videopdcpulthr',width:110,align:'center',formatter:showTooltip">视频上行PDCP层平均速率(kbps)</th>
						 <th data-options="field:'videopdcpdlthr',width:180,align:'center',formatter:showTooltip">视频下行PDCP层平均速率(kbps)</th>
					</thead>
					<tbody>
					<tr>
					  <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
					</tr>
					</tbody>
				</table>

			</div>
			<div title="资源类" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg3"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end3" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table3" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
   						<th rowspan="2" data-options="field:'country',width:80,align:'center',formatter:showTooltip">国名</th>
	    				<th rowspan="2" data-options="field:'province',width:80,align:'center',formatter:showTooltip">省名</th>
	    				<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    				<th rowspan="2" data-options="field:'boxId',width:80,align:'center',formatter:showTooltip">BOXID</th>
	    				<th rowspan="2" data-options="field:'testPlay',width:80,align:'center',formatter:showTooltip">测试计划</th>
	    				<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    				<th rowspan="2" data-options="field:'floorName',width:80,align:'center',formatter:showTooltip">楼宇</th>
	    				<th rowspan="2" data-options="field:'keypointno',width:80,align:'center',formatter:showTooltip">场景</th>
	    				<th rowspan="2" data-options="field:'floorno',width:80,align:'center',formatter:showTooltip">楼层</th>
   						<th rowspan="2" data-options="field:'audioprbfrequl',width:100,align:'center'" >语音上行占用PRB平均数（个/秒）</th>
   						<th rowspan="2" data-options="field:'audioprbfreqdl',width:100,align:'center'"  >语音下行占用PRB平均数（个/秒）</th>
   						<th colspan="2" >语音下行平均MCS</th>
   						<th rowspan="2" data-options="field:'audioulmcs',width:100,align:'center'" >语音上行平均MCS</th>
   						<th rowspan="2" data-options="field:'videoprbfrequl',width:100,align:'center'" >视频上行占用PRB<br>平均数（个/秒）</th>
   						<th rowspan="2" data-options="field:'videoprbfreqdl',width:100,align:'center'" >视频上行占用PRB<br>平均数（个/秒）</th>
                        <th colspan="2" >视频下行平均MCS</th>
   						<th rowspan="2" data-options="field:'videoulmcs',width:100,align:'center'"  >视频上行平均MCS</th>
						</tr>
						<tr>
						 <th data-options="field:'audiocodeword0',width:100,align:'center',formatter:showTooltip">码字0</th>
						 <th data-options="field:'audiocodeword1',width:100,align:'center',formatter:showTooltip">码字1</th>
						 <th data-options="field:'videocodeword0',width:100,align:'center',formatter:showTooltip">码字0</th>
						 <th data-options="field:'videocodeword1',width:100,align:'center',formatter:showTooltip">码字1</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>

			</div>
		</div>
	</div>
</body>
</html>
