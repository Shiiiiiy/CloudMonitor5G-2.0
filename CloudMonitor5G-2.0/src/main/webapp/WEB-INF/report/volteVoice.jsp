<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Volte语音业务</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../taglibs/jquery.jsp"%>
	<%@ include file="../../taglibs/easyui.jsp"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/geocode.js" ></script>
	<script type="text/javascript">
	$(function(){
		initTable();
		$('#tt').tabs({
			tools:[{
				text:'导出当前页',
				iconCls:'icon-excel',
				handler:function(){
					goToPage("${pageContext.request.contextPath}/report/downloadOneSheetVolteVoiceTotalExcel.action?sheetName="+sheetName+"");
				}
			},{
				iconCls:'icon-batchexcel',
				text:'批量导出',
				handler:function(){
					goToPage('${pageContext.request.contextPath}/report/downloadVolteVoiceTotalExcel.action');
				}
			}]
		});
	});
	var sheetName="KPI汇总";
	//加载数据
	function initTable(){
		$.post("${pageContext.request.contextPath}/report/quaryVoiceKpi.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#Table1").datagrid('loadData',result.totalKpi);
					$("#Table2").datagrid('loadData',result.volteKpi);
					$("#Table3").datagrid('loadData',result.csKpi);
					$("#Table4").datagrid('loadData',result.coverKpi);
					$("#Table5").datagrid('loadData',result.disturbKpi);
					$("#Table6").datagrid('loadData',result.dispatcherKpi);
					$("#Table7").datagrid('loadData',result.mosKpi);
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
					$('#beg4').textbox('setValue', result.startDate);
					$('#end4').textbox('setValue', result.endDate);
					$("#beg4").textbox('readonly',true);//设置只读
					$("#end4").textbox('readonly',true);//设置只读
					$('#beg5').textbox('setValue', result.startDate);
					$('#end5').textbox('setValue', result.endDate);
					$("#beg5").textbox('readonly',true);//设置只读
					$("#end5").textbox('readonly',true);//设置只读
					$('#beg6').textbox('setValue', result.startDate);
					$('#end6').textbox('setValue', result.endDate);
					$("#beg6").textbox('readonly',true);//设置只读
					$("#end6").textbox('readonly',true);//设置只读
					$('#beg7').textbox('setValue', result.startDate);
					$('#end7').textbox('setValue', result.endDate);
					$("#beg7").textbox('readonly',true);//设置只读
					$("#end7").textbox('readonly',true);//设置只读
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
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;" >
    	<div id="tt" class="easyui-tabs" style="width:100%;height:100%;" data-options="fit:true,onSelect:sele">
			<div title="KPI汇总" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg1"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end1" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table1" class="easyui-datagrid" data-options="striped:true,fitColumns:false,singleSelect:true"   >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="3" >语音业务总KPI</th>
	    						<th colspan="5" >VOLTE语音KPI</th>
	    						<th colspan="4" >CS域语音KPI</th>
	    						<th colspan="4" >各类呼叫占比</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'callcompleterate',width:80,align:'center',formatter:showTooltip">全程呼叫成功率</th>
	    						<th data-options="field:'callconnectrate',width:80,align:'center',formatter:showTooltip">接通率</th>
	    						<th data-options="field:'calldroprate',width:80,align:'center',formatter:showTooltip">掉话率</th>
	    						<th data-options="field:'voltecallcompleterate',width:80,align:'center'">VOLTE全程呼叫成功率</th>
	    						<th data-options="field:'voltecallconnectrate',width:80,align:'center'">VOLTE接通率</th>
	    						<th data-options="field:'voltecalldroprate',width:70,align:'center'">VOLTE掉话率</th>
	    						<th data-options="field:'voltecallestablishdelay',width:100,align:'center'">呼叫建立时延(s)</th>
	    						<th data-options="field:'voltertpdiscrate',width:120,align:'center'">丢包率</th>
	    						<th data-options="field:'cscallcompleterate',width:120,align:'center'">CS域全程呼叫成功率</th>
	    						<th data-options="field:'cscallconnectrate',width:120,align:'center'">CS域接通率</th>
	    						<th data-options="field:'cscalldroprate',width:120,align:'center'">CS域掉话率</th>
	    						<th data-options="field:'cscallestablishdelay',width:120,align:'center'">呼叫建立时延(s)</th>
	    						<th data-options="field:'voltecallrate',width:120,align:'center'">VOLTE呼叫占比</th>
	    						<th data-options="field:'csfbcallrate',width:120,align:'center'">CSFB呼叫占比</th>
	    						<th data-options="field:'gsmcallrate',width:120,align:'center'">GSM呼叫占比</th>
	    						<th data-options="field:'tdscallrate',width:120,align:'center'">TD呼叫占比</th>
	    					</tr>
	    				</thead>
	    			</table>
			</div>
			<div title="VOLTE统计指标" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg2"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end2" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table2"  class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th rowspan="2" data-options="field:'moormt',width:80,align:'center'">主被叫</th>
	    						<th rowspan="2" data-options="field:'imsregistersuccessrate',width:80,align:'center'">IMS注册成功率</th>
	    						<th rowspan="2" data-options="field:'voltecallestablishsuccessrate',width:80,align:'center'">VOLTE语音建立成功率</th>
	    						<th rowspan="2" data-options="field:'voltecalldroprate',width:80,align:'center'">VOLTE掉话率</th>
	    						<th rowspan="2" data-options="field:'voltecallholdtimerate',width:80,align:'center'">VOLTE通话时长占比</th>
	    						<th rowspan="2" data-options="field:'voltecallattemptmocount',width:80,align:'center'">VOLTE试呼次数</th>
	    						<th rowspan="2" data-options="field:'voltecallconnectmocount',width:80,align:'center'">VOLTE接通次数</th>
	    						<th rowspan="2" data-options="field:'voltecalldropcount',width:80,align:'center'">VOLTE掉话次数</th>
	    						<th rowspan="2" data-options="field:'esrvcchocount',width:80,align:'center'">呼叫eSRVCC切换次数</th>
	    						<th rowspan="2" data-options="field:'ulspsenabletimerate',width:80,align:'center'">上行SPS开启时长占比</th>
	    						<th rowspan="2" data-options="field:'dlspsenabletimerate',width:80,align:'center'">下行SPS开启时长占比</th>
	    						<th rowspan="2" data-options="field:'voltemosaverage',width:80,align:'center'">VOLTE平均MOS</th>
	    						<th rowspan="2" data-options="field:'amrnbtimerate',width:80,align:'center'">AMR_NB时长占比</th>
	    						<th rowspan="2" data-options="field:'amrwbtimerate',width:80,align:'center'">AMR_WB时长占比</th>
	    						<th colspan="2" >码字0BLER</th>
	    						<th colspan="2" >码字1BLER</th>
	    						<th rowspan="2" data-options="field:'rtpdiscrate',width:80,align:'center'">RTP丢包率</th>
	    						<th rowspan="2" data-options="field:'rtpjitterdelay',width:80,align:'center'">RTP抖动(ms)</th>
	    						<th rowspan="2" data-options="field:'rtpe2edelay',width:80,align:'center'">RTP端到端时延(s)</th>
	    						<th rowspan="2" data-options="field:'callestablishdelay',width:80,align:'center'">呼叫建立时延(s)</th>
	    						<th rowspan="2" data-options="field:'imsregisterdelay',width:80,align:'center'">IMS注册时延(s)</th>
	    						<th rowspan="2" data-options="field:'ulpdcpthroughput',width:80,align:'center'">上行速率</th>
	    						<th rowspan="2" data-options="field:'dlpdcpthroughput',width:80,align:'center'">下行速率</th>
	    						<th colspan="6" >切换中断时延</th>
	    						<th colspan="3" >切换成功率</th>
	    					</tr>
	    					<tr>
	    						<th data-options= "field:'code0initialbler',width:80,align:'center'">初始BLER</th>
	    						<th data-options="field:'code0residualbler',width:80,align:'center'">残留BLER</th>
	    						<th data-options="field:'code1initialbler',width:80,align:'center'">初始BLER</th>
	    						<th data-options="field:'code1residualbler',width:80,align:'center'">残留BLER</th>
	    						<th data-options="field:'ltehoctrlpaneldelay',width:70,align:'center'">LTE HO控制面</th>
	    						<th data-options="field:'ltehouserpaneldelay',width:100,align:'center'">LTE HO用户面</th>
	    						<th data-options="field:'lteesrvccctrlpaneldelay',width:120,align:'center'">eSRVCC控制面</th>
	    						<th data-options="field:'lteesrvccuserpaneldelay',width:120,align:'center'">eSRVCC用户面</th>
	    						<th data-options="field:'ltereestabctrlpaneldelay',width:120,align:'center'">RLF/RE-ESTABLISH控制面</th>
	    						<th data-options="field:'ltereestabuserpaneldelay',width:120,align:'center'">RLF/RE-ESTABLISH用户面</th>
	    						<th data-options="field:'ltehosuccessrate',width:120,align:'center'">LTE HO成功率</th>
	    						<th data-options="field:'esrvccsuccessrate',width:120,align:'center'">eSRVCC成功率</th>
	    						<th data-options="field:'ltereestabsuccessrate',width:120,align:'center'">RLF/RE-ESTABLISH成功率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>

			</div>
			<div title="CS域语音统计指标 " style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg3"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end3" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table3" class="easyui-datagrid"  data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th data-options="field:'moormt',width:80,align:'center'">主被叫</th>
	    						<th data-options="field:'cscallcompleterate',width:80,align:'center'">CS域全程成功率(%)</th>
	    						<th data-options="field:'cscallconnectrate',width:80,align:'center'">CS域接通率(%)</th>
	    						<th data-options="field:'cscalldroprate',width:80,align:'center'">CS域掉话率(%)</th>
	    						<th data-options="field:'cscallattemptcount',width:70,align:'center'">CS域试呼次数</th>
	    						<th data-options="field:'cscallconnectcount',width:100,align:'center'">CS域接通次数</th>
	    						<th data-options="field:'cscalldropcount',width:120,align:'center'">CS域掉话次数</th>
	    						<th data-options="field:'cscallestablishdelay',width:120,align:'center'">CS域平均呼叫建立时延(s)</th>
	    						<th data-options="field:'csfb2gsmsuccessrate',width:120,align:'center'">CSFB回落GSM成功比例(%)</th>
	    						<th data-options="field:'csfb2gsmdelay',width:120,align:'center'">CSFB回落GSM时延(s)</th>
	    						<th data-options="field:'csfb2tdssuccessrate',width:120,align:'center'">CSFB回落TD比例(%)</th>
	    						<th data-options="field:'csfb2tdsdelay',width:120,align:'center'">CSFB回落TD平均时延(s)</th>
	    						<th data-options="field:'gsmreturntdlsuccessrate',width:120,align:'center'">GSM成功返回TD-LTE比例(%)</th>
	    						<th data-options="field:'gsmreturntdldelay',width:120,align:'center'">GSM返回TD-LTE平均时延(s)</th>
	    						<th data-options="field:'tdsreturntdlrate',width:120,align:'center'">TD返回TD-LTE比例(%)</th>
	    						<th data-options="field:'tdsreturntdldelay',width:120,align:'center'">TD返回TD-LTE平均时延(s)</th>
	    						<th data-options="field:'rxquality0-4rate',width:120,align:'center'">Rxquality0-4级占比</th>
	    						<th data-options="field:'csmosaverage',width:120,align:'center'">CS域MOS均值</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>

			</div>
			<div title="覆盖类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg4"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end4" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table4" class="easyui-datagrid"  data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true" >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="19" >覆盖类</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'rsrpaverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'rsrpedge',width:80,align:'center'">边缘RSRP</th>
	    						<th data-options="field:'ltecoverage110rate',width:80,align:'center'">LTE覆盖率(RSRP>-110<br> and SINR>-3)</th>
	    						<th data-options="field:'ltecoverage103rate',width:80,align:'center'">LTE覆盖率(RSRP>-103<br> and SINR>-3)</th>
	    						<th data-options="field:'ltecoverage101rate',width:80,align:'center'">LTE覆盖率(RSRP>-101 <br>and SINR>-3)</th>
	    						<th data-options="field:'distancetotal',width:70,align:'center'">LTE测试总里程(km)</th>
	    						<th data-options="field:'outofservicedistance',width:100,align:'center'">脱网里程(km)</th>
	    						<th data-options="field:'tdlservicedistance',width:120,align:'center'">TDL驻网里程(km)</th>
	    						<th data-options="field:'tdsservicedistance',width:120,align:'center'">TDS驻网里程(km)</th>
	    						<th data-options="field:'gsmservicedistance',width:120,align:'center'">GSM驻网里程(km)</th>
	    						<th data-options="field:'testtimetotal',width:120,align:'center'">测试总时长(小时)</th>
	    						<th data-options="field:'outofservicetime',width:120,align:'center'">脱网时长(小时)</th>
	    						<th data-options="field:'tdlservicetime',width:120,align:'center'">TDL驻网时长(小时)</th>
	    						<th data-options="field:'tdsservicetime',width:120,align:'center'">TDS驻网时长(小时)</th>
	    						<th data-options="field:'gsmservicetime',width:120,align:'center'">GSM驻网时长(小时)</th>
	    						<th data-options="field:'speed',width:120,align:'center'">平均车速(km/h)</th>
	    						<th data-options="field:'rsrpweakdistancerate',width:120,align:'center'">RSRP连续弱覆盖里程占比</th>
	    						<th data-options="field:'rsrpnodistancerate',width:120,align:'center'">RSRP连续无覆盖里程占比</th>
	    						<th data-options="field:'uetxpoweroverdistrate',width:120,align:'center'">连续UE高发射功率里程占比</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>

			</div>
			<div title="干扰类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg5"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end5" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table5" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="13" >干扰类</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'sinraverage',width:80,align:'center'">平均SINR</th>
	    						<th data-options="field:'sinredge',width:80,align:'center'">边缘SINR</th>
	    						<th data-options="field:'sinrsign3rate',width:80,align:'center'">SINR -3以上占比</th>
	    						<th data-options="field:'sinr0rate',width:80,align:'center'">SINR 0以上占比</th>
	    						<th data-options="field:'rsrqaverage',width:80,align:'center'">平均RSRQ</th>
	    						<th data-options="field:'sinrweaksign1distrate',width:70,align:'center'">连续SINR质差里程占比<br>(SINR小于-1)(%)</th>
	    						<th data-options="field:'sinrweaksign3distrate',width:100,align:'center'">连续SINR质差里程占比<br>(SINR小于-3)(%)</th>
	    						<th data-options="field:'pdschbleraverage',width:120,align:'center'">PDSCH BLER平均值</th>
	    						<th data-options="field:'dlintialharqretxrate',width:120,align:'center'">下行初始HARQ重传比率</th>
	    						<th data-options="field:'overlapcover3rate',width:120,align:'center'">重叠覆盖率(重叠覆盖度<br>>=3)(%)</th>
	    						<th data-options="field:'overlapcover3distrate',width:120,align:'center'">重叠覆盖里程占比(重叠覆盖度<br>>=3)(%)</th>
	    						<th data-options="field:'overlapcover4rate',width:120,align:'center'">重叠覆盖率(重叠覆盖度<br>=4)(%)</th>
	    						<th data-options="field:'overlapcover4distrate',width:120,align:'center'">重叠覆盖里程占比(重叠覆盖度<br>>=4)(%)</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
			</div>
			<div title="调度类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg6"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end6" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table6"  class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="3" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th rowspan="3" data-options="field:'puschtxpower',width:80,align:'center'">PUSCH TxPower</th>
	    						<th colspan="5" >SPS开启时</th>
	    						<th colspan="5" >SPS关闭时</th>
	    						<th colspan="5" >汇总</th>
	    					</tr>
	    					<tr>
	    						<th rowspan="2"data-options="field:'scheduleopulrbaverage',width:80,align:'center'">上行平均RB</th>
	    						<th rowspan="2" data-options="field:'scheduleopendlrbaverage',width:80,align:'center'">下行平均RB</th>
	    						<th colspan="2" >下行平均MCS</th>
	    						<th rowspan="2"data-options="field:'scheduleopulmcsaverage',width:80,align:'center'">上行平均MCS</th>
	    						<th rowspan="2" data-options="field:'scheduleclulrbaverage',width:80,align:'center'">上行平均RB</th>
	    						<th rowspan="2"data-options="field:'schedulecldlrbaverage',width:80,align:'center'">下行平均RB</th>
	    						<th colspan="2" >下行平均MCS</th>
	    						<th rowspan="2"data-options="field:'scheduleclulmcsaverage',width:80,align:'center'">上行平均MCS</th>
	    						<th rowspan="2" data-options="field:'ulrbaverage',width:80,align:'center'">上行平均RB</th>
	    						<th rowspan="2"data-options="field:'dlrbaverage',width:80,align:'center'">下行平均RB</th>
	    						<th colspan="2" >下行平均MCS</th>
	    						<th rowspan="2"data-options="field:'ulmcsaverage',width:80,align:'center'">上行平均MCS</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'scheduleopcodeword0',width:80,align:'center'">码字0</th>
	    						<th data-options="field:'scheduleopcodeword1',width:80,align:'center'">码字1</th>
	    						<th data-options="field:'scheduleclcodeword0',width:70,align:'center'">码字0</th>
	    						<th data-options="field:'scheduleclcodeword1',width:100,align:'center'">码字1</th>
	    						<th data-options="field:'codeword0',width:120,align:'center'">码字0</th>
	    						<th data-options="field:'codeword1',width:120,align:'center'">码字1</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
			</div>
			<div title="MOS统计" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg7"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end7" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table7" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="3" >PS域MOS占比</th>
	    						<th colspan="3" >CS域MOS占比</th>
	    						<th colspan="26" >PS域MOS分段</th>
	    						<th colspan="26" >CS域MOS分段</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'psmosover28rate',width:80,align:'center'">MOS>=2.8占比</th>
	    						<th data-options="field:'psmosover30rate',width:80,align:'center'">MOS>=3.0占比</th>
	    						<th data-options="field:'psmosover358rate',width:80,align:'center'">MOS>=3.5占比</th>
	    						<th data-options="field:'csmosover28rate',width:80,align:'center'">MOS>=2.8占比</th>
	    						<th data-options="field:'csmosover30rate',width:80,align:'center'">MOS>=3.0占比</th>
	    						<th data-options="field:'csmosover35rate',width:70,align:'center'">MOS>=3.5占比</th>
	    						<th data-options="field:'psmos0t21',width:100,align:'center'">[0,2.1)</th>
	    						<th data-options="field:'psmos21t22',width:120,align:'center'">[2.1,2.2)</th>
	    						<th data-options="field:'psmos22t23',width:120,align:'center'">[2.2,2.3)</th>
	    						<th data-options="field:'psmos23t24',width:120,align:'center'">[2.3,2.4)</th>
	    						<th data-options="field:'psmos24t25',width:120,align:'center'">[2.4,2.5)</th>
	    						<th data-options="field:'psmos25t26',width:120,align:'center'">[2.5,2.6)</th>
	    						<th data-options="field:'psmos26t27',width:120,align:'center'">[2.6,2.7)</th>
	    						<th data-options="field:'psmos27t28',width:120,align:'center'">[2.7,2.8)</th>
	    						<th data-options="field:'psmos28t29',width:120,align:'center'">[2.8,2.9)</th>
	    						<th data-options="field:'psmos29t30',width:120,align:'center'">[2.9,3)</th>
	    						<th data-options="field:'psmos30t31',width:120,align:'center'">[3,3.1)</th>
	    						<th data-options="field:'psmos31t32',width:120,align:'center'">[3.1,3.2)</th>
	    						<th data-options="field:'psmos32t33',width:120,align:'center'">[3.2,3.3)</th>
	    						<th data-options="field:'psmos33t34',width:120,align:'center'">[3.3,3.4)</th>
	    						<th data-options="field:'psmos34t35',width:120,align:'center'">[3.4,3.5)</th>
	    						<th data-options="field:'psmos35t36',width:120,align:'center'">[3.5,3.6)</th>
	    						<th data-options="field:'psmos36t37',width:120,align:'center'">[3.6,3.7)</th>
	    						<th data-options="field:'psmos37t38',width:120,align:'center'">[3.7,3.8)</th>
	    						<th data-options="field:'psmos38t39',width:120,align:'center'">[3.8,3.9)</th>
	    						<th data-options="field:'psmos39t40',width:120,align:'center'">[3.9,4)</th>
	    						<th data-options="field:'psmos40t41',width:120,align:'center'">[4,4.1)</th>
	    						<th data-options="field:'psmos41t42',width:120,align:'center'">[4.1,4.2)</th>
	    						<th data-options="field:'psmos42t43',width:120,align:'center'">[4.2,4.3)</th>
	    						<th data-options="field:'psmos43t44',width:120,align:'center'">[4.3,4.4)</th>
	    						<th data-options="field:'psmos44t45',width:120,align:'center'">[4.4,4.5)</th>
	    						<th data-options="field:'psmos45t50',width:120,align:'center'">[4.5,5)</th>
	    						<th data-options="field:'csmos0t21',width:100,align:'center'">[0,2.1)</th>
	    						<th data-options="field:'csmos21t22',width:120,align:'center'">[2.1,2.2)</th>
	    						<th data-options="field:'csmos22t23',width:120,align:'center'">[2.2,2.3)</th>
	    						<th data-options="field:'csmos23t24',width:120,align:'center'">[2.3,2.4)</th>
	    						<th data-options="field:'csmos24t25',width:120,align:'center'">[2.4,2.5)</th>
	    						<th data-options="field:'csmos25t26',width:120,align:'center'">[2.5,2.6)</th>
	    						<th data-options="field:'csmos26t27',width:120,align:'center'">[2.6,2.7)</th>
	    						<th data-options="field:'csmos27t28',width:120,align:'center'">[2.7,2.8)</th>
	    						<th data-options="field:'csmos28t29',width:120,align:'center'">[2.8,2.9)</th>
	    						<th data-options="field:'csmos29t30',width:120,align:'center'">[2.9,3)</th>
	    						<th data-options="field:'csmos30t31',width:120,align:'center'">[3,3.1)</th>
	    						<th data-options="field:'csmos31t32',width:120,align:'center'">[3.1,3.2)</th>
	    						<th data-options="field:'csmos32t33',width:120,align:'center'">[3.2,3.3)</th>
	    						<th data-options="field:'csmos33t34',width:120,align:'center'">[3.3,3.4)</th>
	    						<th data-options="field:'csmos34t35',width:120,align:'center'">[3.4,3.5)</th>
	    						<th data-options="field:'csmos35t36',width:120,align:'center'">[3.5,3.6)</th>
	    						<th data-options="field:'csmos36t37',width:120,align:'center'">[3.6,3.7)</th>
	    						<th data-options="field:'csmos37t38',width:120,align:'center'">[3.7,3.8)</th>
	    						<th data-options="field:'csmos38t39',width:120,align:'center'">[3.8,3.9)</th>
	    						<th data-options="field:'csmos39t40',width:120,align:'center'">[3.9,4)</th>
	    						<th data-options="field:'csmos40t41',width:120,align:'center'">[4,4.1)</th>
	    						<th data-options="field:'csmos41t42',width:120,align:'center'">[4.1,4.2)</th>
	    						<th data-options="field:'csmos42t43',width:120,align:'center'">[4.2,4.3)</th>
	    						<th data-options="field:'csmos43t44',width:120,align:'center'">[4.3,4.4)</th>
	    						<th data-options="field:'csmos44t45',width:120,align:'center'">[4.4,4.5)</th>
	    						<th data-options="field:'csmos45t50',width:120,align:'center'">[4.5,5)</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
			</div>
</div>
		<%-- <div align="right"
					style="margin-right: 5px; height: 23px; padding-top: 2px;"
					id="exportDiv">
					<a id="batchExportExcel" href="#"
						style="vertical-align: middle; line-height: 23px"> <img
							id="batchExcelImg" name="excel" title="excel批量导出"
							src="${pageContext.request.contextPath}/images/batchexcel.png" border="0"> </a>
					&nbsp;&nbsp;
					<a href="#" style="vertical-align: middle; line-height: 23px"
						id="exportExcel"> <img name="excel" title="excel单页导出"
							src="${pageContext.request.contextPath}/images/excel.png" border="0"> </a>
				</div> --%>
</body>
</html>
