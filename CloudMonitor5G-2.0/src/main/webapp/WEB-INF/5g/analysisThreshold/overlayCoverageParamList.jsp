<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>重叠覆盖参数设置</title>

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
   		.incorrect:before {
		    content: '\2716';
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
			var cellDifferenceForOverlayCoverageLte = $("#cellDifferenceForOverlayCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellDifferenceForOverlayCoverageLte" + ",";
			fieldValue = fieldValue + cellDifferenceForOverlayCoverageLte + ",";
			
			var cellNumForOverlayCoverageLte = $("#cellNumForOverlayCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellNumForOverlayCoverageLte" + ",";
			fieldValue = fieldValue + cellNumForOverlayCoverageLte + ",";
			
			var rsrpThresholdForOverlayCoverageLte = $("#rsrpThresholdForOverlayCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "rsrpThresholdForOverlayCoverageLte" + ",";
			fieldValue = fieldValue + rsrpThresholdForOverlayCoverageLte + ",";
			
			var samplingPointRatioForOverlayCoverageLte = $("#samplingPointRatioForOverlayCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "samplingPointRatioForOverlayCoverageLte" + ",";
			fieldValue = fieldValue + samplingPointRatioForOverlayCoverageLte + ",";
			
			var cellSamplingPointNumberForOverlayCoverageLte = $("#cellSamplingPointNumberForOverlayCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointNumberForOverlayCoverageLte" + ",";
			fieldValue = fieldValue + cellSamplingPointNumberForOverlayCoverageLte + ",";
			
			var nCellRsrpThresholdForOverlayCoverageNR = $("#nCellRsrpThresholdForOverlayCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellRsrpThresholdForOverlayCoverageNR" + ",";
			fieldValue = fieldValue + nCellRsrpThresholdForOverlayCoverageNR + ",";
			
			var cellBeamDifferenceForOverlayCoverageNR = $("#cellBeamDifferenceForOverlayCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellBeamDifferenceForOverlayCoverageNR" + ",";
			fieldValue = fieldValue + cellBeamDifferenceForOverlayCoverageNR + ",";
			
			var cellSamplingPointRatioForOverlayCoverageNR = $("#cellSamplingPointRatioForOverlayCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointRatioForOverlayCoverageNR" + ",";
			fieldValue = fieldValue + cellSamplingPointRatioForOverlayCoverageNR + ",";
			
			var nCellSamplingPointNumberForOverlayCoverageNR = $("#nCellSamplingPointNumberForOverlayCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellSamplingPointNumberForOverlayCoverageNR" + ",";
			fieldValue = fieldValue + nCellSamplingPointNumberForOverlayCoverageNR + ",";
			
			var baseSsbOrCsiAnalysisForOverlayCoverageNR = $('input[name="baseSsbOrCsiAnalysisForOverlayCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "baseSsbOrCsiAnalysisForOverlayCoverageNR" + ",";
			fieldValue = fieldValue + baseSsbOrCsiAnalysisForOverlayCoverageNR + ",";
			
			var andOrRelationshipForOverlayCoverageNR = $('input[name="andOrRelationshipForOverlayCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "andOrRelationshipForOverlayCoverageNR" + ",";
			fieldValue = fieldValue + andOrRelationshipForOverlayCoverageNR + ",";
			
			var flag = $("#whetherSameConsiderLteCoverForOverlayCoverageNR").is(":checked");
			if(flag){
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForOverlayCoverageNR";
			fieldValue = fieldValue + "1";
			}else{
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForOverlayCoverageNR";
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
					$("#cellDifferenceForOverlayCoverageLte").textbox('setValue',"6");
					$("#cellNumForOverlayCoverageLte").textbox('setValue',"4");
					$("#rsrpThresholdForOverlayCoverageLte").textbox('setValue',"-85");
					$("#samplingPointRatioForOverlayCoverageLte").textbox('setValue',"20");
					$("#cellSamplingPointNumberForOverlayCoverageLte").textbox('setValue',"1000");
					
					$("#nCellRsrpThresholdForOverlayCoverageNR").textbox('setValue',"-85");
					$("#cellBeamDifferenceForOverlayCoverageNR").textbox('setValue',"6");
					$("#cellSamplingPointRatioForOverlayCoverageNR").textbox('setValue',"20");
					$("#nCellSamplingPointNumberForOverlayCoverageNR").textbox('setValue',"1000");
					
					$("#baseSsbOrCsiAnalysisForOverlayCoverageNRssb").prop("checked", true); 
					$("#andOrRelationshipForOverlayCoverageNRand").prop("checked", true); 
					$("#whetherSameConsiderLteCoverForOverlayCoverageNR").prop("checked", true); 
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
						<div class="panel-title">LTE重叠覆盖定义</div>
					</div>
				</div>
	   			<div class="inputDivShow">LTE锚点小区RSRP-LTE同频邻区RSRP<
		    		<input id="cellDifferenceForOverlayCoverageLte" name="cellDifferenceForOverlayCoverageLte" style="width:120px;" value="${EmbbOverlayCoverAnasis.cellDifferenceForOverlayCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100 " />
		    		dB的邻区数量>=
		    		<input id="cellNumForOverlayCoverageLte" name="cellNumForOverlayCoverageLte" style="width:120px;" value="${EmbbOverlayCoverAnasis.cellNumForOverlayCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10 " />
		    		个
		    	</div>
		    	<div class="inputDivShow">且&nbsp;&nbsp;采样点RSRP>=
		    		<input id="rsrpThresholdForOverlayCoverageLte" name="rsrpThresholdForOverlayCoverageLte" style="width:120px;" value="${EmbbOverlayCoverAnasis.rsrpThresholdForOverlayCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow">LTE重叠覆盖采样点占比>=
			    		<input id="samplingPointRatioForOverlayCoverageLte" name="samplingPointRatioForOverlayCoverageLte" style="width:120px;"  value="${EmbbOverlayCoverAnasis.samplingPointRatioForOverlayCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="cellSamplingPointNumberForOverlayCoverageLte" name="cellSamplingPointNumberForOverlayCoverageLte" style="width:120px;"  value="${EmbbOverlayCoverAnasis.cellSamplingPointNumberForOverlayCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为重叠覆盖LTE锚点小区
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
						<div class="panel-title">5G重叠覆盖定义</div>
					</div>
				</div>
				<div class="inputDivShow">
					<input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForOverlayCoverageNRcsi" name="baseSsbOrCsiAnalysisForOverlayCoverageNR" <c:if test="${EmbbOverlayCoverAnasis.baseSsbOrCsiAnalysisForOverlayCoverageNR == 'csi'}">checked="checked"</c:if> value="csi">基于CSI-RSRP进行分析
		            <input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForOverlayCoverageNRssb" name="baseSsbOrCsiAnalysisForOverlayCoverageNR" <c:if test="${EmbbOverlayCoverAnasis.baseSsbOrCsiAnalysisForOverlayCoverageNR == 'ssb'}">checked="checked"</c:if> value="ssb">基于SSB-RSRP进行分析
                </div>
	   			<div class="inputDivShow">采样点NR RSRP>=
		    		<input id="nCellRsrpThresholdForOverlayCoverageNR" name="nCellRsrpThresholdForOverlayCoverageNR" style="width:120px;"  value="${EmbbOverlayCoverAnasis.nCellRsrpThresholdForOverlayCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 "/>
		    		dBm
		    	</div>
		    	<div class="inputDivShow">首强beam RSRP与次强beam RSRP的差值<=
		    		<input id="cellBeamDifferenceForOverlayCoverageNR" name="cellBeamDifferenceForOverlayCoverageNR" style="width:120px;" value="${EmbbOverlayCoverAnasis.cellBeamDifferenceForOverlayCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100 " />
		    		dB
		    	</div>
		    	<div class="inputDivShow">
               		<input type="checkbox" style="width:10px" id="whetherSameConsiderLteCoverForOverlayCoverageNR"   name="whetherSameConsiderLteCoverForOverlayCoverageNR"  <c:if test="${EmbbOverlayCoverAnasis.whetherSameConsiderLteCoverForOverlayCoverageNR == 1}">checked="checked"</c:if> value="1" >是否同时考虑LTE覆盖情况
                	<input type="radio" style="width:10px" id="andOrRelationshipForOverlayCoverageNRand" name="andOrRelationshipForOverlayCoverageNR" <c:if test="${EmbbOverlayCoverAnasis.andOrRelationshipForOverlayCoverageNR == 'And'}">checked="checked"</c:if> value="And">且
		            <input type="radio" style="width:10px" id="andOrRelationshipForOverlayCoverageNRor" name="andOrRelationshipForOverlayCoverageNR" <c:if test="${EmbbOverlayCoverAnasis.andOrRelationshipForOverlayCoverageNR == 'Or'}">checked="checked"</c:if> value="Or">或
                </div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow" >NR重叠覆盖采样点占比>=
			    		<input id="cellSamplingPointRatioForOverlayCoverageNR"  name="cellSamplingPointRatioForOverlayCoverageNR" style="width:120px;"  value="${EmbbOverlayCoverAnasis.cellSamplingPointRatioForOverlayCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="nCellSamplingPointNumberForOverlayCoverageNR" name="nCellSamplingPointNumberForOverlayCoverageNR" style="width:120px;"  value="${EmbbOverlayCoverAnasis.nCellSamplingPointNumberForOverlayCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为重叠覆盖NR辅小区
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
