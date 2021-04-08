<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>历史轨迹</title>

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
	//gisCommon会调用获取终端boxID
	function getGpsPointTerminalIds(){
		return boxIdArray;
	}
	//gisCommon会调用获取事件轨迹的url
	function getEventGpsPointActionUrl(){
		return "<%=basePath%>mapMonitor/queryGPSEventRequestParam.action";
	}
	var boxIdArray=new Array();
	/*初始化时间以及终端选项*/
	$(function(){
		//初始化开始时间和结束时间
		var beginDate;
		var endDate;
		var nowDate=new Date();
		var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");	
		endDate=dateString;
		$("#endDate").datetimebox('setValue',dateString);
		nowDate.setDate(nowDate.getDate()-7);
		dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
		beginDate=dateString;
		$("#beginDate").datetimebox('setValue',dateString);
		$.post("${pageContext.request.contextPath}/mapMonitor/allHaveGpsTerminal.action?beginDate="+beginDate+"&endDate="+endDate+"",
				function(result){
					//for(var i=0;i<result.length;i++)
			        //{
						//"<option value='"+result[i]+"'>"+result[i]+"</option>"
			         $("#boxID").combobox({data:result});
			        //} 
				}
			,"json");
	});
	//用户选择时间同步刷新终端
	function arrayBoxId(date){
		var beginDate=$("#beginDate").datetimebox('getValue');
		var endDate=$("#endDate").datetimebox('getValue');
		$.post("${pageContext.request.contextPath}/mapMonitor/allHaveGpsTerminal.action?beginDate="+beginDate+"&endDate="+endDate+"",
				function(result){
					//for(var i=0;i<result.length;i++)
			        //{
						//"<option value='"+result[i]+"'>"+result[i]+"</option>"
			         $("#boxID").combobox({data:result});
			        //} 
				}
			,"json");
	}
	/* 条件查询 */
	function queryHistory(){
	 $('#queryForm').form('submit', {  
                url:'${pageContext.request.contextPath}/mapMonitor/queryHistoryOtherInformation.action',  
                onSubmit: function(){  
                        if($("#queryForm").form("validate")) 
                           return true  
                        else  
                            return false;  
                        },  
                success:function(data){ 
                	data = JSON.parse(data);
					$("#Info").datagrid('loadData',[]);
					$("#Info").datagrid('loadData',data.Info);
                  
                }  
              }); 
	}
	//查询历史信息
	function query(){
		queryHistory();
	}
	//回放历史轨迹
	function doAnalysis(){
		var beginDate;
		var endDate;
		var boxID;
		var info = $("#Info").datagrid('getSelected');//获取表格中用户选中数据
		if(!info){// 操作前至少选中一条
			$.messager.alert("提示","请勾选某个时间段",'warning');
			return ;
		}
		boxID = info.boxId;
		beginDate = info.loginTimeString;
		endDate = info.offlineTimeString;
		if(!boxID){
			$.messager.alert("提示","请勾选某个时间段",'warning');
			return ;
		}
		boxIdArray=[];
		boxIdArray.push(boxID);
		$.post("${pageContext.request.contextPath}/mapMonitor/queryHistoryTerminalGpsPoint.action?boxID="+boxID+"&beginDates="+beginDate+"&endDates="+endDate+"",
				function(data){
        	$("#eventInfo").datagrid('loadData',[]);
			$("#eventInfo").datagrid('loadData',data.event);
			$("#alarmInfo").datagrid('loadData',[]);
			$("#alarmInfo").datagrid('loadData',data.alarm);
			$("#MOSInfo").datagrid('loadData',[]);
			$("#MOSInfo").datagrid('loadData',data.mos);
			$("#statusInfo").datagrid('loadData',[]);
			$("#statusInfo").datagrid('loadData',data.status);
			$("#ATUInfo").datagrid('loadData',[]);
			$("#ATUInfo").datagrid('loadData',data.ATULogin);
			parent.mapIframe.window.drawTerminalHistory (data.gsp2.rows,data.gsp.rows);
				}
			,"json");
		 $('#mm').linkbutton('enable');
		 $('#mm').linkbutton({
			    text: '暂停'
			});
		
	}
	//暂停、继续轨迹回放
	function hiddenLayout(){
		var a=$("#mm").linkbutton("options").text;
		if(a==="暂停"){
			parent.mapIframe.window.pauseHistoryTrack(0);
			$('#mm').linkbutton({
			    text: '继续'
			});
		}
		 if(a==="继续"){
			parent.mapIframe.window.pauseHistoryTrack(1);
				$('#mm').linkbutton({
				    text: '暂停'
				});
		 }
	}
	/* 重置表单 */
	function resetForm(){
		$("#queryForm").form('reset');
		var nowDate=new Date();
		var dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");		
		$("#endDate").datetimebox('setValue',dateString);
		nowDate.setDate(nowDate.getDate()-7);
		dateString = nowDate.Format("yyyy-MM-dd hh:mm:ss");
		$("#beginDate").datetimebox('setValue',dateString);
	}
	</script>
	<style type="text/css">
		.inputDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 210px;
   		}
   		.inputDivShow input{
   			width:140px;
   		}
   		.inputDivShow select{
   			width:140px;
   		}
	</style>
  </head>
  <body class="easyui-layout">
    <div region='north' border=false split="false" title='查询' style="width:100%;height:20%;">
    	<form id="queryForm" class="esyui-form" method="post" style="width:100%;height: 100%;margin:0;" >  	
			<div class="inputDivShow">开始时间
			  	<input id="beginDate" name="beginDate"  class="easyui-datetimebox" data-options="onSelect:arrayBoxId,required:true,editable:false" />
			</div>
			<div class="inputDivShow">结束时间
			   	<input id="endDate" name="endDate"  class="easyui-datetimebox" data-options="onSelect:arrayBoxId,required:true,editable:false" />
			</div>
			    		
			<div class="inputDivShow">&nbsp;&nbsp;&nbsp;BOXID
				<select id="boxID" data-options="valueField:'id',textField:'value'" name="boxID" class="easyui-combobox"  >
				</select>
			</div>
			<br/>

			<table width="100%">
			   <tr>
				   <td width="50%" align="right"><a class="easyui-linkbutton" onclick="query();" style="width: 80px;" data-options="iconCls:'icon-search'" >查找</a></td>
				   <td width="50%" align="left"><a class="easyui-linkbutton" onclick="resetForm();" style="width: 80px;" data-options="iconCls:'icon-reload'" >重置</a></td>
			   </tr>
			 </table>
		 </form>
		 <div style="width:100%;border-bottom:1px solid #95b8e7;bottom:0;position:absolute;"></div>	
   	</div>
    <div region="south"  border=false split="false" title="详细" style="width:100%;height:40%;">
    	<div class="easyui-tabs" border=false  style="width:100%;height:100%;">
			<div title="事     件" style="width:100%;height:100%;">
				<table id="eventInfo" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true">
					<thead>
						<tr>
							<th data-options="field:'channelNo',width:45,align:'center',formatter:showTooltip">通道</th>
							<th data-options="field:'channelType',width:90,align:'center',formatter:showTooltip">通道类型</th>
							<th data-options="field:'eventType',width:90,align:'center',formatter:showTooltip">事件类型</th>
							<th data-options="field:'eventCode',width:90,align:'center',formatter:showTooltip">事件代码</th>
							<th data-options="field:'eventName',width:80,align:'center',formatter:showTooltip">事件名称</th>
							<th data-options="field:'eventTime',width:100,align:'center',formatter:showTooltip">事件时间</th>
						</tr>
					</thead>
				</table>

			</div>
			<div title="告      警" style="width:100%;height:100%;">
				<table  id="alarmInfo"class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true">
					<thead>
						<tr>
							<th data-options="field:'channelNo',width:50,align:'center',formatter:showTooltip">通道</th>
							<th data-options="field:'channelType',width:100,align:'center',formatter:showTooltip">通道类型</th>
							<th data-options="field:'alarmCode',width:100,align:'center',formatter:showTooltip">告警代码</th>
							<th data-options="field:'alarmReason',width:50,align:'center',formatter:showTooltip">原因</th>
							<th data-options="field:'alarmType',width:50,align:'center',formatter:showTooltip">告警类型</th>
							<th data-options="field:'alarmTime',width:100,align:'center',formatter:showTooltip">告警时间</th>
							<th data-options="field:'alarmClearTime',width:100,align:'center',formatter:showTooltip">清除时间</th>
						</tr>
					</thead>
				</table>

			</div>
			<div title="MOS" style="width:100%;height:100%;">
				<table id="MOSInfo" class="easyui-datagrid" data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true">
					<thead>
						<tr>
							<th data-options="field:'channelNo',width:70,align:'center',formatter:showTooltip">通道</th>
							<th data-options="field:'channelType',width:140,align:'center',formatter:showTooltip">通道类型</th>
							<th data-options="field:'mosValue',width:120,align:'center',formatter:showTooltip">Mos值</th>
							<th data-options="field:'mosTime',width:160,align:'center',formatter:showTooltip">Mos时间</th>
						</tr>
					</thead>
				</table>

			</div>
			<div title="状     态" style="width:100%;height:100%;">
				<table id="statusInfo"class="easyui-datagrid"  data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true">
					<thead>
						<tr>
							<th data-options="field:'temperature',width:90,align:'center',formatter:showTooltip">工作温度（℃）</th>
							<th data-options="field:'powerMode',width:60,align:'center',formatter:showTooltip">电源模式</th>
							<th data-options="field:'spaceLeft',width:120,align:'center',formatter:showTooltip">剩余磁盘空间（MB）</th>
							<th data-options="field:'filesLeft',width:130,align:'center',formatter:showTooltip">未上传完成文件个数</th>
							<th data-options="field:'statusReportTime',width:70,align:'center',formatter:showTooltip">状态时间</th>
						</tr>
					</thead>
				</table>

			</div>
			<div title="登     陆" style="width:100%;height:100%;">
				<table id="ATUInfo"class="easyui-datagrid"  data-options="scrollbarSize:0,fit:true,striped:true,fitColumns:true">
					<thead>
						<tr>
							<th data-options="field:'loginTime',width:70,align:'center',formatter:showTooltip">登录时间</th>
							<th data-options="field:'offlineTime',width:70,align:'center',formatter:showTooltip">离线时间</th>
							<th data-options="field:'status',width:70,align:'center',formatter:showTooltip">登录状态</th>
							<th data-options="field:'failReason',width:105,align:'center',formatter:showTooltip">登录失败原因</th>
							<th data-options="field:'sessionId',width:50,align:'center',formatter:showTooltip">会话ID</th>
							<th data-options="field:'testPlanVersion',width:110,align:'center',formatter:showTooltip">测试计划版本</th>
						</tr>
					</thead>
				</table>
			</div>
</div>
    </div>
    <div region="center" border=false style="width:100%;" split="false"  title="查询结果">
    	<div class="easyui-layout" style="width:100%;height:100%;" >
			<div data-options="region:'south',border:false" style="width:100%;height:32px;">
				<table width="100%">
				   <tr>
					   <td width="50%" align="right"><a class="easyui-linkbutton" onclick="doAnalysis();" style="width: 80px;" title="回放轨迹"" >回放</a></td>
					   <td  width="50%" align="left"><a id="mm" class="easyui-linkbutton" disabled="true" onclick="hiddenLayout();" style="width: 80px;"title="暂停回放" >暂停</a></td>
				   </tr>
				 </table>
			</div>
			<div data-options="region:'center',border:false" style="width:100%;">
		    	<table style="width:100%;height:100%;" id="Info" class="easyui-datagrid" data-options="singleSelect:true,checkOnSelect:true,scrollbarSize:0,fit:true,striped:true,fitColumns:true">
					<thead>
						<tr>
							<th data-options="field:'itemid',checkbox:true,width:80">显示</th>
							<th data-options="field:'boxId',width:100">ATUID</th>
							<th data-options="field:'loginTimeString',width:172,align:'right',formatter:showTooltip">开始时间</th>
							<th data-options="field:'offlineTimeString',width:172,align:'right'">结束时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div style="width:100%;border-bottom:1px solid #95b8e7;bottom:0;position:absolute;"></div>	
    </div>
</body>
</html>
