<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>5G数据业务</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
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
					goToPage("${pageContext.request.contextPath}/report5g/downloadOneSheetFgDataTotalExcel.action?sheetName="+sheetName);
				}
			},{
				iconCls:'icon-batchexcel',
				text:'批量导出',
				handler:function(){
					goToPage('${pageContext.request.contextPath}/report5g/downloadFgDataTotalExcel.action');
				}
			}]
		});
		initTable();
	});
	var sheetName="覆盖类指标统计";
	function initTable(){
		$.post("${pageContext.request.contextPath}/report5g/quaryDataKpi.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#Table1").datagrid('loadData',result.coverKpi);
					$("#Table2").datagrid('loadData',result.disturbKpi);
					$("#Table3").datagrid('loadData',result.subIndexKpi);
					$("#Table4").datagrid('loadData',result.eeKpi);
					$("#Table5").datagrid('loadData',result.beamKpi);
					$(".begTime").each(function(i,dome){
						$(dome).textbox('setValue', result.startDate);
					});
					$(".endTime").each(function(i,dome){
						$(dome).textbox('setValue', result.endDate);
					});
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
   	<div id="tt"class="easyui-tabs" data-options="fit:true,onSelect:sele">
   		<div title="覆盖类指标统计" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="padding:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table1" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center'">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="22" >覆盖类</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'rsrpaverage',align:'center'">平均RSRP</th>
		   						<th data-options="field:'rsrpedge',align:'center'">边缘RSRP</th>
		   						<th data-options="field:'ltecoverage110rate',align:'center'">5G覆盖率(RSRP>-110<br> and SINR>-3)</th>
		   						<th data-options="field:'ltecoverage103rate',align:'center'">5G覆盖率(RSRP>-103<br> and SINR>-3)</th>
		   						<th data-options="field:'ltecoverage101rate',align:'center'">5G覆盖率(RSRP>-101 <br>and SINR>-3)</th>
		   						<th data-options="field:'distancetotal',align:'center'">测试总里程(km)</th>
		   						<th data-options="field:'fgservicedistance',align:'center'">5G测试里程(km)</th>
		   						<th data-options="field:'tdlservicedistance',align:'center'">LTE测试里程(km)</th>
		   						<th data-options="field:'tdsservicedistance',align:'center'">TDS测试里程(km)</th>
		   						<th data-options="field:'gsmservicedistance',align:'center'">GSM测试里程(km)</th>
		   						<th data-options="field:'outofservicedistance',align:'center'">5G脱网里程(km)</th>
		   						<th data-options="field:'testtimetotal',align:'center'">测试总时长(s)</th>
		   						<th data-options="field:'outofservicetime',align:'center'">脱网时长(s)</th>
		   						<th data-options="field:'fgservicetime',align:'center'">5G驻网时长(s)</th>
		   						<th data-options="field:'tdlservicetime',align:'center'">TDL驻网时长(s)</th>
		   						<th data-options="field:'tdsservicetime',align:'center'">TDS驻网时长(s)</th>
		   						<th data-options="field:'gsmservicetime',align:'center'">GSM驻网时长(s)</th>
		   						<th data-options="field:'fgservicerate',align:'center'">5G驻网时长占比</th>
		   						<th data-options="field:'speed',align:'center'">平均车速(km/h)</th>
		   						<th data-options="field:'rsrpweakdistancerate',align:'center'">RSRP连续弱覆盖里程占比</th>
		   						<th data-options="field:'rsrpnodistancerate',align:'center'">RSRP连续无覆盖里程占比</th>
		   						<th data-options="field:'uetxpoweroverdistrate',align:'center'">连续UE高发射功率里程占比</th>
		   					</tr>
		   				</thead>
		   			</table>
				</div>
			</div>
		</div>
		<div title="干扰类指标统计" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table2" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center'">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="10" >干扰类</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'sinraverage',align:'center'">平均SINR</th>
		   						<th data-options="field:'sinredge',align:'center'">边缘SINR</th>
		   						<th data-options="field:'sinrsign3rate',align:'center'">SINR -3以上占比</th>
		   						<th data-options="field:'sinr0rate',align:'center'">SINR 0以上占比</th>
		   						<th data-options="field:'sinrweaksign1distrate',align:'center'">连续SINR质差里程占比<br>(SINR小于-1)(%)</th>
		   						<th data-options="field:'sinrweaksign3distrate',align:'center'">连续SINR质差里程占比<br>(SINR小于-3)(%)</th>
		   						<th data-options="field:'overlapcover3rate',align:'center'">重叠覆盖率(重叠覆盖度<br>>=3)(%)</th>
		   						<th data-options="field:'overlapcover3distrate',align:'center'">重叠覆盖里程占比(重叠覆盖度<br>>=3)(%)</th>
		   						<th data-options="field:'overlapcover4rate',align:'center'">重叠覆盖率(重叠覆盖度<br>=4)(%)</th>
		   						<th data-options="field:'overlapcover4distrate',align:'center'">重叠覆盖里程占比(重叠覆盖度<br>>=4)(%)</th>
		   					</tr>
		   				</thead>
		   			</table>
				</div>
			</div>
		</div>
		<div title="分段占比统计" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table3" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:true,singleSelect:true,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center',width:80">城市</th>
		                        <th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip,width:240">文件名</th>
		   						<th colspan="7" >RSRP区间分布统计占比</th>
		   						<th colspan="7" >SINR区间分布统计占比</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'rsrp110less',align:'center',width:80">小于-110</th>
		   						<th data-options="field:'rsrp110t105',align:'center',width:80">[-110,-105)</th>
		   						<th data-options="field:'rsrp105t100',align:'center',width:80">[-105,-100)</th>
		   						<th data-options="field:'rsrp100t95',align:'center',width:80">[-100,-95)</th>
		   						<th data-options="field:'rsrp95t85',align:'center',width:80">[-95,-85)</th>
		   						<th data-options="field:'rsrp85t75',align:'center',width:80">[-85,-75)</th>
		   						<th data-options="field:'rsrp75t40',align:'center',width:80">[-75,-40)</th>
		   						<th data-options="field:'sinr2less',align:'center',width:80">小于-3</th>
		   						<th data-options="field:'sinr3t0',align:'center',width:80">[-3,0)</th>
		   						<th data-options="field:'sinr0t3',align:'center',width:80">[0,3)</th>
		   						<th data-options="field:'sinr3t6',align:'center',width:80">[3,6)</th>
		   						<th data-options="field:'sinr6t9',align:'center',width:80">[6,9)</th>
		   						<th data-options="field:'sinr9t15',align:'center',width:80">[9,15)</th>
		   						<th data-options="field:'sinr15more',align:'center',width:80">大于15</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
		<div title="数据业务（5G）统计指标" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table4" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center'">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="1" >FTP</th>
		   						<th colspan="28" >FTP下载业务</th>
		   						<th colspan="26" >FTP上传业务</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'totaldroprate',align:'center'">总掉线率</th>
		   						<th data-options="field:'ftpattemptcountdl',align:'center'">FTP下载尝试次数</th>
		   						<th data-options="field:'ftpattemptcountdltfg',align:'center'">5G-FTP下载尝试次数</th>
		   						<th data-options="field:'ftpattemptcountdltdl',align:'center'">TDL-FTP下载尝试次数</th>
		   						<th data-options="field:'ftpattemptcountdltds',align:'center'">TDS-FTP下载尝试次数</th>
		   						<th data-options="field:'ftpattemptcountdlgsm',align:'center'">GSM-FTP下载尝试次数</th>
		   						<th data-options="field:'ftpsuccesscountdl',align:'center'">FTP下载成功次数</th>
		   						<th data-options="field:'ftpsuccesscountdltfg',align:'center'">5G-FTP下载成功次数</th>
		   						<th data-options="field:'ftpsuccesscountdltdl',align:'center'">TDL-FTP下载成功次数</th>
		   						<th data-options="field:'ftpsuccesscountdltds',align:'center'">TDS-FTP下载成功次数</th>
		   						<th data-options="field:'ftpsuccesscountdlgsm',align:'center'">GSM-FTP下载成功次数</th>
		   						<th data-options="field:'ftpsuccessratedl',align:'center'">FTP下载成功率(%)</th>
		   						<th data-options="field:'ftpsuccessratedltdl',align:'center'">TDL-FTP下载成功率(%)</th>
		   						<th data-options="field:'ftpsuccessratedltds',align:'center'">TDS-FTP下载成功率(%)</th>
		   						<th data-options="field:'ftpsuccessratedlgsm',align:'center'">GSM-FTP下载成功率(%)</th>
		   						<th data-options="field:'ftpdropcountdl',align:'center'">FTP下载掉线次数</th>
		   						<th data-options="field:'ftpdropcountdltdl',align:'center'">TDL-FTP下载掉线次数</th>
		   						<th data-options="field:'ftpdropcountdltds',align:'center'">TDS-FTP下载掉线次数</th>
		   						<th data-options="field:'ftpdropcountdlgsm',align:'center'">GSM-FTP下载掉线次数</th>
		   						<th data-options="field:'ftpdropratedl',align:'center'">FTP下载掉线率(%)</th>
		   						<th data-options="field:'ftpdropratedlfg',align:'center'">5G-FTP下载掉线率(%)</th>
		   						<th data-options="field:'ftpdropratedltdl',align:'center'">TDL-FTP下载掉线率(%)</th>
		   						<th data-options="field:'ftpdropratedltds',align:'center'">TDS-FTP下载掉线率(%)</th>
		   						<th data-options="field:'ftpdropratedlgsm',align:'center'">GSM-FTP下载掉线率(%)</th>
		   						<th data-options="field:'ftprectotal2dropdl',align:'center'">FTP下载数据掉线比(Kb/次)</th>
		   						<th data-options="field:'ftprectotal2dropdlfg',align:'center'">5G-FTP下载数据掉线比(Kb/次)</th>
		   						<th data-options="field:'ftprectotal2dropdltdl',align:'center'">TDL-FTP下载数据掉线比(Kb/次)</th>
		   						<th data-options="field:'ftprectotal2dropdltds',align:'center'">TDS-FTP下载数据掉线比(Kb/次)</th>
		   						<th data-options="field:'ftprectotal2dropdlgsm',align:'center'">GSM-FTP下载数据掉线比(Kb/次)</th>
		   						<th data-options="field:'ftpattemptcountul',align:'center'">FTP上传尝试次数</th>
		   						<th data-options="field:'ftpattemptcountulfg',align:'center'">5G-FTP上传尝试次数</th>
		   						<th data-options="field:'ftpattemptcountultdl',align:'center'">TDL-FTP上传尝试次数</th>
		   						<th data-options="field:'ftpattemptcountultds',align:'center'">TDS-FTP上传尝试次数</th>
		   						<th data-options="field:'ftpattemptcountulgsm',align:'center'">GSM-FTP上传尝试次数</th>
		   						<th data-options="field:'ftpsuccesscountul',align:'center'">FTP上传成功次数</th>
		   						<th data-options="field:'ftpsuccesscountulfg',align:'center'">5G-FTP上传成功次数</th>
		   						<th data-options="field:'ftpsuccesscountultdl',align:'center'">TDL-FTP上传成功次数</th>
		   						<th data-options="field:'ftpsuccesscountultds',align:'center'">TDS-FTP上传成功次数</th>
		   						<th data-options="field:'ftpsuccesscountulgsm',align:'center'">GSM-FTP上传成功次数</th>
		   						<th data-options="field:'ftpsuccessrateul',align:'center'">FTP上传成功率(%)</th>
		   						<th data-options="field:'ftpsuccessrateultdl',align:'center'">TDL-FTP上传成功率(%)</th>
		   						<th data-options="field:'ftpsuccessrateultds',align:'center'">TDS-FTP上传成功率(%)</th>
		   						<th data-options="field:'ftpsuccessrateulgsm',align:'center'">GSM-FTP上传成功率(%)</th>
		   						<th data-options="field:'ftpdropcountul',align:'center'">FTP上传掉线次数</th>
		   						<th data-options="field:'ftpdropcountulfg',align:'center'">5G-FTP上传掉线次数</th>
		   						<th data-options="field:'ftpdropcountultdl',align:'center'">TDL-FTP上传掉线次数</th>
		   						<th data-options="field:'ftpdropcountultds',align:'center'">TDS-FTP上传掉线次数</th>
		   						<th data-options="field:'ftpdropcountulgsm',align:'center'">GSM-FTP上传掉线次数</th>
		   						<th data-options="field:'ftpdroprateul',align:'center'">FTP上传掉线率(%)</th>
		   						<th data-options="field:'ftpdroprateultdl',align:'center'">TDL-FTP上传掉线率(%)</th>
		   						<th data-options="field:'ftpdroprateultds',align:'center'">TDS-FTP上传掉线率(%)</th>
		   						<th data-options="field:'ftpdroprateuulgsm',align:'center'">GSM-FTP上传掉线率(%)</th>
		   						<th data-options="field:'ftprectotal2dropul',align:'center'">FTP上传数据掉线比(Kb/次)</th>
		   						<th data-options="field:'ftprectotal2dropulfg',align:'center'">5G-FTP上传数据掉线比(Kb/次)</th>
		   						<th data-options="field:'ftprectotal2dropultdl',align:'center'">TDL-FTP上传数据掉线比(Kb/次)</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
		<div title="波束类统计指标" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table5" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center'">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="12" >波束类统计指标</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'maxbeamrsrpaverage',align:'center'">最强波束RSRP均值</th>
		   						<th data-options="field:'maxbeamrsrqaverage',align:'center'">最强波束RSRQ均值</th>
		   						<th data-options="field:'maxbeamsinraverage',align:'center'">最强波束SINR均值</th>
		   						<th data-options="field:'nextbeamrsrpaverage',align:'center'">次强波束RSRP均值</th>
		   						<th data-options="field:'nextbeamrsrqaverage',align:'center'">次强波束RSRQ均值</th>
		   						<th data-options="field:'nextbeamsinraverage',align:'center'">次强波束SINR均值</th>
		   						<th data-options="field:'minbeamrsrpaverage',align:'center'">最弱波束RSRP均值</th>
		   						<th data-options="field:'minbeamrsrqaverage',align:'center'">最弱波束RSRQ均值</th>
		   						<th data-options="field:'minbeamsinraverage',align:'center'">最弱波束SINR均值</th>
		   						<th data-options="field:'maxbeamrate',align:'center'">接入最强波束采样点占比</th>
		   						<th data-options="field:'fourbeamrate',align:'center'">四波束占比</th>
		   						<th data-options="field:'eightbeamrate',align:'center'">八波束占比</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
	</div>
</body>
</html>
