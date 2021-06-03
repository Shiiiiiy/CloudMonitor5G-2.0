<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>指标概览</title>

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
				text:'统计',
				iconCls:'icon-excel',
				handler:function(){
					goToPage("${pageContext.request.contextPath}/report/downloadOneSheetLteDataTotalExcel.action?sheetName="+sheetName+"");
				}
			},{
				iconCls:'icon-batchexcel',
				text:'下载',
				handler:function(){
					goToPage('${pageContext.request.contextPath}/report/downloadLteDataTotalExcel.action');
				}
			}]
		});
	});
	//统计按钮
	function  statis(){
		$.post('${pageContext.request.contextPath}/floorReport/getFloorInfo.action', {floorName : floorName}, 
				function(result) {
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$("#moveDataTable").datagrid('loadData',result.info.moveData);
						$("#moveVoiceTable").datagrid('loadData',result.info.moveVoice);
						$("#moveVideoTable").datagrid('loadData',result.info.moveVideo);
						$("#linkDataTable").datagrid('loadData',result.info.linkData);
						$("#linkVoiceTable").datagrid('loadData',result.info.linkVoice);
						$("#linkVideoTable").datagrid('loadData',result.info.linkVideo);
						$("#telecomDataTable").datagrid('loadData',result.info.telecomData);
						$("#telecomVoiceTable").datagrid('loadData',result.info.telecomVoice);
						$("#telecomVideoTable").datagrid('loadData',result.info.telecomVideo);
					}
				},
			'json');
		goToPage("${pageContext.request.contextPath}/floorReport/goAddInfo.action");
	}
	//下载按钮
	function download(){
		goToPage("${pageContext.request.contextPath}/floorReport/downloadExcel.action");
	}
	</script>
  </head>
  <body style="width: 100%;height: 100%;margin: 0 auto;padding: 0 auto;list-style:none;overflow:hidden;">
    	<div id="tt"class="easyui-tabs" style="width:100%;height:100%;"data-options="fit:true,tools:'#ss'">
			<div title="移动" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
  					<table id="moveDataTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="10" >数据</th>
	    					</tr>
	    					<tr>
	    						<tr>
	    						<th data-options="field:'FTPAttemptCountDL',width:100,align:'center'">FTP下载尝试次数</th>
	    						<th data-options="field:'FTPsuccessRateDL',width:100,align:'center'">FTP下载成功率(%)</th>
	    						<th data-options="field:'FTPDropRateDL',width:100,align:'center'">FTP下载掉线率(%)</th>
	    						<th data-options="field:'FTPRecvTotalsuccessDL',width:100,align:'center'">应用层下载流<br>量(不含掉线)(KB)</th>
	    						<th data-options="field:'FTPAttemptCountUL',width:100,align:'center'">FTP上传尝试次数</th>
	    						<th data-options="field:'FTPsuccessRateUL',width:100,align:'center'">FTP上传成功率(%)</th>
	    						<th data-options="field:'FTPDropRateUL',width:100,align:'center'">FTP上传掉线率(%)</th>
	    						<th data-options="field:'FTPULThrputsuccess',width:120,align:'center'">应用层平均上传<br>速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'RSRPAverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'LTECoverage110Rate',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
	    			<table id="moveVoiceTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="10" >语音</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'CallCompleteRate',width:120,align:'center'">全程呼叫成功率</th>
	    						<th data-options="field:'CallConnectRate',width:60,align:'center'">接通率</th>
	    						<th data-options="field:'CallDropRate',width:60,align:'center'">掉话率</th>
	    						<th data-options="field:'VoLTECallCompleteRate',width:140,align:'center'">VOLTE全程呼叫成功率</th>
	    						<th data-options="field:'VoLTECallConnectRate',width:100,align:'center'">VOLTE接通率</th>
	    						<th data-options="field:'VoLTECallDropRate',width:100,align:'center'">VOLTE掉话率</th>
	    						<th data-options="field:'VoLTEMOSAverage',width:100,align:'center'">VoLTE平均MOS</th>
	    						<th data-options="field:'PSMOSOver358Rate',width:120,align:'center'">VoLTE MOS>=3.5占比</th>
	    						<th data-options="field:'RSRPAverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'LTECoverage110Rate',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
	    			<table id="moveVideoTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="9" >视频</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫请求次数</th>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫建立次数</th>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫建立成功率</th>
	    						<th data-options="field:'know',width:140,align:'center'">视频被叫建立次数</th>
	    						<th data-options="field:'know',width:90,align:'center'">视频掉线率</th>
	    						<th data-options="field:'know',width:100,align:'center'">VoLTE平均MOS</th>
	    						<th data-options="field:'know',width:100,align:'center'">VoLTE MOS>=3.5占比</th>
	    						<th data-options="field:'know',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'know',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
			</div>
			<div title="联通" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<table id="linkDataTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="10" >数据</th>
	    					</tr>
	    					<tr>
	    						<tr>
	    						<th data-options="field:'FTPAttemptCountDL',width:100,align:'center'">FTP下载尝试次数</th>
	    						<th data-options="field:'FTPsuccessRateDL',width:100,align:'center'">FTP下载成功率(%)</th>
	    						<th data-options="field:'FTPDropRateDL',width:100,align:'center'">FTP下载掉线率(%)</th>
	    						<th data-options="field:'FTPRecvTotalsuccessDL',width:100,align:'center'">应用层下载流<br>量(不含掉线)(KB)</th>
	    						<th data-options="field:'FTPAttemptCountUL',width:100,align:'center'">FTP上传尝试次数</th>
	    						<th data-options="field:'FTPsuccessRateUL',width:100,align:'center'">FTP上传成功率(%)</th>
	    						<th data-options="field:'FTPDropRateUL',width:100,align:'center'">FTP上传掉线率(%)</th>
	    						<th data-options="field:'FTPULThrputsuccess',width:120,align:'center'">应用层平均上传<br>速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'RSRPAverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'LTECoverage110Rate',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
	    			<table id="linkVoiceTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="10" >语音</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'CallCompleteRate',width:120,align:'center'">全程呼叫成功率</th>
	    						<th data-options="field:'CallConnectRate',width:60,align:'center'">接通率</th>
	    						<th data-options="field:'CallDropRate',width:60,align:'center'">掉话率</th>
	    						<th data-options="field:'VoLTECallCompleteRate',width:140,align:'center'">VOLTE全程呼叫成功率</th>
	    						<th data-options="field:'VoLTECallConnectRate',width:100,align:'center'">VOLTE接通率</th>
	    						<th data-options="field:'VoLTECallDropRate',width:100,align:'center'">VOLTE掉话率</th>
	    						<th data-options="field:'VoLTEMOSAverage',width:100,align:'center'">VoLTE平均MOS</th>
	    						<th data-options="field:'PSMOSOver358Rate',width:120,align:'center'">VoLTE MOS>=3.5占比</th>
	    						<th data-options="field:'RSRPAverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'LTECoverage110Rate',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
	    			<table id="linkVideoTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="9" >视频</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫请求次数</th>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫建立次数</th>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫建立成功率</th>
	    						<th data-options="field:'know',width:140,align:'center'">视频被叫建立次数</th>
	    						<th data-options="field:'know',width:90,align:'center'">视频掉线率</th>
	    						<th data-options="field:'know',width:100,align:'center'">VoLTE平均MOS</th>
	    						<th data-options="field:'know',width:100,align:'center'">VoLTE MOS>=3.5占比</th>
	    						<th data-options="field:'know',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'know',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
		    	
			</div>
			<div title="电信" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<table id="telecomDataTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="10" >数据</th>
	    					</tr>
	    					<tr>
	    						<tr>
	    						<th data-options="field:'FTPAttemptCountDL',width:100,align:'center'">FTP下载尝试次数</th>
	    						<th data-options="field:'FTPsuccessRateDL',width:100,align:'center'">FTP下载成功率(%)</th>
	    						<th data-options="field:'FTPDropRateDL',width:100,align:'center'">FTP下载掉线率(%)</th>
	    						<th data-options="field:'FTPRecvTotalsuccessDL',width:100,align:'center'">应用层下载流<br>量(不含掉线)(KB)</th>
	    						<th data-options="field:'FTPAttemptCountUL',width:100,align:'center'">FTP上传尝试次数</th>
	    						<th data-options="field:'FTPsuccessRateUL',width:100,align:'center'">FTP上传成功率(%)</th>
	    						<th data-options="field:'FTPDropRateUL',width:100,align:'center'">FTP上传掉线率(%)</th>
	    						<th data-options="field:'FTPULThrputsuccess',width:120,align:'center'">应用层平均上传<br>速率(不含掉线)(kbps)</th>
	    						<th data-options="field:'RSRPAverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'LTECoverage110Rate',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
	    			<table id="telecomVoiceTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="10" >语音</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'CallCompleteRate',width:120,align:'center'">全程呼叫成功率</th>
	    						<th data-options="field:'CallConnectRate',width:60,align:'center'">接通率</th>
	    						<th data-options="field:'CallDropRate',width:60,align:'center'">掉话率</th>
	    						<th data-options="field:'VoLTECallCompleteRate',width:140,align:'center'">VOLTE全程呼叫成功率</th>
	    						<th data-options="field:'VoLTECallConnectRate',width:100,align:'center'">VOLTE接通率</th>
	    						<th data-options="field:'VoLTECallDropRate',width:100,align:'center'">VOLTE掉话率</th>
	    						<th data-options="field:'VoLTEMOSAverage',width:100,align:'center'">VoLTE平均MOS</th>
	    						<th data-options="field:'PSMOSOver358Rate',width:120,align:'center'">VoLTE MOS>=3.5占比</th>
	    						<th data-options="field:'RSRPAverage',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'LTECoverage110Rate',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
	    			<table id="telecomVideoTable" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true"  >
	    				<thead>
	    					<tr>
	    						<th colspan="9" >视频</th>
	    					</tr>
	    					<tr>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫请求次数</th>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫建立次数</th>
	    						<th data-options="field:'know',width:150,align:'center'">视频主叫建立成功率</th>
	    						<th data-options="field:'know',width:140,align:'center'">视频被叫建立次数</th>
	    						<th data-options="field:'know',width:90,align:'center'">视频掉线率</th>
	    						<th data-options="field:'know',width:100,align:'center'">VoLTE平均MOS</th>
	    						<th data-options="field:'know',width:100,align:'center'">VoLTE MOS>=3.5占比</th>
	    						<th data-options="field:'know',width:80,align:'center'">平均RSRP</th>
	    						<th data-options="field:'know',width:80,align:'center'">LTE覆盖率</th>
	    					</tr>
	    				</thead>
	    				<tbody>
	    				<tr></tr>
	    				</tbody>
	    			</table>
			</div>
			</div>
			<div id="ss">
		    	<a class="icon-large-chart" title="统计" onclick="statis()"  >统计</a>
		    	<a class="icon-download" title="下载"  onclick="download()" >下载</a>
        	</div>

</body>
</html>
