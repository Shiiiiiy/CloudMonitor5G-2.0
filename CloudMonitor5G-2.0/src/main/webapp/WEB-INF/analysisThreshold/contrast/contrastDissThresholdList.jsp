<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>对比分析门限</title>

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
		/* 保存修改 ,同时关闭相关界面*/
		function submitForm(){
			//$.messager.progress();	// display the progress bar
			var selectedTab = $("#tt").tabs('getSelected');
			var index = $('#tt').tabs('getTabIndex',selectedTab);
			$("#thresholdForm").form('submit',{
				success:function(data){
					//关闭对比分析的所有TAB
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
						url:"${pageContext.request.contextPath}/json/contrastDissThresholdInitialValue.json",
						dataType:"json",
						type:"post",
						success:function(jsonData){
							
							for ( var index in jsonData) {
								$("#"+jsonData[index].nameEn+tabsIndex+"value").textbox('setValue',jsonData[index].value);
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
			<div data-options="closeable:'false',title:'对比分析门限',cache:false" style="padding:4px;overflow-y:auto;">
				<form id="thresholdForm" method="post" action="${pageContext.request.contextPath}/contrastThreshold/saveContrastDissThreshold.action" >
		    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">栅格对比</div></div>
		    	<table>
		    	
		    		<c:forEach items="${ContrastList}" var="quality" varStatus="status">
			    		<c:choose>
						    <c:when test='${quality.nameEn eq "GridSize"}'>
						      <tr>
						    		<td><div class="titleDivShow"><strong>${quality.thresholdType}</strong></div></td>
						    		<td>
							    		<div class="inputDivShow" type="hidden">门限值:
									    	<select id="${quality.nameEn}0value" name="aboutThresholdMap['${quality.nameEn}_${status.index}'].value"  class="easyui-combobox" >
									    		<option <c:if test="${quality.value eq '50'}">selected</c:if> value=50>50</option>
									    		<option <c:if test="${quality.value eq '100'}">selected</c:if> value=100>100</option>
									    		<option <c:if test="${quality.value eq '500'}">selected</c:if> value=500>500</option>
									    	</select>
									   </div> 
								   </td>
					    		</tr>
						    </c:when>
						    
						    <c:otherwise>
						        <tr>
						    		<td><div class="titleDivShow"><strong>${quality.thresholdType}</strong></div></td>
						    		<td>
						    		<div class="inputDivShow" type="hidden">门限值:
								    	<input id="${quality.nameEn}0value" name="aboutThresholdMap['${quality.nameEn}_${status.index}'].value" value="${quality.value}" class="easyui-textbox" />
								   
								   </div> </td>
					    		</tr>
						    </c:otherwise>
						</c:choose>
			    		
			    		<input id="${quality.nameEn}0id" type="hidden" name="aboutThresholdMap['${quality.nameEn}_${status.index}'].id"value="${quality.id}" />
			    		<input id="${quality.nameEn}0thresholdType" type="hidden" name="aboutThresholdMap['${quality.nameEn}_${status.index}'].thresholdType" value="${quality.thresholdType}" />
			    		<input id="${quality.nameEn}0nameEn" type="hidden" name="aboutThresholdMap['${quality.nameEn}_${status.index}'].nameEn" value="${quality.nameEn}" />
			    			
			    		
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
