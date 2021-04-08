<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>5G异常事件报表</title>

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
					goToPage("${pageContext.request.contextPath}/report5g/downloadOneSheetFgEeTotalExcel.action?sheetName="+sheetName+"");
				}
			},{
				iconCls:'icon-batchexcel',
				text:'批量导出',
				handler:function(){
					goToPage('${pageContext.request.contextPath}/report5g/downloadFgEeTotalExcel.action');
				}
			}]
		});
		initTable();
	});
	var sheetName="接入类异常事件";
	function initTable(){
		$.post("${pageContext.request.contextPath}/report5g/quaryEeKpi.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#Table1").datagrid('loadData',result.insertKpi);
					$("#Table2").datagrid('loadData',result.moverKpi);
					$("#Table3").datagrid('loadData',result.voiceEeKpi);
					$("#Table4").datagrid('loadData',result.pduKpi);
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
		<div title="接入类异常事件" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table1" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:true,singleSelect:true,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center',width:80">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip,width:240">文件名</th>
		   						<th colspan="6">接入类</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'rrcsetupsuccesrate',align:'center',width:80">RRC接入成功率</th>
		   						<th data-options="field:'rrcsetupreqnum',align:'center',width:80">RRC接入尝试次数</th>
		   						<th data-options="field:'rrcsetupsuccnum',align:'center',width:80">RRC接入成功次数	</th>
		   						<th data-options="field:'rrcresumesuccessrate',align:'center',width:80">RRC恢复成功率</th>
		   						<th data-options="field:'rrcresumereqnum',align:'center',width:80">RRC恢复尝试次数</th>
		   						<th data-options="field:'rrcresumesuccnum',align:'center',width:80">RRC恢复成功次数</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
		<div title="移动类异常事件" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table2" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:true,singleSelect:true,fit:true"   >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center',width:80">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip,width:240">文件名</th>
		   						<th colspan="6">移动类</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'rnausuccessrate',align:'center',width:80">RNA更新成功率</th>
		   						<th data-options="field:'rnaureqnum',align:'center',width:80">RNA更新尝试次数</th>
		   						<th data-options="field:'rnausuccnum',align:'center',width:80">RNA更新成功次数</th>
		   						<th data-options="field:'hosuccessratio',align:'center',width:80">切换成功率</th>
		   						<th data-options="field:'hoattemptcount',align:'center',width:80">切换尝试次数</th>
		   						<th data-options="field:'hosucccount',align:'center',width:80">切换成功次数</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
		<div title="语音业务（4G）异常事件统计指标" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table3" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true,fit:true">
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center'">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="18" >语音业务（4G）异常事件统计指标</th>
		   					</tr>
		                    <tr>
		   						<th data-options="field:'moormt',align:'center'">主被叫</th>
		   						<th data-options="field:'cscallcompleterate',align:'center'">CS域全程呼叫成功率</th>
		   						<th data-options="field:'cscallconnectrate',align:'center'">CS域接通率</th>
		   						<th data-options="field:'cscalldroprate',align:'center'">CS域掉话率</th>
		   						<th data-options="field:'cscallattemptcount',align:'center'">CS域试呼次数</th>
		   						<th data-options="field:'cscallconnectcount',align:'center'">CS域接通次数</th>
		   						<th data-options="field:'cscalldropcount',align:'center'">CS域掉话次数</th>
		   						<th data-options="field:'cscallestablishdelay',align:'center'">CS呼叫建立时延(s)</th>
		   						<th data-options="field:'csfb2gsmsuccessrate',align:'center'">CSFB回落GSM成功比例(%)</th>
		   						<th data-options="field:'csfb2gsmdelay',align:'center'">CSFB回落GSM时延(s)</th>
		   						<th data-options="field:'csfb2tdssuccessrate',align:'center'">CSFB回落TD比例(%)</th>
		   						<th data-options="field:'csfb2tdsdelay',align:'center'">CSFB回落TD平均时延(s)</th>
		   						<th data-options="field:'gsmreturntdlsuccessrate',align:'center'">GSM成功返回TD-LTE比例(%)</th>
		   						<th data-options="field:'gsmreturntdldelay',align:'center'">GSM返回TD-LTE平均时延(s)</th>
		   						<th data-options="field:'tdsreturntdlrate',align:'center'">TD返回TD-LTE比例(%)</th>
		   						<th data-options="field:'tdsreturntdldelay',align:'center'">TD返回TD-LTE平均时延(s)</th>
		   						<th data-options="field:'rxquality04rate',align:'center'">Rxquality0-4级占比</th>
		   						<th data-options="field:'csmosaverage',align:'center'">CS域MOS均值</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
		<div title="会话类异常事件" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="margin:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table4" class="easyui-datagrid"data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true,fit:true">
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogTerminalGroup',align:'center'">城市</th>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="12" >会话类异常事件统计指标</th>
		   					</tr>
		                    <tr>
		   						<th data-options="field:'pdubuildreqnum',align:'center'">PDU会话建立请求次数</th>
		   						<th data-options="field:'pdubuildsuccnum',align:'center'">PDU会话建立成功次数</th>
		   						<th data-options="field:'pdubuildbadrate',align:'center'">PDU会话建立失败率</th>
		   						<th data-options="field:'pdurebuildreqnum',align:'center'">PDU会话重建请求次数</th>
		   						<th data-options="field:'pdurebuildsuccnum',align:'center'">PDU会话重建成功次数</th>
		   						<th data-options="field:'pdurebuildbadrate',align:'center'">PDU会话重建失败率</th>
		   						<th data-options="field:'pduureqnum',align:'center'">PDU会话修改请求次数</th>
		   						<th data-options="field:'pduusuccnum',align:'center'">PDU会话修改成功次数</th>
		   						<th data-options="field:'pduubadrate',align:'center'">PDU会话修改失败率</th>
		   						<th data-options="field:'pdureleasereqnum',align:'center'">PDU会话释放请求次数</th>
		   						<th data-options="field:'pdureleasesuccnum',align:'center'">PDU会话释放成功次数</th>
		   						<th data-options="field:'pdureleasebadrate',align:'center'">PDU会话释放失败率</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
	</div>
</body>
</html>
