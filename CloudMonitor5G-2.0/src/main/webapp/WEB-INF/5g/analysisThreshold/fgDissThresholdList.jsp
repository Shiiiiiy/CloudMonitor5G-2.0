<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>5G专题分析门限(参数设置)</title>

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
   		.inputDivShow1{
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: center;
		    width: 160px;
   		}
   		.inputDivShow2{
			display: inline-block; *
			display: inline;
		    font-size: 12px;
		    margin: 5px;
		    padding-left: 0;
		    padding-right: 0;
		    text-align: center;
		    width: 80px;
   		}
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
   		.inputDivShow input,.inputDivShow1 input{
   			width:100px;
   		}
   		.inputDivShow select.inputDivShow1 select{
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
					//parent.closeTab('VoLTE总体概览');
					//parent.closeTab('VoLTE语音质差');
					//parent.closeTab('VoLTE语音未接通');
					///parent.closeTab('VoLTE语音掉话');
					//parent.closeTab('IMS注册失败');
					//parent.closeTab('CSFB失败');
					//parent.closeTab('系统内切换失败');
					//parent.closeTab('SRVCC切换失败');
					//parent.selectTab('测试日志');
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
						url:"${pageContext.request.contextPath}/json/volteDissThresholdInitialValue.json",
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
		
		
		
		function mapParamSel(record){
			var mapParamSelOptions =  $('#mapParamSel').combobox('getData');
			for ( var i = 0 ; i < mapParamSelOptions.length ; i++ ){
				if(record.value==mapParamSelOptions[i].value){
					$('#'+mapParamSelOptions[i].value).css('display','block');
				}else{
					$('#'+mapParamSelOptions[i].value).css('display','none');
				}
			}
		}
		
		
		function addRow(){
			var mapParamSelOption =  $('#mapParamSel').combobox('getValue');
			var inHtml = '<div class="'+mapParamSelOption+'_context">'+
				'<div class="inputDivShow1"><input class="easyui-numberbox" data-options="required:true"  /></div>&nbsp;'+
				'<div class="inputDivShow2"><<strong>'+mapParamSelOption+'</strong><=</div>&nbsp;'+
				'<div class="inputDivShow1"><input class="easyui-numberbox" data-options="required:true"  /></div>&nbsp;'+
				'<div class="inputDivShow1"><input type="color" value="#00b050"  style="border:none;padding:0;background-color:white;"/></div>&nbsp;'+
				'</div>';
			$('.'+mapParamSelOption+'_last').before(inHtml);
			$('.easyui-numberbox').numberbox();
		}
		
		function removeRow(){
			var mapParamSelOption =  $('#mapParamSel').combobox('getValue');
			$('.'+mapParamSelOption+'_context:last').remove();
		}
	</script>
  </head>
  <body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div data-options="region:'center',border:false">
		<form id="thresholdForm0" method="post" action="${pageContext.request.contextPath}/aboutThreshold/saveVolteDissThreshold.action" >
			<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">质差分析参数</div></div>
    		<c:forEach items="${DisturbAnalysis}" var="quality" varStatus="status">
    			<div class="titleDivShow"><strong>${quality.nameCh}</strong></div>
    			<div class="inputDivShow" >门限值：
		    		<input id="${quality.thresholdType}${quality.nameEn}0currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" value="${quality.currentThreshold}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'"  />
		    	</div>
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
    			<c:if test="${status.index%2==1}"><br/></c:if>
    		</c:forEach>
	    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">eMBB覆盖类专题分析参数</div></div>
    		<c:forEach items="${EmbbCoverAnalysis}" var="quality" varStatus="status">
	    		<div class="titleDivShow"><strong>${quality.nameCh}</strong></div>
    			<div class="inputDivShow" >门限值：
		    		<input id="${quality.thresholdType}${quality.nameEn}0currentThreshold" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].currentThreshold" value="${quality.currentThreshold}" class="easyui-textbox" data-options="required:true,validType:'length[1,24]'"  />
		    	</div>
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].id" value="${quality.id}" />
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameEn" value="${quality.nameEn}" />
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].nameCh" value="${quality.nameCh}" />
	    		<input type="hidden" name="thresholdMap['${quality.thresholdType}_${quality.nameEn}'].thresholdType" value="${quality.thresholdType}" />
    			<c:if test="${status.index%2==1}"><br/></c:if>
    		</c:forEach>
	    	<div style="height:16px;background-color:#e8f1ff;padding:5px;border:1px solid #95b8e7;"><div class="panel-title">地图参数</div></div>
	    	
    		<div class="inputDivShow1">
    			<select id="mapParamSel" class="easyui-combobox" name="dept" data-options="onSelect:mapParamSel" style="width:100px;">
	    			<c:forEach items="${MapParam}" var="quality" varStatus="status">
					    <option value="${quality[0].nameCh}" <c:if test="${status.first}">selected="selected"</c:if> >${quality[0].nameCh}</option>
					 </c:forEach>
				</select>
			</div>
			<c:forEach items="${MapParam}" var="quality" varStatus="status">
	    		<div id="${quality[0].nameCh}" <c:if test="${!status.first}">style="display:none;"</c:if> >
		    		<c:forEach items="${quality}" var="row" varStatus="status_inner">
			    		<c:if test="${status_inner.first}">
				    		<div class="${row.nameCh}_first">
				    			<div class="inputDivShow1"><input value="${row.begin}" class="easyui-textbox" data-options="required:true,editable:false"  /></div>
				    			<div class="inputDivShow2"><<strong>${row.nameCh}</strong><=</div>
				    			<div class="inputDivShow1"><input value="${row.end}" class="easyui-numberbox" data-options="required:true"  /></div>
				    			<div class="inputDivShow1"><input type="color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
				    		</div>
			    		</c:if>
			    		<c:if test="${status_inner.last}">
				    		<div class="${row.nameCh}_last">
				    			<div class="inputDivShow1"><input value="${row.begin}" class="easyui-numberbox" data-options="required:true"  /></div>
				    			<div class="inputDivShow2"><<strong>${row.nameCh}</strong><</div>
				    			<div class="inputDivShow1"><input value="${row.end}" class="easyui-textbox" data-options="required:true,editable:false"  /></div>
				    			<div class="inputDivShow1"><input type="color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
				    			<div class="inputDivShow2"></div>
				    			<a class="easyui-linkbutton" data-options="plain:true" iconCls="icon-add" style="width:40px;"  onclick="addRow();"  ></a>
				    			<a class="easyui-linkbutton" data-options="plain:true" iconCls="icon-remove" style="width:40px;" onclick="removeRow();" ></a>
				    		</div>
			    		</c:if>
			    		<c:if test="${!status_inner.last&&!status_inner.first}">
			    			<div class="${row.nameCh}_context">
				    			<div class="inputDivShow1"><input value="${row.begin}" class="easyui-numberbox" data-options="required:true"  /></div>
				    			<div class="inputDivShow2"><<strong>${row.nameCh}</strong><=</div>
				    			<div class="inputDivShow1"><input value="${row.end}" class="easyui-numberbox" data-options="required:true"  /></div>
				    			<div class="inputDivShow1"><input type="color" value="${row.color}"  style="border:none;padding:0;background-color:white;"/></div>
			    			</div>
			    		</c:if>
	    			</c:forEach>
	    		</div>
  			</c:forEach>
	    </form>
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
