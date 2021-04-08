<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiroextend" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>过覆盖参数设置</title>

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
			var cellRadiusMultipleForOverCoverageLte = $("#cellRadiusMultipleForOverCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellRadiusMultipleForOverCoverageLte" + ",";
			fieldValue = fieldValue + cellRadiusMultipleForOverCoverageLte + ",";
			
			var rsrpThresholdForOverCoverageLte = $("#rsrpThresholdForOverCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "rsrpThresholdForOverCoverageLte" + ",";
			fieldValue = fieldValue + rsrpThresholdForOverCoverageLte + ",";
			
			var samplingPointRatioForOverCoverageLte = $("#samplingPointRatioForOverCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "samplingPointRatioForOverCoverageLte" + ",";
			fieldValue = fieldValue + samplingPointRatioForOverCoverageLte + ",";
			
			var cellSamplingPointNumberForOverCoverageLte = $("#cellSamplingPointNumberForOverCoverageLte").textbox('getValue');
			fieldNameEn = fieldNameEn + "cellSamplingPointNumberForOverCoverageLte" + ",";
			fieldValue = fieldValue + cellSamplingPointNumberForOverCoverageLte + ",";
			
			var nCellBeamRadiusMultipleForOverCoverageNR = $("#nCellBeamRadiusMultipleForOverCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellBeamRadiusMultipleForOverCoverageNR" + ",";
			fieldValue = fieldValue + nCellBeamRadiusMultipleForOverCoverageNR + ",";
			
			var nCellMinBeamRsrpForOverCoverageNR = $("#nCellMinBeamRsrpForOverCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellMinBeamRsrpForOverCoverageNR" + ",";
			fieldValue = fieldValue + nCellMinBeamRsrpForOverCoverageNR + ",";
			
			var nCellsamplingPointRatioForOverCoverageNR = $("#nCellsamplingPointRatioForOverCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellsamplingPointRatioForOverCoverageNR" + ",";
			fieldValue = fieldValue + nCellsamplingPointRatioForOverCoverageNR + ",";
			
			var nCellSamplingPointNumberForOverCoverageNR = $("#nCellSamplingPointNumberForOverCoverageNR").textbox('getValue');
			fieldNameEn = fieldNameEn + "nCellSamplingPointNumberForOverCoverageNR" + ",";
			fieldValue = fieldValue + nCellSamplingPointNumberForOverCoverageNR + ",";
			
			var baseSsbOrCsiAnalysisForOverCoverageNR = $('input[name="baseSsbOrCsiAnalysisForOverCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "baseSsbOrCsiAnalysisForOverCoverageNR" + ",";
			fieldValue = fieldValue + baseSsbOrCsiAnalysisForOverCoverageNR + ",";
			
			var andOrRelationshipForOverCoverageNR = $('input[name="andOrRelationshipForOverCoverageNR"]:checked ').val();
			fieldNameEn = fieldNameEn + "andOrRelationshipForOverCoverageNR" + ",";
			fieldValue = fieldValue + andOrRelationshipForOverCoverageNR + ",";
			
			var flag = $("#whetherSameConsiderLteCoverForOverCoverageNR").is(":checked");
			if(flag){
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForOverCoverageNR";
			fieldValue = fieldValue + "1";
			}else{
			fieldNameEn = fieldNameEn + "whetherSameConsiderLteCoverForOverCoverageNR";
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
					$("#cellRadiusMultipleForOverCoverageLte").textbox('setValue',"4");
					$("#rsrpThresholdForOverCoverageLte").textbox('setValue',"-85");
					$("#samplingPointRatioForOverCoverageLte").textbox('setValue',"20");
					$("#cellSamplingPointNumberForOverCoverageLte").textbox('setValue',"1000");
					
					$("#nCellBeamRadiusMultipleForOverCoverageNR").textbox('setValue',"4");
					$("#nCellMinBeamRsrpForOverCoverageNR").textbox('setValue',"-85");
					$("#nCellsamplingPointRatioForOverCoverageNR").textbox('setValue',"20");
					$("#nCellSamplingPointNumberForOverCoverageNR").textbox('setValue',"1000");
					
					$("#baseSsbOrCsiAnalysisForOverCoverageNRssb").prop("checked", true); 
					$("#andOrRelationshipForOverCoverageNRand").prop("checked", true); 
					$("#whetherSameConsiderLteCoverForOverCoverageNR").prop("checked", true); 
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
						<div class="panel-title">LTE过覆盖定义</div>
					</div>
				</div>
	   			<div class="inputDivShow">采样点距离LTE锚点小区的距离>
		    		<input id="cellRadiusMultipleForOverCoverageLte" name="cellRadiusMultipleForOverCoverageLte" style="width:120px;" value="${EmbbOverCoverAnasis.cellRadiusMultipleForOverCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100 " />
		    		<span class="status incorrect"></span>&nbsp;LTE小区覆盖半径
		    	</div>
		    	<div class="inputDivShow">采样点RSRP>=
		    		<input id="rsrpThresholdForOverCoverageLte" name="rsrpThresholdForOverCoverageLte" style="width:120px;" value="${EmbbOverCoverAnasis.rsrpThresholdForOverCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow">LTE过覆盖采样点占比>=
			    		<input id="samplingPointRatioForOverCoverageLte" name="samplingPointRatioForOverCoverageLte" style="width:120px;"  value="${EmbbOverCoverAnasis.samplingPointRatioForOverCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="cellSamplingPointNumberForOverCoverageLte" name="cellSamplingPointNumberForOverCoverageLte" style="width:120px;"  value="${EmbbOverCoverAnasis.cellSamplingPointNumberForOverCoverageLte}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为过覆盖LTE锚点小区
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
						<div class="panel-title">5G过覆盖定义</div>
					</div>
				</div>
				<div class="inputDivShow">
					<input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForOverCoverageNRcsi" name="baseSsbOrCsiAnalysisForOverCoverageNR" <c:if test="${EmbbOverCoverAnasis.baseSsbOrCsiAnalysisForOverCoverageNR == 'csi'}">checked="checked"</c:if> value="csi">基于CSI-RSRP进行分析
		            <input type="radio" style="width:10px" id="baseSsbOrCsiAnalysisForOverCoverageNRssb" name="baseSsbOrCsiAnalysisForOverCoverageNR" <c:if test="${EmbbOverCoverAnasis.baseSsbOrCsiAnalysisForOverCoverageNR == 'ssb'}">checked="checked"</c:if> value="ssb">基于SSB-RSRP进行分析
                </div>
	   			<div class="inputDivShow">采样点距离NR辅小区的距离>
		    		<input id="nCellBeamRadiusMultipleForOverCoverageNR" name="nCellBeamRadiusMultipleForOverCoverageNR" style="width:120px;"  value="${EmbbOverCoverAnasis.nCellBeamRadiusMultipleForOverCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100 "/>
		    		<span class="status incorrect"></span>&nbsp;5G小区覆盖半径
		    	</div>
		    	<div class="inputDivShow">采样点最小的Beam RSRP>=
		    		<input id="nCellMinBeamRsrpForOverCoverageNR" name="nCellMinBeamRsrpForOverCoverageNR" style="width:120px;" value="${EmbbOverCoverAnasis.nCellMinBeamRsrpForOverCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:-140,max:-40 " />
		    		dBm
		    	</div>
		    	<div class="inputDivShow">
               		<input type="checkbox" style="width:10px" id="whetherSameConsiderLteCoverForOverCoverageNR"   name="whetherSameConsiderLteCoverForOverCoverageNR"  <c:if test="${EmbbOverCoverAnasis.whetherSameConsiderLteCoverForOverCoverageNR == 1}">checked="checked"</c:if> value="1" >是否同时考虑LTE覆盖情况
                	<input type="radio" style="width:10px" id="andOrRelationshipForOverCoverageNRand" name="andOrRelationshipForOverCoverageNR" <c:if test="${EmbbOverCoverAnasis.andOrRelationshipForOverCoverageNR == 'And'}">checked="checked"</c:if> value="And">且
		            <input type="radio" style="width:10px" id="andOrRelationshipForOverCoverageNRor" name="andOrRelationshipForOverCoverageNR" <c:if test="${EmbbOverCoverAnasis.andOrRelationshipForOverCoverageNR == 'Or'}">checked="checked"</c:if> value="Or">或
                </div>
		    </div>
	    	<div style="width:100%;height:50%;">
			    <div style="height:16px;background-color:#e8f1ff;padding:5px;"><div class="panel-title" style="font-size: 11px">小区判定条件</div></div>
	    			<div class="inputDivShow" >NR过覆盖采样点占比>=
			    		<input id="nCellsamplingPointRatioForOverCoverageNR"  name="nCellsamplingPointRatioForOverCoverageNR" style="width:120px;"  value="${EmbbOverCoverAnasis.nCellsamplingPointRatioForOverCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:100" />
			    		%，采样点数>=
			    		<input id="nCellSamplingPointNumberForOverCoverageNR" name="nCellSamplingPointNumberForOverCoverageNR" style="width:120px;"  value="${EmbbOverCoverAnasis.nCellSamplingPointNumberForOverCoverageNR}" class="easyui-numberbox" data-options="required:true,validType:'length[1,24]',min:1,max:10000"  />
			    		个，判定为过覆盖NR辅小区
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
