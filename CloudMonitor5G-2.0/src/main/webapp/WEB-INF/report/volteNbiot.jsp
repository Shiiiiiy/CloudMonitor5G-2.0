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
<title>NB-IoT业务</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@ include file="../../taglibs/jquery.jsp"%>
<%@ include file="../../taglibs/easyui.jsp"%>
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
				goToPage("${pageContext.request.contextPath}/report/downloadOneSheetNbiotTotalExcel.action?sheetName="+sheetName+"");
			}
		},{
			iconCls:'icon-batchexcel',
			text:'批量导出',
			handler:function(){
				goToPage('${pageContext.request.contextPath}/report/downloadNbiotTotalExcel.action');
			}
		}]
	});
	initTable();
});
var sheetName="业务统计指标";
function initTable(){
	$.post("${pageContext.request.contextPath}/report/quaryNbiotKpi.action",{},
		function(result){
			if (result.errorMsg) {
				$.messager.alert("系统提示", result.errorMsg,'error');
			} else {
				$("#Table1").datagrid('loadData',result.businessKpi);
				$("#Table2").datagrid('loadData',result.coverKpi);
				$("#Table3").datagrid('loadData',result.disturbKpi);
				$("#Table4").datagrid('loadData',result.dispatcherKpi);
				$("#Table5").datagrid('loadData',result.moveKpi);
				$("#Table6").datagrid('loadData',result.insertKpi);
				$("#Table7").datagrid('loadData',result.coverProportionKpi);
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
<body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
		<div id="tt"class="easyui-tabs" style="width:100%;height:100%;"data-options="fit:true,onSelect:sele">
			<div title="数据业务统计指标" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg1"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end1" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table1" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
   						<th rowspan="2" data-options="field:'testLogFileName',width:60,align:'center',formatter:showTooltip" >文件名</th>
   						<th colspan="8" >Ping业务</th>
   						<th colspan="20" >UDP下载业务</th>
   						<th colspan="10" >UDP上传业务</th>
						</tr>
						<tr>
						 <th data-options="field:'pingattemptcount',width:110,align:'center',formatter:showTooltip">Ping尝试次数</th>
						 <th data-options="field:'pingsuccesscount',width:110,align:'center',formatter:showTooltip">Ping成功次数</th>
						 <th data-options="field:'pingsuccessrate',width:110,align:'center',formatter:showTooltip">Ping成功率(%)</th>
						 <th data-options="field:'pingfailcount',width:130,align:'center',formatter:showTooltip">Ping失败次数</th>
						 <th data-options="field:'pingfailrate',width:150,align:'center',formatter:showTooltip">Ping失败率(%)</th>
						 <th data-options="field:'pingmaxdelay',width:110,align:'center',formatter:showTooltip">最大时延(ms)</th>
						 <th data-options="field:'pingmindelay',width:110,align:'center',formatter:showTooltip">最小时延(ms)</th>
						 <th data-options="field:'pingavgdelay',width:110,align:'center',formatter:showTooltip">平均时延(ms)</th>
						 <th data-options="field:'udpdlattemptcount',width:110,align:'center',formatter:showTooltip">UDP下载尝试次数</th>
						 <th data-options="field:'udpdlsuccesscount',width:160,align:'center',formatter:showTooltip">UDP下载成功次数</th>
						 <th data-options="field:'udpdlsuccessrate',width:160,align:'center',formatter:showTooltip">UDP下载成功率(%)</th>
						 <th data-options="field:'udpdldropcount',width:110,align:'center',formatter:showTooltip">UDP下载掉线次数</th>
						 <th data-options="field:'udpdldroprate',width:180,align:'center',formatter:showTooltip">UDP下载掉线率(%)</th>
						 <th data-options="field:'udpdlrectotal2drop',width:180,align:'center',formatter:showTooltip">UDP下载数据<br>掉线比(Kb/次)</th>
						 <th data-options="field:'udpdlrectotal',width:130,align:'center',formatter:showTooltip">应用层下载流量<br>(含掉线)(KB)</th>
						 <th data-options="field:'udpdlrectotalsuccess',width:160,align:'center',formatter:showTooltip">应用层下载流量<br>(不含掉线)(KB)</th>
						 <th data-options="field:'udpdltimeall',width:160,align:'center',formatter:showTooltip">应用层下载时间<br>(含掉线)(s)</th>
						 <th data-options="field:'udpdltimesuccess',width:110,align:'center',formatter:showTooltip">应用层下载时间<br>(不含掉线)(s))</th>
						 <th data-options="field:'udpdlthrputall',width:110,align:'center',formatter:showTooltip">应用层平均下载<br>速率(含掉线)(kbps)</th>
						 <th data-options="field:'udpdlthrputsuccess',width:110,align:'center',formatter:showTooltip">应用层平均下载<br>速率(不含掉线)(kbps)</th>
						 <th data-options="field:'udpdlblerall',width:110,align:'center',formatter:showTooltip">下行丢包率(含掉线)(%)</th>
						 <th data-options="field:'udpdlblersuccess',width:110,align:'center',formatter:showTooltip">下行丢包率(不含掉线)(%)</th>
						 <th data-options="field:'udpdljitterall',width:110,align:'center',formatter:showTooltip">下行抖动(含掉线)(ms)</th>
						 <th data-options="field:'udpdljittersuccess',width:110,align:'center',formatter:showTooltip">下行抖动(不含掉线)(ms)</th>
						 <th data-options="field:'udppdcpdlthrall',width:110,align:'center',formatter:showTooltip">RLC下行吞吐量(含掉线)(kbps)</th>
						 <th data-options="field:'udppdcpdlthrsuccess',width:110,align:'center',formatter:showTooltip">RLC下行吞吐量(不含掉线)(kbps)</th>
						 <th data-options="field:'udppdcpdlcdf5thrall',width:110,align:'center',formatter:showTooltip">边缘应用层下载吞吐量(含掉线)(kbps)</th>
						 <th data-options="field:'udppdcpdlcdf5thrsuccess',width:110,align:'center',formatter:showTooltip">边缘应用层下载吞吐量(不含掉线)(kbps)</th>
						 <th data-options="field:'udpulattemptcount',width:110,align:'center',formatter:showTooltip">UDP上传尝试次数</th>
						 <th data-options="field:'udpulsuccesscount',width:110,align:'center',formatter:showTooltip">UDP上传成功次数</th>
						 <th data-options="field:'udpulsuccessrate',width:110,align:'center',formatter:showTooltip">UDP上传成功率(%)</th>
						 <th data-options="field:'udpulfailcount',width:110,align:'center',formatter:showTooltip">UDP上传失败次数</th>
						 <th data-options="field:'udpulfailrate',width:110,align:'center',formatter:showTooltip">UDP上传失败率(%)</th>
						 <th data-options="field:'udpulrectotal',width:110,align:'center',formatter:showTooltip">应用层成功上传流量(KB)</th>
						 <th data-options="field:'udpulrectimeall',width:110,align:'center',formatter:showTooltip">应用层成功上传时间(s)</th>
						 <th data-options="field:'udpulthrsuccess',width:110,align:'center',formatter:showTooltip">应用层平均上传速率(不含失败)(kbps)</th>
						 <th data-options="field:'udppdcpulthrsuccess',width:110,align:'center',formatter:showTooltip">RLC上行吞吐量(不含失败)(kbps)</th>
						 <th data-options="field:'udpcdf5thrsuccess',width:110,align:'center',formatter:showTooltip">边缘应用层上行吞吐量(不含失败)(kbps)</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>
			</div>
			<div title="覆盖类" style="width:100%;height:100%;">
			<a>开始时间：<input id="beg2"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end2" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table2" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
   						<th rowspan="2" data-options="field:'testLogFileName',width:60,align:'center',formatter:showTooltip" >文件名</th>
   						<th colspan="11" >覆盖指标</th>
						</tr>
						<tr>
						 <th data-options="field:'rsrpaverage',width:110,align:'center',formatter:showTooltip">平均RSRP</th>
						 <th data-options="field:'nbcoverage94rate',width:110,align:'center',formatter:showTooltip">覆盖率（RSRP >- 94）</th>
						 <th data-options="field:'nbcoverage97rate',width:110,align:'center',formatter:showTooltip">覆盖率（RSRP >- 97）</th>
						 <th data-options="field:'rsrpcdf1',width:110,align:'center',formatter:showTooltip">RSRP(CDF 1%)</th>
						 <th data-options="field:'rsrpcdf5',width:110,align:'center',formatter:showTooltip">RSRP(CDF 5%)</th>
						 <th data-options="field:'rsrpcdf50',width:130,align:'center',formatter:showTooltip">RSRP(CDF 50%)</th>
						 <th data-options="field:'distancetotal',width:150,align:'center',formatter:showTooltip">测试总里程(km)</th>
						 <th data-options="field:'outofservicedistance',width:110,align:'center',formatter:showTooltip">脱网里程(km)</th>
						 <th data-options="field:'testtimetotal',width:110,align:'center',formatter:showTooltip">测试总时长(s)</th>
						 <th data-options="field:'outofservicetime',width:110,align:'center',formatter:showTooltip">脱网时长(s)</th>
						 <th data-options="field:'speed',width:110,align:'center',formatter:showTooltip">平均车速(km/h)</th>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>

			</div>
			<div title="干扰类" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg3"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end3" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table3" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:150,align:'center',formatter:showTooltip">城市</th>
   						<th rowspan="2" data-options="field:'testLogFileName',width:200,align:'center',formatter:showTooltip" >文件名</th>
   						<th colspan="11" >干扰类</th>
						</tr>
						<tr>
						 <th data-options="field:'sinraverage',width:100,align:'center',formatter:showTooltip">平均SINR</th>
						 <th data-options="field:'nbsinrsign3rate',width:100,align:'center',formatter:showTooltip">SINR -3以上占比</th>
						 <th data-options="field:'nbsinr0rate',width:100,align:'center',formatter:showTooltip">SINR 0以上占比</th>
						 <th data-options="field:'sinrcdf1',width:100,align:'center',formatter:showTooltip">SINR(CDF 1%)</th>
						 <th data-options="field:'sinrcdf5',width:100,align:'center',formatter:showTooltip">SINR(CDF 5%)</th>
						 <th data-options="field:'sinrcdf50',width:100,align:'center',formatter:showTooltip">SINR(CDF 50%)</th>
						 <th data-options="field:'rsrqaverage',width:100,align:'center',formatter:showTooltip">平均RSRQ</th>
						 <th data-options="field:'bleraverage',width:100,align:'center',formatter:showTooltip">BLER平均值</th>
						 <th data-options="field:'npuschretxrate',width:100,align:'center',formatter:showTooltip">NPUSCH 重传率（%）</th>
						 <th data-options="field:'npdschretxrate',width:100,align:'center',formatter:showTooltip">NPDSCH 重传率（%）</th>
						 <th data-options="field:'npuschtxpower',width:100,align:'center',formatter:showTooltip">NPUSCH 平均发射功率（dBm）</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>
			</div>
			<div title="调度类" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg4"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end4" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table4" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
   						<th rowspan="2" data-options="field:'testLogFileName',width:100,align:'center',formatter:showTooltip" >文件名</th>
   						<th rowspan="2" data-options="field:'bleraverage',width:100,align:'center'" >下行BLER</th>
   						<th rowspan="2" data-options="field:'ulmcsaverage',width:100,align:'center'"  >上行平均MCS</th>
   						<th rowspan="2" data-options="field:'ultbsaverage',width:100,align:'center'" >上行平均TBS</th>
   						<th rowspan="2" data-options="field:'ulschrunum',width:100,align:'center'" >上行平均调度RU数</th>
   						<th rowspan="2" data-options="field:'dlmcsaverage',width:100,align:'center'"  >下行平均MCS</th>
   						<th rowspan="2" data-options="field:'dltbsaverage',width:100,align:'center'" >下行平均TBS</th>
   						<th rowspan="2" data-options="field:'dlschsfnum',width:100,align:'center'" >下行平均调度SF数</th>
   						<th colspan="7" >上行RU配置</th>
                        <th colspan="3" >重复次数</th>
                        <th colspan="3" >NPDCCH</th>
                        <th colspan="3" >调度占比</th>
						</tr>
						<tr>
						 <th data-options="field:'subcarrier3dot75ratio',width:100,align:'center',formatter:showTooltip">子载波带宽3.75占比</th>
						 <th data-options="field:'subcarrier15ratio',width:100,align:'center',formatter:showTooltip">子载波带宽15K占比</th>
						 <th data-options="field:'subcarrierschnum',width:100,align:'center',formatter:showTooltip">子载波个数均值</th>
						 <th data-options="field:'subcarriernum1ratio',width:100,align:'center',formatter:showTooltip">子载波个数1占比</th>
						 <th data-options="field:'subcarriernum3ratio',width:100,align:'center',formatter:showTooltip">子载波个数3占比</th>
						 <th data-options="field:'subcarriernum6ratio',width:100,align:'center',formatter:showTooltip">子载波个数6占比</th>
						 <th data-options="field:'subcarriernum12ratio',width:100,align:'center',formatter:showTooltip">子载波个数12占比</th>
						 <th data-options="field:'npuschrepnum',width:100,align:'center',formatter:showTooltip">NPUSCH</th>
						 <th data-options="field:'nprachrepnum',width:100,align:'center',formatter:showTooltip">NPRACH</th>
						 <th data-options="field:'npdschrepnum',width:100,align:'center',formatter:showTooltip">NPDSCH</th>
						 <th data-options="field:'npdcchreunum',width:100,align:'center',formatter:showTooltip">重复次数</th>
						 <th data-options="field:'npdcchn0schnum',width:100,align:'center',formatter:showTooltip">平均每秒的N0调度次数</th>
						 <th data-options="field:'npdcchn1schnum',width:100,align:'center',formatter:showTooltip">平均每秒的N1调度次数</th>
						 <th data-options="field:'nbformatn0rate',width:100,align:'center',formatter:showTooltip">Format_N0占比（%）</th>
						 <th data-options="field:'nbformatn1rate',width:100,align:'center',formatter:showTooltip">Format_N1占比（%）</th>
						 <th data-options="field:'nbformatn2rate',width:100,align:'center',formatter:showTooltip">Format_N2占比（%）</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>
			</div>
			<div title="移动类" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg5"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end5" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table5" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
   						<th rowspan="2" data-options="field:'testLogFileName',width:100,align:'center',formatter:showTooltip" >文件名</th>
   						<th colspan="8" >移动类</th>
						</tr>
						<tr>
						 <th data-options="field:'reselectattemptcount',width:100,align:'center',formatter:showTooltip">重选次数</th>
						 <th data-options="field:'reselectbrsrp',width:100,align:'center',formatter:showTooltip">重选前平均RSRP</th>
						 <th data-options="field:'reselectbsinr',width:100,align:'center',formatter:showTooltip">重选前平均SINR</th>
						 <th data-options="field:'reselectarsrp',width:100,align:'center',formatter:showTooltip">重选后平均RSRP</th>
						 <th data-options="field:'reselectasinr',width:100,align:'center',formatter:showTooltip">重选后平均SINR</th>
						 <th data-options="field:'reselectdelay',width:100,align:'center',formatter:showTooltip">重选时延</th>
						 <th data-options="field:'reselectsuccesscount',width:100,align:'center',formatter:showTooltip">重选成功次数</th>
						 <th data-options="field:'reselectsuccessrate',width:100,align:'center',formatter:showTooltip">重选成功率</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>
			</div>
			<div title="接入类" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg6"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end6" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table6" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
   						<th rowspan="2" data-options="field:'testLogFileName',width:100,align:'center',formatter:showTooltip" >文件名</th>
   						<th colspan="8" >接入类</th>
						</tr>
						<tr>
						 <th data-options="field:'attachsuccessrate',width:100,align:'center',formatter:showTooltip">ATTACH成功率(%)</th>
						 <th data-options="field:'attachdelay',width:100,align:'center',formatter:showTooltip">ATTACH平均时延(s)</th>
						 <th data-options="field:'cpservicesuccessrate',width:100,align:'center',formatter:showTooltip">CP SERVICE成功率(%)</th>
						 <th data-options="field:'cpservicedelay',width:100,align:'center',formatter:showTooltip">CP SERVICE平均时延(s)</th>
						 <th data-options="field:'rrcsetupsuccesrate',width:100,align:'center',formatter:showTooltip">RRC连接建立成功率(%)</th>
						 <th data-options="field:'rrcsetupdelay',width:100,align:'center',formatter:showTooltip">RRC连接建立时延(s)</th>
						 <th data-options="field:'rrcresumesuccessrate',width:100,align:'center',formatter:showTooltip">RRC Resume成功率(%)</th>
						 <th data-options="field:'rrcresumedelay',width:100,align:'center',formatter:showTooltip">RRC Resume时延(s)</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>
			</div>
			<div title="覆盖类分段占比统计" style="width:100%;height:100%;">
			 <a>开始时间：<input id="beg7"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end7" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
				<table id="Table7" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
					<thead>
						<tr>
						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
   						<th rowspan="2" data-options="field:'testLogFileName',width:100,align:'center',formatter:showTooltip" >文件名</th>
   						<th colspan="7" >RSRP区间分布统计占比</th>
                        <th colspan="7" >SINR区间分布统计占比</th>
						</tr>
						<tr>
						 <th data-options="field:'rsrp110less',width:100,align:'center',formatter:showTooltip">小于-110</th>
						 <th data-options="field:'rsrp110t105',width:100,align:'center',formatter:showTooltip">[-110,-105)</th>
						 <th data-options="field:'rsrp105t100',width:100,align:'center',formatter:showTooltip">[-105,-100)</th>
						 <th data-options="field:'rsrp100t95',width:100,align:'center',formatter:showTooltip">[-100,-95)</th>
						 <th data-options="field:'rsrp95t85',width:100,align:'center',formatter:showTooltip">[-95,-85)</th>
						 <th data-options="field:'rsrp85t75',width:100,align:'center',formatter:showTooltip">[-85,-75)</th>
						 <th data-options="field:'rsrp75t40',width:100,align:'center',formatter:showTooltip">[-75,-40)</th>
						 <th data-options="field:'sinr2less',width:100,align:'center',formatter:showTooltip">小于-3</th>
						 <th data-options="field:'sinr3t0',width:100,align:'center',formatter:showTooltip">[-3,0)</th>
						 <th data-options="field:'sinr0t3',width:100,align:'center',formatter:showTooltip">[0,3)</th>
						 <th data-options="field:'sinr3t6',width:100,align:'center',formatter:showTooltip">[3,6)</th>
						 <th data-options="field:'sinr6t9',width:100,align:'center',formatter:showTooltip">[6,9)</th>
						 <th data-options="field:'sinr9t15',width:100,align:'center',formatter:showTooltip">[9,15)</th>
						 <th data-options="field:'sinr15more',width:100,align:'center',formatter:showTooltip">大于15</th>
						</tr>
					</thead>
					<tbody>
					<tr>
					</tr>
					</tbody>
				</table>
			</div>
		</div>
</body>
</html>
