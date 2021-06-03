<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>LTE数据业务</title>

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
		$('#tt').tabs({
			tools:[{
				text:'导出当前页',
				iconCls:'icon-excel',
				handler:function(){
					goToPage("${pageContext.request.contextPath}/report/downloadOneSheetLteDataTotalExcel.action?sheetName="+sheetName+"");
				}
			},{
				iconCls:'icon-batchexcel',
				text:'批量导出',
				handler:function(){
					goToPage('${pageContext.request.contextPath}/report/downloadLteDataTotalExcel.action');
				}
			}]
		});
		initTable();
	});
	var sheetName="数据业务统计指标";
	function initTable(){
		$.post("${pageContext.request.contextPath}/report/quaryDataKpi.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#Table1").datagrid('loadData',result.indexKpi);
					$("#Table2").datagrid('loadData',result.coverKpi);
					$("#Table3").datagrid('loadData',result.disturbKpi);
					$("#Table4").datagrid('loadData',result.dispatcherKpi);
					$("#Table5").datagrid('loadData',result.moveKpi);
					$("#Table6").datagrid('loadData',result.insertKpi);
					$("#Table7").datagrid('loadData',result.ftpSectionKpi);
					$("#Table8").datagrid('loadData',result.networkWerkcoverKpi);
					$("#Table9").datagrid('loadData',result.stopKpi);
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
					$('#beg8').textbox('setValue', result.startDate);
					$('#end8').textbox('setValue', result.endDate);
					$("#beg8").textbox('readonly',true);//设置只读
					$("#end8").textbox('readonly',true);//设置只读
					$('#beg9').textbox('setValue', result.startDate);
					$('#end9').textbox('setValue', result.endDate);
					$("#beg9").textbox('readonly',true);//设置只读
					$("#end9").textbox('readonly',true);//设置只读
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
			<div title="数据业务统计指标" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				
				<a>开始时间：<input id="beg1"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end1" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table1" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="1" >FTP</th>
	    						<th colspan="4" >LTE物理层吞吐量</th>
	    						<th colspan="53" >FTP下载业务</th>
	    						<th colspan="53" >FTP上传业务</th>
	    						<th colspan="8" >HTTP浏览业务</th>
	    						<th colspan="7" >HTTP下载业务</th>
	    						<th colspan="11" >流媒体业务</th>
	    						<th colspan="5" >邮件发送业务(5M附件)</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'totaldroprate',width:80,align:'center'">总掉线率</th>
	    						<th data-options="field:'ltephythroughputdlall',width:80,align:'center'">LTE物理层下行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ltephythroughputdlsuccess',width:80,align:'center'">LTE物理层下行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ltephythroughputulall',width:80,align:'center'">LTE物理层上行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ltephythroughputulsuccess',width:80,align:'center'">LTE物理层上行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpattemptcountdl',width:80,align:'center'">FTP下载尝试次数</th>
	    						<th data-options="field:'ftpattemptcountdltdl',width:80,align:'center'">TDL-FTP下载尝试次数</th>
	    						<th data-options="field:'ftpattemptcountdltds',width:80,align:'center'">TDS-FTP下载尝试次数</th>
	    						<th data-options="field:'ftpattemptcountdlgsm',width:80,align:'center'">GSM-FTP下载尝试次数</th>
	    						<th data-options="field:'ftpsuccesscountdl',width:80,align:'center'">FTP下载成功次数</th>
	    						<th data-options="field:'ftpsuccesscountdltdl',width:80,align:'center'">TDL-FTP下载成功次数</th>
	    						<th data-options="field:'ftpsuccesscountdltds',width:80,align:'center'">TDS-FTP下载成功次数</th>
	    						<th data-options="field:'ftpsuccesscountdlgsm',width:80,align:'center'">GSM-FTP下载成功次数</th>
	    						<th data-options="field:'ftpsuccessratedl',width:80,align:'center'">FTP下载成功率(%)</th>
	    						<th data-options="field:'ftpsuccessratedltdl',width:80,align:'center'">TDL-FTP下载成功率(%)</th>
	    						<th data-options="field:'ftpsuccessratedltds',width:80,align:'center'">TDS-FTP下载成功率(%)</th>
	    						<th data-options="field:'ftpsuccessratedlgsm',width:80,align:'center'">GSM-FTP下载成功率(%)</th>
	    						<th data-options="field:'ftpdropcountdl',width:80,align:'center'">FTP下载掉线次数</th>
	    						<th data-options="field:'ftpdropcountdltdl',width:80,align:'center'">TDL-FTP下载掉线次数</th>
	    						<th data-options="field:'ftpdropcountdltds',width:80,align:'center'">TDS-FTP下载掉线次数</th>
	    						<th data-options="field:'ftpdropcountdlgsm',width:80,align:'center'">GSM-FTP下载掉线次数</th>
	    						<th data-options="field:'ftpdropratedl',width:80,align:'center'">FTP下载掉线率(%)</th>
	    						<th data-options="field:'ftpdropratedltdl',width:80,align:'center'">TDL-FTP下载掉线率(%)</th>
	    						<th data-options="field:'ftpdropratedltds',width:80,align:'center'">TDS-FTP下载掉线率(%)</th>
	    						<th data-options="field:'ftpdropratedlgsm',width:80,align:'center'">GSM-FTP下载掉线率(%)</th>
	    						<th data-options="field:'FTPRecTotal2DropDL',width:80,align:'center'">FTP下载数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftprectotal2dropdltdl',width:80,align:'center'">TDL-FTP下载数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftprectotal2dropdltds',width:80,align:'center'">TDS-FTP下载数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftprectotal2dropdlgsm',width:80,align:'center'">GSM-FTP下载数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftptimedltdl',width:80,align:'center'">TDL驻网时长(s)(业务态)</th>
	    						<th data-options="field:'ftptimeratiodltds',width:80,align:'center'">TDS驻网时长(s)(业务态)</th>
	    						<th data-options="field:'ftptimedlgsm',width:80,align:'center'">GSM驻网时长(s)(业务态)</th>
	    						<th data-options="field:'ftptimeratiodltdl',width:80,align:'center'">TDL驻网时长占比(业务态)</th>
	    						<th data-options="field:'ftptimeratiodltds',width:80,align:'center'">TDS驻网时长占比(业务态)</th>
	    						<th data-options="field:'ftptimeratiodlgsm',width:80,align:'center'">GSM驻网时长占比(业务态)</th>
	    						<th data-options="field:'ftprecvtotaldl',width:80,align:'center'">应用层下载流量(含掉线)(KB)</th>
	    						<th data-options="field:'ftprecvtotalsuccessdl',width:80,align:'center'">应用层下载流量(不含掉线)(KB)</th>
	    						<th data-options="field:'ftpdltimeall',width:80,align:'center'">应用层下载时间(含掉线)(s)</th>
	    						<th data-options="field:'ftpdltimesuccess',width:80,align:'center'">应用层下载时间(不含掉线)(s)</th>
	    						<th data-options="field:'ftpdlthrputall',width:80,align:'center'">应用层平均下载速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputsuccess',width:80,align:'center'">应用层平均下载速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputalltdl',width:80,align:'center'">TDL-应用层平均下载速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputsuccesstdl',width:80,align:'center'">TDL-应用层平均下载速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputalltds',width:80,align:'center'">TDS-应用层平均下载速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputsuccesstds',width:80,align:'center'">TDS-应用层平均下载速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputallgsm',width:80,align:'center'">GSM-应用层平均下载速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputsuccessgsm',width:80,align:'center'">GSM-应用层平均下载速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlthrputperrball',width:80,align:'center'">每RB平均下载量(含掉线)(bit/RB)</th>
	    						<th data-options="field:'ftpdlthrputperrbsuccess',width:80,align:'center'">每RB平均下载量(不含掉线)(bit/RB)</th>
	    						<th data-options="field:'ftpdlpdcpthrputall',width:80,align:'center'">PDCP下行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlpdcpthrputsuccess',width:80,align:'center'">PDCP下行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlpdcpcdf5thrputall',width:80,align:'center'">边缘PDCP下行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlpdcpcdf5thrputsuccess',width:80,align:'center'">边缘PDCP下行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlcdf5thrputall',width:80,align:'center'">边缘应用层下行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlcdf5thrputsuccess',width:80,align:'center'">边缘应用层下行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpdlpcdplthrputratio',width:80,align:'center'">PDCP下行低吞吐量占比</th>
	    						<th data-options="field:'ftpdllthrputratio',width:80,align:'center'">应用层下行低吞吐量占比</th>
	    						<th data-options="field:'ftpdlphythrputmax',width:80,align:'center'">物理层瞬时最高下载速率(kbps)</th>
	    						<th data-options="field:'ftpattemptcountul',width:80,align:'center'">FTP上传尝试次数</th>
	    						<th data-options="field:'ftpattemptcountultdl',width:80,align:'center'">TDL-FTP上传尝试次数</th>
	    						<th data-options="field:'ftpattemptcountultds',width:80,align:'center'">TDS-FTP上传尝试次数</th>
	    						<th data-options="field:'ftpattemptcountulgsm',width:80,align:'center'">GSM-FTP上传尝试次数</th>
	    						<th data-options="field:'ftpsuccesscountul',width:80,align:'center'">FTP上传成功次数</th>
	    						<th data-options="field:'ftpsuccesscountultdl',width:80,align:'center'">TDL-FTP上传成功次数</th>
	    						<th data-options="field:'ftpsuccesscountultds',width:80,align:'center'">TDS-FTP上传成功次数</th>
	    						<th data-options="field:'ftpsuccesscountulgsm',width:80,align:'center'">GSM-FTP上传成功次数</th>
	    						<th data-options="field:'ftpsuccessrateul',width:80,align:'center'">FTP上传成功率(%)</th>
	    						<th data-options="field:'ftpsuccessrateultdl',width:80,align:'center'">TDL-FTP上传成功率(%)</th>
	    						<th data-options="field:'ftpsuccessrateultds',width:80,align:'center'">TDS-FTP上传成功率(%)</th>
	    						<th data-options="field:'ftpsuccessrateulgsm',width:80,align:'center'">GSM-FTP上传成功率(%)</th>
	    						<th data-options="field:'ftpdropcountul',width:80,align:'center'">FTP上传掉线次数</th>
	    						<th data-options="field:'ftpdropcountultdl',width:80,align:'center'">TDL-FTP上传掉线次数</th>
	    						<th data-options="field:'ftpdropcountultds',width:80,align:'center'">TDS-FTP上传掉线次数</th>
	    						<th data-options="field:'ftpdropcountulgsm',width:80,align:'center'">GSM-FTP上传掉线次数</th>
	    						<th data-options="field:'ftpdroprateul',width:80,align:'center'">FTP上传掉线率(%)</th>
	    						<th data-options="field:'ftpdroprateultdl',width:80,align:'center'">TDL-FTP上传掉线率(%)</th>
	    						<th data-options="field:'ftpdroprateultds',width:80,align:'center'">TDS-FTP上传掉线率(%)</th>
	    						<th data-options="field:'ftpdroprateuulgsm',width:80,align:'center'">GSM-FTP上传掉线率(%)</th>
	    						<th data-options="field:'ftprectotal2dropul',width:80,align:'center'">FTP上传数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftprectotal2dropultdl',width:80,align:'center'">TDL-FTP上传数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftprectotal2dropultds',width:80,align:'center'">TDS-FTP上传数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftprectotal2dropulgsm',width:80,align:'center'">GSM-FTP上传数据掉线比(Kb/次)</th>
	    						<th data-options="field:'ftptimeultdl',width:80,align:'center'">TDL驻网时长(s)(业务态)</th>
	    						<th data-options="field:'ftptimeultds',width:80,align:'center'">TDS驻网时长(s)(业务态)</th>
	    						<th data-options="field:'ftptimeulgsm',width:80,align:'center'">GSM驻网时长(s)(业务态)</th>
	    						<th data-options="field:'ftptimeratioultdl',width:80,align:'center'">TDL驻网时长占比(业务态)</th>
	    						<th data-options="field:'ftptimeratioultds',width:80,align:'center'">TDS驻网时长占比(业务态)</th>
	    						<th data-options="field:'ftptimeratioulgsm',width:80,align:'center'">GSM驻网时长占比(业务态)</th>
	    						<th data-options="field:'ftprecvtotalul',width:80,align:'center'">应用层上传流量(含掉线)(KB)</th>
	    						<th data-options="field:'ftprecvtotalsuccessul',width:80,align:'center'">应用层上传流量(不含掉线)(KB)</th>
	    						<th data-options="field:'ftpultimeall',width:80,align:'center'">应用层上传时间(含掉线)(s)</th>
	    						<th data-options="field:'ftpultimesuccess',width:80,align:'center'">应用层上传时间(不含掉线)(s)</th>
	    						<th data-options="field:'ftpulthrputall',width:80,align:'center'">应用层平均上传速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputsuccess',width:80,align:'center'">应用层平均上传速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputalltdl',width:80,align:'center'">TDL-应用层平均上传速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputsuccesstdl',width:80,align:'center'">TDL-应用层平均上传速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputalltds',width:80,align:'center'">TDS-应用层平均上传速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputsuccesstds',width:80,align:'center'">TDS-应用层平均上传速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputallgsm',width:80,align:'center'">GSM-应用层平均上传速率(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputsuccessgsm',width:80,align:'center'">GSM-应用层平均上传速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulthrputperrball',width:80,align:'center'">每RB平均上传量(含掉线)(bit/RB)</th>
	    						<th data-options="field:'ftpulthrputperrbsuccess',width:80,align:'center'">每RB平均上传量(不含掉线)(bit/RB)</th>
	    						<th data-options="field:'ftpulpdcpthrputall',width:80,align:'center'">PDCP上行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulpdcpthrputsuccess',width:80,align:'center'">PDCP上行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulpdcpcdf5thrputall',width:80,align:'center'">边缘PDCP上行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulpdcpcdf5thrputsuccess',width:80,align:'center'">边缘PDCP上行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulcdf5thrputall',width:80,align:'center'">边缘应用层上行吞吐量(含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulcdf5thrputsuccess',width:80,align:'center'">边缘应用层上行吞吐量(不含掉线)(kbps)</th>
	    						<th data-options="field:'ftpulpcdplthrputratio',width:80,align:'center'">PDCP上行低吞吐量占比</th>
	    						<th data-options="field:'ftpullthrputratio',width:80,align:'center'">应用层上行低吞吐量占比</th>
	    						<th data-options="field:'ftpulphythrputmax',width:80,align:'center'">物理层瞬时最高上传速率(kbps)</th>
	    						<th data-options="field:'httpbrowseattemptcount',width:80,align:'center'">HTTP登陆尝试次数</th>
	    						<th data-options="field:'httpbrowselogincount',width:80,align:'center'">HTTP登陆成功次数</th>
	    						<th data-options="field:'httpbrowsesuccesscount',width:80,align:'center'">HTTP完全加载次数</th>
	    						<th data-options="field:'httpbrowseloginrate',width:80,align:'center'">HTTP登陆成功率</th>
	    						<th data-options="field:'httpbrowselogindelay',width:80,align:'center'">HTTP登陆时延(s)</th>
	    						<th data-options="field:'httpbrowsesuccessrate',width:80,align:'center'">HTTP浏览成功率</th>
	    						<th data-options="field:'httpbrowsesuccesstime',width:80,align:'center'">HTTP浏览时长(s)</th>
	    						<th data-options="field:'httpbrowsedlthrput',width:80,align:'center'">应用层下载速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'httpdlattemptcount',width:80,align:'center'">HTTP下载尝试次数</th>
	    						<th data-options="field:'httpdlsuccesscount',width:80,align:'center'">HTTP下载成功次数</th>
	    						<th data-options="field:'httpdlsuccessrate',width:80,align:'center'">HTTP下载成功率</th>
	    						<th data-options="field:'httpdldropcount',width:80,align:'center'">HTTP下载掉线次数</th>
	    						<th data-options="field:'httpdldroprate',width:80,align:'center'">HTTP下载掉线率</th>
	    						<th data-options="field:'httpdlthrputall',width:80,align:'center'">HTTP应用层下载速率(含掉线)(kbps)</th>
	    						<th data-options="field:'httpdlthrputsuccess',width:80,align:'center'">HTTP应用层下载速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'streamattemptcount',width:80,align:'center'">流媒体业务发起次数</th>
	    						<th data-options="field:'streamsuccesscount',width:80,align:'center'">流媒体业务成功次数</th>
	    						<th data-options="field:'streamsuccessrate',width:80,align:'center'">流媒体业务成功率</th>
	    						<th data-options="field:'streamrealplaydelay',width:80,align:'center'">流媒体加载时延(s)</th>
	    						<th data-options="field:'streamfiletime',width:80,align:'center'">流媒体时长(s)</th>
	    						<th data-options="field:'streampausetime',width:80,align:'center'">流媒体卡顿时长(s)</th>
	    						<th data-options="field:'streamplaytimetotal',width:80,align:'center'">流媒体播放总时长(s)</th>
	    						<th data-options="field:'streampausecount',width:80,align:'center'">流媒体播放卡顿次数</th>
	    						<th data-options="field:'streamplayovertimerate',width:80,align:'center'">流媒体播放超时比例</th>
	    						<th data-options="field:'streamdlthrputsuccess',width:80,align:'center'">应用层下载速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'streamrealplaythrput',width:80,align:'center'">流媒体加载速率(kbps)</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center'">邮件发送尝试次数</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center'">邮件发送成功次数</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center'">邮件发送成功率</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center'">邮件发送时延(s)</th>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center'">应用层上传速率(不含掉线)(kbps)</th>
	    						
	    					</tr>
	    				</thead>
	    				<tbody><tr></tr>
	    				</tbody>
	    			</table>
		    	
			</div>
			<div title="覆盖类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg2"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end2" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table2" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="20" >覆盖类</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'rsrpaverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'rsrpedge',width:80,align:'center'">边缘RSRP</th>
	    						<th data-options="field:'ltecoverage110rate',width:80,align:'center'">LTE覆盖率(RSRP>-110<br> and SINR>-3)</th>
	    						<th data-options="field:'ltecoverage103rate',width:80,align:'center'">LTE覆盖率(RSRP>-103<br> and SINR>-3)</th>
	    						<th data-options="field:'ltecoverage101rate',width:80,align:'center'">LTE覆盖率(RSRP>-101 <br>and SINR>-3)</th>
	    						<th data-options="field:'distancetotal',width:70,align:'center'">测试总里程(km)</th>
	    						<th data-options="field:'tdlservicedistance',width:100,align:'center'">LTE测试里程(km)</th>
	    						<th data-options="field:'tdsservicedistance',width:120,align:'center'">TDS测试里程(km)</th>
	    						<th data-options="field:'gsmservicedistance',width:120,align:'center'">GSM测试里程(km)</th>
	    						<th data-options="field:'outofservicedistance',width:120,align:'center'">LTE脱网里程(km)</th>
	    						<th data-options="field:'testtimetotal',width:120,align:'center'">测试总时长(s)</th>
	    						<th data-options="field:'outofservicetime',width:120,align:'center'">脱网时长(s)</th>
	    						<th data-options="field:'tdlservicetime',width:120,align:'center'">TDL驻网时长(s)</th>
	    						<th data-options="field:'tdsservicetime',width:120,align:'center'">TDS驻网时长(s)</th>
	    						<th data-options="field:'gsmservicetime',width:120,align:'center'">GSM驻网时长(s)</th>
	    						<th data-options="field:'tdlservicerate',width:120,align:'center'">TDL驻网时长占比</th>
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
				<a>开始时间：<input id="beg3"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end3" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table3" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
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
				<a>开始时间：<input id="beg4"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end4" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table4" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true">
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="35" >调度类</th>
	    					</tr>
                            <tr>
	    						<th data-options="field:'prbperslotdl',width:120,align:'center'">下行平均每时隙调度<br>PRB个数</th>
	    						<th data-options="field:'schedulefreqdl',width:100,align:'center'">下行子帧调度率</th>
	    						<th data-options="field:'prbfreqdl',width:120,align:'center'">下行平均每秒调度<br>PRB个数</th>
	    						<th data-options="field:'mcsavgcw0',width:120,align:'center'">下行码字0MCS平均值</th>
	    						<th data-options="field:'maxfreqmcscw0',width:120,align:'center'">下行码字0最高频率<br>MCS</th>
	    						<th data-options="field:'mcsavgcw1',width:100,align:'center'">下行码字1MCS平均值</th>
	    						<th data-options="field:'maxfreqmcscw1',width:100,align:'center'">下行码字1最高频率MCS</th>
	    						<th data-options="field:'cqiavgcw0',width:100,align:'center'">下行码字0CQI平均值</th>
	    						<th data-options="field:'maxfreqcqicw0',width:120,align:'center'">下行码字0最高频率CQI</th>
	    						<th data-options="field:'cqiavgcw1',width:120,align:'center'">下行码字1CQI平均值</th>
	    						<th data-options="field:'maxfreqcqicw1',width:120,align:'center'">下行码字1最高频率<br>CQI</th>
	    						<th data-options="field:'m64qamratiocw0',width:120,align:'center'">下行码字0 64QAM比例</th>
	    						<th data-options="field:'m16qamratiocw0',width:120,align:'center'">下行码字0 16QAM比例</th>
	    						<th data-options="field:'mqpskratiocw0',width:120,align:'center'">下行码字0 QPSK比例</th>
	    						<th data-options="field:'m64qamratiocw1',width:120,align:'center'">下行码字1 64QAM比例</th>
	    						<th data-options="field:'m16qamratiocw1',width:120,align:'center'">下行码字1 16QAM比例</th>
	    						<th data-options="field:'mqpskratiocw1',width:120,align:'center'">下行码字1 QPSK比例</th>
	    						<th data-options="field:'prbperslotul',width:100,align:'center'">上行平均每时隙调度<br>PRB个数</th>
	    						<th data-options="field:'schedulefrequl',width:100,align:'center'">上行子帧调度率</th>
	    						<th data-options="field:'prbfrequl',width:100,align:'center'">上行平均每秒调度<br>PRB个数</th>
	    						<th data-options="field:'mcsavgul',width:120,align:'center'">上行码字0MCS平均值</th>
	    						<th data-options="field:'maxfreqmcsul',width:120,align:'center'">上行码字0最高频率MCS</th>
	    						<th data-options="field:'m64qamratioul',width:120,align:'center'">上行码字0 64QAM比例</th>
	    						<th data-options="field:'m16qamratioul',width:120,align:'center'">上行码字0 16QAM比例</th>
	    						<th data-options="field:'mqpskratioul',width:120,align:'center'">上行码字0 QPSK比例</th>
	    						<th data-options="field:'rank1ratio',width:120,align:'center'">上报RANK1采样占比</th>
	    					    <th data-options="field:'rank2ratio',width:80,align:'center'">上报RANK2采样占比</th>
	    						<th data-options="field:'sstreamtimeratio',width:70,align:'center'">单流时长占比</th>
	    						<th data-options="field:'dstreamtimeratio',width:100,align:'center'">双流时长占比</th>
	    						<th data-options="field:'sstreamthrputratio',width:120,align:'center'">单流流量占比</th>
	    						<th data-options="field:'dstreamthrputratio',width:120,align:'center'">双流流量占比</th>
	    						<th data-options="field:'tm2timeratio',width:120,align:'center'">传输模式(TM=2)时长<br>占比(s)</th>
	    						<th data-options="field:'tm3timeratio',width:120,align:'center'">传输模式(TM=3)时长<br>占比(s)</th>
	    						<th data-options="field:'tm7timeratio',width:120,align:'center'">传输模式(TM=7)时长<br>占比(s)</th>
	    						<th data-options="field:'tm8timeratio',width:120,align:'center'">传输模式(TM=8)时长<br>占比(s)</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
		    	
			</div>
			<div title="移动类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg5"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end5" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table5" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"   >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="53">移动类</th>
	    					</tr>
	    					<tr>
	    						<th data-options= "field:'ltehoattemptcount',width:120,align:'center'">LTE网内切换尝试总次数</th>
	    						<th data-options="field:'ltehosuccesscount',width:80,align:'center'">LTE网内切换成功总次数</th>
	    						<th data-options="field:'ltehofailcount',width:80,align:'center'">LTE网内切换失败总次数</th>
	    						<th data-options="field:'ltehosuccessratio',width:80,align:'center'">LTE网内切换成功率(%)</th>
	    						<th data-options="field:'ltehodelay',width:70,align:'center'">LTE网内切换平均时延(s)</th>
	    						<th data-options="field:'lteintrafreqhoattemptcount',width:100,align:'center'">LTE同频切换尝试总次数</th>
	    						<th data-options="field:'lteintrafreqhosuccesscount',width:120,align:'center'">LTE同频切换成功总次数</th>
	    						<th data-options="field:'lteintrafreqhofailcount',width:120,align:'center'">LTE同频切换失败总次数</th>
	    						<th data-options="field:'lteintrafreqhosuccessratio',width:120,align:'center'">LTE同频切换成功率(%)</th>
	    						<th data-options="field:'lteintrafreqhodelay',width:120,align:'center'">LTE同频切换平均时延(s)</th>
	    						<th data-options="field:'lteinterfreqhoattemptcount',width:120,align:'center'">LTE异频切换尝试总次数</th>
	    						<th data-options="field:'lteinterfreqhosuccesscount',width:120,align:'center'">LTE异频切换成功总次数</th>
	    						<th data-options="field:'lteinterfreqhofailcount',width:120,align:'center'">LTE异频切换失败总次数</th>
	    						<th data-options= "field:'lteinterfreqhosuccessratio',width:80,align:'center'">LTE异频切换成功率(%)</th>
	    						<th data-options="field:'lteinterfreqhodelay',width:80,align:'center'">LTE异频切换平均时延(s)</th>
	    						<th data-options="field:'lterselattemptcount',width:80,align:'center'">LTE网间重选尝试次数</th>
	    						<th data-options="field:'lterselsuccesscount',width:80,align:'center'">LTE网间重选成功次数</th>
	    						<th data-options="field:'lterselsuccessratio',width:70,align:'center'">LTE网间重选成功率(%)</th>
	    						<th data-options="field:'tdl2tdsrselattemptcount',width:100,align:'center'">TDL-TDS重选尝试次数</th>
	    						<th data-options="field:'tdl2tdsrselsucesscount',width:120,align:'center'">TDL-TDS重选成功次数</th>
	    						<th data-options="field:'tdl2tdsrselsucessratio',width:120,align:'center'">TDL-TDS重选成功率(%)</th>
	    						<th data-options="field:'tdl2tdsrseldelay',width:120,align:'center'">TDL-TDS重选时延(s)</th>
	    						<th data-options="field:'td22tdlrselattemptcount',width:120,align:'center'">TDS-TDL重选尝试次数</th>
	    						<th data-options="field:'td22tdlrselsucesscount',width:120,align:'center'">TDS-TDL重选成功次数</th>
	    						<th data-options="field:'td22tdlrselsucessratio',width:120,align:'center'">TDS-TDL重选成功率(%)	</th>
	    						<th data-options="field:'td22tdlrseldelay',width:120,align:'center'">TDS-TDL重选时延(s)</th>
	    						<th data-options= "field:'tdl2gsmrselattemptcount',width:80,align:'center'">TDL-GSM重选尝试次数</th>
	    						<th data-options="field:'tdl2gsmrselsucesscount',width:80,align:'center'">TDL-GSM重选成功次数</th>
	    						<th data-options="field:'tdl2gsmrselsucessratio',width:80,align:'center'">TDL-GSM重选成功率(%)</th>
	    						<th data-options="field:'tdl2gsmrseldelay',width:80,align:'center'">TDL-GSM重选时延(s)</th>
	    						<th data-options="field:'gsm2tdlrselattemptcount',width:70,align:'center'">GSM-TDL重选尝试次数</th>
	    						<th data-options="field:'gsm2tdlrselsucesscount',width:100,align:'center'">GSM-TDL重选成功次数</th>
	    						<th data-options="field:'gsm2tdlrselsucessratio',width:120,align:'center'">GSM-TDL重选成功率(%)</th>
	    						<th data-options="field:'gsm2tdlrseldelay',width:120,align:'center'">GSM-TDL重选时延(s)</th>
	    						<th data-options="field:'lteredirattemptcount',width:120,align:'center'">LTE网间重定向尝试次数</th>
	    						<th data-options="field:'lteredirsuccesscount',width:120,align:'center'">LTE网间重定向成功次数</th>
	    						<th data-options="field:'lteredirsuccessratio',width:120,align:'center'">LTE网间重定向成功率(%)</th>
	    						<th data-options="field:'tdl2tdsredirattemptcount',width:120,align:'center'">TDL-TDS重定向尝试次数</th>
	    						<th data-options="field:'tdl2tdsredirsuccesscount',width:120,align:'center'">TDL-TDS重定向成功次数</th>
	    						<th data-options= "field:'tdl2tdsredirsuccessratio',width:80,align:'center'">TDL-TDS重定向成功率(%)</th>
	    						<th data-options="field:'tdl2tdsredirdelay',width:80,align:'center'">TDL-TDS重定向平均时延(s)</th>
	    						<th data-options="field:'tdl2gsmredirattemptcount',width:80,align:'center'">TDL-GSM重定向尝试次数</th>
	    						<th data-options="field:'tdl2gsmredirsuccesscount',width:80,align:'center'">TDL-GSM重定向成功次数</th>
	    						<th data-options="field:'tdl2gsmredirsuccessratio',width:70,align:'center'">TDL-GSM重定向成功率(%)</th>
	    						<th data-options="field:'tdl2gsmredirdelay',width:100,align:'center'">TDL-GSM重定向平均时延(s)</th>
	    						<th data-options="field:'tds2tdlredirattemptcount',width:120,align:'center'">TDS-TDL重定向尝试次数</th>
	    						<th data-options="field:'tds2tdlredirsuccesscount',width:120,align:'center'">TDS-TDL重定向成功次数</th>
	    						<th data-options="field:'tds2tdlredirsuccessratio',width:120,align:'center'">TDS-TDL重定向成功率(%)</th>
	    						<th data-options="field:'tds2tdlredirdelay',width:120,align:'center'">TDS-TDL重定向平均时延(s)</th>
	    						<th data-options="field:'tauattemptcount',width:120,align:'center'">TA更新尝试次数</th>
	    						<th data-options="field:'tausuccesscount',width:120,align:'center'">TA更新成功次数</th>
	    						<th data-options="field:'tausuccessratio',width:120,align:'center'">TA更新成功率(%)</th>
	    						<th data-options= "field:'taudelay',width:80,align:'center'">TA更新时延(s)</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
		    	

			</div>
			<div title="接入类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				
				<a>开始时间：<input id="beg6"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end6" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table6" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="7">接入类</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'lteattachsuccessratio',width:140,align:'center'">LTE ATTACH成功率(%)</th>
	    						<th data-options="field:'lteattachdelay',width:140,align:'center'">LTE ATTACH平均时延(s)</th>
	    						<th data-options="field:'lteservicereqsuccessratio',width:140,align:'center'">LTE SERVICE成功率(%)</th>
	    						<th data-options="field:'lteservicereqdelay',width:140,align:'center'">LTE SERVICE平均时延(s)</th>
	    						<th data-options="field:'dbandtimeratio',width:100,align:'center'">D频段时长占比</th>
	    						<th data-options="field:'ebandtimeratio',width:100,align:'center'">E频段时长占比</th>
	    						<th data-options="field:'fbandtimeratio',width:100,align:'center'">F频段时长占比</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
		    	

			</div>
			
			<div title="应用层FTP速率分段占比统计" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg7"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end7" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table7" class="easyui-datagrid"  data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true" >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	    						<th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="16" >应用层FTP下载速率分段占比</th>
	    						<th colspan="9" >应用层FTP上传速率分段占比</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'ftpdl0t1',width:80,align:'center'">[0,1)</th>
	    						<th data-options="field:'ftpdl1t2',width:80,align:'center'">[1,2)</th>
	    						<th data-options="field:'ftpdl2t4',width:80,align:'center'">[2,4)</th>
	    						<th data-options="field:'ftpdl4t5',width:80,align:'center'">[4,5)</th>
	    						<th data-options="field:'ftpdl5t8',width:80,align:'center'">[5,8)</th>
	    						<th data-options="field:'ftpdl8t10',width:70,align:'center'">[8,10)</th>
	    						<th data-options="field:'ftpdl10t12',width:100,align:'center'">[10,12)</th>
	    						<th data-options="field:'ftpdl12t15',width:120,align:'center'">[12,15)</th>
	    						<th data-options="field:'ftpdl15t20',width:120,align:'center'">[15,20)</th>
	    						<th data-options="field:'ftpdl20t25',width:120,align:'center'">[20,25)</th>
	    						<th data-options="field:'ftpdl25t30',width:120,align:'center'">[25,30)</th>
	    						<th data-options="field:'ftpdl30t35',width:120,align:'center'">[30,35)</th>
	    						<th data-options="field:'ftpdl35t40',width:120,align:'center'">[35,40)</th>
	    						<th data-options="field:'ftpdl40more',width:120,align:'center'">大于等于40M</th>
	    						<th data-options="field:'ftpdlless2',width:120,align:'center'">小于2M占比</th>
	    						<th data-options="field:'ftpdl10more',width:120,align:'center'">大于10M占比</th>
	    						<th data-options="field:'ftpul0dot256',width:120,align:'center'">小于256K</th>
	    						<th data-options="field:'ftpuldot256t1',width:120,align:'center'">[256K,1M)</th>
	    						<th data-options="field:'ftpul1t3dot5',width:120,align:'center'">[1,3.5)</th>
	    						<th data-options="field:'ftpul3dot5t6',width:120,align:'center'">[3.5,6)</th>
	    						<th data-options="field:'ftpul6t9',width:120,align:'center'">[6,9)</th>
	    						<th data-options="field:'ftpul9t12',width:120,align:'center'">[9,12)</th>
	    						<th data-options="field:'ftpul12t15',width:120,align:'center'">[12,15)</th>
	    						<th data-options="field:'ftpul15t20',width:120,align:'center'">[15,20)</th>
	    						<th data-options="field:'ftpul20more',width:120,align:'center'">大于等于20M</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
		    	
			</div>
			<div title="网络覆盖类分段占比统计" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				
				<a>开始时间：<input id="beg8"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end8" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table8" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	                            <th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="7" >RSRP区间分布统计占比</th>
	    						<th colspan="7" >SINR区间分布统计占比</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'rsrp110less',width:80,align:'center'">小于-110</th>
	    						<th data-options="field:'rsrp110t105',width:80,align:'center'">[-110,-105)</th>
	    						<th data-options="field:'rsrp105t100',width:80,align:'center'">[-105,-100)</th>
	    						<th data-options="field:'rsrp100t95',width:80,align:'center'">[-100,-95)</th>
	    						<th data-options="field:'rsrp95t85',width:80,align:'center'">[-95,-85)</th>
	    						<th data-options="field:'rsrp85t75',width:70,align:'center'">[-85,-75)</th>
	    						<th data-options="field:'rsrp75t40',width:100,align:'center'">[-75,-40)</th>
	    						<th data-options="field:'sinr2less',width:120,align:'center'">小于-3</th>
	    						<th data-options="field:'sinr3t0',width:120,align:'center'">[-3,0)</th>
	    						<th data-options="field:'sinr0t3',width:120,align:'center'">[0,3)</th>
	    						<th data-options="field:'sinr3t6',width:120,align:'center'">[3,6)</th>
	    						<th data-options="field:'sinr6t9',width:120,align:'center'">[6,9)</th>
	    						<th data-options="field:'sinr9t15',width:120,align:'center'">[9,15)</th>
	    						<th data-options="field:'sinr15more',width:120,align:'center'">大于15</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
		    	
			</div>
			<div title="停车测试" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<a>开始时间：<input id="beg9"type="text" class="easyui-textbox" style="width:35%;height:100%">  结束时间：<input id="end9" type="text" class="easyui-textbox"  style="width:35%;height:100%"></a>
  					<table id="Table9" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th rowspan="2" data-options="field:'testLogTerminalGroup',width:80,align:'center',formatter:showTooltip">城市</th>
	                            <th rowspan="2" data-options="field:'testLogFileName',width:80,align:'center',formatter:showTooltip">文件名</th>
	    						<th colspan="8" ></th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'m_stRoadName',width:80,align:'center'">测试时间</th>
	    						<th data-options="field:'m_dbDistance',width:80,align:'center'">停车开始时间</th>
	    						<th data-options="field:'m_dbContinueTime',width:80,align:'center'">停车结束时间</th>
	    						<th data-options="field:'startTime2String',width:80,align:'center'">停车时长</th>
	    						<th data-options="field:'cellName',width:80,align:'center'">停车开始时经度</th>
	    						<th data-options="field:'rsrpAvg',width:70,align:'center'">停车开始时纬度</th>
	    						<th data-options="field:'fileName',width:100,align:'center'">停车结束时经度</th>
	    						<th data-options="field:'recSeqNo',width:120,align:'center'">停车结束时纬度</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
		    	</div>
			</div>


</body>
</html>
