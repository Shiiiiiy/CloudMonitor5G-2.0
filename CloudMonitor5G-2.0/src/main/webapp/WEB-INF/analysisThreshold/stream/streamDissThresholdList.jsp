<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>流媒体专题分析门限</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="../../../taglibs/jquery.jsp"%>
	<%@ include file="../../../taglibs/easyui.jsp"%>
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" ></script> --%>
	<style type="text/css">
		.inputDivShow{
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 200px;
   		}
		.titleDivShow{ 
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: right;
		    width: 150px;
   		}
   		.inputDivShow input{
   			width:100px;
   		}
   		.inputDivShow select{
   			width:100px;
   		}
	</style>
	<script type="text/javascript">
		$(function(){
			//是否在页面加载完成的时候加载门限参数的初始值缓存到页面?
		});
		/* 保存修改 */
		function submitForm(){
			//$.messager.progress();	// display the progress bar
			var selectedTab = $("#tt").tabs('getSelected');
			var index = $('#tt').tabs('getTabIndex',selectedTab);
			$("#thresholdForm"+index).form('submit',{
				success:function(data){
					//alert(data);
					//$.messager.progress('close');	// hide progress bar while submit successfully
					//关闭VoLTE专题分析的所有TAB
						parent.closeTab('VoLTE总体概览');
						parent.closeTab('VoLTE语音质差');
						parent.closeTab('VoLTE语音未接通');
						parent.closeTab('VoLTE语音掉话');
						parent.closeTab('IMS注册失败');
						parent.closeTab('CSFB失败');
						parent.closeTab('系统内切换失败');
						parent.closeTab('SRVCC切换失败');
						parent.selectTab('测试日志');
					$.messager.alert('提示','修改成功','info');
				}
			});
			//document.forms[index].submit();
		}
		/* 重置初始值 */
		function resetForm(){
			$.messager.confirm('提示','你确认要重置为初始值?',function(flag){
				if(flag){
					var selectedTab = $("#tt").tabs('getSelected');
					var tabsIndex = $('#tt').tabs('getTabIndex',selectedTab);
					$.ajax({
						url:"${pageContext.request.contextPath}/json/streamDissThresholdInitialValue.json",
						dataType:"json",
						type:"post",
						success:function(jsonData){
							for ( var index in jsonData) {
								$("#"+jsonData[index].thresholdType+jsonData[index].nameEn+tabsIndex+"currentThreshold").combobox('setValue',jsonData[index].currentThreshold);
								$("#"+jsonData[index].thresholdType+jsonData[index].nameEn+tabsIndex+"threshold1").textbox('setValue',jsonData[index].threshold1);
								$("#"+jsonData[index].thresholdType+jsonData[index].nameEn+tabsIndex+"threshold2").textbox('setValue',jsonData[index].threshold2);
								$("#"+jsonData[index].thresholdType+jsonData[index].nameEn+tabsIndex+"threshold3").textbox('setValue',jsonData[index].threshold3);
							}
						}
					});
				}
			});
		}
	</script>
  </head>
  <body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div data-options="region:'center',border:false">
  		<div class="easyui-tabs" id="tt" data-options="fit:true,plain:true,tabWidth:100,border:false">
			<div data-options="closeable:'false',title:'问题判决门限',cache:false" style="padding:4px;overflow-y:auto;">
				<form id="thresholdForm0" method="post" action="${pageContext.request.contextPath}//aboutThreshold/saveStreamDissThreshold.action" >
				<!-- <div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">视频质差</div></div> -->
		    	<table>
		    	
		    		<c:forEach items="${StreamVideoQualityBad}" var="quality" varStatus="status">
			    		<tr>
			    		
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    		<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}0currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	
						    	</td><td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}0threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}0threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}0threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
				    		</td>
			    		</tr>
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		</c:forEach>
		    	</table>
		    	
		    	</form>
			</div>

			<div data-options="closeable:'false',title:'视频质差门限',cache:false" style="padding:4px;overflow-y:auto;">
		    	<form id="thresholdForm1"  method="post" action="${pageContext.request.contextPath}//aboutThreshold/saveStreamDissThreshold.action" >
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">关键参数原因门限：</div></div> 
		    	<table>
		    		<c:forEach items="${StreamKeyParameterCause}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		  		<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">无线问题原因门限之公共：</div></div>
		    	<table>
		    		<c:forEach items="${StreamWirelessProblemCausePublic}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">无线问题原因门限之乒乓切换：</div></div>
		    	<table>
		    		<c:forEach items="${StreamWirelessProblemCausePingPong}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">无线问题原因门限之邻区问题：</div></div>
		    	<table>
		    		<c:forEach items="${StreamWirelessProblemCauseAdjacentregion}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">无线问题原因门限之弱覆盖：</div></div>
		    	<table>
		    		<c:forEach items="${StreamWirelessProblemCauseWeakcover}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">无线问题原因门限之干扰：</div></div>
		    	<table>
		    		<c:forEach items="${StreamWirelessProblemCauseDisturb}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">无线问题原因门限之重叠覆盖：</div></div>
		    	<table>
		    		<c:forEach items="${StreamWirelessProblemCauseOvercover}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">无线问题原因门限之下行调度小：</div></div>
		    	<table>
		    		<c:forEach items="${StreamWirelessProblemCauseDownDispatchSmall}" var="quality" varStatus="status">
			    		<tr>
				    		<td><div class="titleDivShow"><strong>${quality.nameCh}</strong></div></td>
				    		<td>
				    			<c:if test="${status.index==0}">
				    			<div class="inputDivShow" type="hidden">当前使用
						    		<select id="${quality.thresholdType}${quality.nameEn}1currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" class="easyui-combobox" data-options="required:true" >
										<option <c:if test="${quality.currentThreshold==quality.threshold1}">selected</c:if> value="1">门限1</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold2}">selected</c:if> value="2">门限2</option>
										<option <c:if test="${quality.currentThreshold==quality.threshold3}">selected</c:if> value="3">门限3</option>
						    		</select>
						    	</div>
						    	</c:if>
						    	</td>
						    	<td>
						    	<div class="inputDivShow">门限1
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold1" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold1" value="${quality.threshold1}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限2
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold2" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold2" value="${quality.threshold2}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'" />
						    	</div>
						    	<div class="inputDivShow">门限3
						    		<input id="${quality.thresholdType}${quality.nameEn}1threshold3" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].threshold3" value="${quality.threshold3}"  class="easyui-textbox" data-options="required:true,validType:'length[1,24]'<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">,readonly:false</shiroextend:hasAnyPermissions>" />
						    	</div>
				    		</td>
			    		</tr>
			    		
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
			    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
		    		
		    		</c:forEach>
		    	</table>
		    	</form>
			</div>
	    </div>
  	</div>
  	<div data-options="region:'south',border:false" style="height:40px;">
	   	<table width="100%" style="border-top:1px solid #95b8e7;">
	   		<tr height="35px">
	    		<td width="50%;" align="right">
	    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:save">
	    				<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >确认</a>
	    			</shiroextend:hasAnyPermissions>
	    		</td>
	    		<td width="50%;">
	    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:init">
	    				<a class="easyui-linkbutton" iconCls="icon-reload" style="width:100px;" onclick="resetForm();" >重置</a>
	    			</shiroextend:hasAnyPermissions>
	    		</td>
	   		</tr>
	   	</table>
  	</div>
  </body>
</html>
