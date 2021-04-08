<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>单站参数设置</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%@ include file="../../../taglibs/echarts.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/gisCommon.js" ></script>
	<style type="text/css">
		.titleDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 180px;
   		}
	</style>
	<script type="text/javascript">
		$(function(){
			$.post('${pageContext.request.contextPath}/stationParam/getInitTemplate.action', {}, 
					function(result) {
						initSelect(result.dzList,"",'singleStationMOdelSelect');
						initSelect(result.oppo3dlist,"",'contrastOpen3DMOdelSelect');
					}
			,'json');
		});
		
		/*填充名称下拉框*/
		function initSelect(ugList,nameList,id){
			var data = [];
			for(var i = 0;i < ugList.length;i++){
				var oneUp = {};
				oneUp.id = ugList[i].id;
				oneUp.name = ugList[i].name;
				if(nameList.indexOf(ugList[i].name)!=-1){
					oneUp.selected = true;
				}
				data.push(oneUp);
			}
		    $("#"+id+"").combobox({
				valueField: 'id', 
				textField: 'name',
				data: data,
				filter: function(q, row){
					var opts = $(this).combobox('options');
					return row[opts.textField].toLowerCase().indexOf(q.toLowerCase())>-1;
				}
		    }); 
		}
	
		function areaTreeFunction(node){
			if(node == null || node.children != null){
				$.messager.alert('提示','请选择一个城市','info');
				return;
			}
			var cityName = '';
			var cityId = '';
			if(node.id != -2){//全部
				cityId = node.id;
				cityName = node.text;
			}else{
				$.messager.alert('提示','请选择一个城市','info');
				return;
			}
			
			$.ajax({
	             type: "GET",
	             url: "${pageContext.request.contextPath}/stationParam/getStationParam.action",
	             data: {'cityName':cityName,'cityId':cityId},
	             dataType: "json",
	             success: function(data){
	            	console.log(data);
	            	if(data.menu.stationParamPojo){
	            		var stationParam = data.menu.stationParamPojo;
	            		$("#stationId").val(stationParam.id);
	            		$("#upgradeGood").numberbox("setValue",stationParam.upgradeGood);
	            		$("#upgradeMid").numberbox("setValue",stationParam.upgradeMid );
	            		$("#upgradeBad").numberbox("setValue",stationParam.upgradeBad );
	            		$("#uploadGood").numberbox("setValue",stationParam.uploadGood );
	            		$("#uploadMid").numberbox("setValue",stationParam.uploadMid );
	            		$("#uploadBad").numberbox("setValue",stationParam.uploadBad );
	            		
	            		$("#downloadGoodApex").numberbox("setValue",stationParam.downloadGoodApex);
	            		$("#uploadGoodApex").numberbox("setValue",stationParam.uploadGoodApex);
	            		
	            		$("#connectSuccessRatio4G").numberbox("setValue",stationParam.connectSuccessRatio4G );
	            		$("#connectNumber4g").numberbox("setValue",stationParam.connectNumber4g );
	            		$("#changeSuccessRatio4G").numberbox("setValue",stationParam.changeSuccessRatio4G );
	            		$("#disconectRatio4G").numberbox("setValue",stationParam.disconectRatio4G );
	            		$("#rrcRatio4G").numberbox("setValue",stationParam.rrcRatio4G );
	            		$("#connectSuccess5G").numberbox("setValue",stationParam.connectSuccess5G );
	            		$("#connectNumber5G").numberbox("setValue",stationParam.connectNumber5G );
	            		$("#changeSuccessRatio5G").numberbox("setValue",stationParam.changeSuccessRatio5G );
	            		$("#disconectRatio5G").numberbox("setValue",stationParam.disconectRatio5G );
	            		$("#ping32DelayTime").numberbox("setValue",stationParam.ping32DelayTime );
	            		$("#ping32SuccessRatio").numberbox("setValue",stationParam.ping32SuccessRatio );
	            		$("#ping1500DelayTime").numberbox("setValue",stationParam.ping1500DelayTime );
	            		$("#ping1500SuccessRatio").numberbox("setValue",stationParam.ping1500SuccessRatio );
	            		$("#ping32Number").numberbox("setValue",stationParam.ping32Number );
	            		$("#ping1500Nember").numberbox("setValue",stationParam.ping1500Nember );
	            		$("#nrAccessDelayTime").numberbox("setValue",stationParam.nrAccessDelayTime );
	            		$("#upgradeVeryGood3D4G").numberbox("setValue",stationParam.upgradeVeryGood3D4G );
	            		$("#uploadVeryGood3D4G").numberbox("setValue",stationParam.uploadVeryGood3D4G );
	            		$("#ping32DelayTime3D").numberbox("setValue",stationParam.ping32DelayTime3D );
	            		$("#ping32SuccessRatio3D").numberbox("setValue",stationParam.ping32SuccessRatio3D );
	            		$("#ping32Number3D").numberbox("setValue",stationParam.ping32Number3D );
	            		
	            		$("#voiceAccessRatio").numberbox("setValue",stationParam.voiceAccessRatio );
	            		$("#voiceDropRatio").numberbox("setValue",stationParam.voiceDropRatio );
	            		$("#voiceAccesDelayTime").numberbox("setValue",stationParam.voiceAccesDelayTime );
	            		
	            		$("#dtDownloadRate").numberbox("setValue",stationParam.dtDownloadRate );
	            		$("#dtUploadRate").numberbox("setValue",stationParam.dtUploadRate );
	            		
	            		$("#dtTestRsrp").numberbox("setValue",stationParam.dtTestRsrp );
	            		$("#dtTestSinr").numberbox("setValue",stationParam.dtTestSinr );
	            		
	            		$("#epsFallbackTestNum").numberbox("setValue",stationParam.epsFallbackTestNum);
	            		$("#epsFallbackTimeDelay").numberbox("setValue",stationParam.epsFallbackTimeDelay);
	            		$("#vonrTestNumber").numberbox("setValue",stationParam.vonrTestNumber);
	            		
	            		if(stationParam.trailSamDisplay == null || stationParam.trailSamDisplay == 0){
	            			$('#trailSamDisplay').prop('checked', false);
	            		}else{
	            			$('#trailSamDisplay').prop('checked', true);
	            		}
	            		
	            		$("#singleStationMOdelSelect").combobox("setValue",stationParam.singleStationMOdelSelect );
	            		$("#contrastOpen3DMOdelSelect").combobox("setValue",stationParam.contrastOpen3DMOdelSelect );
	            		
	            	}else{
	            		$("#stationId").val(-1);
	            		$("#upgradeGood").numberbox("setValue",300);
	            		$("#upgradeMid").numberbox("setValue",120);
	            		$("#upgradeBad").numberbox("setValue",30);
	            		$("#uploadGood").numberbox("setValue",30);
	            		$("#uploadMid").numberbox("setValue",20);
	            		$("#uploadBad").numberbox("setValue",1);
	            		
	            		$("#downloadGoodApex").numberbox("setValue",880);
	            		$("#uploadGoodApex").numberbox("setValue",220);
	            		
	            		$("#connectSuccessRatio4G").numberbox("setValue",100);
	            		$("#connectNumber4g").numberbox("setValue",10);
	            		$("#changeSuccessRatio4G").numberbox("setValue",100);
	            		$("#disconectRatio4G").numberbox("setValue",0);
	            		$("#rrcRatio4G").numberbox("setValue",100);
	            		$("#connectSuccess5G").numberbox("setValue",100);
	            		$("#connectNumber5G").numberbox("setValue",100);
	            		$("#changeSuccessRatio5G").numberbox("setValue",100);
	            		$("#disconectRatio5G").numberbox("setValue",0);
	            		$("#ping32DelayTime").numberbox("setValue",15);
	            		$("#ping32SuccessRatio").numberbox("setValue",100);
	            		$("#ping1500DelayTime").numberbox("setValue",17);
	            		$("#ping1500SuccessRatio").numberbox("setValue",100);
	            		$("#ping32Number").numberbox("setValue",20);
	            		$("#ping1500Nember").numberbox("setValue",20);
	            		$("#nrAccessDelayTime").numberbox("setValue",100);
	            		$("#upgradeVeryGood3D4G").numberbox("setValue",300 );
	            		$("#uploadVeryGood3D4G").numberbox("setValue",120 );
	            		$("#ping32DelayTime3D").numberbox("setValue",30 );
	            		$("#ping32SuccessRatio3D").numberbox("setValue",95 );
	            		$("#ping32Number3D").numberbox("setValue",100 );
	            		
	            		$("#voiceAccessRatio").numberbox("setValue",95 );
	            		$("#voiceDropRatio").numberbox("setValue",5);
	            		$("#voiceAccesDelayTime").numberbox("setValue",2000);
	            		
	            		$("#dtDownloadRate").numberbox("setValue",200 );
	            		$("#dtUploadRate").numberbox("setValue",40);
	            		
	            		$("#dtTestRsrp").numberbox("setValue",-100);
	            		$("#dtTestSinr").numberbox("setValue",-3);
	            		
	            		$("#epsFallbackTestNum").numberbox("setValue",10);
	            		$("#epsFallbackTimeDelay").numberbox("setValue",200);
	            		$("#vonrTestNumber").numberbox("setValue",10);
	            		
	            		$('#trailSamDisplay').prop('checked', false);  // 默认控制不选中
	            		
	            		$("#singleStationMOdelSelect").combobox("setValue","1" );
	            		$("#contrastOpen3DMOdelSelect").combobox("setValue","1" );
	            	}
	            }
	         });
		}
		
		/* 重置初始值 */
		function resetForm(){
			$.messager.confirm('提示','你确认要重置为初始值?',function(flag){
				if(flag){
					var selectedTab = $("#tt").tabs('getSelected');
					var tabsIndex = $('#tt').tabs('getTabIndex',selectedTab);
					$.ajax({
						url:"${pageContext.request.contextPath}/json/volteDissThresholdInitialValue.json",
						dataType:"json",
						type:"post",
						success:function(jsonData){
							
						}
					});
				}
			});
		}
		
		function submitForm(){
			var select = $('#areaTree').tree('getSelected');
			if(select == null || select.children != null){
				$.messager.alert('提示','请选择一个城市','info');
			}else{
				$("#paramFormId").form('submit',{
					success:function(data){
						$.messager.alert('提示','修改成功','info');
					},
			        onSubmit: function(param){
				        var isValid = $(this).form('validate');
						if(isValid){
							param.cityId = select.id;
						}
						return isValid;
			        }
				});
			}
		}
		
		function downReportModel(modelType){
			var modelSelect = null;
			if(modelType==1){ //单验报告模板下载
				modelSelect = $("#singleStationMOdelSelect").combobox("getValue");
			}else{ //反开3d报告模板下载
				modelSelect = $("#contrastOpen3DMOdelSelect").combobox("getValue");
			}
			goToPage('${pageContext.request.contextPath}/stationParam/downloadStationModel?modelType='+modelType+'&modelSelect='+modelSelect);
		}
		
	</script>

  </head>
  
  <body id="cc" class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
	<div data-options="region:'west',title:'区域选择',split:true,tools:'#tt3'" style="width:20%;overflow: auto;">
    	<ul id="areaTree" class="easyui-tree"  data-options="onClick:areaTreeFunction,url:'${pageContext.request.contextPath}/activationShowAction/terminalGroupTree.action',lines:true,
    	formatter:function(node){
	    	return node.text;
    	}"></ul>
    </div>
    <div data-options="region:'center',title:'参数设置',split:false,tools:'#tt3'"  style="width:70%;overflow: auto;">
    	<div data-options="region:'center',border:false">
			<form id="paramFormId" method="post" action="${pageContext.request.contextPath}/stationParam/saveStationParam.action" >
				<input id="stationId" name="sid" value="" style="display: none;" />
				<div style="margin-top:10px;height:70px;">
					<div style="width: 2%;float:left;height: 30px;">&nbsp;&nbsp;</div>
					<div style="width: 98%;float:left;height: 30px;">
						<div style="width: 49%;float:left;">
							单验模板选择:
							<select id="singleStationMOdelSelect" class="easyui-combobox" name="singleStationMOdelSelect" style="width:180px;" data-options="editable:false">
					    	 	
					    	</select>
					    	<a  style="width:150px;color: blue;margin-left:5px;cursor: pointer;" onclick="downReportModel(1);" >单验报告模板下载</a>
						</div>
						<div style="width: 49%;float:left;">
							反开3d测试模板选择:
							<select id="contrastOpen3DMOdelSelect" class="easyui-combobox" name="contrastOpen3DMOdelSelect" style="width:180px;" data-options="editable:false">
					    		
					    	</select>
					    	<a  style="width:150px;color: blue;margin-left:5px;cursor: pointer;" onclick="downReportModel(2);"  >反开3d报告模板下载</a>
						</div>
					</div>
					<div style="margin-top:10px;width: 2%;float:left;height:30px;">&nbsp;&nbsp;</div>
					<div style="height:30px;float:left;">
						<input id="trailSamDisplay" type="checkbox" style="width:15px" name="trailSamDisplay"  value="1">路测轨迹抽样显示
					</div>
				</div>
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;">
					<div class="panel-title">FTP速率测试通过标准设置</div>
				</div>
				<div style="margin-top:10px;height:30px;">
					<div style="width: 10%;text-align:center;float:left;">下载:</div>
					<div style="width: 30%;float:left;">
						好点门限>=<input id="upgradeGood" name="upgradeGood" value="300" class="easyui-numberbox" data-options="required:true,precision:2"/>Mbps
					</div>
					<div style="width: 30%;float:left;">
						中点门限>=<input id="upgradeMid" name="upgradeMid" value="120" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"/>Mbps
					</div>
					<div style="width: 30%;float:left;">
						差点门限>=<input id="upgradeBad" name="upgradeBad" value="20" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"/>Mbps
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
					<div style="width: 10%;text-align:center;float:left;">上传:</div>
					<div style="width: 30%;float:left;">
						好点门限>=<input id="uploadGood" name="uploadGood" value="30" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"  />Mbps
					</div>
					<div style="width: 30%;float:left;">
						中点门限>=<input id="uploadMid" name="uploadMid" value="20" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"  />Mbps
					</div>
					<div style="width: 30%;float:left;">
						差点门限>=<input id="uploadBad" name="uploadBad" value="1" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"  />Mbps
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
					<div style="width: 10%;text-align:center;float:left;">峰值门限:</div>
					<div style="width: 30%;float:left;">
						下载好点>=<input id="downloadGoodApex" name="downloadGoodApex" value="300" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"/>Mbps
					</div>
					<div style="width: 30%;float:left;">
						上传好点>=<input id="uploadGoodApex" name="uploadGoodApex" value="120" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"/>Mbps
					</div>
				</div>
					
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;margin-top:3%;">
			    	<div class="panel-title">接入切换测试通过标准设置</div>
			    </div>
			    <div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						attach成功率门限>=<input id="connectSuccessRatio4G" name="connectSuccessRatio4G"
						 value="100" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100 " />%
					</div>
					<div style="width: 33%;float:left;">
						attach尝试次数门限>=<input id="connectNumber4g" name="connectNumber4g" value="10" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />次
					</div>
					<div style="width: 32%;float:left;">
						4G切换成功率门限>=<input id="changeSuccessRatio4G" name="changeSuccessRatio4G" 
						value="100" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						4G掉线率门限&lt;=<input id="disconectRatio4G" name="disconectRatio4G" value="0" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
					<div style="width: 33%;float:left;">
						4GRRC重建比例门限>=<input id="rrcRatio4G" name="rrcRatio4G" value="100" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
					<div style="width: 32%;float:left;">
						5G连接成功率门限>=<input id="connectSuccess5G" name="connectSuccess5G" value="100" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						5G连接尝试次数门限>=<input id="connectNumber5G" name="connectNumber5G" value="100" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />次
					</div>
					<div style="width: 33%;float:left;">
						5G切换成功率门限>=<input id="changeSuccessRatio5G" name="changeSuccessRatio5G" value="100" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
					<div style="width: 32%;float:left;">
						5G掉线率门限&lt;=<input id="disconectRatio5G" name="disconectRatio5G" value="0" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
				</div>
				
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;margin-top:3%;">
			    	<div class="panel-title">ping测试通过标准设置</div>
			    </div>
			    <div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						Ping32时延门限&lt;=<input id="ping32DelayTime" name="ping32DelayTime" value="15" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />ms
					</div>
					<div style="width: 33%;float:left;">
						Ping32成功率门限>=<input id="ping32SuccessRatio" name="ping32SuccessRatio" value="100" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
					<div style="width: 32%;float:left;">
						Ping1500时延门限&lt;=<input id="ping1500DelayTime" name="ping1500DelayTime" value="17" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />ms
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						Ping1500成功率门限>=<input id="ping1500SuccessRatio" name="ping1500SuccessRatio" 
						value="100" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
					<div style="width: 33%;float:left;">
						Ping32次数门限>=<input id="ping32Number" name="ping32Number" value="20" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />次
					</div>
					<div style="width: 32%;float:left;">
						Ping1500次数门限>=<input id="ping1500Nember" name="ping1500Nember" value="20" 
						class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />次
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						NR接入时延&lt;=<input id="nrAccessDelayTime" name="nrAccessDelayTime" 
						value="100" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />ms
					</div>
				</div>
				
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;margin-top:3%;">
			    	<div class="panel-title">反开3d测试通过标准设置</div>
			    </div>
			    <div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						4G上传极好点门限>=<input id="upgradeVeryGood3D4G" name="upgradeVeryGood3D4G" value="300" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"  />Mbps
					</div>
					<div style="width: 65%;float:left;">
						4G下载极好点门限>=<input id="uploadVeryGood3D4G" name="uploadVeryGood3D4G" value="120" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"  />Mbps
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						ping32时延门限&lt;=<input id="ping32DelayTime3D" name="ping32DelayTime3D" value="30" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />ms
					</div>
					<div style="width: 33%;float:left;">
						ping32成功率门限>=<input id="ping32SuccessRatio3D" name="ping32SuccessRatio3D" value="95" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100" />%
					</div>
					<div style="width: 32%;float:left;">
						ping32次数门限>=<input id="ping32Number3D" name="ping32Number3D" value="100" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />次
					</div>
				</div>
				
				
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;margin-top:3%;">
			    	<div class="panel-title">语音通过标准设置</div>
			    </div>
			    <div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						语音接通率>=<input id="voiceAccessRatio" name="voiceAccessRatio" value="95" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100"  />%
					</div>
					<div style="width: 33%;float:left;">
						语音掉话率&lt;=<input id="voiceDropRatio" name="voiceDropRatio" value="5" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2,min:0,max:100" />%
					</div>
					<div style="width: 32%;float:left;">
						语音接通时延&lt;=<input id="voiceAccesDelayTime" name="voiceAccesDelayTime" value="2000" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />ms
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						EpsFallback尝试次数>=<input id="epsFallbackTestNum" name="epsFallbackTestNum" value="10" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />次
					</div>
					<div style="width: 33%;float:left;">
						EpsFallback时延&lt;=<input id="epsFallbackTimeDelay" name="epsFallbackTimeDelay" value="200" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />ms
					</div>
					<div style="width: 32%;float:left;">
						VONR尝试次数>=<input id="vonrTestNumber" name="vonrTestNumber" value="10" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />次
					</div>
				</div>
				
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;margin-top:3%;">
			    	<div class="panel-title">DT测试通过标准设置</div>
			    </div>
			    <div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						下载速率门限>=<input id="dtDownloadRate" name="dtDownloadRate" value="200" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2"  />Mbps
					</div>
					<div style="width: 65%;float:left;">
						上传速率门限>=<input id="dtUploadRate" name="dtUploadRate" value="40" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',precision:2" />Mbps
					</div>
				</div>
				<div style="margin-top:10px;height:30px;">
			    	<div style="width: 2%;float:left;">&nbsp;&nbsp;</div>
					<div style="width: 33%;float:left;">
						RSRP>=<input id="dtTestRsrp" name="dtTestRsrp" value="-100" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'"  />dBm
					</div>
					<div style="width: 65%;float:left;">
						SINR>=<input id="dtTestSinr" name="dtTestSinr" value="-3" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]'" />dB
					</div>
				</div>
			</form>
		</div>
		<div data-options="region:'south',border:false" style="height:40px;margin-top:4%;">
		   	<table width="100%" style="border-top:1px solid #95b8e7;">
		   		<tr height="35px">
		    		<td width="50%;" align="center">
		    			<shiroextend:hasAnyPermissions name="stationParam:add">
		    				<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >确认</a>
		    			</shiroextend:hasAnyPermissions>
		    		</td>
		    		<%-- <td width="50%;">
		    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">
		    				<a class="easyui-linkbutton" iconCls="icon-reload" style="width:100px;" onclick="resetForm();" >重置</a>
		    			</shiroextend:hasAnyPermissions>
		    		</td> --%>
		   		</tr>
		   	</table>
	  	</div>
	</div>
  </body>
</html>
