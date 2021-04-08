<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>NSA指标报表</title>

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
					goToPage("${pageContext.request.contextPath}/report5g/downloadOneSheetNsaTotal.action?sheetName="+sheetName);
				}
			},{
				iconCls:'icon-batchexcel',
				text:'批量导出',
				handler:function(){
					goToPage('${pageContext.request.contextPath}/report5g/downloadNsaTotal.action');
				}
			}]
		});
		initTable();
	});
	var sheetName="指标汇总";
	function initTable(){
		$.post("${pageContext.request.contextPath}/report5g/quaryNsaKpi.action",{},
			function(result){
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg,'error');
				} else {
					$("#Table1").datagrid('loadData',result.summaryKpi);
					$("#Table2").datagrid('loadData',result.businessKpi);
					$("#Table3").datagrid('loadData',result.coverKpi);
					$("#Table4").datagrid('loadData',result.mobileKpi);
					$("#Table5").datagrid('loadData',result.accessKpi);
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
   		<div title="指标汇总" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
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
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="11" >测试概要</th>
		   						<th colspan="7" >测量指标</th>
		   						<th colspan="8" >FTP Download</th>
		   						<th colspan="8" >FTP Upload</th>
		   						<th colspan="4" >Ping指标</th>
		   						<th colspan="12" >上行速率</th>
		   						<th colspan="12" >下行速率</th>
		   						<th colspan="8" >移动性指标</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'testtimetotal',align:'center'">测试总时长(s)</th>
								<th data-options="field:'testdistancetotal',align:'center'">测试总里程(m)</th>
								<th data-options="field:'testspeed',align:'center'">测试车速(km/h)</th>
								<th data-options="field:'ltetestdistance',align:'center'">4G测试里程(m)</th>
								<th data-options="field:'nrtestdistance',align:'center'">5G测试里程(m)</th>
								<th data-options="field:'nrtestdistancerate',align:'center'">5G测试里程占比(%)</th>
								<th data-options="field:'ltetestdistancerate',align:'center'">4G测试里程占比(%)</th>
								<th data-options="field:'nroccupytime',align:'center'">5G占用时长(s)</th>
								<th data-options="field:'lteoccupytime',align:'center'">4G占用时长(s)</th>
								<th data-options="field:'nroccupytimerate',align:'center'">5G时长占比(%)</th>
								<th data-options="field:'lteoccupytimerate',align:'center'">4G时长占比(%)</th>
								
								<th data-options="field:'lteavgrsrp',align:'center'">LTE平均RSRP</th>
								<th data-options="field:'lteavgsinr',align:'center'">LTE平均SINR</th>
								<th data-options="field:'lteavguepuschtxpower',align:'center'">LTE平均UE PUSCH TxPower</th>
								<th data-options="field:'lteavgssbrsrp',align:'center'">NR平均SSB RSRP</th>
								<th data-options="field:'nravgdlfirstbler',align:'center'">NR平均下行首传bler</th>
								<th data-options="field:'nravgssbrsrp',align:'center'">NR平均SSB SINR</th>
								<th data-options="field:'nravguepuschtxpower',align:'center'">NR平均UE TxPower</th>
								
								<th data-options="field:'testcountdl',align:'center'">下载尝试次数</th>
								<th data-options="field:'successratedl',align:'center'">下载成功率(100%)</th>
								<th data-options="field:'indropavgdlrate',align:'center'">平均下载速率(Mbps)(包含掉线)</th>
								<th data-options="field:'notindropavgdlrate',align:'center'">平均下载速率(Mbps)(不包含掉线)</th>
								<th data-options="field:'notindropavgdlbitperrb',align:'center'">每RB平均下载量（不含掉线）(bit/RB)</th>
								<th data-options="field:'indropavgdlbitperrb',align:'center'">每RB平均下载量（含掉线）(bit/RB)</th>
								<th data-options="field:'dlrate40lesssampoint',align:'center'">下载速率(Mbps)<40占比（采样点）</th>
								<th data-options="field:'dlrate1024moresampoint',align:'center'">下载速率(Mbps)>1024占比（采样点）</th>
								
								<th data-options="field:'testcountul',align:'center'">上传尝试次数</th>
								<th data-options="field:'successrateul',align:'center'">上传成功率(100%)</th>
								<th data-options="field:'indropavgulrate',align:'center'">平均上传速率(Mbps)(包含掉线)</th>
								<th data-options="field:'notindropavgulrate',align:'center'">平均上传速率(Mbps)(不包含掉线)</th>
								<th data-options="field:'notindropavgulbitperrb',align:'center'">每RB平均上传量（不含掉线）(bit/RB)</th>
								<th data-options="field:'indropavgulbitperrb',align:'center'">每RB平均上传量（含掉线）(bit/RB)</th>
								<th data-options="field:'ulrate12lesssampoint',align:'center'">上传速率(Mbps)<12占比（采样点）</th>
								<th data-options="field:'ulrate100moresampoint',align:'center'">上传速率(Mbps>100占比（采样点）</th>
								
								<th data-options="field:'pingsuccessrate',align:'center'">Ping包成功率(%)</th>
								<th data-options="field:'pingavgtimedelay',align:'center'">Ping包平均时延(ms)</th>
								<th data-options="field:'pingmaxtimedelay',align:'center'">Ping包最大时延(ms)</th>
								<th data-options="field:'pingmintimedelay',align:'center'">Ping包最小时延(ms)</th>
								
								<th data-options="field:'lteupspeedpdcp',align:'center'">LTE上行PDCP速率(Mbps)</th>
								<th data-options="field:'nrupspeedpdcp',align:'center'">NR上行PDCP速率(Mbps)</th>
								<th data-options="field:'upspeedpdcp',align:'center'">上行PDCP速率(Mbps)</th>
								<th data-options="field:'lteupspeedrlc',align:'center'">LTE上行RLC速率(Mbps)</th>
								<th data-options="field:'nrupspeedrlc',align:'center'">NR上行RLC速率(Mbps)</th>
								<th data-options="field:'upspeedrlc',align:'center'">上行RLC速率(Mbps)</th>
								<th data-options="field:'lteupspeedmac',align:'center'">LTE上行MAC速率(Mbps)</th>
								<th data-options="field:'nrupspeedmac',align:'center'">NR上行MAC速率(Mbps)</th>
								<th data-options="field:'upspeedmac',align:'center'">上行MAC速率(Mbps)</th>
								<th data-options="field:'lteupspeedphy',align:'center'">LTE上行PHY速率(Mbps)</th>
								<th data-options="field:'nrupspeedphy',align:'center'">NR上行PHY速率(Mbps)</th>
								<th data-options="field:'upspeedphy',align:'center'">总上行PHY速率(Mbps)</th>
								
								<th data-options="field:'ltedownspeedpdcp',align:'center'">LTE下行PDCP速率(Mbps)</th>
								<th data-options="field:'nrdownspeedpdcp',align:'center'">NR下行PDCP速率(Mbps)</th>
								<th data-options="field:'downspeedpdcp',align:'center'">下行PDCP速率(Mbps)</th>
								<th data-options="field:'ltedownspeedrlc',align:'center'">LTE下行RLC速率(Mbps)</th>
								<th data-options="field:'nrdownspeedrlc',align:'center'">NR下行RLC速率(Mbps)</th>
								<th data-options="field:'downspeedrlc',align:'center'">下行RLC速率(Mbps)</th>
								<th data-options="field:'ltedownspeedmac',align:'center'">LTE下行MAC速率(Mbps)</th>
								<th data-options="field:'nrdownspeedmac',align:'center'">NR下行MAC速率(Mbps)</th>
								<th data-options="field:'downspeedmac',align:'center'">下行MAC速率(Mbps)</th>
								<th data-options="field:'ltedownspeedphy',align:'center'">LTE下行PHY速率(Mbps)</th>
								<th data-options="field:'nrdownspeedphy',align:'center'">NR下行PHY速率(Mbps)</th>
								<th data-options="field:'downspeedphy',align:'center'">总下行PHY速率(Mbps)</th>
								
								<th data-options="field:'attachscgaccessrate',align:'center'">Attach-SCG接入成功率(%)</th>
								<th data-options="field:'attachscgaccesstimedelay',align:'center'">Attach-SCG接入时延(ms)</th>
								<th data-options="field:'nrtimeswitchfailcount',align:'center'">NR切换失败次数</th>
								<th data-options="field:'nrtimeswitchfailrate',align:'center'">NR持续时长切换失败比(s)</th>
								<th data-options="field:'seconnodechasucrate',align:'center'">辅节点变更成功率(%)</th>
								<th data-options="field:'seconnodechatimedelay',align:'center'">辅节点变更时延(ms)</th>
								<th data-options="field:'lteswitchrate',align:'center'">LTE切换比例(%)</th>
								<th data-options="field:'nrswitchrate',align:'center'">NR切换比例(%)</th>
		   					</tr>
		   				</thead>
		   			</table>
				</div>
			</div>
		</div>
		<div title="业务类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="padding:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table2" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="3" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="11" >FTP下载</th>
		   						<th colspan="11" >FTP上传</th>
		   						<th colspan="11" >NR下行指标</th>
		   						<th colspan="8" >NR上行指标</th>
		   						<th colspan="10" >LTE下行指标</th>
		   						<th colspan="8" >LTE上行指标</th>
		   						<th colspan="6" >PING业务类</th>
		   					</tr>
		   					<tr>
		   						<th colspan="3" >汇总</th>
		   						<th colspan="2" >峰值速率</th>
		   						<th colspan="5" >平均速率(成功)</th>
		   						<th colspan="1" >平均速率(成功+掉线)</th>
		   						<th colspan="3" >汇总</th>
		   						<th colspan="2" >峰值速率</th>
		   						<th colspan="5" >平均速率(成功)</th>
		   						<th colspan="1" >平均速率(成功+掉线)</th>
		   						<th colspan="3" >BLER</th>
		   						<th colspan="6" >MCS</th>
		   						<th colspan="2" >调度</th>
		   						<th colspan="3" >BLER</th>
		   						<th colspan="3" >MCS</th>
		   						<th colspan="2" >调度</th>
		   						<th colspan="3" >BLER</th>
		   						<th colspan="5" >MCS</th>
		   						<th colspan="2" >调度</th>
		   						<th colspan="3" >BLER</th>
		   						<th colspan="3" >MCS</th>
		   						<th colspan="2" >调度</th>
		   						<th rowspan="2" data-options="field:'pingrequestcount',align:'center'">Ping业务请求次数</th>
								<th rowspan="2" data-options="field:'pingsuccesscount',align:'center'">Ping业务成功次数</th>
								<th rowspan="2" data-options="field:'pingsuccessrate',align:'center'">Ping包成功率(%)</th>
								<th rowspan="2" data-options="field:'pingavgtimedelay',align:'center'">Ping包平均时延(ms)</th>
								<th rowspan="2" data-options="field:'pingmaxtimedelay',align:'center'">Ping包最大时延(ms)</th>
								<th rowspan="2" data-options="field:'pingmintimedelay',align:'center'">Ping包最小时延(ms)</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'testcountdl',align:'center'">下载尝试次数</th>	
								<th data-options="field:'successcountdl',align:'center'">下载成功次数</th>	
								<th data-options="field:'successratedl',align:'center'">下载成功率(100%)</th>	
								
								<th data-options="field:'ftpmaxspeeddl',align:'center'">FTP峰值速率(Mbps)</th>	
								<th data-options="field:'pdcpmaxspeeddl',align:'center'">PDCP峰值速率(Mbps)</th>	
								
								<th data-options="field:'dlspeedftp',align:'center'">下载FTP速率(Mbps)</th>	
								<th data-options="field:'downspeedpdcp',align:'center'">下行PDCP速率(Mbps)</th>	
								<th data-options="field:'downspeedrlc',align:'center'">下行RLC速率(Mbps)</th>	
								<th data-options="field:'downspeedmac',align:'center'">下行MAC速率(Mbps)</th>	
								<th data-options="field:'downspeedphy',align:'center'">下行PHY速率(Mbps)</th>	
								
								<th data-options="field:'dlftprate',align:'center'">下载速率(Mbps)</th>	
								
								<th data-options="field:'testcountul',align:'center'">上传尝试次数</th>	
								<th data-options="field:'successcountul',align:'center'">上传成功次数</th>	
								<th data-options="field:'successrateul',align:'center'">上传成功率(100%)</th>	
								
								<th data-options="field:'ftpmaxspeedul',align:'center'">FTP峰值速率(Mbps)</th>	
								<th data-options="field:'pdcpmaxspeedul',align:'center'">PDCP峰值速率(Mbps)</th>
								
								<th data-options="field:'ulspeedftp',align:'center'">上传FTP速率(Mbps)</th>	
								<th data-options="field:'upspeedpdcp',align:'center'">上行PDCP速率(Mbps)</th>	
								<th data-options="field:'upspeedrlc',align:'center'">上行RLC速率(Mbps)</th>	
								<th data-options="field:'upspeedmac',align:'center'">上行MAC速率(Mbps)</th>	
								<th data-options="field:'upspeedphy',align:'center'">上行PHY速率(Mbps)</th>	
								
								<th data-options="field:'upftprate',align:'center'">上传FTP速率(Mbps)</th>
								
								<th data-options="field:'nrdownibler',align:'center'">下行iBLER(%)</th>
								<th data-options="field:'nrdownbler',align:'center'">下行BLER(%)</th>	
								<th data-options="field:'nrdownrbler',align:'center'">下行rBLER(%)</th>	
								
								<th data-options="field:'nrdowncodeword0avg',align:'center'">下行码字0MCS Avg</th>	
								<th data-options="field:'nrdowncodeword0most',align:'center'">下行码字0MCS most</th>	
								<th data-options="field:'nrdowncodeword0best',align:'center'">下行码字0MCS Best</th>	
								<th data-options="field:'nrdowncodeword1avg',align:'center'">下行码字1MCS Avg</th>	
								<th data-options="field:'nrdowncodeword1most',align:'center'">下行码字1MCS most</th>	
								<th data-options="field:'nrdowncodeword1best',align:'center'">下行码字1MCS best</th>	
								
								<th data-options="field:'nrdownavgrb',align:'center'">下行平均RB</th>	
								<th data-options="field:'nrdowngrantnum',align:'center'">下行Grant Num</th>	
								
								<th data-options="field:'nrupibler',align:'center'">上行iBLER(%)</th>	
								<th data-options="field:'nrupbler',align:'center'">上行BLER(%)</th>	
								<th data-options="field:'nruprbler',align:'center'">上行rBLER(%)</th>
								
								<th data-options="field:'nrupmcsavg',align:'center'">上行MCSAvg</th>	
								<th data-options="field:'nrupmcsmost',align:'center'">上行MCSmost</th>	
								<th data-options="field:'nrupmcsbest',align:'center'">上行MCSBest</th>
								
								<th data-options="field:'nrupavgrb',align:'center'">上行平均RB</th>	
								<th data-options="field:'nrupgrantcount',align:'center'">上行Grant Count</th>	
								
								<th data-options="field:'ltedownibler',align:'center'">下行iBLER(%)</th>	
								<th data-options="field:'ltedownbler',align:'center'">下行BLER(%)</th>	
								<th data-options="field:'ltedownrbler',align:'center'">下行rBLER(%)</th>	
								
								<th data-options="field:'ltedowncodeword0avg',align:'center'">码字0下行MCS Avg</th>	
								<th data-options="field:'ltedowncodeword0most',align:'center'">码字0下行MCS Best</th>	
								<th data-options="field:'ltedowncodeword0best',align:'center'">码字0下行MCS most</th>	
								<th data-options="field:'ltedowncodeword1best',align:'center'">码字1下行MCS Best</th>	
								<th data-options="field:'ltedowncodeword1most',align:'center'">码字1下行MCS most</th>	
								
								<th data-options="field:'ltedownavgrb',align:'center'">下行平均RB</th>	
								<th data-options="field:'ltedowngrantnum',align:'center'">下行Grant Num</th>	
								
								<th data-options="field:'lteupibler',align:'center'">上行iBLER(%)</th>	
								<th data-options="field:'lteupbler',align:'center'">上行BLER(%)</th>	
								<th data-options="field:'lteuprbler',align:'center'">上行rBLER(%)</th>
								
								<th data-options="field:'lteupmcsavg',align:'center'">上行MCS Avg</th>	
								<th data-options="field:'lteupmcsbest',align:'center'">上行MCS Best</th>	
								<th data-options="field:'lteupmcsmost',align:'center'">上行MCS most</th>
								
								<th data-options="field:'lteupavgrb',align:'center'">上行平均RB</th>	
								<th data-options="field:'lteupgrantnum',align:'center'">上行Grant Num</th>
		   					</tr>
		   				</thead>
		   			</table>
				</div>
			</div>
		</div>
		<div title="覆盖类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="padding:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table3" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,singleSelect:true,fit:true"  >
		   				<thead>
		   					<tr>
		                        <th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip,width:240">文件名</th>
		   						<th colspan="18" >NR</th>
		   						<th colspan="15" >LTE</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'ssbt1rsrp100sinr3nr',align:'center'">SSB综合覆盖率1(SSB RSRP >= -100且SSB SINR>=-3)(%)</th>	
		   						<!-- <th data-options="field:'ssbt2rsrp103sinr3nr',align:'center'">SSB综合覆盖率2(SSB RSRP >= -103且SSB SINR>=-3)(%)</th>	 -->
								<th data-options="field:'ssbt3rsrp110sinr3nr',align:'center'">SSB综合覆盖率3(SSB RSRP >= -110且SSB SINR>=-3)(%)</th>	
								<th data-options="field:'avgssbrsrpnr',align:'center'">平均SSB RSRP</th> 	
								<th data-options="field:'edssbrsrp5cdfnr',align:'center'">边缘SSB RSRP(CDF等于5%的值 )</th>	
								<th data-options="field:'ssbrsrp105or3nr',align:'center'">SSB RSRP连续弱覆盖率(SSB RSRP<-105 或SSB SINR<-3占比)</th>	
								<th data-options="field:'ssbdistrsrp105or3nr',align:'center'">SSB RSRP连续弱覆盖里程占比(SSB RSRP<-105 或SSB SINR<-3占比)</th>	
								<th data-options="field:'ssboverlayrate3nr',align:'center'">SSB重叠覆盖率(重叠覆盖度>=3)(%)</th>	
								<th data-options="field:'ssboverlaydist3nr',align:'center'">SSB重叠覆盖里程占比(重叠覆盖度>=3)(%)</th>	
								<th data-options="field:'ssboverlay4nr',align:'center'">SSB重叠覆盖率(重叠覆盖度>=4)(%)</th>	
								<th data-options="field:'ssboverlaydistance4nr',align:'center'">SSB重叠覆盖里程占比(重叠覆盖度>=4)(%)</th>	
								<th data-options="field:'avgssbsinrnr',align:'center'">平均SSB SINR</th> 	
								<th data-options="field:'edgessbsinr5eqcdfnr',align:'center'">边缘SSB SINR(CDF等于5%的值)</th> 	
								<th data-options="field:'ssbsinrqpdistratenr',align:'center'">连续SSB SINR质差里程占比(SSB SINR<-3dB占比)</th> 	 
								<th data-options="field:'uppucchsendpowernr',align:'center'">pucch上行发射功率</th>	 
								<th data-options="field:'upprachsendpowernr',align:'center'">prach上行发射功率</th>	 
								<th data-options="field:'uppuschsendpowernr',align:'center'">pusch上行发射功率</th>	
								<th data-options="field:'uesendpowratenr',align:'center'">连续UE高发射功率里程占比</th>	
								
								<th data-options="field:'rsrp100sinr3lte',align:'center'">综合覆盖率(RSRP >= -100且 SINR>=-3)</th>	
								<th data-options="field:'avgrsrplte',align:'center'">平均RSRP</th>	
								<th data-options="field:'edgersrp5eqcdflte',align:'center'">边缘RSRP(CDF等于5%的值 )</th>	
								<th data-options="field:'weakrsrp1053ratelte',align:'center'">RSRP连续弱覆盖比例(RSRP<-105 或SINR<-3占比)</th>	
								<th data-options="field:'avgrssinrlte',align:'center'">平均RS-SINR</th> 	
								<th data-options="field:'edgerssinr5eqcdflte',align:'center'">边缘RS-SINR(CDF等于5%的值)</th> 	
								<th data-options="field:'overlayrate3lte',align:'center'">重叠覆盖率(重叠覆盖度>=3)(%)</th>	
								<th data-options="field:'overlaydistrate3lte',align:'center'">重叠覆盖里程占比(重叠覆盖度>=3)(%)</th>	
								<th data-options="field:'overlayrate4lte',align:'center'">重叠覆盖率(重叠覆盖度>=4)(%)</th>	
								<th data-options="field:'overlaydistrate4lte',align:'center'">重叠覆盖里程占比(重叠覆盖度>=4)(%)</th>	
								<th data-options="field:'sinrqpdistratelte',align:'center'">连续SINR质差里程占比(SINR<-3dB占比)</th>	
								<th data-options="field:'uppucchsplte',align:'center'">pucch上行发射功率</th>	
								<th data-options="field:'upprachsprlte',align:'center'">prach上行发射功率</th>	
								<th data-options="field:'uppuschsplte',align:'center'">pusch上行发射功率</th>	
								<th data-options="field:'uehighspratelte',align:'center'">连续UE高发射功率里程占比</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
		<div title="移动类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="padding:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table4" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="6" >EN-DC切换汇总</th>
		   						<th colspan="5" >NR SN Change</th>
		   						<th colspan="5" >Diff_LTE_Same_NR</th>
		   						<th colspan="5" >Same_LTE_Diff_NR</th>
		   						<th colspan="5" >Diff_LTE_Diff_NR</th>
		   						<th colspan="2" >同频/异频切换</th>
		   						<th colspan="4" >站内/站间切换</th>
		   						<th colspan="4" >TAU</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'nsaswitchreqendc',align:'center'">NSA切换请求总数</th>	
								<th data-options="field:'nsaswitchsucendc',align:'center'">NSA切换成功总数</th>	
								<th data-options="field:'nsaswsucrateendc',align:'center'">NSA切换总体成功率(%)</th>	
								<th data-options="field:'nsaswitchavgtidendc',align:'center'">NSA切换平均时延(ms)</th>	
								<th data-options="field:'nrswitchrateendc',align:'center'">NR切换比例(%)</th>	
								<th data-options="field:'lteswitchrateendc',align:'center'">LTE切换比例(%)</th>
								
								<th data-options="field:'secnodereqcountendc',align:'center'">辅节点变更请求次数</th>	
								<th data-options="field:'secnodesuccountendc',align:'center'">辅节点变更成功次数</th>	
								<th data-options="field:'secnodesucrateendc',align:'center'">辅节点变更成功率(%)</th>	
								<th data-options="field:'secnodealtexcereleendc',align:'center'">变更中辅小区异常释放次数</th>	
								<th data-options="field:'secnodealtavgtmdelayendc',align:'center'">辅节点变更平均时延(ms)</th>	
								
								<th data-options="field:'nsaswitchreqcountds',align:'center'">NSA切换请求次数</th>
								<th data-options="field:'nsaswitchsuccountds',align:'center'">NSA切换成功次数</th>	
								<th data-options="field:'nsaswitchavgtimedelayds',align:'center'">NSA切换平均时延(ms)</th>	
								<th data-options="field:'secnodealtexcereleds',align:'center'">切换中辅小区异常释放次数</th>	
								<th data-options="field:'nsaswitchsucrateds',align:'center'">NSA切换成功率(%)</th>	
								
								<th data-options="field:'nsaswitchreqcountsd',align:'center'">NSA切换请求次数</th>	
								<th data-options="field:'nsaswitchsuccountsd',align:'center'">NSA切换成功次数</th>	
								<th data-options="field:'nsaswitchavgtimedelaysd',align:'center'">NSA切换平均时延(ms)</th>	
								<th data-options="field:'nsaswitchsucratesd',align:'center'">NSA切换成功率(%)</th>	
								<th data-options="field:'secnodealtexcerelesd',align:'center'">切换中辅小区异常释放次数</th>	
								
								<th data-options="field:'totaldropratedd',align:'center'">NSA切换请求次数</th>	
								<th data-options="field:'totaldropratedd',align:'center'">NSA切换成功次数</th>	
								<th data-options="field:'totaldropratedd',align:'center'">NSA切换平均时延(ms)</th>	
								<th data-options="field:'totaldropratedd',align:'center'">NSA切换成功率(%)</th>	
								<th data-options="field:'totaldropratedd',align:'center'">切换中辅小区异常释放次数</th>	
								
								<th data-options="field:'ltesamefreqswitchcount',align:'center'">LTE同频切换次数</th>	
								<th data-options="field:'lteheterofreqswitchcount',align:'center'">LTE异频切换次数</th>
								
								<th data-options="field:'nrswitchcountinstation',align:'center'">NR站内切换次数</th>	
								<th data-options="field:'nrswitchcountbetstation',align:'center'">NR站间切换次数</th>	
								<th data-options="field:'lteswitchcountinstation',align:'center'">LTE站内切换次数</th>	
								<th data-options="field:'lteswitchcountbetstation',align:'center'">LTE站间切换次数</th>
								
								<th data-options="field:'taupdatereqcount',align:'center'">TA更新请求次数</th>	
								<th data-options="field:'taupdatesuccesscount',align:'center'">TA更新成功次数</th>	
								<th data-options="field:'taupdatesuccessrate',align:'center'">TA更新成功率(%)</th>	
								<th data-options="field:'taupdateavgtimedalay',align:'center'">TA更新平均时延(ms)</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
		<div title="接入类" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
			<div class="easyui-layout" style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;">
				<div data-options="region:'north',border:false" style="height:42px;">
					<div style="padding:10px;">
						开始时间：<input class="begTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;  
						结束时间：<input class="endTime easyui-textbox" editable=false type="text"  style="width:200px;">&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<div data-options="region:'center',border:false" >
					<table id="Table5" class="easyui-datagrid" data-options="scrollbarSize:0,striped:true,fitColumns:false,fit:true"  >
		   				<thead>
		   					<tr>
		   						<th rowspan="2" data-options="field:'testLogFileName',align:'center',formatter:showTooltip">文件名</th>
		   						<th colspan="4" >Attach-SCG 接入</th>
		   						<th colspan="4" >RRC建立</th>
		   						<th colspan="4" >Service建立</th>
		   						<th colspan="4" >LTE Attach</th>
		   					</tr>
		   					<tr>
		   						<th data-options="field:'actestcountattachscg',align:'center'">Attach-SCG接入尝试次数</th>	
								<th data-options="field:'acsuccountattachscg',align:'center'">Attach-SCG接入成功次数</th>	
								<th data-options="field:'acsucrateattachscg',align:'center'">Attach-SCG接入成功率(%)</th>	
								<th data-options="field:'acavgtidattachscg',align:'center'">Attach-SCG接入平均时延(ms)</th>	
								
								<th data-options="field:'accesstestcountrrc',align:'center'">RRC连接建立尝试次数</th>	
								<th data-options="field:'accesssuccesscountrrc',align:'center'">RRC连接建立成功次数</th>	
								<th data-options="field:'accesssuccessraterrc',align:'center'">RRC连接建立成功率(%)</th>	
								<th data-options="field:'accessavgtimedelayrrc',align:'center'">RRC连接建立平均时延(ms)</th>
								
								<th data-options="field:'accesstestcountservice',align:'center'">Service建立尝试次数</th>	
								<th data-options="field:'accesssuccesscountservice',align:'center'">Service建立成功次数</th>	
								<th data-options="field:'accesssuccessrateservice',align:'center'">Service成功率(%)</th>	
								<th data-options="field:'accessavgtimedelayservice',align:'center'">Service平均时延(ms)</th>
								
								<th data-options="field:'accesstestcountattach',align:'center'">Attach尝试次数</th>	
								<th data-options="field:'accesssuccesscountattach',align:'center'">Attach成功次数</th>	
								<th data-options="field:'accesssuccessrateattach',align:'center'">Attach成功率(%)</th>	
								<th data-options="field:'accessavgtimedelayattach',align:'center'">Attach平均时延(ms)</th>
		   					</tr>
		   				</thead>
		   			</table>
		   		</div>
		   	</div>
		</div>
	</div>
</body>
</html>
