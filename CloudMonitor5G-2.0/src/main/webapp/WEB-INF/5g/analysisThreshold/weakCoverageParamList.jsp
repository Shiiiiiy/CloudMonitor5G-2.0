<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>弱覆盖参数设置</title>

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
		    padding-right: 0;
		    text-align: left;
		    width: 100%;
   			padding-left: 10px;
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

	</style>
	<script type="text/javascript">
		$(function(){
			//是否在页面加载完成的时候加载门限参数的初始值缓存到页面?
		});
		/* 保存修改 */
		function submitForm(){
		    var fieldNameEn = "";
		    var fieldValue = "";
			var rsrpThresholdForWeakCoverageLte = $("#rsrpThresholdForWeakCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "rsrpThresholdForWeakCoverageLte" + ",";
			fieldValue = fieldValue + rsrpThresholdForWeakCoverageLte + ",";
			
			var samplingPointRatioForWeakCoverageLte = $("#samplingPointRatioForWeakCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "samplingPointRatioForWeakCoverageLte" + ",";
			fieldValue = fieldValue + samplingPointRatioForWeakCoverageLte + ",";
			
			var cellSamplingPointNumberForWeakCoverageLte = $("#cellSamplingPointNumberForWeakCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointNumberForWeakCoverageLte" + ",";
			fieldValue = fieldValue + cellSamplingPointNumberForWeakCoverageLte + ",";
			
			var nCellStrongestbeamRSRPForWeakCoverageNR = $("#nCellStrongestbeamRSRPForWeakCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellStrongestbeamRSRPForWeakCoverageNR" + ",";
			fieldValue = fieldValue + nCellStrongestbeamRSRPForWeakCoverageNR + ",";
			
			var samplingPointRatioForWeakCoverageNR = $("#samplingPointRatioForWeakCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "samplingPointRatioForWeakCoverageNR" + ",";
			fieldValue = fieldValue + samplingPointRatioForWeakCoverageNR + ",";
			
			var nCellSamplingPointNumberForWeakCoverageNR = $("#nCellSamplingPointNumberForWeakCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellSamplingPointNumberForWeakCoverageNR" + ",";
			fieldValue = fieldValue + nCellSamplingPointNumberForWeakCoverageNR + ",";
			
			var baseSsbOrCsiAnalysisForWeakCoverageNR = $('input[name="baseSsbOrCsiAnalysisForWeakCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "baseSsbOrCsiAnalysisForWeakCoverageNR" + ",";
			fieldValue = fieldValue + baseSsbOrCsiAnalysisForWeakCoverageNR + ",";
			
			var andOrRelationshipForWeakCoverageNR = $('input[name="andOrRelationshipForWeakCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "andOrRelationshipForWeakCoverageNR" + ",";
			fieldValue = fieldValue + andOrRelationshipForWeakCoverageNR + ",";
			
			var flag = $("#whetherSameConsiderLteCoverForWeakCoverageNR").is(":checked");
			if(flag){
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForWeakCoverageNR";
			fieldValue = fieldValue + "1";
			}else{
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForWeakCoverageNR";
			fieldValue = fieldValue + "0";
			}
			
			$.ajax({
				url:"${pageContext.request.contextPath}/coverageParam/saveWeakCoverThreshold.action",
				dataType:"json",
				type:"post",
				data: {'fieldNameEn':fieldNameEn,'fieldValue':fieldValue},
				success:function(result){
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg,'error');
					} else {
						$.messager.alert('提示','修改成功','info');
					}
				}
			});
			
		}
		/* 重置初始值 */
		function resetForm(){
			$.messager.confirm('提示','你确认要重置为初始值?',function(flag){
				if(flag){
					$("#rsrpThresholdForWeakCoverageLte").textbox('setValue',"-125");
					$("#samplingPointRatioForWeakCoverageLte").textbox('setValue',"20");
					$("#cellSamplingPointNumberForWeakCoverageLte").textbox('setValue',"1000");
					$("#nCellStrongestbeamRSRPForWeakCoverageNR").textbox('setValue',"-125");
					$("#samplingPointRatioForWeakCoverageNR").textbox('setValue',"20");
					$("#nCellSamplingPointNumberForWeakCoverageNR").textbox('setValue',"1000");
					
					$("#baseSsbOrCsiAnalysisForWeakCoverageNRssb").prop("checked", true); 
					$("#andOrRelationshipForWeakCoverageNRand").prop("checked", true); 
					$("#whetherSameConsiderLteCoverForWeakCoverageNR").prop("checked", true); 
				}
			});
		}
		
		
	</script>
  </head>
  <body class="easyui-layout" style="width:100%;height: 100%;margin: 0 auto;list-style:none;overflow:hidden;">
  	<div style="width:49%;height:90%;float:left; margin-right: 10px;border:1px solid #95b8e7;">
	  	<div data-options="region:'west',border:false,split:true">
	  		<div style="width:100%;height:50%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
					<div class="panel-heading">
						<div class="panel-title">LTE弱覆盖判定</div>
					</div>
				</div>
	   			<div class="inputDivShow">LTE锚点小区RSRP<=
		    		<input id="rsrpThresholdForWeakCoverageLte" name="rsrpThresholdForWeakCoverageLte" value="${EmbbWeakCoverAnasis.rsrpThresholdForWeakCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow">LTE弱覆盖采样点占比>=
			    		<input id="samplingPointRatioForWeakCoverageLte" name="samplingPointRatioForWeakCoverageLte" value="${EmbbWeakCoverAnasis.samplingPointRatioForWeakCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="cellSamplingPointNumberForWeakCoverageLte" name="cellSamplingPointNumberForWeakCoverageLte" value="${EmbbWeakCoverAnasis.cellSamplingPointNumberForWeakCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个
			    	</div>
			    </div>
			 </div>
		</div>
	</div>
	<div style="width:50%;height:90%;float:left;border:1px solid #95b8e7;">
	  	<div data-options="region:'east',border:false,split:true">
	  		<div style="width:100%;height:50%;">
				<div style="height:16px;background-color:#e8f1ff;padding:5px;border-bottom:1px solid #95b8e7;">
					<div class="panel-heading">
						<div class="panel-title">5G弱覆盖判定</div>
					</div>
				</div>
				<div class="inputDivShow">
					<input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForWeakCoverageNRcsi" name="baseSsbOrCsiAnalysisForWeakCoverageNR" <c:if test="${EmbbWeakCoverAnasis.baseSsbOrCsiAnalysisForWeakCoverageNR == 'csi'}">checked="checked"</c:if> value="csi">基于CSI-RSRP进行分析
		            <input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForWeakCoverageNRssb" name="baseSsbOrCsiAnalysisForWeakCoverageNR" <c:if test="${EmbbWeakCoverAnasis.baseSsbOrCsiAnalysisForWeakCoverageNR == 'ssb'}">checked="checked"</c:if> value="ssb">基于SSB-RSRP进行分析
                </div>
	   			<div class="inputDivShow">NR辅小区最强的Beam RSRP<=
		    		<input id="nCellStrongestbeamRSRPForWeakCoverageNR" name="nCellStrongestbeamRSRPForWeakCoverageNR" value="${EmbbWeakCoverAnasis.nCellStrongestbeamRSRPForWeakCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    	<div class="inputDivShow">
               		<input type="checkbox" style="width:10px" id="whetherSameConsiderLteCoverForWeakCoverageNR"   name="whetherSameConsiderLteCoverForWeakCoverageNR"  <c:if test="${EmbbWeakCoverAnasis.whetherSameConsiderLteCoverForWeakCoverageNR == 1}">checked="checked"</c:if> value="1" >是否同时考虑LTE覆盖情况
                	<input type="radio" style="width:10px" id="andOrRelationshipForWeakCoverageNRand" name="andOrRelationshipForWeakCoverageNR" <c:if test="${EmbbWeakCoverAnasis.andOrRelationshipForWeakCoverageNR == 'And'}">checked="checked"</c:if> value="And">且
		            <input type="radio" style="width:10px" id="andOrRelationshipForWeakCoverageNRor" name="andOrRelationshipForWeakCoverageNR" <c:if test="${EmbbWeakCoverAnasis.andOrRelationshipForWeakCoverageNR == 'Or'}">checked="checked"</c:if> value="Or">或
                </div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow">NR弱覆盖采样点占比>=
			    		<input id="samplingPointRatioForWeakCoverageNR" name="samplingPointRatioForWeakCoverageNR" value="${EmbbWeakCoverAnasis.samplingPointRatioForWeakCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="nCellSamplingPointNumberForWeakCoverageNR" name="nCellSamplingPointNumberForWeakCoverageNR" value="${EmbbWeakCoverAnasis.nCellSamplingPointNumberForWeakCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个
			    	</div>
			    </div>
			 </div>
		</div>
	</div>
  	<div data-options="region:'south',border:false" style="height:10%;">
	   	<table width="100%" style="border-top:1px solid #95b8e7;">
	   		<tr height="35px">
	    		<td width="50%;" align="right">
	    			<shiroextend:hasAnyPermissions name="volteanalysisthreshold:save">
	    				<a id="saveButton" iconCls="icon-ok" class="easyui-linkbutton" style="width:100px;" onclick="submitForm();"  >保存</a>
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
